package com.example.goodweather.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.goodweather.R;
import com.example.goodweather.adapter.SearchCityAdapter;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.goodweather.contract.SearchCityContract;
import com.example.goodweather.eventbus.SearchCityEvent;
import com.example.goodweather.utils.CodeToStringUtils;
import com.example.goodweather.utils.Constant;
import com.example.goodweather.utils.SPUtils;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.goodweather.utils.ToastUtils;
import com.example.mvplibrary.flowlayout.FlowLayout;
import com.example.mvplibrary.flowlayout.RecordsDao;
import com.example.mvplibrary.flowlayout.TagAdapter;
import com.example.mvplibrary.flowlayout.TagFlowLayout;
import com.example.mvplibrary.mvp.MvpActivity;
import com.example.mvplibrary.utils.SizeUtils;
import com.example.mvplibrary.view.dialog.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SearchCityActivity extends MvpActivity<SearchCityContract.SearchCityPresenter> implements SearchCityContract.ISearchCityView {

    @BindView(R.id.edit_query)
    AutoCompleteTextView editQuery;
    @BindView(R.id.iv_clear_search)
    ImageView ivClearSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.clear_all_records)
    ImageView clearAllRecords;//清理所有历史记录
    @BindView(R.id.fl_search_records)
    TagFlowLayout flSearchRecords;//搜索历史布局
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;//超过三行就会出现，展开显示更多
    @BindView(R.id.ll_history_content)
    LinearLayout llHistoryContent;//搜索历史主布局
    List<NewSearchCityResponse.LocationBean> mList = new ArrayList<>();//数据源
    SearchCityAdapter mAdapter;//适配器

    private RecordsDao mRecordsDao;
    //默然展示词条个数
    private final int DEFAULT_RECORD_NUMBER = 10;
    private List<String> recordList = new ArrayList<>();
    private TagAdapter mRecordsAdapter;
    private LinearLayout mHistoryContent;
    /**
     * 提示弹窗
     */
    private AlertDialog tipDialog = null;

    /**
     * 显示提示弹窗
     * @param data 数据
     * @param content 内容
     */
    private void showTipDialog(Object data, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .addDefaultAnimation()
                .setCancelable(true)
                .setContentView(R.layout.dialog_tip)
                .setWidthAndHeight(SizeUtils.dp2px(context, 270), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setText(R.id.tv_content, content)
                .setOnClickListener(R.id.tv_cancel, v -> {
                    tipDialog.dismiss();
                }).setOnClickListener(R.id.tv_sure, v -> {
                    //传入all则删除所有
                    if (Constant.ALL_RECORD.equals(data)) {
                        flSearchRecords.setLimit(true);
                        //清除所有数据
                        mRecordsDao.deleteUsernameAllRecords();
                        llHistoryContent.setVisibility(View.GONE);
                    } else {
                        //删除某一条记录  传入单个的position
                        mRecordsDao.deleteRecord(recordList.get((Integer) data));
                        initTagFlowLayout();
                    }

                    tipDialog.dismiss();
                });
        tipDialog = builder.create();
        tipDialog.show();
    }



    //点击事件
    @OnClick({R.id.iv_clear_search,R.id.clear_all_records, R.id.iv_arrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clear_search://清空输入的内容
                ivClearSearch.setVisibility(View.GONE);
                editQuery.setText("");
                break;
            case R.id.clear_all_records://清除所有记录
                showTipDialog("all","确定要删除全部历史记录?");
                break;
            case R.id.iv_arrow://向下展开
                flSearchRecords.setLimit(false);
                mRecordsAdapter.notifyDataChanged();
                break;
        }
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtil.transparencyBar(context);//透明状态栏
        StatusBarUtil.StatusBarLightMode(context);//黑色字体
        Back(toolbar);
        initView();//初始化页面数据
        initAutoComplete("history", editQuery);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_city;
    }

    private void initView() {
        //默认账号
        String username = "007";
        //初始化数据库
        mRecordsDao = new RecordsDao(this, username);

        initTagFlowLayout();

        //创建历史标签适配器
        //为标签设置对应的内容
        mRecordsAdapter = new TagAdapter<String>(recordList) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv_history,
                        flSearchRecords, false);
                //为标签设置对应的内容
                tv.setText(s);
                return tv;
            }
        };
        editQuery.addTextChangedListener(textWatcher);//添加输入监听
        editQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String location = editQuery.getText().toString();
                    if (!TextUtils.isEmpty(location)) {
                        showLoadingDialog();
                        //添加数据
                        mRecordsDao.addRecords(location);
                        mPresent.newSearchCity(location);
                        //数据保存
                        saveHistory("history", editQuery);
                    } else {
                        ToastUtils.showShortToast(context, "请输入搜索关键词");
                    }
                }
                return false;
            }
        });

        flSearchRecords.setAdapter(mRecordsAdapter);
        flSearchRecords.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public void onTagClick(View view, int position, FlowLayout parent) {
                //清空editText之前的数据
                editQuery.setText("");
                //将获取到的字符串传到搜索结果界面,点击后搜索对应条目内容
                editQuery.setText(recordList.get(position));
                editQuery.setSelection(editQuery.length());
            }
        });
        //长按删除某个条目
        flSearchRecords.setOnLongClickListener(new TagFlowLayout.OnLongClickListener() {
            @Override
            public void onLongClick(View view, final int position) {
                showTipDialog(position,"确定要删除该条历史记录?");
            }
        });

        //view加载完成时回调
        flSearchRecords.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOverFlow = flSearchRecords.isOverFlow();
                boolean isLimit = flSearchRecords.isLimit();
                if (isLimit && isOverFlow) {
                    ivArrow.setVisibility(View.VISIBLE);
                } else {
                    ivArrow.setVisibility(View.GONE);
                }
            }
        });

        //初始化搜索返回的数据列表
        mAdapter = new SearchCityAdapter(R.layout.item_search_city_list, mList);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SPUtils.putString(Constant.LOCATION, mList.get(position).getName(), context);
                //发送消息
                EventBus.getDefault().post(new SearchCityEvent(mList.get(position).getName(),
                        mList.get(position).getAdm2()));//adm2代表市

                finish();
            }
        });

    }



    //历史记录布局
    private void initTagFlowLayout() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                emitter.onNext(mRecordsDao.getRecordsByNumber(DEFAULT_RECORD_NUMBER));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> s) throws Exception {
                        recordList.clear();
                        recordList = s;
                        if (null == recordList || recordList.size() == 0) {
                            llHistoryContent.setVisibility(View.GONE);
                        } else {
                            llHistoryContent.setVisibility(View.VISIBLE);
                        }
                        if (mRecordsAdapter != null) {
                            mRecordsAdapter.setData(recordList);
                            mRecordsAdapter.notifyDataChanged();
                        }
                    }
                });
    }


    //输入监听
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {//输入后，显示清除按钮
                ivClearSearch.setVisibility(View.VISIBLE);
            } else {//隐藏按钮
                ivClearSearch.setVisibility(View.GONE);
            }
        }
    };


    //绑定Presenter
    @Override
    protected SearchCityContract.SearchCityPresenter createPresent() {
        return new SearchCityContract.SearchCityPresenter() ;
    }



    /**
     * 搜索城市返回数据  V7
     *
     * @param response
     */
    @Override
    public void getNewSearchCityResult(NewSearchCityResponse response) {
        dismissLoadingDialog();
        if (response.getCode().equals(Constant.SUCCESS_CODE)) {
            mList.clear();
            mList.addAll(response.getLocation());
            mAdapter.notifyDataSetChanged();
            runLayoutAnimation(rv);
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }
    }


    private void runLayoutAnimation(RecyclerView rv) {
    }

    //搜索错误返回
    @Override
    public void getDataFailed() {
        dismissLoadingDialog();//关闭弹窗
        ToastUtils.showShortToast(context, "网络异常");//这里的context是框架中封装好的，等同于this
    }

    /**
     * 使 AutoCompleteTextView在一开始获得焦点时自动提示
     *
     * @param field                保存在sharedPreference中的字段名
     * @param autoCompleteTextView 要操作的AutoCompleteTextView
     */
    private void initAutoComplete(String field, AutoCompleteTextView autoCompleteTextView) {
        SharedPreferences sp = getSharedPreferences("sp_history", 0);
        String etHistory = sp.getString("history", "深圳");//获取缓存
        String[] histories = etHistory.split(",");//通过,号分割成String数组
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_tv_history, histories);

        // 只保留最近的50条的记录
        if (histories.length > 50) {
            String[] newHistories = new String[50];
            System.arraycopy(histories, 0, newHistories, 0, 50);
            adapter = new ArrayAdapter<String>(this, R.layout.item_tv_history, newHistories);
        }
        //AutoCompleteTextView可以直接设置数据适配器，并且在获得焦点的时候弹出，
        //通常是在用户第一次进入页面的时候，点击输入框输入的时候出现，如果每次都出现
        //是会应用用户体验的，这里不推荐这么做
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {//出现历史输入记录
                    view.showDropDown();
                }
            }
        });
    }


    /**
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
     * 每次输入完之后调用此方法保存输入的值到缓存里
     *
     * @param field                保存在sharedPreference中的字段名
     * @param autoCompleteTextView 要操作的AutoCompleteTextView
     */
    private void saveHistory(String field, AutoCompleteTextView autoCompleteTextView) {

        String text = autoCompleteTextView.getText().toString();//输入的值
        SharedPreferences sp = getSharedPreferences("sp_history", 0);
        String tvHistory = sp.getString(field, "深圳");

        if (!tvHistory.contains(text + ",")) {//如果历史缓存中不存在输入的值则

            StringBuilder sb = new StringBuilder(tvHistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();//写入缓存

        }
    }



}

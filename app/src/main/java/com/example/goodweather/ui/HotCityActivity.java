package com.example.goodweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.goodweather.R;
import com.example.goodweather.adapter.HotCityAdapter;
import com.example.goodweather.bean.HotCityBean;
import com.example.goodweather.contract.HotCityContract;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.goodweather.utils.ToastUtils;
import com.example.mvplibrary.mvp.MvpActivity;
import com.example.mvplibrary.utils.LiWindow;
import com.example.mvplibrary.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

import static com.example.mvplibrary.utils.RecyclerViewAnimation.runLayoutAnimation;

public class HotCityActivity extends MvpActivity<HotCityContract.HotCityPresenter> implements HotCityContract.IHotCityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;//标题bar
    @BindView(R.id.rv)
    RecyclerView rv;//列表
    @BindView(R.id.tv_title)
    TextView tvTitle;//标题
    @BindView(R.id.lay_bg)
    LinearLayout layBg;//标题

    private int type = -1;//类型判断
    private LiWindow liWindow;
    private List<HotCityBean.HeWeather6Bean.BasicBean> mList = new ArrayList<>();
    private HotCityAdapter mAdapter;

    /**
     * popupWindow依赖于Activity,并且要等activity所有的生命周期方法执行完成之后才能显示
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    showTypeWindow();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_hot_city;
    }

    @Override
    protected HotCityContract.HotCityPresenter createPresent() {
        return new HotCityContract.HotCityPresenter();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarColor(context, R.color.orange);//白色状态栏
        Back(toolbar);//返回
        handler.sendEmptyMessageDelayed(0,500);

    }



    private void initList(int type) {
        mAdapter = new HotCityAdapter(R.layout.item_hot_city_list, mList,type);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //点击之后的业务逻辑处理
                Intent intent = new Intent(context, HotCityWeatherActivity.class);
                intent.putExtra("location", mList.get(position).getLocation());
                startActivity(intent);
            }
        });
    }

    /**
     * 显示选择类型弹窗
     */
    private void showTypeWindow() {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_hot_type, null);
        TextView tvInland = view.findViewById(R.id.tv_inland);//国内
        TextView tvForeign = view.findViewById(R.id.tv_foreign);//海外
        tvInland.setOnClickListener(v -> {
            type = 0;
            initList(type);
            showLoadingDialog();
            mPresent.hotCity(context, "cn");
            liWindow.closePopupWindow();
        });
        tvForeign.setOnClickListener(v -> {
            type = 1;
            initList(type);
            showLoadingDialog();
            mPresent.hotCity(context, "overseas");
            liWindow.closePopupWindow();
        });

        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 280), SizeUtils.dp2px(context, 120), false);

    }


    @Override
    public void getHotCityResult(Response<HotCityBean> response) {
        dismissLoadingDialog();
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_return_white));//返回箭头颜色
            tvTitle.setTextColor(getResources().getColor(R.color.white));//标题颜色
            if (type == 0) {//标题判断
                tvTitle.setText("国内热门城市");
                StatusBarUtil.setStatusBarColor(context, R.color.blue_one);//蓝色底状态栏
                toolbar.setBackgroundColor(getResources().getColor(R.color.blue_one));//标题  蓝色
                layBg.setBackgroundColor(getResources().getColor(R.color.shallow_blue));//背景 蓝色
            } else {
                tvTitle.setText("海外热门城市");
                StatusBarUtil.setStatusBarColor(context, R.color.orange);//橙色底  状态栏
                toolbar.setBackgroundColor(getResources().getColor(R.color.orange));//标题  橙色
                layBg.setBackgroundColor(getResources().getColor(R.color.shallow_orange));//背景  橙色
            }

            //数据赋值
            if (response.body().getHeWeather6().get(0).getBasic() != null && response.body().getHeWeather6().get(0).getBasic().size() > 0) {
                mList.clear();
                mList.addAll(response.body().getHeWeather6().get(0).getBasic());
                mAdapter.notifyDataSetChanged();
                runLayoutAnimation(rv);//刷新适配器
            } else {
                ToastUtils.showShortToast(context, "数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }


    @Override
    public void getDataFailed() {
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "网络异常");//这里的context是框架中封装好的，等同于this
    }

}
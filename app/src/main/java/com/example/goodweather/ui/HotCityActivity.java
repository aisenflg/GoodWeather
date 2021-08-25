package com.example.goodweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    List<HotCityBean.HeWeather6Bean.BasicBean> mList = new ArrayList<>();
    HotCityAdapter mAdapter;


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
        showLoadingDialog();//加载弹窗
        StatusBarUtil.setStatusBarColor(context, R.color.orange);//白色状态栏
        Back(toolbar);//返回
        initList();//初始化列表数据
    }

    private void initList() {
        mAdapter = new HotCityAdapter(R.layout.item_hot_city_list, mList);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(mAdapter);
        mPresent.hotCity();
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

        @Override
    public void getHotCityResult(Response<HotCityBean> response) {
        dismissLoadingDialog();
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            //数据赋值
            if (response.body().getHeWeather6().get(0).getBasic() != null && response.body().getHeWeather6().get(0).getBasic().size() > 0) {
                mList.clear();
                mList.addAll(response.body().getHeWeather6().get(0).getBasic());
                mAdapter.notifyDataSetChanged();
                runLayoutAnimation(rv);//刷新适配器
            }else {
                ToastUtils.showShortToast(context,"数据为空");
            }
        }else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }


    @Override
    public void getDataFailed() {
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "网络异常");//这里的context是框架中封装好的，等同于this
    }

}
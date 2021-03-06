package com.pinger.vivideo.presenter;

import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.pinger.vivideo.base.RxPresenter;
import com.pinger.vivideo.model.bean.GankHttpResponse;
import com.pinger.vivideo.model.bean.GankItemBean;
import com.pinger.vivideo.model.net.RetrofitHelper;
import com.pinger.vivideo.presenter.contract.WelfareContract;
import com.pinger.vivideo.utils.Preconditions;
import com.pinger.vivideo.utils.RxUtil;
import com.pinger.vivideo.utils.StringUtils;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Description:
 * Creator: yxc
 * date: $date $time
 */

public class WelfarePresenter extends RxPresenter implements WelfareContract.Presenter {
    WelfareContract.View mView;
    public static final int NUM_OF_PAGE = 20;

    private int currentPage = 1;

    public WelfarePresenter(@NonNull WelfareContract.View view) {
        mView = Preconditions.checkNotNull(view);
        mView.setPresenter(this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        Subscription rxSubscription = RetrofitHelper.getGankApis().getGirlList(NUM_OF_PAGE, currentPage)
                .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GankItemBean>>handleGankResult())
                .subscribe(new Action1<List<GankItemBean>>() {
                    @Override
                    public void call(List<GankItemBean> gankItemBeen) {
                        setHeight(gankItemBeen);
                        mView.showContent(gankItemBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscribe(rxSubscription);
    }

    @Override
    public void loadMore() {
        Subscription rxSubscription = RetrofitHelper.getGankApis().getGirlList(NUM_OF_PAGE, ++currentPage)
                .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GankItemBean>>handleGankResult())
                .subscribe(new Action1<List<GankItemBean>>() {
                    @Override
                    public void call(List<GankItemBean> gankItemBeen) {
                        setHeight(gankItemBeen);
                        mView.showMoreContent(gankItemBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载更多数据失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscribe(rxSubscription);
    }

    private void setHeight(List<GankItemBean> list) {
        DisplayMetrics dm = mView.getContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels / 2;//宽度为屏幕宽度一半
        for (GankItemBean gankItemBean : list) {
            gankItemBean.setHeight(width * StringUtils.getRandomNumber(3, 6) / 3);//随机的高度
        }

    }


}

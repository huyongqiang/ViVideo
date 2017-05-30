package com.pinger.vivideo.presenter;

import android.support.annotation.NonNull;

import com.pinger.vivideo.base.RxPresenter;
import com.pinger.vivideo.model.bean.VideoRes;
import com.pinger.vivideo.model.net.RetrofitHelper;
import com.pinger.vivideo.model.net.VideoHttpResponse;
import com.pinger.vivideo.presenter.contract.VideoListContract;
import com.pinger.vivideo.utils.Preconditions;
import com.pinger.vivideo.utils.RxUtil;
import com.pinger.vivideo.utils.StringUtils;

import rx.Subscription;
import rx.functions.Action1;

public class VideoListPresenter extends RxPresenter implements VideoListContract.Presenter {
    @NonNull
    final VideoListContract.View mView;
    int page = 1;
    String catalogId = "";


    public VideoListPresenter(@NonNull VideoListContract.View addTaskView, String catalogId) {
        mView = Preconditions.checkNotNull(addTaskView);
        mView.setPresenter(this);
        this.catalogId = catalogId;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getVideoList(catalogId);
    }

    private void getVideoList(String catalogID) {
        Subscription rxSubscription = RetrofitHelper.getVideoApi().getVideoList(catalogID, page + "")
                .compose(RxUtil.<VideoHttpResponse<VideoRes>>rxSchedulerHelper())
                .compose(RxUtil.<VideoRes>handleResult())
                .subscribe(new Action1<VideoRes>() {
                    @Override
                    public void call(VideoRes res) {
                        if (res != null) {
                            if (mView.isActive()) {
                                if (page == 1) {
                                    mView.showContent(res.list);
                                } else {
                                    mView.showMoreContent(res.list);
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (page > 1) {
                            page--;
                        }
                        mView.refreshFaild(StringUtils.getErrorMsg(throwable.getMessage()));
                    }
                });
        addSubscribe(rxSubscription);
    }

    /**
     * 搜索电影
     *
     * @param searchStr
     */
    private void getSearchVideoList(String searchStr) {
        Subscription rxSubscription = RetrofitHelper.getVideoApi().getVideoListByKeyWord(searchStr, page + "")
                .compose(RxUtil.<VideoHttpResponse<VideoRes>>rxSchedulerHelper())
                .compose(RxUtil.<VideoRes>handleResult())
                .subscribe(new Action1<VideoRes>() {
                    @Override
                    public void call(VideoRes res) {
                        if (res != null) {
                            if (mView.isActive()) {
                                if (page == 1) {
                                    mView.showContent(res.list);
                                } else {
                                    mView.showMoreContent(res.list);
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (page > 1) {
                            page--;
                        }
                        mView.refreshFaild(StringUtils.getErrorMsg(throwable.getMessage()));
                    }
                });
        addSubscribe(rxSubscription);
    }

    @Override
    public void loadMore() {
        page++;
        getVideoList(catalogId);
    }

}

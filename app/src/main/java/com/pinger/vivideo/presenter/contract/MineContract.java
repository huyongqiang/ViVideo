package com.pinger.vivideo.presenter.contract;


import com.pinger.vivideo.base.BasePresenter;
import com.pinger.vivideo.base.BaseView;
import com.pinger.vivideo.model.bean.VideoType;

import java.util.List;

/**
 * Description: CollectionContract
 * Creator: cp
 * date: 2016/9/29 12:19
 */
public interface MineContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showContent(List<VideoType> list);

    }

    interface Presenter extends BasePresenter {
        void getHistoryData();

        void delAllHistory();
    }
}

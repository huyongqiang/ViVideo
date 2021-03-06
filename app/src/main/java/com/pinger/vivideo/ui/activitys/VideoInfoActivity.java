package com.pinger.vivideo.ui.activitys;

import android.os.Bundle;

import com.pinger.vivideo.R;
import com.pinger.vivideo.base.SwipeBackActivity;
import com.pinger.vivideo.model.bean.VideoInfo;
import com.pinger.vivideo.presenter.VideoInfoPresenter;
import com.pinger.vivideo.ui.view.VideoInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class VideoInfoActivity extends SwipeBackActivity {

    VideoInfo videoInfo;
    @BindView(R.id.video_info_view)
    VideoInfoView videoInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);
        unbinder = ButterKnife.bind(this);
        getIntentData();
        mPresenter = new VideoInfoPresenter(videoInfoView, videoInfo);
    }

    private void getIntentData() {
        videoInfo = (VideoInfo) getIntent().getSerializableExtra("videoInfo");
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
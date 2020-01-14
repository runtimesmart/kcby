package com.ctb_open_car.view.fragment.dialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.map.RoadDto;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.presenter.TrafficCheckoutPresenter;
import com.ctb_open_car.utils.DateUtils;
import com.ywl5320.libenum.MuteEnum;
import com.ywl5320.libenum.SampleRateEnum;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.util.RawAssetsUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;
import timber.log.Timber;

public class NaviTrafficDialog extends DialogFragment {

    @BindView(R.id.traffic_reporter)
    TextView mReporter;
    @BindView(R.id.traffic_pub_time)
    TextView mPubTime;

    @BindView(R.id.traffic_type_icon)
    ImageView mTrafficTypeIcon;

    @BindView(R.id.traffic_type_info)
    TextView mTrafficTxt;

    @BindView(R.id.trafic_audio_layout)
    FrameLayout mVoiceLayout;
    @BindView(R.id.play_complete_countdown)
    TextView mCounterTxt;


    @BindView(R.id.traffic_pics)
    RecyclerCoverFlow mBnner;

    private WlMusic wlMusic;


    private RoadDto mRoadData;
    private TrafficCheckoutPresenter presenter = new TrafficCheckoutPresenter(getActivity());

    public NaviTrafficDialog() {
        initAudio();
    }

    /**
     * 配置轮播图
     */
//    private void configAutoBanner() {
//        mAutoBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//        mAutoBanner.setImageLoader(new BannerImageLoader());
//        mAutoBanner.setBannerAnimation(Transformer.Stack);
//
//        mAutoBanner.isAutoPlay(true);
//        mAutoBanner.setDelayTime(3000);
//    }
    private void loadData() {
        if (null == mRoadData) {
            return;
        }
//        configAutoBanner();
        List<String> imgList = new ArrayList<>();
        String audio = "";
        for (ResourceFileDto fileDto : mRoadData.getRoadFiles()) {
            if (0 == fileDto.getResourceType()) {
                imgList.add(fileDto.getResourceUrl());

            } else if (1 == fileDto.getResourceType()) {
                audio = fileDto.getResourceUrl();
            }
        }


//        mAutoBanner.setImages(imgList);
//        mAutoBanner.start();
        if (0 == imgList.size()) {
            mBnner.setVisibility(View.GONE);
        } else {
            mBnner.setGreyItem(true);
            NaviTrafficDialogAdapter adapter = new NaviTrafficDialogAdapter(getContext());
            mBnner.setAdapter(adapter);
            mBnner.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
                @Override
                public void onItemSelected(int position) {
                }
            });
            adapter.setData(imgList);
        }


        mPubTime.setText(DateUtils.convertTimeToFormat(mRoadData.getPublishTime()) + "报告");
        mReporter.setText(mRoadData.getUser().nickName);
        int roadType = mRoadData.getRcType();

        // 1：交警路检、2：违停贴条、3：免费停车、4：交警拍照、5：交通事故、6：严重拥堵
        Drawable drawable = null;
        String s = "";
        int defaultAudio = 0;
        switch (roadType) {
            case 1:
                s = "前方道路有交警路检";
                defaultAudio = R.raw.traffic_check;
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.police_road_inspection);
                break;
            case 2:
                s = "前方道路有违停贴条";
                defaultAudio = R.raw.traffic_ticket;

                drawable = ContextCompat.getDrawable(getContext(), R.drawable.violation_sticker);
                break;
            case 3:
                s = "前方道路有免费停车";
                defaultAudio = R.raw.park_free;

                drawable = ContextCompat.getDrawable(getContext(), R.drawable.free_parking);
                break;
            case 4:
                s = "前方道路有交警拍照";
                defaultAudio = R.raw.traffic_camera;

                drawable = ContextCompat.getDrawable(getContext(), R.drawable.police_taking_pictures);
                break;
            case 5:
                s = "前方道路有交通事故";
                defaultAudio = R.raw.traffic_accident;

                drawable = ContextCompat.getDrawable(getContext(), R.drawable.traffic_accident);
                break;
            case 6:
                s = "前方道路有严重拥堵";
                defaultAudio = R.raw.jack;

                drawable = ContextCompat.getDrawable(getContext(), R.drawable.severe_congestion);
                break;
            default:
                s = "前方道路有交警路检";
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.police_road_inspection);
                break;
        }

        mTrafficTxt.setText(s);
        mTrafficTypeIcon.setImageDrawable(drawable);
        if (TextUtils.isEmpty(audio)) {
            mVoiceLayout.setVisibility(View.INVISIBLE);
            String url = RawAssetsUtil.getRawFilePath(getContext(), defaultAudio, defaultAudio + ".mp3");
            prepareStart(url);


        } else {
            mVoiceLayout.setVisibility(View.VISIBLE);

            prepareStart(audio);
        }
    }

    private void initAudio() {
        wlMusic = WlMusic.getInstance();
        wlMusic.setCallBackPcmData(true);//是否返回音频PCM数据
        wlMusic.setShowPCMDB(true);//是否返回音频分贝大小
        wlMusic.setPlayCircle(false); //设置不间断循环播放音频
        wlMusic.setVolume(100); //设置音量 65%
        wlMusic.setPlaySpeed(1.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
        wlMusic.setPlayPitch(1.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
        wlMusic.setMute(MuteEnum.MUTE_CENTER); //设置立体声（左声道、右声道和立体声）
        wlMusic.setConvertSampleRate(SampleRateEnum.RATE_44100);//设定恒定采样率（null为取消）

        wlMusic.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                wlMusic.start();
            }
        });
        wlMusic.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                Timber.i("播放结束--dimiss");
//                dismiss();
                mCounterTxt.post(new Runnable() {
                    @Override
                    public void run() {
                        new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                mCounterTxt.setText(String.valueOf(millisUntilFinished / 1000));
                            }

                            @Override
                            public void onFinish() {
                                cancel();
                                dismiss();
                            }
                        }.start();
                    }
                });

            }
        });
    }

    public boolean getAudioPlaying() {
        if (null == wlMusic) {
            return false;
        }
        return wlMusic.isPlaying();
    }

    private void prepareStart(String url) {
        Timber.i("待播放url" + url);
        wlMusic.setSource(url);
        wlMusic.prePared();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TrafficDialog);
        mRoadData = (RoadDto) getArguments().getSerializable("traffic");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.navi_traffic_dialog_layout, container, false);
        ButterKnife.bind(this, view);
        loadData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;

        window.setAttributes(windowParams);
    }


    @OnClick(R.id.traffic_incorrect)
    void incorrectClick(View v) {
        wlMusic.stop();
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setId(TrafficCheckoutPresenter.ACTION_TYPE_INCORRECT);
        EventBus.getDefault().post(messageEvent);

        dismiss();
        Toasty.info(getContext(), "感谢反馈").show();
    }

    @OnClick(R.id.traffic_thanks)
    void correctClick(View v) {
        wlMusic.stop();
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setId(TrafficCheckoutPresenter.ACTION_TYPE_CORRECT);
        EventBus.getDefault().post(messageEvent);

//        presenter.correctCheck(mRoadData.getRcId() + "", "1");
        dismiss();
        Toasty.info(getContext(), "感谢反馈").show();
    }


}

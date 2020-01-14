package com.ctb_open_car.view.fragment.dialog;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;
import com.ctb_open_car.bean.roadcondition.RoadConditionBean;
import com.ctb_open_car.eventbus.ReportRoadEvent;
import com.ctb_open_car.utils.AliossUtils;
import com.ctb_open_car.utils.FileSizeUtils;
import com.ctb_open_car.utils.StringUtils;
import com.ctb_open_car.view.activity.dynamic.AblumSelecteActivity;
import com.ctb_open_car.view.adapter.dynamic.ReleaseAlbumAddAdapter;
import com.library.BottomDialog;
import com.library.Item;
import com.library.OnItemClickListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordSoundSizeListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static android.media.MediaRecorder.VideoSource.CAMERA;

/*******  发布路况  ******/
public class RoadConditionVoiceDialogFragment extends DialogFragment {

    @BindView(R.id.image_recyclerviw)
    RecyclerView mRecyclerviw;
    @BindView(R.id.time_tv)
    TextView mTimeTv;
    @BindView(R.id.address_tv)
    TextView mAddressTv;

    @BindView(R.id.dialog_title)
    TextView mDialogTitle;
    @BindView(R.id.dialog_tips)
    TextView mDialogTips;

    @BindView(R.id.bt_voice)
    Button mbTVoice;
    @BindView(R.id.sound_view)
    RelativeLayout mSoundView;

    @BindView(R.id.countdown_bg)
    View mCountdownBg;
    @BindView(R.id.voice_bg)
    View mVoiceBg;
    @BindView(R.id.countdown)
    TextView mCountdown;

    private boolean isActionMove = false;
    private RoadConditionVoiceClickListener mRoadListener;
    private RoadConditionBean mRoadConditionBean;
    private List<ReleaseDynamics> mCommentList = new ArrayList<>();
    private ReleaseAlbumAddAdapter mAdapter;
    private static final int RESULT_CODE_STARTCAMERA = 1002;
    private static final int REQUEST_CODE_CHOOSE = 1001;
    private final RecordManager recordManager = RecordManager.getInstance();
    private String mAudioFilePath = "";
    private float mRawY = 0;
    private boolean isALiSuccess = true;
    private static final int GET_RECODE_AUDIO = 1;
    private static String[] PERMISSION_AUDIO = {
               Manifest.permission.RECORD_AUDIO
    };

    private CountDownTimer mTimer;
//    public static RoadConditionVoiceDialogFragment newInstance(RoadConditionBean roadConditionBean) {
//        RoadConditionVoiceDialogFragment frag = new RoadConditionVoiceDialogFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("roadConditionBean", roadConditionBean);
//        frag.setArguments(args);
//        return frag;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        mRoadConditionBean = (RoadConditionBean)getArguments().getSerializable("roadConditionBean");
        if (AliossUtils.getSingleton().getOss() == null) {
            AliossUtils.getSingleton().getAliStsToken((BaseActivity)getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.map_road_voice_dialog, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        mDialogTitle.setText(mRoadConditionBean.getPositionName());
        mDialogTips.setText(getString(R.string.road_condition_voice_tip, mRoadConditionBean.getPositionName()));
        String timeData = DateFormat.format("MM月dd日 HH:mm", Calendar.getInstance()).toString();
        mTimeTv.setText(timeData);
        mAddressTv.setText(mRoadConditionBean.getAddress());
        mSoundView.setVisibility(View.GONE);
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initView() {
        if (AliossUtils.getSingleton().getOss() == null) {
            AliossUtils.getSingleton().getAliStsToken((RxAppCompatActivity)getActivity());
        }

        ReleaseDynamics releaseDynamics = new ReleaseDynamics();
        releaseDynamics.setType(1);
        mCommentList.add(releaseDynamics);

        mAdapter = new ReleaseAlbumAddAdapter(getActivity(), mCommentList);
        mAdapter.setOnClickListener(new ReleaseAlbumAddAdapter.AdapterClickListener() {
            @Override
            public void onClickListenerDelete(int position) {
                mCommentList.remove(position);
                for (int i = 0; i < mCommentList.size(); i ++) {
                    mCommentList.get(i).setAlbumNum(i);
                }
                if (!TextUtils.isEmpty(mCommentList.get(mCommentList.size() - 1).getAlbumImgUrl())) {
                    ReleaseDynamics releaseDynamics = new ReleaseDynamics();
                    releaseDynamics.setType(1);
                    mCommentList.add((mCommentList.size()), releaseDynamics);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClickListenerSelectAlbum() {
                BottomDialog bottomDialog = new BottomDialog(getActivity());
                bottomDialog.orientation(BottomDialog.VERTICAL)
                        .layout(BottomDialog.CENTER)
                        .inflateMenu(R.menu.menu_album, new OnItemClickListener() {
                            @Override
                            public void click(Item item) {
                                int id = item.getId();
                                switch (id) {
                                    case R.id.photo:
                                        startCame();
                                        bottomDialog.dismiss();
                                        break;
                                    case R.id.album_select:
                                        Intent intent = new Intent(getActivity(), AblumSelecteActivity.class);
                                        List<ReleaseDynamics> commentList = new ArrayList<>();
                                        commentList.addAll(mCommentList);
                                        commentList.remove(commentList.size() - 1);
                                        intent.putExtra("SelectMax", 3);
                                        intent.putParcelableArrayListExtra("select_image", (ArrayList<? extends Parcelable>) commentList);
                                        getActivity().startActivityForResult(intent, REQUEST_CODE_CHOOSE);
                                        bottomDialog.dismiss();
                                        break;
                                    case R.id.cancel:
                                        bottomDialog.dismiss();
                                        break;
                                }
                            }
                        }).show();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerviw.setLayoutManager(layoutManager);
        mRecyclerviw.setAdapter(mAdapter);

        mbTVoice.setOnTouchListener((v, event) -> {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mRawY = event.getRawY();
                    int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSION_AUDIO, GET_RECODE_AUDIO);
                    } else {
                        countDown();
                        startVoice();
                    }

                   break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(mRawY - event.getRawY()) > 100) {
                        isActionMove = true;

                        if (mTimer != null) {
                            mTimer.cancel();
                            mCountdownBg.setVisibility(View.GONE);
                            mVoiceBg.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                   if (!isActionMove) {
                        recordManager.pause();
                        recordManager.stop();
                   } else {
                        isActionMove = false;
                        mSoundView.setVisibility(View.GONE);
                   }
                    break;
            }
            return true;
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
        super.onActivityCreated(savedInstanceState);
    }


    public void setmRoadListener(RoadConditionVoiceClickListener onClick) {
        mRoadListener = onClick;
    }

    @OnClick({R.id.close_image})
    public void onViewClicked() {
         dismiss();
    }

    public void startVoice() {
        mSoundView.setVisibility(View.VISIBLE);
        recordManager.init(CTBApplication.getInstance(), false);
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(16000));
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_8BIT));
        recordManager.changeFormat(RecordConfig.RecordFormat.MP3);
        File sdState = getActivity().getExternalFilesDir(null);
        recordManager.changeRecordDir(sdState.getPath()+"/audio/");
        recordManager.setRecordSoundSizeListener(new RecordSoundSizeListener() {
            @Override
            public void onSoundSize(int soundSize) {

            }
        });

        recordManager.setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                mAudioFilePath = result.getPath();
            }
        });

        recordManager.setRecordStateListener(new RecordStateListener() {
            @Override
            public void onStateChange(RecordHelper.RecordState state) {
                switch (state) {
                    case PAUSE:
                       // ("暂停中");
                        break;
                    case IDLE:
                        // ("空闲中");

                        if (mTimer != null) {
                            mTimer.cancel();
                            mCountdownBg.setVisibility(View.GONE);
                            mVoiceBg.setVisibility(View.VISIBLE);
                        }
                        try {
                            double fileSize = FileSizeUtils.getFileOrFilesSize(mAudioFilePath, FileSizeUtils.SIZETYPE_B);
                            if (fileSize > 500) {
                                MyThread myThread = new MyThread();
                                myThread.start();
                            } else {
                                mSoundView.setVisibility(View.GONE);
                                Toasty.info(getActivity(), "录音时间太短").show();
                            }
                        } catch (Exception e) {
                            mSoundView.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        break;
                    case RECORDING:
                        // ("录音中");
                        break;
                    case STOP:
                        // ("停止");
                        break;
                    case FINISH:
                        // ("录音结束");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onError(String error) {
            }
        });

        recordManager.start();
    }

    public void startCame() {
        //判断是否开户相册权限
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)) {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityForResult(camera, CAMERA);
        }else{
            //提示用户开户权限
            String[] perms = {"android.permission.CAMERA"};
            ActivityCompat.requestPermissions(getActivity(),perms, RESULT_CODE_STARTCAMERA);
        }

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case RESULT_CODE_STARTCAMERA:
                boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted){
                    //授权成功之后，调用系统相机进行拍照操作等
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, CAMERA);
                } else {
                    Toasty.info(getActivity(), "请开启应用拍照权限").show();
                }
                break;

            case GET_RECODE_AUDIO:
                boolean audioAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(audioAccepted){
                    startVoice();
                } else {
                    Toasty.info(getActivity(), "请开启应用录音权限").show();
                }
                break;
        }
    }

    public interface RoadConditionVoiceClickListener {
        void onClickListenerRoadConditionVoice(RoadConditionBean mRoadConditionBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(ReportRoadEvent event) {
        switch (event.getType()){
            case "RoadPictureSelector":
                List<ReleaseDynamics> commentList = event.getObjectList();
                mCommentList.clear();
                mCommentList.addAll(commentList);
                if (mCommentList.size() < 3) {
                    //mCommentList.remove(mCommentList.size() - 1);
                    ReleaseDynamics releaseDynamics = new ReleaseDynamics();
                    releaseDynamics.setType(1);
                    mCommentList.add((mCommentList.size()), releaseDynamics);
                }

                if (mCommentList.size() > 3) {
                    mCommentList.remove(mCommentList.size() - 1);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case "RoadPictureSelectorCamer":
                ReleaseDynamics releaseDynamics = event.getObject();
                releaseDynamics.setAlbumNum(mCommentList.size() -1);
                releaseDynamics.setImageStatus(true);
                mCommentList.add(0, releaseDynamics);
                if (mCommentList.size() > 3) {
                    mCommentList.remove(mCommentList.size() - 1);
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String yestoday = sdf.format(calendar.getTime());

            StringBuffer sb = new StringBuffer();
            for (ReleaseDynamics dynamics : mCommentList) {

                String filePath = dynamics.getAlbumImgUrl();
                if (TextUtils.isEmpty(filePath)) {
                    continue;
                }
                String objectKey = "ctb/opencar/image/road/condition/img/" + yestoday + File.separator + StringUtils.generateUUID() + "." + StringUtils.getFileSuffix(filePath);
                String eTag = AliossUtils.getSingleton().updateImage(objectKey, filePath);
                sb.append(objectKey).append(",");

                if (TextUtils.isEmpty(eTag)) {
                    isALiSuccess = false;
                }
            }

            String objectKeyAudio = "ctb/opencar/audio/road/condition/audio/" + yestoday + File.separator + StringUtils.generateUUID() + "." + StringUtils.getFileSuffix(mAudioFilePath);
            String eTag = AliossUtils.getSingleton().updateImage(objectKeyAudio, mAudioFilePath);
            sb.append(objectKeyAudio);
            String mObjectKey = sb.toString();
            mRoadConditionBean.setFileUrls(mObjectKey);
            if (isALiSuccess && !TextUtils.isEmpty(eTag)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSoundView.setVisibility(View.GONE);
                        mRoadListener.onClickListenerRoadConditionVoice(mRoadConditionBean);
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.info(getActivity(), getString(R.string.release_dynamics_failed)).show();
                    }
                });

            }

        }
    }

    /**
     * 倒计时显示
     */
    private void countDown() {

        mTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int count = (int) (millisUntilFinished / 1000);
                if (count <= 25) {
                    mCountdownBg.setVisibility(View.VISIBLE);
                    mVoiceBg.setVisibility(View.GONE);
                    mCountdown.setText(String.valueOf(count));
                }

            }

            @Override
            public void onFinish() {
                recordManager.pause();
                recordManager.stop();
                mSoundView.setVisibility(View.GONE);
                mCountdownBg.setVisibility(View.GONE);
                mVoiceBg.setVisibility(View.VISIBLE);

            }
        }.start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}

package com.ctb_open_car.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;
import com.ctb_open_car.bean.roadcondition.RoadConditionBean;
import com.ctb_open_car.eventbus.ReportRoadEvent;
import com.ctb_open_car.presenter.ReportRoadConditionPresenter;
import com.ctb_open_car.utils.StringUtils;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.fragment.dialog.ReportRoadConditionDialogFragment;
import com.ctb_open_car.view.fragment.dialog.RoadConditionVoiceDialogFragment;
import com.rxretrofitlibrary.RxRetrofitApp;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.OnClick;

import static android.media.MediaRecorder.VideoSource.CAMERA;


public class BaseMapActivity extends BaseActivity implements
        ReportRoadConditionDialogFragment.RoadConditionClickListener {

    private static final int REQUEST_CODE_CHOOSE = 1001;
    protected ReportRoadConditionPresenter mReportPresenter;
    protected RoadConditionVoiceDialogFragment mVoiceDialog;

    protected String mCurrentPosition;  //当前地址（省，市，区，路，路号）
    protected double mLatitude; //经度
    protected double mLongitude; //维度
    protected RxRetrofitApp sRxInstance;
    protected LatLng mCurrentLoc;
    protected String mCurrentCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReportPresenter = new ReportRoadConditionPresenter(this);
        sRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
    }

    @OnClick(R.id.report_road)
    public void onReportBtnClick() {
        if (sRxInstance.mHeadBean.getUserId() > 0) {
            ReportRoadConditionDialogFragment editNameDialog = new ReportRoadConditionDialogFragment();
            editNameDialog.setmRoadListener(this);
            editNameDialog.show(getSupportFragmentManager(), "ReportRoadDialog");
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("LatLng", mCurrentLoc);
            startActivity(intent);
        }

    }

    public void onClickListenerRoadCondition(String roadNameType, int rcTyhpe) {
        RoadConditionBean roadConditionBean = new RoadConditionBean();
        roadConditionBean.setLatitude(mLatitude);
        roadConditionBean.setLongitude(mLongitude);
        roadConditionBean.setAddress(mCurrentPosition);
        roadConditionBean.setPublishType(1);
        roadConditionBean.setPositionName(roadNameType);
        roadConditionBean.setRcType(rcTyhpe);
        mReportPresenter.reportRoadCondition(roadConditionBean);
    }

    @Override
    public void onClickLongListenerRoadCondition(String roadNameType, int rcTyhpe) {
        RoadConditionBean roadConditionBean = new RoadConditionBean();
        roadConditionBean.setLatitude(mLatitude);
        roadConditionBean.setLongitude(mLongitude);
        roadConditionBean.setAddress(mCurrentPosition);
        roadConditionBean.setPublishType(2);
        roadConditionBean.setPositionName(roadNameType);
        roadConditionBean.setRcType(rcTyhpe);

        mVoiceDialog = new RoadConditionVoiceDialogFragment();
        mVoiceDialog.setmRoadListener(new RoadConditionVoiceDialogFragment.RoadConditionVoiceClickListener() {
            @Override
            public void onClickListenerRoadConditionVoice(RoadConditionBean mRoadConditionBean) {
                mReportPresenter.reportRoadCondition(roadConditionBean);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putSerializable("roadConditionBean", roadConditionBean);
        mVoiceDialog.setArguments(bundle);
        mVoiceDialog.show(getSupportFragmentManager(), "ReportRoadVoidDialog");
    }

    public void dismissVoiceDialog() {
        if (mVoiceDialog != null) {
            mVoiceDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /***** 上报路况，选择图片返回数据处理  Begin ****/
        if (requestCode == REQUEST_CODE_CHOOSE && null != data) {
            Bundle bundle = data.getBundleExtra("bundle");
            List<ReleaseDynamics> commentList = bundle.getParcelableArrayList("select_image");
            ReportRoadEvent messageEvent = new ReportRoadEvent();
            messageEvent.setType("RoadPictureSelector");
            messageEvent.setObjectList(commentList);
            EventBus.getDefault().post(messageEvent);
        }

        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK && null != data) {
            File sdState = getExternalFilesDir(null);//Environment.getExternalStorageState();
            new DateFormat();
            // String name= DateFormat.format("yyyyMMdd", Calendar.getInstance()) + generateUUID() +".png";
            String name = StringUtils.generateUUID() + ".png";
            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            File file = new File(sdState.getPath() + "/feed/");
            file.mkdirs();
            String filename = file.getPath() + File.separator + name;
            try {
                fout = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ReleaseDynamics releaseDynamics = new ReleaseDynamics();
            releaseDynamics.setAlbumImgUrl(filename);

            ReportRoadEvent messageEvent = new ReportRoadEvent();
            messageEvent.setType("RoadPictureSelectorCamer");
            messageEvent.setObject(releaseDynamics);
            EventBus.getDefault().post(messageEvent);
        }
        /***** 上报路况，选择图片返回数据处理  end ****/
    }

    @Override
    public Object getTag() {
        return null;
    }
}

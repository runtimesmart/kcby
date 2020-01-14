package com.ctb_open_car.view.activity.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.activites.ActivitiesBean;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.PublishActivityApi;
import com.ctb_open_car.engine.net.api.PublishDetailApi;
import com.ctb_open_car.utils.AliossUtils;
import com.ctb_open_car.utils.StringUtils;
import com.ctb_open_car.view.activity.dynamic.AblumSelecteActivity;
import com.ctb_open_car.view.adapter.dynamic.ReleaseAlbumAddAdapter;
import com.ctb_open_car.view.dialog.NearbLocationDialog;
import com.google.gson.JsonObject;
import com.library.BottomDialog;
import com.library.Item;
import com.library.OnItemClickListener;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static android.media.MediaRecorder.VideoSource.CAMERA;

/******* 发布活动页面*****/
public class PushActivitiesActivity extends RxAppCompatActivity implements ReleaseAlbumAddAdapter.AdapterClickListener {
    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.release)
    TextView mReleaseTV;
    @BindView(R.id.release_content)
    EditText mReleaseContent;
    @BindView(R.id.image_recyclerviw)
    RecyclerView mRecyclerviw;
    @BindView(R.id.loading_view)
    View mLoadingView;

    private static final int REQUEST_CODE_CHOOSE = 1001;
    private static final int RESULT_CODE_STARTCAMERA = 1002;
    private List<ReleaseDynamics> mCommentList = new ArrayList<>();
    private ReleaseAlbumAddAdapter mAdapter;
    private String mObjectKey = "";
    private boolean isCaneclPush = false;
    private ActivitiesBean mActivitiesBean;
    private boolean isALiSuccess = true;
    private Dialog mDialog;
    private RxRetrofitApp sRxInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_activities);
        ButterKnife.bind(this);
        sRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AliossUtils.getSingleton().getOss() == null) {
            AliossUtils.getSingleton().getAliStsToken(this);
        }
    }

    private void initData() {
        mActivitiesBean = (ActivitiesBean)getIntent().getSerializableExtra("ActivitiesBean");
        mTitle.setText(getString(R.string.push_activity));
        mLoadingView.setVisibility(View.GONE);
        mAdapter = new ReleaseAlbumAddAdapter(this, mCommentList);
        mAdapter.setOnClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerviw.setLayoutManager(layoutManager);
        mRecyclerviw.setAdapter(mAdapter);

        ReleaseDynamics releaseDynamics = new ReleaseDynamics();
        releaseDynamics.setType(1);
        mCommentList.add(releaseDynamics);

        mReleaseContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCommentList.size() > 1 || mReleaseContent.length() > 0) {
                    mReleaseTV.setBackgroundResource(R.drawable.events_new);
                } else {
                    mReleaseTV.setBackgroundResource(R.drawable.activity_release__rectangle);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String yestoday = sdf.format(calendar.getTime());

            StringBuffer sb = new StringBuffer();
            for (ReleaseDynamics dynamics : mCommentList) {
                if (isCaneclPush) {
                    break;
                }
                String filePath = dynamics.getAlbumImgUrl();
                if (TextUtils.isEmpty(filePath)) {
                    continue;
                }
                String objectKey = "ctb/opencar/image/activity/img/" + yestoday + File.separator + generateUUID() + "." + getFileSuffix(filePath);
                String path = AliossUtils.getSingleton().updateImage(objectKey, filePath);
                sb.append(objectKey).append(",");

                if (TextUtils.isEmpty(path)) {
                    isALiSuccess = false;
                }
            }

            String fileInvite = mActivitiesBean.getActivityInviteIcon();
            String objectKey = "ctb/opencar/image/activity/invite/" + yestoday + File.separator + generateUUID() + "." + getFileSuffix(fileInvite);
            String eTag = AliossUtils.getSingleton().updateImage(objectKey, fileInvite);
            mActivitiesBean.setActivityInviteIcon(objectKey);

            if (isALiSuccess && !TextUtils.isEmpty(eTag)) {
                if (sb.length() > 0) {
                    mObjectKey = sb.deleteCharAt(sb.length() - 1).toString();
                 }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishDetail();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.info(PushActivitiesActivity.this, getString(R.string.release_dynamics_failed)).show();
                    }
                });
            }
        }
    }

    @OnClick({R.id.cancel, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                onBackPressed();
                break;
            case R.id.release:
                    if (!TextUtils.isEmpty(mReleaseContent.getText().toString()) ||
                            mCommentList.size() > 1) {
                        isCaneclPush = false;
                       // mLoadingView.setVisibility(View.VISIBLE);
                        showDialog();
                        new MyThread().start();
                    } else {
                        Toasty.info(this, getString(R.string.publish_detail_tips)).show();
                    }

                break;
        }
    }

    public String getFileSuffix(String pathandname) {
        int start = pathandname.lastIndexOf(".");
        int end = pathandname.length();//pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    private void publishDetail() {
        if (mActivitiesBean == null) {
            onBackPressed();
        }

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("activityTitle", mActivitiesBean.getActivityTitle());
        queryMap.put("activityDeparturePlace", mActivitiesBean.getActivityDeparturePlace());
        queryMap.put("activityInviteIcon", mActivitiesBean.getActivityInviteIcon());
        queryMap.put("activityImages", mObjectKey);
        queryMap.put("activityBegintime", mActivitiesBean.getActivityBegintime());
        queryMap.put("activityEnrollEndtime", mActivitiesBean.getActivityEnrollEndtime());
        queryMap.put("activityEndtime", mActivitiesBean.getActivityEndtime());
        queryMap.put("activityEnrollLimit", mActivitiesBean.getActivityEnrollLimit());
        queryMap.put("activityDesc", mReleaseContent.getText().toString());
        queryMap.put("longitude", mActivitiesBean.getLongitude());
        queryMap.put("latitude", mActivitiesBean.getLatitude());
        queryMap.put("placeName", StringUtils.mCurrentPosition);

        PublishActivityApi publishDetail = new PublishActivityApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                Timber.e("xxxxx object= " + object.toString());
                JsonObject jsonObject = (JsonObject) object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code.replace("\"", ""));
                if (codeIndex == 0) {
                    mLoadingView.setVisibility(View.GONE);
                    mReleaseContent.setText("");
                    mCommentList.clear();
                    mObjectKey="";
                    ReleaseDynamics releaseDynamics = new ReleaseDynamics();
                    releaseDynamics.setType(1);
                    mCommentList.add(releaseDynamics);
                    mAdapter.notifyDataSetChanged();
                    mReleaseTV.setBackgroundResource(R.drawable.activity_release__rectangle);
                    Toasty.info(PushActivitiesActivity.this, getString(R.string.release_dynamics_success)).show();
                    mDialog.dismiss();
                    onBackPressed();
                } else {
                   // mLoadingView.setVisibility(View.GONE);
                    mDialog.dismiss();
                    Toasty.info(PushActivitiesActivity.this, getString(R.string.release_dynamics_failed)).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                mLoadingView.setVisibility(View.GONE);
                Timber.e("mListNewBean " + e.toString());
            }
        }, this);

        publishDetail.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(publishDetail);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isSoftShowing() && isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    mReleaseContent.clearFocus();
                    mReleaseContent.setClickable(false);
                    mReleaseContent.setFocusable(false);
                    mReleaseContent.setFocusableInTouchMode(false);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
            } else  {
                mReleaseContent.setClickable(true);
                mReleaseContent.setFocusable(true);
                mReleaseContent.setFocusableInTouchMode(true);
            }

            if (mLoadingView.getVisibility() == View.VISIBLE && !isShouldHideInput(v, ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //判断软键盘是否正在展示
    private boolean isSoftShowing() {
        int screenHeight = getWindow().getDecorView().getHeight();
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    //是否需要隐藏键盘
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        } else if (v != null && (v instanceof TextView)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

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
        BottomDialog bottomDialog = new BottomDialog(this);
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
                                Intent intent = new Intent(PushActivitiesActivity.this, AblumSelecteActivity.class);
                                List<ReleaseDynamics> commentList = new ArrayList<>();
                                commentList.addAll(mCommentList);
                                commentList.remove(commentList.size() - 1);
                                intent.putParcelableArrayListExtra("select_image", (ArrayList<? extends Parcelable>) commentList);
                                startActivityForResult(intent, REQUEST_CODE_CHOOSE);
                                bottomDialog.dismiss();
                                break;
                            case R.id.cancel:
                                bottomDialog.dismiss();
                                break;
                        }
                    }
                }).show();
    }

    public void startCame() {
        //判断是否开户相册权限
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)) {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA);
        }else{
            //提示用户开户权限
            String[] perms = {"android.permission.CAMERA"};
            ActivityCompat.requestPermissions(PushActivitiesActivity.this,perms, RESULT_CODE_STARTCAMERA);
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
                }else{
                    Toasty.info(PushActivitiesActivity.this, "请开启应用拍照权限").show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && null != data) {
            Bundle bundle = data.getBundleExtra("bundle");
            List<ReleaseDynamics> commentList = bundle.getParcelableArrayList("select_image");
            mCommentList.clear();
            mCommentList.addAll(commentList);
            if (mCommentList.size() < 12) {
                //mCommentList.remove(mCommentList.size() - 1);
                ReleaseDynamics releaseDynamics = new ReleaseDynamics();
                releaseDynamics.setType(1);
                mCommentList.add((mCommentList.size()), releaseDynamics);
            }
            mAdapter.notifyDataSetChanged();
        }

        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK && null != data) {
            File sdState = getExternalFilesDir(null);//Environment.getExternalStorageState();

            new DateFormat();
            // String name= DateFormat.format("yyyyMMdd", Calendar.getInstance()) + generateUUID() +".png";
            String name = generateUUID() + ".png";

            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            File file = new File(sdState.getPath()+ "/feed/");
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
            mCommentList.add(0, releaseDynamics);
            if (mCommentList.size() > 12) {
                mCommentList.remove(mCommentList.size() - 1);
            }
            mAdapter.notifyDataSetChanged();
        }

        if (mCommentList.size() > 1 || mReleaseContent.length() > 0) {
            mReleaseTV.setBackgroundResource(R.drawable.events_new);
        } else {
            mReleaseTV.setBackgroundResource(R.drawable.activity_release__rectangle);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mLoadingView.getVisibility() == View.VISIBLE) {
            Toasty.info(this, getString(R.string.publish_detail_tips)).show();
        } else {
            super.onBackPressed();
        }
    }

    public void showDialog() {
        mDialog = new Dialog(this, R.style.CustomDialog);
        View view = View.inflate(this, R.layout.loading_view, null);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        mDialog.show();
    }

}

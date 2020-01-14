package com.ctb_open_car.view.activity.dynamic;

import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.PublishDetailApi;
import com.ctb_open_car.utils.AliossUtils;
import com.ctb_open_car.utils.StringUtils;
import com.ctb_open_car.view.activity.activities.PushActivitiesActivity;
import com.ctb_open_car.view.activity.community.CommunityActivty;
import com.ctb_open_car.view.adapter.dynamic.ReleaseAlbumAddAdapter;
import com.ctb_open_car.view.dialog.NearbLocationDialog;
import com.google.gson.JsonObject;
import com.library.BottomDialog;
import com.library.Item;
import com.library.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

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

/******* 发布动态页面*****/
public class ReleaseActivity extends RxAppCompatActivity implements ReleaseAlbumAddAdapter.AdapterClickListener {
    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.release)
    TextView mReleaseTV;
    @BindView(R.id.release_content)
    EditText mReleaseContent;
    @BindView(R.id.position_tv)
    TextView mPositionTv;
    @BindView(R.id.position_lay)
    LinearLayout mPositionLay;
    @BindView(R.id.image_recyclerviw)
    RecyclerView mRecyclerviw;
    @BindView(R.id.loading_view)
    View mLoadingView;

    private static final int REQUEST_CODE_CHOOSE = 1001;
    private static final int RESULT_CODE_STARTCAMERA = 1002;
    public static final int REQUEST_CODE_PUSHDYNMIC= 0x1002;
    private List<ReleaseDynamics> mCommentList = new ArrayList<>();
    private ReleaseAlbumAddAdapter mAdapter;
    private String mObjectKey;
    private boolean isCaneclPush = false;
    private boolean isALiSuccess = true;
    private RxRetrofitApp mRxInstance;
    private String mCurrentPosition;

    private ArrayList<String> mLocationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mCurrentPosition = getIntent().getStringExtra("CurrentPosition");
        initData();
        poiSearch(mRxInstance.mHeadBean.getLongitude(), mRxInstance.mHeadBean.getLatitude(), 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AliossUtils.getSingleton().getOss() == null) {
            AliossUtils.getSingleton().getAliStsToken(this);
        }
    }

    private void initData() {
        mPositionTv.setText(mCurrentPosition);
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
                String objectKey = "ctb/opencar/image/feed/img/" + yestoday + File.separator + generateUUID() + "." + getFileSuffix(filePath);
                String path = AliossUtils.getSingleton().updateImage(objectKey, filePath);
                sb.append(objectKey).append(",");

                if (TextUtils.isEmpty(path)) {
                    isALiSuccess = false;
                }
            }

            if (isALiSuccess) {
                if (sb.length() > 0) {
                    mObjectKey = sb.deleteCharAt(sb.length() - 1).toString();
                } else {
                    mObjectKey = "";
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishDetail();
                    }
                });
            } else {
                Toasty.info(ReleaseActivity.this, getString(R.string.release_dynamics_failed)).show();
            }

        }
    }

    @OnClick({R.id.cancel, R.id.release, R.id.position_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (mLoadingView.getVisibility() == View.VISIBLE) {
                    mLoadingView.setVisibility(View.GONE);
                    isCaneclPush = true;
                } else {
                    onBackPressed();
                }
                break;
            case R.id.release:
                if (mLoadingView.getVisibility() != View.VISIBLE) {
                    if (!TextUtils.isEmpty(mReleaseContent.getText().toString()) ||
                            mCommentList.size() > 1) {
                        isCaneclPush = false;
                        mLoadingView.setVisibility(View.VISIBLE);
                        new MyThread().start();
                    } else {
                        Toasty.info(this, getString(R.string.publish_detail_tips)).show();
                    }
                }
                break;
            case R.id.position_lay:
                NearbLocationDialog dialog = NearbLocationDialog.newInstance("",mLocationList);
                dialog.setDialogClickListener(new NearbLocationDialog.DialogClickListener() {
                    @Override
                    public void onClickListenerSelected(String address) {
                        mPositionTv.setText(address);
                    }
                });
                dialog.show(getSupportFragmentManager(), "view");
                break;
        }
    }

    private void poiSearch(double longitude, double latitude, float distances) {
        LatLonPoint point=new LatLonPoint(latitude,longitude);
        GeocodeSearch geocodeSearch=new GeocodeSearch(this);
        RegeocodeQuery regeocodeQuery=new RegeocodeQuery(point,distances,GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                Logger.d(rCode);
                if (1000==rCode){
                    RegeocodeAddress address=regeocodeResult.getRegeocodeAddress();
                    StringBuffer stringBuffer=new StringBuffer();
                    String area = address.getProvince();//省或直辖市
                    String cityName = address.getCity();//地级市或直辖市
                    String subLoc = address.getDistrict();//区或县或县级市
                    List<PoiItem> pois = address.getPois();//获取周围兴趣点
                    for(PoiItem poiItem : pois) {
                        if (!TextUtils.isEmpty(poiItem.getSnippet())) {
                            mLocationList.add(poiItem.getSnippet());
                        }
                   }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
                Logger.d(rCode);
                Logger.d(geocodeResult.getGeocodeAddressList());
                Logger.d(geocodeResult.getGeocodeQuery());
            }
        });

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
        Double longitude = mRxInstance.mHeadBean.getLongitude();
        Double latitude = mRxInstance.mHeadBean.getLatitude();


        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("feedContent", mReleaseContent.getText().toString());
        queryMap.put("feedImgs", mObjectKey);
        queryMap.put("longitude", null == longitude ? "" : longitude +"");
        queryMap.put("latitude", null == latitude ? "" : latitude +"");
        queryMap.put("placeName",mPositionTv.getText().toString());

        PublishDetailApi publishDetail = new PublishDetailApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
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
                    Toasty.info(ReleaseActivity.this, getString(R.string.release_dynamics_success)).show();

                    setResult(REQUEST_CODE_PUSHDYNMIC);
                    finish();
                } else {
                    mLoadingView.setVisibility(View.GONE);
                    Toasty.info(ReleaseActivity.this, getString(R.string.release_dynamics_failed)).show();
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
                                Intent intent = new Intent(ReleaseActivity.this, AblumSelecteActivity.class);
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
            ActivityCompat.requestPermissions(ReleaseActivity.this,perms, RESULT_CODE_STARTCAMERA);
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
                    Toasty.info(ReleaseActivity.this, "请开启应用拍照权限").show();
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
}

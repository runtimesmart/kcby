package com.ctb_open_car.view.activity.dynamic;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;
import com.ctb_open_car.view.adapter.dynamic.AlbumAdapter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/******* 相册页面*****/
public class AblumSelecteActivity extends RxAppCompatActivity implements AlbumAdapter.AdapterClickListener{

    private static final int REQUEST_CODE_CHOOSE = 1001;
    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.release)
    TextView mRelease;
    @BindView(R.id.image_recyclerviw)
    RecyclerView mRecyclerviw;

    private List<ReleaseDynamics> mCommentList = new ArrayList<>();
    private AlbumAdapter mAdapter;
    private ContentResolver contentResolver;

    private List<ReleaseDynamics> mSelectList = new ArrayList<>();
    private List<Integer> mSelectNum = new ArrayList<>();
    private List<Integer> mCancelNum = new ArrayList<>();

    private boolean isSelect = true;
    private int mSelectMax = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ablum_selecte);
        ButterKnife.bind(this);
        initData();
        requestPower();
        getImage();
    }

    private void initData() {
        contentResolver = this.getContentResolver();
        mCommentList = getIntent().getParcelableArrayListExtra("select_image");
        mSelectMax  = getIntent().getIntExtra("SelectMax", 12);
        mSelectList.addAll(mCommentList);

        for (ReleaseDynamics dynamics : mSelectList) {
            mSelectNum.add(dynamics.getAlbumNum(),dynamics.getAlbumNum());
        }
        mAdapter = new AlbumAdapter(this, mCommentList);
        mAdapter.setOnClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);

        mRecyclerviw.setLayoutManager(layoutManager);
        mRecyclerviw.setAdapter(mAdapter);
    }

    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
        }
    }
    private void getImage() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 获得图片
        Cursor mCursor = contentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] { "image/jpeg", "image/png" },MediaStore.Images.Media.DEFAULT_SORT_ORDER);

        while (mCursor.moveToNext()){
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            boolean isExit = false;
            for (ReleaseDynamics reDyna : mCommentList) {
                 if (!TextUtils.isEmpty(reDyna.getAlbumImgUrl()) && reDyna.getAlbumImgUrl().equals(path)) {
                     isExit = true;
                     break;
                 }
            }

            if (!isExit) {
                ReleaseDynamics releaseDynamics = new ReleaseDynamics();
                releaseDynamics.setAlbumImgUrl(path);
                releaseDynamics.setType(0);
                mCommentList.add(releaseDynamics);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.cancel, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                onBackPressed();
                break;
            case R.id.release:
                Intent intentTemp = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("select_image", (ArrayList<? extends Parcelable>) mSelectList);
                intentTemp.putExtra("bundle", bundle);
                setResult(REQUEST_CODE_CHOOSE,intentTemp);
                onBackPressed();
                break;
        }
    }

    @Override
    public void onClickListenerSelected(int position) {
        if (!isSelect) {
            return;
        }
        isSelect = false;
        if (mSelectNum.size() < mSelectMax || mCommentList.get(position).getImageStatus()) {
            if (!mCommentList.get(position).getImageStatus() && mCancelNum.size() == 0) {

                if (mSelectNum.size() == 0) {
                    mCommentList.get(position).setAlbumNum(0);
                    mSelectNum.add(mCommentList.get(position).getAlbumNum(),mCommentList.get(position).getAlbumNum());
                } else {
                    mCommentList.get(position).setAlbumNum(mSelectNum.get(mSelectNum.size()-1) + 1);
                    mSelectNum.add(mCommentList.get(position).getAlbumNum(),mCommentList.get(position).getAlbumNum());
                }

                mSelectList.add(mCommentList.get(position));
            } else if (!mCommentList.get(position).getImageStatus() && mCancelNum.size() > 0) {
                mCommentList.get(position).setAlbumNum(mCancelNum.get(0));
                if (mCommentList.get(position).getAlbumNum() > mSelectNum.size()) {
                    mSelectNum.add(mSelectNum.size(), mCommentList.get(position).getAlbumNum());
                } else {
                    mSelectNum.add(mCommentList.get(position).getAlbumNum(), mCommentList.get(position).getAlbumNum());
                }
                mCancelNum.remove(0);
                mSelectList.add(mCommentList.get(position));
            } else {
                mCancelNum.add(mCommentList.get(position).getAlbumNum());
                mCommentList.get(position).setAlbumNum(0);
                mSelectNum.remove(mCommentList.get(position).getAlbumNum());
                mSelectList.remove(mCommentList.get(position));
            }
            mCommentList.get(position).setImageStatus(!mCommentList.get(position).getImageStatus());
            mAdapter.notifyItemChanged(position);
        } else {
            Toasty.info(this, getString(R.string.select_photo_max_num, mSelectMax)).show();
        }
        isSelect = true;
    }
}

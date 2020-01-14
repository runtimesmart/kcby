package com.net.rxretrofit.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.net.rxretrofit.RxRetrofitApplication;
import com.net.rxretrofit.download.DaoMaster;
import com.net.rxretrofit.download.DaoSession;
import com.net.rxretrofit.download.DownInfo;
import com.net.rxretrofit.download.DownInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件下载的数据库工具类
 */
public class DownloadDbManager {
    private final static String dbName = "wayclouds_download_db";

    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    private static class DownloadDbManagerHolder {
        private static final DownloadDbManager INSTANCE = new DownloadDbManager();
    }

    private DownloadDbManager() {
        context = RxRetrofitApplication.getInstance();
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    public static DownloadDbManager getInstance() {
        return DownloadDbManagerHolder.INSTANCE;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    public void insert(DownInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.insert(info);
    }

    public void update(DownInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.update(info);
    }

    public void update(List<DownInfo> infos) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.updateInTx(infos);
    }

    public DownInfo queryDownBy(Long Id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownInfoDao.Properties.Id.eq(Id));
        List<DownInfo> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public List<DownInfo> queryDownAll(String userId, String did) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownInfoDao.Properties.UserId.eq(userId), DownInfoDao.Properties.DeviceDid.eq(did));
        return qb.list();
    }

    public List<DownInfo> queryDownAllBy(String userId, String did, int state) {
        if (TextUtils.isEmpty(did) || TextUtils.isEmpty(userId)) {
            return new ArrayList<>();
        }
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownInfoDao.Properties.UserId.eq(userId), DownInfoDao.Properties.DeviceDid.eq(did), DownInfoDao.Properties.StateInte.eq(state));
        return qb.list();
    }

    public void deleteDowninfo(DownInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.delete(info);
    }

    /**
     * 批量删除
     *
     * @param downInfos
     */
    public void deleteByDownInfos(List<DownInfo> downInfos) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.deleteInTx(downInfos);
    }

    public void deleteAll(String userId, String did) {
        DownInfoDao downInfoDao = new DaoMaster(getWritableDatabase()).newSession().getDownInfoDao();
        List<DownInfo> list = downInfoDao.queryBuilder().where(DownInfoDao.Properties.UserId.eq(userId), DownInfoDao.Properties.DeviceDid.eq(did)).list();
        if (list != null) {
            downInfoDao.deleteInTx(list);
        }
    }
}

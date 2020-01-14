package com.net.rxretrofit.subscribers;


import com.net.rxretrofit.download.DownInfo;
import com.net.rxretrofit.download.DownLoadListener.DownloadProgressListener;
import com.net.rxretrofit.download.DownState;
import com.net.rxretrofit.download.HttpDownManager;
import com.net.rxretrofit.listener.HttpDownOnNextListener;
import com.net.rxretrofit.utils.DownloadDbManager;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 断点下载处理类Subscriber
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class ProgressDownSubscriber<T> extends Subscriber<T> implements DownloadProgressListener {
    //弱引用结果回调
    private HttpDownOnNextListener mSubscriberOnNextListener;
    /*下载数据*/
    private DownInfo downInfo;


    public ProgressDownSubscriber(DownInfo downInfo) {
        this.mSubscriberOnNextListener =downInfo.getListener();
        this.downInfo = downInfo;
    }


    public void setDownInfo(DownInfo downInfo) {
        this.mSubscriberOnNextListener = downInfo.getListener();
        this.downInfo = downInfo;
    }

    public void updateDownInfo(DownInfo downInfo) {
        this.downInfo = downInfo;
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onStart(downInfo);
        }
        downInfo.setState(DownState.START);
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {

        HttpDownManager.getInstance().remove(downInfo);
        downInfo.setState(DownState.FINISH);
        downInfo.setCreateTime(System.currentTimeMillis() + "");

        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onComplete(downInfo);
        }
        DownloadDbManager.getInstance().update(downInfo);

    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {

        HttpDownManager.getInstance().remove(downInfo);

        downInfo.setState(DownState.ERROR);
        if (mSubscriberOnNextListener!= null) {
            mSubscriberOnNextListener.onError(e, downInfo);
        }
        DownloadDbManager.getInstance().update(downInfo);
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    @Override
    public void update(long read, long count, boolean done) {
        if (downInfo.getCountLength() > count) {
            read = downInfo.getCountLength() - count + read;
        } else {
            downInfo.setCountLength(count);
        }

        downInfo.setReadLength(read);
        DownloadDbManager.getInstance().update(downInfo);
        if (mSubscriberOnNextListener != null) {
            /*接受进度消息，造成UI阻塞，如果不需要显示进度可去掉实现逻辑，减少压力*/
            rx.Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                      /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
                            if (downInfo.getState() == DownState.PAUSE
                                    || downInfo.getState() == DownState.STOP)
                                return;
                            downInfo.setState(DownState.DOWN);
                            mSubscriberOnNextListener.updateProgress(aLong, downInfo.getCountLength(), downInfo);
                        }
                    });
        }
    }

}
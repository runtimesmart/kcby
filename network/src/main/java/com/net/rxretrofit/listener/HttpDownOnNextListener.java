package com.net.rxretrofit.listener;

/**
 * 下载过程中的回调处理
 *
 */
public abstract class HttpDownOnNextListener<T> {
    /**
     * 成功后回调方法
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 开始下载
     */
    public abstract void onStart(T t);

    /**
     * 完成下载
     */
    public abstract void onComplete(T t);

    /**
     * 下载进度
     * @param readLength
     * @param countLength
     */
    public abstract void updateProgress(long readLength, long countLength, T t);

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     * @param e
     */
     public  void onError(Throwable e, T t){

     }

    /**
     * 暂停下载
     */
    public void onPause(T t){

    }

    /**
     * 停止下载销毁
     */
    public void onStop(T t){

    }
}

package com.ctb_open_car.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FileLoggerUtils implements Runnable {
    private static final int INTERVAL = 200;
    private static final int MAX_FAIL_COUNT = 30000 / INTERVAL;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String mLogPath;
    private ArrayList<String> mLogList = new ArrayList<>();
    private static FileLoggerUtils mInstance = new FileLoggerUtils();
    private Thread mThread;
    private boolean mIsRunning;

    public static FileLoggerUtils getInstance() {
        return mInstance;
    }

    public void setLogPath(String logPath) {
        if (logPath != null && !logPath.endsWith(File.separator)) {
            this.mLogPath = logPath + File.separator;
        } else {
            this.mLogPath = logPath;
        }
    }

    private boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public void addLog(String TAG, String message) {
        if (!isSDCardAvailable() || mLogPath == null) {
            return;
        }
        synchronized (this) {
            mLogList.add("[" + getTime() + "][" + TAG + "]  " + message);
            notifyWrite();
        }
    }

    public void addLog(String TAG, Throwable e) {
        if (!isSDCardAvailable() || mLogPath == null) {
            return;
        }
        synchronized (this) {
            addLog(TAG, e.toString());
            StackTraceElement[] elements = e.getStackTrace();
            for (int i = 0; i < elements.length; i++) {
                StackTraceElement element = elements[i];
                mLogList.add("    " + element.toString());
            }
            Throwable cause = e.getCause();
            if (cause != null) {
                mLogList.add("    Caused by: " + cause.toString());
                elements = cause.getStackTrace();
                for (int i = 0; i < elements.length; i++) {
                    StackTraceElement element = elements[i];
                    mLogList.add("    " + element.toString());
                }
            }
            notifyWrite();
        }
    }

    public void addLog(String TAG, String message, Throwable e) {
        if (!isSDCardAvailable() || mLogPath == null) {
            return;
        }
        synchronized (this) {
            addLog(TAG, message);
            addLog(TAG, e);
        }
    }

    private String getLog() {
        synchronized (this) {
            if (mLogList.size() > 0) {
                return mLogList.remove(0);
            }
            return null;
        }
    }

    private void notifyWrite() {
        if (!mIsRunning && isSDCardAvailable()) {
            mIsRunning = true;
            mThread = new Thread(this);
            mThread.start();
        }
    }

    public String getTime() {
        return dateTimeFormat.format(new Date());
    }

    public String getLogFileName() {
        return dateFormat.format(new Date()) + ".txt";
    }

    public void stop() {
        mIsRunning = false;
    }

    public void createDirs(File path) {
        if (path != null && !path.exists()) {
            path.mkdirs();
        }
    }

    @Override
    public void run() {
        FileOutputStream fos = null;
        BufferedWriter writer = null;
        int count = 0;

        while (true) {
            String log = getLog();
            if (!mIsRunning) {
                break;
            }
            if (log == null) {
                if (count > MAX_FAIL_COUNT) {
                    break;
                } else {
                    ++count;
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {

                    }
                    continue;
                }
            }

            if (!isSDCardAvailable() || mLogPath == null) {
                break;
            }
            try {
                String logDir = mLogPath;
                File filePath = new File(logDir);
                if (filePath != null && !filePath.exists()) {
                    filePath.mkdirs();
                }
                File file = new File(logDir + getLogFileName());
                if (!file.exists()) {
                    file.createNewFile();
                }

                if (!file.exists())
                    return;

                fos = new FileOutputStream(file, true);
                writer = new BufferedWriter(new OutputStreamWriter(fos));
                //鍐欐枃浠�
                writer.write(log);
                writer.newLine();

                Thread.sleep(10);
            } catch (Throwable e) {
                e.printStackTrace();
                break;
            } finally {
                if (writer == null)
                    return;

                try {
                    writer.flush();
                    writer.close();
                } catch (Exception e) {

                }

                if (fos == null)
                    return;

                try {
                    fos.flush();
                    fos.close();
                } catch (Exception e) {

                }
            }
        }

        mIsRunning = false;
        mThread = null;
    }
}

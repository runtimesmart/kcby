package com.ctb_open_car.utils;

import android.content.Context;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveReadObjUtil {
    private static <T> void objToSP(Context context, T t, String keyName) throws IOException {
        ByteArrayOutputStream bos;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            String ObjStr = Base64.encodeToString(bytes, Base64.DEFAULT);
            PreferenceUtils.putString(context, keyName, ObjStr);
        } finally {
            if (oos != null) {
                oos.flush();
                oos.close();
            }
        }

    }

    public static <T> void saveObjToSP(Context context, T t, String keyName) throws IOException {
        objToSP(context, t, keyName);
    }

    private static <T> T objFromSp(Context context, String keyName) throws IOException, ClassNotFoundException {
        if (PreferenceUtils.getString(context, keyName) == null || PreferenceUtils.getString(context, keyName).equals("")) {
            return null;
        }

        byte[] bytes = Base64.decode(PreferenceUtils.getString(context, keyName), Base64.DEFAULT);
        ByteArrayInputStream bis;
        ObjectInputStream ois = null;
        T obj = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }

    public static <T> T getObjFromSp(Context context, String keyName) throws IOException, ClassNotFoundException {
        return objFromSp(context, keyName);
    }
}

package com.ctb_open_car.engine.manager;

import com.ctb_open_car.view.adapter.GsonTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GsonManager {
    private static GsonManager gsonManager;
    private static Gson gson;


    private GsonManager() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(), new GsonTypeAdapter())
                    .setLenient()// json宽松
                    .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                    .serializeNulls() //智能null
                    .setPrettyPrinting()// 调教格式
                    .disableHtmlEscaping() //默认是GSON把HTML 转义的
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                    .create();
        }


    }

    public static synchronized GsonManager getInstance() {
        if (gsonManager == null) {
            synchronized (GsonManager.class) {
                if (gsonManager == null) {
                    gsonManager = new GsonManager();
                }
            }

        }
        return gsonManager;
    }

    public Gson getGson() {
        return gson;
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "{}";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    /**
     *
     * 对象转化为json 数据
     * @param object 需要转化的对象
     * @return
     */
    public  String GsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * json 数据转化为实体类对象
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public  <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     *
     * Json 数据转化为List集合--集合中为实体类
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public  <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(gsonString).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;

    }


    /**
     *
     * Json 数据转化为List集合--集合中为实体类
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public  <T> LinkedList<T> GsonToLinkedList(String gsonString, Class<T> cls) {
        LinkedList<T> mList = new LinkedList<T>();
        JsonArray array = new JsonParser().parse(gsonString).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;

    }

    /**
     * 将数据转化成List集合--集合中为map
     *
     * @param gsonString
     * @return
     */
    public  <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 将json数据转化成map
     *
     * @param gsonString
     * @return
     */
    public  <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}

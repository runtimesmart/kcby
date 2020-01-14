package com.ctb_open_car.bean.releasedynamics;

import android.os.Parcel;
import android.os.Parcelable;

public class ReleaseDynamics  implements Parcelable {
    private String albumImgUrl;
    private int albumNum;
    private int type;
    private boolean imageStatus;

    public ReleaseDynamics() {
    }

    public ReleaseDynamics(Parcel in) {
        albumImgUrl = in.readString();
        albumNum = in.readInt();
        type = in.readInt();
        imageStatus = in.readByte() != 0;
    }

    public static final Creator<ReleaseDynamics> CREATOR = new Creator<ReleaseDynamics>() {
        @Override
        public ReleaseDynamics createFromParcel(Parcel in) {
            return new ReleaseDynamics(in);
        }

        @Override
        public ReleaseDynamics[] newArray(int size) {
            return new ReleaseDynamics[size];
        }
    };

    public void setAlbumImgUrl(String albumImgUrl) {
        this.albumImgUrl = albumImgUrl;
    }

    public String getAlbumImgUrl() {
        return albumImgUrl;
    }

    public void setAlbumNum(int albumNum) {
        this.albumNum = albumNum;
    }

    public int getAlbumNum() {
        return albumNum;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setImageStatus(boolean imageStatus) {
        this.imageStatus = imageStatus;
    }

    public boolean getImageStatus() {
        return imageStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumImgUrl);
        dest.writeInt(albumNum);
        dest.writeInt(type);
        dest.writeByte((byte) (imageStatus ? 1 : 0));
    }
}

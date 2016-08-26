package com.greenshadow.thebeginning.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author greenshadow
 */
public class MusicBean implements Parcelable {
    private int _id;
    private String name;
    private String artist;
    private String album;
    private String filePath;
    private String lyric;

    public MusicBean() {
    }

    public void setId(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public static Creator<MusicBean> getCREATOR() {
        return CREATOR;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected MusicBean(Parcel in) {
        _id = in.readInt();
        name = in.readString();
        artist = in.readString();
        album = in.readString();
        filePath = in.readString();
        lyric = in.readString();
    }

    public static final Creator<MusicBean> CREATOR = new Creator<MusicBean>() {
        @Override
        public MusicBean createFromParcel(Parcel in) {
            return new MusicBean(in);
        }

        @Override
        public MusicBean[] newArray(int size) {
            return new MusicBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(filePath);
        dest.writeString(lyric);
    }
}

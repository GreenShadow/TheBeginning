package com.greenshadow.thebeginning.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author greenshadow
 */
public class MusicBean implements Parcelable {
    private String name;
    private String artist;
    private String album;
    private String filePath;
    private String lyric;

    protected MusicBean(Parcel in) {
        name = in.readString();
        artist = in.readString();
        album = in.readString();
        filePath = in.readString();
        lyric = in.readString();
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public static Creator<MusicBean> getCREATOR() {
        return CREATOR;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getLyric() {
        return lyric;
    }

    public String getName() {
        return name;
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
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(filePath);
        dest.writeString(lyric);
    }
}

package model;

import android.os.Parcel;
import android.os.Parcelable;


// TODO : 1) Creating Trailer class with its attributes and implementing Parcelable
public class Trailer implements Parcelable {

    // TODO : 2) Defining trailer ID
    private String id;
    // TODO : 3) Defining trailer name
    private String name;
    // TODO : 4) Defining trailer key
    private String key;
    // TODO : 84 ) Defining trailer type
    private String type;
    // TODO : 109 ) Defining trailer size
    private int size;
    // TODO : 110 ) Defining trailer site
    private String site;

    // TODO : 5) Defining empty constuctor
    public Trailer() {

    }

    // TODO : 6) Defining a constructor with parameters
    public Trailer(String id, String name, String key,String type,int size,String site) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
        this.size = size;
        this.site = site;
    }

    protected Trailer(Parcel in) {
        id = in.readString();
        name = in.readString();
        key = in.readString();
        type = in.readString();
        size = in.readInt();
        site = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(type);
        dest.writeInt(size);
        dest.writeString(site);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    // TODO : 7) Getter and Setters method
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    // TODO : 108 ) Getting video from URL
    public String getURL(){
        return "https://www.youtube.com/watch?v=" + getKey();
    }
}


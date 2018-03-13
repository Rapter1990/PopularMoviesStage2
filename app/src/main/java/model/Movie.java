package model;

import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {


    private String id;
    private String originalTitle;
    private String posterPath;
    private String overview;
    private String rating;
    private String releaseDate;


    public Movie(){

    }

    public Movie(String id, String originalTitle, String posterPath, String overview, String rating, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }


    protected Movie(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUrl(){
        return "http://image.tmdb.org/t/p/w185/"+posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
    }
}

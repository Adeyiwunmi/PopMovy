package com.jp.popularmoviess1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LENIOVO on 5/22/2017.
 */

public class Movie implements Parcelable{
    private String title;
    private String posterUrl;
   private String overView;
    private String userRatiing;
    private String releaseDate;
    private  String id;


    public Movie(String title, String posterUrl, String overView, String userRatiing, String releaseDate , String movieId) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.overView = overView;
        this.userRatiing = userRatiing;
        this.releaseDate = releaseDate;
        this.id = movieId;
    }


    protected Movie(Parcel in) {
        title = in.readString();
        posterUrl = in.readString();
        overView = in.readString();
        userRatiing = in.readString();
        releaseDate = in.readString();
        id = in.readString();
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

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {

        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {

        this.posterUrl = posterUrl;
    }

    public String getOverView() {

        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getUserRatiing() {
        return userRatiing;
    }

    public void setUserRatiing(String userRatiing) {
        this.userRatiing = userRatiing;
    }

    public String getReleaseDate() {

        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(overView);
        dest.writeString(userRatiing);
        dest.writeString(releaseDate);
        dest.writeString(id);
    }


    public String getId() {
        return id;
    }

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }
}

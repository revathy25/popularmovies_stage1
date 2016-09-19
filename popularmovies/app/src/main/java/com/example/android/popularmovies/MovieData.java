package com.example.android.popularmovies;

/**
 * Created by rgunasek on 9/1/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class MovieData implements Parcelable{
    String imageRelativePath;
    String movieName;
    String releaseYear;
    String releaseDate;
    String voteAverage;
    String plotSynopsis;

    public MovieData(String vImageRelativePath, String vMovieName, String vReleaseYear,String vReleaseDate,String vVoteAverage,String vPlotSynopsis )
    {
        this.imageRelativePath = vImageRelativePath;
        this.movieName = vMovieName;
        this.releaseYear = vReleaseYear;
        this.releaseDate = vReleaseDate;
        this.voteAverage = vVoteAverage;
        this.plotSynopsis = vPlotSynopsis;
    }

    private MovieData(Parcel in){
        imageRelativePath = in.readString();
        movieName = in.readString();
        releaseYear = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
      }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getImageRelativePath() {
        return imageRelativePath;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getReleaseYear() { return releaseYear; }

    public String getReleaseDate() { return releaseDate; }

    public String getVoteAverage() { return voteAverage; }

    public String getPlotSynopsis() { return plotSynopsis; }

    @Override
    public String toString() {
        return "MovieData [imageRelativePath=" + imageRelativePath + ", movieName=" + movieName + ", releaseYear=" + releaseYear +", releaseDate="
                + releaseDate + ", voteAverage=" + voteAverage + ", plotSynopsis=" + plotSynopsis + "]";
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageRelativePath);
        parcel.writeString(movieName);
        parcel.writeString(releaseYear);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);
        parcel.writeString(plotSynopsis);
    }

    public static  final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel parcel) {
            return new MovieData(parcel);
        }

        @Override
        public MovieData[] newArray(int i) {
            return new MovieData[i];
        }

    };
}
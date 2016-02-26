package app.com.dkphoenix.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DkPhoenix on 9/21/2015.
 * Object for movie data
 */
public class Movie implements Parcelable{

    private int id; // id
    private String title; // original_title
    private String poster; // poster_path
    private String description; // overview
    private float rating; // vote_average
    private String releaseDate; // release_date
    // Not needed for current project
    private float popularity; // popularity
    private String backdrop; // backdrop_path
    private int ratingCount; // vote_count
    private String genre; //

    public Movie() {}

    /**
     * Movie object to hold values from tmdb
     *
     * @param id
     * @param title original_title
     * @param poster poster_path
     * @param description overview
     * @param rating vote_average
     * @param releaseDate release_date
     * @param popularity popularity
     * @param backdrop backdrop_path
     * @param ratingCount vote_count
     * @param genre genre
     */
    public Movie(int id, String title, String poster, String description,
                 float rating, String releaseDate, float popularity,
                 String backdrop, int ratingCount, String genre) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.backdrop = backdrop;
        this.ratingCount = ratingCount;
        this.genre = genre;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster = in.readString();
        description = in.readString();
        rating = in.readFloat();
        releaseDate = in.readString();
        popularity = in.readFloat();
        backdrop = in.readString();
        ratingCount = in.readInt();
        genre = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(description);
        dest.writeFloat(rating);
        dest.writeString(releaseDate);
        dest.writeFloat(popularity);
        dest.writeString(backdrop);
        dest.writeInt(ratingCount);
        dest.writeString(genre);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String image) {
        this.poster = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }
}

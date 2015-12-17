package app.com.dkphoenix.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DkPhoenix on 9/21/2015.
 * Defines table and column names for the movie database
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "app.com.dkphoenix.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_GENRES = "genres";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        // Movie id as returned by API
        public static final String COLUMN_MOVIE_ID = "movie_id";
        // Title of the movie
        public static final String COLUMN_TITLE = "title";
        // Poster URL
        public static final String COLUMN_POSTER_URL = "poster_url";
        // Movie description
        public static final String COLUMN_DESCRIPTION = "description";
        // Movie Genres
        public static final String COLUMN_GENRES = "genres";
        // Movie Rating stored as a float
        public static final String COLUMN_RATING = "rating";
        // Number of people who have reviewed the movie stored as int
        public static final String COLUMN_RATING_COUNT = "rating_count";
        // Movie release date, stored as a simple string
        public static final String COLUMN_RELEASE_DATE = "release_date";
        // Movie popularity stored as a float, i.e. 4.8 out of 5 stars
        public static final String COLUMN_POPULARITY = "popularity";
        // Background image url for movie
        public static final String COLUMN_BACKGROUND_IMAGE = "background_image";

        public static Uri buildMovieUri (long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieWithId(int movie_id) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(movie_id)).build();
        }

        public static int getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

    /* Inner class that defines the table contents of the genre table */
    public static final class GenreEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRES;

        public static final String TABLE_NAME = "genres";

        // Name of the genre
        public static final String COLUMN_NAME = "name";

        public static Uri buildGenreUri (long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildGenreWithId(int genre_id) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(genre_id)).build();
        }

        public static int getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }
}

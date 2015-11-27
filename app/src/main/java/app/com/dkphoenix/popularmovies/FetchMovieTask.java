package app.com.dkphoenix.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import app.com.dkphoenix.popularmovies.data.MovieContract;

/**
 * Created by DkPhoenix on 9/21/2015.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private final Context mContext;
    private static String mSortBy;

    // TODO: Update API Key
    private final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;


    public FetchMovieTask(Context context, String mSortBy) {
        mContext = context;
        this.mSortBy = mSortBy;
    }

    private void getMovieDataFromJson(String movieJasonStr) {

        // Names of the JSON objects that need to be extracted
        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_TITLE = "original_title";
        final String MDB_POSTER = "poster_path";
        final String MDB_DESCRIPTION = "overview";
        final String MDB_RATING = "vote_average";
        final String MDB_RELEASE_DATE = "release_date";
        // Not needed for project
        final String MDB_POPULARITY = "popularity";
        final String MDB_IMAGE = "backdrop_path";
        final String MDB_RATING_COUNT = "vote_count";

        try {
            JSONObject movieJson = new JSONObject(movieJasonStr);
            JSONArray movieArray = movieJson.getJSONArray(MDB_RESULTS);

            // Insert the new movie information into the database
            Vector<ContentValues> cVector = new Vector<>(movieArray.length());

            // return value that will be filled with movie details
            //String[] resultsStrs = new String[];
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);

                ContentValues movieValues = new ContentValues();

                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getInt(MDB_ID));
                movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getString(MDB_TITLE));
                movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, movie.getString(MDB_POSTER));
                movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, movie.getString(MDB_DESCRIPTION));
                movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, Float.parseFloat(movie.getString(MDB_RATING)));
                movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getString(MDB_RELEASE_DATE));
                movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, Float.parseFloat(movie.getString(MDB_POPULARITY)));
                movieValues.put(MovieContract.MovieEntry.COLUMN_BACKGROUND_IMAGE, movie.getString(MDB_IMAGE));
                movieValues.put(MovieContract.MovieEntry.COLUMN_RATING_COUNT, movie.getInt(MDB_RATING_COUNT));
//                Movie movieObject = new Movie(
//                        movie.getInt(MDB_ID),
//                        movie.getString(MDB_TITLE),
//                        movie.getString(MDB_POSTER),
//                        movie.getString(MDB_DESCRIPTION),
//                        Float.parseFloat(movie.getString(MDB_RATING)),
//                        movie.getString(MDB_RELEASE_DATE),
//                        Float.parseFloat(movie.getString(MDB_POPULARITY)),
//                        movie.getString(MDB_IMAGE),
//                        movie.getInt(MDB_RATING_COUNT)
//                );

                //movieValues.put()
                cVector.add(movieValues);
            }

            int inserted = 0;
            // add to database
            if (cVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVector.size()];
                cVector.toArray(cvArray);
                Uri tUri = MovieContract.MovieEntry.CONTENT_URI;
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchMovieTask Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * Method to get data from MovieDb site
     *
     * @param params Sort Order - popularity, vote_average, revenue
     *               plus .desc or .asc
     */
    @Override
    protected Void doInBackground(String... params) {

        // If there's no sort order specified, default to popularity
        String sortOrder = mSortBy;
        if (params.length > 0) {
            sortOrder = params[0];
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            // Construct URL for MovieDB query
            final String MOIVE_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String APIKEY_PARAM = "api_key";
            final String SORT_PARAM = "sort_by";

            Uri buildUri = Uri.parse(MOIVE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sortOrder)
                    .appendQueryParameter(APIKEY_PARAM, apiKey)
                    .build();

            URL url = new URL(buildUri.toString());

            // Create request to MovieDB and open connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // nothing came back
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // add newlines to make debugging easier
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // stream is empty
                return null;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream ", e);
                }
            }
        }
        return null;
    }
}
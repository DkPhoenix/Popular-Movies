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

/**
 * Created by DkPhoenix on 9/21/2015.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private final Context mContext;

    // TODO: Update API Key
    private final String apiKey = "6768486c0965e874fe2131f1fca3cd83";


    public FetchMovieTask(Context context) {
        mContext = context;
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
                Movie movieObject = new Movie(
                        movie.getInt(MDB_ID),
                        movie.getString(MDB_TITLE),
                        movie.getString(MDB_POSTER),
                        movie.getString(MDB_DESCRIPTION),
                        Float.parseFloat(movie.getString(MDB_RATING)),
                        movie.getString(MDB_RELEASE_DATE),
                        Float.parseFloat(movie.getString(MDB_POPULARITY)),
                        movie.getString(MDB_IMAGE),
                        movie.getInt(MDB_RATING_COUNT)
                );

                ContentValues movieValues = new ContentValues();

                //movieValues.put()
            }


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
        String sortOrder = "popularity.desc";
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
                    .appendQueryParameter(SORT_PARAM, params[0])
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
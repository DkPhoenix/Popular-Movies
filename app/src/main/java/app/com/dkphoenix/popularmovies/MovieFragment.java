package app.com.dkphoenix.popularmovies;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import app.com.dkphoenix.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER = 0;
    private String mSortBy = "popularity.desc";

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_URL,
            MovieContract.MovieEntry.COLUMN_DESCRIPTION,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_RATING_COUNT,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_BACKGROUND_IMAGE
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_mID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_POSTER_URL = 3;
    static final int COL_MOVIE_DESCRIPTION = 4;
    static final int COL_MOVIE_RATING = 5;
    static final int COL_MOVIE_RATING_COUNT = 6;
    static final int COL_MOVIE_RELEASE_DATE = 7;
    static final int COL_MOVIE_POPULARITY = 8;
    static final int COL_MOVIE_BACKGROUND_IMAGE = 9;

    private MovieAdapter mMovieAdapter;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Added to allow fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        if (mSortBy == "popularity.desc") {
            if (!action_sort_by_popularity.isChecked())
                action_sort_by_popularity.setChecked(true);
        } else {
            if (!action_sort_by_rating.isChecked())
                action_sort_by_rating.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
               if (!item.isChecked()) {
                    item.setChecked(true);
                    mSortBy = "popularity.desc";
                    updateMovie();
                }
                return true;
            case R.id.action_sort_by_rating:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mSortBy = "vote_average.desc";
                    updateMovie();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Take data and populate the GridView it's attached to.
        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMovieAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateMovie() {
        //mSortBy
        FetchMovieTask movieTask = new FetchMovieTask(getActivity(), mSortBy);
        movieTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                movieUri,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }
}

package app.com.dkphoenix.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import app.com.dkphoenix.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private ShareActionProvider mShareActionProvider;
    private String mMovie;
    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

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
    private static final int MOVIE_LOADER = 0;

    private ImageView mBackgroundImage;
    private TextView mTitleTextView;
    private TextView mDescriptionView;
    private TextView mDateView;
    private TextView mRating;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mUri = args.getParcelable(MovieDetailActivityFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mBackgroundImage = (ImageView) rootView.findViewById(R.id.detail_background_image);
        mTitleTextView = (TextView) rootView.findViewById(R.id.detail_title);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_description);
        mDateView = (TextView) rootView.findViewById(R.id.detail_release_date);
        mRating = (TextView) rootView.findViewById(R.id.detail_rating);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            String title = data.getString(COL_MOVIE_TITLE);
            mTitleTextView.setText(title);

            String description = data.getString(COL_MOVIE_DESCRIPTION);
            if (description == null)
                mDescriptionView.setText("No description given.");
            else
                mDescriptionView.setText(description);

            String release_date = data.getString(COL_MOVIE_RELEASE_DATE);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(release_date).getTime(),
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                mDateView.setText("Release Date: " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String rating = data.getString(COL_MOVIE_RATING);
            mRating.setText(rating + " / 10");

            String image_url = "http://image.tmdb.org/t/p/w154" +
                    data.getString(COL_MOVIE_POSTER_URL);
            Picasso.with(getActivity()).load(image_url).into(mBackgroundImage);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}

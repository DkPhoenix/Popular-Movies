package app.com.dkphoenix.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by DkPhoenix on 9/21/2015.
 * Much of the code was taken from Udacities Android Custom ArrayAdapter class
 * https://github.com/udacity/android-custom-arrayadapter
 */
public class MovieAdapter extends CursorAdapter {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, Cursor c, int flags) { super(context, c, flags); }

    /**
     *  Cache of the children views for a movie list item
     */
    public static class ViewHolder {
        public final ImageView posterView;
        public final TextView titleView;
        public final TextView genreView;
        public final RatingBar ratingBar;

        public ViewHolder(View view) {
            posterView = (ImageView) view.findViewById(R.id.grid_item_posterImage);
            titleView = (TextView) view.findViewById(R.id.grid_item_title);
            genreView = (TextView) view.findViewById(R.id.grid_item_Genre);
            ratingBar = (RatingBar) view.findViewById(R.id.grid_item_rating);
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_poster, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String image_url = "http://image.tmdb.org/t/p/w185" +
                cursor.getString(MovieFragment.COL_MOVIE_POSTER_URL);
        Picasso.with(context).load(image_url).into(viewHolder.posterView);

        //String title = cursor.getString(MovieFragment.COL_MOVIE_TITLE);
        viewHolder.titleView.setText(cursor.getString(MovieFragment.COL_MOVIE_TITLE));

        viewHolder.genreView.setText(cursor.getString(MovieFragment.COL_MOVIE_GENRES));

        viewHolder.ratingBar.setRating(cursor.getFloat(MovieFragment.COL_MOVIE_RATING)/2);

    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
//        Movie movie = getItem(position);
//
//        // Adapters recycle views to AdapterViews.
//        // If this is a new View object we're getting, then inflate the layout.
//        // If not, this view already has the layout inflated from a previous call to getView,
//        // and we modify the View widgets as usual.
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_poster, parent, false);
//        }
//
//        String image_url = "http://image.tmdb.org/t/p/w185" + movie.getPoster();
//
//        ImageView posterView = (ImageView) convertView.findViewById(R.id.grid_item_posterImage);
//        Picasso.with(mContext).load(image_url).into(posterView);
//
//        TextView titleView = (TextView) convertView.findViewById(R.id.grid_item_title);
//        titleView.setText(movie.getTitle());
//
//        return convertView;
//    }
}

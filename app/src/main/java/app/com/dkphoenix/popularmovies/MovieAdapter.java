package app.com.dkphoenix.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by DkPhoenix on 9/21/2015.
 * Much of the code was taken from Udacities Android Custom ArrayAdapter class
 * https://github.com/udacity/android-custom-arrayadapter
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private static Context mContext;

   public MovieAdapter(Context context) {
       super(context,0);
       mContext = context;
   }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_poster, parent, false);
        }

        String image_url = "http://image.tmdb.org/t/p/w185" + movie.getPoster();

        ImageView posterView = (ImageView) convertView.findViewById(R.id.grid_item_posterImage);
        Picasso.with(mContext).load(image_url).into(posterView);

        TextView titleView = (TextView) convertView.findViewById(R.id.grid_item_title);
        titleView.setText(movie.getTitle());

        return convertView;
    }
}
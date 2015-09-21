package app.com.dkphoenix.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DkPhoenix on 9/21/2015.
 * Much of the code was taken from Udacities Android Custom ArrayAdapter class
 * https://github.com/udacity/android-custom-arrayadapter
 */
public class MoviePosterAdapter extends ArrayAdapter<MoviePoster> {
    private static final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();

    /**
     * Custom constructor
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param moviePosters A List of MovieFlavor objects to display in the grid
     */
    public MoviePosterAdapter(Activity context, List<MoviePoster> moviePosters) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        super(context,0, moviePosters);
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
        MoviePoster moviePoster = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_flavor, parent, false);
//        }
//
//        ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_icon);
//        iconView.setImageResource(androidFlavor.image);
//
//        TextView versionNameView = (TextView) convertView.findViewById(R.id.list_item_version_name);
//        versionNameView.setText(androidFlavor.versionName);
//
//        TextView versionNumberView = (TextView) convertView.findViewById(R.id.list_item_versionnumber_textview);
//        versionNumberView.setText(androidFlavor.versionNumber);
        return convertView;
    }
}

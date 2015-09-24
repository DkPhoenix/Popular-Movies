package app.com.dkphoenix.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by DkPhoenix on 9/23/2015.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final int TEST_MOVIE_ID = 21;

    // content://app.com.dkphoenix.popularmovies/movies
    private static final Uri TEST_MOVIES_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIES_WITH_ID_ITEM =
            MovieContract.MovieEntry.buildMovieWithId(TEST_MOVIE_ID);

    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The MOVIES URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIES_DIR), MovieProvider.MOVIES);
        assertEquals("Error: The MOVIES WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIES_WITH_ID_ITEM), MovieProvider.MOVIE_WITH_ID);
    }
}

package app.com.dkphoenix.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by DkPhoenix on 9/24/2015.
 */
public class TestMovieContract extends AndroidTestCase {
    private static final String TEST_MOVIE_ID = "76341";
    private static final int MOVIE_ID = 76341;

    public void testBuildMovieWithId() {
        Uri movieUri = MovieContract.MovieEntry.buildMovieWithId(MOVIE_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildMovieWithId in " +
                        "MovieContract.", movieUri);
        assertEquals("Error: Movie ID not properly appended to the end of the Uri",
                TEST_MOVIE_ID, movieUri.getLastPathSegment());
        assertEquals("Error: Movie ID Uri doesn't match our expected result",
                movieUri.toString(),
                "content://app.com.dkphoenix.popularmovies/movies/76341");
        //content://app.com.dkphoenix.popularmovies/movies/76341?movie_id=76341
    }
}

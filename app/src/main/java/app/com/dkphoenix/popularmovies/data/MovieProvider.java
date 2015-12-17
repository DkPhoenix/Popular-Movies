package app.com.dkphoenix.popularmovies.data;


import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by DkPhoenix on 9/21/2015.
 */
public class MovieProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIES = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int GENRE = 200;
    static final int GENRE_WITH_ID = 201;

    private static final SQLiteQueryBuilder sGenreByIdQueryBuilder;
    private static final SQLiteQueryBuilder sMovieByIdQueryBuilder;

    static{
        sGenreByIdQueryBuilder = new SQLiteQueryBuilder();
        sGenreByIdQueryBuilder.setTables(MovieContract.GenreEntry.TABLE_NAME);

        sMovieByIdQueryBuilder = new SQLiteQueryBuilder();
        sMovieByIdQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }

    //genre.id = ?
    private static final String sId =
            MovieContract.GenreEntry.TABLE_NAME +
                    "." + MovieContract.GenreEntry._ID + " = ? ";

    private Cursor getGenreById(Uri uri, String[] projection, String sortOrder) {
        int genreId = MovieContract.GenreEntry.getIdFromUri(uri);

        return sGenreByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sId,
                new String[] {Integer.toString(genreId)},
                null,
                null,
                sortOrder
        );
    }

    //movie.id_setting = ?
    private static final String sIdSetting =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";

    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder) {
        int movieId = MovieContract.MovieEntry.getIdFromUri(uri);

        return sMovieByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sIdSetting,
                new String[] {Integer.toString(movieId)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_GENRES, GENRE);
        matcher.addURI(authority, MovieContract.PATH_GENRES + "/#", GENRE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case GENRE:
                return MovieContract.GenreEntry.CONTENT_TYPE;
            case GENRE_WITH_ID:
                return MovieContract.GenreEntry.CONTENT_ITEM_TYPE;
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Switch statement that determines what kind of request and queries the db.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "genre"
            case GENRE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.GenreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case GENRE_WITH_ID:
                retCursor = getGenreById(uri, projection, sortOrder);
                break;
            // "movies"
            case MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_WITH_ID:
                retCursor = getMovieById(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long _id;

        switch (match) {
            case GENRE:
                _id = db.insert(MovieContract.GenreEntry.TABLE_NAME,null,values);
                if (_id>0)
                    returnUri = MovieContract.GenreEntry.buildGenreUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case MOVIES:
                _id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                if (_id>0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case GENRE:
                rowsDeleted = db.delete(
                        MovieContract.GenreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIES:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case GENRE:
                rowsUpdated = db.update(
                        MovieContract.GenreEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIES:
                rowsUpdated = db.update(
                        MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        // TODO: find a better way to only show movies we want
        delete(uri, null, null);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case GENRE:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.GenreEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case MOVIES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(15)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}

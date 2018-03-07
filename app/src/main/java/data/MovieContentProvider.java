package data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


// TODO : 65 ) Getting TABLE_NAME from MoviesEntity Class
import static data.MoviesContract.FavoriteEntry.TABLE_NAME;



// TODO : 55 ) Creating Content Provider for Movie in terms of CRUD operations.
public class MovieContentProvider extends ContentProvider {

    // TODO : 56 ) Creating MOVIE AND MOVIE_WITH_ID variables for URI Matcher
    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;

    // TODO : 57 ) Defining URI Matcher after getting path in contract of movie class
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // TODO : 59 ) Defining MovieDpHelper
    private MoviesDpHelper moviesDbHelper;

    // TODO : 60 ) Creating a MoviesDpHelper object in onCreate method and return true
    @Override
    public boolean onCreate() {
        moviesDbHelper = new MoviesDpHelper(getContext());
        return true;
    }


    // TODO : 61 ) Creating a query method
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // TODO : 62 ) Preparing a database as a readable operation
        final SQLiteDatabase db = moviesDbHelper.getReadableDatabase();

        // TODO : 63 ) Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // TODO : 64 ) Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case MOVIE:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // TODO : 66 ) Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);


        // TODO : 67 ) Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        // TODO : 68 ) Preparing a database as a writable operation
        final SQLiteDatabase db=moviesDbHelper.getWritableDatabase();

        // TODO : 69 ) Write URI match code and set a variable to return a Uri
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        // TODO : 70 ) Insert Query for the tasks directory and write a default case
        switch(match) {
            case MOVIE:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.FavoriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // TODO : 71 ) Set a notification URI if a task was inserted and return that Uri
        getContext().getContentResolver().notifyChange(uri, null);

        // TODO : 72 ) return that Uri
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // TODO : 73 ) Preparing a database as a writable operation
        final SQLiteDatabase db=moviesDbHelper.getWritableDatabase();

        // TODO : 74 ) Write URI match code and set a variable to return a taskDeleted
        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        // TODO : 75 ) Delete Query for the tasks directory and write a default case
        switch (match) {
            case MOVIE :
                tasksDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // TODO : 76 ) Checking whether tasksDeleted has a value or not
        if (tasksDeleted != 0) {

            // TODO : 77 ) Set a notification URI if a task was deleted
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // TODO : 78 ) Returning taskDeleted
        return tasksDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        // TODO : 79 ) Preparing a database as a writable operation
        final SQLiteDatabase db=moviesDbHelper.getWritableDatabase();

        // TODO : 80 ) Write URI match code and set a variable to return a tasksUpdated
        int match = sUriMatcher.match(uri);
        int tasksUpdated;

        // TODO : 81 ) Update Query for the tasks directory and write a case for movie's id
        switch (match) {
            case MOVIE_WITH_ID:
                //update a single task by getting the id
//                String id = uri.getPathSegments().get(1);
                //using selections
                tasksUpdated = moviesDbHelper.getWritableDatabase().update(TABLE_NAME, contentValues,
                        "_id=?", new String[]{selection});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // TODO : 81 ) Checking whether tasksDeleted has a value or not
        if (tasksUpdated != 0) {

            // TODO : 82 ) Set a notification URI if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // TODO : 83 ) Returning number of tasks updated
        return tasksUpdated;
    }


    // TODO : 58 ) Returning uri matcher adding MOVIE and MOVIE_WITH_ID
    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_TASKS, MOVIE);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_TASKS + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

}

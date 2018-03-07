package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// TODO : 48 ) Creating MoviesDpHelper for creating and updating database
public class MoviesDpHelper extends SQLiteOpenHelper {

    // TODO : 48 ) Defining database name and its version
    private static final String DATABASE_NAME       = "movielist.db";
    private static final int    DATABASE_VERSION    = 1;

    // TODO : 49 ) Defining query comments and constraints for SQL Operations
    private static final String CREATE_TABLE = "CREATE TABLE";
    private static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT,";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL,";
    private static final String INTEGER_NOT_NULL = " INTEGER NOT NULL,";
    private static final String TEXT_NOT_NULL_LAST_COLUMN = " TEXT NOT NULL";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    // TODO : 50 ) Defining a constuctor with database name and its version
    public MoviesDpHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // TODO : 51 ) Creating a String variable for SQL Query to create a table in the database.
        final String CREATE_MOVIELIST_TABLE = CREATE_TABLE + MoviesContract.FavoriteEntry.TABLE_NAME + " (" +
                MoviesContract.FavoriteEntry._ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT +
                MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + INTEGER_NOT_NULL +
                MoviesContract.FavoriteEntry.COLUMN_TITLE + TEXT_NOT_NULL +
                MoviesContract.FavoriteEntry.COLUMN_POSTER + TEXT_NOT_NULL +
                MoviesContract.FavoriteEntry.COLUMN_OVERVIEW + TEXT_NOT_NULL +
                MoviesContract.FavoriteEntry.COLUMN_RATING + TEXT_NOT_NULL +
                MoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE + TEXT_NOT_NULL_LAST_COLUMN + " );" ;

        // TODO : 52 ) Creating a table
        sqLiteDatabase.execSQL(CREATE_MOVIELIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO : 53 ) Droping a table
        sqLiteDatabase.execSQL(DROP_TABLE_IF_EXISTS + MoviesContract.FavoriteEntry.TABLE_NAME);

        // TODO : 54 ) Creating a table after droping a table
        onCreate(sqLiteDatabase);
    }
}

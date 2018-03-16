package data;

import android.net.Uri;
import android.provider.BaseColumns;

// TODO : 43 ) Creating MoviesContract for defining each column of movies that will be registered in the database.
public class MoviesContract {

    // TODO : 44 ) Defining authority of project
    public static final String  AUTHORITY = "com.example.android.popularmoviesstage2";

    // TODO : 45 ) Defining content with authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // TODO : 46 ) Defining path of content
    public static final String PATH_TASKS  = "movielist";

    // TODO : 47 ) Defining Favorite class to define movies' attributes as a column
    public static final class FavoriteEntry implements BaseColumns {
        public static final Uri    CONTENT_URI      = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME       = "movie";
        public static final String COLUMN_MOVIE_ID   = "id";
        public static final String COLUMN_TITLE     = "title";
        public static final String COLUMN_POSTER    = "poster";
        public static final String COLUMN_OVERVIEW  = "overview";
        public static final String COLUMN_RATING    = "rating";
        public static final String COLUMN_RELEASE_DATE = "releasedate";

    }
}

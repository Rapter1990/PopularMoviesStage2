package apirequest;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage2.BuildConfig;
import com.example.android.popularmoviesstage2.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;


public class API {

    // TODO : 126 ) Creating LOG TAG -->
    private final static String LOG_TAG = API.class.getSimpleName();

    private static final String MOVIE_URL = "http://api.themoviedb.org/3/movie";

    // TODO : 127 ) Defining related variables about URL -->
    public final static String API_BASE_URL = "http://api.themoviedb.org/3/";
    public final static String API_POPULAR = "popular";
    public final static String API_TOP_RATED = "top_rated";

    private static final String API_KEY = "api_key";

    // TODO : 39 ) Defining videos and review for url
    private final static String TRAILER = "videos";
    private final static String REVIEWS = "reviews";

    // TODO : 40 ) Defining API_KEY name for query parameters
    private final static String API_KEY_NAME = BuildConfig.OPEN_MOVIE_API_KEY;


    public static URL buildUrl(String query) {


        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter(API_KEY, API_KEY_NAME)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    // TODO : 41 ) Creating url for trailers according to movie ID
    public static URL buildURLforTrailer(String filmId) {

        URL url = null;
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(filmId)
                .appendPath(TRAILER)
                .appendQueryParameter(API_KEY, API_KEY_NAME)
                .build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    // TODO : 42 ) Creating url for reviews according to movie ID
    public static URL buildURLforReview(String filmId) {
        URL url = null;
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(filmId)
                .appendPath(REVIEWS)
                .appendQueryParameter(API_KEY, API_KEY_NAME)
                .build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // TODO : 128 ) Defining url according to popular and top-rated -->
    public static String getRequestUrlWithPreference(String sortOrder, Context context) throws IOException {
        URL movieRequestUrl;
        String requestUrlString;
        String prefPopular = context.getResources().getString(R.string.pref_sort_popular_value);
        String prefRatings = context.getResources().getString(R.string.pref_sort_ratings_value);
        if (sortOrder.equals(prefPopular)) {
            movieRequestUrl = buildUrl(API_POPULAR);
            requestUrlString = getResponseFromHttpUrl(movieRequestUrl);
        } else if (sortOrder.equals(prefRatings)) {
            movieRequestUrl = buildUrl(API_TOP_RATED);
            requestUrlString = getResponseFromHttpUrl(movieRequestUrl);
        } else {
            return null;
        }
        return requestUrlString;
    }
}

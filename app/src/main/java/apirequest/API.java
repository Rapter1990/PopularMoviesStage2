package apirequest;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage2.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;


public class API {


    private static final String MOVIE_URL = "http://api.themoviedb.org/3/movie";


    private static final String API_KEY = "api_key";


    public static URL buildUrl(String query) {


        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter(API_KEY, BuildConfig.OPEN_MOVIE_API_KEY)
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
}

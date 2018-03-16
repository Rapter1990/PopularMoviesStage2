package com.example.android.popularmoviesstage2;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import com.example.android.popularmoviesstage2.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import adapter.MovieAdapter;
import apirequest.API;
import data.MoviesContract;
import json.JSONClass;
import model.Movie;

// TODO : 129) Implement OnSharedPreferenceChangeListener on MainActivity
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,MovieAdapter.MovieAdapterOnClickHandler {

    // TODO : 130) Defining LOG TAG
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MovieAdapter mMovieAdapter;

    // TODO : 131) Defining SharedPreference
    private SharedPreferences sharedPreferences;

    // TODO : 133) Defining Loader ids for database and movies
    private static final int NETWORK_MOVIES_LOADER_ID = 1;
    private static final int DATABASE_MOVIES_LOADER_ID = 2;


    // TODO : 134) Add a private static boolean flag for preference updates and initialize it to false(DONE)
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    // TODO : 136) Creating ActivityMainBinding to access each items in activity_main.xml
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO : 137) Setting activity Content
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // TODO : 139) Defining sahredPreference
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);




        // TODO : 140) Defining GridLayoutManager to show design of main activity
        int orientation = GridLayout.VERTICAL;
        int span = getResources().getInteger(R.integer.gridlayout_span);
        boolean reverseLayout = false;
        GridLayoutManager layoutManager = new GridLayoutManager(this, span, orientation, reverseLayout);
        mainBinding.recyclerview.setLayoutManager(layoutManager);
        mainBinding.recyclerview.setHasFixedSize(true);


        mMovieAdapter = new MovieAdapter(this,getApplicationContext());
        mainBinding.recyclerview.setAdapter(mMovieAdapter);

        // TODO : 200) Showing movies from network or from local storage according to the preference of Settings.
        String sortOrderPref = sharedPreferences
                .getString(getString(R.string.pref_sort_key)
                        , getString(R.string.pref_sort_popular_value));
        if (sortOrderPref.equals(getString(R.string.pref_sort_favourites_value)) ||
                !onlineStatus(MainActivity.this)) {
            getLoaderManager().initLoader(DATABASE_MOVIES_LOADER_ID, null, databaseLoaderListener);
        } else {
            getLoaderManager().initLoader(NETWORK_MOVIES_LOADER_ID, null, networkLoaderListener);
        }


        // TODO : 138) Register MainActivity as a OnSharedPreferenceChangedListener in onCreate
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    // TODO : 149) Defining LoaderManager for accessing movies via network
    LoaderManager.LoaderCallbacks<ArrayList<Movie>> networkLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {


        @Override
        public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
            // TODO : 150) returning the list of movie via AsyncTaskLoader
            return new AsyncTaskLoader<ArrayList<Movie>>(MainActivity.this) {

                ArrayList<Movie> moviesList;

                // TODO : 151) Before loading data, showing Loading Indicator
                @Override
                protected void onStartLoading() {
                    if (moviesList != null) {
                        deliverResult(moviesList);
                    } else {
                        mainBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                // TODO : 152) Loading data
                @Override
                public ArrayList<Movie> loadInBackground() {
                    ArrayList <Movie> movieDataList = null;
                    try {

                        String sortOrderPreference = sharedPreferences
                                .getString(getString(R.string.pref_sort_key)
                                        , getString(R.string.pref_sort_popular_value));

                        String movieUrl = API.getRequestUrlWithPreference(sortOrderPreference,
                                getContext());
                        System.out.println(movieUrl);
                        if (onlineStatus(getContext())) {
                            movieDataList = JSONClass
                                    .getMovieStringsFromJson(MainActivity.this, movieUrl);
                        }

                        return movieDataList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }



                // TODO : 153) Delivering data
                @Override
                public void deliverResult(ArrayList<Movie> data) {
                    moviesList = data;
                    super.deliverResult(data);
                }
            };
        }

        // TODO : 154) Using onLoaderFinished to determine whether data is show or an error is thrown
        @Override
        public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
            mainBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieAdapter.setMovieData(data);
            if (data == null) {
                showErrorMessage(getString(R.string.no_data_error));
            } else {
                showMovieDataView();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

        }

    };


    // TODO : 155) Defining LoaderManager for loading favourites from the database.
    LoaderManager.LoaderCallbacks<Cursor> databaseLoaderListener = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

            // TODO : 156) Defining projections for getting variables from database
            String[] projection = new String[]{
                    MoviesContract.FavoriteEntry._ID,
                    MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID,
                    MoviesContract.FavoriteEntry.COLUMN_TITLE,
                    MoviesContract.FavoriteEntry.COLUMN_POSTER,
                    MoviesContract.FavoriteEntry.COLUMN_OVERVIEW,
                    MoviesContract.FavoriteEntry.COLUMN_RATING,
                    MoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE,
            };

            // TODO : 157) Returning variables from database
            return new CursorLoader(MainActivity.this,
                    MoviesContract.FavoriteEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                ArrayList<Movie> favoriteMovies = new ArrayList<Movie>();

                // TODO : 158) Showing cursor
                Log.v(LOG_TAG, "Cursor is: " + DatabaseUtils.dumpCursorToString(cursor));

                // TODO : 159) Moving cursor to first variable
                cursor.moveToFirst();

                // TODO : 160) Getting all variables form table in database and add them into movie object
                while(!cursor.isAfterLast()){

                    String movieId = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID));
                    String title = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_TITLE));
                    String posterLink = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_POSTER));
                    String plot = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_OVERVIEW));
                    String userRating = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_RATING));
                    String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE));

                    Movie movie = new Movie(movieId,title,posterLink,plot,userRating,releaseDate);

                    favoriteMovies.add(movie);

                    cursor.moveToNext();
                }


                for(int i = 0 ; i<favoriteMovies.size();i++){
                    System.out.println(i+".Movie Name : " + favoriteMovies.get(i).getOriginalTitle());
                }


                // TODO : 161) Adding list into adapter
                mMovieAdapter.setMovieData(favoriteMovies);

                // TODO : 162) Checking whether favoriteMovies is null or not
                if(favoriteMovies.size() == 0){
                    showErrorMessage(getString(R.string.no_data_error));
                }else{

                    // TODO : 163) Showing connection error message if there are no network connection and favorite value
                    String sortOrderPreference = sharedPreferences
                            .getString(getString(R.string.pref_sort_key)
                                    , getString(R.string.pref_sort_popular_value));
                    if (!sortOrderPreference.equals(getString(R.string.pref_sort_favourites_value)) ||
                            !onlineStatus(MainActivity.this)) {
                        showErrorMessage(getString(R.string.no_connection_error_favorite));
                    }
                    showMovieDataView();


                }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };



    @Override
    public void onClick(Movie movieData) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent movieDetailActivity = new Intent(context, destinationClass);

        Bundle b = new Bundle();
        b.putParcelable("MOVIE", movieData);
        movieDetailActivity.putExtra("movie", b);
        startActivity(movieDetailActivity);

    }


    // TODO : 147)  In onStart, if preferences have been changed, refresh the data and set the flag to false
    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            PREFERENCES_HAVE_BEEN_UPDATED = false;
            invalidateData();
            String sortOrderPreference = sharedPreferences
                    .getString(getString(R.string.pref_sort_key)
                            , getString(R.string.pref_sort_popular_value));
            if (sortOrderPreference.equals(getString(R.string.pref_sort_favourites_value)) ||
                    !onlineStatus(this)) {
                getLoaderManager()
                        .restartLoader(DATABASE_MOVIES_LOADER_ID, null, databaseLoaderListener);
            } else {
                getLoaderManager()
                        .restartLoader(NETWORK_MOVIES_LOADER_ID, null, networkLoaderListener);
            }
        }else{
            getLoaderManager()
                    .restartLoader(NETWORK_MOVIES_LOADER_ID, null, networkLoaderListener);
        }
    }

    // TODO : 146)  Override onDestroy and unregister MainActivity as a SharedPreferenceChangedListener
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    // TODO : 145) Inflatering menu on the top of part
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    // TODO : 142) Calling settings activity via onOptionsItemSelected method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO : 141) Setting null data in the adapter
    private void invalidateData() {
        mMovieAdapter.setMovieData(null);
    }

    // TODO : 135) Override onSharedPreferenceChanged to set the preferences flag to true(DONE)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    // TODO : 143) Using showingMovieDataView by accessing each attribute in activity.xml via binding
    private void showMovieDataView() {
        mainBinding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mainBinding.recyclerview.setVisibility(View.VISIBLE);
    }

    // TODO : 144) Using showErrorMessage by accessing each attribute in activity.xml via binding
    private void showErrorMessage(String errorMessage) {

        mainBinding.recyclerview.setVisibility(View.INVISIBLE);
        mainBinding.tvErrorMessageDisplay.setText(errorMessage);
        mainBinding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    // TODO : 148)  Checking if internet connection is currently available.
    public static boolean onlineStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


}

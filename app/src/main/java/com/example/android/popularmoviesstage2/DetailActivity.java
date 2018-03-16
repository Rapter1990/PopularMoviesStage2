package com.example.android.popularmoviesstage2;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import adapter.ReviewAdapter;
import adapter.TrailerAdapter;
import apirequest.API;
import data.MoviesContract;
import image.ImageUtilities;
import json.JSONClass;
import model.Movie;
import model.Review;
import model.Trailer;


public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    // TODO : 164) Defining LOG TAG
    private final static String LOG_TAG = DetailActivity.class.getSimpleName();

    // TODO : 165) Creating ActivityDetailActivityBinding to access each items in activity_detail.xml
    private ActivityDetailBinding mBinding;

    // TODO : 166) Defining both trailer and review adapter
    private TrailerAdapter mTrailersAdapter;
    private ReviewAdapter mReviewsAdapter;

    // TODO : 167) Defining Loader ids for trailer and reviews and lastly favorite
    private final int FAVOURITE_CURSOR_LOADER = 3;
    private final int TRAILER_LOADER = 4;
    private final int REVIEWS_LOADER = 5;

    // TODO : 168) Defining Movie object
    private Movie movieData;



    // TODO : 169) Getting favourite data from the database
    private LoaderManager.LoaderCallbacks<Cursor> favoriteMoviesLoaderListener = new LoaderManager.LoaderCallbacks<Cursor>() {

        // TODO : 170) Creating onCreateLoader to show detailed information of movie in terms of its id.
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = new String[]{ MoviesContract.FavoriteEntry._ID };
            String selection = MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(bundle.getInt("id"))};
            return new CursorLoader(DetailActivity.this,
                    MoviesContract.FavoriteEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);
        }

        // TODO : 171) If the favorite button is pressed, set true value
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Log.v(LOG_TAG, "Cursor is: " + DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.getCount() > 0) {
                mBinding.barlayout.setClickable(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    // TODO : 187) Getting trailers from the network
    LoaderManager.LoaderCallbacks<ArrayList<Trailer>> trailerLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {

        // TODO : 188) Getting trailers from the network
        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int i, final Bundle bundle) {
            // TODO : 189) returning the list of trailer via AsyncTaskLoader
            return new AsyncTaskLoader<ArrayList<Trailer>>(DetailActivity.this) {

                ArrayList<Trailer> trailersList;

                @Override
                protected void onStartLoading() {
                    if (trailersList != null) {
                        deliverResult(trailersList);
                    } else {
                        forceLoad();
                    }
                }

                // TODO : 190) Loading data
                @Override
                public ArrayList<Trailer> loadInBackground() {

                    ArrayList<Trailer> trailerDataList = null;

                    String trailerId = String.valueOf(bundle.getString("id"));

                    try {
                        String trailerUrl = API.getResponseFromHttpUrl(API.buildURLforTrailer(trailerId));

                        trailerDataList = JSONClass.getTrailerStringsFromJson(DetailActivity.this,trailerUrl);

                        return trailerDataList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                }

                // TODO : 191) Delivering data
                @Override
                public void deliverResult(ArrayList<Trailer> data) {
                    trailersList = data;
                    super.deliverResult(data);
                }
            };
        }

        // TODO : 192) Using onLoaderFinished to determine whether data is shown or an error is thrown
        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> trailers) {

            mTrailersAdapter.setTrailerData(trailers);
            if(trailers == null){
                notShowTrailers();
            }else{
                showTrailers();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        }
    };


    // TODO : 193) Getting reviews from the network
    LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {

        // TODO : 194) returning the list of trailer via AsyncTaskLoader
        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int i, final Bundle bundle) {
            return new AsyncTaskLoader<ArrayList<Review>>(DetailActivity.this) {


                ArrayList<Review> reviewsList;

                @Override
                protected void onStartLoading() {
                    if (reviewsList != null) {
                        deliverResult(reviewsList);
                    } else {
                        forceLoad();
                    }
                }

                // TODO : 195) Loading data
                @Override
                public ArrayList<Review> loadInBackground() {

                    ArrayList<Review> reviewDataList = null;

                    String trailerId = String.valueOf(bundle.getString("id"));

                    try {
                        String trailerUrl = API.getResponseFromHttpUrl(API.buildURLforReview(trailerId));

                        reviewDataList = JSONClass.getReviewStringsFromJson(DetailActivity.this,trailerUrl);

                        return reviewDataList;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                // TODO : 196) Delivering data
                @Override
                public void deliverResult(ArrayList<Review> data) {
                    reviewsList = data;
                    super.deliverResult(data);
                }
            };
        }

        // TODO : 197) Using onLoaderFinished to determine whether data is shown or an error is thrown
        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> reviews) {

            mReviewsAdapter.setReviewData(reviews);
            if(reviews == null){
                notShowReviews();
            }else{
                showReviews();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {

        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // TODO : 172) Set Favourite by pressing FloatingActionButton to save favourite movie info.
        mBinding.barlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO : 173) Checking whether FloatingActionButton is pressed or not
                if (mBinding.barlayout.isClickable()) {

                    // TODO : 174) Saving the movie's poster as its id to local storage.(
                    String posterUrl = movieData.getUrl();
                    Picasso.with(DetailActivity.this)
                            .load(posterUrl)
                            .into(ImageUtilities.saveImage(getApplicationContext(),movieData.getId()));

                    // TODO : 175) Saving all values to database
                    ContentValues values = new ContentValues();
                    values.put(MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID , movieData.getId());
                    values.put(MoviesContract.FavoriteEntry.COLUMN_TITLE , movieData.getOriginalTitle());
                    values.put(MoviesContract.FavoriteEntry.COLUMN_OVERVIEW, movieData.getOverview());
                    values.put(MoviesContract.FavoriteEntry.COLUMN_RATING, movieData.getRating());
                    values.put(MoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE, movieData.getReleaseDate());

                    getContentResolver().insert(MoviesContract.FavoriteEntry.CONTENT_URI, values);
                    Toast.makeText(DetailActivity.this, getString(R.string.favourite_saved), Toast.LENGTH_SHORT).show();

                }
                else {

                    // TODO : 176) Deleting the image from local storage.(MISSING POSTER ISSUE)
                    ImageUtilities.deleteImage(getApplicationContext(),movieData);

                    // TODO : 177) Deleting the movie from the database.
                    String where = MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?";
                    String[] whereArgs = new String[]{String.valueOf(movieData.getId())};
                    getContentResolver().delete(MoviesContract.FavoriteEntry.CONTENT_URI, where, whereArgs);
                    Toast.makeText(DetailActivity.this, getString(R.string.favourite_deleted), Toast.LENGTH_SHORT).show();

                }
            }
        });

        // TODO : 177) Setting Trailer data in Trailers RecyclerView
        mBinding.trailerReview.recyclerviewTrailers.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.trailerReview.recyclerviewTrailers.setLayoutManager(layoutManager);
        mTrailersAdapter = new TrailerAdapter(this,this);
        mBinding.trailerReview.recyclerviewTrailers.setAdapter(mTrailersAdapter);

        // TODO : 178) Setting Reviews data in Reviews RecyclerView
        mBinding.trailerReview.recyclerviewReviews.setHasFixedSize(true);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.trailerReview.recyclerviewReviews.setLayoutManager(layoutManagerReviews);
        mReviewsAdapter = new ReviewAdapter(this);
        mBinding.trailerReview.recyclerviewReviews.setAdapter(mReviewsAdapter);

        // TODO : 181) Loading favorite ad detailed information of movies
        if (movieData != null) {
            Bundle loaderArgs = new Bundle();
            loaderArgs.putString("id", movieData.getId());
            if (onlineStatus(this)) {
                getLoaderManager()
                        .initLoader(TRAILER_LOADER, loaderArgs, trailerLoaderListener);

                getLoaderManager()
                        .initLoader(REVIEWS_LOADER, loaderArgs, reviewLoaderListener);

                getLoaderManager()
                        .initLoader(FAVOURITE_CURSOR_LOADER, loaderArgs, favoriteMoviesLoaderListener)
                        .forceLoad();
            } else {
                getLoaderManager()
                        .initLoader(FAVOURITE_CURSOR_LOADER, loaderArgs, favoriteMoviesLoaderListener)
                        .forceLoad();
                notShowReviews();
                notShowTrailers();
                displayMovieInformation();
            }
        }

    }



    private void displayMovieInformation() {

        Intent detailInformationActivity = getIntent();

        if (detailInformationActivity != null) {

            if (detailInformationActivity.hasExtra("movie")) {

                Bundle intentBundle = getIntent().getBundleExtra("movie");
                movieData = intentBundle.getParcelable("MOVIE");

                Picasso.with(DetailActivity.this).load(movieData.getUrl()).into(mBinding.expandedImage);
                Picasso.with(DetailActivity.this).load(movieData.getUrl()).into(mBinding.detailmovieposter);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBinding.collapsing.setTitle(movieData.getOriginalTitle());
                }
                mBinding.ratingtextViewInformation.setText(movieData.getRating());
                mBinding.releaseDateTextView.setText(movieData.getReleaseDate());
                mBinding.moviePlotText.setText(movieData.getOverview());

            }
        }

    }

    // TODO : 182) Checking if internet connection is currently available.
    public static boolean onlineStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    // TODO : 183) Not showing trailers because of not having a connection to Internet
    private void notShowTrailers() {
        mBinding.trailerReview.tvTrailersHeading.setVisibility(View.INVISIBLE);
        mBinding.trailerReview.recyclerviewTrailers.setVisibility(View.INVISIBLE);

    }

    // TODO : 184) Not showing reviews because of not having a connection to Internet
    private void notShowReviews() {
        mBinding.trailerReview.tvReviewsHeading.setVisibility(View.INVISIBLE);
        mBinding.trailerReview.recyclerviewReviews.setVisibility(View.INVISIBLE);

    }

    // TODO : 185) Showing trailers because of having a connection to Internet
    private void showTrailers() {
        mBinding.trailerReview.tvTrailersHeading.setVisibility(View.VISIBLE);
        mBinding.trailerReview.recyclerviewTrailers.setVisibility(View.VISIBLE);
    }

    // TODO : 186) Showing reviews because of having a connection to Internet
    private void showReviews() {
        mBinding.trailerReview.tvReviewsHeading.setVisibility(View.VISIBLE);
        mBinding.trailerReview.recyclerviewReviews.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(Trailer trailer) {

        Log.v(LOG_TAG, Uri.parse(trailer.getURL()).toString());
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(trailer.getURL())));
    }
}

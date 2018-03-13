package com.example.android.popularmoviesstage2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import adapter.ReviewAdapter;
import adapter.TrailerAdapter;
import model.Movie;


public class DetailActivity extends AppCompatActivity {

    // TODO : 164) Defining LOG TAG
    private final static String LOG_TAG = DetailActivity.class.getSimpleName();

    // TODO : 165) Creating ActivityMainBinding to access each items in activity_detail.xml
    private ActivityDetailBinding mBinding;

    // TODO : 166) Defining both trailer and review adapter
    private TrailerAdapter mTrailersAdapter;
    private ReviewAdapter mReviewsAdapter;

    // TODO : 167) Defining Loader ids for trailer and reviews and lastly favorite
    private final int TRAILER_REVIEWS_LOADER = 3;
    private final int FAVOURITE_CURSOR_LOADER = 4;

    // TODO : 168) Defining Movie object
    private Movie movieData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayMovieInformation();
    }

    private void displayMovieInformation() {


        Intent detailInformationActivity = getIntent();


        if (detailInformationActivity != null) {


            if (detailInformationActivity.hasExtra("movie")) {


                movieData = (Movie) detailInformationActivity.getSerializableExtra("movie");


                Picasso.with(getApplicationContext()).load(movieData.getUrl()).into(mBinding.expandedImage);
                Picasso.with(getApplicationContext()).load(movieData.getUrl()).into(mBinding.detailmovieposter);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBinding.collapsing.setTitle(movieData.getOriginalTitle());
                }

                mBinding.ratingtextViewInformation.setText(movieData.getRating());
                mBinding.releaseDateTextView.setText(movieData.getReleaseDate());
                mBinding.moviePlotText.setText(movieData.getOverview());

            }

        }

    }
}

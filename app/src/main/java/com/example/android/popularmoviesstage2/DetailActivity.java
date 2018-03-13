package com.example.android.popularmoviesstage2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import model.Movie;


public class DetailActivity extends AppCompatActivity {


    private ImageView expandedImage;
    private CollapsingToolbarLayout imageName;
    private ImageView moviePoster;
    private TextView rating;
    private TextView releaseDate;
    private TextView plot;
    private Movie movieData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        expandedImage = findViewById(R.id.expandedImage);
        imageName = findViewById(R.id.collapsing);
        moviePoster = findViewById(R.id.detail_movie_poster);
        rating = findViewById(R.id.ratingtextViewInformation);
        releaseDate = findViewById(R.id.releaseDateTextView);
        plot = findViewById(R.id.moviePlotText);

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


                Picasso.with(DetailActivity.this).load(movieData.getUrl()).into(expandedImage);
                Picasso.with(DetailActivity.this).load(movieData.getUrl()).into(moviePoster);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageName.setTitle(movieData.getOriginalTitle());
                }

                rating.setText(movieData.getRating());
                releaseDate.setText(movieData.getReleaseDate());
                plot.setText(movieData.getOverview());

            }

        }

    }
}

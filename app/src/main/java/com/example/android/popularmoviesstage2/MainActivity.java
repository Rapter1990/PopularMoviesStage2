package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import adapter.MovieAdapter;
import apirequest.API;
import json.JSONClass;
import model.Movie;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener,MovieAdapter.MovieAdapterOnClickHandler {


    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);



        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


        mMovieAdapter = new MovieAdapter(this,getApplicationContext());
        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        loadMovieData();


    }


    private void loadMovieData() {
        showMovieDataView();


        new FetchMovieTask().execute("popular");
    }


    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage(String errorMessage) {

        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setText(errorMessage);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(Movie movieData) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent movieDetailActivity = new Intent(context, destinationClass);
        movieDetailActivity.putExtra("movie", movieData);
        startActivity(movieDetailActivity);
    }



    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String movie = params[0];
            URL movieRequestUrl = API.buildUrl(movie);

            try {
                String jsonMovieResponse = API
                        .getResponseFromHttpUrl(movieRequestUrl);

                ArrayList <Movie> jsonMovieData = JSONClass
                        .getMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                return jsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);


            // error messages are appeared.
            if(onlineStatus()){
                if (movies != null) {
                    showMovieDataView();
                    mMovieAdapter.setMovieData(movies);
                } else {

                    showErrorMessage(getString(R.string.no_data_error));

                }
            }
            else
            {
                showErrorMessage(getString(R.string.no_connection_error));
            }
        }



        public boolean onlineStatus() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }


    public boolean onMenuItemSelect(MenuItem item) {
        showPopup(findViewById(item.getItemId()));
        return true;
    }


    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        popup.getMenuInflater().inflate(R.menu.sorting, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case R.id.top_rated:
                //Toast.makeText(this,"Top-Rated pressed",Toast.LENGTH_SHORT).show();
                new FetchMovieTask().execute("top_rated");

                break;
            case R.id.popular:
                //Toast.makeText(this,"Popular pressed",Toast.LENGTH_SHORT).show();
                new FetchMovieTask().execute("popular");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

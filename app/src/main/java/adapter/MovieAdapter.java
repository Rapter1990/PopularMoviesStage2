package adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage2.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import model.Movie;



public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {


    private ArrayList<Movie> mMovieData;
    public Context context;


    private final MovieAdapterOnClickHandler mClickHandler;


    public MovieAdapter(MovieAdapterOnClickHandler mClickHandler, Context context) {
        this.mClickHandler = mClickHandler;
        this.context = context;
    }



    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_poster, parent, false);
        return new MovieAdapterViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {

        // TODO 201: Getting image from network or local storage
        String moviePosterLink = mMovieData.get(position).getUrl();

        if(moviePosterLink.contains("imageDir")){
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File myImageFile =
                    new File(directory, String.valueOf(mMovieData.get(position).getId()));
            Picasso.with(context).load(myImageFile).into(movieAdapterViewHolder.thumb);
        }else{
            Picasso.with(context).load(mMovieData.get(position).getUrl()).into(movieAdapterViewHolder.thumb);
        }



    }


    @Override
    public int getItemCount() {
        if(mMovieData == null){
            return 0;
        }
        return mMovieData.size();
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private ImageView thumb;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            thumb=(ImageView)itemView.findViewById(R.id.movie_poster_image);

            itemView.setOnClickListener(this);
        }


        public ImageView getThumb() {
            return thumb;
        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movieDetail = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movieDetail);
        }

    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movieData);
    }

    public void setMovieData(ArrayList<Movie> MovieData) {
        mMovieData = MovieData;
        notifyDataSetChanged();
    }
}

package json;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Movie;
import model.Review;
import model.Trailer;


public final class JSONClass {


    public static ArrayList<Movie> getMovieStringsFromJson(Context context, String movieJsonStr) throws JSONException {


        // id,poster_path,title,overview,vote_count,release_date
        // Because these are unchanged values , they are defined as final
        final String ID="id";
        final String POSTER_PATH="poster_path";
        final String TITLE="title";
        final String OVERVIEW="overview";
        final String VOTE="vote_average";
        final String RELEASE_DATE="release_date";


        final String RESULTS = "results";


        if(movieJsonStr == null){
            return null;
        }else{

            JSONObject movieJson = new JSONObject(movieJsonStr);


            JSONArray movieArray = movieJson.getJSONArray(RESULTS);


            ArrayList<Movie> JSONparsedMovieData = new ArrayList<Movie>();


            JSONparsedMovieData.clear();


            for (int i = 0; i < movieArray.length(); i++) {


                JSONObject movieInformation = movieArray.getJSONObject(i);


                Movie movie = new Movie();
                movie.setId(movieInformation.getString(ID));
                movie.setPosterPath(movieInformation.getString(POSTER_PATH));
                movie.setOriginalTitle(movieInformation.getString(TITLE));
                movie.setOverview(movieInformation.getString(OVERVIEW));
                movie.setRating(movieInformation.getString(VOTE));
                movie.setReleaseDate(movieInformation.getString(RELEASE_DATE));


                JSONparsedMovieData.add(movie);

            }



            return JSONparsedMovieData;
        }

    }

    // TODO : 14) Creating getReviewStringsFromJson to get parsed Review Date
    public static ArrayList<Review> getReviewStringsFromJson(Context context,String reviewJsonStr) throws JSONException{

        // TODO : 15) Defining results attribute
        final String RESULTS = "results";
        // TODO : 16) Defining author of review attribute
        final String AUTHOR = "author";
        // TODO : 17) Defining content of review attribute
        final String CONTENT = "content";

        // TODO : 18) Checking whether string is null or not
        if(reviewJsonStr == null){
            return null;
        }else{

            // TODO : 19) Defining JSON object in terms of reviewJsonStr
            JSONObject reviewJson = new JSONObject(reviewJsonStr);

            // TODO : 20) Getting results from JSON String
            JSONArray reviewArray = reviewJson.getJSONArray(RESULTS);

            // TODO : 21) Defining arraylist with Review Object
            ArrayList<Review> JSONparsedReviewData = new ArrayList<Review>();

            // TODO : 22) Clearing arraylist
            JSONparsedReviewData.clear();

            // TODO : 23) Adding each review attributes in review object and then adding it into arraylist
            for (int i = 0; i < reviewArray.length(); i++) {


                JSONObject reviewInformation = reviewArray.getJSONObject(i);

                Review review = new Review();
                review.setAuthor(reviewInformation.getString(AUTHOR));
                review.setReview(reviewInformation.getString(CONTENT));

                JSONparsedReviewData.add(review);

            }

            // TODO : 24) Returning arraytlist with its review objects with its attributes
            return JSONparsedReviewData;

        }

    }

    // TODO : 25) Creating getTrailerStringsFromJson to get parsed Trailer Date
    public static ArrayList<Trailer> getTrailerStringsFromJson(Context context,String trailerJsonStr) throws JSONException {

        // TODO : 26) Defining results attribute
        final String RESULTS = "results";

        // TODO : 27) Defining an attribute holding the id of the trailer
        final String ID = "id";

        // TODO : 28) Defining an attribute holding key of the trailer accessed from the youtube
        final String KEY = "key";

        // TODO : 29) Defining an attribute holding name of the trailer accessed from the youtube
        final String NAME = "name";

        // TODO : 30) Checking whether string is null or not
        if(trailerJsonStr == null){
            return null;
        }else{

            // TODO : 31) Defining JSON object in terms of reviewJsonStr
            JSONObject trailerJson = new JSONObject(trailerJsonStr);

            // TODO : 32) Getting results from JSON String
            JSONArray trailerArray = trailerJson.getJSONArray(RESULTS);

            // TODO : 33) Defining arraylist with Review Object
            ArrayList<Trailer> JSONparsedTrailerData = new ArrayList<Trailer>();

            // TODO : 34) Clearing arraylist
            JSONparsedTrailerData.clear();

            // TODO : 35) Adding each review attributes in trailer object and then adding it into arraylist
            for (int i = 0; i < trailerArray.length(); i++) {


                JSONObject reviewInformation = trailerArray.getJSONObject(i);

                Trailer trailer = new Trailer();

                trailer.setId(reviewInformation.getString(ID));
                trailer.setKey(reviewInformation.getString(KEY));
                trailer.setName(reviewInformation.getString(NAME));

                JSONparsedTrailerData.add(trailer);

            }

            // TODO : 36) Returning arraytlist with its trailer objects with its attributes
            return JSONparsedTrailerData;

        }


    }



}

package adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;

import java.util.ArrayList;

import model.Review;

// TODO : 99 ) Creating ReviewAdapter to determine review.xml in recyleview
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    // TODO : 100 ) Defining Arraylist for review objects
    private ArrayList<Review> mReviewList;

    // TODO : 101 ) Defining a constructor for ReviewAdapter
    public ReviewAdapter() {}

    // TODO : 103 ) Defining onCreateViewHolder to determine review.xml
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    // TODO : 97 ) Defining onBindViewHolder to determine textviews in trailer.xml
    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        String mAuthor = mReviewList.get(position).getAuthor();
        String mContent = mReviewList.get(position).getReview();

        holder.author.setText(mAuthor);
        holder.content.setText(mContent);
    }

    // TODO : 102 ) Returning the size of review list
    @Override
    public int getItemCount() {
        if(mReviewList == null){
            return 0;
        }
        return mReviewList.size();
    }

    // TODO : 103 ) Creating ReviewViewHolder for defining author and content and determining onClick action
    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        // TODO : 104 ) Defining author and content
        TextView author;
        TextView content;

        // TODO : 105 ) Determining author and content with their ids in review.xml in the constructor.
        public ReviewViewHolder(View itemView) {
            super(itemView);

            author =(TextView)itemView.findViewById(R.id.author_review);
            content =(TextView)itemView.findViewById(R.id.content_review);

        }

        // TODO : 106 ) Getting returns for each TextView
        public TextView getAuthor(){ return author; }
        public TextView getContent(){ return content; }

    }


    // TODO : 107 ) Setting reviewData to mTrailerList and save it
    public void setReviewData(ArrayList<Review> reviewData) {
        mReviewList = reviewData;
        notifyDataSetChanged();
    }


}

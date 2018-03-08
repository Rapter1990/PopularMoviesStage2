package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;

import java.util.ArrayList;
import model.Trailer;


// TODO : 85 ) Creating TrailerAdapter to determine trailer.xml in recyleview
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    // TODO : 86 ) Defining Arraylist for trailer objects
    private ArrayList<Trailer> mTrailerList;

    // TODO : 87 ) Defining TrailerAdapterOnClickHandler for onClick method
    private final TrailerAdapterOnClickHandler mClickHandler;

    // TODO : 88 ) Defining TrailerAdapter Constructor with TrailerAdapterOnClickHandler parameter
    public TrailerAdapter(TrailerAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }


    // TODO : 96 ) Defining onCreateViewHolder to determine trailer.xml
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    // TODO : 97 ) Defining onBindViewHolder to determine textviews in trailer.xml
    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        String nameTrailer = mTrailerList.get(position).getName();
        String typeTrailer = mTrailerList.get(position).getType();

        holder.trailerName.setText(nameTrailer);
        holder.trailerType.setText(typeTrailer);
    }

    // TODO : 95 ) Returning the size of trailer list
    @Override
    public int getItemCount() {
        if(mTrailerList == null){
            return 0;
        }
        return mTrailerList.size();
    }

    // TODO : 89 ) Creating TrailerViewHolder for defining trailer key and type and determining onClick action
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // TODO : 90 ) Defining trailer name and type
        TextView trailerName;
        TextView trailerType;

        // TODO : 91 ) Determining trailer name and type with their ids in trailer.xml in the constructor.
        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerName =(TextView)itemView.findViewById(R.id.tv_name_trailer);
            trailerType =(TextView)itemView.findViewById(R.id.tv_type_trailer);

            itemView.setOnClickListener(this);
        }

        // TODO : 92 ) Adding click function when click each item of the list
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailerList.get(adapterPosition);
            mClickHandler.onClick(trailer);

        }

        // TODO : 93 ) Getting returns for each TextView
        public TextView getTrailerName(){ return trailerName; }
        public TextView getTrailerType(){ return trailerType; }
    }

    // TODO : 94 ) Defining Interface for TrailerViewHolder
    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    // TODO : 98 ) Setting trailerdata to mTrailerList and save it
    public void setTrailerData(ArrayList<Trailer> trailerData) {
        mTrailerList = trailerData;
        notifyDataSetChanged();
    }

}

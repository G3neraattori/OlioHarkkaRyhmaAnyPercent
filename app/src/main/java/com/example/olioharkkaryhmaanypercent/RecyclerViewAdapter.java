package com.example.olioharkkaryhmaanypercent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Object[] moviearray;
    // RecyclerView recyclerView;
    public RecyclerViewAdapter(Object[] moviearray) {
        this.moviearray = moviearray;
    }
    // RecyclerView Layout Set
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.movie_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    // RecyclerView Set Text And Image Or Click
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = (Movie)moviearray[position];
        holder.textView.setText(movie.getMovieName());
        //holder.imageView.setImageResource(listdata[position].getImgId());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: ", Toast.LENGTH_LONG).show();
            }
        });
    }
    // RecyclerView Set Text And Image Or Click  End
    // RecyclerView Set View item limit
    @Override
    public int getItemCount() {
        return moviearray.length;
    }
    // RecyclerView Set View item limit  End
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.movie_item_imageview);
            this.textView = (TextView) itemView.findViewById(R.id.movie_item_textview);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
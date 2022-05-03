package com.example.olioharkkaryhmaanypercent;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder>{
    private Object[] moviearray;
    private UserpageFragment fragment;
    ImageView imageView;
    // RecyclerView recyclerView;
    public RecyclerViewAdapter2(Object[] moviearray, UserpageFragment fragment) {
        this.fragment = fragment;
        this.moviearray = moviearray;
    }
    // RecyclerView Layout Set
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.moviearray = UserData.actuallyLoadUserData(MainActivity.userManager.getCurrentUser().getUsername()).values().toArray(new Movie[0]);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.movie_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    // RecyclerView Set Text And Image Or Click
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Movie movie = (Movie)moviearray[position];
        holder.textView.setText(movie.getMovieName());

        //Get pictures from url
        if (movie.getImageurl().trim().length() != 0) {
            Picasso.get().load(movie.getImageurl()).resize(100, 150).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(holder.imageView);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(position);
                fragment.onMovieClick(view, position);
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
package com.example.olioharkkaryhmaanypercent;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Object[] moviearray;
    private Fragment2 fragment;
    ImageView imageView;
    // RecyclerView recyclerView;
    public RecyclerViewAdapter(Object[] moviearray, Fragment2 fragment) {
        this.fragment = fragment;
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
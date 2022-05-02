package com.example.olioharkkaryhmaanypercent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Fragment_infopage extends Fragment {
    public int position;
    View view;

    public void setPosition(int position) { this.position = position; }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_infopage, container, false);
        //TextView movie_info_name = (TextView) view.findViewById(R.id.movie_info_name);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView movie_info_name = (TextView) view.findViewById(R.id.movie_info_name);
        TextView movie_info_description = (TextView) view.findViewById(R.id.movie_info_description);
        TextView movie_info_year = (TextView) view.findViewById(R.id.movie_info_year);
        ImageView movie_info_image = (ImageView) view.findViewById(R.id.movie_info_image);
        Movie[] movielist = MainActivity.movieManager.getMovieList().values().toArray(new Movie[0]);
        Movie movie = movielist[position];
        movie_info_name.setText(movie.getMovieName());
        movie_info_description.setText(movie.getMovieDescription());
        movie_info_year.setText(Integer.toString(movie.getMovieYear()));
        System.out.println("onviewcreated");
    }
}
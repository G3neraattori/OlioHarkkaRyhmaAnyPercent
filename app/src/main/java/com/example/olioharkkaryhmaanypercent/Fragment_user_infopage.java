package com.example.olioharkkaryhmaanypercent;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class Fragment_user_infopage extends Fragment {
    public int position;
    View view;
    public String location;

    public void setLocation(String location) { this.location = location; }
    public String getLocation() { return this.location; }
    ImageView imageView;

    public void setPosition(int position) { this.position = position; }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_infopage, container, false);
        setLocation("");
        //TextView movie_info_name = (TextView) view.findViewById(R.id.movie_info_name);
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView movie_info_name = view.findViewById(R.id.movie_userinfo_name);
        TextView movie_info_description = view.findViewById(R.id.movie_userpage_fragment);
        TextView movie_info_year = view.findViewById(R.id.movie_userpage_year);
        TextView movie_info_show_times = view.findViewById(R.id.movie_info_show_times);
        TextView movie_personalrating = view.findViewById(R.id.userpage_personalrating);
        ImageView movie_info_image = view.findViewById(R.id.movie_info_image);
        Button rateButton = view.findViewById(R.id.movie_info_rate);
        Button favouriteButton = view.findViewById(R.id.movie_info_favourite);
        Movie[] movielist = UserData.actuallyLoadUserData(MainActivity.userManager.getCurrentUser().
                getUsername()).values().toArray(new Movie[0]);
        Movie movie = movielist[position];
        System.out.println(movie.getPersonalRating() + " personasldnalskdjn ");
        movie_personalrating.setText("Oma arvostelu:\n\n" + movie.getPersonalRating());
        String imageurl = movie.getImageurl();
        movie_info_name.setText(movie.getMovieName());
        movie_info_description.setText(movie.getMovieDescription());
        movie_info_year.setText(Integer.toString(movie.getMovieYear()));

    }
}
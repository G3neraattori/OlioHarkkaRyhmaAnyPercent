package com.example.olioharkkaryhmaanypercent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
        TextView movie_imdbrating = view.findViewById(R.id.userpage_imdbrating);
        ImageView movie_info_image = view.findViewById(R.id.movie_info_image);
        Button rateButton = view.findViewById(R.id.movie_info_rate);
        Button favouriteButton = view.findViewById(R.id.movie_info_favourite);
        Movie[] movielist = UserData.actuallyLoadUserData(MainActivity.userManager.getCurrentUser().
                getUsername()).values().toArray(new Movie[0]);
        Movie movie = movielist[position];
        movie_imdbrating.setText("Imdb rating:\n\n" + movie.getImdbRating());
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == view.findViewById(R.id.movie_info_favourite)) {
                    if (MainActivity.userManager.currentUser==null) {
                        Toast.makeText(getContext(), "Kirjaudu sisään ensin, jotta voit tallentaa elokuvia suosikkeihin.", Toast.LENGTH_SHORT).show();
                    } else {
                        UserData.saveMovie(MainActivity.userManager.currentUser,movie);
                        Toast.makeText(getContext(), "Elokuva tallennettu suosikeihin", Toast.LENGTH_SHORT).show();
                    }
                } else if (view == view.findViewById(R.id.movie_info_rate)) {
                    if (MainActivity.userManager.currentUser==null) {
                        UserData user = new UserData(requireContext());
                        Toast.makeText(getContext(), "Tallenna elokuva ennen arvostelua.", Toast.LENGTH_SHORT).show();

                    } else {
                        //rateMovie(movie, personalRating)
                        UserData user = new UserData(requireContext());
                        user.saveReview(MainActivity.userManager.currentUser, movie, "Coda - Kahden maailman välissä", 5);
                        Toast.makeText(getContext(), "Elokuva arvosteltu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        rateButton.setOnClickListener(listener);
        favouriteButton.setOnClickListener(listener);
        String imageurl = movie.getImageurl();
        //Picture for url with picasso library

        movie_info_name.setText(movie.getMovieName());
        movie_info_description.setText(movie.getMovieDescription());
        movie_info_year.setText(Integer.toString(movie.getMovieYear()));
    }
}
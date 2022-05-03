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
        view = inflater.inflate(R.layout.fragment_infopage, container, false);
        setLocation("");
        //TextView movie_info_name = (TextView) view.findViewById(R.id.movie_info_name);
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView movie_info_name = view.findViewById(R.id.movie_info_name);
        TextView movie_info_description = view.findViewById(R.id.movie_info_description);
        TextView movie_info_year = view.findViewById(R.id.movie_info_year);
        TextView movie_info_imdbrating = view.findViewById(R.id.movie_info_imdbrating);
        TextView movie_info_show_times = view.findViewById(R.id.movie_info_show_times);
        ImageView movie_info_image = view.findViewById(R.id.movie_info_image);
        MaterialCalendarView mCalendarView = (MaterialCalendarView) view.findViewById(R.id.movie_info_calendar);
        Button rateButton = view.findViewById(R.id.movie_info_rate);
        Button favouriteButton = view.findViewById(R.id.movie_info_favourite);
        Movie[] movielist = MainActivity.movieManager.getMovieList().values().toArray(new Movie[0]);
        Movie movie = movielist[position];
        if (movie.getImdbRating()==0.0) {
            movie_info_imdbrating.setText("Imdb rating:\n\n" + MainActivity.movieManager.getDataFromImdb(movie.getOriginalName()));
        } else {
            movie_info_imdbrating.setText("Imdb rating:\n\n" + movie.getImdbRating());
        }
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
                    if (1==1/*movieIsFavourite==1*/) {
                        UserData user = new UserData(requireContext());
                        Toast.makeText(getContext(), "Tallenna elokuva ennen arvostelua.", Toast.LENGTH_SHORT).show();
                    } else {
                        //rateMovie(movie, personalRating)
                        Toast.makeText(getContext(), "Elokuva arvosteltu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        rateButton.setOnClickListener(listener);
        favouriteButton.setOnClickListener(listener);
        String imageurl = movie.getImageurl();
        //Picture for url with picasso library
        if (movie.getImageurl().trim().length() != 0) {
            Picasso.get().load(imageurl).resize(200, 300).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(movie_info_image);
        }
        String location = "";
        Collection<LocalDateTime> dateTimes = MainActivity.movieManager.getMovieDates(movie.getMovieID(), location);
        Collection<CalendarDay> days = new HashSet<CalendarDay>();
        for (LocalDateTime e : dateTimes) {
            days.add(CalendarDay.from(e.getYear(),e.getMonthValue(),e.getDayOfMonth()));
        }
        HashMap<String, String> spinnerTheaterList = MainActivity.movieManager.getSpinnerTheaterList();
        List<String> spinnerTheaterList2 = new ArrayList<String>();
        for (int i = 0; i < spinnerTheaterList.size();i++) {
            spinnerTheaterList2.add(spinnerTheaterList.values().toArray()[i].toString());
        }
        List<String> spinnerTheaterList3 = spinnerTheaterList2.stream().sorted().collect(Collectors.toList());
        ArrayAdapter customAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinnerTheaterList3);
        Spinner spinner = view.findViewById(R.id.movie_info_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCalendarView.removeDecorators();
                setLocation(spinnerTheaterList3.get(position));
                Collection<LocalDateTime> dateTimes = MainActivity.movieManager.getMovieDates(movie.getMovieID(), getLocation());
                Collection<CalendarDay> days = new HashSet<CalendarDay>();
                for (LocalDateTime e : dateTimes) {
                    days.add(CalendarDay.from(e.getYear(),e.getMonthValue(),e.getDayOfMonth()));
                }
                EventDecorator decorator = new EventDecorator(Color.GREEN, days);
                mCalendarView.addDecorator(decorator);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        OnDateSelectedListener dateListener = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                HashSet<LocalTime> showTimes = MainActivity.movieManager.getMovieShowTimes(movie, date, getLocation());
                String showTimeString = "Esitysajat:\n";
                for (LocalTime time : showTimes) {
                    showTimeString = showTimeString.concat(time.toString() + "\n");
                }
                movie_info_show_times.setText(showTimeString);
            }
        };

        mCalendarView.setOnDateChangedListener(dateListener);
        spinner.setAdapter(customAdapter);
        EventDecorator decorator = new EventDecorator(Color.GREEN, days);
        mCalendarView.addDecorator(decorator);
        movie_info_name.setText(movie.getMovieName());
        movie_info_description.setText(movie.getMovieDescription());
        movie_info_year.setText(Integer.toString(movie.getMovieYear()));
        movie_info_imdbrating.setText("Imdb pisteet:\n\n" + MainActivity.movieManager.getDataFromImdb(movie.getOriginalName()));
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {

            //uncomment below line in image name have spaces.
            //src = src.replaceAll(" ", "%20");

            URL url = new URL(src);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            Log.d("", e.toString());
            return null;
        }
    }
}
package com.example.olioharkkaryhmaanypercent;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView movie_info_name = view.findViewById(R.id.movie_info_name);
        TextView movie_info_description = view.findViewById(R.id.movie_info_description);
        TextView movie_info_year = view.findViewById(R.id.movie_info_year);
        ImageView movie_info_image = view.findViewById(R.id.movie_info_image);
        MaterialCalendarView mCalendarView = (MaterialCalendarView) view.findViewById(R.id.movie_info_calendar);
        Movie[] movielist = MainActivity.movieManager.getMovieList().values().toArray(new Movie[0]);
        Movie movie = movielist[position];
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
            System.out.println(spinnerTheaterList.values().toArray()[i].toString());
        }
        List<String> spinnerTheaterList3 = spinnerTheaterList2.stream().sorted().collect(Collectors.toList());
        ArrayAdapter customAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinnerTheaterList3);
        Spinner spinner = view.findViewById(R.id.movie_info_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("tää on " + position);
                mCalendarView.removeDecorators();
                String location = spinnerTheaterList3.get(position);
                Collection<LocalDateTime> dateTimes = MainActivity.movieManager.getMovieDates(movie.getMovieID(), location);
                Collection<CalendarDay> days = new HashSet<CalendarDay>();
                for (LocalDateTime e : dateTimes) {
                    days.add(CalendarDay.from(e.getYear(),e.getMonthValue(),e.getDayOfMonth()));
                }
                System.out.println(MainActivity.movieManager.getEntryList().size());
                EventDecorator decorator = new EventDecorator(Color.GREEN, days);
                mCalendarView.addDecorator(decorator);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(customAdapter);
        EventDecorator decorator = new EventDecorator(Color.GREEN, days);
        mCalendarView.addDecorator(decorator);
        movie_info_name.setText(movie.getMovieName());
        movie_info_description.setText(movie.getMovieDescription());
        movie_info_year.setText(Integer.toString(movie.getMovieYear()));
    }
}
package com.example.olioharkkaryhmaanypercent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.HashSet;

public class SearchFragment extends Fragment {
    View view;
    public HashMap<String, Movie> movielist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        movielist = MainActivity.movieManager.getMovieList();
        view = inflater.inflate(R.layout.fragment_fragment1, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SearchView searchView = view.findViewById(R.id.movie_searchbar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.movie_search_recycler);
        RecyclerViewAdapter3 adapter = new RecyclerViewAdapter3(movielist.values().toArray(new Movie[0]), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        SearchView.OnQueryTextListener searchlistener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                movielist = MainActivity.movieManager.getMovieList();
                HashSet<Movie> movieset = new HashSet<>();
                for (Object m : movielist.values().toArray()) {
                    Movie movie = ((Movie)m);
                    MainActivity.MovieManager.Cast cast = ((MainActivity.MovieManager.Cast) movie.getMovieCast());

                    if ((movie.getMovieName().toLowerCase().contains(s.toLowerCase())
                    || Integer.toString(movie.getMovieYear()).contains(s)
                    || cast.getCastDirector().toLowerCase().contains(s))) {
                        movieset.add(movie);
                    } else {
                        for (String actor : cast.getCastActors()) {
                            if (actor.toLowerCase().contains(s)) {
                                movieset.add(movie);
                            }
                        }
                    }
                    movielist.clear();
                    for (Movie e : movieset) { // construct as new movielist to get right page when clicking movie
                        movielist.put(Integer.toString(e.getMovieID()), e);
                    }
                    refreshMovies(movielist);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(searchlistener);
    }

    public void refreshMovies(HashMap<String, Movie> movieset) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.movie_search_recycler);
        RecyclerViewAdapter3 adapter = new RecyclerViewAdapter3(movieset.values().toArray(new Movie[0]), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void onMovieClick(@NonNull View view, int position) {
        Fragment_infopage fragment = new Fragment_infopage();
        fragment.setPosition(position);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentWindow, fragment);
        transaction.commit();
    }
}
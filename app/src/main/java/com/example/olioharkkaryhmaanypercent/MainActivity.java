package com.example.olioharkkaryhmaanypercent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity { // asdf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MovieManager movieManager = new MovieManager();

        // For testing class construction
        movieManager.entryList.add(new Entry("12:00", "12.04.2022"));
        movieManager.entryList.get(0).entryMovie = new Movie("Yön ritari", "Batman ja muut seikailee",
                new Cast("Nintentovehkeet", new ArrayList<String>()), 20000){};
        movieManager.entryList.add(new Entry("12:00", "13.04.2022"));
        movieManager.entryList.get(1).entryMovie = new Movie("yokeri", "asdf",
                new Cast("Rippikoulu", new ArrayList<String>()), 2000){};
        movieManager.printEntries();
        // Luokkien luomisen testaukseen
    }



    public class MovieManager {   // MovieManager class contains the list for all movie entries
        private ArrayList<Entry> entryList;

        public MovieManager() {
            this.entryList = new ArrayList<Entry>();
        }
        public void printEntries() { // gettien testaamistarkoitukseen
            for (int i=0; i<2; i++) {
                String date = entryList.get(i).getEntryDate();
                String time = entryList.get(i).getEntryTime();
                String location = entryList.get(i).getEntryLocation();
                String name = entryList.get(i).entryMovie.getMovieName();
                String desc = entryList.get(i).entryMovie.getMovieDescription();
                String length = entryList.get(i).entryMovie.getMovieLength().toString();


                System.out.println("Movie date and time: " + date + " " + time);
                System.out.println("Movie location: " + location);
                System.out.println("Movie name: " + name + "\n" + "Decription: " + desc);
                System.out.println("Movie length: " + length + "\n");
                System.out.println("Movie Director: " + entryList.get(i).entryMovie.getMovieCast().getCastDirector());
                System.out.println("Movie actors:");
                for (int n=0;i<entryList.get(i).entryMovie.getMovieCast().getCastActors().size();n++) {
                    System.out.println(entryList.get(i).entryMovie.getMovieCast().getCastActors().get(n) + "\n");
                }
                System.out.println("\n");
            }
        }

    }
    public class Entry { // each entry is fetched from Finkino xml and contains info on one
        // näytös :D of a movie. Each entry added to entryList to contain information
        // some näytökset may contain the same movie so more entry class than movie class
        public Entry(String entryTime, String entryDate) {
            this.entryMovie = null;
            this.entryTime = entryTime;
            this.entryDate = entryDate;
            this.entryLocation = "lappeen Ranta";

        }
        private Movie entryMovie;
        private String entryDate;
        private String entryTime;  // TODO muuta time ja date olioiksi
        private String entryLocation;

        private String getEntryDate() {
            return this.entryDate;
        }
        private String getEntryLocation() {
            return this.entryLocation;
        }
        private String getEntryTime() {
            return this.entryTime;
        }
    }
    public class Movie {   // Movie class contains information on one unique movie

        // Constructor with parameters for movie info
        public Movie(String movieName, String movieDescription, Cast movieCast, int movieLength) {
            this.movieName = movieName;
            this.movieDescription = movieDescription;
            this.movieCast = movieCast;
            this.movieLength = new Time(movieLength);
        }
        public Movie() {  // Empty constructor
            this.movieName = "empty";
            this.movieDescription = "empty";
            this.movieCast = null;
            this.movieLength = new Time(0);
        }
        private String getMovieName() {
            return this.movieName;
        }
        private String getMovieDescription() {
            return this.movieDescription;
        }
        private Cast getMovieCast() {
            return this.movieCast;
        }
        private Time getMovieLength() {
            return this.movieLength;
        }

        private String movieName;
        private String movieDescription;
        private Time movieLength;
        private Cast movieCast;
    }

    public class Cast {     // Cast class contains a movie's director and actors
                            // Could be integrated to movie class ??
        public Cast(String castDirector, ArrayList<String> castActors) { // Constructor with parameters
            this.castDirector = castDirector;
            this.castActors = castActors;
        }
        public Cast() {         // Empty constructor
            this.castDirector = "empty";
            this.castActors = null;
        }
        private String getCastDirector() {
            return this.castDirector;
        }
        private ArrayList<String> getCastActors() {
            return castActors;
        }
        private String castDirector;
        private ArrayList<String> castActors;
    }
}

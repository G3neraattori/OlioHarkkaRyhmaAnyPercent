package com.example.olioharkkaryhmaanypercent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity { // asdf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MovieManager movieManager = new MovieManager();

        // For testing class construction
        movieManager.entryList.add(new Entry("12:00", "12.04.2022"));
        movieManager.entryList.get(0).entryMovie = new Movie("Yön ritari", 123, "Batman ja muut seikailee",
                new Cast("Nintentovehkeet", new ArrayList<>()), 20000){};
        movieManager.entryList.add(new Entry("12:00", "13.04.2022"));
        movieManager.entryList.get(1).entryMovie = new Movie("yokeri", 1234, "asdf",
                new Cast("Rippikoulu", new ArrayList<>()), 2000){};
        movieManager.printEntries();
        // Luokkien luomisen testaukseen
    }



    public class MovieManager {   // MovieManager class contains the list for all movie entries
        private ArrayList<Entry> entryList;
        private ArrayList<Movie> movieList; // lists all currently available movies

        public MovieManager() {
            this.entryList = new ArrayList<>();
            this.movieList = new ArrayList<>();
        }


        public void printEntries() { // gettien testaamistarkoitukseen
            for (int i=0; i<2; i++) {
                String location = entryList.get(i).getEntryLocation();
                String name = entryList.get(i).entryMovie.getMovieName();
                String desc = entryList.get(i).entryMovie.getMovieDescription();
                String length = entryList.get(i).entryMovie.getMovieLength().toString();


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
        public Movie checkIfMovieExists(int newMovieID) {  // This method goes through entries and checks if a
            // movie object already exists
            for (int i = 0;i < this.entryList.size();i++) {
                int movieID = this.entryList.get(i).entryMovie.getMovieID();
                if (movieID == newMovieID) {
                    return this.entryList.get(i).getEntryMovie();
                }
            }
            return null;
        }
        public void getMovies() { // Generate a list of all currently available movies
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                String url = "https://www.finnkino.fi/xml/Events/";
                Document doc = builder.parse(url);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getDocumentElement().getElementsByTagName("Event");

                for (int n = 0; n < nList.getLength(); n++) {
                    Movie newMovie = new Movie();
                    Node node = nList.item(n);
                    Element element = (Element) node;

                    newMovie.setMovieID(Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent()));
                    newMovie.setMovieName(element.getElementsByTagName("Title").item(0).getTextContent());
                    newMovie.setMovieLength(Integer.parseInt(element.getElementsByTagName("LengthInMinutes").item(0).getTextContent()));
                    newMovie.setMovieDescription(element.getElementsByTagName("Synopsis").item(0).getTextContent());
                    newMovie.movieCast = new Cast(
                            element.getElementsByTagName("Director").item(0).getTextContent();
                    );
                }
                NodeList nList = doc.getDocumentElement().getElementsByTagName("Cast");

            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }

        }
        public void getEntries() {  // Gets the entries from Finnkino xml and saves them.

            // List containing all the theater ids to go through
            int[] allTheatersId = {1014,1015,1016,1017,1018,1019,1021,1022,1041};

            try {
                for (int i = 0; i<9;i++) {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    String url = "https://www.finnkino.fi/xml/Schedule/?area=" + allTheatersId[i] + "&nrOfDays=31";
                    Document doc = builder.parse(url);
                    doc.getDocumentElement().normalize();
                    NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

                    for (int n = 0; n < nList.getLength(); n++) {
                        Node node = nList.item(n);
                        Element element = (Element) node;

                        int movieID = Integer.parseInt(element.getElementsByTagName("EventID").item(0).getTextContent());
                        String location = element.getElementsByTagName("Theatre").item(0).getTextContent();
                        int lengthInMin = Integer.parseInt(element.getElementsByTagName("LengthInMinutes").item(0).getTextContent());
                        String[] dateTimeString = element.getElementsByTagName("dttmShowStart").item(0).getTextContent().split("T", 2);
                        String[] dateString = dateTimeString[0].split("-",3);
                        String[] timeString = dateTimeString[1].split(":",3);
                        Date date = new Date(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
                        Time time = new Time(Integer.parseInt(timeString[0]), Integer.parseInt(timeString[1]), Integer.parseInt(timeString[2]));
                        Movie newMovie = this.checkIfMovieExists(movieID);
                        if (newMovie  == null) {
                            String name = element.getElementsByTagName("Title").item(0).getTextContent();
                        } else {

                        }
                    }
                }
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
    public class Entry { // each entry is fetched from Finkino xml and contains info on one
        // näytös :D of a movie.
        // some näytökset may contain the same movie so more entry class than movie class
        public Entry(String entryTime, String entryDate) {
            this.entryMovie = null;
            this.entryTime = new Time(0,0,0);
            this.entryDate = new Date(0,0,0);
            this.entryLocation = "lappeen Ranta";

        }
        private Movie entryMovie;
        private Date entryDate;
        private Time entryTime;
        private String entryLocation;

        private Movie getEntryMovie() { return this.entryMovie; }
        private Date getEntryDate() {
            return this.entryDate;
        }
        private String getEntryLocation() {
            return this.entryLocation;
        }
        private Time getEntryTime() {
            return this.entryTime;
        }
    }
    public class Movie {   // Movie class contains information on one unique movie

        // Constructor with parameters for movie info
        public Movie(String movieName, int movieID, String movieDescription, Cast movieCast, int movieLength) {
            this.movieName = movieName;
            this.movieID = movieID;
            this.movieDescription = movieDescription;
            this.movieCast = movieCast;
            this.movieLength = new Time(movieLength);
        }
        public Movie() {  // Empty constructor
            this.movieName = "empty";
            this.movieID = 0;
            this.movieDescription = "empty";
            this.movieCast = null;
            this.movieLength = new Time(0);
        }
        private String getMovieName() {
            return this.movieName;
        }
        private int getMovieID() { return this.movieID; }
        private String getMovieDescription() {
            return this.movieDescription;
        }
        private Cast getMovieCast() {
            return this.movieCast;
        }
        private int getMovieLength() {
            return this.movieLength;
        }

        private void setMovieName(String movieName) { this.movieName = movieName; }
        private void setMovieID(int movieID) { this.movieID = movieID; }
        private void setMovieDescription(String movieDescription) { this.movieName = movieDescription; }
        private void setMovieCast(Cast movieCast) { this.movieCast = movieCast; }
        private void setMovieLength(int movieLength) { this.movieLength = movieLength; }

        private String movieName;
        private int movieID;
        private String movieDescription;
        private int movieLength;
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

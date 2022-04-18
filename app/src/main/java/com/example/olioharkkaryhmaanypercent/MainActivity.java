package com.example.olioharkkaryhmaanypercent;

import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// Luokkarakenne melkein ok, ei tallenna actoreita oikein / niitä ei voi getata oikein en tiiä :D
// Date ja Time ilmeisesti deprecated, pitää korjata

public class MainActivity extends AppCompatActivity { // asdf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        MovieManager movieManager = new MovieManager();

        // For testing class construction
        movieManager.getMovies();
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
            for (int i=0; i<this.movieList.size(); i++) {
                String movieName = this.movieList.get(i).getMovieName();
                String movieDescription = this.movieList.get(i).getMovieDescription();
                String movieDirector = this.movieList.get(i).getMovieCast().getCastDirector();


                System.out.println("Movie name: " + movieName + "\n" + "Decription: " + movieDescription);
                System.out.println("Movie Director: " + movieDirector.trim());
                System.out.println("Movie actors:");
                System.out.println(this.movieList.get(i).getMovieCast().getCastActors().get(0).trim() + "\n"); // TODO ei tallenna tai tulosta Cast olioon actoreita :D
                System.out.println("************");
            }
        }
        public void getMovies() { // Generate a list of all currently available movies
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                String url = "https://www.finnkino.fi/xml/Events/";
                Document doc = builder.parse(url);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getDocumentElement().getElementsByTagName("Event");

                for (int n = 0; n < nList.getLength(); n++) {
                    Node node = nList.item(n);
                    Element element = (Element) node;
                    int movieID = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                    String movieName = element.getElementsByTagName("Title").item(0).getTextContent();
                    int length = Integer.parseInt(element.getElementsByTagName("LengthInMinutes").item(0).getTextContent());
                    int productionYear = Integer.parseInt(element.getElementsByTagName("ProductionYear").item(0).getTextContent());
                    String description = element.getElementsByTagName("Synopsis").item(0).getTextContent();
                    ArrayList<String> actors = new ArrayList<>();
                    String director = element.getElementsByTagName("Directors").item(0).getTextContent();

                    for (int i = 0; i < element.getElementsByTagName("Cast").getLength(); i++) {
                        actors.add(element.getElementsByTagName("Cast").item(i).getTextContent());
                    }
                    this.movieList.add(new Movie(movieName, movieID, description, new Cast(director, (ArrayList<String>)actors.clone()), length, productionYear));
                    actors.clear();
                }
                } catch (ParserConfigurationException | SAXException | IOException parserConfigurationException) {
                parserConfigurationException.printStackTrace();
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
                        String name = element.getElementsByTagName("Title").item(0).getTextContent();
                        String description = element.getElementsByTagName("Title").item(0).getTextContent();
                        String location = element.getElementsByTagName("Theatre").item(0).getTextContent();
                        int lengthInMin = Integer.parseInt(element.getElementsByTagName("LengthInMinutes").item(0).getTextContent());
                        String[] dateTimeString = element.getElementsByTagName("dttmShowStart").item(0).getTextContent().split("T", 2);
                        String[] dateString = dateTimeString[0].split("-",3);
                        String[] timeString = dateTimeString[1].split(":",3);
                        Date date = new Date(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
                        Time time = new Time(Integer.parseInt(timeString[0]), Integer.parseInt(timeString[1]), Integer.parseInt(timeString[2]));
                        //Movie newMovie = new Movie(name, movieID, ,  , 0);
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
        public Movie(String movieName, int movieID, String movieDescription, Cast movieCast, int movieLength, int movieYear) {
            this.movieName = movieName;
            this.movieID = movieID;
            this.movieDescription = movieDescription;
            this.movieCast = movieCast;
            this.movieLength = movieLength;
            this.movieYear = movieYear;
            for (int i = 0; i < this.getMovieCast().getCastActors().size(); i++) {
                System.out.println(this.getMovieCast().getCastActors().get(i)+"\n");;
            }
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
        private int movieYear;
    }

    public class Cast {     // Cast class contains a movie's director and actors
                            // Could be integrated to movie class ??
        public Cast(String castDirector, ArrayList<String> castActors) { // Constructor with parameters
            this.castDirector = castDirector;
            this.castActors = castActors;
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

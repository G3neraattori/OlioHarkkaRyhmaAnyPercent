package com.example.olioharkkaryhmaanypercent;

import android.content.res.AssetFileDescriptor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;
import android.content.Context;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
// Luokkarakenne melkein ok, ei tallenna actoreita oikein / niitä ei voi getata oikein en tiiä :D
// Date ja Time ilmeisesti deprecated, pitää korjata

public class MainActivity extends AppCompatActivity {



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;


                if (view == findViewById(R.id.button1)) {
                    System.out.println("Fragment 1");
                    fragment = new Fragment1();
                } else if (view == findViewById(R.id.button2)) {
                    System.out.println("Fragment 2");
                    fragment = new Fragment2();
                } else{
                    System.out.println("Fragment 3");
                    fragment = new Fragment3();
                }

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentWindow, fragment);
                transaction.commit();
            }
        };

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(listener);
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(listener);
        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(listener);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MovieManager movieManager = new MovieManager();

        // For testing class construction
        movieManager.generateMovieList();
        movieManager.generateEntryList();
        //movieManager.getDataFromImdb();
        movieManager.printEntries();
    }


    public class MovieManager {   // MovieManager class contains the list for all movie entries
        private ArrayList<Entry> entryList;
        private HashMap<String, Movie> movieList; // lists all currently available movies

        public MovieManager() {
            this.entryList = new ArrayList<>();
            this.movieList = new HashMap<>();
        }

        public HashMap<String, Movie> getMovieList() {
            return this.movieList;
        }

        public ArrayList<Entry> getEntryList() {
            return this.entryList;
        }

        public void printEntries() { // gettien testaamistarkoitukseen
            ArrayList<Entry> listOfEntries = this.getEntryList();
            for (int i = 0; i < 50; i++) {
                Entry entry = listOfEntries.get(i);
                System.out.println("Entry data: ");
                System.out.println("Entry location: " + entry.getEntryLocation());
                System.out.println("Entry time and date: " + entry.getEntryDate().toString() + " " + entry.getEntryTime().toString());
                System.out.println("MovieID: " + entry.getEntryMovie(this).getMovieID());
                System.out.println("Movie title: " + entry.getEntryMovie(this).getMovieName());
                System.out.println("Movie year: " + entry.getEntryMovie(this).getMovieYear());
                System.out.println("Movie length: " + entry.getEntryMovie(this).getMovieLength() + " minutes");
                System.out.println("Movie synopsis: " + entry.getEntryMovie(this).getMovieDescription());
                System.out.println("Movie director: " + entry.getEntryMovie(this).getMovieCast().getCastDirector());
                System.out.println("Movie rating IMDB: " + entry.getEntryMovie(this).getImdbRating());
                System.out.println("Movie image url: " + entry.getEntryMovie(this).getImageurl());
                System.out.println("Movie actors:");
                for (int j = 0; j < entry.getEntryMovie(this).getMovieCast().getCastActors().size(); j++) {
                    System.out.println(entry.getEntryMovie(this).getMovieCast().getCastActors().get(j));
                }
                System.out.println("*******************");
            }
        }

        public void generateMovieList() { // Generate a list of all currently available movies
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
                    String originalName = element.getElementsByTagName("OriginalTitle").item(0).getTextContent();
                    int length = Integer.parseInt(element.getElementsByTagName("LengthInMinutes").item(0).getTextContent());
                    int productionYear = Integer.parseInt(element.getElementsByTagName("ProductionYear").item(0).getTextContent());
                    String description = element.getElementsByTagName("Synopsis").item(0).getTextContent().replace("\n", "");
                    ArrayList<String> actors = new ArrayList<>();
                    Element image = (Element)element.getElementsByTagName("Images").item(0);
                    String imageurl = image.getTextContent().split("\n")[3].trim();
                    String[] directorArray = element.getElementsByTagName("Directors").item(0).getTextContent().split("\n");
                    String director;
                    if (directorArray.length > 1) {
                        director = directorArray[2].trim() + " " + directorArray[3].trim();
                    } else {
                        director = directorArray[0].trim();
                    }

                    String actor_string; // a string to help with extracting actors
                    for (int i = 0; i < element.getElementsByTagName("Cast").getLength(); i++) {
                        actor_string = element.getElementsByTagName("Cast").item(i).getTextContent();
                        String[] actor_array = actor_string.split("\n");
                        for (int j = 0; j < actor_array.length; j = j + 2) {
                            if (actor_array[j].length() > 8) {
                                actors.add(actor_array[j].trim() + " " + actor_array[j + 1].trim());
                            }
                        }
                    }
                    this.movieList.put(Integer.toString(movieID), new Movie(movieName, movieID, description, originalName, new Cast(director, (ArrayList<String>) actors.clone()), length, productionYear, imageurl));
                    actors.clear();
                }
            } catch (ParserConfigurationException | SAXException | IOException parserConfigurationException) {
                parserConfigurationException.printStackTrace();
            }
        }

        public void generateEntryList() {  // Gets the entries from Finnkino xml and saves them.

            // List containing all the theater ids to go through
            int[] allTheatersId = {1014, 1015, 1016, 1017, 1018, 1019, 1021, 1022, 1041};
            try {
                for (int i = 0; i < 9; i++) {
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
                        String[] dateString = dateTimeString[0].split("-", 3);
                        String[] timeString = dateTimeString[1].split(":", 3);
                        Date date = new Date(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
                        Time time = new Time(Integer.parseInt(timeString[0]), Integer.parseInt(timeString[1]), Integer.parseInt(timeString[2]));
                        // Date and Time classes are deprecated, but couldnt figure another way to do this, as LocalTime and LocalDate dont
                        // work on android 6.0
                        this.getEntryList().add(new Entry(movieID, location, lengthInMin, date, time));
                    }
                }
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }

        public void getDataFromImdb() {
            for (Movie movie : this.getMovieList().values()) {
                try {
                    String searchTerm = movie.getOriginalName();
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    String apiKey = "k_aaaaaaaa";
                    URL url = new URL("https://imdb-api.com/API/AdvancedSearch/" + apiKey + "/?title=" + searchTerm);
                    HttpURLConnection connection;
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);

                    InputStream stream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    reader.close();
                    String jsonString = stringBuilder.toString();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getJSONArray("results").length() != 0) {
                        JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);
                        String ratingString = results.getString("imDbRating");
                        System.out.println(ratingString + "Tää on se score");
                        try {
                            movie.setImdbRating(Double.parseDouble(ratingString));
                        } catch (NullPointerException | NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ParserConfigurationException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public class Entry { // each entry is fetched from Finkino xml and contains info on one
                            // showing of a movie
            public Entry(int movieID, String location, int lengthInMin, Date entryDate, Time entryTime) {
                this.movieID = movieID;
                this.entryTime = entryTime;
                this.entryDate = entryDate;
                this.entryLocation = location;
                this.lengthInMin = lengthInMin;

            }

            private int movieID;
            private Date entryDate;
            private Time entryTime;
            private String entryLocation;
            private int lengthInMin;


            private Date getEntryDate() {
                return this.entryDate;
            }

            private String getEntryLocation() {
                return this.entryLocation;
            }

            private Time getEntryTime() {
                return this.entryTime;
            }

            private int getMovieID() {
                return this.movieID;
            }

            private Movie getEntryMovie(MovieManager movieManager) {
                return movieManager.getMovieList().get(Integer.toString(this.movieID));
            }
        }

        public class Movie {   // Movie class contains information on one unique movie
            // Constructor with parameters for movie info
            public Movie(String movieName, int movieID, String movieDescription, String originalName, Cast movieCast, int movieLength, int movieYear, String imageurl) {
                this.movieName = movieName;
                this.originalName = originalName;
                this.movieID = movieID;
                this.movieDescription = movieDescription;
                this.movieCast = movieCast;
                this.movieLength = movieLength;
                this.movieYear = movieYear;
                this.imdbRating = 0.0;
                this.imageurl = imageurl;
            }

            private String getMovieName() {
                return this.movieName;
            }

            private String getOriginalName() {
                return this.originalName;
            }

            private int getMovieID() {
                return this.movieID;
            }

            private String getMovieDescription() {
                return this.movieDescription;
            }

            private Cast getMovieCast() {
                return this.movieCast;
            }

            private int getMovieLength() {
                return this.movieLength;
            }

            private int getMovieYear() {
                return this.movieYear;
            }

            private double getImdbRating() {
                return this.imdbRating;
            }

            private String getImageurl() { return this.imageurl; }

            private void setMovieName(String movieName) {
                this.movieName = movieName;
            }

            private void setMovieID(int movieID) {
                this.movieID = movieID;
            }

            private void setMovieDescription(String movieDescription) {
                this.movieName = movieDescription;
            }

            private void setMovieCast(Cast movieCast) {
                this.movieCast = movieCast;
            }

            private void setMovieLength(int movieLength) {
                this.movieLength = movieLength;
            }

            private void setImdbRating(double imdbRating) {
                this.imdbRating = imdbRating;
            }

            private void setOriginalName(String originalName) {
                this.originalName = originalName;
            }
            private void setImageurl(String imageurl) { this.imageurl = imageurl; }

            private String movieName;
            private int movieID;
            private String movieDescription;
            private int movieLength;
            private Cast movieCast;
            private int movieYear;
            private double imdbRating;
            private String originalName;
            private String imageurl;
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
                return this.castActors;
            }

            private String castDirector;
            private ArrayList<String> castActors;
        }
    }

    


}

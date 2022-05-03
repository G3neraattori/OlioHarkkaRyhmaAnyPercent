package com.example.olioharkkaryhmaanypercent;

public class Movie {   // Movie class contains information on one unique movie
    // Constructor with parameters for movie info
    public Movie(String movieName, int movieID, String movieDescription, String originalName, MainActivity.MovieManager.Cast movieCast, int movieLength, int movieYear, String imageurl) {
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

    public String getMovieName() { return this.movieName; }
    public String getOriginalName() { return this.originalName; }
    public int getMovieID() { return this.movieID; }
    public String getMovieDescription() { return this.movieDescription; }
    public MainActivity.MovieManager.Cast getMovieCast() { return this.movieCast; }
    public int getMovieLength() { return this.movieLength; }
    public int getMovieYear() { return this.movieYear; }
    public double getImdbRating() { return this.imdbRating; }
    public String getImageurl() { return this.imageurl; }
    public int getPersonalRating() { return this.personalRating; }

    private void setMovieName(String movieName) { this.movieName = movieName; }
    private void setMovieID(int movieID) { this.movieID = movieID; }
    private void setMovieDescription(String movieDescription) { this.movieName = movieDescription; }
    private void setMovieCast(MainActivity.MovieManager.Cast movieCast) { this.movieCast = movieCast; }
    private void setMovieLength(int movieLength) { this.movieLength = movieLength; }
    public void setImdbRating(double imdbRating) { this.imdbRating = imdbRating; }
    private void setOriginalName(String originalName) { this.originalName = originalName; }
    private void setImageurl(String imageurl) { this.imageurl = imageurl; }
    public void setpersonalRating(int personalRating) { this.personalRating = personalRating; }

    private String movieName;
    private int movieID;
    private String movieDescription;
    private int movieLength;
    private MainActivity.MovieManager.Cast movieCast;
    private int movieYear;
    private double imdbRating;
    private String originalName;
    private String imageurl;
    private int personalRating;
}
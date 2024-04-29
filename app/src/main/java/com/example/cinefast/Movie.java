package com.example.cinefast;

public class Movie {
    private String name;
    private String genre;
    private String duration;
    private int posterResId;
    private String trailerUrl;
    private boolean isComingSoon;

    public Movie(String name, String genre, String duration, int posterResId, String trailerUrl, boolean isComingSoon) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.posterResId = posterResId;
        this.trailerUrl = trailerUrl;
        this.isComingSoon = isComingSoon;
    }

    public String getName() { return name; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public int getPosterResId() { return posterResId; }
    public String getTrailerUrl() { return trailerUrl; }
    public boolean isComingSoon() { return isComingSoon; }
}

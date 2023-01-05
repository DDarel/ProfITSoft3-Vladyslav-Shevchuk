package org.example.task2;

import java.time.Instant;

public class Movie {
    @Property(name = "movieName")
    private String movieName;
    @Property(name = "movieReleaseDate", timeFormat = "dd.MM.yyyy HH:mm")
    private Instant releaseDate;
    @Property(name = "movieRating")
    private int movieRating;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(int movieRating) {
        this.movieRating = movieRating;
    }

    @Override
    public String toString() {
        return "Movie " +
                "name=" + movieName +
                ", rating=" + movieRating +
                ", release date=" + releaseDate;
    }
}

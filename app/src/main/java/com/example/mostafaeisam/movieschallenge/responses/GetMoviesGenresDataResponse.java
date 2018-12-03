package com.example.mostafaeisam.movieschallenge.responses;

import com.example.mostafaeisam.movieschallenge.classes.Genres;
import com.example.mostafaeisam.movieschallenge.classes.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMoviesGenresDataResponse {
    @SerializedName("genres")
    private List<Genres> genres;

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

}

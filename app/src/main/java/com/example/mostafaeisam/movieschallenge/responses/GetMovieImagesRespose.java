package com.example.mostafaeisam.movieschallenge.responses;

import com.example.mostafaeisam.movieschallenge.classes.Backdrops;
import com.example.mostafaeisam.movieschallenge.classes.Posters;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by messam on 9/20/2018.
 */

public class GetMovieImagesRespose {
    private int id;
    @SerializedName("backdrops")
    private List<Backdrops> backdrops;
    @SerializedName("posters")
    private List<Posters> posters;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Backdrops> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Backdrops> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Posters> getPosters() {
        return posters;
    }

    public void setPosters(List<Posters> posters) {
        this.posters = posters;
    }
}

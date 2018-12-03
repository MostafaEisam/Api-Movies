package com.example.mostafaeisam.movieschallenge.responses;

import com.example.mostafaeisam.movieschallenge.classes.Results;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTrailerResponse {
    private int id;
    @SerializedName("results")
    private List<Results> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}

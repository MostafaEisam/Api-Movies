package com.example.mostafaeisam.movieschallenge.utilities;

import android.content.Context;

import com.example.mostafaeisam.movieschallenge.classes.Movie;
import com.example.mostafaeisam.movieschallenge.responses.GetMoviesDataResponse;

import java.util.ArrayList;

/**
 * Created by messam on 9/18/2018.
 */

public class ManageFavoriteMoviesList {

    public static void addMoviesIdtoFivorite(Context context, Movie movie){
        ArrayList<Movie> genresArrayList = getFavoriteMovies(context);
        genresArrayList.add(movie);

        SavePrefs<GetMoviesDataResponse> mMatchesListResponseSavePrefs = new SavePrefs<>(context,GetMoviesDataResponse.class);

        GetMoviesDataResponse mMatchesListResponse = new GetMoviesDataResponse();
        mMatchesListResponse.setResults(genresArrayList);

        mMatchesListResponseSavePrefs.save(mMatchesListResponse);

    }

    public static ArrayList<Movie> getFavoriteMovies(Context context){
        if (context==null)
            return new ArrayList<>();
        // 1- First Step
        SavePrefs<GetMoviesDataResponse> movieDetailsSavePrefs = new SavePrefs<>(context,GetMoviesDataResponse.class);
        GetMoviesDataResponse getMovieDetails = movieDetailsSavePrefs.load();
        if (getMovieDetails==null)
        {
            return new ArrayList<>();
        }
        else
        {
            return (ArrayList<Movie>) getMovieDetails.getResults();
        }
    }

    public static void removeMoviesIdtoFivorite(Context context,Movie movie){
        ArrayList<Movie> movieArrayList = getFavoriteMovies(context);
        for (int i = 0 ; i< movieArrayList.size(); i++ ){
            if (movie.getId()==(movieArrayList.get(i).getId()))
            {
                movieArrayList.remove(i);
                break;
            }
        }

        SavePrefs<GetMoviesDataResponse> movieDetailsSavePrefs = new SavePrefs<>(context,GetMoviesDataResponse.class);

        GetMoviesDataResponse movieDetails = new GetMoviesDataResponse();
        movieDetails.setResults(movieArrayList);
        movieDetailsSavePrefs.save(movieDetails);
    }
}

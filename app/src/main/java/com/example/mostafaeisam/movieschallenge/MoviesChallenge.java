package com.example.mostafaeisam.movieschallenge;

import android.app.Application;

import com.example.mostafaeisam.movieschallenge.classes.Backdrops;

import java.util.List;

/**
 * Created by messam on 9/20/2018.
 */

public class MoviesChallenge  extends Application {

    private static  List<Backdrops> mImagesList;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static List<Backdrops> getmImagesList() {
        return mImagesList;
    }

    public static void setmImagesList(List<Backdrops> mImagesList) {
        MoviesChallenge.mImagesList = mImagesList;
    }
}

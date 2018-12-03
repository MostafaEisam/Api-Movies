package com.example.mostafaeisam.movieschallenge.utilities;

import android.content.Context;
import android.content.Intent;

import com.example.mostafaeisam.movieschallenge.activites.FullScreenImage;
import com.example.mostafaeisam.movieschallenge.activites.MainActivity;
import com.example.mostafaeisam.movieschallenge.activites.MovieDetailsActivity;
import com.example.mostafaeisam.movieschallenge.activites.SplashActivity;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by messam on 9/20/2018.
 */

public class UiManager {

    public static void startMainActivityWithoutExtrasOrFlags(Context context)
    {
        //Intent
        Intent mainActivity = new Intent(context,MainActivity.class);
        startActivity(context,mainActivity,null);
    }

    public static void startFullScreenImageActivity(Context context,String position)
    {
        //Intent
        Intent fullScreenImageActivity = new Intent(context,FullScreenImage.class);
        fullScreenImageActivity.putExtra("position",position);
        startActivity(context,fullScreenImageActivity,null);
    }

    public static void startMovieDetailsActivity(Context context,String Obj)
    {
        //Intent
        Intent fullScreenImageActivity = new Intent(context,MovieDetailsActivity.class);
        fullScreenImageActivity.putExtra("Obj",Obj);
        startActivity(context,fullScreenImageActivity,null);
    }

}

package com.example.mostafaeisam.movieschallenge.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mostafaeisam.movieschallenge.MoviesChallenge;
import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.adapters.FullScreenMovieImagesAdapter;
import com.example.mostafaeisam.movieschallenge.classes.Backdrops;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenImage extends AppCompatActivity {
    @BindView(R.id.rv_fullScreenImage)
    RecyclerView mRvFullScreenImage;
    private FullScreenMovieImagesAdapter mRvMovieImageAdapter;
    List<Backdrops> mImagesList;
    String mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ButterKnife.bind(this);

        mImagesList = new ArrayList<>();
        mImagesList = MoviesChallenge.getmImagesList();

        mPosition = getIntent().getStringExtra("position");

        mRvFullScreenImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRvMovieImageAdapter = new FullScreenMovieImagesAdapter(FullScreenImage.this, mImagesList);
        mRvFullScreenImage.setAdapter(mRvMovieImageAdapter);
        mRvFullScreenImage.setVerticalScrollBarEnabled(false);
        mRvFullScreenImage.setNestedScrollingEnabled(false);
        mRvFullScreenImage.smoothScrollToPosition(Integer.parseInt(mPosition));
    }
}

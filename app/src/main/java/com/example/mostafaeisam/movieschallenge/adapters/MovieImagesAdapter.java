package com.example.mostafaeisam.movieschallenge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.mostafaeisam.movieschallenge.MoviesChallenge;
import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.utilities.Constants;
import com.example.mostafaeisam.movieschallenge.classes.Backdrops;
import com.example.mostafaeisam.movieschallenge.utilities.UiManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by messam on 9/20/2018.
 */

public class MovieImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Backdrops> mImagesList;
    private LayoutInflater mInflater;


    public MovieImagesAdapter(Context context, List<Backdrops> mImagesList) {
        this.mContext = context;
        this.mImagesList = mImagesList;
        this.mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.row_items_movie_images, parent, false);

        return new MovieImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MovieImagesHolder movieImagesHolder = (MovieImagesHolder) holder;

        movieImagesHolder.mProgressBar.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(Constants.BASE_IMAGE_URL + mImagesList.get(position).getFile_path())
                .into(movieImagesHolder.mIvMovieImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        movieImagesHolder.mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        movieImagesHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoviesChallenge.setmImagesList(mImagesList);
                /*
                Intent fullScreenImage = new Intent(mContext, FullScreenImage.class);
                fullScreenImage.putExtra("position",String.valueOf(position));
                mContext.startActivity(fullScreenImage);
                */
                UiManager.startFullScreenImageActivity(mContext,String.valueOf(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public class MovieImagesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movieImage)
        ImageView mIvMovieImage;
        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;


        public MovieImagesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}

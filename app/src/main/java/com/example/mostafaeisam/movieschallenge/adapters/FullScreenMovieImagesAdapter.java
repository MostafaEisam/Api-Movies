package com.example.mostafaeisam.movieschallenge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.utilities.Constants;
import com.example.mostafaeisam.movieschallenge.classes.Backdrops;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by messam on 9/20/2018.
 */

public class FullScreenMovieImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Backdrops> mImagesList;
    private Context mContext;
    private LayoutInflater mInflater;


    public FullScreenMovieImagesAdapter(Context context, List<Backdrops> mImagesList) {
        this.mContext = context;
        this.mImagesList = mImagesList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.row_images_full_screen, parent, false);

        return new FullScreenMovieImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final FullScreenMovieImagesHolder fullScreenMovieImagesHolder = (FullScreenMovieImagesHolder) holder;
        final String imageUrl = Constants.BASE_FULL_SCREEN_IMAGE_URL + mImagesList.get(position).getFile_path();

        fullScreenMovieImagesHolder.mProgressBar.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(imageUrl)
                .into(fullScreenMovieImagesHolder.mIvMovieImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        fullScreenMovieImagesHolder.mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public class FullScreenMovieImagesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movieImage)
        ImageView mIvMovieImage;
        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;

        public FullScreenMovieImagesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

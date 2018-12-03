package com.example.mostafaeisam.movieschallenge.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.mostafaeisam.movieschallenge.activites.MovieDetailsActivity;
import com.example.mostafaeisam.movieschallenge.utilities.Constants;
import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.classes.Genres;
import com.example.mostafaeisam.movieschallenge.classes.Movie;
import com.example.mostafaeisam.movieschallenge.utilities.UiManager;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Movie> mMoviesList;
    private List<Genres> mGenresList;
    private Context mContext;
    private int numOfViewHolder;

    public MoviesAdapter(Context context, List<Movie> moviesList , List<Genres> genresList) {
        this.mContext = context;
        this.mMoviesList = moviesList;
        this.mGenresList = genresList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        if (getNumOfViewHolder() == Constants.ID_DISCOVER) {
            ViewGroup discover = (ViewGroup) mInflater.inflate(R.layout.row_items_discover, parent, false);
            return new MoviesHolderDiscover(discover);

        } else if (getNumOfViewHolder() == Constants.ID_INTHEATERS) {
            ViewGroup inTheaters = (ViewGroup) mInflater.inflate(R.layout.row_items_intheaters, parent, false);
            return new MoviesHolderInTheaters(inTheaters);
        } else if (getNumOfViewHolder() == Constants.ID_GENRES) {
            ViewGroup genres = (ViewGroup) mInflater.inflate(R.layout.row_items_genres, parent, false);
            return new MoviesHolderGenres(genres);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (getNumOfViewHolder() == Constants.ID_DISCOVER) {
            final Movie movie = mMoviesList.get(position);
            final MoviesHolderDiscover moviesHolderdiscover = (MoviesHolderDiscover) holder;

            moviesHolderdiscover.mProgressBar.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(movie.getOriginal_name())) {
                moviesHolderdiscover.mTvMovieTitle.setText(movie.getOriginal_title());
            }
            else
                {
                moviesHolderdiscover.mTvMovieTitle.setText(movie.getOriginal_name());
            }
            moviesHolderdiscover.mTvNumOfLikes.setText(String.valueOf(movie.getVote_count()));

            Picasso.get()
                    .load(Constants.BASE_IMAGE_URL + movie.getBackdrop_path())
                    .fit()
                    .into(moviesHolderdiscover.mIvMovieBackground, new Callback() {
                        @Override
                        public void onSuccess() {
                            moviesHolderdiscover.mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

        }
        else if (getNumOfViewHolder() == Constants.ID_INTHEATERS)
        {
            final MoviesHolderInTheaters moviesHolderInTheaters = (MoviesHolderInTheaters) holder;
            Movie movie = mMoviesList.get(position);
            moviesHolderInTheaters.mProgressBar.setVisibility(View.VISIBLE);
            if (movie.getPoster_path()==null)
            {
                moviesHolderInTheaters.mProgressBar.setVisibility(View.GONE);
                Picasso.get()
                        .load(Constants.BASE_IMAGE_URL + movie.getPoster_path())
                        .fit()
                        .placeholder(R.drawable.holder)
                        .into(moviesHolderInTheaters.mIvMovieBackground, new Callback() {
                            @Override
                            public void onSuccess() {
                                //moviesHolderInTheaters.mProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

            }else {
                Picasso.get()
                        .load(Constants.BASE_IMAGE_URL + movie.getPoster_path())
                        .fit()
                        .into(moviesHolderInTheaters.mIvMovieBackground, new Callback() {
                            @Override
                            public void onSuccess() {
                                moviesHolderInTheaters.mProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
        }
        else if (getNumOfViewHolder() == Constants.ID_GENRES)
        {
            MoviesHolderGenres moviesHolderGenres = (MoviesHolderGenres) holder;
            Genres genres = mGenresList.get(position);
            moviesHolderGenres.mIvBackground.setBackgroundResource(genres.getImage());
            moviesHolderGenres.mTvMovieTitle.setText(genres.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (getNumOfViewHolder() == Constants.ID_GENRES){
            return mGenresList.size() ;
        }else {
            return mMoviesList.size() ;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MoviesHolderDiscover extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_background)
        RelativeLayout mRvBackground;
        @BindView(R.id.tv_movieTitle)
        TextView mTvMovieTitle;
        @BindView(R.id.tv_numOfLikes)
        TextView mTvNumOfLikes;
        @BindView(R.id.iv_movieBackground)
        ImageView mIvMovieBackground;
        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;

        public MoviesHolderDiscover(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = mMoviesList.get(getAdapterPosition());
                    passData(movie);
                }

                private void passData(Movie movie) {
                    /*
                    Intent movieDetailsActivity = new Intent(mContext,MovieDetailsActivity.class);
                    //Convert Obj To String
                    movieDetailsActivity.putExtra("Obj", new Gson().toJson(movie));
                    mContext.startActivity(movieDetailsActivity);
                    */
                    UiManager.startMovieDetailsActivity(mContext,new Gson().toJson(movie));
                }
            });
        }
    }


    public class MoviesHolderInTheaters extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movieBackground)
        ImageView mIvMovieBackground;
        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;

        public MoviesHolderInTheaters(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MoviesHolderGenres extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_background)
        ImageView mIvBackground;
        @BindView(R.id.tv_movieTitle)
        TextView mTvMovieTitle;

        public MoviesHolderGenres(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



    public int getNumOfViewHolder() {
        return numOfViewHolder;
    }

    public void setNumOfViewHolder(int numOfViewHolder) {
        this.numOfViewHolder = numOfViewHolder;
    }
}

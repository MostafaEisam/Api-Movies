package com.example.mostafaeisam.movieschallenge.activites;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaeisam.movieschallenge.adapters.CastAdapter;
import com.example.mostafaeisam.movieschallenge.adapters.MovieImagesAdapter;
import com.example.mostafaeisam.movieschallenge.classes.Crew;
import com.example.mostafaeisam.movieschallenge.classes.GuestSessionId;
import com.example.mostafaeisam.movieschallenge.classes.Backdrops;
import com.example.mostafaeisam.movieschallenge.responses.GetMovieImagesRespose;
import com.example.mostafaeisam.movieschallenge.utilities.ManageFavoriteMoviesList;
import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.classes.Cast;
import com.example.mostafaeisam.movieschallenge.utilities.Constants;
import com.example.mostafaeisam.movieschallenge.classes.Movie;
import com.example.mostafaeisam.movieschallenge.classes.Results;
import com.example.mostafaeisam.movieschallenge.responses.GetAllCastResponse;
import com.example.mostafaeisam.movieschallenge.responses.GetMovieDetailsResponse;
import com.example.mostafaeisam.movieschallenge.responses.GetTrailerResponse;
import com.example.mostafaeisam.movieschallenge.services.RequestListener;
import com.example.mostafaeisam.movieschallenge.services.ServiceFactory;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity implements RequestListener,View.OnClickListener {

    @BindView(R.id.iv_poster)
    ImageView mIvPoster;
    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.tv_overview)
    TextView mTvOverview;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_rating)
    TextView mTvRating;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.tv_release_date)
    TextView mTvReleaseDate;
    @BindView(R.id.tv_runtime)
    TextView mTvRuntime;
    @BindView(R.id.tv_genres)
    TextView mTvGenres;
    @BindView(R.id.bt_play)
    ImageButton mBtPlay;
    @BindView(R.id.bt_Save)
    ImageButton mBtSave;
    @BindView(R.id.bt_share)
    ImageButton mBtShare;
    @BindView(R.id.iv_directorProfilePicture)
    ImageView mIvDirectorProfilePicture;
    @BindView(R.id.tv_directorName)
    TextView mTvDirectorName;
    @BindView(R.id.tv_directorJob)
    TextView mTvDirectorJob;

    private String mMovieId;
    private List<Results> mResultsTrailer;
    private Movie mMovie;
    private List<Movie> mMovieListSavedInFavorite;
    private List<Integer> mMoviesIdsList;
    private List<Cast> mAllCastList;
    private List<Crew> mAllCrewList;

    private CastAdapter mRvAdapter;

    private LinearLayoutManager mlinearLayoutManager;
    private boolean mGetTrailerFromPlayButton = false;

    @BindView(R.id.rv_cast)
    RecyclerView mRvCast;

    @BindView(R.id.bt_showMore)
    Button mBtShowMore;

    @BindView(R.id.rv_movieImages)
    RecyclerView mRvMovieImages;

    private String mGuestSessionIdValue;
    private int mNumOfPostRateResponseStatusCode;

    private List<Backdrops> mMovieImages;
    private MovieImagesAdapter mRvMovieImageAdapter;

/*
    RateMovie Commented Beacause it need to more work on it
    by calling it on SplashActivity and everyTime look on device time if it equal
    expires_at or not
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        initView();
        //Banner Ads
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        mRatingBar.setRating(5);
        mProgressBar.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        //Convert String To Obj
        mMovie = gson.fromJson(getIntent().getStringExtra("Obj"), Movie.class);
        mMovieId = String.valueOf(mMovie.getId());

        if (!mMovieListSavedInFavorite.isEmpty()) {

            for (int x = 0; x < mMovieListSavedInFavorite.size(); x++) {
                int movieId = mMovieListSavedInFavorite.get(x).getId();
                mMoviesIdsList.add(movieId);
            }

            if (mMoviesIdsList.contains(Integer.parseInt(mMovieId))) {
                mBtSave.setBackgroundResource(R.drawable.ic_action_checked);

            } else {
                mBtSave.setBackgroundResource(R.drawable.ic_action_add);
            }
        }

        getMovieDetails();
        getAllCast();
        getAllMovieImages();
        //getGuestSessionId();




    }

    private void getAllMovieImages() {
        String url = Constants.BASE_MOVIE_DETAILS_AND_CAST_URL + mMovieId + "/images?" + Constants.API_KEY;
        ServiceFactory.getData(this, url, Constants.ID_MOVIE_IMAGES, this);
    }

    /*
        private void getGuestSessionId() {
            String url = Constants.BASE_GUEST_SESSION_URL + Constants.API_KEY_MOVIE_DETAILS_AND_CAST_AND_UPCOMING;
            ServiceFactory.getData(this,url,Constants.ID_GUEST_SESSION,this);
        }
    */

    private void initView() {
        ButterKnife.bind(this);
        mResultsTrailer = new ArrayList<>();
        mMovieListSavedInFavorite = ManageFavoriteMoviesList.getFavoriteMovies(this);
        mMoviesIdsList = new ArrayList<>();
        mAllCastList = new ArrayList<>();
        mlinearLayoutManager = new LinearLayoutManager(this);
        mAllCrewList = new ArrayList<>();
        mMovieImages = new ArrayList<>();
        // Used To Make RecyclerView More Speed
        mRvCast.setNestedScrollingEnabled(false);

        mBtPlay.setOnClickListener(this);
        mBtSave.setOnClickListener(this);
        mBtShare.setOnClickListener(this);
        mBtShowMore.setOnClickListener(this);

    }

    private void getAllCast() {
        String url = Constants.BASE_MOVIE_DETAILS_AND_CAST_URL + mMovieId + "/credits?" + Constants.API_KEY_MOVIE_DETAILS_AND_CAST_AND_UPCOMING;
        ServiceFactory.getData(this, url, Constants.ID_ALL_CAST, this);
    }

    private void getMovieDetails() {
        String url = Constants.BASE_MOVIE_DETAILS_AND_CAST_URL + mMovieId + "?" + Constants.API_KEY_MOVIE_DETAILS_AND_CAST_AND_UPCOMING;
        ServiceFactory.getData(this, url, Constants.ID_MOVIE_DETAILS, this);
    }

    private void addToFavoriteAndSetButtonView() {
        mMovieListSavedInFavorite = ManageFavoriteMoviesList.getFavoriteMovies(MovieDetailsActivity.this);
        if (mMovieListSavedInFavorite.isEmpty()) {
            ManageFavoriteMoviesList.addMoviesIdtoFivorite(MovieDetailsActivity.this, mMovie);
            Toast.makeText(MovieDetailsActivity.this, "added", Toast.LENGTH_SHORT).show();
            mBtSave.setBackgroundResource(R.drawable.ic_action_checked);
        } else {
            for (int x = 0; x < mMovieListSavedInFavorite.size(); x++) {
                int movieId = mMovieListSavedInFavorite.get(x).getId();
                mMoviesIdsList.add(movieId);
            }

            if (mMoviesIdsList.contains(Integer.parseInt(mMovieId))) {
                Toast.makeText(MovieDetailsActivity.this, "removed", Toast.LENGTH_SHORT).show();
                ManageFavoriteMoviesList.removeMoviesIdtoFivorite(MovieDetailsActivity.this, mMovie);
                mBtSave.setBackgroundResource(R.drawable.ic_action_add);

            } else {
                ManageFavoriteMoviesList.addMoviesIdtoFivorite(MovieDetailsActivity.this, mMovie);
                mBtSave.setBackgroundResource(R.drawable.ic_action_checked);
                Toast.makeText(MovieDetailsActivity.this, "added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getTrailer() {
        String url = Constants.BASE_MOVIE_DETAILS_AND_CAST_URL + mMovieId + "/videos?" + Constants.API_KEY_MOVIE_DETAILS_AND_CAST_AND_UPCOMING;
        ServiceFactory.getData(this, url, Constants.ID_TRAILER, this);
    }

    @Override
    public void onSuccess(Object object, int idRequest) {
        mProgressBar.setVisibility(View.GONE);
        if (idRequest == Constants.ID_TRAILER) {

            GetTrailerResponse trailer = new Gson().fromJson((String) object, GetTrailerResponse.class);
            mResultsTrailer.addAll(trailer.getResults());

            if (isGetTrailerFromPlayButton()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + mResultsTrailer.get(0).getKey())));
                setGetTrailerFromPlayButton(false);
            } else {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_TITLE, mMovie.getOriginal_name() + " Trailer");
                share.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + mResultsTrailer.get(0).getKey());
                startActivity(Intent.createChooser(share, "Share Movie Trailer To...."));

            }

        } else if (idRequest == Constants.ID_MOVIE_DETAILS) {

            GetMovieDetailsResponse movieDetails = new Gson().fromJson((String) object, GetMovieDetailsResponse.class);
            mTvOverview.setText(movieDetails.getOverview());
            mTvRuntime.setText(String.valueOf(movieDetails.getRuntime()) + " min");
            mTvReleaseDate.setText(movieDetails.getRelease_date());
            mTvRating.setText(String.valueOf(movieDetails.getVote_average()) + "/10");
            mTvGenres.setText(movieDetails.getGenres().get(0).getName());
            Picasso.get()
                    .load(Constants.BASE_IMAGE_URL + movieDetails.getPoster_path())
                    .fit()
                    .into(mIvPoster);

        } else if (idRequest == Constants.ID_ALL_CAST) {
            GetAllCastResponse allCast = new Gson().fromJson((String) object, GetAllCastResponse.class);
            mAllCastList.addAll(allCast.getCast());
            mAllCrewList.addAll(allCast.getCrew());

            for (int z = 0; z < mAllCrewList.size(); z++) {
                String job = mAllCrewList.get(z).getJob();
                if (job.equals("Director")) {
                    mTvDirectorJob.setText(job);
                    mTvDirectorName.setText(mAllCrewList.get(z).getName());
                    Picasso.get()
                            .load(Constants.BASE__ACTOR_PROFILE_PICTURE_URL + mAllCrewList.get(z).getProfile_path())
                            .fit()
                            .into(mIvDirectorProfilePicture);
                }
            }
            setCastRecyclerViewAdapter();
        } else if (idRequest == Constants.ID_GUEST_SESSION) {
            GuestSessionId guestSessionIdAllData = new Gson().fromJson((String) object, GuestSessionId.class);
            mGuestSessionIdValue = guestSessionIdAllData.getGuest_session_id();
            /*
            String url = "https://api.themoviedb.org/3/movie/346910/rating?api_key=4b02956659b67c4ef65ebb3deb6bff17"+"&guest_session_id="+mGuestSessionIdValue;
            ServiceFactory.post(this,url,10,this);
            */
        } else if (idRequest == Constants.ID_MOVIE_IMAGES) {
            GetMovieImagesRespose getMovieImagesRespose = new Gson().fromJson((String) object, GetMovieImagesRespose.class);
            mMovieImages.addAll(getMovieImagesRespose.getBackdrops());
            setHorizontalMovieImages();
        }
        /*
        else if (idRequest == 10){
            PostRateResponse  postRateResponse = new Gson().fromJson((String) object, PostRateResponse.class);
            mNumOfPostRateResponseStatusCode = postRateResponse.getStatus_code();
            if (mNumOfPostRateResponseStatusCode == 1){
                Toast.makeText(this, "Rated For First Time", Toast.LENGTH_SHORT).show();
            }else if (mNumOfPostRateResponseStatusCode == 12){
                Toast.makeText(this, "Your Rate Updated", Toast.LENGTH_SHORT).show();
            }
        }
        */
    }

    private void setHorizontalMovieImages() {
        // false to make recyclerView swipe to left
        // true to make recyclerView swipe to Right
        mRvMovieImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRvMovieImageAdapter = new MovieImagesAdapter(MovieDetailsActivity.this, mMovieImages);
        mRvMovieImages.setAdapter(mRvMovieImageAdapter);
        mRvMovieImages.setVerticalScrollBarEnabled(false);
        mRvMovieImages.setNestedScrollingEnabled(false);
    }

    private void setCastRecyclerViewAdapter() {
        mRvCast.setLayoutManager(mlinearLayoutManager);
        mRvCast.setHasFixedSize(true);
        mRvAdapter = new CastAdapter(this, mAllCastList);
        mRvAdapter.setShowMoreClicked(false);
        mRvCast.setAdapter(mRvAdapter);
    }

    private void updateDataAfterClickShowMore() {
        mRvCast.setLayoutManager(mlinearLayoutManager);
        mRvCast.setHasFixedSize(true);
        mRvAdapter = new CastAdapter(this, mAllCastList);
        mRvAdapter.setShowMoreClicked(true);
        mRvCast.setAdapter(mRvAdapter);
    }

    @Override
    public void onFailure(int errorCode, int idRequest) {
        Toast.makeText(this, "onFailure", Toast.LENGTH_SHORT).show();
    }

    public boolean isGetTrailerFromPlayButton() {
        return mGetTrailerFromPlayButton;
    }

    public void setGetTrailerFromPlayButton(boolean getTrailerFromPlayButton) {
        this.mGetTrailerFromPlayButton = getTrailerFromPlayButton;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_play:
                setGetTrailerFromPlayButton(true);
                getTrailer();
                break;
            case R.id.bt_Save:
                addToFavoriteAndSetButtonView();
                break;
            case R.id.bt_share:
                setGetTrailerFromPlayButton(false);
                getTrailer();
                break;
            case R.id.bt_showMore:
                mBtShowMore.setVisibility(View.GONE);
                mRvAdapter.notifyDataSetChanged();
                updateDataAfterClickShowMore();
                break;
            default:
        }
    }
}

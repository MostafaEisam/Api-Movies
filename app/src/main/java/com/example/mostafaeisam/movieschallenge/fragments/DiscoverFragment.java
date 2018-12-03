package com.example.mostafaeisam.movieschallenge.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.mostafaeisam.movieschallenge.utilities.Constants;
import com.example.mostafaeisam.movieschallenge.adapters.MoviesAdapter;
import com.example.mostafaeisam.movieschallenge.classes.Genres;
import com.example.mostafaeisam.movieschallenge.responses.GetMoviesDataResponse;
import com.example.mostafaeisam.movieschallenge.classes.Movie;
import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.responses.GetMoviesGenresDataResponse;
import com.example.mostafaeisam.movieschallenge.services.RequestListener;
import com.example.mostafaeisam.movieschallenge.services.ServiceFactory;
import com.example.mostafaeisam.movieschallenge.utilities.Utilities;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DiscoverFragment extends Fragment implements RequestListener,View.OnClickListener {
    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerViewMovies;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    MoviesAdapter mRvAdapter;
    @BindView(R.id.progressBarBottom)
    ProgressBar mProgressBarBottom;
    @BindView(R.id.bt_topLists)
    Button mBtTopLists;
    @BindView(R.id.bt_genres)
    Button mBtGenres;
    @BindView(R.id.bt_inTheaters)
    Button mBtInTheaters;
    @BindView(R.id.bt_upcoming)
    Button mBtUpcoming;
    @BindView(R.id.bt_retry)
    Button mBtRetry;
    @BindView(R.id.rv_DiscoverFragment)
    RelativeLayout mRvDiscoverFragment;

    private int mNumOfScrollType;
    private List<Movie> mInTheatersList;
    private List<Genres> mGenresList;
    private List<Movie> mMoviesList;
    private LinearLayoutManager mlinearLayoutManager;
    private boolean loading = true;
    private int mPastVisiblesItems, mVisibleItemCount, mtotalItemCount;
    private int mDiscoverPageNum = 1;
    private int mInTheatersPageNum = 1;
    private int mUpComingPageNum = 1;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        initView(v);


        if (Utilities.hasInternetConnection(getActivity())) {
            mRvDiscoverFragment.setVisibility(View.VISIBLE);
            mBtRetry.setVisibility(View.GONE);

            mProgressBar.setVisibility(View.VISIBLE);
            getMoviesDataDiscover();
        } else {
            mRvDiscoverFragment.setVisibility(View.GONE);
            mBtRetry.setVisibility(View.VISIBLE);
        }

        topListButtonClikedDesign(R.drawable.red_radius_left_button, "#000000");
        setNumOfScrollType(Constants.ID_DISCOVER);

        mRecyclerViewMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {

                    if (getNumOfScrollType() == Constants.ID_DISCOVER) {
                        mVisibleItemCount = mlinearLayoutManager.getChildCount();
                        mtotalItemCount = mlinearLayoutManager.getItemCount();
                        mPastVisiblesItems = mlinearLayoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if ((mVisibleItemCount + mPastVisiblesItems) >= mtotalItemCount - 5) {
                                loading = false;
                                mDiscoverPageNum = ++mDiscoverPageNum;
                                mProgressBarBottom.setVisibility(View.VISIBLE);
                                getMoviesDataDiscover();
                            }
                        }
                    } else if (getNumOfScrollType() == Constants.ID_INTHEATERS) {
                        mVisibleItemCount = new GridLayoutManager(getActivity(), 3).getChildCount();
                        mtotalItemCount = new GridLayoutManager(getActivity(), 3).getItemCount();
                        mPastVisiblesItems = new GridLayoutManager(getActivity(), 3).findFirstVisibleItemPosition();

                        if (loading) {
                            if ((mVisibleItemCount + mPastVisiblesItems) >= mtotalItemCount - 5) {
                                loading = false;
                                mInTheatersPageNum = ++mInTheatersPageNum;
                                mProgressBarBottom.setVisibility(View.VISIBLE);
                                getMoviesDataInTheaters();
                            }
                        }
                    } else if (getNumOfScrollType() == Constants.ID_GENRES) {
                    }
                }
            }
        });


        return v;
    }

    private void getMoviesDataUpcoming() {
        String url = Constants.BASE_UPCOMING_URL + Constants.API_KEY_MOVIE_DETAILS_AND_CAST_AND_UPCOMING + Constants.LANGUAGE + "&page=" + mUpComingPageNum;
        ServiceFactory.getData(getActivity(), url, Constants.ID_UPCOMING, this);
    }

    private void setInTheatersData() {
        mRecyclerViewMovies.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerViewMovies.setHasFixedSize(true);
        mRvAdapter = new MoviesAdapter(getActivity(), mInTheatersList, null);
        mRvAdapter.setNumOfViewHolder(Constants.ID_INTHEATERS);
        mRecyclerViewMovies.setAdapter(mRvAdapter);
    }

    private void getMoviesDataDiscover() {
        String url = Constants.BASE_MOVIES_URL + mDiscoverPageNum + Constants.API_KEY;
        ServiceFactory.getData(getActivity(), url, Constants.ID_DISCOVER, this);
    }

    private void getMoviesDataInTheaters() {
        String date = "&primary_release_date.gte=" + Utilities.date(getActivity());
        String url = Constants.BASE_MOVIES_URL + mInTheatersPageNum + Constants.API_KEY + date;
        ServiceFactory.getData(getActivity(), url, Constants.ID_INTHEATERS, this);
    }

    private void getGenresData() {
        String url = Constants.BASE_GENRES_URL + Constants.API_KEY + Constants.LANGUAGE;
        ServiceFactory.getData(getActivity(), url, Constants.ID_GENRES, this);
    }

    private void initView(View v) {
        ButterKnife.bind(this, v);
        mMoviesList = new ArrayList<>();
        mGenresList = new ArrayList<>();
        mInTheatersList = new ArrayList<>();
        mlinearLayoutManager = new LinearLayoutManager(getActivity());
        setDiscoverData();
        mBtTopLists.setOnClickListener(this);
        mBtGenres.setOnClickListener(this);
        mBtInTheaters.setOnClickListener(this);
        mBtUpcoming.setOnClickListener(this);
        mBtRetry.setOnClickListener(this);
    }

    private void setDiscoverData() {
        mRecyclerViewMovies.setLayoutManager(mlinearLayoutManager);
        mRecyclerViewMovies.setHasFixedSize(true);
        mRvAdapter = new MoviesAdapter(getActivity(), mMoviesList, null);
        mRvAdapter.setNumOfViewHolder(Constants.ID_DISCOVER);
        mRecyclerViewMovies.setAdapter(mRvAdapter);
    }

    @Override
    public void onSuccess(final Object object, final int idRequest) {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerViewMovies.setVisibility(View.VISIBLE);

        if (idRequest == Constants.ID_DISCOVER) {
            final GetMoviesDataResponse moviesData = new Gson().fromJson((String) object, GetMoviesDataResponse.class);
            mMoviesList.addAll(moviesData.getResults());
            mProgressBarBottom.setVisibility(View.GONE);
            mRvAdapter.notifyDataSetChanged();

            loading = true;

        } else if (idRequest == Constants.ID_INTHEATERS) {
            mProgressBarBottom.setVisibility(View.GONE);
            final GetMoviesDataResponse moviesData = new Gson().fromJson((String) object, GetMoviesDataResponse.class);
            mInTheatersList.addAll(moviesData.getResults());
            mRvAdapter.notifyDataSetChanged();
            loading = true;

        } else if (idRequest == Constants.ID_GENRES) {

            int[] photos = {R.drawable.action, R.drawable.adventure, R.drawable.animation, R.drawable.comdey, R.drawable.crime, R.drawable.documentary,
                    R.drawable.drama, R.drawable.family, R.drawable.fantasy, R.drawable.history, R.drawable.horror, R.drawable.music, R.drawable.mystery
                    , R.drawable.romance, R.drawable.science_fiction, R.drawable.tv_movie, R.drawable.thriller, R.drawable.war, R.drawable.western};

            GetMoviesGenresDataResponse getMoviesGenresDataResponse = new Gson().fromJson((String) object, GetMoviesGenresDataResponse.class);
            mGenresList.addAll(getMoviesGenresDataResponse.getGenres());
            for (int i = 0; i < photos.length; i++) {
                mGenresList.get(i).setImage(photos[i]);
            }
            mRvAdapter.notifyDataSetChanged();
        } else if (idRequest == Constants.ID_UPCOMING) {
            Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
        }

    }

    private void setGenresMoviesData() {
        mRecyclerViewMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewMovies.setHasFixedSize(true);
        mRvAdapter = new MoviesAdapter(getActivity(), null, mGenresList);
        mRvAdapter.setNumOfViewHolder(Constants.ID_GENRES);
        mRecyclerViewMovies.setAdapter(mRvAdapter);
    }

    @Override
    public void onFailure(int errorCode, int idRequest) {
        getActivity().runOnUiThread(new Thread(new Runnable() {
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mProgressBarBottom.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
                getMoviesDataDiscover();
            }
        }));

    }


    private void topListButtonClikedDesign(int red_radius_left_button, String s) {
        mBtTopLists.setBackgroundResource(red_radius_left_button);
        mBtTopLists.setTextColor(Color.parseColor(s));
    }

    private void genresButtonClickedDesign(int red_no_radius_button, String s) {
        mBtGenres.setBackgroundResource(red_no_radius_button);
        mBtGenres.setTextColor(Color.parseColor(s));
    }

    private void inTheatersButtonClickedDesign(int red_no_radius_button, String s) {
        mBtInTheaters.setBackgroundResource(red_no_radius_button);
        mBtInTheaters.setTextColor(Color.parseColor(s));
    }

    private void upComingButtonClickedDesign(int red_radius_right_button, String s) {
        mBtUpcoming.setBackgroundResource(red_radius_right_button);
        mBtUpcoming.setTextColor(Color.parseColor(s));
    }


    public int getNumOfScrollType() {
        return mNumOfScrollType;
    }

    public void setNumOfScrollType(int numOfScrollType) {
        this.mNumOfScrollType = numOfScrollType;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_topLists:
                topListButtonClikedDesign(R.drawable.red_radius_left_button, "#000000");
                genresButtonClickedDesign(R.drawable.red_no_radius_without_left, "#FF3B2F");
                inTheatersButtonClickedDesign(R.drawable.red_no_corners, "#FF3B2F");
                upComingButtonClickedDesign(R.drawable.red_right_corners, "#FF3B2F");
                setNumOfScrollType(Constants.ID_DISCOVER);
                getMoviesDataDiscover();
                setDiscoverData();
                break;
            case R.id.bt_genres:
                genresButtonClickedDesign(R.drawable.red_no_radius_button, "#000000");
                topListButtonClikedDesign(R.drawable.red_left_corners, "#FF3B2F");
                inTheatersButtonClickedDesign(R.drawable.red_no_corners, "#FF3B2F");
                upComingButtonClickedDesign(R.drawable.red_right_corners, "#FF3B2F");

                setNumOfScrollType(Constants.ID_GENRES);

                if (mProgressBarBottom.getVisibility() == View.VISIBLE) {
                    mProgressBarBottom.setVisibility(View.GONE);
                }

                if (mGenresList.isEmpty()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    getGenresData();
                    setGenresMoviesData();
                } else {
                    setGenresMoviesData();
                }
                break;
            case R.id.bt_inTheaters:
                inTheatersButtonClickedDesign(R.drawable.red_no_radius_button, "#000000");
                topListButtonClikedDesign(R.drawable.red_left_corners, "#FF3B2F");
                genresButtonClickedDesign(R.drawable.red_no_radius_without_left, "#FF3B2F");
                upComingButtonClickedDesign(R.drawable.red_right_corners, "#FF3B2F");

                if (mInTheatersList.isEmpty()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                setNumOfScrollType(Constants.ID_INTHEATERS);
                getMoviesDataInTheaters();
                setInTheatersData();
                break;
            case R.id.bt_upcoming:
                upComingButtonClickedDesign(R.drawable.red_radius_right_button, "#000000");
                topListButtonClikedDesign(R.drawable.red_left_corners, "#FF3B2F");
                genresButtonClickedDesign(R.drawable.red_no_radius_without_left, "#FF3B2F");
                inTheatersButtonClickedDesign(R.drawable.red_no_corners, "#FF3B2F");
                getMoviesDataUpcoming();
                break;
            case R.id.bt_retry:
                if (Utilities.hasInternetConnection(getActivity())) {
                    mRvDiscoverFragment.setVisibility(View.VISIBLE);
                    mBtRetry.setVisibility(View.GONE);

                    mProgressBar.setVisibility(View.VISIBLE);
                    getMoviesDataDiscover();

                } else {
                    mRvDiscoverFragment.setVisibility(View.GONE);
                    mBtRetry.setVisibility(View.VISIBLE);
                }
                break;
            default:

        }
    }
}

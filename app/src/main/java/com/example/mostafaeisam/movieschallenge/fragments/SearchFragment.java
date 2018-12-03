package com.example.mostafaeisam.movieschallenge.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.adapters.MoviesAdapter;
import com.example.mostafaeisam.movieschallenge.utilities.Constants;
import com.example.mostafaeisam.movieschallenge.classes.Movie;
import com.example.mostafaeisam.movieschallenge.responses.GetMoviesDataResponse;
import com.example.mostafaeisam.movieschallenge.services.RequestListener;
import com.example.mostafaeisam.movieschallenge.services.ServiceFactory;
import com.example.mostafaeisam.movieschallenge.utilities.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchFragment extends Fragment implements RequestListener {

    @BindView(R.id.et_inputSearch)
    EditText mEtInputSearch;
    @BindView(R.id.bt_search)
    ImageButton mBtSearch;
    @BindView(R.id.rv_searchResult)
    RecyclerView mRecyclerViewSearchResult;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private List<Movie> mMovieList;
    private MoviesAdapter mRecyclerViewAdapter;

    private boolean mLoading = true;
    private int mPastVisiblesItems, mVisibleItemCount, mTotalItemCount;

    private int mSearchPageNum = 1;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);

        mEtInputSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        mMovieList.clear();
                        Utilities.hideKeyboardFrom(getActivity(), v);
                        mProgressBar.setVisibility(View.VISIBLE);
                        getMoviesSearchData();
                        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                return false;
            }
        });

        mRecyclerViewSearchResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    mVisibleItemCount = new LinearLayoutManager(getActivity()).getChildCount();
                    mTotalItemCount = new LinearLayoutManager(getActivity()).getItemCount();
                    mPastVisiblesItems = new LinearLayoutManager(getActivity()).findFirstVisibleItemPosition();

                    if (mLoading) {
                        if ((mVisibleItemCount + mPastVisiblesItems) >= mTotalItemCount - 5) {
                            mLoading = false;
                            mSearchPageNum = ++mSearchPageNum;
                            getMoviesSearchData();
                        }
                    }
                }
            }
        });

        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovieList.clear();
                Utilities.hideKeyboardFrom(getActivity(), v);
                mProgressBar.setVisibility(View.VISIBLE);
                getMoviesSearchData();
            }

        });
        return view;
    }

    private void initView(View view) {
        mMovieList = new ArrayList<>();
        ButterKnife.bind(this, view);
        mRecyclerViewSearchResult.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewSearchResult.setHasFixedSize(true);
        mRecyclerViewAdapter = new MoviesAdapter(getActivity(), mMovieList, null);
        mRecyclerViewSearchResult.setAdapter(mRecyclerViewAdapter);
    }


    private void getMoviesSearchData() {
        String query = mEtInputSearch.getText().toString();
        String url = Constants.BASE_SEARCH_URL + query + Constants.API_KEY + "&page=" + mSearchPageNum;
        ServiceFactory.getData(getActivity(), url, Constants.ID_SEARCH, this);
    }

    @Override
    public void onSuccess(Object object, int idRequest) {
        mProgressBar.setVisibility(View.GONE);
        GetMoviesDataResponse mAllMovies = new Gson().fromJson((String) object, GetMoviesDataResponse.class);
        mMovieList.addAll(mAllMovies.getResults());
        mRecyclerViewAdapter.notifyDataSetChanged();
        mLoading = true;
    }

    @Override
    public void onFailure(int errorCode, int idRequest) {
        Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
    }
}

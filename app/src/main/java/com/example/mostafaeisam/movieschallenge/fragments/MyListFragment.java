package com.example.mostafaeisam.movieschallenge.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.mostafaeisam.movieschallenge.utilities.ManageFavoriteMoviesList;
import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.adapters.MoviesAdapter;
import com.example.mostafaeisam.movieschallenge.utilities.Constants;
import com.example.mostafaeisam.movieschallenge.classes.Movie;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MyListFragment extends Fragment {

    private List<Movie> mMovieList;
    private MoviesAdapter mRvAdapter;
    @BindView(R.id.rv_myList)
    RecyclerView mRvMyList;
    @BindView(R.id.tv_no)
    TextView mTvNo;

    public MyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_list, container, false);
        ButterKnife.bind(this, v);
        mMovieList = ManageFavoriteMoviesList.getFavoriteMovies(getActivity());

        if (!mMovieList.isEmpty()){
            mTvNo.setVisibility(View.GONE);
            mRvMyList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRvMyList.setHasFixedSize(true);
            mRvAdapter = new MoviesAdapter(getActivity(), mMovieList,null);
            mRvAdapter.setNumOfViewHolder(Constants.ID_DISCOVER);
            mRvMyList.setAdapter(mRvAdapter);
        }else {
            mTvNo.setVisibility(View.VISIBLE);
            mRvMyList.setVisibility(View.GONE);
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMovieList = ManageFavoriteMoviesList.getFavoriteMovies(getActivity());
        if (!mMovieList.isEmpty()){
            mTvNo.setVisibility(View.GONE);
            mRvMyList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRvMyList.setHasFixedSize(true);
            mRvAdapter = new MoviesAdapter(getActivity(), mMovieList,null);
            mRvAdapter.setNumOfViewHolder(Constants.ID_DISCOVER);
            mRvMyList.setAdapter(mRvAdapter);
        }else {
            mTvNo.setVisibility(View.VISIBLE);
            mRvMyList.setVisibility(View.GONE);
        }
    }
}

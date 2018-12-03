package com.example.mostafaeisam.movieschallenge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.classes.Cast;
import com.example.mostafaeisam.movieschallenge.utilities.Constants;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by messam on 9/19/2018.
 */

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Cast> mAllCastList;
    private LayoutInflater mInflater;
    private boolean mShowMoreClicked = false;

    public CastAdapter(Context context, List<Cast> allCastList) {
        this.mContext = context;
        this.mAllCastList = allCastList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_items_cast, parent, false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CastHolder castHolder = (CastHolder) holder;
        Cast personOfCast = mAllCastList.get(position);

        castHolder.mTvActorName.setText(personOfCast.getName());
        castHolder.mTvActorNameInMovie.setText("as " + personOfCast.getCharacter());
        Picasso.get()
                .load(Constants.BASE_IMAGE_URL + personOfCast.getProfile_path())
                .fit()
                .into(castHolder.mIvActorProfilePicture);
    }

    @Override
    public int getItemCount() {
        if (isShowMoreClicked() == true){
            return mAllCastList.size();
        }else {
            return 4;
        }
    }

    public class CastHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_actorProfilePicture)
        ImageView mIvActorProfilePicture;
        @BindView(R.id.tv_actorName)
        TextView mTvActorName;
        @BindView(R.id.tv_actorNameInMovie)
        TextView mTvActorNameInMovie;

        public CastHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public boolean isShowMoreClicked() {
        return mShowMoreClicked;
    }

    public void setShowMoreClicked(boolean showMoreClicked) {
        this.mShowMoreClicked = showMoreClicked;
    }
}

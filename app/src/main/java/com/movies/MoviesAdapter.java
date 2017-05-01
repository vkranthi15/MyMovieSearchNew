package com.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<JSONObject> mitemList;
    private Context mContext;
    SharedPreferences mPref;
    SharedPreferences.Editor mPrefEdit;


    public MoviesAdapter(Context context, List<JSONObject> itemList) {
        this.mitemList = itemList;
        this.mContext = context;



    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_item, parent, false);
        return new ViewHolder(v);
    }

    public void addMore(List<JSONObject> jsonObjectList) {

        if (mitemList != null) {
            int extaraCount = mitemList.size() % 20;
            for (int i = extaraCount; i < jsonObjectList.size(); i++) {
                mitemList.add(jsonObjectList.get(i));
            }
            notifyDataSetChanged();

        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            JSONObject jsonObject = mitemList.get(position);

            holder.movieTitle.setText(jsonObject.getString("overview"));

            final String id = jsonObject.getString("id");


            String imageUrl = "https://image.tmdb.org/t/p/w500" + jsonObject.getString("poster_path");

            if (imageUrl != null ? imageUrl.trim().length() > 0 : false) {

                //https://image.tmdb.org/t/p/bwCeUHApBPF1JpDX7HpQPjo9Jyp.jpg

                Picasso.with(mContext).load(imageUrl).into(holder.profilepic);
            }


            holder.profilepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,MainActivity.class);
                    intent.putExtra("id",id);

                    mContext.startActivity(intent);
                }
            });



        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return mitemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilepic;
        public TextView movieTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            profilepic = (ImageView) itemView.findViewById(R.id.profile_image);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }
    }
}

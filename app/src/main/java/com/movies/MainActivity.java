package com.movies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.movies.webservices.WSResponnse;
import com.movies.webservices.WebServices;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import dtos.MovieDto;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    LinearLayout linearLayout;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        imgView = (ImageView) findViewById(R.id.imgBanner);
        linearLayout = (LinearLayout) findViewById(R.id.llContent);
        String id = getIntent().getExtras().getString("id");
        loadContent(id);
    }

    private void loadContent(String id) {
        WebServices webServices = new WebServices(mContext);

        RequestParams params = new RequestParams();
        params.put("api_key", "146c49c181283b2bf0f5fe701380d51e");

        String url = "https://api.themoviedb.org/3/movie/" + id;
        webServices.invokeWebService(params, url, new WSResponnse() {
            @Override
            public void onResponse(boolean success, String response) {
                // parse response here
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    HashMap<String, String> hashMap = new HashMap<String, String>();

                    String imageUrl ="https://image.tmdb.org/t/p/w500"+ jsonObject.getString("backdrop_path");
                    Picasso.with(mContext).load(imageUrl).into(imgView);

                    hashMap.put("backdrop_path", imageUrl);
                    hashMap.put("budget", jsonObject.getString("budget"));
                    hashMap.put("homepage", jsonObject.getString("homepage"));
                    hashMap.put("original_language", jsonObject.getString("original_language"));
                    hashMap.put("original_title", jsonObject.getString("original_title"));
                    hashMap.put("overview", jsonObject.getString("overview"));
                    hashMap.put("poster_path", jsonObject.getString("poster_path"));
                    hashMap.put("release_date", jsonObject.getString("release_date"));
                    hashMap.put("revenue", jsonObject.getString("revenue"));
                    hashMap.put("runtime", jsonObject.getString("runtime") + " min");
                    hashMap.put("status", jsonObject.getString("status"));
                    hashMap.put("tagline", jsonObject.getString("tagline"));
                    addViews(hashMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addViews(HashMap<String, String> hashMap) {

        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);

            tvTitle.setText(pair.getKey() + "");
            tvDesc.setText(pair.getValue() + "");
            linearLayout.addView(view);
            System.out.println(pair.getKey() + " = " + pair.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }


    }
}

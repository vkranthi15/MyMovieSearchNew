package com.movies;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


import com.loopj.android.http.RequestParams;
import com.movies.webservices.WSResponnse;
import com.movies.webservices.WebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesListActivity extends AppCompatActivity {

    private MoviesListActivity mContext;
    private RecyclerView second_places_recycler_view;
    private SearchView et_search;
    private String movieQuery;
    private SwipeRefreshLayout movieSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        mContext = this;


        et_search = (SearchView) findViewById(R.id.et_search);

        second_places_recycler_view = (RecyclerView) findViewById(R.id.second_places_recycler_view);

        movieSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.movie_refresh_layout);
        setSearchLisner();
        scrollChangeOfFoodies();
    }


    private void setSearchLisner() {

        et_search.setIconifiedByDefault(false);

        et_search.setGravity(Gravity.CENTER);
        et_search.setQueryHint(Html.fromHtml("<font color = #dedede>" + "Search Movies" + "</font>"));

        //*** setOnQueryTextFocusChangeListener ***

        et_search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        //*** setOnQueryTextListener ***
        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {


                movieQuery=query;
                    getsearchList(query,1);



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }


    boolean loading;

    private void scrollChangeOfFoodies() {

        second_places_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int totalItem = layoutManager22.getItemCount();
                int lastVisibleItem = layoutManager22.findLastVisibleItemPosition();


                if (!loading && lastVisibleItem == totalItem - 1) {
                    loading = true;



                    getsearchList(movieQuery,(moviesAdapter.getItemCount() / 20) + 1);



                    loading = false;
                }
            }
        });

    }

    private MoviesAdapter moviesAdapter = null;
    LinearLayoutManager layoutManager22;
    private void getsearchList(String searchKeyword, final int size) {


        WebServices ws = new WebServices(mContext);
        RequestParams params = new RequestParams();

        params.put("query", searchKeyword);
        params.put("page",size);
        params.put("api_key", "146c49c181283b2bf0f5fe701380d51e");


        String url="https://api.themoviedb.org/3/search/movie";

        ws.invokeWebService(params, url, new WSResponnse() {

            @Override
            public void onResponse(boolean success, String response) {

                if (success) {
                    if (movieSwipeLayout.isRefreshing()) {
                        movieSwipeLayout.setRefreshing(false);
                    }


                    try {

                        loadSearchAdapter(response,size);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Movies list not found", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loadSearchAdapter(String response,int size) {
        try {
            JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                if (jsonArray != null ? jsonArray.length() > 0 : false) {

                    ArrayList<JSONObject> mydata = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                            mydata.add(jObj);
                    }

                    if(size<=1){
                        moviesAdapter = new MoviesAdapter(mContext, mydata);
                        layoutManager22 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        second_places_recycler_view.setLayoutManager(layoutManager22);
                        second_places_recycler_view.setAdapter(moviesAdapter);
                    }else{

                        if(mydata!=null?mydata.size()>0:false)
                            moviesAdapter.addMore(mydata);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Movies list not found", Toast.LENGTH_SHORT).show();
                }





        } catch (Exception e) {

        }


    }

}

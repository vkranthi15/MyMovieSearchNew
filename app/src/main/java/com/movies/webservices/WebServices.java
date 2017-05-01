package com.movies.webservices;

import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WebServices extends Activity {
	/**
	 * Method that performs RESTful webservice invocations
	 *
	 * @param params
	 */

	Context context;
	List<String> data;

	public WebServices(Context c) {
		this.context = c;
		data = new ArrayList<String>();
	}

	public void invokeWebService(RequestParams params,
								 final String tag, final WSResponnse wsResponnse
	) {
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(getUrlPath(tag), params, new JsonHttpResponseHandler() {
			// When the response returned by REST has Http response code
			// '200'



			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);

				wsResponnse.onResponse(true,  response.toString() );

			}


//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//				super.onSuccess(statusCode, headers, responseBody);
//				wsResponnse.onResponse(true,  String.valueOf(responseBody[0]) );
//			}

//			@Override
//			public void onSuccess(String response) {
//				// Hide Progress Dialog
////                Log.i("test", response + " is the data");
//				wsResponnse.onResponse(true, response);
//			}


			@Override
			public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
				super.onFailure(statusCode, e, errorResponse);
				wsResponnse.onResponse(false, "");
				// When Http response code is '404'
				if (statusCode == 404) {
				}
				// When Http response code is '500'
				else if (statusCode == 500) {
				}
				// When Http response code other than 404, 500
				else {

				}
			}
		});
	}

	private String getUrlPath(String tag){
		return tag;
	}
}

package com.example.android.popularmovies;

import com.example.android.popularmovies.adapter.ImageAdapter;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UTFDataFormatException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private final String LOG_TAG = MainActivity.class.getSimpleName();

	GridView gridView;
	ImageAdapter mImageAdapter;

	//static final String[] MOBILE_OS = new String[] { "Android", "iOS",
	//		"Windows", "Blackberry" };
	private ArrayList<MovieData> popularMovies;
	public static final String API_RELEASE_DATE_FORMAT = "yyyy-MM-dd";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(LOG_TAG, "****onCreate savedInstanceState:" + savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView1);
		executeFetchMovieTask();
		mImageAdapter = new ImageAdapter(this ,R.layout.mobile, getPopularMoviesData(savedInstanceState));
		gridView.setAdapter(mImageAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				//CharSequence toastText = "toast from initial load";//((TextView) v.findViewById(R.id.grid_item_label)).getText();
				MovieData gridItemData = (MovieData)mImageAdapter.getItem(position);
				CharSequence toastText = gridItemData.getMovieName();
						Toast.makeText(
						getApplicationContext(),toastText
						, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this, DetailActivity.class)
						.putExtra("ImageClicked",gridItemData);
				startActivity(intent);

			}
		});

	}

	private ArrayList<MovieData> getPopularMoviesData(Bundle savedInstanceState) {
		if(popularMovies == null ) {
			Log.v(LOG_TAG, "(popularMovies == null");
			popularMovies = getHardcodedPopularMoviesData();
		}
		else if (!savedInstanceState.containsKey("popularmovies")){
			Log.v(LOG_TAG, "****!savedInstanceState.containsKey");
			popularMovies = getHardcodedPopularMoviesData();
		}
		else {
			popularMovies = savedInstanceState.getParcelableArrayList("popularmovies");
		}
		Log.v(LOG_TAG, "****getPopularMoviesData: " + popularMovies);
		return popularMovies;
	}

	private ArrayList<MovieData> getHardcodedPopularMoviesData() {
		popularMovies = new ArrayList<MovieData>();
		MovieData emptyMovieData = new MovieData("","","", "","","");
		popularMovies.add(emptyMovieData);
		/*
		MovieData m1 = new MovieData("/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg","Suicide Squad","2016", "08.03/16","5.88",
				"From DC Comics comes the Suicide Squad, an antihero team of incarcerated supervillains who act as deniable assets for the United States government, undertaking high-risk black ops missions in exchange for commuted prison sentences.");
		popularMovies.add(m1);
		MovieData m2 = new MovieData("/y31QB9kn3XSudA15tV7UWQ9XLuW.jpg","Guardians of the Galaxy","2014","07.30/14","7.96",
				"Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser.");
		popularMovies.add(m2);
		*/
		return popularMovies;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		CharSequence toastText;

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			toastText="You selected settings menu!";
			showToast(getApplicationContext(),toastText);
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
			return true;
		} /*
		else if (id == R.id.action_refresh){
			toastText="You selected Refresh menu! getting data from API!";
			showToast(getApplicationContext(),toastText);
			executeFetchMovieTask();
			return true;
		} */

		return super.onOptionsItemSelected(item);
	}

	private void executeFetchMovieTask(){
		FetchMoviesTask movieTask = new FetchMoviesTask();
		String sortPreference = Utility.getPreferredSortOrder(getApplicationContext());
		movieTask.execute(sortPreference);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.v(LOG_TAG, "****onSaveInstanceState: " + popularMovies);
		outState.putParcelableArrayList("popularmovies", popularMovies);
		super.onSaveInstanceState(outState);
	}

	private void showToast(Context context, CharSequence text){
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
	}


	//Start of inner class
	public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieData>> {
		private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
    /* ########## Start of JSON Parsing Helper methods ###### */

		private ArrayList<MovieData> getMoviesDataFromJson(String moviesJsonStr)
				throws JSONException {

			// These are the names of the JSON objects that need to be extracted.
			final String MOVIE_RESULT_LIST = "results";
			final String MOVIE_IMAGE_PATH = "poster_path";
			final String MOVIE_TITLE = "title";
			final String MOVIE_RELEASE_DATE = "release_date";
			final String MOVIE_SYNOPSIS = "overview";
			final String MOVIE_VOTE_AVERAGE = "vote_average";
			final String MOVIE_ID = "id";

			JSONObject moviesJson = new JSONObject(moviesJsonStr);
			JSONArray movieResultArray = moviesJson.getJSONArray(MOVIE_RESULT_LIST);
			ArrayList<MovieData> result = new ArrayList<MovieData>();
			for(int i = 0; i < movieResultArray.length(); i++) {

				JSONObject movieJson = movieResultArray.getJSONObject(i);
				String imagepath = movieJson.getString(MOVIE_IMAGE_PATH);
				String title = movieJson.getString(MOVIE_TITLE);
				String apiReleaseDate = movieJson.getString(MOVIE_RELEASE_DATE);
				String voteAverage = movieJson.getString(MOVIE_VOTE_AVERAGE);
				String synopsis = movieJson.getString(MOVIE_SYNOPSIS);
				String releaseyear = Utility.getYearFromDate(apiReleaseDate,API_RELEASE_DATE_FORMAT);
				String detailPageFormatedDate = Utility.getDetailPageFormattedDate(apiReleaseDate,API_RELEASE_DATE_FORMAT);
				Log.v(LOG_TAG, "***detailPageFormatedDate:" + detailPageFormatedDate);
				MovieData data = new MovieData(imagepath,title,releaseyear,detailPageFormatedDate,voteAverage,synopsis);
				result.add(data);
			}

			for (MovieData data : result) {
				Log.v(LOG_TAG, "****Movie entry: " + data);
			}
			return result;

		}
		/* ########## End of JSON Parsing Helper methods ###### */
		protected ArrayList<MovieData> doInBackground(String... params) {

			// If there's no sort preference return null as we do not have the api url to build upon.
			if (params.length == 0) {
				return null;
			}

			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;

			// Will contain the raw JSON response as a string.
			String movieJsonStr = null;

			try {
				final String SORTORDER = params[0];
				Log.v(LOG_TAG, "SORTORDER:" + SORTORDER);
				final String MOVIEDB_BASE_URL = BuildConfig.MOVIE_BASE_URL + SORTORDER;
						//"http://api.themoviedb.org/3/movie/popular";
				final String APPID_PARAM = "api_key";

				Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
						.appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
						.build();

				URL url = new URL(builtUri.toString());

				Log.v(LOG_TAG, "Built URI *" + builtUri.toString());
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();

				// Read the input stream into a String
				InputStream inputStream = urlConnection.getInputStream();
				StringBuffer buffer = new StringBuffer();
				if (inputStream == null) {
					// Nothing to do.
					return null;
				}
				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				while ((line = reader.readLine()) != null) {
					// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
					// But it does make debugging a *lot* easier if you print out the completed
					// buffer for debugging.
					buffer.append(line + "\n");
				}

				if (buffer.length() == 0) {
					return null;
				}
				movieJsonStr = buffer.toString();

				Log.v(LOG_TAG, "Movie Json string:" + movieJsonStr);
			} catch (IOException e) {
				Log.e(LOG_TAG, "Error ", e);
				return null;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						Log.e(LOG_TAG, "Error closing stream", e);
					}
				}
			}

			try {
				return getMoviesDataFromJson(movieJsonStr);
			} catch (JSONException e) {
				Log.e(LOG_TAG, e.getMessage(), e);
				e.printStackTrace();
			}

			return null;
		}
		/** progress dialog to show user that the backup is processing. */
		/** application context. */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Please wait");
			this.dialog.show();
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		protected void onPostExecute( ArrayList<MovieData> result) {
			Log.v(LOG_TAG, "In post Execute movie task:" + result);
			if (result != null) {
				Log.v(LOG_TAG, "In post Execute movie task: MovieData Size" + result.size());
				mImageAdapter.clear();
				for(MovieData movieData : result) {
					Log.v(LOG_TAG, "****In adding movie to adapter - MovieData: " + movieData);
					mImageAdapter.add(movieData);
				}
				Log.v(LOG_TAG, "In post Execute movie task: mImageAdapter Size" + mImageAdapter.getCount());
				Log.v(LOG_TAG, "In post Execute movie task: result.get(0):" + result.get(0).getMovieName()+ "mImageAdapter.get(0):" + mImageAdapter.getItem(0).toString());
				popularMovies = result;
				mImageAdapter.notifyDataSetChanged();
				Log.v(LOG_TAG, "***********notified data change to adapter" );
			} else {
				Log.v(LOG_TAG, "In post Execute Movie data from API was null" );
			}

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

}
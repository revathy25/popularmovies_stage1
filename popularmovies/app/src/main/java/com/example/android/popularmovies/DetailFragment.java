/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;



/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private ShareActionProvider mShareActionProvider;
    private String mForecast;
    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private ImageView mIconView;
    private TextView movieTextView;
    private TextView movieTitleTextView;
    private TextView movieSynopsisTextView;
    private TextView runningTimeTextView;
    private TextView releaseDateTextView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        //CharSequence textToShow = "";
        MovieData movieDetailData;
        if (arguments != null) {
            //mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            movieDetailData = (MovieData)arguments.getParcelable("MovieDetailData");
            movieTitleTextView =  (TextView) rootView.findViewById(R.id.movie_title_textview);
            movieTitleTextView.setText(movieDetailData.getMovieName());
            mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
            //Picasso.with(getActivity().getApplicationContext()).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(mIconView);
            ///lH2Ga8OzjU1XlxJ73shOlPx6cRw.jpg /nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
            Picasso.with(getActivity().getApplicationContext()).load(IMAGE_BASE_URL + movieDetailData.getImageRelativePath()).into(mIconView);
            movieTextView = (TextView) rootView.findViewById(R.id.release_year_textview);
           //TODO Just get year
            movieTextView.setText(movieDetailData.getReleaseYear());
            //TODO currently displaying vote average
            runningTimeTextView = (TextView) rootView.findViewById(R.id.running_time_textview);
            runningTimeTextView.setText(movieDetailData.getVoteAverage());
            releaseDateTextView = (TextView) rootView.findViewById(R.id.release_date_textview);
            releaseDateTextView.setText(movieDetailData.getReleaseDate());
            movieSynopsisTextView = (TextView) rootView.findViewById(R.id.movie_synopsis_textview);
            movieSynopsisTextView.setText(movieDetailData.getPlotSynopsis());
        }



        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //inflater.inflate(R.menu.detail, menu);

        // Retrieve the share menu item
       // MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
       // mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
           // mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
        /*
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            ); */
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
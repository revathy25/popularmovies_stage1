package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.MovieData;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends ArrayAdapter {
	private Context context;
	private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

	public ImageAdapter(Context context,int resource, List<MovieData> movieData) {
		super(context,resource,movieData);
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		//if (convertView == null ) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.mobile, null);
			MovieData gridItemData = (MovieData)super.getItem(position);
			// set value into textview
			/*
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			//textView.setText(mobileValues[position]);
			textView.setText(gridItemData.getMovieName()); */
			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);

			Picasso.with(context).load(IMAGE_BASE_URL+gridItemData.getImageRelativePath()).into(imageView);
	/*
		} else {
			gridView = (View) convertView;
		}
*/
		return gridView;
	}

}

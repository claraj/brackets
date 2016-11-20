package com.clara.brackets;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 11/19/16.
 */

public class MatchesListAdapter extends ArrayAdapter<Match> {

	private static final String TAG = "MATCHES LIST ADAPTER";

	public MatchesListAdapter(Context context, int resource, List<Match> objects) {
		super(context, resource, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d(TAG, "Matches list adapter get view for " + getItem(position));

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.match_list_element, null);
		}


		TextView competitor1 = (TextView) convertView.findViewById(R.id.first_competitor_tv);
		TextView competitor2 = (TextView) convertView.findViewById(R.id.second_competitor_tv);

		Match match = getItem(position);



		if (match.comp_1 != null) {
			competitor1.setText(match.comp_1.name);
		} else {
			competitor1.setText("no opponent");
		}

		if (match.comp_2 != null) {
			competitor2.setText(match.comp_2.name);
		} else {
			competitor2.setText("no opponent");
		}

		if (match.winner != null && match.winner == match.comp_1) {
			competitor1.setTypeface(null, Typeface.BOLD);
			competitor1.setTextColor(Color.RED);
			Log.d(TAG, " winner is comp_1 set  style");
			competitor2.setTextColor(Color.GRAY);

		}

		if (match.winner != null && match.winner == match.comp_2) {
			Log.d(TAG, "winner is comp_2 loser style");

			competitor2.setTypeface(null, Typeface.BOLD);
			competitor2.setTextColor(Color.RED);
			Log.d(TAG, " winner is comp_1 set  style");
			competitor1.setTextColor(Color.GRAY);

		}

		return convertView;

	}

}

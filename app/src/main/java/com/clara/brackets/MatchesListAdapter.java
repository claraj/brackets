package com.clara.brackets;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
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

		if (match == null) { return convertView; }

		if (match.comp_1 != null) {
			competitor1.setText(match.comp_1.name);
		} else {
			competitor1.setText(R.string.tbd);
		}

		if (match.comp_2 != null) {
			competitor2.setText(match.comp_2.name);
		} else {
			competitor2.setText(R.string.tbd);
		}


		if (match.winner != null && match.winner == match.comp_1) {

			Log.d(TAG, " winner is comp_1");

			competitor1.setTypeface(null, Typeface.BOLD);
			competitor1.setTextColor(ContextCompat.getColor(getContext(), R.color.winner_text));

			competitor2.setTextColor(ContextCompat.getColor(getContext(), R.color.loser_text));
		}

		if (match.winner != null && match.winner == match.comp_2) {

			Log.d(TAG, "winner is comp_2");

			competitor2.setTypeface(null, Typeface.BOLD);
			competitor2.setTextColor(ContextCompat.getColor(getContext(), R.color.winner_text));
			competitor1.setTextColor(ContextCompat.getColor(getContext(), R.color.loser_text));

		}

		return convertView;

	}

}

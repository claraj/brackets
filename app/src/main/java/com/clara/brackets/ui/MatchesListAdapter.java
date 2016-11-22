package com.clara.brackets.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.clara.brackets.R;
import com.clara.brackets.data.Match;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Clara on 11/19/16.
 * List adapter for one level of the competition. Show competitors names, and match date. Highlights winner.
 */

public class MatchesListAdapter extends ArrayAdapter<Match> {

	private static String dateTemplate = "kk:mm, dd/MM/yy";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(dateTemplate, Locale.getDefault());

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
		TextView dateTV = (TextView) convertView.findViewById(R.id.match_date_tv);


		Match match = getItem(position);

		if (match == null) { return convertView; }

		//if winner and match date set
		if (match.matchDate != null && match.winner != null)  {
			String dateString = dateFormat.format(match.matchDate);
			dateTV.setText(dateString);
		}


		if (match.comp_1 != null) {
			competitor1.setText(match.comp_1.name);
		} else {
			competitor1.setText(R.string.tbd);  //no competitor identified yet
		}

		if (match.comp_2 != null) {
			competitor2.setText(match.comp_2.name);
		} else {
			competitor2.setText(R.string.tbd);   //no competitor identified yet
		}

		//If winner known, change text to winner color; change loser color

		if (match.winner != null && match.winner.id == match.comp_1.id) {

			Log.d(TAG, " winner is comp_1");

			competitor1.setTypeface(null, Typeface.BOLD);
			competitor1.setTextColor(ContextCompat.getColor(getContext(), R.color.winner_text));
			competitor2.setTextColor(ContextCompat.getColor(getContext(), R.color.loser_text));

		}

		if (match.winner != null && match.winner.id == match.comp_2.id) {

			Log.d(TAG, "winner is comp_2");

			competitor2.setTypeface(null, Typeface.BOLD);
			competitor2.setTextColor(ContextCompat.getColor(getContext(), R.color.winner_text));
			competitor1.setTextColor(ContextCompat.getColor(getContext(), R.color.loser_text));

		}


		return convertView;

	}

}

package com.clara.brackets;

import android.app.Activity;
import android.content.Context;
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
	Context context;

	public MatchesListAdapter(Context context, int resource, List<Match> objects) {
		super(context, resource, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d(TAG, "Matches list adapter get view for " + getItem(position));

		if (convertView == null) {
			LayoutInflater inflater = ((Activity)context) .getLayoutInflater();
			inflater.inflate(R.layout.match_list_element, parent);

		}

		TextView competitor1 = (TextView) convertView.findViewById(R.id.first_competitor_tv);
		TextView competitor2 = (TextView) convertView.findViewById(R.id.second_competitor_tv);

		Match match = getItem(position);

		competitor1.setText(match.comp_1.name);
		competitor2.setText(match.comp_2.name);

		return convertView;

	}

}

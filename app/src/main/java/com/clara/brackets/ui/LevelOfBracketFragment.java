package com.clara.brackets.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clara.brackets.R;
import com.clara.brackets.data.Match;
import com.clara.brackets.ui.MatchesListAdapter;

import java.util.ArrayList;



public class LevelOfBracketFragment extends Fragment {

	// the fragment initialization parameters
	private static final String ARG_MATCHES = "param_list_of_matches";
	private static final String ARG_LEVEL = "param_level_int";

	//Logging tag
	private static final String TAG = "LEVEL OF BRACKET FRAG";

	private static final java.lang.String DIALOG_TAG = "dialog_fragment_transaction_tag";

	private ArrayList<Match> mMatches;
	private MatchesListAdapter mAdapter;
	private int mLevel;


	public LevelOfBracketFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters. */
	public static LevelOfBracketFragment newInstance(ArrayList<Match> matches, int level) {
		LevelOfBracketFragment fragment = new LevelOfBracketFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList(ARG_MATCHES, matches);
		args.putInt(ARG_LEVEL, level);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mMatches = getArguments().getParcelableArrayList(ARG_MATCHES);
			mLevel = getArguments().getInt(ARG_LEVEL);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.d(TAG, "on create view for Level Of Bracket fragment");

		View view = inflater.inflate(R.layout.fragment_level_of_bracket, container, false);

		mAdapter = new MatchesListAdapter(getContext(), R.layout.match_list_element, mMatches);

		ListView listView = (ListView) view.findViewById(R.id.matches_listview);

		listView.setAdapter(mAdapter);

		TextView levelTV = (TextView) view.findViewById(R.id.level_tv);
		levelTV.setText("Level = " + mLevel);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Log.d(TAG, "click on item");

				Match match = mAdapter.getItem(i);

				if (match.comp_1 == null || match.comp_2 == null) {
					Toast.makeText(LevelOfBracketFragment.this.getActivity(), "Need both competitors before a winner can be set", Toast.LENGTH_SHORT).show();
					return;
				}

				if (match.comp_1.bye || match.comp_2.bye)  {
					Toast.makeText(LevelOfBracketFragment.this.getActivity(), "Bye: " + match.comp_2.name + " is the winner", Toast.LENGTH_SHORT).show();
					return;
				}

				EnterMatchResultDialogFragment fragment = EnterMatchResultDialogFragment.newInstance(mAdapter.getItem(i));
				fragment.show(getFragmentManager(), DIALOG_TAG);

			}
		});
		return view;

	}



	public void updateList(ArrayList<Match> matches) {
		mMatches = matches;
		mAdapter.clear();
		mAdapter.addAll(mMatches);
		mAdapter.notifyDataSetChanged();
	}

}

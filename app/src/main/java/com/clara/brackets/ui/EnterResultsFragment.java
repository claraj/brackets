package com.clara.brackets.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clara.brackets.R;
import com.clara.brackets.data.Bracket;
import com.clara.brackets.data.Match;

import java.util.Date;


public class EnterResultsFragment extends Fragment  implements EnterMatchResultDialogFragment.MatchResultDialogFragmentListener {

	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_BRACKET = "bracket";
	private static final String TAG = "ENTER RESULTS FRAG";

	private Bracket mBracket;

	private OnMatchUpdated mListener;

	MatchesPagerAdapter pagerAdapter;

	public EnterResultsFragment() {
		// Required empty public constructor
	}

//	/**
//	 * Use this factory method to create a new instance of
//	 * this fragment using the provided parameters.
//	 *
//	 * @param bracket the Bracket to display.
//	 * @return A new instance of fragment EnterResultsFragment.
//	 */

	public static EnterResultsFragment newInstance(Bracket bracket) {
		EnterResultsFragment fragment = new EnterResultsFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_BRACKET, bracket);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mBracket = getArguments().getParcelable(ARG_BRACKET);
			Log.d(TAG, "when extracted from fragment arguments, Bracket is as follows:");
			mBracket.logTree();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_enter_results, container, false);


		Log.d(TAG, "Creating view for Enter Result Fragment");

		//The pager is for swiping between different levels of the bracket.
		ViewPager pager = (ViewPager) view.findViewById(R.id.matches_pager);
		pagerAdapter = new MatchesPagerAdapter(getChildFragmentManager(), mBracket);
		pager.setAdapter(pagerAdapter);

		return view;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		if (context instanceof OnMatchUpdated) {
			mListener = (OnMatchUpdated) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnMatchUpdated");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}


	public interface OnMatchUpdated {
		void matchUpdated(Match match);
	}


	@Override
	public void matchResultUpdated(Match match) {
		//notify Activity

		Log.d(TAG, "updated match " + match);

		match.matchDate = new Date();

		mBracket.updateMatch(match);
		mBracket.advanceWinners();
		pagerAdapter.setBracket(mBracket);

		mListener.matchUpdated(match);


	}
}

package com.clara.brackets.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.clara.brackets.R;

import org.w3c.dom.Text;


public class AppLaunchOptionsFragment extends Fragment implements View.OnClickListener{

	private static final String ARG_TOURNAMENT_IN_PROGRESS = "in progress";

	private static final String TAG = "APP LAUNCH OPTS";

	private boolean mInProgress;

	private OnUserSelectionListener mListener;

	public AppLaunchOptionsFragment() {
		// Required empty public constructor
	}

	public static AppLaunchOptionsFragment newInstance(boolean inProgress) {
		AppLaunchOptionsFragment fragment = new AppLaunchOptionsFragment();
		Bundle args = new Bundle();
		args.putBoolean(ARG_TOURNAMENT_IN_PROGRESS, inProgress);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mInProgress = getArguments().getBoolean(ARG_TOURNAMENT_IN_PROGRESS);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_app_launch_options, container, false);

		TextView inProgressTV = (TextView) view.findViewById(R.id.tournament_in_progress_indicator_tv);

		//Show or hide the in progress indicator, depending on if there is a tournament in progress
		int visibility = (mInProgress) ? View.VISIBLE : View.INVISIBLE;
		inProgressTV.setVisibility(visibility);

		return view;

	}

	@Override
	public void onClick(View view) {

		Log.d(TAG, "This view clicked:" + view);

		switch (view.getId()) {
			case R.id.continue_existing_tournament : {
				mListener.continueExistingTournament();
				break;
			}

			case R.id.start_new_tournament : {
				mListener.startNewTournament();
				break;
			}

			case R.id.start_new_tournament_with_test_data : {
				mListener.startNewTournamentWithTestData();
			}
		}


	}


		@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnUserSelectionListener) {
			mListener = (OnUserSelectionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnUserSelectionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnUserSelectionListener {
		void startNewTournament();
		void startNewTournamentWithTestData();
		void continueExistingTournament();
	}
}

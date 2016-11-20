package com.clara.brackets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;


/**
 * Created by admin on 11/19/16.
 */

public class MatchResultDialogFragment extends DialogFragment {

	private static final String ARG_MATCH = "arg_match";

	private MatchResultDialogFragmentListener mListener;

	private Match mMatch;

	private static final String TAG = "MATCH RESULT DIALOG";

	private CharSequence[] mCompetitors;

	interface MatchResultDialogFragmentListener {
		void matchResultUpdated(Match match);
	}

	public static MatchResultDialogFragment newInstance(Match match) {
		Bundle args = new Bundle();
		args.putParcelable(ARG_MATCH, match);
		MatchResultDialogFragment fragment = new MatchResultDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		//the parent fragment is a EnterResultsFragment

		if (getParentFragment() instanceof MatchResultDialogFragmentListener) {
			mListener = (MatchResultDialogFragmentListener) getParentFragment();
		} else {
			throw new RuntimeException(getParentFragment().toString() + " must implement MatchResultDialogFragmentListener");
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mMatch = getArguments().getParcelable(ARG_MATCH);

		Log.d(TAG, "Dialog for results of match " + mMatch);

		mCompetitors = new CharSequence[2];
		mCompetitors[0] = mMatch.comp_1.name;
		mCompetitors[1] = mMatch.comp_2.name;

	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		return builder.setTitle("Select match winner")
				.setItems(mCompetitors, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

						if (i == 0) {
							mMatch.winner = mMatch.comp_1;
						}

						if (i == 1) {
							mMatch.winner = mMatch.comp_2;
						}

						mListener.matchResultUpdated(mMatch);

					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						//close dialog, do nothing
						mMatch.winner = null;
					}
				}).create();

	}

}

package com.clara.brackets;

import android.content.Context;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LevelOfBracketFragment.OnMatchResult} interface
 * to handle interaction events.
 * Use the {@link LevelOfBracketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelOfBracketFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_MATCHES = "param_list_of_matches";
	private static final String ARG_LEVEL = "param_level_int";
	private static final String TAG = "LEVEL OF BRACKET FRAG";
	private static final java.lang.String DIALOG_TAG = "dialog_fragment_transaction_tag";

	private ArrayList<Match> mMatches;
	private MatchesListAdapter mAdapter;
	private int mLevel;

	private OnMatchResult mListener;

	public LevelOfBracketFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
//	 * @param param1 Parameter 1.
//	 * @param param2 Parameter 2.
//	 * @return A new instance of fragment LevelOfBracketFragment.
	 */
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

				if (match.comp_1 == null && match.comp_2 != null)  {
					Toast.makeText(LevelOfBracketFragment.this.getActivity(), "The winner is " + match.comp_2.name, Toast.LENGTH_LONG).show();
				}

				else if (match.comp_1 != null && match.comp_2 == null)  {
					Toast.makeText(LevelOfBracketFragment.this.getActivity(), "The winner is " + match.comp_1.name, Toast.LENGTH_LONG).show();
				}

				else if (match.comp_1 == null && match.comp_2 == null) {
					//don't think this can happen (?)
					Log.e(TAG, "comp1 and comp2 are null" + match);
				}

				else {
					MatchResultDialogFragment fragment = MatchResultDialogFragment.newInstance(mAdapter.getItem(i));
					fragment.show(getFragmentManager(), DIALOG_TAG);
				}
			}
		});
		return view;

	}

	// TODO: Rename method, update argument and hook method into UI event
//	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
//	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnMatchResult) {
			mListener = (OnMatchResult) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnMatchResult");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public void updateList(ArrayList<Match> matches) {
		mMatches = matches;
		mAdapter.clear();
		mAdapter.addAll(mMatches);
		mAdapter.notifyDataSetChanged();
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
	public interface OnMatchResult {
		// TODO: Update argument type and name
		void onResultOfMatch(Match match);
	}
}

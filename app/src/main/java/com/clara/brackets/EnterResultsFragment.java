package com.clara.brackets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnterResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnterResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterResultsFragment extends Fragment  implements EnterMatchResultDialogFragment.MatchResultDialogFragmentListener {

	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_BRACKET = "bracket";
	private static final String TAG = "ENTER RESULTS FRAG";

	private Bracket mBracket;

	private OnFragmentInteractionListener mListener;

	MatchesPagerAdapter pagerAdapter;

	public EnterResultsFragment() {
		// Required empty public constructor
	}

//	/**
//	 * Use this factory method to create a new instance of
//	 * this fragment using the provided parameters.
//	 *
//	 * @param param1 Parameter 1.
//	 * @param param2 Parameter 2.
//	 * @return A new instance of fragment EnterResultsFragment.
//	 */
	// TODO: Rename and change types and number of parameters
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
		//pagerAdapter.setBracket(mBracket);
		pager.setAdapter(pagerAdapter);

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

		//TODO - listener not needed yet.
		/*	if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		} */
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
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
	//	void onFragmentInteraction(Uri uri);
	}


	@Override
	public void matchResultUpdated(Match match) {
		//notify Activity
		//match has to tell it's parent what updates

		Log.d(TAG, "updated match " + match);

		match.matchDate = new Date();

		mBracket.updateMatchWinnerAndDate(match);
		mBracket.advanceWinners();
		pagerAdapter.setBracket(mBracket);


	}
}

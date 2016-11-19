package com.clara.brackets;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnterCompetitorsFragment.OnEnterCompetitorFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnterCompetitorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterCompetitorsFragment extends Fragment implements View.OnClickListener {

	private static final String TAG = "ENTER COMPETITORS FRAG";
	private Button mSaveNameButton, mSaveAllButton;
	private EditText mNameET;
	private ListView mListView;
	private ArrayAdapter<Competitor> mAdapter;

	private ArrayList<Competitor> competitors;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_COMPETITOR_LIST = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mCompetitors;
	private String mParam2;

	private OnEnterCompetitorFragmentInteractionListener mListener;

	public EnterCompetitorsFragment() {
		// Required empty public constructor
	}

//	/**
//	 * Use this factory method to create a new instance of
//	 * this fragment using the provided parameters.
//	 *
//	 * @param param1 Parameter 1.
//	 * @param param2 Parameter 2.
//	 * @return A new instance of fragment EnterCompetitorsFragment.
//	 */
	// TODO: Rename and change types and number of parameters
	public static EnterCompetitorsFragment newInstance(ArrayList<Competitor> competitors /*, String param2*/) {
		EnterCompetitorsFragment fragment = new EnterCompetitorsFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList(ARG_COMPETITOR_LIST, competitors);
//		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "ON CREATE RUNS");
		
		if (getArguments() != null) {
			competitors = getArguments().getParcelableArrayList(ARG_COMPETITOR_LIST);
		}

		if (competitors == null) {
			competitors = new ArrayList<Competitor>();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		Log.d(TAG, "ON CREATE *VIEW* RUNS");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_enter_competitors, container, false);
		mSaveNameButton = (Button) view.findViewById(R.id.save_competitor_name_button);
		mSaveNameButton.setOnClickListener(this);
		mSaveAllButton = (Button) view.findViewById(R.id.save_competitors_button);
		mSaveAllButton.setOnClickListener(this);
		mNameET = (EditText) view.findViewById(R.id.new_competitor_name_et);
		mListView = (ListView) view.findViewById(R.id.current_competitors_list);

		mAdapter = new ArrayAdapter<Competitor>(this.getContext(), R.layout.enter_competitor_list_element, R.id.comp_name_list_tv, competitors);

		mListView.setAdapter(mAdapter);

		return view;

	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnEnterCompetitorFragmentInteractionListener) {
			mListener = (OnEnterCompetitorFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.save_competitor_name_button: {
				//save

				String name = mNameET.getText().toString();
				Competitor competitor = new Competitor(name);
				competitors.add(competitor);
				mAdapter.notifyDataSetChanged();
				break;
			}

			case R.id.save_competitors_button: {
				mListener.onCompetitorListCreated(competitors);
				break;
			}

		}


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
	public interface OnEnterCompetitorFragmentInteractionListener {
		// TODO: Update argument type and name
		void onCompetitorListCreated(ArrayList<Competitor> competitors);
	}
}

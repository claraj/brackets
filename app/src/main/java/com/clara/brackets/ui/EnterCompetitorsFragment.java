package com.clara.brackets.ui;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.clara.brackets.R;
import com.clara.brackets.data.Competitor;

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
	private TextView mNumberCompetitors;
	private ListView mListView;
	private ArrayAdapter<Competitor> mAdapter;

	private ArrayList<Competitor> mCompetitors;

	// the fragment initialization parameters,
	private static final String ARG_COMPETITOR_LIST = "param1";


	private OnEnterCompetitorFragmentInteractionListener mListener;

	public EnterCompetitorsFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 */
	public static EnterCompetitorsFragment newInstance(ArrayList<Competitor> competitors) {
		EnterCompetitorsFragment fragment = new EnterCompetitorsFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList(ARG_COMPETITOR_LIST, competitors);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "ON CREATE RUNS");
		
		if (getArguments() != null) {
			mCompetitors = getArguments().getParcelableArrayList(ARG_COMPETITOR_LIST);
		}

		if (mCompetitors == null) {
			mCompetitors = new ArrayList<>();
		}

		Log.d(TAG, "mCompetitors " + mCompetitors);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		Log.d(TAG, "ON CREATE *VIEW* RUNS");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_enter_competitors, container, false);
		mSaveNameButton = (Button) view.findViewById(R.id.save_competitor_name_button);
		mSaveNameButton.setOnClickListener(this);
		mNumberCompetitors = (TextView) view.findViewById(R.id.number_of_competitors_tv);
		mSaveAllButton = (Button) view.findViewById(R.id.save_competitors_button);
		mSaveAllButton.setOnClickListener(this);
		mNameET = (EditText) view.findViewById(R.id.new_competitor_name_et);
		mListView = (ListView) view.findViewById(R.id.current_competitors_list);

		mAdapter = new ArrayAdapter<Competitor>(this.getContext(), R.layout.enter_competitor_list_element, R.id.comp_name_list_tv, mCompetitors);
		mListView.setAdapter(mAdapter);

		if (mCompetitors.size() == 0) {										//onCreate runs first, and initializes mCompetitors.
			mNumberCompetitors.setText("(No competitors)");
		} else {
			mNumberCompetitors.setText(" (" + mCompetitors.size() + " competitor(s) )");
		}

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
				//Get competitor name
				String name = mNameET.getText().toString();
				//Verify name is unique
				if (duplicateName(name)) {
					Toast.makeText(this.getContext(), "You already have a competitor called " + name, Toast.LENGTH_LONG).show();
					return;
				}

				if (name.equals("")) {
					Toast.makeText(this.getContext(), "Please enter a name", Toast.LENGTH_LONG).show();
					return;
				}

				Competitor competitor = new Competitor(name);
				mCompetitors.add(competitor);
				mNameET.setText("");   //clear EditText for the new competitor's name
				mAdapter.notifyDataSetChanged();

				mNumberCompetitors.setText(" (" + mCompetitors.size() + " competitors)");

				break;

			}

			case R.id.save_competitors_button: {
				mListener.onCompetitorListCreated(mCompetitors);
				break;
			}

		}


	}

	private boolean duplicateName(String name) {

		for (Competitor c : mCompetitors) {
			if (name.equals(c.name)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * This interface must be implemented by Activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * Activity.
	 */
	public interface OnEnterCompetitorFragmentInteractionListener {
		void onCompetitorListCreated(ArrayList<Competitor> competitors);
	}
}

package com.clara.brackets;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
		EnterCompetitorsFragment.OnEnterCompetitorFragmentInteractionListener,
		LevelOfBracketFragment.OnMatchResult {

	ArrayList<Competitor> mCompetitors;

	EnterCompetitorsFragment enterCompetitorsFragment;
	//Not_needed_ShowResultsFragment showResultsFragment;
	EnterResultsFragment enterResultsFragment;

	BracketManager manager;

	private final String TAG = "MAIN ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//If a competition is in progress, display the Show Results fragment

		//Otherwise, show the EnterCompetitorsFragment

		manager = new BracketManager(this);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		if (isCompetitionInProgress()) {

			manager.setCompetitors(mockCompetitors(14));

			enterResultsFragment = EnterResultsFragment.newInstance(manager.createBracket());   //todo provide results (?)
			transaction.add(R.id.content, enterResultsFragment);
		} else {
			enterCompetitorsFragment = EnterCompetitorsFragment.newInstance(mockCompetitors(14));
			transaction.add(R.id.content, enterCompetitorsFragment);
		}

		transaction.commit();



	}

	private boolean isCompetitionInProgress() {

		//todo how to check?
		return true;

	}


	private ArrayList<Competitor> mockCompetitors(int num) {

		char name = 65;
		ArrayList<Competitor> mock = new ArrayList<>();
		for (int x = 0 ; x < num ; x++) {
			Competitor c = new Competitor(name++ + "");
			mock.add(c);
		}

		return mock;
	}


	@Override
	public void onCompetitorListCreated(ArrayList<Competitor> competitors) {

		mCompetitors = competitors;
		manager.saveCompetitors(mCompetitors);

		//show enter results screen

		Bracket bracket = manager.createBracket();

		manager.saveMatches();

		enterResultsFragment = EnterResultsFragment.newInstance(bracket);

		Toast.makeText(this, "Should show results fragment", Toast.LENGTH_LONG).show();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content, enterResultsFragment);
		transaction.commit();


	}

	@Override
	public void onResultOfMatch(Match match) {
		Log.d(TAG, "Match result from fragment " + match);
	}
}

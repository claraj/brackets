package com.clara.brackets;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
		EnterCompetitorsFragment.OnEnterCompetitorFragmentInteractionListener,
		EnterResultsFragment.OnMatchUpdated
{

	private static final String TOURNAMENT_IN_PROGRESS = "is tournament in progress?";
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

			manager.getCompetitorsFromDB();
			Bracket bracket = manager.createBracket();
			enterResultsFragment = EnterResultsFragment.newInstance(bracket);
			transaction.add(R.id.content, enterResultsFragment);

		} else {

			//create new competitors.

			enterCompetitorsFragment = EnterCompetitorsFragment.newInstance(mockCompetitors(14));
			transaction.add(R.id.content, enterCompetitorsFragment);
		}

		transaction.commit();

	}


	private boolean isCompetitionInProgress() {

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		return pref.getBoolean(TOURNAMENT_IN_PROGRESS, false);
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
		manager.saveCompetitors(mCompetitors);    //this should create a pk_id for each competitor

		Log.d(TAG, "Competitors saved: " + mCompetitors);

		//show enter results screen

		Bracket bracket = manager.createBracket();

		manager.saveNewMatchesToDB(bracket);

		enterResultsFragment = EnterResultsFragment.newInstance(bracket);

		Toast.makeText(this, "Competitors saved, start the tournament!", Toast.LENGTH_LONG).show();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content, enterResultsFragment);
		transaction.commit();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.edit().putBoolean(TOURNAMENT_IN_PROGRESS, true).apply();


	}

	@Override
	public void onPause() {
		super.onPause();
		manager.closeDB();
	}

	@Override
	public void matchUpdated(Match match) {
		manager.saveUpdatedMatch(match);
	}
}

package com.clara.brackets.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.clara.brackets.BracketManager;
import com.clara.brackets.R;
import com.clara.brackets.data.Bracket;
import com.clara.brackets.data.Competitor;
import com.clara.brackets.data.Match;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
		EnterCompetitorsFragment.OnEnterCompetitorFragmentInteractionListener,
		EnterResultsFragment.OnMatchUpdated,
		AppLaunchOptionsFragment.OnUserSelectionListener
{

	private static final String TOURNAMENT_IN_PROGRESS = "is tournament in progress?";

	EnterCompetitorsFragment enterCompetitorsFragment;
	EnterResultsFragment enterResultsFragment;

	BracketManager mManager;

	private final String TAG = "MAIN ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//If a competition is in progress, display the Show Results fragment

		//Otherwise, show the EnterCompetitorsFragment

		mManager = new BracketManager(this);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		AppLaunchOptionsFragment appLaunchOptionsFragment = AppLaunchOptionsFragment.newInstance(isCompetitionInProgress());
		transaction.add(R.id.content, appLaunchOptionsFragment);
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

		Log.d(TAG, "Competitors to be saved: " + competitors);

		Bracket bracket = mManager.createBracket(competitors);  //manager keeps a reference to the Bracket

		enterResultsFragment = EnterResultsFragment.newInstance(bracket);

		Toast.makeText(this, "Competitors saved, start the tournament!", Toast.LENGTH_LONG).show();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content, enterResultsFragment);
		transaction.commit();

		//Tournament in progress. Save in shared preferences.
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.edit().putBoolean(TOURNAMENT_IN_PROGRESS, true).apply();


	}

	@Override
	public void onPause() {
		super.onPause();
		mManager.closeDB();
	}


	//Callback from EnterResultsFragment

	@Override
	public void matchUpdated(Match match) {
		mManager.saveUpdatedMatch(match);
	}


	//Callbacks from AppLaunchOptionsFragment

	@Override
	public void startNewTournament() {


		Log.d(TAG, "Deleting existing data. Start new tournament starting EnterCompetitorsFragment");

		//todo - a warning before deleting!
		mManager.clearDatabase();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		enterCompetitorsFragment = EnterCompetitorsFragment.newInstance(null);
		transaction.replace(R.id.content, enterCompetitorsFragment);

		transaction.commit();


	}

	@Override
	public void startNewTournamentWithTestData() {

		Log.d(TAG, "Deleting existing data. Start new tournament starting EnterCompetitorsFragment");

		//todo - a warning before deleting!
		//wipe DB
		mManager.clearDatabase();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		//create new competitors. For testing, create some mock competitors. Replace with null for real app.
		enterCompetitorsFragment = EnterCompetitorsFragment.newInstance(mockCompetitors(14));
		transaction.replace(R.id.content, enterCompetitorsFragment);

		transaction.commit();

	}

	@Override
	public void continueExistingTournament() {

		Log.d(TAG, "Tournament is in progress. Loading data from DB and starting EnterResultsFragment");

		Bracket bracket = mManager.createBracketFromDB();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		enterResultsFragment = EnterResultsFragment.newInstance(bracket);
		transaction.replace(R.id.content, enterResultsFragment);
		transaction.commit();
	}
}

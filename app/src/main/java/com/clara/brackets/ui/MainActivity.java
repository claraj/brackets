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
		EnterResultsFragment.OnMatchUpdated
{

	private static final String TOURNAMENT_IN_PROGRESS = "is tournament in progress?";

	EnterCompetitorsFragment enterCompetitorsFragment;
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

			Log.d(TAG, "Competition is in progress. Loading data from DB and starting EnterResultsFragment");

			Bracket bracket = manager.createBracketFromDB();

			enterResultsFragment = EnterResultsFragment.newInstance(bracket);
			transaction.add(R.id.content, enterResultsFragment);


		} else {

			Log.d(TAG, "No saved competitors, starting EnterCompetitorsFragment");

			//create new competitors. For testing, create some mock competitors. Replace with null for real app.
			enterCompetitorsFragment = EnterCompetitorsFragment.newInstance(mockCompetitors(6));
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

		Log.d(TAG, "Competitors to be saved: " + competitors);

		//manager.setCompetitors();
		Bracket bracket = manager.createBracket(competitors);  //manager keeps a reference to the Bracket

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
		manager.closeDB();
	}


	//Callback from EnterResultsFragment

	@Override
	public void matchUpdated(Match match) {
		manager.saveUpdatedMatch(match);
	}
}

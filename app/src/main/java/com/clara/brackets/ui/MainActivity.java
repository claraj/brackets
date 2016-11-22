package com.clara.brackets.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
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
		transaction.replace(R.id.content, appLaunchOptionsFragment);
		transaction.commit();

	}


	private boolean isCompetitionInProgress() {

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		return pref.getBoolean(TOURNAMENT_IN_PROGRESS, false);
	}



	@Override
	public void onCompetitorListCreated(ArrayList<Competitor> competitors) {

		Log.d(TAG, "Competitors to be saved: " + competitors);

		if (competitors == null || competitors.size() == 0) {

			Log.d(TAG, "No competitors entered");

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			AppLaunchOptionsFragment fragment = AppLaunchOptionsFragment.newInstance(isCompetitionInProgress());
			transaction.replace(R.id.content, fragment);
			transaction.commit();

		}

		else {
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
		transaction.addToBackStack("enter_competitors_transaction");
		transaction.commit();


	}

	@Override
	public void startNewTournamentWithTestData() {

		Log.d(TAG, "Deleting existing data. Start new tournament starting EnterCompetitorsFragment");

		//create new competitors. For testing, create some mock competitors. Replace with null for real app.

		final EditText numberInputET = new EditText(this);
		numberInputET.setInputType(InputType.TYPE_CLASS_NUMBER);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("How many test competitors?")
				.setView(numberInputET)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

						//todo - a warning before deleting!
						//wipe DB
						mManager.clearDatabase();
						try {
							int number = Integer.parseInt(numberInputET.getText().toString());
							if (number <= 0) { throw new NumberFormatException("Number must be 1 or more."); }

							//create desired number of mock competitors
							ArrayList<Competitor> mockCompetitors = mockCompetitors(number);

							//send these to Enter Competitors fragment
							enterCompetitorsFragment = EnterCompetitorsFragment.newInstance(mockCompetitors);

							//launch enter competitors fragment, pre-populated with mock competitors
							FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
							transaction.replace(R.id.content, enterCompetitorsFragment);
							transaction.addToBackStack("enter_competitors_transaction");
							transaction.commit();

						} catch (NumberFormatException nfe) {
							Toast.makeText(MainActivity.this, "Enter positive integer number", Toast.LENGTH_LONG).show();
						}

					}
				})
				.setNegativeButton(android.R.string.cancel, null)
				.create()
				.show();

	}

	@Override
	public void continueExistingTournament() {

		Log.d(TAG, "Tournament is in progress. Loading data from DB and starting EnterResultsFragment");

		Bracket bracket = mManager.createBracketFromDB();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		enterResultsFragment = EnterResultsFragment.newInstance(bracket);
		transaction.replace(R.id.content, enterResultsFragment);
		transaction.addToBackStack("enter_results_transaction");

		transaction.commit();
	}


	private ArrayList<Competitor> mockCompetitors(int num) {

		//This could be more sophisticated - there's a gap between 126-160 which results in getting non-printing characters above z and before extended latin char set symbols start
		//https://en.wikipedia.org/wiki/List_of_Unicode_characters#Basic_Latin
		//It will do for development.

		char name = 65;
		ArrayList<Competitor> mock = new ArrayList<>();
		for (int x = 0 ; x < num ; x++) {
			Competitor c = new Competitor(name++ + "");
			mock.add(c);
		}

		Log.d(TAG, "Mock competitors: " + mock);

		return mock;
	}


}

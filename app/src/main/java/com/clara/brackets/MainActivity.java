package com.clara.brackets;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EnterCompetitorsFragment.OnEnterCompetitorFragmentInteractionListener {

	ArrayList<Competitor> mCompetitors;

	EnterCompetitorsFragment enterCompetitorsFragment;
	ShowResultsFragment showResultsFragment;
	EnterResultsFragment enterResultsFragment;

	Database mDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//If a competition is in progress, display the Show Results fragment

		//Otherwise, show the EnterCompetitorsFragment

		mDatabase = new Database(this);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		if (isCompetitionInProgress()) {
			showResultsFragment = ShowResultsFragment.newInstance();   //todo provide results (?)
			transaction.add(R.id.content, showResultsFragment);
		} else {
			enterCompetitorsFragment = EnterCompetitorsFragment.newInstance(null);
			transaction.add(R.id.content, enterCompetitorsFragment);
		}

		transaction.commit();



	}

	private boolean isCompetitionInProgress() {

		//todo how to check?
		return false;
	}

	@Override
	public void onCompetitorListCreated(ArrayList<Competitor> competitors) {

		mCompetitors = competitors;
		mDatabase.saveCompetitors(mCompetitors);

		//show enter results screen

		enterResultsFragment = EnterResultsFragment.newInstance();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content, enterResultsFragment);
		transaction.commit();



	}
}

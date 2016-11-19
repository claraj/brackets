package com.clara.brackets;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//If a competition is in progress, display the Show Results fragment

		//Otherwise, show the EnterCompetitorsFragment

		Fragment startFragment;

		if (isCompetitionInProgess()) {
			startFragment = ShowResultsFragment.newInstance();
		} else {
			startFragment = EnterCompetitorsFragment.newInstance(null);
		}

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.content, startFragment);


	}

	private boolean isCompetitionInProgess() {

		//todo how to check?
		return false;
	}

}

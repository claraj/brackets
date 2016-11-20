package com.clara.brackets;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by admin on 11/19/16.
 */

public class BracketManager {

	private static final String TAG = "BRACKET MANAGER";
	private Database database;

	ArrayList<Competitor> mCompetitors;

	BracketManager(Context context) {

		database = new Database(context);

	}

	void saveCompetitors(ArrayList<Competitor> competitors) {
		mCompetitors = competitors;
		database.saveCompetitors(competitors);

	}

	public void setCompetitors(ArrayList<Competitor> competitors) {
		this.mCompetitors = competitors;
	}


	public Bracket createBracket() {

		int levels = padCompetitorList();

		Log.d(TAG, "levels of tree needed = " + levels);

		//Randomize

		Collections.shuffle(mCompetitors);

		//list should have power of two elements. Make tree of appropriate height, set each match leaf to pairs of competitor.

		Bracket bracket = new Bracket(levels);

		Log.d(TAG, "created Bracket tree ");
		bracket.logTree();

		bracket.addMatchesAsLeaves(mCompetitors);

		Log.d(TAG, "Added competitors to Bracket tree ");
		bracket.logTree();

		return bracket;

	}

	private int padCompetitorList() {

		//is length power of 2?

		Log.d(TAG, "padding");

		int len =  mCompetitors.size();

		//multiply by 2 until get value larger than size

		int test = 1;

		int power = 0;

		while (true) {


			if (test == len) {
				//a power of two
				Log.d(TAG, "A power of two");
				break;
			}

			if (test >= len) {
				break;
			}

			test *= 2;
			power++;

			Log.d(TAG, "Test, " + test + " power " + power + " leng " + len);


		}

		int padItems = test - mCompetitors.size();

		for (int x = 0 ; x < padItems ; x++) {
			mCompetitors.add(null);
		}

		Log.d(TAG, mCompetitors.toString());

		return power;

	}

	public void saveMatches() {
	}

	public Bracket getCurrentBracket() {
		//read Bracket from DB
		return null;
	}
}

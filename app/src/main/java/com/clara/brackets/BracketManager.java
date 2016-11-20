package com.clara.brackets;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by clara on 11/19/16.  creates bracket, deals with database updates.
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

		Collections.shuffle(mCompetitors);

		int levels = padCompetitorList();

		Log.d(TAG, "levels of tree needed = " + levels);

		//Randomize. Make sure there are not two byes in the same round.

		//list should have power of two elements. Make tree of appropriate height, set each match leaf to pairs of competitor.

		Bracket bracket = new Bracket(levels);

		Log.d(TAG, "created Bracket tree ");
		bracket.logTree();

		bracket.addMatchesAsLeaves(mCompetitors);

		Log.d(TAG, "Added competitors to Bracket tree ");
		bracket.logTree();

		bracket.advanceWinners();   //advances byes from first round

		Log.d(TAG, "Advanced byes (competitors without an opponent for the first round) ");
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

		int insertPos = 0;

		for (int x = 0 ; x < padItems ; x++) {
			mCompetitors.add(insertPos+=2, new Competitor(true));  //bye competitor spaced two apart
		}

		Log.d(TAG, mCompetitors.toString());

		return power;

	}

	public void saveMatches() {
		//todo
	}

	public Bracket getCurrentBracket() {
		//read Bracket from DB
		return null;
	}
}

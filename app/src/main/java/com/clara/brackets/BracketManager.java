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
		database.saveNewCompetitors(competitors);

		Log.d(TAG, "Competitors saved with primary key values:" + competitors);

	}



	public void setCompetitors(ArrayList<Competitor> competitors) {
		this.mCompetitors = competitors;
	}


	public Bracket createBracket() {

		//list should have power of two elements. Make tree of appropriate height, set each match leaf to pairs of competitor.

		Collections.shuffle(mCompetitors);
		int levels = padCompetitorList();   //The number of competitors should be a power of 2. Pad with bye competitors if needed.

		Log.d(TAG, "levels of tree needed = " + levels);

		Bracket bracket = new Bracket(levels);

		Log.d(TAG, "created Bracket tree ");
		bracket.logTree();

		bracket.addMatchesAsLeaves(mCompetitors);

		Log.d(TAG, "Added competitors to Bracket tree ");
		bracket.logTree();

		bracket.advanceWinners();   //advances byes and winners. Here, should just have the opponents of byes advanced.

		Log.d(TAG, "Advanced byes (competitors without an opponent) for the first round ");
		bracket.logTree();

		return bracket;

	}


	private int padCompetitorList() {

		//is length power of 2?

		int len =  mCompetitors.size();

		// Start with 1 and multiply by 2 until get value larger than size of list.
		// Keep count of how many multiplications, which equals the next largest power of 2 than the size.

		int test = 1;

		int power = 0;

		while (true) {


			if (test == len) {
				//a power of two
				Log.d(TAG, "The length of the list is a power of two");
				break;
			}

			if (test >= len) {
				break;
			}

			test *= 2;
			power++;
		}

		int padItems = test - mCompetitors.size();

		int insertPosition = 0;

		for (int x = 0 ; x < padItems ; x++) {

			//bye competitors should be spaced two apart, so the first round doesn't have two Byes playing each other.
			mCompetitors.add(insertPosition, new Competitor(true));
			insertPosition+=2;
		}

		Log.d(TAG, mCompetitors.toString());

		return power;

	}

	public void saveUpdatedMatch(Match match) {

		ArrayList<Match> oneMatch = new ArrayList<>();
		oneMatch.add(match);

		database.updateBracketMatches(oneMatch);

	}


	public void getCompetitorsFromDB() {

		mCompetitors = database.readCompetitors();

	}



	public void saveNewMatchesToDB(Bracket bracket) {
		//todo
		// does not save child-to-parent links, only parent-to-child

		ArrayList<Match> allMatches = bracket.getListOfMatches();
		for (Match m : allMatches){
			database.saveMatchCreateID(m);
		}


		Log.d(TAG, "List of matches with pk: " + allMatches);

		database.updateBracketMatches(allMatches);

	}

	public Bracket getCurrentBracketFromDB() {
		//read Bracket from DB

		ArrayList<Match> matches = database.getAllMatchesForBracket();
		Bracket bracket = new Bracket(matches);
		bracket.setParents();
		return bracket;

	}


	public void closeDB() {
		database.close();
	}

}

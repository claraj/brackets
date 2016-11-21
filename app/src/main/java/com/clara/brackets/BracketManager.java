package com.clara.brackets;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by clara on 11/19/16.
 * Creates bracket, deals with database interaction and updates.
 */

public class BracketManager {

	private static final String TAG = "BRACKET MANAGER";
	private static final String ROOT_DB_ID = "root node match ID";
	private Database database;

	Bracket mBracket;

	private ArrayList<Competitor> mCompetitors;

	private Context context;

	BracketManager(Context context) {
		database = new Database(context);
		this.context = context;
	}


	void saveCompetitors(ArrayList<Competitor> competitors) {
		mCompetitors = competitors;
		database.saveNewCompetitors(competitors);
		Log.d(TAG, "Competitors saved with primary key values:" + competitors);
	}


	public void getCompetitorsFromDB() {
		mCompetitors = database.readCompetitors();
	}


	public void setCompetitors(ArrayList<Competitor> competitors) {
		this.mCompetitors = competitors;
	}



	public Bracket createBracket() {

		//list should have power of two elements. Make tree of appropriate height, set each match leaf to pairs of competitor.

		Collections.shuffle(mCompetitors);
		int levels = padCompetitorList();   //The number of competitors should be a power of 2. Pad with bye competitors if needed.
		//Log.d(TAG, "levels of tree needed = " + levels);

		Bracket bracket = new Bracket(levels);

		Log.d(TAG, "created Bracket tree ");
		//bracket.logTree();

		mBracket = bracket;
		return bracket;
	}


	 void addInitialCompetitors() {

		mBracket.addMatchesAsLeaves(mCompetitors);

		Log.d(TAG, "Added competitors to Bracket tree ");
		//bracket.logTree();

		mBracket.advanceWinners();   //advances byes and winners. Here, should just have the opponents of byes advanced.

		Log.d(TAG, "Advanced byes (competitors without an opponent) for the first round ");
		//bracket.logTree();

		//mBracket = bracket;

		saveAllMatchesToDB();


		//return bracket;

	}


	public void saveUpdatedMatch(Match match) {

		ArrayList<Match> oneMatch = new ArrayList<>();
		oneMatch.add(match);

		database.updateBracketMatches(oneMatch);

	}


	public void saveAllMatchesToDB() {
		ArrayList<Match> allMatches = mBracket.getListOfMatches();
		database.updateBracketMatches(allMatches);
	}


	public void saveNewMatchesToDB() {
		//todo
		// does not save child-to-parent links, only parent-to-child

		ArrayList<Match> allMatches = mBracket.getListOfMatches();

		for (Match m : allMatches){
			database.saveMatchCreateID(m);
		}

		Log.d(TAG, "List of matches with pk: " + allMatches);

		database.updateBracketMatches(allMatches);

//		rootDbId = mBracket.getRootDB_ID();
//
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//		prefs.edit().putLong(ROOT_DB_ID, rootDbId).apply();

	}




	public Bracket createBracketFromDBAndCompetitors() {

		//create blank bracket from competitors  //todo don't add leaves!?
		Bracket bracket = createBracket();

		//read Match info from DB

		ArrayList<Match> matches = database.getAllMatchesForBracket();

		//place matches into bracket

		for (Match match : matches) {
			placeMatchIntoBracket(match);
		}

		bracket.setParents();
		return bracket;

	}


	private void placeMatchIntoBracket(Match match) {

		mBracket.placeMatch(match);

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


	public void closeDB() {
		database.close();
	}

}

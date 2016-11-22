package com.clara.brackets;

import android.content.Context;
import android.util.Log;

import com.clara.brackets.data.Bracket;
import com.clara.brackets.data.Competitor;
import com.clara.brackets.data.Match;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by clara on 11/19/16.
 * Can be used to create a Bracket, and can deal with database interaction and updates.
 */

public class BracketManager {

	private static final String TAG = "BRACKET MANAGER";

	private Database database;
	private Context context;

	public BracketManager(Context context) {
		database = new Database(context);
		this.context = context;
	}


	private void saveCompetitors(ArrayList<Competitor> competitors) {
//		mCompetitors = competitors;
		database.saveNewCompetitors(competitors);
		Log.d(TAG, "Competitors saved with primary key values:" + competitors);
	}


	public ArrayList<Competitor> getCompetitorsFromDB() {
		ArrayList<Competitor> competitors = database.readCompetitors();
		Log.d(TAG, "Competitors read from db: " + competitors);
		return competitors;
	}


//	public void setCompetitors(ArrayList<Competitor> competitors) {
//		this.mCompetitors = competitors;
//	}


	//Creates empty Bracket of designated number of levels
	private Bracket createBracket(int levels) {

		//list should have power of two elements. Make tree of appropriate height, set each match leaf to pairs of competitor.
		Bracket bracket = new Bracket(levels);
		Log.d(TAG, "created Bracket tree ");
		bracket.logTree();
		return bracket;
	}

	//Create bracket with enough levels to hold all of the competitors in the list given.
	public Bracket createBracket(ArrayList<Competitor> competitors) {
		int levels = getNumberOfLevels(competitors.size());
		Bracket bracket = createBracket(levels);
		Log.d(TAG, "Created Bracket");

		addInitialCompetitors(bracket, competitors);
		Log.d(TAG, "Padded competitor list" + competitors);
		bracket.logTree();

		bracket.advanceWinners();
		Log.d(TAG, "Advanced byes (competitors without an opponent) for the first round ");
		bracket.logTree();


		saveNewMatchesToDB(bracket);    //saves new matches created by advancing byes
		saveCompetitors(competitors);   //saves the padded list including byes

		return bracket;
	}


	private void addInitialCompetitors(Bracket bracket, ArrayList<Competitor> competitors) {

		 Collections.shuffle(competitors);

		 padCompetitorList(competitors);   //The number of competitors should be a power of 2. Pad with bye competitors if needed.
		 //Log.d(TAG, "levels of tree needed = " + levels);

		 bracket.addMatchesAsLeaves(competitors);

	}

	private int getNumberOfLevels(int competitors) {
		int levels = (int) Math.ceil( Math.log(competitors) / Math.log(2) ) ;
		Log.d(TAG, "Maths .... levels for size " + competitors + " is " + levels);
		return levels;

	}


	public void saveUpdatedMatch(Match match) {

		ArrayList<Match> oneMatch = new ArrayList<>();
		oneMatch.add(match);
		database.updateBracketMatches(oneMatch);

	}


	public void saveAllMatchesToDB(Bracket bracket) {

		ArrayList<Match> allMatches = bracket.getListOfMatches();

		Log.d(TAG, "All matches to be saved to DB");
		database.updateBracketMatches(allMatches);
	}


	public void saveNewMatchesToDB(Bracket bracket) {

		ArrayList<Match> allMatches = bracket.getListOfMatches();

		for (Match m : allMatches){
			database.saveMatchCreateID(m);
		}

		Log.d(TAG, "List of matches with pk: " + allMatches);

		database.updateBracketMatches(allMatches);

	}


	public Bracket createBracketFromDB() {

		//Only needed to get size of competitor list
//		ArrayList<Competitor> competitors = getCompetitorsFromDB();
		int count = database.competitorCount();
		int levels = getNumberOfLevels(count);

		//create blank bracket from competitors. Don't add leaves
		Bracket bracket = createBracket(levels);

		//read Match info from DB
		ArrayList<Match> matches = database.getAllMatchesForBracket();

		Log.d(TAG, "matches from DB = " + matches);

		//place matches into BracketNodes
		for (Match match : matches) {
			bracket.placeMatch(match);
		}

		bracket.linkParents();
		bracket.advanceWinners();
		return bracket;

	}


//	private void placeMatchIntoBracket(BracjMatch match) {
//		mBracket.placeMatch(match);
//
//	}


	private void padCompetitorList(ArrayList<Competitor> competitors) {

		//is length power of 2?

		int len =  competitors.size();

		int levels = getNumberOfLevels(competitors.size());

		int totalStart = (int) Math.pow(2, levels);

		int padItems = totalStart - len;

		int insertPosition = 0;

		for (int x = 0 ; x < padItems ; x++) {

			//bye competitors should be spaced two apart, so the first round doesn't have two Byes playing each other.
			competitors.add(insertPosition, new Competitor(true));
			insertPosition+=2;
		}

		Log.d(TAG, "After padding, the list of competitors is " + competitors.toString());

		//return competitors;

	}



	public void closeDB() {
		database.close();
	}

}

package com.clara.brackets;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by admin on 11/19/16.
 */

public class Bracket implements Parcelable {
	private static final String TAG = "BRACKET TREE";

	//A tree of matches.

	Match root;   //points to the other matches

	int levels;

	ArrayList<Match> allMatchesAtLevel(int level) {
		//todo

		ArrayList<Match> matches = new ArrayList<>();

		addLevelMatchToResults(level, root, matches);

		Log.d(TAG, "Matches at this level: " + level + " " + matches);

		return matches;
	}

	public int getLevels() {
		return levels;
	}

	void addLevelMatchToResults(int level, Match node, ArrayList<Match> matchesList) {

		if (node == null) {
			return;
		}

		if (node.level == level) {
			matchesList.add(node);
			return;
		}

		else {

			addLevelMatchToResults(level, node.leftChild, matchesList);
			addLevelMatchToResults(level, node.rightChild, matchesList);

		}

	}



	Bracket(int levels) {

		this.levels = levels;

		root = new Match(levels-1);   // for a 4-level tree, this is 3

		//Create tree of given level. All competitors will be null, but leftChild,
		// rightChild should point to correct children and
		// TODO nodes should be numbered
		// and have levels assigned

		root.addEmptyChildren();
		Match.nodeNumberCounter = 0;
		root.addNodeNumbers();

	}

	void addMatchesAsLeaves(ArrayList<Competitor> competitors) {

		root.setCompetitorsAsLeaves(competitors);   //hopefully, enough competitors for leaves :)

	}





	void addChild() {

	}


	ArrayList<Match> getListOfMatches() {
		return null;  //todo

		//traverse tree. output nodes.
	}

	Bracket(ArrayList<Match> matches) {

		//re-create tree by doing reverse of method above.
	}


	public void logTree() {

		Log.d(TAG, root.toString());

		print(root);
	}

	void print(Match node) {

		Log.d(TAG, node.toString());

		if (node.leftChild != null) {
			print(node.leftChild);
		}

		if (node.rightChild != null) {
			print(node.rightChild);
		}

	}


	//TODO (or write and read from db...)

	protected Bracket(Parcel in) {
	}

	public static final Creator<Bracket> CREATOR = new Creator<Bracket>() {
		@Override
		public Bracket createFromParcel(Parcel in) {
			return new Bracket(in);
		}

		@Override
		public Bracket[] newArray(int size) {
			return new Bracket[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
	}

	//A tree of matches

}

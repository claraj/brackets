package com.clara.brackets;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Clara on 11/19/16.
 * A Bracket is a full tree structure
 * Each node is a Match element (all nodes have 2 children, except the leaves, which have none)
 * storing all of the Matches for this tournament.
 */

public class Bracket implements Parcelable {
	private static final String TAG = "BRACKET TREE";

	//A tree of matches.

	private Match root;   //points to the a left child and a right child Match, which in turn, point to their child Match objects

	private int levels;

	// Create a full tree of Matches with the desired number of levels.  All levels are full.
	// All competitors will be null,
	// leftChild, rightChild should point to correct children
	// nodes should be numbered, with a unique number
	// each node should have the correct level assigned. The leave nodes are level 0; the root is (number of levels - 1)

	Bracket(int levels) {

		this.levels = levels;

		root = new Match(levels-1, false);   	// Create the root node. for a 4-level tree, this is 3. Second argument isLeftChild.
		root.addEmptyChildren();		// Add empty children. This method will recurse through the tree, adding children
		Match.nodeNumberCounter = 0;    // Initialize a static counter to number the nodes, from 0.
		root.addNodeNumbers();			// Add number to the root, then this method will recurse through the nodes adding an ID number to each node
		root.setParents();				// Recurse through the tree and create a reference to each node's parent node

	}

	public void setParents() {
		root.setParents();
	}


	public int getLevels() {
		return levels;
	}



	//Traverse the tree. If a Match (node) is at the desired level, add it to a list.
	ArrayList<Match> allMatchesAtLevel(int level) {

		ArrayList<Match> matches = new ArrayList<>();
		addLevelMatchToResults(level, root, matches);
		Log.d(TAG, "Matches at this level: " + level + " " + matches);
		return matches;
	}

	//Companion method to the above. Checks a node for being at a particular level, then calls this method on any non-null children.
	private void addLevelMatchToResults(int level, Match node, ArrayList<Match> matchesList) {

		if (node == null) {
			return;
		}

		if (node.level == level) {
			matchesList.add(node);
		}

		else {
			addLevelMatchToResults(level, node.leftChild, matchesList);
			addLevelMatchToResults(level, node.rightChild, matchesList);
		}

	}


	//Useful for the initial empty, start tree. Assign one competitor to each leaf node.
	void addMatchesAsLeaves(ArrayList<Competitor> competitors) {
		int leaves = (int) Math.pow(2, levels);
		if (competitors.size() != leaves) {
			Log.e(TAG, "Wrong number of competitors for tree, need " + leaves + " but got " + competitors.size());
			throw new RuntimeException("Wrong number of competitors for tree, need " + leaves + " but got " + competitors.size());
		}
		root.setCompetitorsAsLeaves(competitors);   //hopefully, enough competitors for leaves :)
	}



	ArrayList<Match> getListOfMatches() {
		//TODO traverse tree; output a list of nodes for saving to the database

		ArrayList<Match> matches = new ArrayList<>();

		matches.add(root);

		root.addChildrenToList(matches);

		return matches;


	}

	Bracket(ArrayList<Match> matches) {
		//TODO re-create tree by doing reverse of method above.

	}


	//toString isn't going to work very well here.
	// These two methods log all of the tree's nodes to the console.
	public void logTree() {
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



	public void advanceWinners() {
		root.advanceWinningChildren();
	}



	public void updateMatchWinnerAndDate(Match match) {

		//find this match in the tree, save new winner
		root.findAndUpdateMatchWinner(match);

	}


	//TODO - parcelable implementation? There are cyclic references because children have references to their parents, and parents have references to their children
	// When parceling, will need to break the child-to-parent links, and then re-create when un-parcelling.
	// Also, it may work better to flatten the tree into an arraylist with getListOfMatches and then recreate it with the Bracket(ArrayList<Matches>) constructir.

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

}

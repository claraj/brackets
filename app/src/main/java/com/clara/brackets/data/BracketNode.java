package com.clara.brackets.data;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by clara on 11/19/16.
 * A node in the brackets tree.
 */


public class BracketNode  {

	private static final String TAG = "BRACKET NODE";

	int level;    // 0 is the lowest level of the tree. In a 4-level tree, the top level is 3.

	Match match;

	BracketNode leftChild;
	BracketNode rightChild;

	BracketNode parent;

	boolean isLeftChild;

	int nodeId;    //numbers the nodes in this Bracket tree


	BracketNode(int level, boolean isLeftChild) {
		this.level = level;
		this.isLeftChild = isLeftChild;
		match = new Match();
	}


	void addEmptyChildren() {

		match = new Match();   //new empty match

		if (level == 0) {
			return;           //do not add children to level 0
		}


		leftChild = new BracketNode(level - 1, true);
		leftChild.addEmptyChildren();
		rightChild = new BracketNode(level - 1, false);
		rightChild.addEmptyChildren();

	}


	public void addNodeNumbers() {

		this.nodeId = Bracket.nodeNumberCounter++;
		match.nodeId = nodeId;

		if (leftChild != null) {
			leftChild.addNodeNumbers();
		}

		if (rightChild != null) {
			rightChild.addNodeNumbers();
		}

	}



	//assign parent of each node to be the node id of the parent
	public void linkParent() {

		//find your children and set their parent Bracket object to you

		Log.d(TAG, "Link parent");

		if (leftChild != null) {
			leftChild.parent = this;
			leftChild.linkParent();
		}

		if (rightChild != null) {
			rightChild.parent = this;
			rightChild.linkParent();
		}

	}



	public void advanceWinningChildren() {

		//If root, return

		Log.d(TAG, "Advance winning children for match " + match);

		//If no parent, then nowhere to advance to
		if (parent == null) {
			Log.d(TAG, "This is the top of the tree " + match);
		}


		//Request children advance their winners
		if (leftChild != null) {
			leftChild.advanceWinningChildren();
		}

		if (rightChild != null) {
			rightChild.advanceWinningChildren();
		}


		//TODO test that match.comp1 and comp2 are not null

		if (match.comp_2 != null && match.comp_1 != null) {

			//Advance oppoenents of byes
			if (match.comp_1.bye) {
				//comp 2 wins
				match.winner = match.comp_2;
			}

			if (match.comp_2.bye) {
				//comp 1 wins
				match.winner = match.comp_1;
			}

			//If there is a parent, then advance the winner to the parent
			if (match.winner != null && parent != null) {
				if (isLeftChild) {
					parent.match.comp_1 = match.winner;    //Advance left child winner to comp_1 of parent
				} else {
					parent.match.comp_2 = match.winner;    //Advance right child winner to comp_2 of parent
				}

			}
		}
		Log.d(TAG, "Advanced winning children, now match is " + match);

	}



	void addChildrenToList(ArrayList<Match> matches) {

		if (leftChild != null) {
			matches.add(leftChild.match);
			leftChild.addChildrenToList(matches);
		}

		if (rightChild != null) {
			matches.add(rightChild.match);
			rightChild.addChildrenToList(matches);
		}

	}


	void setCompetitorsAsLeaves(ArrayList<Competitor> competitors) {

		if (level == 0) {
			match.comp_1 = competitors.remove(0);
			match.comp_2 = competitors.remove(0);
		}

		else {
			leftChild.setCompetitorsAsLeaves(competitors);
			rightChild.setCompetitorsAsLeaves(competitors);
		}
	}



	void findAndUpdateMatch(Match updateMatch) {

		//Find BracketNode by node_ID. Replace match with updateMatch.

		if (nodeId == updateMatch.nodeId) {
			match = updateMatch;
		}

		else {
			if (leftChild != null) {
				leftChild.findAndUpdateMatch(updateMatch);
			}

			if (rightChild != null) {
				rightChild.findAndUpdateMatch(updateMatch);
			}
		}
	}




	@Override
	public String toString() {
		return "BracketNode{" +
				"nodeId=" + nodeId +
				//"database pk" + db_id +
				" match " + match +
				", level=" + level +
				", leftChild=" + leftChild +
				", rightChild=" + rightChild +
				//", parentId=" + parent.nodeId +
				'}';
	}


}



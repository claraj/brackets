package com.clara.brackets.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.clara.brackets.Database;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by clara on 11/19/16.
 * A node in the brackets tree.
*/


public class Match implements Parcelable {

	private static final String TAG = "MATCH";

	public long db_id;   //primary key from the database

	public Competitor comp_1;
	public Competitor comp_2;
	public Competitor winner;

	public int level;    // 0 is the lowest level of the tree. In a 4-level tree, the top level is 3.

	public Date matchDate;

	public Match leftChild;
	public Match rightChild;

	public Match parent;

	public boolean isLeftChild;

	public int nodeId;

	public Match(int level, boolean isLeftChild) {
		this.level = level;
		this.isLeftChild = isLeftChild;

	}

	public Match() {}  // used by DB


	void addEmptyChildren() {

		if (level == 0) {
			return;
		}

		leftChild = new Match(level - 1, true);
		leftChild.addEmptyChildren();
		rightChild = new Match(level - 1, false);
		rightChild.addEmptyChildren();

	}


	static int nodeNumberCounter;


	public void addNodeNumbers() {

		this.nodeId = nodeNumberCounter++;

		if (leftChild != null) {
			leftChild.addNodeNumbers();
		}

		if (rightChild != null) {
			rightChild.addNodeNumbers();
		}

	}


	public void setCompetitorsAsLeaves(ArrayList<Competitor> competitors) {

		if (level == 0) {
			comp_1 = competitors.remove(0);
			comp_2 = competitors.remove(0);
		}

		else {
			leftChild.setCompetitorsAsLeaves(competitors);
			rightChild.setCompetitorsAsLeaves(competitors);
		}


	}

	//assign parent of each node to be the node id of the parent
	public void setParents() {

		//find your children and set their parent Match object to you

		if (leftChild != null) {
			leftChild.parent = this;
			leftChild.setParents();
		}

		if (rightChild != null) {
			rightChild.parent = this;
			rightChild.setParents();
		}

	}



	public void advanceWinningChildren() {

		//If root, return

		if (parent == null) {
			return;
		}

		if (comp_1 != null && comp_2 != null && (comp_1.bye || winner == comp_2 )) {

			//comp_2 is winner, advance comp_2 to parentMatch

			winner = comp_2;

			if (isLeftChild) {
				parent.comp_1 = winner;
			} else {
				parent.comp_2 = winner;
			}

		}

		if (comp_2 != null && comp_1 != null && (comp_2.bye || winner == comp_1)) {
			//comp_1 is winner, advance comp_1 to parent Match
			winner = comp_1;

			if (isLeftChild) {
				parent.comp_1 = winner;
			} else {
				parent.comp_2 = winner;
			}
		}


		if (leftChild != null) {
			leftChild.advanceWinningChildren();
		}

		if (rightChild != null) {
			rightChild.advanceWinningChildren();
		}


	}



	public void addChildrenToList(ArrayList<Match> matches) {

		if (leftChild != null) {
			matches.add(leftChild);
			leftChild.addChildrenToList(matches);
		}

		if (rightChild != null) {
			matches.add(rightChild);
			rightChild.addChildrenToList(matches);
		}

	}



	void findAndUpdateMatchWinner(Match matchNode) {

		if (nodeId == matchNode.nodeId) {

			//set winner to whatever matchNode's winner is. TODO Anything else to update?

			winner = matchNode.winner;
			matchDate = matchNode.matchDate;

		}

		else {

			if (leftChild != null) {
				leftChild.findAndUpdateMatchWinner(matchNode);
			}

			if (rightChild != null) {
				rightChild.findAndUpdateMatchWinner(matchNode);
			}
		}
	}


	public void placeMatchInTree(Match match) {

		if (nodeId == match.nodeId) {

			//update pk - todo anything else?
			db_id = match.db_id;
			comp_1 = match.comp_1;
			comp_2 = match.comp_2;
			winner = match.winner;

		}

		else {

			//keep searching for node with same nodeId

			if (leftChild != null) {
				leftChild.placeMatchInTree(match);
			}

			if (rightChild != null) {
				rightChild.placeMatchInTree(match);
			}
		}

	}

	@Override
	public String toString() {
		return "Match{" +
				"nodeId=" + nodeId +
				"database pk" + db_id +
				", comp_1=" + comp_1 +
				", comp_2=" + comp_2 +
				", winner=" + winner +
				", level=" + level +
				", matchDate=" + matchDate +
				", leftChild=" + leftChild +
				", rightChild=" + rightChild +
				//", parentId=" + parent.nodeId +
				'}';
	}


	//TODO Verify this.

	protected Match(Parcel in) {
		comp_1 = in.readParcelable(Competitor.class.getClassLoader());
		comp_2 = in.readParcelable(Competitor.class.getClassLoader());
		winner = in.readParcelable(Competitor.class.getClassLoader());
		level = in.readInt();
		leftChild = in.readParcelable(Match.class.getClassLoader());
		rightChild = in.readParcelable(Match.class.getClassLoader());
		parent= in.readParcelable(Match.class.getClassLoader());
		nodeId = in.readInt();
	}

	public static final Creator<Match> CREATOR = new Creator<Match>() {
		@Override
		public Match createFromParcel(Parcel in) {
			return new Match(in);
		}

		@Override
		public Match[] newArray(int size) {
			return new Match[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeParcelable(comp_1, i);
		parcel.writeParcelable(comp_2, i);
		parcel.writeParcelable(winner, i);
		parcel.writeInt(level);
		parcel.writeParcelable(leftChild, i);
		parcel.writeParcelable(rightChild, i);
//		parcel.writeParcelable(parent, i);
		parcel.writeInt(nodeId);
	}

	public void addValues(ContentValues values) {


		//	Competitor comp_1;  (save id), and if bye or not
		if (comp_1 != null) {
			values.put(Database.COMP_1_ID, comp_1.id);
		} else {
			values.put(Database.COMP_1_ID, -1);   //no competitor
		}

		// 0 is false and 1 is true
		if (comp_1 != null && comp_1.bye) {
			values.put(Database.COMP_1_IS_BYE, 1);
		} else {
			values.put(Database.COMP_1_IS_BYE, 0);
		}

		//	Competitor comp_2;  (save id), and if bye or not
		if (comp_2 != null) {
			values.put(Database.COMP_2_ID, comp_2.id);
		} else {
			values.put(Database.COMP_2_ID, -1);   //no competitor
		}

		if (comp_2 != null && comp_2.bye) {
			values.put(Database.COMP_2_IS_BYE, 1);
		} else {
			values.put(Database.COMP_2_IS_BYE, 0);     //byes still have id numbers.
		}

		// Competitor winner;  (save id)
		if (winner != null) {
			values.put(Database.WINNER_ID, winner.id);
		} else {
			values.put(Database.WINNER_ID, -1);   //no winner yet
		}

		//	int level;    // 0 is the lowest level of the tree. In a 4-level tree, the top level is 3.
		values.put(Database.LEVEL, level);

		//			Date matchDate;
		if (matchDate != null)  {
			values.put(Database.MATCH_DATE, matchDate.getTime());
		} else {
			values.put(Database.MATCH_DATE, -1);
		}


//		//	Match leftChild; (save db_id)
//		if (leftChild != null) {
//			values.put(Database.LEFT_CHILD_ID, leftChild.db_id);
//		} else {
//			values.put(Database.LEFT_CHILD_ID, -1);   //no left child
//		}
//
//
//		//Match rightChild; (save db_id)
//		if (rightChild != null) {
//			values.put(Database.RIGHT_CHILD_ID, rightChild.db_id);
//		} else {
//			values.put(Database.RIGHT_CHILD_ID, -1);   //no left child
//		}
//
//
//		//	boolean isLeftChild;
//		// 0 is false and 1 is true
//		if (isLeftChild) {
//			values.put(Database.IS_LEFT_CHILD, 1);
//		} else {
//			values.put(Database.IS_LEFT_CHILD, 0);
//		}

		//	int nodeId;
		values.put(Database.NODE_ID, nodeId);

	}


}

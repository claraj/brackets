package com.clara.brackets;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by clara on 11/19/16.
 */

//A node in the brackets tree.

public class Match implements Parcelable {


	Competitor comp_1;
	Competitor comp_2;
	Competitor winner;     // or should be ids?

	int level;    // 0 is the lowest level of the tree. In a 4-level tree, the top level is 3.

	Date matchDate;

	Match leftChild;
	Match rightChild;

	int parentId;

	int nodeId;

	Match(int level) {
		this.level = level;
	//	this.nodeId = nodeId;
	}

	Match() {

	}

	void addEmptyChildren() {


		if (level == 0) {
			return;
		}

		leftChild = new Match(level - 1);
		leftChild.addEmptyChildren();
		rightChild = new Match(level - 1);
		rightChild.addEmptyChildren();


	}


	static int nodeNumberCounter;

	public void addNodeNumbers() {

		this.nodeId = nodeNumberCounter++;

		if (leftChild != null) {
			//leftChild.nodeId = nodeNumberCounter++;
			leftChild.addNodeNumbers();
		}

		if (rightChild != null) {
			//rightChild.nodeId = nodeNumberCounter++;
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



	@Override
	public String toString() {
		return "Match{" +
				"nodeId=" + nodeId +
				", comp_1=" + comp_1 +
				", comp_2=" + comp_2 +
				", winner=" + winner +
				", level=" + level +
				", matchDate=" + matchDate +
				", leftChild=" + leftChild +
				", rightChild=" + rightChild +
				", parentId=" + parentId +
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
		parentId = in.readInt();
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
		parcel.writeInt(parentId);
		parcel.writeInt(nodeId);
	}

}

package com.clara.brackets.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.clara.brackets.Database;

import java.util.Date;

/**
 * Created by clara on 11/19/16.
 * A node in the brackets tree.
*/


//the competitors, winner, is bye or not, comp date.

public class Match implements Parcelable {

	private static final String TAG = "MATCH";
	public static final int NO_DATE = -1;

	public long db_id;   //primary key from the database (?)

	public Competitor comp_1;
	public Competitor comp_2;
	public Competitor winner;

	public Date matchDate;

	public int nodeId;


	public Match() {}  // used by DB


	protected Match(Parcel in) {
		db_id = in.readLong();
		comp_1 = in.readParcelable(Competitor.class.getClassLoader());
		comp_2 = in.readParcelable(Competitor.class.getClassLoader());
		winner = in.readParcelable(Competitor.class.getClassLoader());
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

	//
//	public void placeMatchInTree(Match match) {
//
//		if (nodeId == match.nodeId) {
//
//			//update pk - todo anything else?
//			db_id = match.db_id;
//			comp_1 = match.comp_1;
//			comp_2 = match.comp_2;
//			winner = match.winner;
//
//		}
//
//		else {
//
//			//keep searching for node with same nodeId
//
//			if (leftChild != null) {
//				leftChild.placeMatchInTree(match);
//			}
//
//			if (rightChild != null) {
//				rightChild.placeMatchInTree(match);
//			}
//		}
//
//	}
//
	@Override
	public String toString() {
		return "Match{" +
				"nodeId=" + nodeId +
				", database pk " + db_id +
				", comp_1=" + comp_1 +
				", comp_2=" + comp_2 +
				", winner=" + winner +
				", matchDate=" + matchDate +
				'}';
	}
//
//
//

	public void addValues(ContentValues values) {


		//	Competitor comp_1,  save id, and if bye or not
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

		//	Competitor comp_2,  save id, and if bye or not
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

		// Competitor winner,  save id
		if (winner != null) {
			values.put(Database.WINNER_ID, winner.id);
		} else {
			values.put(Database.WINNER_ID, -1);   //no winner yet
		}


		if (matchDate != null)  {
			values.put(Database.MATCH_DATE, matchDate.getTime());
		} else {
			values.put(Database.MATCH_DATE, NO_DATE);
		}

		// nodeId;
		values.put(Database.NODE_ID, nodeId);

	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeLong(db_id);
		parcel.writeParcelable(comp_1, i);
		parcel.writeParcelable(comp_2, i);
		parcel.writeParcelable(winner, i);
		parcel.writeInt(nodeId);
	}
}

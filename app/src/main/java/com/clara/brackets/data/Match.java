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

	public long db_id;   //primary key from the database

	public Competitor comp_1;
	public Competitor comp_2;
	public Competitor winner;

	public Date matchDate;

	public int nodeId;

	public Match() {}  // used by DB


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


	//Convenience method for the database. Add all fields to ContentValues object
	public void addValues(ContentValues values) {

		//	Competitor comp_1,  save id, and if bye or not
		if (comp_1 != null) {
			values.put(Database.COMP_1_ID, comp_1.id);
		} else {
			values.put(Database.COMP_1_ID, -1);   //no competitor
		}


		//	Competitor comp_2,  save id, and if bye or not
		if (comp_2 != null) {
			values.put(Database.COMP_2_ID, comp_2.id);
		} else {
			values.put(Database.COMP_2_ID, -1);   //-1 to indicate no competitor
		}


		// Competitor winner,  save id
		if (winner != null) {
			values.put(Database.WINNER_ID, winner.id);
		} else {
			values.put(Database.WINNER_ID, -1);   //-1 to indicate no winner yet
		}


		//match date, as long timestamp
		if (matchDate != null)  {
			values.put(Database.MATCH_DATE, matchDate.getTime());
		} else {
			values.put(Database.MATCH_DATE, NO_DATE);
		}

		// nodeId;
		values.put(Database.NODE_ID, nodeId);

	}


	/* Parcelable implementation */

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

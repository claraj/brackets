package com.clara.brackets;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 11/19/16.
 */

public class Competitor implements Parcelable {

	static final String BYE = "* BYE *";

	String name;
	int id;        //from database.

	boolean bye;   // Does this represent a bye - the other competitor automatically wins?

	public Competitor(String name) {
		this.name = name;
		bye = false;
	}

	public Competitor(boolean bye) {
		this.bye = bye;
		name = BYE;
	}

	@Override
	public String toString() {
		return name;
	}



	//todo include bye value
	protected Competitor(Parcel in) {
		name = in.readString();
		id = in.readInt();
	}

	public static final Creator<Competitor> CREATOR = new Creator<Competitor>() {
		@Override
		public Competitor createFromParcel(Parcel in) {
			return new Competitor(in);
		}

		@Override
		public Competitor[] newArray(int size) {
			return new Competitor[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(name);
		parcel.writeInt(id);
	}
}

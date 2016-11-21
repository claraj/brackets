package com.clara.brackets;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 11/19/16.
 */

public class Competitor implements Parcelable {

	static final String BYE = "* BYE *";

	String name;
	long id;        //from database.

	boolean bye;   // Does this represent a bye - the other competitor automatically wins?

	public Competitor(String name) {
		this.name = name;
		bye = false;
	}

	public Competitor(boolean bye) {
		this.bye = bye;
		name = BYE;
	}

	public Competitor(String name, long id, boolean bye) {
		this.name = name;
		this.bye = bye;
		this.id = id;
	}


	@Override
	public String toString() {
		return name + " id " + id + " is bye? " + bye;
	}

	protected Competitor(Parcel in) {
		name = in.readString();
		id = in.readLong();
		bye = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeLong(id);
		dest.writeByte((byte) (bye ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
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



}
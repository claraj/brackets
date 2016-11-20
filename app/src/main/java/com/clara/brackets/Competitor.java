package com.clara.brackets;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 11/19/16.
 */

public class Competitor implements Parcelable {

	String name;
	int id;

	public Competitor(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}


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

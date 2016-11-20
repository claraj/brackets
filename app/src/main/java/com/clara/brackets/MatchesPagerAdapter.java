package com.clara.brackets;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by me on 11/19/16. Adapter for pager view.
 *
 * todo recycling views (?)
 */

public class MatchesPagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = "MATCHES PAGER ADAPTER";
	Bracket mBracket;

	LevelOfBracketFragment[] allFragments;

	public MatchesPagerAdapter(FragmentManager fm, Bracket bracket) {
		super(fm);
		mBracket = bracket;
		allFragments = new LevelOfBracketFragment[mBracket.getLevels()];

	}




	@Override
	public Fragment getItem(int position) {

		ArrayList<Match> matches = mBracket.allMatchesAtLevel(position);
		Log.d(TAG, "Adapter get item position " + position + matches.toString());

		LevelOfBracketFragment fragment = LevelOfBracketFragment.newInstance( mBracket.allMatchesAtLevel(position), position);

		if ( allFragments[position] == null) {
			allFragments[position] = fragment;
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return mBracket.getLevels();
	}

	public void setBracket(Bracket bracket) {
		this.mBracket = bracket;

		//todo tell current Fragment displayed to update

		//todo this seems hacky...

		for (int page = 0 ; page < getCount() ; page++) {
			if (allFragments[page] != null) {
				allFragments[page].updateList(mBracket.allMatchesAtLevel(page));
			}
		}

	}
}

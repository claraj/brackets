package com.clara.brackets;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by me on 11/19/16. Adapter for pager view.
 *
 * todo recycling views
 */

public class MatchesPagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = "MATCHES PAGER ADAPTER";
	Bracket mBracket;

	public MatchesPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	LevelOfBracketFragment currentFragment;
	int currentPosition;

	@Override
	public Fragment getItem(int position) {

		ArrayList<Match> matches = mBracket.allMatchesAtLevel(position);
		Log.d(TAG, "Adapter get item position " + position + matches.toString());

		currentFragment = LevelOfBracketFragment.newInstance( mBracket.allMatchesAtLevel(position), position);
		currentPosition = position;

		return currentFragment;
	}

	@Override
	public int getCount() {
		return mBracket.getLevels();
	}

	public void setBracket(Bracket bracket) {
		this.mBracket = bracket;

		//todo tell current Fragment displayed to update

		//this seems hacky...

		currentFragment.updateList(mBracket.allMatchesAtLevel(currentPosition));


	}
}

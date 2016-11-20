package com.clara.brackets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * SQLite interaction.  Save competitor list, and current status of tournament bracket
 *
 */

public class Database {

	private Context context;
	private SQLHelper helper;
	private SQLiteDatabase db;

	private final String TAG = "DATABASE";

	private  static final String DB_NAME = "brackets.db";

	private  static final int DB_VERSION = 1;


	//Two tables: one for competitors
	//one for results of a particular matchup

	private final String COMPETITORS_TABLE = "competitors";
	private final String COMP_ID = "_id";
	private final String COMPETITOR_NAME = "name";
	private final String COMP_IS_BYE = "is_bye";


	/*

	Competitor comp_1;  (save id)
			//and if is bye
	Competitor comp_2;  (save id)
			and if is bye

	Competitor winner;  (save id)

	int level;    // 0 is the lowest level of the tree. In a 4-level tree, the top level is 3.

	Date matchDate;

	Match leftChild;
	Match rightChild;

	Match parent;

	boolean isLeftChild;
	int nodeId;

*/

	private final String MATCHES_TABLE = "matches";
	private final String MATCH_ID = "_id";   //primary key

	private final String COMP_1_ID = "competitor_1_id";
	private final String COMP_1_IS_BYE = "competitor_1_is_bye";

	private final String COMP_2_ID = "competitor_2_id";
	private final String COMP_2_IS_BYE = "competitor_2_is_bye";

	private final String WINNER_ID = "winner_id";

	private final String LEVEL = "tree_level";
	private final String NODE_ID = "node_id";      //node in Bracket tree
	private final String MATCH_DATE = "match_date";

	private final String LEFT_CHILD_ID = "left_child_id";
	private final String RIGHT_CHILD_ID = "right_child_id";

	private final String IS_LEFT_CHILD = "is_left_child";

	public Database(Context c) {
		this.context = c;
		helper = new SQLHelper(c);
		this.db = helper.getWritableDatabase();
	}

	public void close() {
		helper.close(); //Closes the database - very important!
	}

	public void saveNewCompetitors(ArrayList<Competitor> competitors) {

		for (Competitor c : competitors)  {

			ContentValues newCompetitor = new ContentValues();
			newCompetitor.put(COMPETITOR_NAME, c.name);
			long pk = db.insertOrThrow(COMPETITORS_TABLE, null, newCompetitor);
			c.id = pk;

		}



	}

	public void saveMatchCreateID(Match match) {

		ContentValues values = new ContentValues();

		if (match.comp_1 != null) {
			values.put(COMP_1_ID, match.comp_1.id);
		} else {
			values.put(COMP_1_ID, -1);   //no competitor
		}

		if (match.comp_1 != null && match.comp_1.bye) {
			values.put(COMP_1_IS_BYE, true);
		} else {
			values.put(COMP_1_IS_BYE, false);
		}

		//	Competitor comp_2;  (save id), and if bye or not

		if (match.comp_2 != null) {
			values.put(COMP_2_ID, match.comp_2.id);
		} else {
			values.put(COMP_2_ID, -1);   //no competitor
		}

		if (match.comp_2 != null && match.comp_2.bye) {
			values.put(COMP_2_IS_BYE, true);
		} else {
			values.put(COMP_2_IS_BYE, false);     //byes still have id numbers.
		}


		// Competitor winner;  (save id)

		if (match.winner != null) {
			values.put(WINNER_ID, match.winner.id);
		} else {
			values.put(WINNER_ID, -1);   //no winner yet
		}


		//	int level;    // 0 is the lowest level of the tree. In a 4-level tree, the top level is 3.

		values.put(LEVEL, match.level);

		//			Date matchDate;

		if (match.matchDate != null)  {
			values.put(MATCH_DATE, match.matchDate.getTime());
		} else {
			values.put(MATCH_DATE, -1);
		}


		//	boolean isLeftChild;

		// 0 is false and 1 is true

		if (match.isLeftChild) {
			values.put(IS_LEFT_CHILD, 1);
		} else {
			values.put(IS_LEFT_CHILD, 0);
		}

		//	int nodeId;


		values.put(NODE_ID, match.nodeId);


		long id = db.insert(MATCHES_TABLE, null, values);

		match.db_id = id;



	}

	public void updateBracketMatches(ArrayList<Match> matches) {

		for (Match match : matches) {

			ContentValues values = new ContentValues();

//			Competitor comp_1;  (save id)

			if (match.comp_1 != null) {
				values.put(COMP_1_ID, match.comp_1.id);
			} else {
				values.put(COMP_1_ID, -1);   //no competitor
			}

			// 0 is false and 1 is true

			if (match.comp_1 != null && match.comp_1.bye) {
				values.put(COMP_1_IS_BYE, 1);
			} else {
				values.put(COMP_1_IS_BYE, 0);
			}


			//	Competitor comp_2;  (save id), and if bye or not


			if (match.comp_2 != null) {
				values.put(COMP_2_ID, match.comp_2.id);
			} else {
				values.put(COMP_2_ID, -1);   //no competitor
			}

			// 0 is false and 1 is true

			if (match.comp_2 != null && match.comp_2.bye) {
				values.put(COMP_2_IS_BYE, 1);
			} else {
				values.put(COMP_2_IS_BYE, 0);     //byes still have id numbers.
			}


			// Competitor winner;  (save id)

			if (match.winner != null) {
				values.put(WINNER_ID, match.winner.id);
			} else {
				values.put(WINNER_ID, -1);   //no winner yet
			}


			//	int level;    // 0 is the lowest level of the tree. In a 4-level tree, the top level is 3.

			values.put(LEVEL, match.level);

			//			Date matchDate;

			if (match.matchDate != null)  {
				values.put(MATCH_DATE, match.matchDate.getTime());
			} else {
				values.put(MATCH_DATE, -1);
			}

			//	Match leftChild; (save id)

			if (match.leftChild != null) {
				values.put(LEFT_CHILD_ID, match.leftChild.db_id);
			} else {
				values.put(LEFT_CHILD_ID, -1);   //no left child
			}


			//Match rightChild; (save id)

			if (match.rightChild != null) {
				values.put(RIGHT_CHILD_ID, match.rightChild.db_id);
			} else {
				values.put(RIGHT_CHILD_ID, -1);   //no left child
			}


			//			Match parent; (save id)   (?)


			//			boolean isLeftChild;

			// 0 is false and 1 is true

			if (match.isLeftChild) {
				values.put(IS_LEFT_CHILD, 1);
			} else {
				values.put(IS_LEFT_CHILD, 0);
			}

			//			int nodeId;


			values.put(NODE_ID, match.nodeId);


			String where = MATCH_ID + " = " + match.db_id;

			db.update(MATCHES_TABLE, values, where, null);

		}

	}


	public void allData() {

		Cursor c = db.query(MATCHES_TABLE, null, null, null, null, null, null); //select * from match table

		while (c.moveToNext()) {

	/* MATCH_ID, COMP_1_ID, COMP_1_IS_BYE, COMP_2_ID, COMP_2_IS_BYE,
	WINNER_ID, LEVEL, NODE_ID, MATCH_DATE, LEFT_CHILD_ID, RIGHT_CHILD_ID, IS_LEFT_CHILD);
*/
			String output = "";
			output += MATCH_ID + c.getInt(0) + " - ";
			output += COMP_1_ID + c.getInt(1) + " - ";
			output += COMP_1_IS_BYE + c.getInt(2) + " - ";
			output += COMP_2_ID + c.getInt(3) + " - ";
			output += COMP_2_IS_BYE + c.getInt(4) + " - ";
			output += WINNER_ID + c.getInt(5) + " - ";
			output += LEVEL + c.getInt(6) + " - ";
			output += NODE_ID + c.getInt(7) + " - ";
			output += MATCH_DATE + c.getLong(8) + " - ";
			output += LEFT_CHILD_ID + c.getInt(9) + " - ";
			output += RIGHT_CHILD_ID + c.getInt(10) + " - ";
			output += IS_LEFT_CHILD + c.getInt(11);

			Log.d(TAG, output);

		}

		c.close();

	}



	public ArrayList<Match> getAllMatchesForBracket() {

		return null;    //todo

		//populate comp_1 and comp_2 with database queries

	}

	public ArrayList<Competitor> readCompetitors() {

		ArrayList<Competitor> competitors = new ArrayList<>();

		Cursor cursor = db.query(COMPETITORS_TABLE, null, null, null, null, null, null);

		while (cursor.moveToNext()) {

/*			private final String COMP_ID = "_id";
			private final String COMPETITOR_NAME = "name";
			private final String COMP_IS_BYE = "is_bye";
*/
			long id = cursor.getLong(0);
			String name = cursor.getString(1);
			int bye = cursor.getInt(2);
			// 0 is false and 1 is true

			boolean isBye = (bye == 1 ) ? true : false;

			Competitor comp = new Competitor(name, id, isBye);

			competitors.add(comp);

		}

		cursor.close();
		return competitors;


	}


	public class SQLHelper extends SQLiteOpenHelper {

		private static final String SQL_TAG = "DB / SQL HELPER";

		public SQLHelper(Context c){
			super(c, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("DROP TABLE IF EXISTS " + COMPETITORS_TABLE);    //not in real life!!
			db.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE);

			String createCompetitorsBase = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT UNIQUE, %s TEXT UNIQUE )";  //todo check SQL
			String createCompetitorsSQL = String.format(createCompetitorsBase, COMPETITORS_TABLE, COMP_ID, COMPETITOR_NAME, COMP_IS_BYE);

			String createMatchesBase = "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"%s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, " +     //comp_1, comp_1 is bye, same for comp 2
					"%s INTEGER, " +					//winner id
					"%s INTEGER, %s INTEGER, " +    	//level, node id
					"%s INTEGER, " +					//match date (as timestamp)
					"%s INTEGER, %s INTEGER, " +				//left child id, right child id
					"%s INTEGER " +					//is left child
					" )";
			String createMatchesSQL = String.format(createMatchesBase, MATCHES_TABLE, MATCH_ID, COMP_1_ID, COMP_1_IS_BYE, COMP_2_ID, COMP_2_IS_BYE, WINNER_ID, LEVEL, NODE_ID, MATCH_DATE, LEFT_CHILD_ID, RIGHT_CHILD_ID, IS_LEFT_CHILD);


			Log.d(SQL_TAG, createCompetitorsSQL);
			db.execSQL(createCompetitorsSQL);

			Log.d(SQL_TAG, createMatchesBase);
			db.execSQL(createMatchesSQL);


		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + COMPETITORS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE);

			onCreate(db);
			Log.w(SQL_TAG, "Upgrade table - drop and recreate it");
		}
	}
}

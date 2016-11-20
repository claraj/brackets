package com.clara.brackets;

import android.content.ContentValues;
import android.content.Context;
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
	private  static final String DB_NAME = "products.db";

	private  static final int DB_VERSION = 3;


	//Two tables: one for competitors
	//one for results of a particular matchup

	private final String COMPETITORS_TABLE = "competitors";
	private final String COMP_ID = "_id";
	private final String COMPETITOR_NAME = "name";


	private final String MATCHES_TABLE = "matches";
	private final String RESULT_ID = "_id";
	private final String COMP_1_ID = "competitor_1_id";
	private final String COMP_2_ID = "competitor_2_id";
	private final String WINNER_ID = "winner_id";
	private final String NODE_ID = "node_id";      //node in Bracket tree
	private final String MATCH_DATE = "match_date";



	public Database(Context c) {
		this.context = c;
		helper = new SQLHelper(c);
		this.db = helper.getWritableDatabase();
	}


	public void close() {
		helper.close(); //Closes the database - very important!
	}


	public void saveCompetitors(ArrayList<Competitor> competitors) {

		for (Competitor c : competitors)  {

			ContentValues newCompetitor = new ContentValues();
			newCompetitor.put(COMPETITOR_NAME, c.name);
			db.insertOrThrow(COMPETITORS_TABLE, null, newCompetitor);

		}

	}



	public class SQLHelper extends SQLiteOpenHelper {

		private static final String SQL_TAG = "DB / SQL HELPER";

		public SQLHelper(Context c){
			super(c, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {


			db.execSQL("DROP TABLE IF EXISTS " + COMPETITORS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE);

			String createCompetitorsBase = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT UNIQUE )";  //todo check SQL
			String createCompetitorsSQL = String.format(createCompetitorsBase, COMPETITORS_TABLE, COMP_ID, COMPETITOR_NAME);

			String createResultsBase = "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER )";
			String createResultsSQL = String.format(createResultsBase, MATCHES_TABLE, RESULT_ID, COMP_1_ID, COMP_2_ID, WINNER_ID, MATCH_DATE, NODE_ID);


			Log.d(SQL_TAG, createCompetitorsSQL);
			db.execSQL(createCompetitorsSQL);

			Log.d(SQL_TAG, createResultsSQL);
			db.execSQL(createResultsSQL);


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

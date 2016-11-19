package com.clara.brackets;

/**
 * SQLite interaction.  Save competitor list, and current status of tournament
 *
 */

public class Database {

	//Two tables: one for competitors
	//one for results of a particular matchup

	String COMPETITORS_TABLE = "competitors";
	String COMP_ID = "_id";
	String COMPETITOR_NAME = "name";

	String createCompetitorsBase = "CREATE TABLE %s ( %s INT NOT NULL AUTOINCREMENT, %s UNIQUE VARCHAR(100) )";  //todo check SQL
	String createCompetitorsSQL = String.format(createCompetitorsBase, COMPETITORS_TABLE, COMP_ID, COMPETITOR_NAME);

	String RESULTS_TABLE = "results";
	String RESULT_ID = "_id";
	String COMP_1_NAME = "competitor_1";
	String COMP_2_NAME = "competitor_2";
	String WINNER = "winner";
	String MATCH_DATE = "match_date";

	String createResultsBase = "CREATE TABLE %s (%s INT NOT NULL AUTOINCREMENT, %s VARCHAR(100), %s INT, %s INT, %s INT, %s INT )";
	String createResultsSQL = String.format(createResultsBase, RESULTS_TABLE, RESULT_ID, COMP_1_NAME, COMP_2_NAME, WINNER, MATCH_DATE);




}

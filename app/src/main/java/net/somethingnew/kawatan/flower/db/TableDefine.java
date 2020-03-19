package net.somethingnew.kawatan.flower.db;

import java.util.ArrayList;

/**
 * TableDefine テーブル作成用SQL文リスト定義クラス.
 * 2011/01/20　宮坂　淳
 */
public final class TableDefine {
	/**
	 * テーブル作成用SQL文リスト.<BR>
	 */
	public static final ArrayList<String> CREATE_TABLE_SQL_LIST = new ArrayList<String>();

	// テーブル名
	public static final String TABLE_NAME = "COMPANY_INF";
	// カラム名配列
	public static final String[] COLUMN_NAMES = {"SEC_CD", "MARKET_CD", "CMPNY_NM_SH5", "CMPNY_NM_KABU", "CMPNY_NM_K"};

	private TableDefine() {
	}
	
	static {
		CREATE_TABLE_SQL_LIST.add(
			"CREATE TABLE COMPANY_INF ("
				+ "SEC_CD TEXT NOT NULL, "
				+ "MARKET_CD TEXT NOT NULL, "
				+ "CMPNY_NM_SH5 TEXT, "
				+ "CMPNY_NM_KABU TEXT, "
				+ "CMPNY_NM_K TEXT, "
				+ "PRIMARY KEY("
				+ 	"SEC_CD, "
				+ 	"MARKET_CD"
				+ "))"
		);

		CREATE_TABLE_SQL_LIST.add(
			"CREATE TABLE INDIV_STOCK_INF_REF_HISTORY ("
				+ "SEC_CD TEXT NOT NULL, "
				+ "MARKET_CD TEXT NOT NULL, "
				+ "CMPNY_NM_SH5 TEXT, "
				+ "REF_TM TEXT, "
				+ "PRIMARY KEY("
				+ 	"SEC_CD, "
				+ 	"MARKET_CD"
				+ "))"
		);
		
		CREATE_TABLE_SQL_LIST.add(
			"CREATE TABLE REGISTERED_STOCK_INF_LIST ("
				+ "LIST_NO INTEGER NOT NULL, "
				+ "LIST_NAME TEXT, "
				+ "LIST_COUNT INTEGER, "
				+ "MAX_LIST_COUNT INTEGER, "
				+ "LIST_TYPE TEXT, "
				+ "PRIMARY KEY("
				+ 	"LIST_NO"
				+ "))"
		);	

		CREATE_TABLE_SQL_LIST.add(
			"CREATE TABLE REGISTERED_STOCK_INF_LIST_DETAIL ("
				+ "LIST_NO INTEGER NOT NULL, "
				+ "LIST_SUB_NO INTEGER NOT NULL, "
				+ "REQUEST_CD TEXT, "
				+ "SEC_CD TEXT, "
				+ "SEC_NM TEXT, "
				+ "MARKET_NM TEXT, "
				+ "TRADE_TYP TEXT, "
				+ "NOMINAL TEXT, "
				+ "EXEC_PRICE TEXT, "
				+ "STATUS_CD TEXT, "
				+ "PRIMARY KEY("
				+ 	"LIST_NO, "
				+ 	"LIST_SUB_NO"
				+ "))"
		);
		
		CREATE_TABLE_SQL_LIST.add(
			"CREATE TABLE STOCK_EVENT_INF ("
				+ "EVENT_DLV_DT TEXT NOT NULL, "
				+ "EVENT_NM TEXT NOT NULL, "
				+ "REQUEST_CD TEXT, "
				+ "SEC_CD TEXT, "
				+ "SEC_NM TEXT, "
				+ "MARKET_NM TEXT, "
				+ "EVENT_SUMMARY TEXT"
				+ ")"
		);	
	}
}
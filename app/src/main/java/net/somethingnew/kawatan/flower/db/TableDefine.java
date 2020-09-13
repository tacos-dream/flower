package net.somethingnew.kawatan.flower.db;

import java.util.ArrayList;
import java.util.Date;

/**
 * TableDefine テーブル作成用SQL文リスト定義クラス.
 */
public final class TableDefine {
	/**
	 * テーブル作成用SQL文リスト.<BR>
	 */
	public static final ArrayList<String> CREATE_TABLE_SQL_LIST = new ArrayList<String>();

	private TableDefine() {
	}

	static {
		CREATE_TABLE_SQL_LIST.add(
			"CREATE TABLE FOLDER_TBL ("
				+ "FOLDER_ID				TEXT		NOT NULL, "
				+ "TITLE_NAME				TEXT		NOT NULL, "
				+ "CREATED_DATE				TEXT, "
				+ "UPDATED_DATE				TEXT, "
				+ "LAST_USED_DATE			TEXT, "
				+ "NUM_OF_ALL_CARDS			INTEGER, "
				+ "NUM_OF_LEARNED_CARDS		INTEGER, "
				+ "ICON_RES_ID				INTEGER, "
				+ "COVER_BG_COLOR			INTEGER, "
				+ "FRONT_BG_COLOR			INTEGER, "
				+ "BACK_BG_COLOR			INTEGER, "
				+ "COVER_TXT_COLOR			INTEGER, "
				+ "FRONT_TXT_COLOR			INTEGER, "
				+ "BACK_TXT_COLOR			INTEGER, "
				+ "DISPLAY_ORDER			INTEGER, "
				+ "ICON_CATEGORY			INTEGER, "
				+ "ICON_AUTO_DISPLAY		INTEGER, "
				+ "PRIMARY KEY(FOLDER_ID))"
		);

		CREATE_TABLE_SQL_LIST.add(
			"CREATE TABLE CARD_TBL ("
				+ "CARD_ID					TEXT		NOT NULL, "
				+ "FOLDER_ID				TEXT		NOT NULL, "
				+ "FRONT_TEXT				TEXT		NOT NULL, "
				+ "BACK_TEXT				TEXT		NOT NULL, "
				+ "CREATED_DATE				TEXT, "
				+ "UPDATED_DATE				TEXT, "
				+ "LAST_USED_DATE			TEXT, "
				+ "LEARNED					INTEGER, "
				+ "FUSEN_TAG				INTEGER, "
				+ "ICON_AUTO_DISPLAY		INTEGER, "
				+ "ICON_CATEGORY			INTEGER, "
				+ "ICON_RES_ID				INTEGER, "
				+ "FUSEN_RES_ID				INTEGER"
				+ ")"
		);

	}
}
package net.somethingnew.kawatan.flower.db;

import java.util.Iterator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.somethingnew.kawatan.flower.Constants;
import net.somethingnew.kawatan.flower.util.LogUtility;

/**
 * SQLiteデータベース接続クラス.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 *  DBインスタンス.<BR>
	 */
	private static SQLiteDatabase database = null;

	/**
	 * コンストラクタ.<BR>
	 * @param context Context　コンテキスト
	 */
	public DatabaseHelper(Context context) {
		// DBインスタンスを生成
		super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
		LogUtility.d("constructor");
	}

	/**
	 * テーブル初期化.<BR>
	 * @param db SQLiteDatabase　DBインスタンス
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			LogUtility.d("onCreate");
			// トランザクション開始
			db.beginTransaction();

			// テーブル作成用SQL文を発行
			for (Iterator<String> itr = TableDefine.CREATE_TABLE_SQL_LIST.iterator(); itr.hasNext();) {
				String sqlStr = itr.next();
				LogUtility.d("sqlStr: " + sqlStr);
				db.execSQL(sqlStr);
			}

			// 変更内容をコミット
			db.setTransactionSuccessful();
		} catch (Exception e) {

		} finally {
			// トランザクション終了
			db.endTransaction();
		}
	}

	/**
	 * テーブル定義更新（使用しない）.<BR>
	 * @param db SQLiteDatabase　DBインスタンス
	 * @param oldVersion int　旧データベースバージョン
	 * @param newVersion int　新データベースバージョン
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogUtility.d("onUpgrade oldVersion:" + oldVersion + "  newVersion:" + newVersion);
	}

	/**
	 * DBインスタンス取得.<BR>
	 */
	protected SQLiteDatabase getDBInstance() {
		// DBインスタンスを取得
		if (database == null || !database.isOpen()) {
			database = super.getWritableDatabase();
		}

		return database;
	}

	/**
	 */
	protected void closeDBInstance() {
		// DBインスタンスを取得
		if (database != null && database.isOpen()) {
			database.close();
		}
	}
}

package net.somethingnew.kawatan.flower.db;

import java.util.Iterator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteデータベース接続クラス.
 * 2011/01/20　宮坂　淳
 */
public abstract class DBConnectionHelper extends SQLiteOpenHelper {
	/**
	 * デフォルトデータベース名.<BR>
	 * nullに設定した場合はインメモリで起動
	 */
	private static final String DB_NAME = "MarketViewer.db";
	//private static final String DB_NAME = null;

	/**
	 *  DBインスタンス.<BR>
	 */
	private static SQLiteDatabase database = null;

	/**
	 * コンストラクタ.<BR>
	 * @param context Context　コンテキスト
	 */
	public DBConnectionHelper(Context context) {
		// DBインスタンスを生成
		super(context, DB_NAME, null, 1);
	}

	/**
	 * テーブル初期化.<BR>
	 * @param db SQLiteDatabase　DBインスタンス
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			// トランザクション開始
			db.beginTransaction();

			// テーブル作成用SQL文を発行
			for (Iterator<String> itr = TableDefine.CREATE_TABLE_SQL_LIST.iterator(); itr.hasNext();) {
				String sqlStr = (String)itr.next();
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

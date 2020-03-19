package net.somethingnew.kawatan.flower.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteQueryBuilder;

import net.somethingnew.kawatan.flower.Constants;
import net.somethingnew.kawatan.flower.db.DBConnectionHelper;
import net.somethingnew.kawatan.flower.db.dto.FolderDto;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * COMPANY_INFテーブルアクセスクラス.
 * 2011/01/20　宮坂　淳
 */
public class FolderDao extends DBConnectionHelper {
	// SQL文キャッシュ
	private static String insertSqlStr = null;
	private static String selectLikeBySecCdSqlStr = null;
	private static String selectLikeByDscrNmSqlStr = null;
	private static String selectLikeByDscrNmKanaSqlStr = null;
	private static String selectCountAllSqlStr = null;

	/**
	 * コンストラクタ.<BR>
	 * @param context Context　コンテキスト
	 * @return
	 */
	public FolderDao(Context context) {
		super(context);
	}

	/**
	 * データクリア.<BR>
	 */
	public void truncate() {
		// トランザクション開始
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("DELETE FROM ");
			sqlBuilder.append(Constants.TABLE_NAME_FOLDER);

			// ステートメントを取得
			SQLiteStatement stmt = getDBInstance().compileStatement(sqlBuilder.toString());

			// SQL実行
			stmt.execute();

			// ステートメントをクローズ
			stmt.close();

			// 変更内容をコミット
			getDBInstance().setTransactionSuccessful();
		} catch (Exception e) {
			
		} finally {
			// トランザクション終了
			getDBInstance().endTransaction();
		}
	}

	/**
	 * データ挿入.<BR>
	 * @param data FolderDto 対象データ
	 */
	public void insert(FolderDto data) {
		// トランザクション開始
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (insertSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("INSERT INTO ");
				sqlBuilder.append(Constants.TABLE_NAME_FOLDER);
				sqlBuilder.append(" VALUES (");
				for (int i = 0; i < Constants.COLUMN_NAMES_FOLDER.length; ++i) {
					sqlBuilder.append("?, ");
				}
				sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
				sqlBuilder.append(")");

				insertSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得して変数をバインド
			int index = 1;
			SQLiteStatement stmt = getDBInstance().compileStatement(insertSqlStr);
			/*
			if (data.getSecCd() == null) {
				throw new Exception();
			} else {
				stmt.bindString(index++, data.getSecCd());
			}
			if (data.getMarketCd() == null) {
				throw new Exception();
			} else {
				stmt.bindString(index++, data.getMarketCd());
			}
			if (data.getCmpnyNmK() == null) {
				stmt.bindNull(index++);
			} else {
				stmt.bindString(index++, data.getCmpnyNmSh5());
			}
			if (data.getCmpnyNmKabu() == null) {
				stmt.bindNull(index++);
			} else {
				stmt.bindString(index++, data.getCmpnyNmKabu());
			}
			if (data.getCmpnyNmSh5() == null) {
				stmt.bindNull(index++);
			} else {
				stmt.bindString(index++, data.getCmpnyNmK());
			}

			 */

			// SQL実行
			stmt.executeInsert();

			// ステートメントをクローズ
			stmt.close();

			// 変更内容をコミット
			getDBInstance().setTransactionSuccessful();
		} catch (Exception e) {

		} finally {
			// トランザクション終了
			getDBInstance().endTransaction();
		}
	}

	/**
	 * データ一括挿入.<BR>
	 * @param dataList ArrayList<FolderDto> 対象データリスト
	 */
	public void bulkInsert(ArrayList<FolderDto> dataList) {
		// トランザクション開始
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (insertSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("INSERT INTO ");
				sqlBuilder.append(Constants.TABLE_NAME_FOLDER);
				sqlBuilder.append(" VALUES (");
				for (int i = 0; i < Constants.COLUMN_NAMES_FOLDER.length; ++i) {
					sqlBuilder.append("?, ");
				}
				sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
				sqlBuilder.append(")");

				insertSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得
			SQLiteStatement stmt = getDBInstance().compileStatement(insertSqlStr);

			// 与えられた全データを順次処理
			for (Iterator<FolderDto> itr = dataList.iterator(); itr.hasNext();) {
				int index = 1;

				// １レコード分のデータを取得
				FolderDto data = (FolderDto)itr.next();

				// 変数をバインド
				/*
				if (data.getSecCd() == null) {
					throw new Exception();
				} else {
					stmt.bindString(index++, data.getSecCd());
				}
				if (data.getMarketCd() == null) {
					throw new Exception();
				} else {
					stmt.bindString(index++, data.getMarketCd());
				}
				if (data.getCmpnyNmK() == null) {
					stmt.bindNull(index++);
				} else {
					stmt.bindString(index++, data.getCmpnyNmSh5());
				}
				if (data.getCmpnyNmKabu() == null) {
					stmt.bindNull(index++);
				} else {
					stmt.bindString(index++, data.getCmpnyNmKabu());
				}
				if (data.getCmpnyNmSh5() == null) {
					stmt.bindNull(index++);
				} else {
					stmt.bindString(index++, data.getCmpnyNmK());
				}
				 */

				// SQL実行
				stmt.executeInsert();
			}

			// ステートメントをクローズ
			stmt.close();

			// 変更内容をコミット
			getDBInstance().setTransactionSuccessful();
		} catch (Exception e) {

		} finally {
			// トランザクション終了
			getDBInstance().endTransaction();
		}
	}

	/**
	 * 銘柄コードLIKE検索(前方一致).<BR>
	 * @param secCd String　銘柄コード
	 * @return ArrayList<FolderDto>
	 */
	public ArrayList<FolderDto> selectLikeBySecCd(String secCd) {
		ArrayList<FolderDto> result = null;

		try {
			result = new ArrayList<FolderDto>();

			// SQL文生成
			if (selectLikeBySecCdSqlStr == null) {
				selectLikeBySecCdSqlStr = SQLiteQueryBuilder.buildQueryString(false, Constants.TABLE_NAME_FOLDER, Constants.COLUMN_NAMES_FOLDER, "SEC_CD LIKE ?", null,
						null, "SEC_CD, ROWID", null);
			}

			// 変数をバインドしカーソルを取得
			String[] whereBindValue = {secCd + "%"};
			Cursor cursor = getDBInstance().rawQuery(selectLikeBySecCdSqlStr, whereBindValue);

			// カーソルが終わるまで繰り返し
			while (cursor.moveToNext()) {
				int index = 0;

				FolderDto data = new FolderDto();

				/*
				if (!cursor.isNull(index++)) {
					data.setSecCd(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setMarketCd(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmSh5(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmKabu(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmK(cursor.getString(index - 1));
				}

				result.add(data);

				 */
			}

			// カーソルをクローズ
			cursor.close();
		} catch (Exception e) {

		}

		return result;
	}

	/**
	 * 銘柄名LIKE検索(部分一致).<BR>
	 * @param cmpnyNmKabu String　銘柄名
	 * @return ArrayList<FolderDto>
	 */
	public ArrayList<FolderDto> selectLikeByDscrNm(String cmpnyNmKabu) {
		ArrayList<FolderDto> result = null;

		try {
			result = new ArrayList<FolderDto>();

			// SQL文生成
			if (selectLikeByDscrNmSqlStr == null) {
				selectLikeByDscrNmSqlStr = SQLiteQueryBuilder.buildQueryString(false, Constants.TABLE_NAME_FOLDER, Constants.COLUMN_NAMES_FOLDER, "CMPNY_NM_KABU LIKE ?",
						null, null, "SEC_CD, ROWID", null);
			}

			// 変数をバインドしカーソルを取得
			String[] whereBindValue = {"%" + cmpnyNmKabu + "%"};
			Cursor cursor = getDBInstance().rawQuery(selectLikeByDscrNmSqlStr, whereBindValue);

			// カーソルが終わるまで繰り返し
			while (cursor.moveToNext()) {
				int index = 0;
				FolderDto data = new FolderDto();

				/*
				if (!cursor.isNull(index++)) {
					data.setSecCd(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setMarketCd(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmSh5(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmKabu(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmK(cursor.getString(index - 1));
				}

				 */

				result.add(data);
			}

			// カーソルをクローズ
			cursor.close();
		} catch (Exception e) {

		}

		return result;
	}

	/**
	 * 銘柄名カナLIKE検索(部分一致).<BR>
	 * @param cmpnyNmK String　銘柄名カナ
	 * @return ArrayList<FolderDto>
	 */
	public ArrayList<FolderDto> selectLikeByDscrNmKana(String cmpnyNmK) {
		ArrayList<FolderDto> result = null;

		try {
			result = new ArrayList<FolderDto>();

			// SQL文生成
			if (selectLikeByDscrNmKanaSqlStr == null) {
				selectLikeByDscrNmKanaSqlStr = SQLiteQueryBuilder.buildQueryString(false, Constants.TABLE_NAME_FOLDER, Constants.COLUMN_NAMES_FOLDER, "CMPNY_NM_K LIKE ?",
						null, null, "SEC_CD, ROWID", null);
			}

			// 変数をバインドしカーソルを取得
			String[] whereBindValue = {"%" + cmpnyNmK + "%"};
			Cursor cursor = getDBInstance().rawQuery(selectLikeByDscrNmKanaSqlStr, whereBindValue);

			// カーソルが終わるまで繰り返し
			while (cursor.moveToNext()) {
				int index = 0;
				FolderDto data = new FolderDto();

				/*
				if (!cursor.isNull(index++)) {
					data.setSecCd(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setMarketCd(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmSh5(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmKabu(cursor.getString(index - 1));
				}
				if (!cursor.isNull(index++)) {
					data.setCmpnyNmK(cursor.getString(index - 1));
				}

				 */

				result.add(data);
			}

			// カーソルをクローズ
			cursor.close();
		} catch (Exception e) {

		}

		return result;
	}

	/**
	 * レコード件数取得.<BR>
	 */
	public long selectCountAll() {
		long result = 0;

		try {
			// SQL文生成
			if (selectCountAllSqlStr == null) {
				String[] targetColumns = {"COUNT(" + Constants.COLUMN_NAMES_FOLDER[0] + ")"};
				selectCountAllSqlStr = SQLiteQueryBuilder.buildQueryString(false, Constants.TABLE_NAME_FOLDER, targetColumns, null, null, null, null, null);
			}

			// ステートメントを取得
			SQLiteStatement stmt = getDBInstance().compileStatement(selectCountAllSqlStr);

			// 実行結果を数値で取得
			result = stmt.simpleQueryForLong();
			
			// ステートメントをクローズ    
			stmt.close();
		} catch (Exception e) {

	}

		return result;
	}
}

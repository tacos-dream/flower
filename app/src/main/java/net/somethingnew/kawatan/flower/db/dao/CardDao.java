package net.somethingnew.kawatan.flower.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;

import net.somethingnew.kawatan.flower.Constants;
import net.somethingnew.kawatan.flower.db.DatabaseHelper;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;


/**
 * CARDテーブルアクセスクラス.
 */
public class CardDao extends DatabaseHelper {
	// SQL文キャッシュ
	private static String insertSqlStr = null;
	private static String selectAllSqlStr = null;
	private static String deleteSqlStr = null;
	private static String updateSqlStr = null;
	private static String selectCountAllSqlStr = null;

	/**
	 * コンストラクタ
	 */
	public CardDao(Context context) {
		super(context);
		LogUtility.d("constructor");
	}

	/**
	 * データクリア
	 */
	public void truncate() {
		LogUtility.d("truncate");
		// トランザクション開始
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("DELETE FROM ");
			sqlBuilder.append(Constants.TABLE_NAME_CARD);

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
	 * データ挿入
	 * @param data CardModel 対象データ
	 */
	public void insert(CardModel data) {
		// トランザクション開始
		LogUtility.d("insert");
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (insertSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("INSERT INTO ");
				sqlBuilder.append(Constants.TABLE_NAME_CARD);
				sqlBuilder.append(" VALUES (");
				for (int i = 0; i < Constants.COLUMN_NAMES_CARD.length; i++) {
					sqlBuilder.append("?, ");
				}
				sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
				sqlBuilder.append(")");

				insertSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得して変数をバインド
			int index = 1;
			SQLiteStatement stmt = getDBInstance().compileStatement(insertSqlStr);

			stmt.bindString(index++, data.getId());
			stmt.bindString(index++, data.getFolderId());
			stmt.bindString(index++, data.getFrontText());
			stmt.bindString(index++, data.getBackText());
			stmt.bindString(index++, data.getCreatedDate().toString());
			stmt.bindString(index++, data.getUpdatedDate().toString());
			stmt.bindString(index++, data.getLastUsedDate().toString());
			stmt.bindLong(index++, (data.isLearned()) ? 1 : 0);
			stmt.bindLong(index++, (data.isFusenTag()) ? 1 : 0);
			stmt.bindLong(index++, (data.isIconAutoDisplay()) ? 1 : 0);
			stmt.bindLong(index++, data.getIconCategory());
			stmt.bindLong(index++, data.getImageIconResId());

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
	 * データ一括挿入
	 * @param dataList ArrayList<CardModel> 対象データリスト
	 */
	public void bulkInsert(ArrayList<CardModel> dataList) {
		LogUtility.d("bulkInsert");
		// トランザクション開始
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (insertSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("INSERT INTO ");
				sqlBuilder.append(Constants.TABLE_NAME_CARD);
				sqlBuilder.append(" VALUES (");
				for (int i = 0; i < Constants.COLUMN_NAMES_CARD.length; i++) {
					sqlBuilder.append("?, ");
				}
				sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
				sqlBuilder.append(")");

				insertSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得
			SQLiteStatement stmt = getDBInstance().compileStatement(insertSqlStr);

			// 与えられた全データを順次処理
			for (Iterator<CardModel> itr = dataList.iterator(); itr.hasNext();) {
				int index = 1;

				// １レコード分のデータを取得
				CardModel card = itr.next();

				stmt.bindString(index++, card.getId());
				stmt.bindString(index++, card.getFolderId());
				stmt.bindString(index++, card.getFrontText());
				stmt.bindString(index++, card.getBackText());
				stmt.bindString(index++, card.getCreatedDate().toString());
				stmt.bindString(index++, card.getUpdatedDate().toString());
				stmt.bindString(index++, card.getLastUsedDate().toString());
				stmt.bindLong(index++, (card.isLearned()) ? 1 : 0);
				stmt.bindLong(index++, (card.isFusenTag()) ? 1 : 0);
				stmt.bindLong(index++, (card.isIconAutoDisplay()) ? 1 : 0);
				stmt.bindLong(index++, card.getIconCategory());
				stmt.bindLong(index++, card.getImageIconResId());
				
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
	 * データ更新
	 * とりあえず、すべてのフィールドを対象に更新をかける
	 * @param data CardModel 対象データ
	 */
	public void update(CardModel data) {
		// トランザクション開始
		LogUtility.d("update");
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (updateSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("UPDATE ");
				sqlBuilder.append(Constants.TABLE_NAME_CARD);
				sqlBuilder.append(" SET ");

				// 先頭カラムのidは除外してセットする（idはwhere句のKeyのため）
				for (int i = 1; i < Constants.COLUMN_NAMES_CARD.length; i++) {
					sqlBuilder.append(Constants.COLUMN_NAMES_CARD[i] + " = ?, ");
				}
				sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
				sqlBuilder.append(" WHERE CARD_ID = ?");
				updateSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得して変数をバインド
			int index = 1;
			SQLiteStatement stmt = getDBInstance().compileStatement(updateSqlStr);

			// Set項目のバインド
			stmt.bindString(index++, data.getFolderId());
			stmt.bindString(index++, data.getFrontText());
			stmt.bindString(index++, data.getBackText());
			stmt.bindString(index++, data.getCreatedDate().toString());
			stmt.bindString(index++, data.getUpdatedDate().toString());
			stmt.bindString(index++, data.getLastUsedDate().toString());
			stmt.bindLong(index++, (data.isLearned()) ? 1 : 0);
			stmt.bindLong(index++, (data.isFusenTag()) ? 1 : 0);
			stmt.bindLong(index++, (data.isIconAutoDisplay()) ? 1 : 0);
			stmt.bindLong(index++, data.getIconCategory());
			stmt.bindLong(index++, data.getImageIconResId());

			// Where項目のバインド
			stmt.bindString(index++, data.getId());

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

	public void deleteByCardId(String cardId) {
		// トランザクション開始
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (deleteSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("DELETE FROM ");
				sqlBuilder.append(Constants.TABLE_NAME_CARD);
				sqlBuilder.append(" WHERE CARD_ID = ?");

				deleteSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得して変数をバインド
			int index = 1;
			SQLiteStatement stmt = getDBInstance().compileStatement(deleteSqlStr);
			stmt.bindString(index++, cardId);

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
	 * @return ArrayList<CardModel>
	 */
	public ArrayList<CardModel> selectAll() {
		LogUtility.d("selectAll");
		ArrayList<CardModel> result = new ArrayList<>();

		try {

			// SQL文生成
			if (selectAllSqlStr == null) {
				selectAllSqlStr = SQLiteQueryBuilder.buildQueryString(false, Constants.TABLE_NAME_CARD, Constants.COLUMN_NAMES_CARD, null, null,
						null, "LAST_USED_DATE", null);
			}

			// カーソルを取得
			Cursor cursor = getDBInstance().rawQuery(selectAllSqlStr, null);

			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

			// カーソルが終わるまで繰り返し
			while (cursor.moveToNext()) {
				int index = 0;

				CardModel card = new CardModel();

				card.setId(cursor.getString(index++));
				card.setFolderId(cursor.getString(index++));
				card.setFrontText(cursor.getString(index++));
				card.setBackText(cursor.getString(index++));
				card.setCreatedDate(dateFormat.parse(cursor.getString(index++)));
				card.setUpdatedDate(dateFormat.parse(cursor.getString(index++)));
				card.setLastUsedDate(dateFormat.parse(cursor.getString(index++)));
				card.setLearned((cursor.getInt(index++) == 1)? true : false);
				card.setFusenTag((cursor.getInt(index++) == 1)? true : false);
				card.setIconAutoDisplay((cursor.getInt(index++) == 1)? true : false);
				card.setIconCategory(cursor.getInt(index++));
				card.setImageIconResId(cursor.getInt(index++));

                result.add(card);
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
				String[] targetColumns = {"COUNT(" + Constants.COLUMN_NAMES_CARD[0] + ")"};
				selectCountAllSqlStr = SQLiteQueryBuilder.buildQueryString(false, Constants.TABLE_NAME_CARD, targetColumns, null, null, null, null, null);
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

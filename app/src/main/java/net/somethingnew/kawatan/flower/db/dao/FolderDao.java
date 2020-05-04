package net.somethingnew.kawatan.flower.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteQueryBuilder;

import net.somethingnew.kawatan.flower.Constants;
import net.somethingnew.kawatan.flower.db.DatabaseHelper;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;


/**
 * FOLDERテーブルアクセスクラス.
 */
public class FolderDao extends DatabaseHelper {
	// SQL文キャッシュ
	private static String insertSqlStr = null;
	private static String selectAllSqlStr = null;
	private static String deleteSqlStr = null;
	private static String updateSqlStr = null;
	private static String selectCountAllSqlStr = null;

	/**
	 * コンストラクタ
	 */
	public FolderDao(Context context) {
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
	 * データ挿入
	 * @param data FolderModel 対象データ
	 */
	public void insert(FolderModel data, int order) {
		// トランザクション開始
		LogUtility.d("insert");
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (insertSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("INSERT INTO ");
				sqlBuilder.append(Constants.TABLE_NAME_FOLDER);
				sqlBuilder.append(" VALUES (");
				for (int i = 0; i < Constants.COLUMN_NAMES_FOLDER.length; i++) {
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
			stmt.bindString(index++, data.getTitleName());
			stmt.bindString(index++, data.getCreatedDate().toString());
			stmt.bindString(index++, data.getUpdatedDate().toString());
			stmt.bindString(index++, data.getLastUsedDate().toString());
			stmt.bindLong(index++, data.getNumOfAllCards());
			stmt.bindLong(index++, data.getNumOfLearnedCards());
			stmt.bindLong(index++, data.getImageIconResId());
			stmt.bindLong(index++, data.getCoverBackgroundColor());
			stmt.bindLong(index++, data.getFrontBackgroundColor());
			stmt.bindLong(index++, data.getBackBackgroundColor());
			stmt.bindLong(index++, data.getCoverTextColor());
			stmt.bindLong(index++, data.getFrontTextColor());
			stmt.bindLong(index++, data.getBackTextColor());
			stmt.bindLong(index++, data.getImageFusenResId());
			stmt.bindLong(index++, order);
			stmt.bindLong(index++, data.getIconCategory());
			stmt.bindLong(index++, (data.isIconAutoDisplay()) ? 1 : 0);

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
	 * @param dataList ArrayList<FolderModel> 対象データリスト
	 */
	public void bulkInsert(ArrayList<FolderModel> dataList) {
		LogUtility.d("bulkInsert");
		// トランザクション開始
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (insertSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("INSERT INTO ");
				sqlBuilder.append(Constants.TABLE_NAME_FOLDER);
				sqlBuilder.append(" VALUES (");
				for (int i = 0; i < Constants.COLUMN_NAMES_FOLDER.length; i++) {
					sqlBuilder.append("?, ");
				}
				sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
				sqlBuilder.append(")");

				insertSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得
			SQLiteStatement stmt = getDBInstance().compileStatement(insertSqlStr);

			// 与えられた全データを順次処理
			for (Iterator<FolderModel> itr = dataList.iterator(); itr.hasNext();) {
				int order = 1;
				int index = 1;

				// １レコード分のデータを取得
				FolderModel folder = itr.next();

				stmt.bindString(index++, folder.getId());
				stmt.bindString(index++, folder.getTitleName());
				stmt.bindString(index++, folder.getCreatedDate().toString());
				stmt.bindString(index++, folder.getUpdatedDate().toString());
				stmt.bindString(index++, folder.getLastUsedDate().toString());
				stmt.bindLong(index++, folder.getNumOfAllCards());
				stmt.bindLong(index++, folder.getNumOfLearnedCards());
				stmt.bindLong(index++, folder.getImageIconResId());
				stmt.bindLong(index++, folder.getCoverBackgroundColor());
				stmt.bindLong(index++, folder.getFrontBackgroundColor());
				stmt.bindLong(index++, folder.getBackBackgroundColor());
				stmt.bindLong(index++, folder.getCoverTextColor());
				stmt.bindLong(index++, folder.getFrontTextColor());
				stmt.bindLong(index++, folder.getBackTextColor());
				stmt.bindLong(index++, folder.getImageFusenResId());
				stmt.bindLong(index++, order++);
				stmt.bindLong(index++, folder.getIconCategory());
				stmt.bindLong(index++, (folder.isIconAutoDisplay()) ? 1 : 0);
				
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
	 * @param data FolderModel 対象データ
	 */
	public void update(FolderModel data) {
		// トランザクション開始
		LogUtility.d("update");
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (updateSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("UPDATE ");
				sqlBuilder.append(Constants.TABLE_NAME_FOLDER);
				sqlBuilder.append(" SET ");

				// 先頭カラムのidは除外してセットする（idはwhere句のKeyのため）
				for (int i = 1; i < Constants.COLUMN_NAMES_FOLDER.length; i++) {
					sqlBuilder.append(Constants.COLUMN_NAMES_FOLDER[i] + " = ?, ");
				}
				sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
				sqlBuilder.append(" WHERE FOLDER_ID = ?");
				updateSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得して変数をバインド
			int index = 1;
			SQLiteStatement stmt = getDBInstance().compileStatement(updateSqlStr);

			// Set項目のバインド
			stmt.bindString(index++, data.getTitleName());
			stmt.bindString(index++, data.getCreatedDate().toString());
			stmt.bindString(index++, data.getUpdatedDate().toString());
			stmt.bindString(index++, data.getLastUsedDate().toString());
			stmt.bindLong(index++, data.getNumOfAllCards());
			stmt.bindLong(index++, data.getNumOfLearnedCards());
			stmt.bindLong(index++, data.getImageIconResId());
			stmt.bindLong(index++, data.getCoverBackgroundColor());
			stmt.bindLong(index++, data.getFrontBackgroundColor());
			stmt.bindLong(index++, data.getBackBackgroundColor());
			stmt.bindLong(index++, data.getCoverTextColor());
			stmt.bindLong(index++, data.getFrontTextColor());
			stmt.bindLong(index++, data.getBackTextColor());
			stmt.bindLong(index++, data.getImageFusenResId());
			stmt.bindLong(index++, data.getOrder());
			stmt.bindLong(index++, data.getIconCategory());
			stmt.bindLong(index++, (data.isIconAutoDisplay()) ? 1 : 0);

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

	public void deleteByFolderId(String folderId) {
		// トランザクション開始
		getDBInstance().beginTransaction();

		try {
			// SQL文を作成
			if (deleteSqlStr == null) {
				StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("DELETE FROM ");
				sqlBuilder.append(Constants.TABLE_NAME_FOLDER);
				sqlBuilder.append(" WHERE FOLDER_ID = ?");

				deleteSqlStr = sqlBuilder.toString();
			}

			// ステートメントを取得して変数をバインド
			int index = 1;
			SQLiteStatement stmt = getDBInstance().compileStatement(deleteSqlStr);
			stmt.bindString(index++, folderId);

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
	 * @return ArrayList<FolderModel>
	 */
	public ArrayList<FolderModel> selectAll() {
		LogUtility.d("selectAll");
		ArrayList<FolderModel> result = new ArrayList<>();

		try {

			// SQL文生成
			if (selectAllSqlStr == null) {
				selectAllSqlStr = SQLiteQueryBuilder.buildQueryString(false, Constants.TABLE_NAME_FOLDER, Constants.COLUMN_NAMES_FOLDER, null, null,
						null, "DISPLAY_ORDER", null);
			}

			// カーソルを取得
			Cursor cursor = getDBInstance().rawQuery(selectAllSqlStr, null);

			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

			// カーソルが終わるまで繰り返し
			while (cursor.moveToNext()) {
				int index = 0;

				FolderModel folder = new FolderModel();

                folder.setId(cursor.getString(index++)); 
                folder.setTitleName(cursor.getString(index++)); 
                folder.setCreatedDate(dateFormat.parse(cursor.getString(index++))); 
                folder.setUpdatedDate(dateFormat.parse(cursor.getString(index++))); 
                folder.setLastUsedDate(dateFormat.parse(cursor.getString(index++)));
                folder.setNumOfAllCards(cursor.getInt(index++)); 
                folder.setNumOfLearnedCards(cursor.getInt(index++)); 
                folder.setImageIconResId(cursor.getInt(index++)); 
                folder.setCoverBackgroundColor(cursor.getInt(index++)); 
                folder.setFrontBackgroundColor(cursor.getInt(index++)); 
                folder.setBackBackgroundColor(cursor.getInt(index++)); 
                folder.setCoverTextColor(cursor.getInt(index++)); 
                folder.setFrontTextColor(cursor.getInt(index++)); 
                folder.setBackTextColor(cursor.getInt(index++)); 
                folder.setImageFusenResId(cursor.getInt(index++));
				folder.setOrder(cursor.getInt(index++));
				folder.setIconCategory(cursor.getInt(index++));
				folder.setIconAutoDisplay((cursor.getInt(index++) == 1)? true : false);

                result.add(folder);
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

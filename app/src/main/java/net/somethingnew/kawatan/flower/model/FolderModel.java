package net.somethingnew.kawatan.flower.model;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * ArrayListで管理する各FoloderのModel
 * フィールドとSetter/Getterを持つ
 * フィールド項目はSQLiteの保存項目とほぼ等しいので、
 * SQLiteのDAOとの間でやり取りするDTO（FolderDto）クラスともほぼ同じ形になっている。
 * 冗長的ではあるが、とりあえずこれで！
 */
public class FolderModel implements Cloneable {
	String          		id;                     // ユニークなKeyId (UUIDから生成）
	String          		titleName;              // タイトル名
	Date            		createdDate;            // 作成日
	Date            		updatedDate;            // 最終更新日
	Date            		lastUsedDate;           // 最終利用日
	boolean					learned;				// 習得済みフラグ
	int             		numOfAllCards;          // 含まれるカード数
	int             		numOfLearnedCards;      // 習得済みカード数
	int             		imageIconResId;         // アイコンのResourceId
	int						coverBackgroundColor;	// 表紙の背景色
	int						frontBackgroundColor;	// おもての背景色
	int						backBackgroundColor;	// うらの背景色
	int						coverTextColor;			// 表紙の文字色
	int						frontTextColor;		// おもての文字色
	int						backTextColor;			// うらの文字色
	int						imageFusenResId;		// ふせんのResourceId

	//LinkedList<CardModel>	cardLinkedList;			// HashMapに変更して独立化

	public FolderModel(String titleName, int imageIconResId, int imageFusenResId) {
		this.id                     = UUID.randomUUID().toString();
		this.titleName              = titleName;
		this.createdDate            = new Date();
		this.updatedDate            = new Date();
		this.lastUsedDate           = new Date();
		this.numOfAllCards          = 0;
		this.numOfLearnedCards      = 0;
		this.imageIconResId         = imageIconResId;
		this.imageFusenResId        = imageFusenResId;
		this.learned				= false;
		this.coverBackgroundColor	= Color.WHITE;
		this.frontBackgroundColor	= Color.WHITE;
		this.backBackgroundColor	= Color.WHITE;
		this.coverTextColor			= Color.BLACK;
		this.frontTextColor		= Color.BLACK;
		this.backTextColor			= Color.BLACK;
	}


	@Override
	public FolderModel clone() { //基本的にはpublic修飾子を付け、自分自身の型を返り値とする
		FolderModel cloned = null;

		/*ObjectクラスのcloneメソッドはCloneNotSupportedExceptionを投げる可能性があるので、try-catch文で記述(呼び出し元に投げても良い)*/
		try {
			cloned = (FolderModel)super.clone(); //親クラスのcloneメソッドを呼び出す(親クラスの型で返ってくるので、自分自身の型でのキャストを忘れないようにする)
			//cloned.cardLinkedList=(LinkedList)this.cardLinkedList.clone(); //親クラスのcloneメソッドで深いコピー(複製先のクラス型変数と複製元のクラス型変数で指しているインスタンスの中身が違うコピー)がなされていないクラス型変数をその変数のcloneメソッドで複製し、複製先のクラス型変数に代入
		}catch (Exception e){
			e.printStackTrace();
		}
		return cloned;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String   getTitleName() {
		return titleName;
	}
	public void     setTitleName(String titleName) { this.titleName = titleName;}

	public Date     getCreatedDate() {
		return createdDate;
	}
	public void     setCreatedDate(Date createdDate) { this.createdDate = createdDate;}

	public Date     getUpdatedDate() {
		return updatedDate;
	}
	public void     setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate;}

	public Date     getLastUsedDate() {
		return lastUsedDate;
	}
	public void     setLastUsedDate(Date lastUsedDate) { this.lastUsedDate = lastUsedDate;}

	public void setLearned(boolean learned) {
		this.learned = learned;
	}

	public boolean isLearned() {
		return learned;
	}

	public int      getNumOfAllCards() {
		return numOfAllCards;
	}
	public void     setNumOfAllCards(int numOfAllCards) { this.numOfAllCards = numOfAllCards;}

	public int      getNumOfLearnedCards() {
		return numOfLearnedCards;
	}
	public void     setNumOfLearnedCards(int numOfLearnedCards) { this.numOfLearnedCards = numOfLearnedCards;}

	public int      getImageIconResId() {
		return imageIconResId;
	}
	public void     setImageIconResId(int imageIconResId) { this.imageIconResId = imageIconResId;}

	public int getCoverBackgroundColor() {
		return coverBackgroundColor;
	}

	public void setCoverBackgroundColor(int coverBackgroundColor) {
		this.coverBackgroundColor = coverBackgroundColor;
	}

	public int getFrontBackgroundColor() {
		return frontBackgroundColor;
	}

	public void setFrontBackgroundColor(int frontBackgroundColor) {
		this.frontBackgroundColor = frontBackgroundColor;
	}

	public int getBackBackgroundColor() {
		return backBackgroundColor;
	}

	public void setBackBackgroundColor(int backBackgroundColor) {
		this.backBackgroundColor = backBackgroundColor;
	}

	public int getCoverTextColor() {
		return coverTextColor;
	}

	public void setCoverTextColor(int coverTextColor) {
		this.coverTextColor = coverTextColor;
	}

	public int getFrontTextColor() {
		return frontTextColor;
	}

	public void setFrontTextColor(int frontTextColor) {
		this.frontTextColor = frontTextColor;
	}

	public int getBackTextColor() {
		return backTextColor;
	}

	public void setBackTextColor(int backTextColor) {
		this.backTextColor = backTextColor;
	}

	public int getImageFusenResId() {
		return imageFusenResId;
	}

	public void setImageFusenResId(int imageFusenResId) {
		this.imageFusenResId = imageFusenResId;
	}
}

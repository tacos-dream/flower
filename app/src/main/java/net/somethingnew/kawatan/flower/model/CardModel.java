package net.somethingnew.kawatan.flower.model;

import net.somethingnew.kawatan.flower.R;

import java.util.Date;
import java.util.UUID;

/**
 * ArrayListで管理する各CardのModel
 * フィールドとSetter/Getterを持つ
 * フィールド項目はSQLiteの保存項目とほぼ等しいので、
 * SQLiteのDAOとの間でやり取りするDTO（CardDto）クラスともほぼ同じ形になっている。
 * 冗長的ではあるが、とりあえずこれで！
 */
public class CardModel implements Cloneable{
	String          id;                     // ユニークなKeyId (UUIDから生成）
	String          folderId;
	String          frontText;              // 表面テキスト
	String          backText;              	// 裏面テキスト
	Date            createdDate;            // 作成日
	Date            updatedDate;            // 最終更新日
	Date            lastUsedDate;           // 最終更新日
	boolean         learned;     			// 習得済み
	boolean         fusenTag;               // 付箋
	boolean			iconAutoDisplay;
	int				iconCategory;
	int				imageIconResId;         // アイコンのResourceId

	public CardModel() {
	}

	public CardModel(String folderId) {
		this.id                     = UUID.randomUUID().toString();
		this.folderId				= folderId;
		this.frontText				= "";
		this.backText            	= "";
		this.createdDate            = new Date();
		this.updatedDate            = new Date();
		this.lastUsedDate           = new Date();
		this.learned           		= false;
		this.fusenTag				= false;
		this.iconAutoDisplay		= true;
		this.iconCategory			= 0;			// Dealut: flower
		this.imageIconResId			= R.mipmap.ic_launcher;
	}

	@Override
	public CardModel clone() { //基本的にはpublic修飾子を付け、自分自身の型を返り値とする
		CardModel cloned = null;

		/*ObjectクラスのcloneメソッドはCloneNotSupportedExceptionを投げる可能性があるので、try-catch文で記述(呼び出し元に投げても良い)*/
		try {
			cloned = (CardModel)super.clone(); //親クラスのcloneメソッドを呼び出す(親クラスの型で返ってくるので、自分自身の型でのキャストを忘れないようにする)
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

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFrontText() {
		return frontText;
	}

	public void setFrontText(String frontText) {
		this.frontText = frontText;
	}

	public String getBackText() {
		return backText;
	}

	public void setBackText(String backText) {
		this.backText = backText;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getLastUsedDate() {
		return lastUsedDate;
	}

	public void setLastUsedDate(Date lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}

	public boolean isFusenTag() {
		return fusenTag;
	}

	public void setFusenTag(boolean fusenTag) {
		this.fusenTag = fusenTag;
	}

	public boolean isLearned() {
		return learned;
	}

	public void setLearned(boolean learned) {
		this.learned = learned;
	}

	public int getImageIconResId() {
		return imageIconResId;
	}

	public void setImageIconResId(int imageIconResId) {
		this.imageIconResId = imageIconResId;
	}

	public void setIconAutoDisplay(boolean iconAutoDisplay) {
		this.iconAutoDisplay = iconAutoDisplay;
	}

	public boolean isIconAutoDisplay() {
		return iconAutoDisplay;
	}

	public void setIconCategory(int iconCategory) {
		this.iconCategory = iconCategory;
	}

	public int getIconCategory() {
		return iconCategory;
	}
}

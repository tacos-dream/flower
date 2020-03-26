package net.somethingnew.kawatan.flower.model;

import java.util.Date;
import java.util.UUID;

/**
 * ArrayListで管理する各CardのModel
 * フィールドとSetter/Getterを持つ
 * フィールド項目はSQLiteの保存項目とほぼ等しいので、
 * SQLiteのDAOとの間でやり取りするDTO（CardDto）クラスともほぼ同じ形になっている。
 * 冗長的ではあるが、とりあえずこれで！
 */
public class CardModel {
	String          id;                     // ユニークなKeyId (UUIDから生成）
	String          surfaceText;            // 表面テキスト
	String          backText;              	// 裏面テキスト
	Date            createdDate;            // 作成日
	Date            updatedDate;            // 最終更新日
	Date            lastUsedDate;           // 最終更新日
	boolean         learned;     			// 習得済み
	boolean         fusenTag;               // 付箋

	public CardModel(String surfaceText, String backText) {
		//this.id                     = UUID.randomUUID().toString();
		this.surfaceText            = surfaceText;
		this.backText            	= backText;
		this.createdDate            = new Date();
		this.updatedDate            = new Date();
		this.lastUsedDate           = new Date();
		this.learned           		= false;
		this.fusenTag				= false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSurfaceText() {
		return surfaceText;
	}

	public void setSurfaceText(String surfaceText) {
		this.surfaceText = surfaceText;
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
}
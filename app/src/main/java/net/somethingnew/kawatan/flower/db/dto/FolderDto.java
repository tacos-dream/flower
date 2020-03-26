package net.somethingnew.kawatan.flower.db.dto;

import java.util.Date;
import java.util.UUID;

public class FolderDto {
	String          id;                  	// ユニークなKeyId (UUIDから生成） TBLの検索Keyにする
	String          titleName;              // タイトル名
	Date            createdDate;            // 作成日
	Date            updatedDate;            // 最終更新日
	Date            lastUsedDate;           // 最終利用日
	int             numOfAllCards;          // 含まれるカード数
	int             numOfLearnedCards;      // 習得済みカード数
	int             imageIconResId;             // アイコンキャラ

	public FolderDto() {
		/*
		this.keyId                  = UUID.randomUUID().toString();
		this.titleName              = titleName;
		this.createdDate            = new Date();
		this.updatedDate            = new Date();
		this.lastUsedDate           = new Date();
		this.numOfAllCards          = 0;
		this.numOfLearnedCards      = 0;
		this.imageIconResId         = imageIconResId;

		 */
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
	public void     setImageResId(int imageIconResId) { this.imageIconResId = imageIconResId;}

}

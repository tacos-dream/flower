package net.somethingnew.kawatan.flower.model;

public class SearchResultModel {
	String folderId;
    String folderName;
    String frontText;
    String backText;
    int folderIndex;

    public SearchResultModel() {
    	this.folderId = "";
        this.folderName = "";
        this.frontText = "";
        this.backText = "";
        this.folderIndex = 0;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
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

    public int getFolderIndex() {
        return folderIndex;
    }

    public void setFolderIndex(int folderIndex) {
        this.folderIndex = folderIndex;
    }
}


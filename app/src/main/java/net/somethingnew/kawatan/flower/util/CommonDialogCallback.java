package net.somethingnew.kawatan.flower.util;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Message;

/**
 * CommonDialogCallback
 *
 */
public class CommonDialogCallback {
    /**
     * デフォルトコンストラクタ.<br>
     */
    public CommonDialogCallback() {
    }

    /**
     * mainProcess
     * 通信プログレスダイアログ処理実装.<BR>
     * @return 0 int
     */
    protected int mainProcess() throws Exception {
        return 0;
    }

    /**
     * postProcess
     * 通信プログレスダイアログ後処理実装.<BR>
     * @param msg Message
     */
    protected void postProcess(Message msg) throws Exception {
    }

    /**
     * positiveButtonProcess
     * 肯定ボタンアクション実装.<BR>
     */
    protected void positiveButtonProcess(DialogInterface dialog, int which) {
    }

    /**
     * neutralButtonProcess
     * 中立ボタンアクション実装.<BR>
     */
    protected void neutralButtonProcess(DialogInterface dialog, int which) {
    }

    /**
     * negativeButtonProcess
     * 否定ボタンアクション実装.<BR>
     */
    protected void negativeButtonProcess(DialogInterface dialog, int which) {
    }

    public static class WaitListItem {
        private Bitmap thumbnail = null;
        private String title = null;
        private String provider = null;
        private String videoQuizId = null;
        private String videoId = null;
        private String comment = null;
        private String thumbnailUrl = null;
        private String releaseDt = null;
        private int genre;
        private int numChallenges;
        private int elapsedSec;
        private int status;

        /**
         * 空のコンストラクタ
         */
        public WaitListItem() {
        }

        public Bitmap getThumbnail() {
            return thumbnail;
        }

        public String getComment() {
            return comment;
        }

        public String getProvider() {
            return provider;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getVideoQuizId() {
            return videoQuizId;
        }

        public int getElapsedSec() {
            return elapsedSec;
        }

        public String getReleaseDt() {
            return releaseDt;
        }

        public int getGenre() {
            return genre;
        }

        public int getNumChallenges() {
            return numChallenges;
        }

        public int getStatus() {
            return status;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public void setThumbnail(Bitmap thumbnail) {
            this.thumbnail = thumbnail;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public void setReleaseDt(String releaseDt) {
            this.releaseDt = releaseDt;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setElapsedSec(int elapsedSec) {
            this.elapsedSec = elapsedSec;
        }

        public void setGenre(int genre) {
            this.genre = genre;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public void setNumChallenges(int numChallenges) {
            this.numChallenges = numChallenges;
        }

        public void setVideoQuizId(String videoQuizId) {
            this.videoQuizId = videoQuizId;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
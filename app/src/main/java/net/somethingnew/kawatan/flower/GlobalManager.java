package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import net.somethingnew.kawatan.flower.db.DatabaseHelper;
import net.somethingnew.kawatan.flower.model.AvailableBookInfo;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 全体管理クラス
 */
public class GlobalManager {

    public Context mApplicationContext;

    // GlobalManagerのシングルトンインスタンスの生成は、起動直後のSplashActivityで
    // static関数のonCreateApplication()を呼び出した時点で生成する。
    // 同時にContextを渡してもらい、以後、アプリ内の各所でContext参照できるようにする
    private static GlobalManager mInstance = null;

    public int mCategory;
    public int skinHeaderColor;
    public int skinBodyColor;

    /**
     * Folder一覧を保持するLinedList
     */
    public LinkedList<FolderModel> mFolderLinkedList;
    public LinkedList<FolderModel> mSearchedFolderLinkedList;

    /**
     * Folder一覧でDrag&Dropによる並び順が変更になった場合に、どこからどこの範囲に並び替えの更新が発生したかを保持しておき、
     * 終了時に並び順をDBに反映する
     */
    public int mOrderChangedStartFolderIndex = 0;
    public int mOrderChangedEndFolderIndex = 0;

    /**
     * 現在処理中のFolderのLinkedList上のIndex
     * Listをタップしたときに確定する
     */
    public int mCurrentFolderIndex;

    /**
     * FolderSettingsDialogでユーザによる設定変更が行われたか否かの識別
     */
    public boolean mChangedFolderSettings;

    /**
     * FolderSettingsDialog内のCardViewに表示する背景色と文字色
     * および
     * ViewPagerの各Fragmentの文字色GridViewのIDを保持しておき、
     * パステルカラーGridや色パレットでの色選択時に動的に背景色や文字色を変更するために利用
     */
    public FolderSettings mFolderSettings;

    /**
     * FolderSettingsDialog操作中の色の変更内容を一時的に保持し、ユーザーの明示的な保存行為を受けて
     * mFolderLinkedListの該当箇所のFolderModelを更新するため、その一時保存用
     */
    public FolderModel mTempFolder;

    /**
     * 各FolderのCardListを保持するMap
     * Key: folderのid
     * Value: CardのLinkedList
     */
    public HashMap<String, LinkedList> mCardListMap;

    public boolean mChangedCardSettings;
    public CardSettings mCardSettings;
    public CardModel mTempCard;

    /**
     * 現在処理中のCardのLinkedList上のIndex
     * 主にListをタップしたときに確定する
     */
    public int mCurrentCardIndex;

    /**
     * Card数に変化があった場合（新規追加や削除）にFolderActivityからMainActivityに戻った時に
     * Card数の表示に反映させる必要がある場合かどうかの識別
     */
    public boolean mCardStatsChanged;

    public int currentCategoryPosition;

    public List<AvailableBookInfo> availableBookInfoList;

    public DatabaseHelper mDbHelper;

    public UserSettings mUserSettings;

    // FolderSettingsやCardSettingsでユーザーの色選択や入力文字操作変更を動的に表示に反映させるため、
    // 表示に関連するViewのリソースIDを保持しておく
    // 領域はカバー、おもて、うらで共用で、切り替えて使う（ImageViewを使うのはカバーのみ）
    class FolderSettings {
        CardView cardView;
        EditText editTextTitle;
        ImageView imageViewIcon;
    }

    class CardSettings {
        CardView cardViewFront;
        CardView cardViewBack;
        EditText editTextFront;
        EditText editTextBack;
        ImageView imageViewIcon;
        ImageView imageViewFusen;
    }

    class UserSettings {
        boolean aaa;
        int bbb;
    }

    /**
     * コンストラクタ.
     */
    private GlobalManager(Context applicationContext) {
        this.mApplicationContext = applicationContext;
        this.mCardListMap = new HashMap<>();
        this.mFolderSettings = new FolderSettings();
        this.mCardSettings = new CardSettings();
        this.mUserSettings = new UserSettings();
        this.availableBookInfoList = new ArrayList<>();
        this.currentCategoryPosition = Constants.CATEGORY_INDEX_FLOWER;
    }

    static void onCreateApplication(Context applicationContext) {
        // Application#onCreateのタイミングでシングルトンが生成される
        mInstance = new GlobalManager(applicationContext);
    }

    /**
     * インスタンス取得.<br>
     *
     * @param
     * @return GlobalManager
     */
    public static GlobalManager getInstance() {
        if (mInstance == null) {
            // SplashActivityからonCreateApplication()が呼び出されシングルトンインスタンスが生成されていれば
            // こんなことは起きないはず…
            throw new RuntimeException("MyContext should be initialized!");
        }
        return mInstance;
    }

}

package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import net.somethingnew.kawatan.flower.db.dto.CardDto;
import net.somethingnew.kawatan.flower.db.dto.FolderDto;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;

/**
 * 全体管理クラス
 */
public class GlobalManager {

    public Context                      mApplicationContext;

    // GlobalManagerのシングルトンインスタンスの生成は、起動直後のSplashActivityで
    // static関数のonCreateApplication()を呼び出した時点で生成する。
    // 同時にContextを渡してもらい、以後、アプリ内の各所でContext参照できるようにする
    private static GlobalManager                mInstance = null;

    /**
     * Folder一覧を保持するLinedList
     */
    public LinkedList<FolderModel>              mFolderLinkedList;

    /**
     * 現在処理中のFolderのLinkedList上のIndex
     * Listをタップしたときに確定する
     */
    public int                                  mCurrentFolderIndex;

    /**
     * FolderSettingsDialogでユーザによる設定変更が行われたか否かの識別
     */
    public boolean                              mChangedFolderSettings;

    /**
     * FolderSettingsDialog内のCardViewに表示する背景色と文字色
     * および
     * ViewPagerの各Fragmentの文字色GridView
     * のIDを保持しておき、パステルカラーGridや色パレットでの色選択時にに動的に背景色や文字色を変更するために利用
     */
    CardView                                    mCardViewInFolderSettings;
    View                                        mIncludeCardLayoutInFolderSettings;
    EditText                                    mEditTextTitleNameInFolderSettings;

    /**
     * FolderSettingsDialog操作中の色の変更内容を一時的に保持し、ユーザーの明示的な保存行為を受けて
     * mFolderLinkedListの該当箇所のFolderModelを更新するため、その一時保存用
     */
    FolderModel                                 mTempFolder;

    /**
     * 各FolderのCardListを保持するMap
     * Key: folderのid
     * Value: CardのLinkedList
     */
    public HashMap<String, LinkedList>          mCardListMap;

    /**
     * 現在処理中のCardのLinkedList上のIndex
     * 主にListをタップしたときに確定する
     */
    public int                                  mCurrentCardIndex;

    /**
     * ダイアログからActivityに値を返すためのDTO定義
     */
    public FolderDto                            mFolderDto;
    public CardDto                              mCardDto;

    /**
     * コンストラクタ.
     */
    private GlobalManager (Context applicationContext)
    {
        this.mApplicationContext    = applicationContext;
        this.mCardListMap           = new HashMap<>();
    }

    static void onCreateApplication(Context applicationContext) {
        // Application#onCreateのタイミングでシングルトンが生成される
        mInstance = new GlobalManager(applicationContext);
    }

    /**
     * インスタンス取得.<br>
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

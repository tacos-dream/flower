package net.somethingnew.kawatan.flower;

/**
 * 共通定数クラス.
 * 
 * @author Steve
 * 
 */
public final class Constants {

	Constants() {
	}

    public static final String VERSION = "202002B";

	public static final int SPLASH_TIME_MILLI_SEC       = 1500;
    /**
     * 戻り値
     */
    public static final int RTC_SUCCESS                 = 1;
    public static final int RTC_FAILURE                 = 0;
    public static final int RTC_CONNECTION_ERROR        = -1;
    public static final int RTC_IO_ERROR                = -2;
    public static final int RTC_NETWORK_UNAVAILABLE     = -3;


    /**
     * SharedPreference関連項目
     */
    public static final String SHARED_PREFERENCE_NAME               = "kawatan_pref";
    public static final String SHARED_PREF_KEY_ICON_RANDOM          = "iconRandomMode";


    /**
     * SQLite関連
     */
    public static final String DB_NAME                  = "KawatanV1.db";
    public static final int    DB_VERSION               = 1;
    public static final String TABLE_NAME_FOLDER        = "FOLDER_TBL";
    public static final String TABLE_NAME_CARD          = "CARD_TBL";

    // カラム名配列
    public static final String[] COLUMN_NAMES_FOLDER    =   { "FOLDER_ID", "TITLE_NAME", "CREATED_DATE", "UPDATED_DATE", "LAST_USED_DATE",
                                                              "NUM_OF_ALL_CARDS", "NUM_OF_LEARNED_CARDS", "ICON_RES_ID", "COVER_BG_COLOR",
                                                              "FRONT_BG_COLOR", "BACK_BG_COLOR", "COVER_TXT_COLOR", "FRONT_TXT_COLOR", "BACK_TXT_COLOR",
                                                              "FUSEN_RES_ID", "DISPLAY_ORDER", "ICON_CATEGORY", "ICON_AUTO_DISPLAY"
                                                            };
    public static final String[] COLUMN_NAMES_CARD    =     { "CARD_ID", "FOLDER_ID", "FRONT_TEXT", "BACK_TEXT", "CREATED_DATE",
                                                              "UPDATED_DATE", "LAST_USED_DATE", "LEARNED", "FUSEN_TAG", "ICON_AUTO_DISPLAY",
                                                              "ICON_CATEGORY", "ICON_RES_ID"
                                                            };

    /**
     * AdMob関連
     */
    public static final int ADMOB_AD_LIMITS                     = 10;
    public static final int ADMOB_NUM_OF_DATA_BETWEEN_ADS       = 5;
    public static final int ADMOB_FIRST_AD_INDEX                = 2;


    /**
     * URLs
     */
    public static final String  PLAYSTORE_KAWATAN_URI           = "market://details?id=jp.tacos_mcs.earth1";
    public static final String  LINE_STORE_QREX_URI             = "https://line.me/S/sticker/9712792";
    public static final String  YOUTUBE_TUTRIAL_URI             = "https://line.me/S/sticker/9712792";
    public static final String  NOTE_TERMS_URI                  = "https://line.me/S/sticker/9712792";


    /**
     * FolderSettingsのダイアログ関連
     */
    public static final String FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE      = "mode";
    public static final String FOLDER_SETTINGS_DIALOG_ARG_KEY_FOLDER_ID = "folder_id";
    public static final int FOLDER_SETTINGS_FOR_NEW                     = 1;
    public static final int FOLDER_SETTINGS_FOR_EDIT                    = 2;

    public static final int FOLDER_SETTINGS_NUM_OF_TABS                 = 5;

    public static final int FOLDER_SETTINGS_TAB_ICON                    = 0;
    public static final int FOLDER_SETTINGS_TAB_COVER                   = 1;
    public static final int FOLDER_SETTINGS_TAB_SURFACE                 = 2;
    public static final int FOLDER_SETTINGS_TAB_BACK                    = 3;
    public static final int FOLDER_SETTINGS_TAB_FUSEN                   = 4;
    public static final int FOLDER_SETTINGS_TAB_OTHERS                  = 5;


    /**
     * CardSettingsのダイアログ関連
     */
    public static final String CARD_SETTINGS_DIALOG_ARG_KEY_MODE        = "mode";
    public static final String CARD_SETTINGS_DIALOG_ARG_KEY_CARD_ID     = "card_id";
    public static final int CARD_SETTINGS_FOR_NEW                       = 1;
    public static final int CARD_SETTINGS_FOR_EDIT                      = 2;
    public static final int CARD_SETTINGS_NUM_OF_TABS                   = 6;

    /**
     * フィルターステート
     * ビットフラグの考えで制御する
     * ex.)  LEARNED(1) + NOT_LEARNED(2) = ALL(3)
     */
    public static final int FILTER_STATE_LEARNED                        = 1;
    public static final int FILTER_STATE_NOT_LEARNED                    = 2;
    public static final int FILTER_STATE_FUSEN                          = 1;
    public static final int FILTER_STATE_NO_FUSEN                       = 2;
    public static final int FILTER_STATE_ALL                            = 3;

    /**
     * Exerciseのモード
     */
    public static final String EXERCISE_MODE_KEY_NAME                   = "EXERCISE_MODE";
    public static final int EXERCISE_MODE_NORMAL                        = 1;
    public static final int EXERCISE_MODE_SHUFFLE                       = 2;

    /**
     * OnClickListenerの種類
     */
    public static final int ONCLICK_LISTENER_TYPE_CARD                  = 1;
    public static final int ONCLICK_LISTENER_TYPE_LEARNED               = 2;
    public static final int ONCLICK_LISTENER_TYPE_FUSEN                 = 3;

    /**
     * CategoryIconFragmentがどこで呼ばれたかの識別
     */
    public static final int CATEGORY_ICON_IN_FOLDER_SETTINGS            = 1;
    public static final int CATEGORY_ICON_IN_CARD_SETTINGS              = 2;

    /**
     * 付箋関連
     */
    public static final String      DEFAULT_FUSEN_NAME                  = "fusen_01";

    /**
     * アイコン関連
     */
    public static final int         NUM_OF_ICON_TAB                     = 8;
    public static final int[]       NUM_OF_ICONS_IN_CATEGORY            = {135, 67, 102, 75, 135, 80, 90, 77};
    public static final String[]    ICON_TAB_ARRAY                      = {"jewelry", "flower", "cosme", "heart", "parts", "character", "food", "alphanum"};
    public static final String[]    ICON_TAB_NAME_ARRAY                 = {"ジュエリー", "フラワー", "コスメ", "ハート", "パーツ", "表情", "たべもの", "数字ABC"};
    public static final String[]    ICON_TAB_IMAGE_ID                   = {"jewelry_001", "flower_001", "cosme_001", "heart_001","parts_001", "character_001", "food_001", "alphanum_001"};
    public static final String      DEFAULT_ICON_NAME                   = "alphanum_031";

    /**
     * ひらがなテーブル.<br>
     * ひらがな-&gt;カタカナ処理時の変換元文字列
     * 全80文字 [0] ～ [79]
     */
    // @formatter:off
    public static final Character[] HIRAGANA_CHAR_ARRAY = {
            'あ', 'い', 'う', 'え', 'お',
            'か', 'き', 'く', 'け', 'こ',
            'さ', 'し', 'す', 'せ', 'そ',
            'た', 'ち', 'つ', 'て', 'と',
            'な', 'に', 'ぬ', 'ね', 'の',
            'は', 'ひ', 'ふ', 'へ', 'ほ',
            'ま', 'み', 'む', 'め', 'も',
            'や', 'ゆ', 'よ',
            'ら', 'り', 'る', 'れ', 'ろ',
            'わ', 'を', 'ん',
            'ぁ', 'ぃ', 'ぅ', 'ぇ', 'ぉ',
            'っ',
            'ゃ', 'ゅ', 'ょ',
            'が', 'ぎ', 'ぐ', 'げ', 'ご',
            'ざ', 'じ', 'ず', 'ぜ', 'ぞ',
            'だ', 'ぢ', 'づ', 'で', 'ど',
            'ば', 'び', 'ぶ', 'べ', 'ぼ',
            'ぱ', 'ぴ', 'ぷ', 'ぺ', 'ぽ'
    };

}


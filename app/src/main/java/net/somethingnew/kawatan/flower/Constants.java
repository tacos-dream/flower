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
    public static final String SHARED_PREFERENCE_NAME = "kawatan_pref";
    public static final String SHARED_PREF_KEY_USERID = "userId";

    /**
     * SQLite関連
     */
    public static final String TABLE_NAME_FOLDER        = "FOLDER_TBL";
    public static final String TABLE_NAME_CARD          = "CARD_TBL";

    // カラム名配列
    public static final String[] COLUMN_NAMES_FOLDER    = {"SEC_CD", "MARKET_CD", "CMPNY_NM_SH5", "CMPNY_NM_KABU", "CMPNY_NM_K"};
    public static final String[] COLUMN_NAMES_CARD      = {"SEC_CD", "MARKET_CD", "CMPNY_NM_SH5", "CMPNY_NM_KABU", "CMPNY_NM_K"};


    /**
     * AdMob関連
     */
    public static final int ADMOB_AD_LIMITS                     = 10;
    public static final int ADMOB_NUM_OF_DATA_BETWEEN_ADS       = 5;
    public static final int ADMOB_FIRST_AD_INDEX                = 2;

    /**
     * 各種タイマー時間
     */
    public static final int SPLASH_DISPLAY_TIME = 1500;

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
     * アイコンのカテゴリー
     */
    public static final int ICON_CATEGORY_JEWELRY                   = 0;
    public static final int ICON_CATEGORY_FLOWER                    = 1;
    public static final int ICON_CATEGORY_COSME                     = 2;
    public static final int ICON_CATEGORY_FOOD                      = 3;
    public static final int ICON_CATEGORY_VEHICLE                   = 4;
    public static final int ICON_CATEGORY_ANIMAL                    = 5;

    /**
     * Folderでの表示フィルター
     */
    public static final int FILTER_STATUS_ALL                       = 0;
    public static final int FILTER_STATUS_LEARNED                   = 1;
    public static final int FILTER_STATUS_NOT_LEARNED               = 2;
    public static final int FILTER_STATUS_FUSEN                     = 3;
    public static final int NUM_OF_FILTER_STATUS                    = 4;

    /**
     * 学習ステータス
     */
    public static final int LEARNING_STATUS_NORMAL                  = 1;
    public static final int LEARNING_STATUS_FUSEN                   = 2;
    public static final int LEARNING_STATUS_DONE                    = 3;

    /**
     * フィルターステート
     * ビットフラグの考えで制御する
     * ex.)  LEARNED(1) + NOT_LEARNED(2) = ALL(3)
     */
    public static final int FILTER_STATE_LEARNED                    = 1;
    public static final int FILTER_STATE_NOT_LEARNED                = 2;
    public static final int FILTER_STATE_FUSEN                      = 1;
    public static final int FILTER_STATE_NO_FUSEN                   = 2;
    public static final int FILTER_STATE_ALL                        = 3;

    /**
     * Exerciseのモード
     */
    public static final String EXERCISE_MODE_KEY_NAME               = "EXERCISE_MODE";
    public static final int EXERCISE_MODE_NORMAL                    = 1;
    public static final int EXERCISE_MODE_SHUFFLE                   = 2;

    /**
     * OnClickListenerの種類
     */
    public static final int ONCLICK_LISTENER_TYPE_CARD                  = 1;
    public static final int ONCLICK_LISTENER_TYPE_LEARNED               = 2;
    public static final int ONCLICK_LISTENER_TYPE_FUSEN                 = 3;

    public static final int         NUM_OF_CARD_TAB         = 7;
    public static final String[]    CARD_TAB_ARRAY          = {"jewelry", "flower", "cosme", "food", "vehicle", "animal", "parts"};
    public static final String[]    CARD_TAB_NAME_ARRAY     = {"ジュエリー", "フラワー", "コスメ", "たべもの", "のりもの", "どうぶつ", "パーツ"};
    public static final String[]    CARD_TAB_IMAGE_ID       = {"jewelry_082_21", "flower_024_22", "jewelry_082_24", "flower_024_23","jewelry_082_28", "flower_024_24", "flower_024_24"};

    public static final String[][] ICON_NAME_ARRAY = {
            {
            "jewelry_076_01", "jewelry_076_05", "jewelry_076_06", "jewelry_076_07", "jewelry_076_08", "jewelry_076_09",
            "jewelry_076_10", "jewelry_076_11", "jewelry_076_16", "jewelry_076_17", "jewelry_076_18", "jewelry_076_21",
            "jewelry_076_22", "jewelry_076_23", "jewelry_076_26", "jewelry_076_27", "jewelry_076_29", "jewelry_076_30",
            "jewelry_076_31", "jewelry_076_32", "jewelry_076_33", "jewelry_076_34", "jewelry_076_35", "jewelry_076_37",
            "jewelry_076_38", "jewelry_076_41", "jewelry_078_01", "jewelry_078_02", "jewelry_078_05", "jewelry_078_08"
            },
            {
            "jewelry_078_11", "jewelry_078_12", "jewelry_078_13", "jewelry_078_14", "jewelry_078_16", "jewelry_078_18",
            "jewelry_078_20", "jewelry_078_21", "jewelry_078_25", "jewelry_078_28", "jewelry_078_30", "jewelry_078_33",
            "jewelry_078_34", "jewelry_078_35", "jewelry_080_02", "jewelry_080_03", "jewelry_080_04", "jewelry_080_05",
            "jewelry_080_06", "jewelry_080_07", "jewelry_080_08", "jewelry_080_09", "jewelry_080_10", "jewelry_080_11",
            "jewelry_080_12", "jewelry_080_13", "jewelry_080_15", "jewelry_080_16", "jewelry_080_17", "jewelry_080_18"
            },
            {
            "jewelry_080_19", "jewelry_080_20", "jewelry_080_21", "jewelry_080_22", "jewelry_080_23", "jewelry_080_24",
            "jewelry_080_25", "jewelry_080_26", "jewelry_080_27", "jewelry_080_28", "jewelry_080_29", "jewelry_080_30",
            "jewelry_080_31", "jewelry_080_32", "jewelry_080_33", "jewelry_080_35", "jewelry_080_36", "jewelry_080_37",
            "jewelry_080_38", "jewelry_080_39", "jewelry_080_40", "jewelry_080_41", "jewelry_080_42", "jewelry_080_43",
            "jewelry_080_44", "jewelry_082_01", "jewelry_082_02", "jewelry_082_03", "jewelry_082_04", "jewelry_082_05"
            },
            {
            "jewelry_082_06", "jewelry_082_07", "jewelry_082_08", "jewelry_082_09", "jewelry_082_10", "jewelry_082_11",
            "jewelry_082_12", "jewelry_082_13", "jewelry_082_14", "jewelry_082_15", "jewelry_082_16", "jewelry_082_17",
            "jewelry_082_18", "jewelry_082_19", "jewelry_082_20", "jewelry_082_21", "jewelry_082_22", "jewelry_082_23",
            "jewelry_082_24", "jewelry_082_25", "jewelry_082_26", "jewelry_082_27", "jewelry_082_28", "jewelry_082_29",
            "jewelry_082_30", "jewelry_082_31", "jewelry_082_32", "jewelry_082_33", "jewelry_082_34", "jewelry_082_35"
            },
            {
            "jewelry_082_36","jewelry_082_37","jewelry_082_38","jewelry_082_39","jewelry_082_40","jewelry_082_41",
            "jewelry_082_42","jewelry_082_43","jewelry_082_44","jewelry_082_45","jewelry_082_46","jewelry_082_47",
            "jewelry_082_48","jewelry_082_49","jewelry_082_50","jewelry_082_51","jewelry_082_52","jewelry_082_53",
            "jewelry_082_54","jewelry_082_55","jewelry_082_56","jewelry_082_57","jewelry_082_58","jewelry_084_22",
            "jewelry_084_23","jewelry_084_24","jewelry_084_25","jewelry_084_26","jewelry_084_27","jewelry_084_28"
            },
            {
            "jewelry_084_30","jewelry_086_02","jewelry_086_03","jewelry_086_04","jewelry_086_06","jewelry_086_07",
            "jewelry_086_09","jewelry_086_10","jewelry_086_15","jewelry_086_16","jewelry_086_17","jewelry_086_18",
            "jewelry_086_20","jewelry_086_22","jewelry_086_24"
            },
            {
            "jewelry_084_30","jewelry_086_02","jewelry_086_03","jewelry_086_04","jewelry_086_06","jewelry_086_07",
            "jewelry_086_09","jewelry_086_10","jewelry_086_15","jewelry_086_16","jewelry_086_17","jewelry_086_18",
            "jewelry_086_20","jewelry_086_22","jewelry_086_24"
            }
    };

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


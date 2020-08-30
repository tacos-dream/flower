package net.somethingnew.kawatan.flower;

/**
 * 共通定数クラス.
 *
 * @author Steve
 */
public final class Constants {

    Constants() {
    }

    public static final String VERSION = "202002B";

    /**
     * 時間関連
     */
    public static final int SPLASH_TIME_MILLI_SEC = 3000;

    /**
     * 戻り値
     */
    public static final int RTC_SUCCESS = 1;
    public static final int RTC_FAILURE = 0;
    public static final int RTC_CONNECTION_ERROR = -1;
    public static final int RTC_IO_ERROR = -2;
    public static final int RTC_NETWORK_UNAVAILABLE = -3;

    /**
     * 外部リンク用URL
     */
    public static final String EXTERNAL_LINK_URL_JSPUCHIKIRA = "https://book.impress.co.jp/books/1113101121";
    public static final String EXTERNAL_LINK_URL_KAWATN_INFO = "https://kawatan-2020.an.r.appspot.com/files/introduction.html";
    public static final String EXTERNAL_LINK_URL_KAWATN_HOWTO = "https://kawatan-2020.an.r.appspot.com/files/introduction.html";
    public static final String EXTERNAL_LINK_URL_KAWATN_TERMS = "https://kawatan-2020.an.r.appspot.com/files/terms.html";
    public static final String EXTERNAL_LINK_URL_KAWATN_PRIVACY_POLICY = "https://kawatan-2020.an.r.appspot.com/files/privacy_policy.html";
    //public static final String EXTERNAL_LINK_URL_PLAY_STORE           = "https://play.google.com/apps/testing/jp.tacos_mcs.earth1";
    public static final String EXTERNAL_LINK_URL_PLAY_STORE = "https://play.google.com/store/apps/details?id=jp.tacos_mcs.earth1";
    public static final String EXTERNAL_LINK_URL_HOWTO_YOUTUBE_WEB = "https://www.youtube.com/watch?v=P4ipJ2rXIJs";
    public static final String EXTERNAL_LINK_URL_HOWTO_YOUTUBE_APL = "vnd.youtube:P4ipJ2rXIJs";

    /**
     * メール問い合わせ関連
     */
    public static final String MAIL_TO_ADDRESS = "somethingnew.dream@gmail.com";
    public static final String MAIL_SUBJECT = "【かわたん】ご意見・ご感想・ご質問";
    public static final String MAIL_BODY = "いつもアプリのご利用ありがとうございます。\n\nご自由にご意見、ご感想、ご質問をお書きください。\nよろしくお願い致します。\n-----------------------------------------------";

    /**
     * SharedPreference関連項目
     */
    public static final String SHARED_PREFERENCE_NAME = "kawatan_pref";
    public static final String SHARED_PREF_KEY_CATEGORY = "category";
    public static final String SHARED_PREF_KEY_SKIN_HEADER_COLOR = "skinHeaderColor";
    public static final String SHARED_PREF_KEY_SKIN_BODY_COLOR = "skinBodyColor";
    public static final String SHARED_PREF_KEY_EXERCISE_FILTER = "exerciseFilter";


    /**
     * SQLite関連
     */
    public static final String DB_NAME = "KawatanV1.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME_FOLDER = "FOLDER_TBL";
    public static final String TABLE_NAME_CARD = "CARD_TBL";

    // カラム名配列
    public static final String[] COLUMN_NAMES_FOLDER = {"FOLDER_ID", "TITLE_NAME", "CREATED_DATE", "UPDATED_DATE", "LAST_USED_DATE",
            "NUM_OF_ALL_CARDS", "NUM_OF_LEARNED_CARDS", "ICON_RES_ID", "COVER_BG_COLOR",
            "FRONT_BG_COLOR", "BACK_BG_COLOR", "COVER_TXT_COLOR", "FRONT_TXT_COLOR", "BACK_TXT_COLOR",
            "FUSEN_RES_ID", "DISPLAY_ORDER", "ICON_CATEGORY", "ICON_AUTO_DISPLAY"
    };
    public static final String[] COLUMN_NAMES_CARD = {"CARD_ID", "FOLDER_ID", "FRONT_TEXT", "BACK_TEXT", "CREATED_DATE",
            "UPDATED_DATE", "LAST_USED_DATE", "LEARNED", "FUSEN_TAG", "ICON_AUTO_DISPLAY",
            "ICON_CATEGORY", "ICON_RES_ID"
    };

    /**
     * AdMob関連
     */
    public static final int ADMOB_AD_LIMITS = 10;
    public static final int ADMOB_NUM_OF_DATA_BETWEEN_ADS = 5;
    public static final int ADMOB_FIRST_AD_INDEX = 2;


    /**
     * URLs
     */
    public static final String PLAYSTORE_KAWATAN_URI = "market://details?id=jp.tacos_mcs.earth1";
    public static final String LINE_STORE_QREX_URI = "https://line.me/S/sticker/9712792";
    public static final String YOUTUBE_TUTRIAL_URI = "https://line.me/S/sticker/9712792";
    public static final String NOTE_TERMS_URI = "https://line.me/S/sticker/9712792";


    /**
     * FolderSettingsのダイアログ関連
     */
    public static final String FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE = "mode";
    public static final String FOLDER_SETTINGS_DIALOG_ARG_KEY_FOLDER_ID = "folder_id";
    public static final int FOLDER_SETTINGS_FOR_NEW = 1;
    public static final int FOLDER_SETTINGS_FOR_EDIT = 2;

    public static final int FOLDER_SETTINGS_NUM_OF_TABS = 5;

    public static final int FOLDER_SETTINGS_TAB_ICON = 0;
    public static final int FOLDER_SETTINGS_TAB_COVER = 1;
    public static final int FOLDER_SETTINGS_TAB_SURFACE = 2;
    public static final int FOLDER_SETTINGS_TAB_BACK = 3;
    public static final int FOLDER_SETTINGS_TAB_FUSEN = 4;
    public static final int FOLDER_SETTINGS_TAB_OTHERS = 5;


    /**
     * CardSettingsのダイアログ関連
     */
    public static final String CARD_SETTINGS_DIALOG_ARG_KEY_MODE = "mode";
    public static final String CARD_SETTINGS_DIALOG_ARG_KEY_CARD_ID = "card_id";
    public static final int CARD_SETTINGS_FOR_NEW = 1;
    public static final int CARD_SETTINGS_FOR_EDIT = 2;
    public static final int CARD_SETTINGS_NUM_OF_TABS = 6;

    /**
     * フィルターステート
     * ビットフラグの考えで制御する
     * ex.)  LEARNED(1) + NOT_LEARNED(2) = ALL(3)
     */
    public static final int FILTER_STATE_LEARNED = 1;
    public static final int FILTER_STATE_NOT_LEARNED = 2;
    public static final int FILTER_STATE_FUSEN = 1;
    public static final int FILTER_STATE_NO_FUSEN = 2;
    public static final int FILTER_STATE_ALL = 3;

    /**
     * Exerciseのモード
     */
    public static final String EXERCISE_MODE_KEY_NAME = "EXERCISE_MODE";
    public static final int EXERCISE_MODE_NORMAL = 1;
    public static final int EXERCISE_MODE_SHUFFLE = 2;

    /**
     * 検索関連
     */
    public static final String SEARCH_WORD_KEY_NAME = "SEARCH_WORD";

    /**
     * OnClickListenerの種類
     */
    public static final int ONCLICK_LISTENER_TYPE_CARD = 1;
    public static final int ONCLICK_LISTENER_TYPE_LEARNED = 2;
    public static final int ONCLICK_LISTENER_TYPE_FUSEN = 3;

    /**
     * CategoryIconFragmentがどこで呼ばれたかの識別
     */
    public static final int CATEGORY_ICON_IN_FOLDER_SETTINGS = 1;
    public static final int CATEGORY_ICON_IN_CARD_SETTINGS = 2;

    /**
     * 付箋関連
     */
    public static final String DEFAULT_FUSEN_NAME = "fusen_00";

    /**
     * アイコン関連
     */
    public static final int NUM_OF_ICON_TAB = 6;
    public static final int[] NUM_OF_ICONS_IN_CATEGORY = {40, 63, 49, 42, 50, 86};
    public static final String[] ICON_TAB_ARRAY = {"flower", "jewelry", "cosme", "fashion", "food", "others"};
    public static final String[] ICON_TAB_IMAGE_ID = {"flower_001", "jewelry_001", "cosme_001", "fashion_001", "food_001", "others_001"};
    public static final String[] AUTO_ICON_IMAGE_ID = {"flower_000", "jewelry_000", "cosme_000", "fashion_000", "food_000", "others_000"};

    /**
     * カテゴリー
     * 注意注意注意注意注意注意注意注意　　上記のICONの設定と合わせること　　注意注意注意注意注意注意注意注意
     */
    public static final String[] CATEGORY_NAME = {"Flower", "Jewelry", "Cosme", "Fashion", "Food", "Others"};
    public static final int CATEGORY_INDEX_FLOWER = 0;
    public static final int CATEGORY_INDEX_JEWELRY = 1;
    public static final int CATEGORY_INDEX_COSME = 2;
    public static final int CATEGORY_INDEX_FASHION = 3;
    public static final int CATEGORY_INDEX_FOOD = 4;
    public static final int CATEGORY_INDEX_OTHERS = 5;

    /**
     * COLOR関連
     */
    public static final String[] PASTEL_PALETTE_LIGHT = {"#FFFFFF", "#FFFFBB", "#DDEEAA", "#FFDDDD", "#FFEEDD", "#CCCCCC",
            "#FFFFFF", "#FFFFBB", "#DDEEAA", "#FFDDDD", "#FFEEDD", "#CCCCCC"};
    public static final String[] PASTEL_PALETTE_DEEP = {"#FC9DB8", "#FC9DB8", "#FC9DB8", "#FC9DB8", "#FC9DB8", "#FC9DB8",
            "#DDEEAA", "#FFDD33", "#93B447", "#B68BC4", "#A7B39B", "#B3B3B3"};
    public static final String DEFAULT_SKIN_HEADER_COLOR = "#FC9DB8";
    public static final String DEFAULT_SKIN_BODY_COLOR = "#FFFFBB";

    /**
     * FRAME SIZE etc.
     */
    public static final double DIALOG_FRAGMENT_WIDTH_RATIO = 0.99;
    public static final double DIALOG_FRAGMENT_HEIGHT_RATIO = 0.95;

}


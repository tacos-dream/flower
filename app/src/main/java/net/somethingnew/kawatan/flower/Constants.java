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
    public static final int FOLDER_SETTINGS_NUM_OF_TABS                 = 6;
    public static final int FOLDER_SETTINGS_TAB_ICON                    = 0;
    public static final int FOLDER_SETTINGS_TAB_COVER                   = 1;
    public static final int FOLDER_SETTINGS_TAB_SURFACE                 = 2;
    public static final int FOLDER_SETTINGS_TAB_BACK                    = 3;
    public static final int FOLDER_SETTINGS_TAB_FUSEN                   = 4;
    public static final int FOLDER_SETTINGS_TAB_OTHERS                  = 5;


    /**
     * CardSettingsのダイアログ関連
     */
    public static final String CARD_SETTINGS_DIALOG_ARG_KEY_MODE      = "mode";
    public static final String CARD_SETTINGS_DIALOG_ARG_KEY_CARD_ID = "card_id";
    public static final int CARD_SETTINGS_FOR_NEW                     = 1;
    public static final int CARD_SETTINGS_FOR_EDIT                    = 2;


    /**
     * OnClickListenerの種類
     */
    public static final int ONCLICK_LISTENER_TYPE_CARD                  = 1;
    public static final int ONCLICK_LISTENER_TYPE_LEARNED               = 2;
    public static final int ONCLICK_LISTENER_TYPE_FUSEN                 = 3;

    /**
     * Activityリクエストコード
     */
    public static final int REQUEST_CODE_VOICE_INPUT = 1;

    /**
     * Charset
     */
    public static final String  CHARSET_UTF8      = "UTF-8";

    /**
     * 仮名タイプ
     */
    public static final int KANA_TYPE_HIRAGANA = 0;
    public static final int KANA_TYPE_KATAKANA = 1;

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
    // @formatter:on

    /**
     * カタカナテーブル.<br>
     * ひらがな-&gt;カタカナ処理時の変換元文字列
     * 全80文字 [0] ～ [79]
     */
    // @formatter:off
    public static final Character[] KATAKANA_CHAR_ARRAY = {
            'ア', 'イ', 'ウ', 'エ', 'オ',
            'カ', 'キ', 'ク', 'ケ', 'コ',
            'サ', 'シ', 'ス', 'セ', 'ソ',
            'タ', 'チ', 'ツ', 'テ', 'ト',
            'ナ', 'ニ', 'ヌ', 'ネ', 'ノ',
            'ハ', 'ヒ', 'フ', 'ヘ', 'ホ',
            'マ', 'ミ', 'ム', 'メ', 'モ',
            'ヤ', 'ユ', 'ヨ',
            'ラ', 'リ', 'ル', 'レ', 'ロ',
            'ワ', 'ヲ', 'ン',
            'ァ', 'ィ', 'ゥ', 'ェ', 'ォ',
            'ッ',
            'ャ', 'ュ', 'ョ',
            'ガ', 'ギ', 'グ', 'ゲ', 'ゴ',
            'ザ', 'ジ', 'ズ', 'ゼ', 'ゾ',
            'ダ', 'ヂ', 'ヅ', 'デ', 'ド',
            'バ', 'ビ', 'ブ', 'ベ', 'ボ',
            'パ', 'ピ', 'プ', 'ペ', 'ポ'
    };
}


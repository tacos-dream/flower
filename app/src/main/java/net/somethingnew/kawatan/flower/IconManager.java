package net.somethingnew.kawatan.flower;

import android.content.Context;

import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.Random;

/**
 * 各Category毎にアイコンのリソースIDをArrayListに保持
 */
public class IconManager {

    private static ArrayList<Integer>[] mIconResourceIdListArray;
    private static int[] mAutoIconResourceIds;
    private static int[] mDefaultIconResourceIds;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;

        LogUtility.d("Loading Icons...");
        mIconResourceIdListArray = new ArrayList[Constants.NUM_OF_ICON_TAB];
        for (int category = 0; category < Constants.NUM_OF_ICON_TAB; category++) {
            mIconResourceIdListArray[category] = new ArrayList<>();
            mAutoIconResourceIds = new int[Constants.NUM_OF_CATEGORY];
            mDefaultIconResourceIds = new int[Constants.NUM_OF_CATEGORY];

            // Resource名をR.drawable.名前としてintに変換してarrayに登録
            // 各カテゴリ先頭のxxxx_000はAuto画像なので、それはmIconResourceIdListArrayには登録しない
            for (int i = 1; i <= Constants.NUM_OF_ICONS_IN_CATEGORY[category]; i++) {
                String iconName = Constants.ICON_TAB_ARRAY[category] + "_" + String.format("%03d", i);
                int imageId = mContext.getResources().getIdentifier(iconName, "drawable", mContext.getPackageName());
                mIconResourceIdListArray[category].add(imageId);
            }

            // 各カテゴリのAutoアイコンとDefaultアイコンのResourceIdを事前に配列に用意しておく
            for (int i = 0; i < Constants.NUM_OF_CATEGORY; i++) {
                mAutoIconResourceIds[i] = mContext.getResources().getIdentifier(Constants.AUTO_ICON_IMAGE_ID[i], "drawable", mContext.getPackageName());
                mDefaultIconResourceIds[i] = mContext.getResources().getIdentifier(Constants.ICON_DEFAULT_IMAGE_ID[i], "drawable", mContext.getPackageName());
            }
        }
    }

    public static int getResIdAtRandom(int category) {
        return mIconResourceIdListArray[category].get(new Random().nextInt(Constants.NUM_OF_ICONS_IN_CATEGORY[category] - 1) + 1);
    }

    public static ArrayList<Integer> getIconResourceIdList(int category) {
        return mIconResourceIdListArray[category];
    }

    public static int getAutoIconResId(int category) {
        return mAutoIconResourceIds[category];
    }

    public static int getDefaultIconResId(int category) {
        return mDefaultIconResourceIds[category];
    }

}
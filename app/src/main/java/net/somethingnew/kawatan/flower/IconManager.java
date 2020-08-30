package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.util.Log;

import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.Random;

public class IconManager {

    private static ArrayList<Integer>[]         mIconResourceIdListArray;
    private static Context                      mContext;

    public static void init(Context context) {
        mContext            = context;

        LogUtility.d("Loading Icons...");
        mIconResourceIdListArray                   = new ArrayList[Constants.NUM_OF_ICON_TAB];
        for (int category = 0; category < Constants.NUM_OF_ICON_TAB; category++) {
            mIconResourceIdListArray[category]     = new ArrayList<>();

            // Resource名をR.drawable.名前としてintに変換してarrayに登録
            for (int i = 0; i <= Constants.NUM_OF_ICONS_IN_CATEGORY[category]; i++) {
                String iconName     = Constants.ICON_TAB_ARRAY[category] + "_" +  String.format("%03d", i+1);
                int imageId             = mContext.getResources().getIdentifier(iconName,"drawable", mContext.getPackageName());
                mIconResourceIdListArray[category].add(imageId);
            }
        }
    }

    public static int getResIdAtRandom(int category){
        return mIconResourceIdListArray[category].get(new Random().nextInt(Constants.NUM_OF_ICONS_IN_CATEGORY[category] - 1) + 1);
    }

    public static ArrayList<Integer> getIconResourceIdList(int category){
        return mIconResourceIdListArray[category];
    }

}
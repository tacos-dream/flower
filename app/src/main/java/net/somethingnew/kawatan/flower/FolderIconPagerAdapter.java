package net.somethingnew.kawatan.flower;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.somethingnew.kawatan.flower.util.LogUtility;


public class FolderIconPagerAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public FolderIconPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        LogUtility.d("registeredFragments.put: " + position);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
        LogUtility.d("registeredFragments.remove: " + position);
    }

    // 該当positionのFragmentを返す
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }


    /**
     * position番号は、tabTitlesのリストのインデックス
     */
    public Fragment getItem(int position) {
        LogUtility.d("getItem: " + position);
        return new CategoryIconFragment(position, Constants.CATEGORY_ICON_IN_FOLDER_SETTINGS);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        LogUtility.d("getPageTitle position:" + position);
        return Constants.ICON_TAB_ARRAY[position];
    }

    @Override
    public int getCount() {
        return Constants.NUM_OF_ICON_TAB;
    }
}

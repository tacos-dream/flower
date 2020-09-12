package net.somethingnew.kawatan.flower;

import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import net.somethingnew.kawatan.flower.util.LogUtility;

public class FolderSettingsDialogPagerAdapter extends FragmentPagerAdapter {

    private CharSequence[] tabTitles = {"画像", "カバー", "おもて", "うら"};
    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public FolderSettingsDialogPagerAdapter(FragmentManager fm) {
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
        switch (position) {
            case 0:
                return new FolderIconFragment();
            case 1:
                return new FolderCoverFragment();
            case 2:
                return new FolderFrontFragment();
            case 3:
                return new FolderBackFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        LogUtility.d("getPageTitle position:" + position);
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}

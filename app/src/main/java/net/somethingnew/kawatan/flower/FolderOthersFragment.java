package net.somethingnew.kawatan.flower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import net.somethingnew.kawatan.flower.util.LogUtility;


public class FolderOthersFragment extends Fragment {

    View                        mView;

    public FolderOthersFragment() {
        LogUtility.d("FolderOthersFragment: ");
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_folder_others, container, false);
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

}

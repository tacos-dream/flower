package net.somethingnew.kawatan.flower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import net.somethingnew.kawatan.flower.util.LogUtility;


public class FolderFusenFragment extends Fragment {

    View                        mView;

    public FolderFusenFragment() {
        LogUtility.d("FolderFusenFragment: ");
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_folder_fusen, container, false);
        return mView;
    }

}

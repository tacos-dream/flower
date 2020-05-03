package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;
import com.maltaisn.icondialog.pack.IconPackLoader;
import com.maltaisn.iconpack.defaultpack.IconPackDefault;
import com.maltaisn.iconpack.mdi.IconPackMdi;

import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.buttonCreateFolderDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderDao folderDao = new FolderDao(getApplicationContext());
                //folderDao.truncate();

                ArrayList<FolderModel> arrayList = new ArrayList<>();
                for (int i = 0; i < MyData.folderNameArray.length; i++) {
                    FolderModel folderModel = new FolderModel(MyData.folderDrawableArray[i], R.drawable.fusen_01);
                    folderModel.setTitleName(MyData.folderNameArray[i]);
                    arrayList.add(folderModel);
                }

                folderDao.bulkInsert(arrayList);

            }
        });

        findViewById(R.id.buttonCreateCardDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return;
    }

    public void onStart() {
        super.onStart();
    }

}

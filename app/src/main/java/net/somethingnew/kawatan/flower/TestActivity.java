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

import net.somethingnew.kawatan.flower.util.LogUtility;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class TestActivity extends AppCompatActivity implements IconDialog.Callback {

    @Nullable
    private IconPack iconPack;

    private static final String ICON_DIALOG_TAG = "icon-dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadIconPack();
        setContentView(R.layout.activity_test);

        findViewById(R.id.buttonOpenDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If dialog is already added to fragment manager, get it. If not, create a new instance.
                IconDialog dialog = (IconDialog) getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
                IconDialog iconDialog = dialog != null ? dialog
                        : IconDialog.newInstance(new IconDialogSettings.Builder().build());

                // Open icon dialog
                iconDialog.show(getSupportFragmentManager(), ICON_DIALOG_TAG);
            }
        });

        initExpandableGridView();

        return;
    }

    public void onStart() {
        super.onStart();
    }

    @Nullable
    public IconPack getIconPack() {
        return iconPack != null ? iconPack : loadIconPack();
    }

    private IconPack loadIconPack() {
        // Create an icon pack loader with application context.
        IconPackLoader loader = new IconPackLoader(this);

        // Create an icon pack and load all drawables.
        //iconPack = IconPackDefault.createDefaultIconPack(loader);
        iconPack = IconPackMdi.createMaterialDesignIconPack(loader);
        iconPack.loadDrawables(loader.getDrawableLoader());

        return iconPack;
    }

    @Nullable
    @Override
    public IconPack getIconDialogIconPack() {
        //return ((App) getApplication()).getIconPack();
        return getIconPack();
    }

    @Override
    public void onIconDialogIconsSelected(@NotNull IconDialog dialog, @NotNull List<Icon> icons) {
        // Show a toast with the list of selected icon IDs.
        StringBuilder sb = new StringBuilder();
        for (Icon icon : icons) {
            sb.append(icon.getId());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        Toast.makeText(this, "Icons selected: " + sb, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIconDialogCancelled() {}

    /**
     * ExpandableGridViewのテスト
     */
    private void initExpandableGridView(){

        String[] countryData = {"China", "USA", "UK", "Russia", "France", "Germany", "Japan", "Korea", "Canada"};
        final String[] stateData = {"State A", "State B", "State C", "State D"};

        // 0.Init the ExpandableGridView
        final ExpandableGridView countryGridView = findViewById(R.id.country_grid);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getBaseContext(),
                R.layout.gridview_item_test, R.id.grid_item, countryData);
        // 1.Set adapter for the grid view
        countryGridView.setAdapter(countryAdapter);
        // 2.Add click event listener to the grid view, expand grid view when item is clicked
        countryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // expand the grid view
                countryGridView.expandGridViewAtView(view, new ArrayAdapter<>(getBaseContext(),
                        R.layout.gridview_item_test, R.id.grid_item, stateData));
            }
        });
        // 3.Click event listener of sub GridView items
        countryGridView.setOnExpandItemClickListener(new ExpandableGridView.OnExpandItemClickListener() {
            @Override
            public void onItemClick(int position, Object clickPositionData) {
                Toast.makeText(getBaseContext(), clickPositionData.toString()+" clicked", Toast.LENGTH_LONG).show();
            }
        });
    }
}

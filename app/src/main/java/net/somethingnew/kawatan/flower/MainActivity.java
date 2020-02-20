package net.somethingnew.kawatan.flower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private Context             mContext;
    private Activity            mActivity;

    private CoordinatorLayout   mCLayout;
    private ListView            mListView;
    private ArrayAdapter        mAdapter;
    private Random              mRandom = new Random();

    private SearchView          mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext        = getApplicationContext();
        mActivity       = MainActivity.this;

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // ハンバーガーメニューの準備
        setDrawer(toolbar);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(mCLayout, "Fabを押しました！", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.parseColor("#FF203071"));
                snackbar.show();
            }
        });

        // Get the widget reference from XML layout
        mCLayout = findViewById(R.id.coordinator_layout);
        mListView = findViewById(R.id.folders_list_view);

        // Get a list of installed apps package names
        final List<String> packages = getInstalledPackages();

        // Initialize an array adapter
        mAdapter = new ArrayAdapter<String>(this,R.layout.listview_custom_item_view,packages){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the view
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View itemView = inflater.inflate(R.layout.listview_custom_item_view,null,true);

                // Get current package name
                String packageName = packages.get(position);

                // Get the relative layout
                RelativeLayout relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl);

                // Display the app package name
                TextView tv_package = (TextView) itemView.findViewById(R.id.app_package);
                tv_package.setText(packageName);

                // Get the app label
                TextView tv_label = (TextView) itemView.findViewById(R.id.app_label);
                tv_label.setText(getApplicationLabelByPackageName(packageName));

                // Get the card view
                CardView cardView = (CardView) itemView.findViewById(R.id.card_view);

                // Generate a random background color for card view
                cardView.setBackgroundColor(getRandomDarkerHSVColor());

                // Generate the icon
                ImageView iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
                Drawable icon = getAppIconByPackageName(packageName);
                iv_icon.setImageDrawable(icon);

                // Finally, return the view
                return itemView;
            }
        };

        // Data bind the list view with array adapter items
        mListView.setAdapter(mAdapter);

        // Set an item click listener for list view
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CardView cardView = (CardView) view.findViewById(R.id.card_view);
                TextView tv_label = (TextView) view.findViewById(R.id.app_label);
                TextView tv_package = (TextView) view.findViewById(R.id.app_package);

                // Display the clicked items details on snack bar
                Snackbar snackbar = Snackbar.make(
                        mCLayout,
                        "Clicked : " + tv_label.getText() + "\n"+tv_package.getText(),
                        Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.parseColor("#FF203071"));
                snackbar.show();

                // Initialize a new color drawable
                ColorDrawable[] color = {
                        new ColorDrawable(getRandomDarkerHSVColor()),
                        new ColorDrawable(getRandomDarkerHSVColor())
                };
                // Initialize a new transition drawable
                TransitionDrawable trans = new TransitionDrawable(color);
                trans.startTransition(300); // duration 300 milliseconds

                // Animate the background color of card view
                cardView.setBackground(trans);
                trans.startTransition(300); // duration 3 seconds
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView)myActionMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchWord) {
                // なぜか2回呼ばれてしまうことへの回避策
                mSearchView.clearFocus();

                if (searchWord != null && !searchWord.equals("")) {
                    LogUtility.d("onQueryTextSubmit searchWord: " + searchWord);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //LogUtility.d("onQueryTextChange : " + s);
                return true;
            }
        });
        return true;
    }

    /**
     * 横スライドメニュー（ハンバーガーメニュー）の表示
     */
    private void setDrawer(Toolbar toolbar) {
        // DrawerToggle
        DrawerLayout drawerLayout = findViewById(R.id.main_drawer);
        ActionBarDrawerToggle drawerToggle  = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                // 描画指示
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.addDrawerListener(drawerToggle );
        drawerToggle.syncState();

        // DrawerLayoutだけでもメニューは作れるが、表現をよりリッチにするためにNavigationViewを入れる。
        // NavigationViewでのItemが選択したときのリスナを設定する
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 横スライドメニュー内の項目選択時の処理ハンドラ
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.naviItemSkinDesign:
                LogUtility.d("スキンデザインが選択されました");
                break;
            case R.id.naviItemIconDesign:
                LogUtility.d("アイコンデザインが選択されました");
                break;
            case R.id.naviItemSplashDesign:
                LogUtility.d("スプラッシュデザインが選択されました");
                break;
            case R.id.naviItemImportData:
                LogUtility.d("データインポートが選択されました");
                break;
            case R.id.naviItemExportData:
                LogUtility.d("データバックアップが選択されました");
                break;
            case R.id.naviItemHowToVideo:
                LogUtility.d("操作説明動画が選択されました");
                break;
            case R.id.naviItemHowToWeb:
                LogUtility.d("操作説明ページが選択されました");
                break;
            case R.id.naviItemInfo:
                LogUtility.d("新機能／更新情報が選択されました");
                break;
            case R.id.naviItemQuestionUs:
                LogUtility.d("お問い合わせが選択されました");
                break;
            case R.id.naviItemTerms:
                LogUtility.d("利用規約が選択されました");
                break;
            case R.id.naviItemReviewUs:
                LogUtility.d("レビューするが選択されました");
                break;
            case R.id.naviItemShareUs:
                LogUtility.d("アプリをシェアするが選択されました");
                break;
            case R.id.naviItemAboutUs:
                LogUtility.d("このアプリについてが選択されました");
                break;
        }

        // NavigationDrawerを消す
        DrawerLayout drawer = findViewById(R.id.main_drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Get a list of installed app
    public List<String> getInstalledPackages(){
        // Initialize a new Intent which action is main
        Intent intent = new Intent(Intent.ACTION_MAIN,null);

        // Set the newly created intent category to launcher
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // Set the intent flags
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK|
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );

        // Generate a list of ResolveInfo object based on intent filter
        List<ResolveInfo> resolveInfoList = mContext.getPackageManager().queryIntentActivities(intent,0);

        // Initialize a new ArrayList for holding non system package names
        List<String> packageNames = new ArrayList<>();

        // Loop through the ResolveInfo list
        for(ResolveInfo resolveInfo : resolveInfoList){
            // Get the ActivityInfo from current ResolveInfo
            ActivityInfo activityInfo = resolveInfo.activityInfo;

            // Add the package to the list
            packageNames.add(activityInfo.applicationInfo.packageName);

            // If this is not a system app package
            /*if(!isSystemPackage(resolveInfo)){
                // Add the non system package to the list
                packageNames.add(activityInfo.applicationInfo.packageName);
            }*/
        }

        return packageNames;
    }

    // Custom method to determine an app is system app
    public boolean isSystemPackage(ResolveInfo resolveInfo){
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    // Custom method to get application icon by package name
    public Drawable getAppIconByPackageName(String packageName){
        Drawable icon;
        try{
            icon = mContext.getPackageManager().getApplicationIcon(packageName);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            // Get a default icon
            // このアイコンのxmlは以下からコピーした
            // https://github.com/google/material-design-icons/blob/master/av/drawable-anydpi-v21/ic_play_circle_filled_black_24dp.xml
            icon = ContextCompat.getDrawable(mContext,R.drawable.ic_play_circle_filled_black_24dp);
        }
        return icon;
    }

    // Custom method to get application label by package name
    public String getApplicationLabelByPackageName(String packageName){
        PackageManager packageManager = mContext.getPackageManager();
        ApplicationInfo applicationInfo;
        String label = "Unknown";
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if(applicationInfo!=null){
                label = (String)packageManager.getApplicationLabel(applicationInfo);
            }

        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return label;
    }

    // Custom method to generate random darker HSV color
    public int getRandomDarkerHSVColor(){
        // Generate a random hue value between 0 to 360
        int hue = mRandom.nextInt(361);
        // We make the color depth full
        float saturation = 1.0f;
        // We make a full bright color
        float value = 0.8f;
        // We avoid color transparency
        int alpha = 255;
        // Finally, generate the color
        int color = Color.HSVToColor(alpha, new float[]{hue, saturation, value});
        // Return the color
        return color;
    }
}

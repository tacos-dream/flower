package net.somethingnew.kawatan.flower;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.Arrays;
import java.util.LinkedList;

public class CardSettingsDialogFragment extends DialogFragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    View mView;
    RecyclerView.Adapter mRecyclerViewAdapter;      // CardListのAdapter。新規追加、変更を即時にCardListに反映させるため
    CardModel mCardModel;
    FolderModel mFolder;
    LinkedList<CardModel> mCardLinkedList;
    LayoutInflater mInflater;
    SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();
    int mMode;       // 新規追加 or 編集
    int mPosition;      // 選択したCardのCardの一覧内での位置

    CardSettingsDialogFragment(RecyclerView.Adapter recyclerViewAdapter, CardModel cardModel, int position) {
        this.mRecyclerViewAdapter = recyclerViewAdapter;
        this.mCardModel = cardModel;
        this.mCardLinkedList = globalMgr.mCardListMap.get(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId());
        this.mPosition = position;

        for (int category = 0; category < Constants.NUM_OF_CATEGORY; category++) {
            Fragment fragment = new CategoryIconFragment(category, Constants.CATEGORY_ICON_IN_CARD_SETTINGS);
            mRegisteredFragments.put(category, fragment);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 呼び出し側からの引数の受け取り
        mMode = getArguments().getInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE);

        mFolder = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex);
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_card_settings, container, false);

        mView.findViewById(R.id.linearLayoutWhole).setBackgroundColor(globalMgr.skinBodyColor);
        mView.findViewById(R.id.linearLayoutMenu).setBackgroundColor(globalMgr.skinHeaderColor);

        globalMgr.mCardSettings.cardViewFront = mView.findViewById(R.id.card_view_front);
        globalMgr.mCardSettings.cardViewBack = mView.findViewById(R.id.card_view_back);
        globalMgr.mCardSettings.editTextFront = mView.findViewById(R.id.editTextFront);
        globalMgr.mCardSettings.editTextBack = mView.findViewById(R.id.editTextBack);
        globalMgr.mCardSettings.imageViewIconFront = mView.findViewById(R.id.imageViewIconFront);
        globalMgr.mCardSettings.imageViewIconBack = mView.findViewById(R.id.imageViewIconBack);
        globalMgr.mChangedCardSettings = false;

        buildEventListener();
        buildBottomNavigationMenu();

        // メニュー内アイコン
        ImageView imageView1 = mView.findViewById(R.id.imageViewReserved1);
        imageView1.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
        ImageView imageView2 = mView.findViewById(R.id.imageViewReserved2);
        imageView2.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));

        // ダイアログ表示中のユーザーの設定変更情報を一時インスタンスに保持するためにインスタンス作成（新規かClone）
        if (mMode == Constants.CARD_SETTINGS_FOR_NEW) {
            // アイコン自動表示ONの場合は「Auto」画像を表示するが、OFFの場合はランダムに選んだものを初期表示として一旦、表示する
            int iconResId = globalMgr.isIconAuto ? IconManager.getAutoIconResId(globalMgr.mCategory) : IconManager.getResIdAtRandom(globalMgr.mCategory);
            globalMgr.mTempCard = new CardModel(mFolder.getId(), iconResId);
        } else {
            // 選択されたCardのクローン
            globalMgr.mTempCard = mCardModel.clone();
        }

        // おもてとうらのカードへの初期表示
        globalMgr.mCardSettings.cardViewFront.setCardBackgroundColor(mFolder.getFrontBackgroundColor());
        globalMgr.mCardSettings.cardViewBack.setCardBackgroundColor(mFolder.getBackBackgroundColor());
        globalMgr.mCardSettings.editTextFront.setText(globalMgr.mTempCard.getFrontText());
        globalMgr.mCardSettings.editTextFront.setTextColor(mFolder.getFrontTextColor());
        globalMgr.mCardSettings.editTextBack.setText(globalMgr.mTempCard.getBackText());
        globalMgr.mCardSettings.editTextBack.setTextColor(mFolder.getBackTextColor());
        globalMgr.mCardSettings.imageViewIconFront.setImageResource(
                globalMgr.isIconAuto ? IconManager.getAutoIconResId(globalMgr.mCategory) : globalMgr.mTempCard.getImageIconResId());
        globalMgr.mCardSettings.imageViewIconBack.setImageResource(
                globalMgr.isIconAuto ? IconManager.getAutoIconResId(globalMgr.mCategory) : globalMgr.mTempCard.getImageIconResId());

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        // OSの戻るボタンの無効化
        // メニュー内の戻るボタンを実装するまではとりあえずコメント
        //this.setCancelable(false);

        return mView;
    }

    /**
     * 上記の通り、onCloseの後に呼び出されることはなくなったが、
     * 確認AlertDialogでgetDialog().dismiss()が呼ばれたときはここに来る
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Toast.makeText(getActivity().getApplicationContext(), "onDismiss", Toast.LENGTH_LONG).show();
        LogUtility.d("onDismiss");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    /**
     * 上部メニュー領域の各アイコン画像やカードの編集箇所のイベントハンドラ
     */
    public void buildEventListener() {
        // 戻る
        mView.findViewById(R.id.imageViewGoBack).setOnClickListener(v -> {
            //Toast.makeText(getActivity().getApplicationContext(), "キャンセル", Toast.LENGTH_LONG).show();
            if (mMode == Constants.FOLDER_SETTINGS_FOR_NEW) {
                getDialog().dismiss();      // 一覧に戻る
                return;
            }

            if (!globalMgr.mChangedCardSettings) {
                // 変更なしの場合は確認ダイアログ無しですぐに一覧に戻る
                getDialog().dismiss();
                return;
            }
            LogUtility.d("imageViewGoBack onClick");
            new AlertDialog.Builder(getContext())
                    .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                    .setTitle(R.string.dlg_title_goback_list)
                    .setMessage(R.string.dlg_msg_go_back_to_list)
                    .setPositiveButton(
                            R.string.go_back_list,
                            (dialog, which) -> {
                                LogUtility.d("[一覧に戻る]が選択されました");
                                getDialog().dismiss();      // 親も消す
                            })
                    .setNegativeButton(
                            R.string.cancel,
                            (dialog, which) -> {
                                LogUtility.d("[キャンセル]が選択されました");
                                // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                            })
                    .show();
        });
        // 保存
        mView.findViewById(R.id.imageViewSave).setOnClickListener(v -> {
            if (!globalMgr.mChangedCardSettings) {
                new AlertDialog.Builder(getContext())
                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                        .setTitle(R.string.dlg_title_save_confirm)
                        .setMessage("変更がありません")
                        .setPositiveButton(
                                R.string.close,
                                (dialog, which) -> {
                                    // 単にダイアログが消えて何もしない
                                })
                        .show();
                return;
            }

            if (globalMgr.mTempCard.getFrontText().isEmpty() || globalMgr.mTempCard.getBackText().isEmpty()) {
                new AlertDialog.Builder(getContext())
                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                        .setTitle(R.string.dlg_title_save_confirm)
                        .setMessage("[おもて]と[うら]の両方を入力してください")
                        .setPositiveButton(
                                R.string.close,
                                (dialog, which) -> {
                                    // 単にダイアログが消えて何もしない
                                })
                        .show();
                return;
            }

            // ユーザーによる設定情報の変更をmCardLinkedListに反映する
            new AlertDialog.Builder(getContext())
                    .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                    .setTitle(R.string.dlg_title_save_confirm)
                    .setMessage(R.string.dlg_msg_save)
                    .setPositiveButton(
                            R.string.save,
                            (dialog, which) -> {
                                LogUtility.d("[保存]が選択されました");
                                CardDao cardDao = new CardDao(getActivity().getApplicationContext());
                                if (mMode == Constants.CARD_SETTINGS_FOR_NEW) {
                                    // 先頭に追加
                                    mCardLinkedList.add(0, globalMgr.mTempCard);
                                    cardDao.insert(globalMgr.mTempCard);        // DB上は特に順番は意識しない

                                    // カード数更新
                                    globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).incrementNumOfAllCards();
                                    FolderDao folderDao = new FolderDao(getActivity().getApplicationContext());
                                    folderDao.update(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex));

                                    globalMgr.mCardStatsChanged = true;         // Folder一覧表示時のリフレッシュ動作で参照

                                } else {
                                    // 上書き
                                    mCardLinkedList.set(mPosition, globalMgr.mTempCard);
                                    cardDao.update(globalMgr.mTempCard);

                                    // TODO カード数や習得済み数に変更があった場合に、FOLDER_TBLへの反映
                                    // ここの保存が呼ばれるまではtempFolderに反映しておいて、最後に更新した方がいいが・・・
                                    // その「最後」というのがどのタイミングにすべきかが明確にできない・・・
                                    // とりあえず、ここで反映
                                    globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).incrementNumOfAllCards();
                                    FolderDao folderDao = new FolderDao(getActivity().getApplicationContext());
                                    folderDao.update(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex));
                                }

                                mRecyclerViewAdapter.notifyDataSetChanged();
                                getDialog().dismiss();      // 親も消す
                            })
                    .setNegativeButton(
                            R.string.cancel,
                            (dialog, which) -> {
                                LogUtility.d("[キャンセル]が選択されました");
                                // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                            })
                    .show();

        });
        // ゴミ箱（破棄）
        mView.findViewById(R.id.imageViewTrash).setOnClickListener(v -> {
            if (mMode == Constants.CARD_SETTINGS_FOR_NEW) {
                getDialog().dismiss();      // 一覧に戻る
                return;
            }
            //Toast.makeText(getActivity().getApplicationContext(), "ゴミ箱", Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(getContext())
                    .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                    .setTitle(R.string.dlg_title_delete_confirm)
                    .setMessage(R.string.dlg_msg_delete_card)
                    .setPositiveButton(
                            R.string.delete,
                            (dialog, which) -> {
                                LogUtility.d("[削除]が選択されました");

                                // LinkedListから削除
                                mCardLinkedList.remove(mPosition);

                                // Folderで管理しているカード数などを更新
                                globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).decrementNumOfAllCards();
                                if (globalMgr.mTempCard.isLearned())
                                    globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).decrementNumOfLearnedCards();

                                // DBに反映
                                FolderDao folderDao = new FolderDao(getActivity().getApplicationContext());
                                CardDao cardDao = new CardDao(getActivity().getApplicationContext());
                                folderDao.update(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex));
                                cardDao.deleteByCardId(globalMgr.mTempCard.getId());

                                globalMgr.mCardStatsChanged = true;         // Folder一覧表示時のリフレッシュ動作で参照

                                mRecyclerViewAdapter.notifyItemRemoved(mPosition);
                                getDialog().dismiss();
                            })
                    .setNegativeButton(
                            R.string.no_delete,
                            (dialog, which) -> {
                                LogUtility.d("[キャンセル]が選択されました");
                                // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                            })
                    .show();
        });

        // ヘルプ
        mView.findViewById(R.id.imageViewHelp).setOnClickListener(v -> {
            if (globalMgr.isJapanese) {
                CardSettingsHelpDialogFragment cardSettingsHelpDialogFragment = new CardSettingsHelpDialogFragment();
                cardSettingsHelpDialogFragment.show(getChildFragmentManager(),
                        FolderSettingsDialogFragment.class.getSimpleName());
            }
        });

        // Card FrontのEditTextのイベントリスナ
        globalMgr.mCardSettings.editTextFront.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (mMode == Constants.CARD_SETTINGS_FOR_NEW) {
                    globalMgr.mTempCard.setFrontText(s.toString());
                    globalMgr.mChangedCardSettings = true;
                } else {
                    if (!mCardModel.getFrontText().equals(s.toString())) {
                        globalMgr.mTempCard.setFrontText(s.toString());
                        globalMgr.mChangedCardSettings = true;
                    }
                }
            }
        });

        // Card BackのEditTextのイベントリスナ
        globalMgr.mCardSettings.editTextBack.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (mMode == Constants.CARD_SETTINGS_FOR_NEW) {
                    globalMgr.mTempCard.setBackText(s.toString());
                    globalMgr.mChangedCardSettings = true;
                } else {
                    if (!mCardModel.getBackText().equals(s.toString())) {
                        globalMgr.mTempCard.setBackText(s.toString());
                        globalMgr.mChangedCardSettings = true;
                    }
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Dialog dialog = getDialog();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * Constants.DIALOG_FRAGMENT_WIDTH_RATIO);
        int dialogHeight = (int) (metrics.heightPixels * Constants.DIALOG_FRAGMENT_HEIGHT_RATIO);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
        LogUtility.d("CardSettingsDialog: W" + dialogWidth + " x H" + dialogHeight);
    }

    /**
     * BottomNavigationMenuの構築とリスナー登録
     */
    public void buildBottomNavigationMenu() {
        // BottomNavigationで切り替える５つのアイコンカテゴリーのFragmentを作成
        BottomNavigationView bottomNavigationView = (BottomNavigationView) mView.findViewById(R.id.icon_bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        for (int category = Constants.CATEGORY_INDEX_FLOWER; category < Constants.NUM_OF_CATEGORY; category++) {
            menu.getItem(category).setIcon(IconManager.getResIdAtRandom(category));
        }
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setBackgroundColor(globalMgr.skinHeaderColor);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_flower:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_FLOWER;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_FLOWER));
                    break;
                case R.id.navigation_jewelry:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_JEWELRY;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_JEWELRY));
                    break;
                case R.id.navigation_fashion:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_FASHION;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_FASHION));
                    break;
                case R.id.navigation_food:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_FOOD;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_FOOD));
                    break;
                case R.id.navigation_others:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_OTHERS;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_OTHERS));
                    break;
            }
            return true;
        });

        // 初期表示
        showFragment(mRegisteredFragments.get(globalMgr.currentCategoryPosition));
    }

    // レイアウトのicon_container部分(LinearLayout)を引数のfragmentで入れ替え表示する
    public void showFragment(Fragment fragment) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null)
                .replace(R.id.icon_container, fragment)
                .commit();
    }

}

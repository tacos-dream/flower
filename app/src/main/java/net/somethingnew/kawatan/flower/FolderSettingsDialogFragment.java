package net.somethingnew.kawatan.flower;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;
import java.util.LinkedList;

public class FolderSettingsDialogFragment extends DialogFragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    View view;
    RecyclerView.Adapter recyclerViewAdapter;
    TabLayout tabLayout;
    FolderSettingsDialogPagerAdapter folderSettingsDialogPagerAdapter;
    int mode;       // 新規追加 or 編集
    int currentTabPosition;         // アイコン、カバー、おもて、うら　のTabいずれかの位置を指す

    FolderSettingsDialogFragment(RecyclerView.Adapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 呼び出し側からの引数の受け取り
        mode = getArguments().getInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE);

        view = inflater.inflate(R.layout.dialog_folder_settings, container, false);

        view.findViewById(R.id.linearLayoutWhole).setBackgroundColor(globalMgr.skinBodyColor);
        view.findViewById(R.id.linearLayoutMenu).setBackgroundColor(globalMgr.skinHeaderColor);

        // 呼び出し側からの引数の受け取り
        mode = getArguments().getInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE);

        // 各タブでの選択内容をCardViewに反映させるためにCardViewおよびその中の各種View情報をglobalMgrに保持しておく
        globalMgr.mFolderSettings.cardView = view.findViewById(R.id.card_view);

        globalMgr.mFolderSettings.editTextTitle = view.findViewById(R.id.editTextTitleName);
        globalMgr.mFolderSettings.imageViewIcon = view.findViewById(R.id.imageViewIcon);

        ImageView imageView1 = view.findViewById(R.id.imageViewReserved1);
        ImageView imageView2 = view.findViewById(R.id.imageViewReserved2);
        imageView1.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
        imageView2.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));

        globalMgr.mChangedFolderSettings = false;

        // Pagerでのスワイプなどの操作をTabレイアウトに連動する（伝える）ため、TabレイアウトのリスナーをViewPagerに登録しておく
        tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setBackgroundColor(globalMgr.skinHeaderColor);

        // Pagerで実際に切り替える複数のFragmentをPagerに供給するため、PagerAdapterのインスタンスを作成しPagerに登録しておく
        // PagerAdapterのgetItem()でFragmentを返すことで、PagerはPagerの対象とするFragmentを把握する
        folderSettingsDialogPagerAdapter = new FolderSettingsDialogPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(folderSettingsDialogPagerAdapter);
        viewPager.setOffscreenPageLimit(Constants.FOLDER_SETTINGS_NUM_OF_TABS);

        // TabをクリックしてViewPagerに該当のFragmentのPageを表示するために、ViewPagerをTabLayoutに紐づける以下のsetupWithViewPagerだけでOKだが、
        // どのタブがクリックされたかを拾って何かしたいときは、さらにaddOnTabSelectedListenerでリスナ登録し
        // イベントを処理する
        tabLayout.setupWithViewPager(viewPager);

        // Tabのイベントリスナ
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab selectedTab) {
                //LogUtility.d("OnTabSelectedListener onTabSelected position: " + tab.getPosition());
                // カードのLayoutを該当のものに切り替える
                currentTabPosition = selectedTab.getPosition();
                FolderFrontFragment folderFrontFragment;
                switch (currentTabPosition) {
                    case Constants.FOLDER_SETTINGS_TAB_ICON:
                        // ICONのGridViewの背景色とCardViewの背景色をCoverの背景色で表示する。CardViewに該当アイコンを表示する
                        FolderIconFragment folderIconFragment = (FolderIconFragment) folderSettingsDialogPagerAdapter.getRegisteredFragment(currentTabPosition);
                        folderIconFragment.refreshCategoryIconFragment();
                        globalMgr.mFolderSettings.cardView.setCardBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());
                        globalMgr.mFolderSettings.imageViewIcon.setImageResource(globalMgr.mTempFolder.getImageIconResId());
                        globalMgr.mFolderSettings.imageViewIcon.setVisibility(View.VISIBLE);
                        globalMgr.mFolderSettings.editTextTitle.setTextColor(globalMgr.mTempFolder.getCoverTextColor());
                        globalMgr.mFolderSettings.editTextTitle.setEnabled(true);
                        globalMgr.mFolderSettings.editTextTitle.setText(globalMgr.mTempFolder.getTitleName());
                        break;
                    case Constants.FOLDER_SETTINGS_TAB_COVER:
                        // TextのGridViewの背景色とCardViewの背景色をCoverの背景色で表示する。CardViewに該当アイコンを表示する
                        FolderCoverFragment folderCoverFragment = (FolderCoverFragment) folderSettingsDialogPagerAdapter.getRegisteredFragment(currentTabPosition);
                        folderCoverFragment.changeTextColorButtonsBackground(globalMgr.mTempFolder.getCoverBackgroundColor());
                        globalMgr.mFolderSettings.cardView.setCardBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());
                        globalMgr.mFolderSettings.imageViewIcon.setVisibility(View.VISIBLE);
                        globalMgr.mFolderSettings.imageViewIcon.setImageResource(globalMgr.mTempFolder.getImageIconResId());
                        globalMgr.mFolderSettings.editTextTitle.setTextColor(globalMgr.mTempFolder.getCoverTextColor());
                        globalMgr.mFolderSettings.editTextTitle.setEnabled(true);
                        globalMgr.mFolderSettings.editTextTitle.setText(globalMgr.mTempFolder.getTitleName());
                        break;
                    case Constants.FOLDER_SETTINGS_TAB_FRONT:
                        // TextのGridViewの背景色とCardViewの背景色をFrontの背景色で表示する。
                        folderFrontFragment = (FolderFrontFragment) folderSettingsDialogPagerAdapter.getRegisteredFragment(currentTabPosition);
                        folderFrontFragment.changeTextColorButtonsBackground(globalMgr.mTempFolder.getFrontBackgroundColor());
                        globalMgr.mFolderSettings.cardView.setCardBackgroundColor(globalMgr.mTempFolder.getFrontBackgroundColor());
                        globalMgr.mFolderSettings.imageViewIcon.setVisibility(View.INVISIBLE);
                        globalMgr.mFolderSettings.editTextTitle.setTextColor(globalMgr.mTempFolder.getFrontTextColor());
                        globalMgr.mFolderSettings.editTextTitle.setText(R.string.card_front);
                        globalMgr.mFolderSettings.editTextTitle.setEnabled(false);
                        break;
                    case Constants.FOLDER_SETTINGS_TAB_BACK:
                        // TextのGridViewの背景色とCardViewの背景色をBackの背景色で表示する。
                        FolderBackFragment folderBackFragment = (FolderBackFragment) folderSettingsDialogPagerAdapter.getRegisteredFragment(currentTabPosition);
                        folderBackFragment.changeTextColorButtonsBackground(globalMgr.mTempFolder.getBackBackgroundColor());
                        globalMgr.mFolderSettings.cardView.setCardBackgroundColor(globalMgr.mTempFolder.getBackBackgroundColor());
                        globalMgr.mFolderSettings.imageViewIcon.setVisibility(View.INVISIBLE);
                        globalMgr.mFolderSettings.editTextTitle.setTextColor(globalMgr.mTempFolder.getBackTextColor());
                        globalMgr.mFolderSettings.editTextTitle.setText(R.string.card_back);
                        globalMgr.mFolderSettings.editTextTitle.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab unSelectedTab) {
                LogUtility.d("OnTabSelectedListener onTabUnselected position: " + unSelectedTab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab reSelectedTab) {
                LogUtility.d("OnTabSelectedListener onTabReselected position: " + reSelectedTab.getPosition());
            }
        });

        // フォルダ名のEditTextのイベントリスナ
        globalMgr.mFolderSettings.editTextTitle.addTextChangedListener(new TextWatcher() {
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
                if (currentTabPosition == Constants.FOLDER_SETTINGS_TAB_COVER || currentTabPosition == Constants.FOLDER_SETTINGS_TAB_ICON) {

                    if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
                        globalMgr.mTempFolder.setTitleName(s.toString());
                        globalMgr.mChangedFolderSettings = true;
                    } else {
                        if (!globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getTitleName().equals(s.toString())) {
                            globalMgr.mTempFolder.setTitleName(s.toString());
                            globalMgr.mChangedFolderSettings = true;
                        }
                    }
                }
            }
        });

        buildMenuEventListener();

        // ダイアログ表示中のユーザーの設定変更情報を一時インスタンスに保持するためにインスタンス作成（新規かClone）し、
        // 初期表示設定を行う
        if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
            int iconImageResId = getResources().getIdentifier(Constants.AUTO_ICON_IMAGE_ID[globalMgr.mCategory], "drawable", getActivity().getPackageName());
            int fusenImageResId = getResources().getIdentifier(Constants.DEFAULT_FUSEN_NAME, "drawable", getActivity().getPackageName());
            globalMgr.mTempFolder = new FolderModel(iconImageResId, fusenImageResId);
            globalMgr.mCardListMap.put(globalMgr.mTempFolder.getId(), new LinkedList<>());
        } else {
            // 選択されたFolderの内容の表示
            globalMgr.mTempFolder = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).clone();

            // FolderSettingsダイアログで最初に表示するFragmentは「アイコン」なので、その表示準備
            // アイコン表示の場合もタイトル名（単語帳名）の入力は許可する
            globalMgr.mFolderSettings.editTextTitle.setText(globalMgr.mTempFolder.getTitleName());
            globalMgr.mFolderSettings.editTextTitle.setTextColor(globalMgr.mTempFolder.getCoverTextColor());
            globalMgr.mFolderSettings.imageViewIcon.setImageResource(globalMgr.mTempFolder.getImageIconResId());
            globalMgr.mFolderSettings.cardView.setCardBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        // 戻るボタンの無効化
        // dialog.setCancelableは効かない。DialogFragmentのsetCancelableを使用する
        //getDialog().setCancelable();
        this.setCancelable(false);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        /**
         * Androidの標準戻るボタンだと、ここに来て、親のダイアログが消えた後にAlertDialogでのConfirmが出るので意味ない。
         * DailogFragmentからさらにAlertDialogを出そうとしても、それは親子にならず、DialogFragmentが消えてからAlertDialogが出るみたい。
         * なので、Androidの標準戻るボタンは、DialogFragmentのsetCancelableで無効化する。実質、ここに処理が来ることはなくなった。
         * よって、実質、ここに来ることはなくなった。
         * setCancelableをやめれば、戻るボタン押下でここに来て、そのあとonDismissがよばれる
         *
         */
        //以下を呼ぶと親のダイアログが消えてしまうので呼ばないようにする
        //super.onCancel(dialog);
        LogUtility.d("onCancel");

        if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
            getDialog().dismiss();      // 一覧に戻る
            return;
        }

        if (!globalMgr.mChangedFolderSettings) {
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
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtility.d("[一覧に戻る]が選択されました");
                                getDialog().dismiss();      // 親も消す
                            }
                        })
                .setNegativeButton(
                        R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtility.d("[キャンセル]が選択されました");
                            }
                        })
                .show();

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

    public void buildMenuEventListener() {
        // 戻る
        view.findViewById(R.id.imageViewGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!globalMgr.mChangedFolderSettings) {
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
            }
        });

        // 保存
        view.findViewById(R.id.imageViewSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalMgr.mChangedFolderSettings) {
                    // ユーザーによる設定情報の変更をmFolderLinkedListに反映する
                    new AlertDialog.Builder(getContext())
                            .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                            .setTitle(R.string.dlg_title_save_confirm)
                            .setMessage(R.string.dlg_msg_save)
                            .setPositiveButton(
                                    R.string.save,
                                    (dialog, which) -> {
                                        FolderDao folderDao = new FolderDao(getActivity().getApplicationContext());
                                        if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
                                            // 既存Folderの順序を繰り下げる
                                            int i = 1;
                                            for (FolderModel folder : globalMgr.mFolderLinkedList) {
                                                folder.setOrder(i++);
                                                folderDao.update(folder);
                                            }

                                            // 先頭に追加
                                            globalMgr.mFolderLinkedList.add(0, globalMgr.mTempFolder);
                                            folderDao.insert(globalMgr.mTempFolder, 0);

                                            // CardListMapに入れ物だけ作っておく
                                            LinkedList<CardModel> cardLinkedList = new LinkedList<>();
                                            globalMgr.mCardListMap.put(globalMgr.mTempFolder.getId(), cardLinkedList);

                                        } else {
                                            // 上書き
                                            globalMgr.mFolderLinkedList.set(globalMgr.mCurrentFolderIndex, globalMgr.mTempFolder);
                                            folderDao.update(globalMgr.mTempFolder);
                                        }
                                        recyclerViewAdapter.notifyDataSetChanged();
                                        getDialog().dismiss();      // 親も消す
                                    })
                            .setNegativeButton(
                                    R.string.cancel,
                                    (dialog, which) -> {
                                        LogUtility.d("[キャンセル]が選択されました");
                                        // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                    })
                            .show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "設定変更無し", Toast.LENGTH_LONG).show();
                }
            }
        });

        // ゴミ箱（破棄）
        view.findViewById(R.id.imageViewTrash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "ゴミ箱", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(getContext())
                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                        .setTitle(R.string.dlg_title_delete_confirm)
                        .setMessage(R.string.dlg_msg_delete_folder)
                        .setPositiveButton(
                                R.string.delete,
                                (dialog, which) -> {
                                    if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
                                        getDialog().dismiss();
                                        return;
                                    }

                                    // Card関連
                                    //   LinkedList自体の削除
                                    //   管理しているmapから削除
                                    //   CARD_TBLからの削除
                                    String folderId = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId();
                                    globalMgr.mCardListMap.get(folderId).clear();
                                    globalMgr.mCardListMap.remove(folderId);
                                    CardDao cardDao = new CardDao(getActivity().getApplicationContext());
                                    cardDao.deleteByFolderId(folderId);

                                    // Folderのorderの付け替え処理は不要（Drag&Dropをやめたので）

                                    // FolderのLinkedListから削除
                                    globalMgr.mFolderLinkedList.remove(globalMgr.mCurrentFolderIndex);
                                    FolderDao folderDao = new FolderDao(getActivity().getApplicationContext());
                                    folderDao.deleteByFolderId(folderId);

                                    // 削除したFolderの後ろのorderを更新する
                                    for (int index = globalMgr.mCurrentFolderIndex; index < globalMgr.mFolderLinkedList.size(); index++) {
                                        FolderModel folder = globalMgr.mFolderLinkedList.get(index);
                                        folder.setOrder(index);
                                        folderDao.update(folder);
                                    }

                                    getDialog().dismiss();

                                    recyclerViewAdapter.notifyItemRemoved(globalMgr.mCurrentFolderIndex);
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                (dialog, which) -> {
                                    LogUtility.d("[キャンセル]が選択されました");
                                    // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                })
                        .show();
            }
        });

        // ヘルプ
        view.findViewById(R.id.imageViewHelp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "ヘルプ", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ダイアログの縦横を最大限まで広げる（何もしないと縦長で横幅の狭いデフォルトのダイアログが表示されるため
        Dialog dialog = getDialog();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * Constants.DIALOG_FRAGMENT_WIDTH_RATIO);
        int dialogHeight = (int) (metrics.heightPixels * Constants.DIALOG_FRAGMENT_HEIGHT_RATIO);
        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
        LogUtility.d("FolderSettingsDialog: W" + dialogWidth + " x H" + dialogHeight);
    }

}

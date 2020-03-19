package net.somethingnew.kawatan.flower;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.LinkedList;

public class FolderSettingsDialogFragment extends DialogFragment {

    GlobalManager                       globalMgr = GlobalManager.getInstance();
    View                                view;
    RecyclerView.Adapter                recyclerViewAdapter;
    int                                 mode;       // 新規追加 or 編集

    FolderSettingsDialogFragment(RecyclerView.Adapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    /* 以下のonCreateDialog()でAlertDialogで処理すると、以下のExceptionが発生するので、onCreateView()で処理するようにする
     * java.lang.IllegalStateException: Fragment androidx.fragment.app.Fragment$4@6df3970 does not have a view
     */
    /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater     = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view                   = inflater.inflate(R.layout.dialog_folder_settings, null);

        // 色を選択したときにCardの色を動的に変更できるよう、CardのView情報を保持しておく
        globalMgr.mCardViewInFolderSettings             = view.findViewById(R.id.card_view);

        // 表紙、おもて、うらの選択に応じてCardの表示を変更できるよう、CardのLinerLayoutの情報を保持しておく
        // デフォルトはXMLに記載の表紙の情報だが、これを動的におもてやうらに変更する。
        globalMgr.mIncludeCardLayoutInFolderSettings    = view.findViewById(R.id.cardLayout);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        Dialog dialog = builder.create();

        TabLayout tabLayout     = view.findViewById(R.id.tabLayout);
        ViewPager viewPager     = view.findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        FolderSettingsDialogPagerAdapter folderSettingsDialogPagerAdapter
                = new FolderSettingsDialogPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(folderSettingsDialogPagerAdapter);
        viewPager.setOffscreenPageLimit(Constants.FOLDER_SETTINGS_NUM_OF_TABS);

        tabLayout.setupWithViewPager(viewPager);

        CharSequence[] tabTitles = {"アイコン", "表紙", "おもて", "うら", "付箋", "その他" };
        for (int i = 0; i < Constants.FOLDER_SETTINGS_NUM_OF_TABS; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setTag(tabTitles[i]);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtility.d("OnTabSelectedListener onTabSelected: " + tab.getTag());
                LogUtility.d("OnTabSelectedListener onTabSelected position: " + tab.getPosition());
                // カードのLayoutを該当のものに切り替える
                int position = tab.getPosition();
                switch (position) {
                    case Constants.FOLDER_SETTINGS_TAB_ICON:
                    case Constants.FOLDER_SETTINGS_TAB_COVER:
                    case Constants.FOLDER_SETTINGS_TAB_FUSEN:
                    case Constants.FOLDER_SETTINGS_TAB_OTHERS:
                        // レイアウトを変更
                        drawCardView(R.layout.card_cover, globalMgr.mTempFolder.getCoverBackgroundColor());
                        break;
                    case Constants.FOLDER_SETTINGS_TAB_SURFACE:
                        drawCardView(R.layout.card_surface, globalMgr.mTempFolder.getSurfaceBackgroundColor());
                        break;
                    case Constants.FOLDER_SETTINGS_TAB_BACK:
                        drawCardView(R.layout.card_back, globalMgr.mTempFolder.getBackBackgroundColor());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogUtility.d("OnTabSelectedListener onTabUnselected: " + tab.getTag());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LogUtility.d("OnTabSelectedListener onTabReselected position: " + tab.getPosition());
                int position = tab.getPosition();
            }
        });

        // 選択されたFolderの内容を表示
        FolderModel folderModel         = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex);
        TextView    textViewTitle       = view.findViewById(R.id.textViewTitleName);
        ImageView   imageViewIcon       = view.findViewById(R.id.imageViewIcon);
        textViewTitle.setText(folderModel.getTitleName());
        imageViewIcon.setImageResource(folderModel.getImageResId());

        // ダイアログ表示中のユーザーの設定変更情報を一時インスタンスに保持するためにClone作成
        globalMgr.mTempFolder                           = folderModel.clone();

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);

        // Create the AlertDialog object and return it
        return dialog;
    }
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_folder_settings, container, false);

        // 呼び出し側からの引数の受け取り
        mode = getArguments().getInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE);

        // 色を選択したときにCardの色を動的に変更できるよう、CardのView情報を保持しておく
        globalMgr.mCardViewInFolderSettings             = view.findViewById(R.id.card_view);

        // ここで保存しても、後でTabを選択時にcardView部分のlayoutをdrawCardViewで入れ替えているので、drawCardViewの方でも保存する
        globalMgr.mEditTextTitleNameInFolderSettings    = view.findViewById(R.id.editTextTitleName);

        // 表紙、おもて、うらの選択に応じてCardの表示を変更できるよう、CardのLinerLayoutの情報を保持しておく
        // デフォルトはXMLに記載の表紙の情報だが、これを動的におもてやうらに変更する。
        globalMgr.mIncludeCardLayoutInFolderSettings    = view.findViewById(R.id.cardLayout);

        globalMgr.mChangedFolderSettings                = false;

        TabLayout tabLayout     = view.findViewById(R.id.tabLayout);
        ViewPager viewPager     = view.findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        FolderSettingsDialogPagerAdapter folderSettingsDialogPagerAdapter
                = new FolderSettingsDialogPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(folderSettingsDialogPagerAdapter);
        viewPager.setOffscreenPageLimit(Constants.FOLDER_SETTINGS_NUM_OF_TABS);

        // TabをクリックしてViewPagerに該当のFragmentのPageを表示するために、ViewPagerをTabLayoutに紐づける以下のsetupWithViewPagerだけでOKだが、
        // どのタブがクリックされたかを拾って何かしたいときは、さらにaddOnTabSelectedListenerでリスナ登録し
        // イベントを処理する
        tabLayout.setupWithViewPager(viewPager);

        CharSequence[] tabTitles = {"アイコン", "表紙", "おもて", "うら", "付箋", "その他" };
        for (int i = 0; i < Constants.FOLDER_SETTINGS_NUM_OF_TABS; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setTag(tabTitles[i]);
        }

        // フォルダ名のEditTextのイベントリスナ
        EditText    editTextTitle       = view.findViewById(R.id.editTextTitleName);
        editTextTitle.addTextChangedListener( new TextWatcher() {
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
                if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
                    globalMgr.mTempFolder.setTitleName(s.toString());
                    globalMgr.mChangedFolderSettings = true;
                }
                else {
                    if (!globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getTitleName().equals(s.toString())) {
                        globalMgr.mTempFolder.setTitleName(s.toString());
                        globalMgr.mChangedFolderSettings = true;
                    }
                }
            }
        } );

        // Tabのイベントリスナ
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtility.d("OnTabSelectedListener onTabSelected: " + tab.getTag());
                LogUtility.d("OnTabSelectedListener onTabSelected position: " + tab.getPosition());
                // カードのLayoutを該当のものに切り替える
                int position = tab.getPosition();
                switch (position) {
                    case Constants.FOLDER_SETTINGS_TAB_ICON:
                    case Constants.FOLDER_SETTINGS_TAB_COVER:
                    case Constants.FOLDER_SETTINGS_TAB_FUSEN:
                    case Constants.FOLDER_SETTINGS_TAB_OTHERS:
                        // レイアウトを変更
                        drawCardView(R.layout.card_cover, globalMgr.mTempFolder.getCoverBackgroundColor());
                        break;
                    case Constants.FOLDER_SETTINGS_TAB_SURFACE:
                        drawCardView(R.layout.card_surface, globalMgr.mTempFolder.getSurfaceBackgroundColor());
                        break;
                    case Constants.FOLDER_SETTINGS_TAB_BACK:
                        drawCardView(R.layout.card_back, globalMgr.mTempFolder.getBackBackgroundColor());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogUtility.d("OnTabSelectedListener onTabUnselected: " + tab.getTag());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LogUtility.d("OnTabSelectedListener onTabReselected position: " + tab.getPosition());
                int position = tab.getPosition();
            }
        });

        /**
         * 上部メニュー領域の各アイコン画像のクリックハンドラ
         */
        // 戻る
        view.findViewById(R.id.imageViewGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "キャンセル", Toast.LENGTH_LONG).show();
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
                        .setIcon(R.drawable.flower_024_19)
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
                                        // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                    }
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
                            .setIcon(R.drawable.flower_024_19)
                            .setMessage(R.string.dlg_msg_save)
                            .setPositiveButton(
                                    R.string.save,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            LogUtility.d("[保存]が選択されました");
                                            if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
                                                // 先頭に追加
                                                globalMgr.mFolderLinkedList.add(0, globalMgr.mTempFolder);
                                            }
                                            else {
                                                // 上書き
                                                globalMgr.mFolderLinkedList.set(globalMgr.mCurrentFolderIndex, globalMgr.mTempFolder);
                                            }
                                            recyclerViewAdapter.notifyDataSetChanged();
                                            Toast.makeText(getActivity().getApplicationContext(), "設定変更を保存しました", Toast.LENGTH_LONG).show();
                                            getDialog().dismiss();      // 親も消す
                                        }
                                    })
                            .setNegativeButton(
                                    R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            LogUtility.d("[キャンセル]が選択されました");
                                            // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                        }
                                    })
                            .show();
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "設定変更無し", Toast.LENGTH_LONG).show();
                }
            }
        });
        // ゴミ箱（破棄）
        view.findViewById(R.id.imageViewTrash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
                    getDialog().dismiss();      // 一覧に戻る
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "ゴミ箱", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.flower_024_19)
                        .setMessage(R.string.dlg_msg_delete_folder)
                        .setPositiveButton(
                                R.string.delete,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[削除]が選択されました");

                                        // LinkedListから削除し一覧に戻る
                                        globalMgr.mFolderLinkedList.remove(globalMgr.mCurrentFolderIndex);
                                        // TODO CardのLinkedListとそれを管理しているmapからも削除要

                                        getDialog().dismiss();

                                        recyclerViewAdapter.notifyItemRemoved(globalMgr.mCurrentFolderIndex);
                                    }
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[キャンセル]が選択されました");
                                        // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                    }
                                })
                        .show();
            }
        });
        // 習得済み・習得解除
        view.findViewById(R.id.imageViewLearned).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
                    return;
                }

                //Toast.makeText(getActivity().getApplicationContext(), "習得済み", Toast.LENGTH_LONG).show();
                int res_string_id = R.string.dlg_msg_learned_on;
                if (globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).isLearned()) {
                    res_string_id = R.string.dlg_msg_learned_off;
                }
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.flower_024_19)
                        .setMessage(res_string_id)
                        .setPositiveButton(
                                R.string.apply,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[習得済み/習得解除]が選択されました");
                                        ImageView imageView = view.findViewById(R.id.imageViewLearned);
                                        if (!globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).isLearned()) {
                                            // 習得済みへの変更処理
                                            globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).setLearned(true);
                                            imageView.setImageResource(R.drawable.heart_on);
                                        }
                                        else {
                                            // 習得解除への変更処理
                                            globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).setLearned(false);
                                            imageView.setImageResource(R.drawable.heart_off_grey);
                                        }
                                    }
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[キャンセル]が選択されました");
                                        // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                    }
                                })
                        .show();
            }
        });
        // 共有（シェア）
        /*
        view.findViewById(R.id.imageViewShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "シェア", Toast.LENGTH_LONG).show();
            }
        });
        */

        // ヘルプ
        view.findViewById(R.id.imageViewHelp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "ヘルプ", Toast.LENGTH_LONG).show();
            }
        });

        // ダイアログ表示中のユーザーの設定変更情報を一時インスタンスに保持するためにインスタンス作成（新規かClone）

        if (mode == Constants.FOLDER_SETTINGS_FOR_NEW) {
            globalMgr.mTempFolder           = new FolderModel(getResources().getString(R.string.folder_title_new), R.drawable.flower_024_19);
            globalMgr.mCardListMap.put(globalMgr.mTempFolder.getId(), new LinkedList<>());
        }
        else {
            // 選択されたFolderの内容の表示
            globalMgr.mTempFolder           = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).clone();
        }
        ImageView   imageViewIcon           = view.findViewById(R.id.imageViewIcon);
        ImageView   imageViewLearned        = view.findViewById(R.id.imageViewLearned);
        CardView    cardViewFolder          = view.findViewById(R.id.card_view);
        editTextTitle.setText(globalMgr.mTempFolder.getTitleName());
        imageViewIcon.setImageResource(globalMgr.mTempFolder.getImageResId());
        cardViewFolder.setCardBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());
        if (globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).isLearned()) {
            imageViewLearned.setImageResource(R.drawable.heart_on);
        }
        else {
            imageViewLearned.setImageResource(R.drawable.heart_off_grey);
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
    public void onCancel(DialogInterface dialog) {
        /**
         * DialogFragmentのsetCancelableで戻るボタンを無効化したので、実質、ここに処理が来ることはなくなった。
         * setCancelableをやめれば、戻るボタン押下でここに来て、そのあとonDismissがよばれる
         */
        //以下を呼ぶと親のダイアログが消えてしまうので呼ばないようにする
        //super.onCancel(dialog);
        LogUtility.d("onCancel");
        /*
        Fragment target = getTargetFragment();
        if (target != null) {
            target.onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        }
         */

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.flower_024_19)
                .setPositiveButton(
                        R.string.apply,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtility.d("[適用]が選択されました");
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


    /**
     * CardViewで表示するFolderイメージ領域の表示
     * Tabの選択に合わせて適切なlayoutで表示する
     */
    public void drawCardView(int resLayout, int backgroundColor) {
        // レイアウトを変更
        LinearLayout layout = (LinearLayout)globalMgr.mIncludeCardLayoutInFolderSettings;
        layout.removeAllViews();
        getLayoutInflater().inflate(resLayout, layout);

        // 選択されたFolderの詳細内容を表示（タイトル名、色など）
        EditText    editTextTitle                       = view.findViewById(R.id.editTextTitleName);
        ImageView   imageViewIcon                       = view.findViewById(R.id.imageViewIcon);
        globalMgr.mEditTextTitleNameInFolderSettings    = editTextTitle;
        editTextTitle.setText(globalMgr.mTempFolder.getTitleName());
        imageViewIcon.setImageResource(globalMgr.mTempFolder.getImageResId());
        globalMgr.mCardViewInFolderSettings.setCardBackgroundColor(backgroundColor);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Dialog dialog = getDialog();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.9);
        int dialogHeight = (int) (metrics.heightPixels * 0.9);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
    }

}

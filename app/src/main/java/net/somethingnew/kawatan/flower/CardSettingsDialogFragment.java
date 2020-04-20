package net.somethingnew.kawatan.flower;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.Arrays;
import java.util.LinkedList;

public class CardSettingsDialogFragment extends DialogFragment {

    GlobalManager                       globalMgr = GlobalManager.getInstance();
    View                                mView;
    ViewPager                           mViewPager;
    RecyclerView.Adapter                mRecyclerViewAdapter;
    CardSettingsDialogPagerAdapter      mCardSettingsDialogPagerAdapter;
    TabHost                             mTabHost;
    CardModel                           mCardModel;
    FolderModel                         mFolder;
    LinkedList<CardModel>               mCardLinkedList;
    LayoutInflater                      mInflater;
    int                                 mMode;       // 新規追加 or 編集
    int                                 mPosition;      // 選択したCardのCardの一覧内での位置

    // Tab関連
    CardSettingsDialogFragment(RecyclerView.Adapter recyclerViewAdapter, CardModel cardModel, int position) {
        this.mRecyclerViewAdapter   = recyclerViewAdapter;
        this.mCardModel             = cardModel;
        this.mCardLinkedList        = globalMgr.mCardListMap.get(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId());
        this.mPosition              = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 呼び出し側からの引数の受け取り
        mMode       = getArguments().getInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE);

        mFolder     = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex);
        mInflater   = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView       = inflater.inflate(R.layout.dialog_card_settings, container, false);

        globalMgr.mCardSettings.cardViewFront       = mView.findViewById(R.id.card_view_front);
        globalMgr.mCardSettings.cardViewBack        = mView.findViewById(R.id.card_view_back);
        globalMgr.mCardSettings.editTextFront       = mView.findViewById(R.id.editTextFront);
        globalMgr.mCardSettings.editTextBack        = mView.findViewById(R.id.editTextBack);
        globalMgr.mCardSettings.imageViewIcon       = mView.findViewById(R.id.imageViewIcon);
        globalMgr.mCardSettings.imageViewFusen      = mView.findViewById(R.id.imageViewFusen);
        globalMgr.mChangedCardSettings              = false;

        mCardSettingsDialogPagerAdapter = new CardSettingsDialogPagerAdapter(getChildFragmentManager());
        mViewPager             = mView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mCardSettingsDialogPagerAdapter);
        mViewPager.setOffscreenPageLimit(Constants.NUM_OF_ICON_TAB);

        buildTabMenu();
        buildEventListener();

        // ダイアログ表示中のユーザーの設定変更情報を一時インスタンスに保持するためにインスタンス作成（新規かClone）
        if (mMode == Constants.CARD_SETTINGS_FOR_NEW) {
            globalMgr.mTempCard = new CardModel("Front", "うら");
        } else {
            // 選択されたCardのクローン
            globalMgr.mTempCard = mCardModel.clone();
        }

        // おもてとうらのカードへの初期表示
        globalMgr.mCardSettings.cardViewFront.setCardBackgroundColor(mFolder.getFrontBackgroundColor());
        globalMgr.mCardSettings.cardViewBack.setCardBackgroundColor(mFolder.getBackBackgroundColor());
        globalMgr.mCardSettings.editTextFront.setText(mCardModel.getFrontText());
        globalMgr.mCardSettings.editTextFront.setTextColor(mFolder.getFrontTextColor());
        globalMgr.mCardSettings.editTextBack.setText(mCardModel.getBackText());
        globalMgr.mCardSettings.editTextBack.setTextColor(mFolder.getBackTextColor());
        globalMgr.mCardSettings.imageViewIcon.setImageResource(mCardModel.getImageIconResId());
        globalMgr.mCardSettings.imageViewFusen.setImageResource(mFolder.getImageFusenResId());

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

    /**
     * 上部メニュー領域の各アイコン画像やカードの編集箇所のイベントハンドラ
     */
    public void buildEventListener() {
        // 戻る
        mView.findViewById(R.id.imageViewGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
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
        mView.findViewById(R.id.imageViewSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                if (globalMgr.mChangedCardSettings) {
                    // ユーザーによる設定情報の変更をmCardLinkedListに反映する
                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.flower_024_19)
                            .setMessage(R.string.dlg_msg_save)
                            .setPositiveButton(
                                    R.string.save,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            LogUtility.d("[保存]が選択されました");
                                            if (mMode == Constants.CARD_SETTINGS_FOR_NEW) {
                                                // 先頭に追加
                                                mCardLinkedList.add(0, globalMgr.mTempCard);
                                            } else {
                                                // 上書き
                                                mCardLinkedList.set(mPosition, globalMgr.mTempCard);
                                            }
                                            mRecyclerViewAdapter.notifyDataSetChanged();
                                            //Toast.makeText(getActivity().getApplicationContext(), "設定変更を保存しました", Toast.LENGTH_LONG).show();
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
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "設定変更無し", Toast.LENGTH_LONG).show();
                }
            }
        });
        // ゴミ箱（破棄）
        mView.findViewById(R.id.imageViewTrash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                if (mMode == Constants.CARD_SETTINGS_FOR_NEW) {
                    getDialog().dismiss();      // 一覧に戻る
                    return;
                }
                //Toast.makeText(getActivity().getApplicationContext(), "ゴミ箱", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.flower_024_19)
                        .setMessage(R.string.dlg_msg_delete_card)
                        .setPositiveButton(
                                R.string.delete,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[削除]が選択されました");

                                        // LinkedListから削除し一覧に戻る
                                        mCardLinkedList.remove(mPosition);
                                        // TODO CardのLinkedListとそれを管理しているmapからも削除要

                                        getDialog().dismiss();

                                        mRecyclerViewAdapter.notifyItemRemoved(mPosition);
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
        int dialogWidth = (int) (metrics.widthPixels * 0.9);
        int dialogHeight = (int) (metrics.heightPixels * 0.9);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
        LogUtility.d("CardSettingsDialog: W" + dialogWidth + " x H" + dialogHeight);
    }

    /**
     * Tabメニュー部分の構築とリスナー登録
     */
    public void buildTabMenu() {
        mTabHost = mView.findViewById(android.R.id.tabhost);
        mTabHost.setup();


        for(int i = 0; i < mCardSettingsDialogPagerAdapter.getCount(); i++){
            int imageId = getResources().getIdentifier(Constants.ICON_TAB_IMAGE_ID[i],"drawable", getActivity().getPackageName());
            View tabView = new CustomTabView(getActivity(), "dummy", imageId);
            mTabHost.addTab(mTabHost
                    .newTabSpec(Constants.ICON_TAB_ARRAY[i])
                    .setIndicator(tabView)
                    .setContent(android.R.id.tabcontent));
        }


        // Tabのイベントリスナ
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int position = Arrays.asList(Constants.ICON_TAB_ARRAY).indexOf(tabId);
                LogUtility.d("onTabChanged: " + tabId + " position: " + position);
                // Pagerに表示を指示
                mViewPager.setCurrentItem(position);
            }
        });

        // 以下の実装は、Page側のスワイプに合わせてタブもスライドさせるためのもの
        // 非推奨だが他の方法が不明なのでとりあえず。
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                LogUtility.d("onPageSelected  position: " + position);
                super.onPageSelected(position);

                // 以下の呼び出しで、上記のonTabChanged()がinvokeされる
                mTabHost.setCurrentTab(position);
            }
        });

        // 初期表示のタブ設定
        mTabHost.setCurrentTab(0);
    }

}

package net.somethingnew.kawatan.flower;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * FolderActivityのリスト表示用RecyclerViewer（リスト内に表示するのはCard群）
 */
public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.MyViewHolder> {

    GlobalManager                               globalMgr = GlobalManager.getInstance();
    View.OnClickListener                        cardOnClickListener;
    View.OnClickListener                        learnedOnClickListener;
    View.OnClickListener                        fusenOnClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView    textViewSurface;
        TextView    textViewBack;
        ImageView   imageViewLearned;
        ImageView   imageViewFusen;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.textViewSurface        = itemView.findViewById(R.id.textViewSurface);
            this.textViewBack           = itemView.findViewById(R.id.textViewBack);
            this.imageViewLearned       = itemView.findViewById(R.id.imageViewLearned);
            this.imageViewFusen         = itemView.findViewById(R.id.imageViewFusen);
        }
    }

    public FolderRecyclerViewAdapter(View.OnClickListener cardOnClickListener,
                                     View.OnClickListener learnedOnClickListener,
                                     View.OnClickListener fusenOnClickListener) {
        this.cardOnClickListener                = cardOnClickListener;
        this.learnedOnClickListener             = learnedOnClickListener;
        this.fusenOnClickListener               = fusenOnClickListener;
    }

    /**
     * Create new views (invoked by the layout manager)
     * ここで、各Item毎にViewHolderがnewされる
     * （ただし、UI上の表示が見えている範囲でまず作成され、スクロールについれて必要に応じて作成されていく）
     */
    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**
         * 各カードのViewをInfrateし、OnClickのリスナを設定する
         */
        LogUtility.d("onCreateViewHolder ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_card, parent, false);

        // OnClickを検知してイベント処理できるようにリスナを登録（Card全体とImageViewのそれぞれ）
        view.setOnClickListener(cardOnClickListener);
        view.findViewById(R.id.imageViewLearned).setOnClickListener(learnedOnClickListener);
        view.findViewById(R.id.imageViewFusen).setOnClickListener(fusenOnClickListener);

        /**
         * 各カードのView部品をViewHolderに作成しLayoutManagerに返す
         */
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * ここで、各Item内の各Viewにデータを表示するために呼び出される。
     * 表示が必要になった場合だけ、預けておいたViewHolderが引数にセットされて呼び出される。
     * スクロールで一旦消えたらbindは解かれているっぽく、
     * 再度、スクロールで戻ったときに改めてここが呼び出され紐づけが行われる
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int listPosition) {
        LogUtility.d("onBindViewHolder listPosition: " + listPosition);
        TextView    textViewSurface                 = holder.textViewSurface;
        TextView    textViewBack                    = holder.textViewBack;
        ImageView   imageViewLearned                = holder.imageViewLearned;
        ImageView   imageViewFusen                  = holder.imageViewFusen;

        /**
         * このPositionのView(Card)に表示する
         */
        FolderModel folder                          = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex);
        LinkedList<CardModel> cardLinkedList        = globalMgr.mCardListMap.get(folder.getId());
        textViewSurface.setText(cardLinkedList.get(listPosition).getSurfaceText());
        // 裏面の答えはデフォルトでは非表示にする
        //textViewBack.setText(cardArrayList.get(listPosition).getBackText());

        // cardのidをLearndとFusenのImageViewにTag付けしておき、それらのOnClickのイベント処理の際に
        // どのcardのLearnedやFusenがクリックされたのかの判断に使う
        imageViewLearned.setTag(cardLinkedList.get(listPosition).getId());
        imageViewFusen.setTag(cardLinkedList.get(listPosition).getId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //LogUtility.d("getItemCount: " + dataSet.size());
        return globalMgr.mCardListMap.get(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId()).size();
    }
}

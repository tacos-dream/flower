package net.somethingnew.kawatan.flower;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * FolderActivityのリスト表示用RecyclerViewer（リスト内に表示するのはCard群）
 */
public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.MyViewHolder> {

    private GlobalManager           globalMgr = GlobalManager.getInstance();
    private OnItemClickListener     mListener;

    private FolderModel             mFolder;
    private LinkedList<CardModel>   mCardLinkedList;

    /**
     * Interfaceを定義し、Activity側にoverrideでonItemClick()を実装させ、Adapter側からそれをCallback的に呼び出せるようにする
     */
    public interface OnItemClickListener {
        void onFrontClick(int position);
        void onLearnedClick(int position);
        void onFusenClick(int position);
        void onTrashClick(int position);
    }

    // 上記interfaceを実装したActivity側のListenerインスタンスを受け取っておき、このリスナのonItemClickやonIconClickを
    // Adapter側から呼び出すことで、Activity側に変化を通知する
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView    textViewFront;
        ImageView   imageViewIcon;
        ImageView   imageViewLearned;
        ImageView   imageViewFusen;
        ImageView   imageViewTrash;
        CardView    cardViewFront;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            cardViewFront           = itemView.findViewById(R.id.card_view_front);
            imageViewIcon           = itemView.findViewById(R.id.imageViewIcon);
            textViewFront           = itemView.findViewById(R.id.textViewFront);
            imageViewLearned        = itemView.findViewById(R.id.imageViewLearned);
            imageViewFusen          = itemView.findViewById(R.id.imageViewFusen);
            imageViewTrash          = itemView.findViewById(R.id.imageViewTrash);

            textViewFront.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position    = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Activity側のリスナーをCallbackする
                            listener.onFrontClick(position);
                        }
                    }
                }
            });

            imageViewLearned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position    = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Activity側のリスナーをCallbackする
                            listener.onLearnedClick(position);
                        }
                    }
                }
            });

            imageViewFusen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position    = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Activity側のリスナーをCallbackする
                            listener.onFusenClick(position);
                        }
                    }
                }
            });

            imageViewTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position    = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Activity側のリスナーをCallbackする
                            listener.onTrashClick(position);
                        }
                    }
                }
            });
        }
    }

    public FolderRecyclerViewAdapter(LinkedList<CardModel> linkedList) {
        mFolder                     = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex);
        mCardLinkedList             = linkedList;
    }

    public void changeDataSet(LinkedList<CardModel> linkedList) {
        mCardLinkedList             = linkedList;
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
         * 各カードのViewをInfrateし、その中の表紙、付箋、習得済み、ゴミ箱にそれぞれOnClickのリスナを設定する
         */
        LogUtility.d("onCreateViewHolder ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_card, parent, false);

        /**
         * 各カードのView部品をViewHolderに作成しLayoutManagerに返す
         * Listenerも渡してViewHolder内で使えるようにしておく
         */
        MyViewHolder myViewHolder = new MyViewHolder(view, mListener);
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
        TextView    textViewFront                   = holder.textViewFront;
        ImageView   imageViewIcon                   = holder.imageViewIcon;
        ImageView   imageViewLearned                = holder.imageViewLearned;
        ImageView   imageViewFusen                  = holder.imageViewFusen;
        CardView    cardViewFront                   = holder.cardViewFront;

        /**
         * このPositionのView(Card)に表示する
         */
        CardModel card                              = mCardLinkedList.get(listPosition);
        imageViewIcon.setImageResource(card.isIconAutoDisplay() ?
                globalMgr.mIconResourceIdListArray[card.getIconCategory()].get(new Random().nextInt(Constants.NUM_OF_ICONS_IN_CATEGORY[card.getIconCategory()]) + 1) :
                card.getImageIconResId());
        imageViewLearned.setImageResource(card.isLearned() ? R.drawable.heart_on : R.drawable.heart_off_grey);
        imageViewFusen.setImageResource(card.isFusenTag() ? mFolder.getImageFusenResId() : R.drawable.fusen_00);
        textViewFront.setText(card.getFrontText());
        cardViewFront.setCardBackgroundColor(mFolder.getFrontBackgroundColor());
        textViewFront.setBackgroundColor(mFolder.getFrontBackgroundColor());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        LogUtility.d("getItemCount: " + mCardLinkedList.size());
        //return globalMgr.mCardListMap.get(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId()).size();
        return mCardLinkedList.size();
    }
}

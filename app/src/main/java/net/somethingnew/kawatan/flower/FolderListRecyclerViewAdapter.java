package net.somethingnew.kawatan.flower;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * MainActivityのリスト表示用RecyclerViewer（リスト内に表示するのはFolder群）
 */
public class FolderListRecyclerViewAdapter extends RecyclerView.Adapter<FolderListRecyclerViewAdapter.MyViewHolder> {

    private GlobalManager globalMgr = GlobalManager.getInstance();
    private OnItemClickListener mListener;
    private LinkedList<FolderModel> mFolderLinkedList;

    /**
     * Interfaceを定義し、Activity側にoverrideでonItemClick()を実装させ、Adapter側からそれをCallback的に呼び出せるようにする
     */
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onIconClick(int position);

        void onExerciseClick(int position);

        void onShuffleExerciseClick(int position);
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

        TextView textViewTitleName;
        TextView textViewNumOfCards;
        TextView textViewLastUsedDate;
        ImageView imageViewIcon;
        ImageView imageViewExercise;
        ImageView imageViewShuffleExercise;
        CardView cardViewFolder;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.textViewTitleName = itemView.findViewById(R.id.textViewTitleName);
            this.textViewNumOfCards = itemView.findViewById(R.id.textViewNumOfCards);
            this.textViewLastUsedDate = itemView.findViewById(R.id.textViewLastUsedDate);
            this.imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            this.imageViewExercise = itemView.findViewById(R.id.imageViewExercise);
            this.imageViewShuffleExercise = itemView.findViewById(R.id.imageViewShuffleExercise);
            this.cardViewFolder = itemView.findViewById(R.id.card_view);

            // Item全体のOnClickリスナ
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Activity側のリスナーをCallbackする
                        listener.onItemClick(position);
                    }
                }
            });

            // アイコンのOnClickリスナ
            this.imageViewIcon.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Activity側のリスナーをCallbackする
                        listener.onIconClick(position);
                    }
                }
            });

            // ExerciseボタンのOnClickリスナ
            this.imageViewExercise.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Activity側のリスナーをCallbackする
                        listener.onExerciseClick(position);
                    }
                }
            });

            // ExerciseShuffleボタンのOnClickリスナ
            this.imageViewShuffleExercise.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Activity側のリスナーをCallbackする
                        listener.onShuffleExerciseClick(position);
                    }
                }
            });
        }
    }

    public FolderListRecyclerViewAdapter(LinkedList<FolderModel> folderModelLinkedList) {
        mFolderLinkedList = folderModelLinkedList;
    }

    /**
     * Create new views (invoked by the layout manager)
     * ここで、各Item毎にViewHolderがnewされる
     * （ただし、UI上の表示が見えている範囲でまず作成され、スクロールについれて必要に応じて作成されていく）
     */
    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtility.d("onCreateViewHolder ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_folder, parent, false);

        // Listenerも渡してViewHolder内で使えるようにしておく
        MyViewHolder myViewHolder = new MyViewHolder(view, mListener);
        return myViewHolder;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * ここで、各Item内の各Viewにデータを表示するために呼び出される。
     * 表示が必要になった場合だけ呼び出されるので、スクロールで一旦消えたらbindは解かれているっぽく、
     * 再度、スクロールで戻ったときに改めてここが呼び出され紐づけが行われる
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int listPosition) {
        LogUtility.d("onBindViewHolder listPosition: " + listPosition + "(" + mFolderLinkedList.get(listPosition).getTitleName() + ")");
        TextView textViewTitleName = holder.textViewTitleName;
        TextView textViewLastUsedDate = holder.textViewLastUsedDate;
        TextView textViewNumOfCards = holder.textViewNumOfCards;
        ImageView imageViewIcon = holder.imageViewIcon;
        CardView cardViewFolder = holder.cardViewFolder;

        textViewTitleName.setText(mFolderLinkedList.get(listPosition).getTitleName());
        textViewLastUsedDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(mFolderLinkedList.get(listPosition).getLastUsedDate()));
        textViewNumOfCards.setText(mFolderLinkedList.get(listPosition).getNumOfLearnedCards() + " / " + mFolderLinkedList.get(listPosition).getNumOfAllCards());
        cardViewFolder.setCardBackgroundColor(mFolderLinkedList.get(listPosition).getCoverBackgroundColor());
        int category = mFolderLinkedList.get(listPosition).getIconCategory();
        FolderModel folderModel = mFolderLinkedList.get(listPosition);
        imageViewIcon.setImageResource(folderModel.isIconAutoDisplay() ?
                IconManager.getResIdAtRandom(category) :
                folderModel.getImageIconResId()
        );

        // mFolderLinkedListのpositionだとDrag&Dropにより入れ替わることがあるので、id番号をタグ付けしておく
        imageViewIcon.setTag(mFolderLinkedList.get(listPosition).getId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //LogUtility.d("getItemCount: " + dataSet.size());
        return mFolderLinkedList.size();
    }
}

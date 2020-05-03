package net.somethingnew.kawatan.flower;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * MainActivityのリスト表示用RecyclerViewer（リスト内に表示するのはFolder群）
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    private GlobalManager                       globalMgr = GlobalManager.getInstance();
    private OnItemClickListener                 mListener;

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

        TextView    textViewTitleName;
        TextView    textViewNumOfCards;
        TextView    textViewLastUsedDate;
        ImageView   imageViewIcon;
        ImageView   imageViewExercise;
        ImageView   imageViewShuffleExercise;
        CardView    cardViewFolder;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.textViewTitleName          = itemView.findViewById(R.id.textViewTitleName);
            this.textViewNumOfCards         = itemView.findViewById(R.id.textViewNumOfCards);
            this.textViewLastUsedDate       = itemView.findViewById(R.id.textViewLastUsedDate);
            this.imageViewIcon              = itemView.findViewById(R.id.imageViewIcon);
            this.imageViewExercise          = itemView.findViewById(R.id.imageViewExercise);
            this.imageViewShuffleExercise   = itemView.findViewById(R.id.imageViewShuffleExercise);
            this.cardViewFolder             = itemView.findViewById(R.id.card_view);

            // Item全体のOnClickリスナ
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position    = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Activity側のリスナーをCallbackする
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // アイコンのOnClickリスナ
            this.imageViewIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Activity側のリスナーをCallbackする
                            listener.onIconClick(position);
                        }
                    }
                }
            });

            // ExerciseボタンのOnClickリスナ
            this.imageViewExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Activity側のリスナーをCallbackする
                            listener.onExerciseClick(position);
                        }
                    }
                }
            });

            // ExerciseShuffleボタンのOnClickリスナ
            this.imageViewShuffleExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Activity側のリスナーをCallbackする
                            listener.onShuffleExerciseClick(position);
                        }
                    }
                }
            });
        }
    }

    public MainRecyclerViewAdapter() {
        // きれいな実装としては、ここの引数でArrayListを受け取るのがよいが、今回は
        // globalMgr.mFolderLinkedListで参照するので、
        // とりあえず、コンストラクターでは何もなし
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
        LogUtility.d("onBindViewHolder listPosition: " + listPosition + "(" + globalMgr.mFolderLinkedList.get(listPosition).getTitleName() + ")");
        TextView    textViewTitleName           = holder.textViewTitleName;
        TextView    textViewLastUsedDate        = holder.textViewLastUsedDate;
        TextView    textViewNumOfCards          = holder.textViewNumOfCards;
        ImageView   imageViewIcon               = holder.imageViewIcon;
        CardView    cardViewFolder              = holder.cardViewFolder;

        textViewTitleName.setText(globalMgr.mFolderLinkedList.get(listPosition).getTitleName());
        textViewLastUsedDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(globalMgr.mFolderLinkedList.get(listPosition).getLastUsedDate()));
        textViewNumOfCards.setText(globalMgr.mFolderLinkedList.get(listPosition).getNumOfAllCards() + " / " + globalMgr.mFolderLinkedList.get(listPosition).getNumOfAllCards());
        cardViewFolder.setCardBackgroundColor(globalMgr.mFolderLinkedList.get(listPosition).getCoverBackgroundColor());
        int category                = globalMgr.mFolderLinkedList.get(listPosition).getIconCategory();
        FolderModel folderModel     = globalMgr.mFolderLinkedList.get(listPosition);
        imageViewIcon.setImageResource(folderModel.isIconAutoDisplay() ?
                globalMgr.mIconResourceIdListArray[category].get(new Random().nextInt(Constants.NUM_OF_ICONS_IN_CATEGORY[category]) + 1) :
                folderModel.getImageIconResId()
        );

        // mFolderLinkedListのpositionだとDrag&Dropにより入れ替わることがあるので、id番号をタグ付けしておく
        imageViewIcon.setTag(globalMgr.mFolderLinkedList.get(listPosition).getId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //LogUtility.d("getItemCount: " + dataSet.size());
        return globalMgr.mFolderLinkedList.size();
    }
}

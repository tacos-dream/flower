package net.somethingnew.kawatan.flower;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * MainActivityのリスト表示用RecyclerViewer（リスト内に表示するのはFolder群）
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    GlobalManager                               globalMgr = GlobalManager.getInstance();
    View.OnClickListener                        folderOnClickListener;
    View.OnClickListener                        iconOnClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView    textViewTitleName;
        TextView    textViewCreatedDate;
        TextView    textViewNumOfAllCards;
        ImageView   imageViewIcon;
        CardView    cardViewFolder;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTitleName      = itemView.findViewById(R.id.textViewTitleName);
            this.textViewNumOfAllCards  = itemView.findViewById(R.id.textViewNumOfAllCards);
            //this.textViewCreatedDate    = itemView.findViewById(R.id.textViewCreatedDate);
            this.imageViewIcon          = itemView.findViewById(R.id.imageViewIcon);
            this.cardViewFolder         = itemView.findViewById(R.id.card_view);
        }
    }

    public MainRecyclerViewAdapter(View.OnClickListener folderOnClickListener, View.OnClickListener iconOnClickListener) {
        // OnClickListenerの処理UIなのでAdapterよりはMain側で行いたいためMain側のハンドラを受け取っておき
        // Adapter側でOnClickを検知したときに呼び出す
        this.folderOnClickListener      = folderOnClickListener;
        this.iconOnClickListener        = iconOnClickListener;
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

        // OnClickを検知してイベント処理できるようにリスナを登録（Card全体とImageViewのそれぞれ）
        view.setOnClickListener(folderOnClickListener);
        view.findViewById(R.id.imageViewIcon).setOnClickListener(iconOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
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
        //TextView    textViewCreatedDate         = holder.textViewCreatedDate;
        TextView    textViewNumOfAllCards       = holder.textViewNumOfAllCards;
        ImageView   imageViewIcon               = holder.imageViewIcon;
        CardView    cardViewFolder              = holder.cardViewFolder;

        textViewTitleName.setText(globalMgr.mFolderLinkedList.get(listPosition).getTitleName());
        //textViewCreatedDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(globalMgr.mFolderLinkedList.get(listPosition).getCreatedDate()));
        textViewNumOfAllCards.setText(String.valueOf(globalMgr.mFolderLinkedList.get(listPosition).getNumOfAllCards()));
        imageViewIcon.setImageResource(globalMgr.mFolderLinkedList.get(listPosition).getImageResId());
        cardViewFolder.setCardBackgroundColor(globalMgr.mFolderLinkedList.get(listPosition).getCoverBackgroundColor());

        // アイコンのクリック時にはFolderSettingを開くが、どのFolderかを特定するためにFolderのidをTag付けしておく
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

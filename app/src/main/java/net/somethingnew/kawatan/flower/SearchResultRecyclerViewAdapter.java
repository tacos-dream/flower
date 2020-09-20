package net.somethingnew.kawatan.flower;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.model.SearchResultModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * SearchResultActivityのリスト表示用RecyclerViewer（リスト内に表示するのはFolder群）
 */
public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.MyViewHolder> {

    private GlobalManager globalMgr = GlobalManager.getInstance();
    private OnItemClickListener mListener;
    private LinkedList<SearchResultModel> mSearchResultModelLinkedList;
    private String mSearchWord;

    /**
     * Interfaceを定義し、Activity側にoverrideでonItemClick()を実装させ、Adapter側からそれをCallback的に呼び出せるようにする
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
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

        TextView textViewFolderName;
        TextView textViewFrontBackText;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.textViewFolderName = itemView.findViewById(R.id.textViewFolderName);
            this.textViewFrontBackText = itemView.findViewById(R.id.textViewFrontBackText);

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
        }
    }

    public SearchResultRecyclerViewAdapter(LinkedList<SearchResultModel> searchResultModelLinkedList, String searchWord) {
        this.mSearchResultModelLinkedList = searchResultModelLinkedList;
        this.mSearchWord = searchWord;
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
                .inflate(R.layout.recyclerview_item_search, parent, false);

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
        TextView textViewTitleName = holder.textViewFolderName;
        TextView textViewFrontBackText = holder.textViewFrontBackText;
        textViewTitleName.setText(mSearchResultModelLinkedList.get(listPosition).getFolderName());
        String frontText = getHighlightedText(mSearchResultModelLinkedList.get(listPosition).getFrontText(), mSearchWord);
        String backText = getHighlightedText(mSearchResultModelLinkedList.get(listPosition).getBackText(), mSearchWord);
        textViewFrontBackText.setText(Html.fromHtml(frontText + " / " + backText));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mSearchResultModelLinkedList.size();
    }

    /**
     * 一部分のみハイライトしたHTML文字列に変換する
     */
    public String getHighlightedText(String srcString, String highlightText) {
        int startPos = srcString.indexOf(highlightText);
        if (startPos == -1) {
            return srcString;
        }
        StringBuilder highlightedString = new StringBuilder();
        highlightedString.append(srcString.substring(0, startPos));
        highlightedString.append(Constants.HIGHLIGHT_START_ATTRIBUTE);
        highlightedString.append(highlightText);
        highlightedString.append(Constants.HIGHLIGHT_END_ATTRIBUTE);
        highlightedString.append(srcString.substring(startPos + highlightText.length()));
        return highlightedString.toString();
    }
}

//    public static final String HIGHLIGHT_START_ATTRIBUTE = "&amp;lt;font color=\\\"red\\\"&amp;gt;";
//    public static final String HIGHLIGHT_END_ATTRIBUTE = "&amp;lt;/font&amp;gt;";
//
//"そのほか&amp;lt;font color=\"red\"&amp;gt;一人一人&amp;lt;/font&amp;gt;について"
//        文字列の&lt;font color=\"#00BBEE\"&gt;ここだけ&lt;/font&gt;色を変
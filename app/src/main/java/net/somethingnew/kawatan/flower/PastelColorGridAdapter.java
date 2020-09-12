package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PastelColorGridAdapter extends BaseAdapter {

    GlobalManager globalMgr = GlobalManager.getInstance();

    class ViewHolder {
        TextView textView;
    }

    private List<String> pastelColorArray;
    private LayoutInflater inflater;
    private int layoutId;

    PastelColorGridAdapter(Context context, int layoutId, List<String> pastelColorArray) {

        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.pastelColorArray = pastelColorArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            // fragment_folder_front.xmlなどの <GridView .../> に gridview_item_pastel_color.xml を inflate して convertView とする
            convertView = inflater.inflate(layoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.textViewColorHex);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 色を表す文字列（#000000など）をTextとしてセットする
        // その文字列を文字列→数値→Drawable型に変換してTextViewの背景色としてセットする
        // ForeとBackを同色にしてColorHex文字列がUserには見えないようにする
        holder.textView.setText(pastelColorArray.get(position));
        int pastelColor = Color.parseColor(pastelColorArray.get(position));
        int textColor = Color.parseColor("#000000");
        holder.textView.setBackground(new ColorDrawable(pastelColor));
//        holder.textView.setTextColor(pastelColor);
        holder.textView.setTextColor(textColor);

        return convertView;
    }

    @Override
    public int getCount() {
        // List<String> pastelColorArray の全要素数を返す
        return pastelColorArray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

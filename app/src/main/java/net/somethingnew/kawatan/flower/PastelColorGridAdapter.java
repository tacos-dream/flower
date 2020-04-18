package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.somethingnew.kawatan.flower.util.LogUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PastelColorGridAdapter extends BaseAdapter {

    GlobalManager                               globalMgr = GlobalManager.getInstance();

    class ViewHolder {
        TextView    textView;
    }

    private List<String> pastelColorArray;
    private LayoutInflater inflater;
    private int layoutId;

    PastelColorGridAdapter(Context context, int layoutId, List<String> pastelColorArray) {

        super();
        this.inflater                   = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId                   = layoutId;
        this.pastelColorArray           = pastelColorArray;
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
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(pastelColorArray.get(position));

        // ForeとBackを同色にしてColorHexのTextが見えないようにする
        int pastelColor = Color.parseColor(pastelColorArray.get(position));
        holder.textView.setBackground(new ColorDrawable(pastelColor));
        holder.textView.setTextColor(pastelColor);

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

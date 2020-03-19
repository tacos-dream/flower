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

public class TextColorGridAdapter extends BaseAdapter {

    GlobalManager globalMgr         = GlobalManager.getInstance();

    class ViewHolder {
        TextView                    textView;
    }

    private List<String>            textColorArray;
    private LayoutInflater          inflater;
    private int                     layoutId;

    TextColorGridAdapter(Context context, int layoutId, List<String> textColorArray) {

        super();
        this.inflater               = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId               = layoutId;
        this.textColorArray         = textColorArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            // fragment_folder_surface.xmlなどの <GridView .../> に gridview_item_text_color.xml を inflate して convertView とする
            convertView         = inflater.inflate(layoutId, parent, false);
            // ViewHolder を生成
            holder              = new ViewHolder();
            holder.textView     = convertView.findViewById(R.id.textViewColorHex);

            convertView.setTag(holder);
        }
        else {
            holder              = (ViewHolder) convertView.getTag();
        }

        int textColor = Color.parseColor(textColorArray.get(position));
        holder.textView.setTextColor(textColor);
        holder.textView.setText("あa");

        return convertView;
    }

    @Override
    public int getCount() {
        // List<String> textColorArray の全要素数を返す
        return textColorArray.size();
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

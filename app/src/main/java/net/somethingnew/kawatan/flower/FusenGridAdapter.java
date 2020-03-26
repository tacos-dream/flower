package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FusenGridAdapter extends BaseAdapter {

    GlobalManager globalMgr         = GlobalManager.getInstance();

    class ViewHolder {
        ImageView                   imageView;
    }

    private LayoutInflater          inflater;
    private int                     layoutId;

    FusenGridAdapter(Context context, int layoutId) {

        super();
        this.inflater               = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId               = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            // fragment_folder_icon.xmlの <GridView .../> に gridview_item_icon.xml を inflate して convertView とする
            convertView         = inflater.inflate(layoutId, parent, false);
            // ViewHolder を生成
            holder              = new ViewHolder();
            holder.imageView    = convertView.findViewById(R.id.imageViewFusen);

            convertView.setTag(holder);
        }
        else {
            holder              = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(globalMgr.mFusenResourceIdList.get(position));
        // 以下を実行すると、透過PNG画像の背景に色を付けることができるが、
        // border.xmlで定義してあるstate_pressedが効かない（押したときに色を付ける）
        // どうせ効かないので、border.xmlは一旦使わない
        //holder.imageView.setBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());

        // 以下の方法で枠線を引こうとしたが、CastExceptionが出てだめ・・・
        //ShapeDrawable shapeDrawable = (ShapeDrawable) holder.imageView.getBackground();
        //shapeDrawable.getPaint().setColor(Color.BLACK);
        //GradientDrawable drawable = (GradientDrawable) holder.imageView.getDrawable();
        //drawable.setColor(Color.BLACK);

        return convertView;
    }

    @Override
    public int getCount() {
        // List<String> textColorArray の全要素数を返す
        return globalMgr.mFusenResourceIdList.size();
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

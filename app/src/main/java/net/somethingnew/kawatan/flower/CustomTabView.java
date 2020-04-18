package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomTabView extends FrameLayout {

    GlobalManager                       globalMgr = GlobalManager.getInstance();

    //LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    LayoutInflater inflater         = (LayoutInflater)globalMgr.mApplicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    public CustomTabView(Context context, String title, int icon) {
        super(context);
        View childView = inflater.inflate(R.layout.custom_tab_view, null);
        ImageView imageViewTab = childView.findViewById(R.id.imageViewTab);
        imageViewTab.setImageResource(icon);
        addView(childView);
    }
}

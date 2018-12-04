package Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.webphotobooks.R;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by noor on 05/05/15.
 */
public class ListItemViewHolder extends ViewHolder {

    ImageView imgView;
    TextView title;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        //title = (TextView) itemView.findViewById(R.id.textview_title);
        imgView = (ImageView) itemView.findViewById(R.id.imageview_icon);
    }
}

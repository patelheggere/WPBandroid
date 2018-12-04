package Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.webphotobooks.R;

import java.util.ArrayList;

/**
 * Created by RAMA KANT on 12/22/2015.
 */
public class ImageAdapter extends PagerAdapter {

    Activity activity;
    ArrayList<String> imagePath;
    LayoutInflater inflater;
    int[] imgpath;

    public ImageAdapter(Activity activity,int[] imgpath)
    {
        this.activity = activity;
        this.imgpath = imgpath;
    }
    @Override
    public int getCount() {
        return this.imgpath.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imgDisplay;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image,container,false);
        imgDisplay = (ImageView)viewLayout.findViewById(R.id.imgDisplay);

        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //Bitmap bitmap = BitmapFactory.decodeFile(imagePath.get(position), options);
        //imgDisplay.setImageBitmap(bitmap);
        imgDisplay.setImageResource(imgpath[position]);
        ((ViewPager)container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}

package Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.webphotobooks.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by RAMA KANT on 12/22/2015.
 */
public class GViewImageAdapter extends ArrayAdapter<UserDetail> {
    Activity activity;
    ArrayList<UserDetail> filepath;
    int[] imgpath;
    int resource;
    ViewHolder viewHolder;
    Context context;
    public GViewImageAdapter(Context context, int resource, ArrayList<UserDetail> filepath) {
        super(context, resource, filepath);
        this.context = context;
        this.resource = resource;
        this.filepath = filepath;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        view = convertView;
        UserDetail rowItem = getItem(position);

        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, null);
            viewHolder.MyImageView = (ImageView)view.findViewById(R.id.item_image);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }

        UserDetail TItem = (UserDetail)this.filepath.get(position);
        new DownloadImageTask(viewHolder.MyImageView).execute("http://webphotobooks.in/admin/uploads/category_pics/"+TItem.getimage_name());

       /* Bitmap imageBitmap = null;
        try {

            URL imageURL = new URL("http://atpl.cc/webphotobook/public/user_images/"+filepath.get(position).getImage_url());
            imageBitmap = BitmapFactory.decodeStream(imageURL.openStream());
            viewHolder.MyImageView.setImageBitmap(imageBitmap);
        } catch (IOException e) {
            // TODO: handle exception
            Log.e("error", "Downloading Image Failed");
            //viewHolder.imageView.setImageResource(R.drawable.postthumb_loading);
        }
        //viewHolder.MyImageView.setImageResource(imgpath[position]);
        //viewHolder.MyImageView.setImageURI();*/
        return view;
    }
    public static class ViewHolder{
        ImageView MyImageView;
        TextView MyTextView;
    }

    private class DownloadImageTask extends AsyncTask<String,Void, Bitmap>{
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

            }catch (Exception e){
                Log.e("Error",e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bmImage.setImageBitmap(result);
        }
    }
}

/*    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        view = convertView;
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null){
            view = inflater.inflate(resource,null);
        }
        ImageView imageView;
        int imgSize = 0;
        int imgSizeh = 0;
        if(convertView == null){
            imageView = new ImageView(activity);
        }
        else {
            imageView = (ImageView)convertView;
        }
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        imgSize = size.x /2;

       *//* if (position == 0) {
            imgSize = size.x /2;
        }
       *//**//* } else if(position%2 != 0) {
            imgSize = size.x / 4;
        }*//**//*
        else {
            imgSize = size.x / 4;
        }*//*
        *//*if (position == 0) {
            imgSize = size.x /2;
            imgSizeh = size.y / 2;
        }
        else if (position % 2 == 1) {
            imgSize = size.x / 2;
            imgSizeh = size.y / 2;
        }
        else if (position % 2 == 0) {
            imgSize = size.x /2;
            imgSizeh = size.y / 2;
        }*//*
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new GridView.LayoutParams(imgSize,imgSize));
        imageView.setImageResource(imgpath[position]);
        imageView.setOnClickListener(new OnImageClickListner(position));
        return imageView;
    }
    class OnImageClickListner implements View.OnClickListener{
        int position;

        public OnImageClickListner(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, ViewImageActivity.class);
            intent.putExtra("position", position);
            activity.startActivity(intent);
        }
    }*/

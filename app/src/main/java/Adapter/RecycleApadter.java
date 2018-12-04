package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import in.webphotobooks.R;
import weddingpb.ViewImageActivity;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by RAMA KANT on 12/24/2015.
 */
public class RecycleApadter extends ArrayAdapter {
    int resource;
    Context context;
    ArrayList<Userdetail1> ulist;
    ImageView image1;
    String url;
    public RecycleApadter(Context context, int resource,  ArrayList<Userdetail1> ulist) {
        super(context, resource,ulist);
        this.context=context;
        this.resource=resource;
        this.ulist=ulist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // return super.getView(position, convertView, parent);
        LayoutInflater inflater;
        if(convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);

            image1 = (ImageView) convertView.findViewById(R.id.imageview_icon);
            final Userdetail1 Titem = (Userdetail1) this.ulist.get(position);
            //url = " http://webphotobooks.in/admin/uploads/category_pics/" + Titem.getImage_name();
            Log.d("jas2", Titem.getImage_name());
            try {
                url = "http://webphotobooks.in/admin/uploads/category_pics/" + URLEncoder.encode(Titem.getImage_name(), "utf-8");
                url = url.replaceAll("\\+", "%20");
                Log.d("url -> ", url);
                new DownloadImageTask(image1).execute(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //Picasso.with(context).load(url).into(image1);
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, ViewImageActivity.class);
               /* bundle.putString("album_id", Titem.getImage_name());*/
                    //intent.putExtras(bundle);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }
        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            //Bitmap mIcon11 = null;
            Bitmap resizedBitmap = null;
            try{
                java.net.URL url2 = new java.net.URL(urldisplay);
                HttpURLConnection connection = (HttpURLConnection) url2
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                in.close();
                connection.disconnect();
                int width = mIcon11.getWidth();
                int height = mIcon11.getHeight();
                float scaleWidth = ((float)300) / width;
                float scaleHeight = ((float) 300) / height;
                // CREATE A MATRIX FOR THE MANIPULATION
                Matrix matrix = new Matrix();
                // RESIZE THE BIT MAP
                matrix.postScale(scaleWidth, scaleHeight);
                resizedBitmap = Bitmap.createBitmap(mIcon11, 0, 0, width, height,
                        matrix, false);
                return resizedBitmap;
            }catch (Exception e){
                Log.e("Error",e.getMessage());
                e.printStackTrace();
                return null;
            }
            catch (OutOfMemoryError error)
            {
                Log.d("Do in background","null");
                return null;

            }

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bmImage.setImageBitmap(result);
        }
    }
}
package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import in.webphotobooks.R;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by atpl on 3/3/2016.
 */
public class AllbumAdapter extends ArrayAdapter {
        Context context;
        List<Abc> arrayList;
        ImageView image;
        LayoutInflater inflater;
        String url="";
        ImageView imageDisplay;
        int layoutResourceId;
        SharedPreferences sharedPreferences1;

        public AllbumAdapter(Context context, int layoutResourceId, List<Abc> arrayList) {
            super(context, layoutResourceId, arrayList);
            this.context = context;
            this.layoutResourceId = layoutResourceId;
            this.arrayList = arrayList;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            LayoutInflater inflater;
            if(row==null)
            {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            // holder.imageTitle = (TextView) row.findViewById(R.id.text);
            image = (ImageView) row.findViewById(R.id.image);
            TextView textView=(TextView)row.findViewById(R.id.albimtext);

            final Abc item = (Abc) this.arrayList.get(position);
            if(item.getAlbum_name()!=null)
            textView.setText(item.getAlbum_name());
            try {
                url = "http://webphotobooks.in/admin/uploads/category_pics/" + URLEncoder.encode(item.getAlbum_image(),"utf-8");
                 url=   url.replaceAll("\\+","%20");
                   Log.d("url -> ",url);
                new DownloadImageTask(image).execute(url);

          //      Picasso.with(context).load(url).into(image);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
                textView.setText(item.getAlbum_name());

            }
            //row.setTag(image);

    return row;
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

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bmImage.setImageBitmap(result);
        }
    }
}
//To be continued...................................................
           /* try {
                java.net.URL url2 = new java.net.URL(url);
                HttpURLConnection connection = (HttpURLConnection) url2
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                input.close();
                connection.disconnect();
                int width = myBitmap.getWidth();
                int height = myBitmap.getHeight();
                float scaleWidth = ((float)300) / width;
                float scaleHeight = ((float) 300) / height;
                // CREATE A MATRIX FOR THE MANIPULATION
                Matrix matrix = new Matrix();
                // RESIZE THE BIT MAP
                matrix.postScale(scaleWidth, scaleHeight);

                // "RECREATE" THE NEW BITMAP
                Bitmap resizedBitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height,
                        matrix, false);
                   image.setImageBitmap(resizedBitmap);


                Log.d("ImagePath", url);
                      } catch (IOException e) {
                e.printStackTrace();
                return null;
            }*/


// Picasso.with(context).load(url).fit().into(image);

//Bitmap bmp=image.getDrawingCache();
//  bmp.reconfigure(width,height,Bitmap.Config.RGB_565);
//image.setImageBitmap(bmp);
// Calculate inSampleSize

// Decode bitmap with inSampleSize set

//    Picasso.with(context).load(url).into(image);

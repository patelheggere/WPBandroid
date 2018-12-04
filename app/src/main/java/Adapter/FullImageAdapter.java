package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.webphotobooks.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by RAMA KANT on 12/28/2015.
 */
public class FullImageAdapter extends ArrayAdapter {
    private static final String TAG = "Touch";
    // private Matrix matrix = new Matrix();

    private float scale = 1f;
    private ScaleGestureDetector SGD;


    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    int resource;
    Context context;
    ArrayList<UserDetail> ulist;
    ImageView imageDisplay1, Share;
    String url;
   // LinearLayout Imageshair;
    String pathofBmp;

    public FullImageAdapter(Context context, int resource, ArrayList<UserDetail> ulist) {
        super(context, resource, ulist);
        this.context = context;
        this.resource = resource;
        this.ulist = ulist;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LayoutInflater inflater;
        TextView name, phonenumber;
        if(convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);

            name = (TextView) convertView.findViewById(R.id.name);
            phonenumber = (TextView) convertView.findViewById(R.id.phonenum);
            Share = (ImageView) convertView.findViewById(R.id.l_button1);


            imageDisplay1 = (ImageViewTouch) convertView.findViewById(R.id.imageDisplay);
            //SGD = new ScaleGestureDetector(context, new ScaleListener());
            //  imageDisplay1.setScaleType(ImageView.ScaleType.FIT_CENTER);
            final UserDetail item = (UserDetail) this.ulist.get(position);
            //url = " http://webphotobooks.in/admin/uploads/category_pics/" + item.getimage_name();
            Log.d("sed", item.getimage_name());
            try {
                url = "http://webphotobooks.in/admin/uploads/category_pics/" + URLEncoder.encode(item.getimage_name(), "utf-8");
                url = url.replaceAll("\\+", "%20");
                Log.d("url -> ", url);
                new DownloadImageTask(imageDisplay1).execute(url);
                //      Picasso.with(context).load(url).into(image);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //Picasso.with(context).load(url).into(imageDisplay1);
            name.setText(item.getPhotographer());
            Log.d("name", item.getPhotographer());
            phonenumber.setText(item.getPhotographer_contact());
            Log.d("phonenumber", item.getPhotographer_contact());
            //Imageshair = (LinearLayout) convertView.findViewById(R.id.imageshair);
            final View finalConvertView = convertView;
            Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Bitmap image = Bitmap.createBitmap(imageDisplay1.getWidth(), imageDisplay1.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(image);
                imageDisplay1.draw(c);*/
                    ImageViewTouch viewTouch = (ImageViewTouch) finalConvertView.findViewById(R.id.imageDisplay);
                    // Get access to the URI for the bitmap
                    //try {


                    Uri bmpUri = getLocalBitmapUri(viewTouch);
                    if (bmpUri != null) {
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/jpeg");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        // Launch sharing dialog for image
                        context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    } else {
                        Log.d("share","uri null");
                    }

                }


            });
        }
        return convertView;
    }
    public Uri getLocalBitmapUri(ImageViewTouch imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        imageView.setDrawingCacheEnabled(true);
//bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
bmp=imageView.getDrawingCache();
//        if (drawable instanceof BitmapDrawable){
//            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        } else {
//            Log.d("share","drawable not instance of bitmap");
//            return null;
//        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.d("share","exception of uri");
            e.printStackTrace();
        }
        return bmpUri;
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
                float scaleWidth = ((float)1024) / width;
                float scaleHeight = ((float)800) / height;
                // CREATE A MATRIX FOR THE MANIPULATION
                Matrix matrix = new Matrix();
                // RESIZE THE BIT MAP
                matrix.postScale(scaleWidth,scaleHeight);
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

/*
Bitmap image = Bitmap.createBitmap(imageDisplay1.getWidth(),imageDisplay1.getHeight(), Bitmap.Config.ARGB_8888);
Canvas c = new Canvas(image);
imageDisplay1.draw(c);
        Bitmap b = v.getDrawingCache();
        String extr = Environment.getExternalStorageDirectory().toString();
        File myPath = new File(extr,"temp"+".jpg");
        FileOutputStream fos = null;

        try {
        fos = new FileOutputStream(myPath);
        b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        String bmp=  MediaStore.Images.Media.insertImage(context.getContentResolver(), b, "Screen", "screen");
        Uri bmpUri = Uri.parse(bmp);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(context.getPackageCodePath());
        sharingIntent.setType("image/jpg");

        sharingIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(extr,"temp"+".jpg")));

        //     sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");

        //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share Image"));


        } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }

//     Logout_Button.setImageBitmap(image);
//String pathofBmp = MediaStore.Images.Media.(context.getContentResolver(), image, " ", null);
// String shareBody = "Here is the share content body";
*/

package weddingpb;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.FullImageAdapter;
import Adapter.UserDetail;
import cz.msebera.android.httpclient.Header;
import in.webphotobooks.R;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;


public class ViewImageActivity extends AppCompatActivity implements FlipView.OnFlipListener, FlipView.OnOverFlipListener {
    TextView t1;
    String image_url;
    ImageView imageView,Logout_Button,shar,imageback;
    SharedPreferences album_detail,user_detail;
    String User_id ="";
    FlipView fullpager;
    FullImageAdapter fullImageAdapter;
    int image_id;
    public static int currentPage = 0;
    public static int NUM_PAGES = 0;
    ArrayList<UserDetail> ulist = new ArrayList<>();
    String data="";
    String album_id ="";

    Toolbar toolbar;
    //  FlipSettings settings;
    SharedPreferences sharedPreferences1;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        user_detail = getSharedPreferences("USER",Context.MODE_PRIVATE);
        album_detail = getSharedPreferences("ALB",Context.MODE_PRIVATE);
        User_id = user_detail.getString("user_id", "");
        album_id = album_detail.getString("albumid","");
        imageView = (ImageView)findViewById(R.id.ShowimageView);
        Logout_Button = (ImageView)findViewById(R.id.logout_button);
        imageback=(ImageView)findViewById(R.id.go111);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ViewImageActivity.this, WelActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        fullpager = (FlipView)findViewById(R.id.fullpager);
        showfullimage();
    }
    Context context = this;

    public void on_Click(View v){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        TextView txt = (TextView)dialog.findViewById(R.id.textView);
        txt.setText("Are you sure you want to logout ??");
        Button ok = (Button)dialog.findViewById(R.id.btn_ok);
        Button cancel = (Button)dialog.findViewById(R.id.btn_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_detail.edit().clear().commit()) {
                    album_detail.edit().clear().commit();
                    //ViewImageActivity.this.finish();
                    Intent intent = new Intent(ViewImageActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {
        Log.i("pageflip", "Page: " + position);
        if (position > fullpager.getPageCount() - 3 &&fullpager.getPageCount() < 30) {
            // fullImageAdapter.addItems(5);
        }
    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {
        Log.i("overflip", "overFlipDistance = " + overFlipDistance);
    }

    public void showfullimage(){
        String url = "http://webphotobooks.in/webphotobook/index.php/web_controller/fetch1?id="+User_id+"&album_id="+album_id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Fulli",response.toString());
                String jsonData = response.toString();
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        UserDetail userDetail = new UserDetail();
                        JSONObject realObject = jsonArray.getJSONObject(i);
                        String image_name = realObject.getString("image_name");
                        Log.d("IMG", image_name);
                        String photographer = realObject.getString("photographer");
                        Log.d("IMG1.0", photographer);
                        String photographer_contact = realObject.getString("photographer_contact");
                        userDetail.setimage_name(realObject.getString("image_name"));
                        userDetail.setPhotographer(realObject.getString("photographer"));
                        userDetail.setPhotographer_contact(realObject.getString("photographer_contact"));
                        ulist.add(userDetail);
                    }
                    fullImageAdapter = new FullImageAdapter(ViewImageActivity.this, R.layout.layout_fullview, ulist);
                    fullpager.setAdapter(fullImageAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}





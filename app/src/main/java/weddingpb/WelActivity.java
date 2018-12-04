package weddingpb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.RecycleApadter;
import Adapter.Userdetail1;
import cz.msebera.android.httpclient.Header;
import in.webphotobooks.R;

public class WelActivity extends AppCompatActivity {


    private GridView gridView1;
    ArrayList<Userdetail1> ulist = new ArrayList<>();
    SharedPreferences user_detail,album_detail;
    String User_id = "";
    String album_id ="";
    String data = "";
    RecycleApadter recycleApadter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);
        gridView1 = (GridView)findViewById(R.id.gridView2);
        user_detail = getSharedPreferences("USER", Context.MODE_PRIVATE);
        album_detail = getSharedPreferences("ALB",Context.MODE_PRIVATE);
        //sharedPreferences1=context.getSharedPreferences("allbum_id", Context.MODE_PRIVATE);
        Bundle bundle = getIntent().getExtras();
        User_id = bundle.getString("USER_ID");//user_detail.getString("user_id", "");
        album_id = bundle.getString("ALBUM_ID");//album_detail.getString("albumid","");
        albumdisplay();
    }
    public void albumdisplay() {
        String url = "http://webphotobooks.in/webphotobook/index.php/web_controller/fetch1?id="+User_id+"&album_id="+ album_id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url ,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Image1",response.toString());
                try {
                    if(response.getString("status").equalsIgnoreCase("true")){
                        String result = response.toString();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("images");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jreal = jsonArray.getJSONObject(i);
                            Userdetail1 abc1 = new Userdetail1();
                            abc1.setImage_name(jreal.getString("image_name"));
                            abc1.setPhotographer(jreal.getString("photographer"));
                            abc1.setPhotographer_contact(jreal.getString("photographer_contact"));
                            ulist.add(abc1);
                        }
                        recycleApadter = new RecycleApadter(WelActivity.this, R.layout.item_recycler_view, ulist);
                        gridView1.setAdapter(recycleApadter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("IError",responseString);
            }
        });
    }
}
package weddingpb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.Abc;
import Adapter.AllbumAdapter;
import cz.msebera.android.httpclient.Header;
import in.webphotobooks.R;

public class Allbum_acttivity extends AppCompatActivity {
    private static Object NUM_PAGES = 0;
    public static int currentPage = 0;

    GridView gridView;
    AllbumAdapter gridAdapter;
    private List<Abc> arrayList = new ArrayList<>();
    SharedPreferences user_detail,album_detail;
    String User_id = "";
    int allbum_id;
    String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.allbum_activity);
        gridView = (GridView) findViewById(R.id.gridView);
        user_detail = getSharedPreferences("USER", Context.MODE_PRIVATE);
        album_detail = getSharedPreferences("ALB",Context.MODE_PRIVATE);
        User_id = getIntent().getStringExtra("USER_ID");//user_detail.getString("user_id", "");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //your codes here
        displayAlbum();
    }

    Context context = this;

    public void displayAlbum(){
        String url = "http://webphotobooks.in/webphotobook/index.php/web_controller/fetchalbums?user_id="+User_id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Album",response.toString());
                try {
                    if(response.getString("status").equalsIgnoreCase("true")){
                        String result = response.toString();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("images");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject realObject = jsonArray.getJSONObject(i);
                            Abc abc = new Abc();
                            String image_name = realObject.getString("album_image");
                            String album_id = realObject.getString("album_id");
                            String album_name = realObject.getString("album_name");
                            abc.setAlbum_id(album_id);
                            abc.setAlbum_image(image_name);
                            abc.setAlbum_name(album_name);
                            arrayList.add(abc);
                        }
                        gridAdapter = new AllbumAdapter(getApplicationContext(),R.layout.allbumitem,arrayList);
                        gridView.setAdapter(gridAdapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Abc item = (Abc)gridAdapter.getItem(position);
                                SharedPreferences.Editor editor = album_detail.edit();
                                editor.putString("albumid", item.getAlbum_id());
                                //Toast.makeText(Allbum_acttivity.this,item.getAlbum_id(),Toast.LENGTH_LONG).show();
                                if(editor.commit()) {
                                    Intent intent = new Intent(Allbum_acttivity.this, WelActivity.class);
                                    intent.putExtra("USER_ID", User_id);
                                    intent.putExtra("ALBUM_ID", item.getAlbum_id());
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("AERRor",responseString);
            }

        });
    }
}


package weddingpb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import in.webphotobooks.R;

public class LoginActivity extends AppCompatActivity {

   // Toolbar toolbar;
    Button Btn_submit;
    EditText user,pass;
    String username,password;
    String status ="";
    String user_id = "";
    private static final String LOGIN_URL = "http://atpl.cc/webphotobook/index.php/web_controller/login?email=demo@gmail.com&password=12345";
    SharedPreferences user_detail;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Btn_submit = (Button)findViewById(R.id.button_submit);
        user = (EditText)findViewById(R.id.editTextuser);
        pass = (EditText)findViewById(R.id.editTextpass);
        user_detail = getSharedPreferences("USER",Context.MODE_PRIVATE);

        user.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                user.setHint("");
                return false;
            }
        });

        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pass.setHint("");
                return false;
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void btn_onClick(View v)
    {
        /*Intent intent = new Intent(LoginActivity.this,WelActivity.class);
        startActivity(intent);*/
        username = user.getText().toString();
        password = pass.getText().toString();
        if(TextUtils.isEmpty(username)) {
            user.setError("Enter Your Username");
        }
        else if(TextUtils.isEmpty(password)){
            pass.setError("Enter Your Password");
        }
        else {
            if(isNetworkConnected()) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                logindata();
            }else {
                Toast.makeText(getApplicationContext(), "Please connect internet", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void logindata(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://webphotobooks.in/webphotobook/index.php/web_controller/login?email="+username+"&password="+password;
        client.post(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("LoginR",response.toString());
                try {
                    if(response.getString("status").equalsIgnoreCase("true")){
                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = user_detail.edit();
                        editor.putString("user_id",response.getString("user_id"));
                        if(editor.commit()){
                            Intent intent = new Intent(LoginActivity.this,Allbum_acttivity.class);
                            intent.putExtra("USER_ID", response.getString("user_id"));
                            startActivity(intent);
                      finish();
                        }
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"username or password is incorrect",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,"Error in connection",Toast.LENGTH_SHORT).show();
                Log.d("LoginR", responseString);
            }
        });
    }


    /*public class LOGIN_Detail extends AsyncTask<Void,Void,Void>{

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this, R.style.StyledDialog);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @Override
        protected Void doInBackground(Void... arg) {
            try {
                List params = new ArrayList();
                params.add(new BasicNameValuePair("email", username));
                params.add(new BasicNameValuePair("password", password));
                ServiceHandler sh = new ServiceHandler();
                //JSONObject json = sh.makeHttpRequest("http://atpl.cc/webphotobook/index.php/web_controller/login?email="+username+"&password="+password+"", "GET", params);
                JSONObject json = sh.makeHttpRequest("http://webphotobooks.in/webphotobook/index.php/web_controller/login?email="+username+"&password="+password+"","GET", params);

                Log.d("Login attempt", json.toString());
                Log.d("message",json.getString("status"));
                Log.d("jasmessage",json.getString("user_id"));
                status = json.getString("status");
                user_id = json.getString("user_id");

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
            if(user_id != null) {


            }
            if(status.equals("true"))
            {
                user.setText("");
                pass.setText("");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_id", user_id);
                if(editor.commit()) {
                    Intent intent = new Intent(LoginActivity.this, Allbum_acttivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Username and password incorrect",Toast.LENGTH_SHORT).show();
            }
        }
    }
*/
}

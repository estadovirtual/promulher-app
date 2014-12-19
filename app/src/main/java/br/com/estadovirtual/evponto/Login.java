package br.com.estadovirtual.evponto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import br.com.estadovirtual.evponto.classes.JSONParser;
import br.com.estadovirtual.evponto.classes.Singleton;

public class Login extends Activity {

    private static final String TAG = Login.class.getSimpleName();

    private EditText input_login, input_password;
    private Button button_auth;

    private ProgressDialog progressDialog = null;
    private List<NameValuePair> nameValuePairs;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get the itens on activity
        input_login = (EditText) findViewById(R.id.input_login);
        input_password = (EditText) findViewById(R.id.input_password);
        button_auth = (Button) findViewById(R.id.button_auth);

        button_auth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d(TAG, "Button Click Listener");
                // Hide Keyboard after click on login button
                hideSoftKeyboard();

                // Call auth function
                new initAuthentication().execute();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    class initAuthentication extends AsyncTask<String, String, String> {

        Boolean failure = false;

        @Override
        protected void onPreExecute() {

            Log.d(TAG, "OnPreExecute Function Started");

            super.onPreExecute();

            // Show Progress Dialog
            progressDialog = ProgressDialog.show(Login.this, "", "Validando usuário. Aguarde alguns instantes...", false);
        }

        @Override
        protected String doInBackground(String... args) {

            Log.d(TAG, "doInBackground Function Started");

            int success;

            // Get the parameters
            String username = input_login.getText().toString().trim();
            String password = input_password.getText().toString().trim();

            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                Log.d(TAG, "#### Starting Request...");

                JSONObject json = jsonParser.makeHttpRequest(Singleton.getInstance().get_loginURL(), "POST", nameValuePairs);

                Log.d(TAG, "#### Login Attempt: " + json.toString());

                // Check if have any errors on PHP validation
                success = json.getInt("success");

                if(success == 1) {

                    Log.d(TAG, "#### Login Successful! " + json.getJSONObject("post").getString("message"));

                    Singleton.getInstance().set_userInfo(json.getJSONObject("post").getJSONObject("user_info"));

                    return json.getJSONObject("post").getString("message");

                } else {

                    Log.d(TAG, "#### Login Error! " + json.getJSONObject("post").getString("message"));
                    return json.getJSONObject("post").getString("message");
                }

            }catch (Exception e) {
                Log.e(TAG, "### Exception on make HTTP request! ###");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_message) {

            progressDialog.dismiss();

            if (file_message != null){
                Toast.makeText(Login.this, file_message, Toast.LENGTH_LONG).show();
            }

            if(Singleton.getInstance().get_userInfo() != null){

                Log.d(TAG, "Entrou no IF");

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Entrou no Run");
                        Intent intent = new Intent(Login.this, Home.class);
                        finish();
                        startActivity(intent);
                    }
                }, 300);

            }
        }
    }
}

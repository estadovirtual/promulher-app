package br.com.estadovirtual.promulher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.estadovirtual.promulher.classes.JSONParser;
import br.com.estadovirtual.promulher.classes.Singleton;


public class MainBKP extends Activity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private Intent intent;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        new checkDevice().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class checkDevice extends AsyncTask<String, String, String> {

        Boolean failure = false;

        @Override
        protected void onPreExecute() {

            Log.d("Phil", "onPreExecute Function Started 11");

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {

            Log.d("Phil", "doInBackground Function Started");

            Log.d("Phil", "Android ID: " + Singleton.getInstance().getAndroidID(MainBKP.this));
            Log.d("Phil", "Mac Address: " + Singleton.getInstance().getMacAdress(MainBKP.this));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("android_id", Singleton.getInstance().getAndroidID(MainBKP.this)));
            nameValuePairs.add(new BasicNameValuePair("mac_address", Singleton.getInstance().getMacAdress(MainBKP.this)));

            Log.d("Phil", "NameValuePairs: " + nameValuePairs.toString());

            // Check if this device is registered on database
            JSONObject json = jsonParser.makeHttpRequest(Singleton.getInstance().get_registerDeviceURL(), "POST", nameValuePairs);

            String message;

            try {

                Log.d("Phil", "#### Login Attempt: " + json.toString());

                Integer success = json.getInt("result");

                if(success == 1){

                    // Criou/Achou a conta
                    // Seta os dados do usuário e do dispositivo
                    Singleton.getInstance().setDevice(json.getJSONObject("data").getInt("device_id"));
                    Singleton.getInstance().setUserData(json.getJSONObject("data").getJSONObject("user"));

                    message = "Requisição da API completa com sucesso.";

                } else {
                    Log.d("Phil", "#### Else! ");
                    message = "Algum erro aconteceu durante a requisição";
                    // TODO Erro ao conectar e checar se existe conta ou não. No caso essa parte tem que funcionar, porque ele precisa no mínimo de uma conta vazia e um device_id
                }

            } catch (Exception e) {

                message = "Error: " + e.getMessage();
                Log.d("Phil", "#### Result Error! " + e.getMessage());
            }

            return message;
        }

        @Override
        protected void onPostExecute(String file_message) {

            Log.d("Phil", "onPostExecute Function Started : " + file_message);

            //
            preferences = getSharedPreferences(Singleton.getInstance().get_preferencesName(), MODE_PRIVATE);
            preferencesEditor = preferences.edit();

            //
            boolean firstRun = preferences.getBoolean(Singleton.getInstance().get_firstRunFlag(), true);

            if(!firstRun) {

                Log.d("Phil", "Primeira execução do aplicativo");
                // Caso seja a primera vez que o app está sendo executado, atualiza a flag para que na próxima vez que o app seja aberto, já vá direto para a Home
                preferencesEditor.putBoolean(Singleton.getInstance().get_firstRunFlag(), false);
                preferencesEditor.commit();

                intent = new Intent(MainBKP.this, Preferences.class);
                startActivity(intent);
                finish();

            } else {

                Log.d("Phil", "Não é a primeira execução do aplicativo");

                intent = new Intent(MainBKP.this, Home.class);
                startActivity(intent);
                finish();
            }
        }
    }
}

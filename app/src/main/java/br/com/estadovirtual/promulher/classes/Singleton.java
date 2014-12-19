package br.com.estadovirtual.promulher.classes;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import br.com.estadovirtual.promulher.database.DataBase;
import br.com.estadovirtual.promulher.tables.User;

/**
 * Created by Phil on 27/11/2014.
 */
public class Singleton {

    private static Singleton _instance = null;
    private User user;
    private String ApiBase = "http://www.promulher.estadovirtual.com.br/";

    private Singleton() {}

    public static Singleton getInstance() {

        if (_instance == null) {
            _instance = new Singleton();
        }

        return _instance;
    }

    public User getUser(){
        return this.user;
    }

    public String getApiBase() {
        return ApiBase;
    }

    public Boolean checkUser(Activity activity){

        DataBase dataBase = new DataBase(activity.getBaseContext());
        Boolean check = dataBase.checkUser();

        if(check){

            // Achou um usuário, seta no objeto Usuário
            this.user = dataBase.getUser();

        }else{

            // Não achou um usuário, seta no objeto usuário o AndroidID e salva
            // Se tiver conexão, manda um request para a interface de controle à vítima, para registrar o uso do aplicativo (anônimo por enquanto)

            this.user = new User();
            this.user.set_unique_id(this.getAndroidID(activity));
            this.user.setMac_address(this.getMacAdress(activity));

            // Realiza o insert no banco
            dataBase.insertUserInfos(this.user);

            RequestParams requestParams = new RequestParams();
            requestParams.put("device_mac_address", user.getMac_address());
            requestParams.put("device_android_id", user.get_unique_id());

            // Manda um POST para o sistema armazenar mais uma usuária
            EvPost.post("api/user/manager", requestParams, new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.d("Phil", "JSON OBJECT!!");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                    Log.d("Philippe", "Deu Erro: " + statusCode + " | " + responseString);
                }
            });
        }

        return check;
    }

    /**
     * Realoca o objeto usuario na variável singleton
     * @param userP
     */
    public void reloadUser(User userP){
        this.user = userP;
    }

    /**
     * Retorna o ID do android (Número Único)
     * @param activity
     * @return
     */
    public String getAndroidID(Activity activity) {
        return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Retorna o Mac Address do Telefone
     * @param activity
     * @return
     */
    public String getMacAdress(Activity activity) {

        WifiManager manager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();

        return info.getMacAddress();
    }

    /**
     * Retorna o número de telefone atual
     * @param activity
     * @return
     */
    public String getPhoneNumber(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }
}

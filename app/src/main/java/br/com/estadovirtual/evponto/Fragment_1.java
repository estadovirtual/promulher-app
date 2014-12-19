package br.com.estadovirtual.evponto;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.estadovirtual.evponto.classes.JSONParser;
import br.com.estadovirtual.evponto.classes.Singleton;
import br.com.estadovirtual.evponto.classes.Tracker;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Fragment_1 extends Fragment {

    private ProgressDialog progressDialog = null;
    private Double latitude;
    private Double longitude;
    private Tracker tracker;

    JSONParser jsonParser = new JSONParser();

    public Fragment_1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);

        Button check_in = (Button) rootView.findViewById(R.id.button_check_in);
        check_in.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmação de Ponto");
                builder.setMessage("Você tem certeza que deseja registrar a entrada?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // create class object
                        tracker = new Tracker(getActivity());

                        // check if GPS enabled
                        if(tracker.canGetLocation()){

                            latitude = tracker.getLatitude();
                            longitude = tracker.getLongitude();

                            Log.d("Phil", "Latitude: " + latitude);
                            Log.d("Phil", "Longitude: " + longitude);

                            // Call auth function
                            new ManagePoint(1).execute();

                        }else{
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            tracker.showSettingsAlert();
                        }
                    }
                });

                builder.setNegativeButton(R.string.no, null);
                builder.show();
            }
        });

        Button check_out = (Button) rootView.findViewById(R.id.button_check_out);
        check_out.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmação de Ponto");
                builder.setMessage("Você tem certeza que deseja registrar a saída?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // create class object
                        tracker = new Tracker(getActivity());

                        // check if GPS enabled
                        if(tracker.canGetLocation()){

                            double latitude = tracker.getLatitude();
                            double longitude = tracker.getLongitude();

                            Log.d("Phil", "Latitude: " + latitude);
                            Log.d("Phil", "Longitude: " + longitude);

                            // Call auth function
                            new ManagePoint(2).execute();

                        }else{
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            tracker.showSettingsAlert();
                        }
                    }
                });

                builder.setNegativeButton(R.string.no, null);
                builder.show();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private String getAndroidID() {
        return Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private String getMacAdress() {

        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();

        return info.getMacAddress();
    }

    class ManagePoint extends AsyncTask<String, String, String> {

        private Integer type;

        ManagePoint(Integer type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            // Show Progress Dialog
            progressDialog = ProgressDialog.show(getActivity(), "", "Registrando ponto. Aguarde...", false);

            Log.d("Phil", "OnPreExecute Function Started");
        }

        @Override
        protected String doInBackground(String... args) {

            int success;

            Log.d("Phil", "doInBackground Function Started");

            try {

                Log.d("Phil", "Latitude S: " + latitude.toString());
                Log.d("Phil", "Longitude S: " + longitude.toString());

                // TODO timezone from device
                Long tsLong = System.currentTimeMillis()/1000;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("type", type.toString()));
                nameValuePairs.add(new BasicNameValuePair("time", tsLong.toString()));
                nameValuePairs.add(new BasicNameValuePair("latitude", latitude.toString()));
                nameValuePairs.add(new BasicNameValuePair("longitude", longitude.toString()));
                nameValuePairs.add(new BasicNameValuePair("android_id", getAndroidID()));
                nameValuePairs.add(new BasicNameValuePair("mac_address", getMacAdress()));
                nameValuePairs.add(new BasicNameValuePair("user_id", Singleton.getInstance().get_userInfo().getString("user_id")));

                // TODO device ID
                // TODO network ID
                Log.d("Phil", "#### Starting Request...");

                JSONObject json = jsonParser.makeHttpRequest(Singleton.getInstance().get_pointManagerURL(), "POST", nameValuePairs);

                Log.d("Phil", "#### Result: " + json.toString());

                // Check if have any errors on PHP validation
                success = json.getInt("success");

                if(success == 1) {

                    Log.d("Phil", "#### Result Success! " + json.getJSONObject("post").getString("message"));

                    Singleton.getInstance().set_lastPointTime(tsLong);
                    Singleton.getInstance().set_lastPointType(type);

                    String message;

                    if(type == 1){
                        message = "Ponto de entrada registrado com sucesso.";
                    }else{
                        message = "Ponto de saída registrado com sucesso.";
                    }

                    return message;

                } else {

                    Log.d("Phil", "#### Result Error! " + json.getJSONObject("post").getString("message"));
                    return json.getJSONObject("post").getString("message");
                }

            } catch (Exception e) {

                Log.d("Phil", "#### Result Error! " + e.getMessage());
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String file_message) {

            progressDialog.dismiss();

            if (file_message != null){
                Toast.makeText(getActivity(), file_message, Toast.LENGTH_LONG).show();
            }

            Log.d("Phil", "onPostExecute Function Started");
        }
    }


}

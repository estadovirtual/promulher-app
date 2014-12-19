package br.com.estadovirtual.promulher;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import br.com.estadovirtual.promulher.classes.DynamicInstantiation;
import br.com.estadovirtual.promulher.classes.EvPost;
import br.com.estadovirtual.promulher.classes.Singleton;
import br.com.estadovirtual.promulher.classes.Tracker;


public class Home extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    Button action_1, action_2, action_3, action_4, action_5, action_6;
    Tracker tracker;
    Double latitude, longitude;
    private ProgressDialog progressDialog = null;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new MenuFragment_1();
                break;
            case 1:
                fragment = new MenuFragment_2();
                break;
            case 2:
                fragment = new MenuFragment_3();
                break;
            case 3:
                fragment = new MenuFragment_4();
                break;
            case 4:
                fragment = new MenuFragment_5();
                break;
            case 5:
                fragment = new MenuFragment_6();
                break;
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void onSectionAttached(int number) {

        String[] menu_itens = getResources().getStringArray(R.array.nav_drawer_items);
        mTitle = menu_itens[(number - 1)];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void onClickInitCall(View v){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:180"));
        startActivity(callIntent);
    }

    public void onClick(View v){

        String message;

        String[] exploded = String.valueOf(v.getResources().getResourceName(v.getId())).split("/");
        exploded = exploded[1].split("_");

        status = exploded[2];

        switch (v.getId()) {

            case R.id.button_action_6:
            case R.id.button_action_5:
                message = "Você tem certeza que deseja enviar este status? A polícia será acionada e deverá comparecer ao local dentro de instantes!";
                break;
            default:

                message = "Você tem certeza que deseja enviar este status?";
                break;
        }

        clickHandler(message);
    }

    private void clickHandler(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmação");
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // create class object
                tracker = new Tracker(Home.this);

                // check if GPS enabled
                if(tracker.canGetLocation()){

                    latitude = tracker.getLatitude();
                    longitude = tracker.getLongitude();

                    Log.d("Philippe", "Latitude: " + latitude);
                    Log.d("Philippe", "Longitude: " + longitude);

                    // Call auth function
                    progressDialog = ProgressDialog.show(Home.this, "", "Registrando a solicitação. Aguarde...", false);

                    RequestParams requestParams = new RequestParams();
                    requestParams.put("notification_phone_number", Singleton.getInstance().getPhoneNumber(Home.this));
                    requestParams.put("notification_status_code", status);
                    requestParams.put("notification_latitude", latitude);
                    requestParams.put("notification_longitude", longitude);
                    requestParams.put("device_android_id", Singleton.getInstance().getUser().get_unique_id());

                    // Manda um POST para o sistema armazenar mais uma usuária
                    EvPost.post("api/notifications/create", requestParams, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // If the response is JSONObject instead of expected JSONArray
                            Log.d("Philippe", "JSON OBJECT!!");
                            progressDialog.dismiss();

                            Toast toast = Toast.makeText(Home.this, "Notificação enviada com sucesso!", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                            Log.d("Philippe", "Deu Erro: " + statusCode + " | " + responseString);
                            progressDialog.dismiss();

                            Toast toast = Toast.makeText(Home.this, "Ocorreu um erro: " + statusCode, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });

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
}

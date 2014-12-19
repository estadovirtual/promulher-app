package br.com.estadovirtual.evponto;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import br.com.estadovirtual.evponto.classes.EvPageAdapter;
import br.com.estadovirtual.evponto.classes.Singleton;

public class Home extends FragmentActivity implements ActionBar.TabListener {

    private static final String TAG = Login.class.getSimpleName();
    private ActionBar actionBar;
    private ViewPager viewPager;
    private final Context context = this;

    private Button check_in, check_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        check_in = (Button) findViewById(R.id.button_check_in);
        check_out = (Button) findViewById(R.id.button_check_out);

        // init view pager
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new EvPageAdapter(getFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {}

            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        // init action bar
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab_1 = actionBar.newTab();
        tab_1.setText(R.string.title_actionbar_tab_1);
        tab_1.setTabListener(this);

        ActionBar.Tab tab_2 = actionBar.newTab();
        tab_2.setText(R.string.title_actionbar_tab_2);
        tab_2.setTabListener(this);

        actionBar.addTab(tab_1);
        actionBar.addTab(tab_2);

        initCheckManager();
    }

    private void initCheckManager(){

    }

    // Menu Options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    // Menu Options
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //builder.setIcon();
            builder.setTitle(R.string.confirm_logout_title);
            builder.setMessage(R.string.confirm_logout_message);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                // TODO: Make Logout and return to login activity
                }
            });

            builder.setNegativeButton(R.string.no, null);
            builder.show();

            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
}

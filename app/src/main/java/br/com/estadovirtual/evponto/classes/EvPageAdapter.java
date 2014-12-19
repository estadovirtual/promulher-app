package br.com.estadovirtual.evponto.classes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import br.com.estadovirtual.evponto.Fragment_1;
import br.com.estadovirtual.evponto.Fragment_2;

/**
 * Created by Phil on 24/10/2014.
 */
public class EvPageAdapter extends FragmentPagerAdapter {

    public EvPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg) {

        Fragment fragment = null;

        if(arg == 0) {
            fragment = new Fragment_1();
        }else {
            fragment = new Fragment_2();
        }

        return fragment;
    }

    @Override
    public int getCount() {

        return 2;
    }
}

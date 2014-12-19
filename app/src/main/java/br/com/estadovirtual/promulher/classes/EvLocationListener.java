package br.com.estadovirtual.promulher.classes;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Phil on 17/10/2014.
 */
public class EvLocationListener implements LocationListener{

    private String error;
    private Double latitude;
    private Double longitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public void onLocationChanged(Location loc) {

        this.latitude = loc.getLatitude();
        this.longitude = loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(getApplicationContext(), "Network Disabled", Toast.LENGTH_LONG).show();
        this.error = "Provider "+provider+" disabled.";
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Toast.makeText(getApplicationContext(), "Network Enabled", Toast.LENGTH_LONG).show();
        this.error = "Provider "+provider+" enabled.";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //
    }
}

package br.com.estadovirtual.promulher.classes;

import android.util.Log;

import com.loopj.android.http.*;

/**
 * Created by Phil on 27/11/2014.
 */
public class EvPost {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relative){

        String url = Singleton.getInstance().getApiBase() + relative;

        Log.d("Philippe", "URL: " + url);

        return url;
    }
}

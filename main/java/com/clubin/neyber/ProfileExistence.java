package com.clubin.neyber;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Shruti on 8/3/2015.
 */
public class ProfileExistence extends AsyncTask<String, Void, Boolean> {
    public AsyncResponse response = null;

    static String convertStreamToString(InputStream str) {
        Scanner s = new Scanner(str).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        response.processResult(aBoolean);
    }

    @Override
    protected Boolean doInBackground(String... path) {
        URL url;
        InputStream stream;
        String JsonString;
        try {
            url = new URL(path[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            stream = connection.getInputStream();
            JsonString = convertStreamToString(stream);
            stream.close();
            connection.disconnect();
            Log.d("Common", "Json: " + JsonString);
            if(JsonString.equals("{}"))
                return false;
            else
                return true;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

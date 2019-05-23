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
 * Created by GAURAV on 12-07-2015.
 */

public class GetDatabaseProfile extends AsyncTask<URL, Void, Boolean> {

    static String convertStreamToString(InputStream str) {
        Scanner s = new Scanner(str).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected Boolean doInBackground(URL... url) {
        String profile;
        try {
            Log.i("Common", "Trying to add New Ques");
            HttpURLConnection connection = (HttpURLConnection) url[0].openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            InputStream input = connection.getInputStream();
            profile = convertStreamToString(input);
            if(profile.equals(""))
                return false;
            else
                return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);

    }


}


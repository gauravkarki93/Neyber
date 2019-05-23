package com.clubin.neyber;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by GAURAV on 12-07-2015.
 */

public class MyConnector {
    int responseCode;
    String JsonString;
    URL url;
    HttpURLConnection connection;

    static String convertStreamToString(InputStream str) {
        Scanner s = new Scanner(str).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public String fetchJson(String str) {

        InputStream stream;
        try {
            url = new URL(str);
            connection = (HttpURLConnection) url.openConnection();
            Log.i("Common", "1");
            connection.setReadTimeout(10000);
            Log.i("Common", "2");
            connection.setConnectTimeout(15000);
            Log.i("Common", "3");
            connection.setRequestMethod("GET");
            Log.i("Common", "4");
            connection.setDoInput(true);
            Log.i("Common", "5");
            connection.connect();
            Log.i("Common", "6");
            responseCode = connection.getResponseCode();
            Log.i("TAG", "" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                JsonString = convertStreamToString(stream);
                stream.close();
                return JsonString;
            } else {
                Log.i("TAG", "Error" + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }

    public int postJson(String uri, JSONObject object) {
//        DataOutputStream printout;
        try {
            url = new URL(uri);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(150000);
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Host", "192.168.1.7");

            connection.setRequestProperty("Content-type", "text/plain");
            connection.connect();
            Log.i("common", "Connected");
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            //    osw.write(object.toString().getBytes());
            osw.write(object.toString());
            osw.flush();
            osw.close();
            responseCode = connection.getResponseCode();
            Log.i("Code", "" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                input.close();
            } else {
                Log.i("Code:", "Error" + responseCode);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return responseCode;
    }

    public void deleteJson(String uri) {
        try {
            url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.i("TAG", "responseCode");
            Log.i("TAG", "connected deletejson");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

    }

    public int putJson(String uri, JSONObject object) {
        try {
            url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(150000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            Log.i("TAG", "connected putjson");


            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            //    osw.write(object.toString().getBytes());
            osw.write(object.toString());
            osw.flush();
            osw.close();
            responseCode = connection.getResponseCode();
            Log.i("Code", "" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                input.close();
            } else {
                Log.i("Code:", "Error" + responseCode);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return responseCode;
    }
}

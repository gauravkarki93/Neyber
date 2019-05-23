package com.clubin.neyber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ProfileDetails extends Activity {
    ProfileTracker profileTracker;
    JSONObject profileMap;
    private Profile profile;
    private ImageButton profilePic;
    private EditText userName;
    private EditText about;
    private Button next;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_profile);
        FacebookSdk.sdkInitialize(getApplicationContext());
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (AccessToken.getCurrentAccessToken() == null) {
            //SHOW ERROR MESSAGE
        } else {
            //ALLOW ACCESS
        }
        profilePic = (ImageButton) findViewById(R.id.profilePic);
        userName = (EditText) findViewById(R.id.userName);
        about = (EditText) findViewById(R.id.about);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setEnabled(false);
                profileMap = new JSONObject();
                URL url;
                Log.d("TAG", "ButtonClick");
                try {
                    url = new URL(getString(R.string.parentURI) + "/profiles");
                    profileMap.put("id", profile.getId());
                    profileMap.put("name", userName.getText().toString());
                    profileMap.put("about", about.getText().toString());
                    profileMap.put("facebook_link", profile.getLinkUri().toString());
                    Log.d("TAG", "start doInBackground");
                    new PostProfile().execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        updateProfile();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateProfile();
            }
        };
    }
    private void initialiseProfile() {
        Intent intent = new Intent(getApplicationContext(),RegistrationIntentService.class);
        startService(intent);
    }

    private void updateProfile() {
        profile = Profile.getCurrentProfile();
        if (profile == null) {
            //NO ACTIVE PROFILE
        } else {
            //INITIALISE PROFILE LAYOUT
            profilePic.setImageURI(profile.getProfilePictureUri(128, 128));
            userName.setText(profile.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class PostProfile extends AsyncTask<URL, Void, Void> {
        @Override
        protected Void doInBackground(URL... url) {
            try {
//                String newUrl = "http://www.google.com/uds/GnewsSearch?q=Obama&v=1.0";//getString(R.string.parentURI)+"ajaxcall?" +//
//                        //"id=" + profile.getId();
//                AQuery aq = new AQuery(getApplicationContext());
//                Log.d("TAG","doInBackground");
//                aq.ajax(newUrl, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject object, AjaxStatus status) {
//                        Log.d("TAG", status.getCode() + "," + status.getError());
//                        if (object != null) {
//                            Log.d("TAG", "Success, Json: " + object);
//                        } else {
//                            Log.d("TAG", "Unsuccessful");
//                            super.callback(url, object, status);
//                        }
//                    }
//                });


                HttpURLConnection connection= (HttpURLConnection) url[0].openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "text/plain");
                connection.connect();
                OutputStreamWriter output=new OutputStreamWriter(connection.getOutputStream());
                output.write(profileMap.toString());
                output.flush();
                output.close();
                int response=connection.getResponseCode();
                Log.d("TAG","Response code: "+response);
                if(response==HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    input.close();
                }
                Log.d("TAG","Post request done");
            } catch (Exception e) {
                Log.d("TAG","Exception caught");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sharedPreferences.edit().putString("ProfileId",profile.getId()).apply();
            Log.d("TAG", "starting RegistrationIntentService");
            Intent intent = new Intent(getApplicationContext(),RegistrationIntentService.class);
            startService(intent);
        }
    }

}

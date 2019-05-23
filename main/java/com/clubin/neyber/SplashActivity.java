package com.clubin.neyber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.ShareDialog;

public class SplashActivity extends FragmentActivity implements AsyncResponse{

    private static final String PERMISSION = "publish_actions";
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private Profile profile;
    private ProfileExistence profileTask;
    private boolean executed=false;
    public SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileTask=new ProfileExistence();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.new_splash);
        sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        updateUI();
        callbackManager = CallbackManager.Factory.create();                          //callbackManager instance
        LoginManager.getInstance().registerCallback(callbackManager,                 // callback on login button
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        updateUI();
                    }

                    @Override
                    public void onCancel() {
                        showAlert();
                        updateUI();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            showAlert();
                        }
                        updateUI();
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                }
        );


        profileTracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
                // It's possible that we were waiting for Profile to be populated in order to
                // post a status update.
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);
        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private void updateUI() {
        profile = Profile.getCurrentProfile();
        if (profile != null) {
            if(!executed)
                executed=true;
            else return ;
            ActiveProfile.createNewInstance(profile);
            if(sharedPreferences.getString("ProfileId","null").equals(profile.getId())){ //profile.getId(),false)){
                Log.d("TAG", "Profile in shared pref");
                Toast.makeText(getApplicationContext(), "Welcome " + profile.getFirstName()+"!", Toast.LENGTH_SHORT).show();
                Intent register=new Intent(SplashActivity.this, RegistrationIntentService.class);
                startService(register);
                Log.d("TAG", "MainActivity Done");
            }
            else
            {
                Log.d("TAG", "Profile not in shared pref");
                profileTask=new ProfileExistence();
                profileTask.response=this;
                profileTask.execute(getString(R.string.parentURI) + "/profiles/" +profile.getId());//SplashActivity.activeProfile.getId());
            }
        }
    }

    @Override
    public void processResult(boolean profileExists) {
        if(!profileExists) {
            Log.d("TAG", "Profile not in database");
            Intent newProfile = new Intent(SplashActivity.this, ProfileDetails.class);
            startActivity(newProfile);
        }
        else{
            sharedPreferences.edit().putString("ProfileId",profile.getId()).apply();
            Log.d("TAG", "Profile in database");
            Toast.makeText(getApplicationContext(), "Welcome " + profile.getFirstName()+"!", Toast.LENGTH_SHORT);
            Intent intent = new Intent(getApplicationContext(),RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void initialiseProfile() {
        Intent intent = new Intent(getApplicationContext(),RegistrationIntentService.class);
        startService(intent);
    }
}
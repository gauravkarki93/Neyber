package com.clubin.neyber;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MyProfile extends Activity {

    TextView pname;
    TextView pabout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

      /*  Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.profile_image);

        Bitmap bm = NativeStackBlur.process(icon, 12);
        RelativeLayout rel =(RelativeLayout)findViewById(R.id.rel);
        Drawable d = new BitmapDrawable(getResources(), bm);
        rel.setBackground(d);*/

        //   new MyJsonParser().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
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
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(getApplicationContext(), EditProfile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    class MyJsonParser extends AsyncTask<Void, Void, Void> {

        String about;
        String name;

        @Override
        protected Void doInBackground(Void... params) {

            MyConnector connector = new MyConnector();
            String data = connector.fetchJson(getString(R.string.parentURI) + "/profiles/"+ActiveProfile.getInstance().getId());
            pname = (TextView) findViewById(R.id.pname);
            pabout = (TextView) findViewById(R.id.about);
            //DO Parsing here using Jackson API
            if (data != null) {
                try {
                    JSONObject object = new JSONObject(data);
                    name = object.getString("name");
                    about = object.getString("about");
                    Log.i("Common", name + "  " + about);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (name != null && about != null) {
                pabout.setText(about);
                pname.setText(name);
            } else {
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

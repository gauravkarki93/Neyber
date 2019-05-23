package com.clubin.neyber;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;


public class EditGroup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            new MyJsonParser().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyJsonParser extends AsyncTask<Void, Void, Integer> {


        EditText editText;

        @Override
        protected Integer doInBackground(Void... params) {


            editText = (EditText) findViewById(R.id.group_name);
            String name = editText.getText().toString();

            editText = (EditText) findViewById(R.id.category_name);
            String category = editText.getText().toString();

            editText = (EditText) findViewById(R.id.desc);
            String desc = editText.getText().toString();


            try {
                MyConnector connector = new MyConnector();
                JSONObject jsonObject = new JSONObject();

                // set other params here
                jsonObject.put("name", name);
                jsonObject.put("category", category);
                jsonObject.put("group_info", desc);

                Log.i("TAG", "Edit group JSONObject: " + jsonObject);
                int responseCode =  connector.putJson(getString(R.string.parentURI)+"/groups/"+GroupModel.getInstance().getGroupId(), jsonObject);
                return responseCode;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer== HttpURLConnection.HTTP_OK)
            {
                Toast.makeText(getApplicationContext(), "Group Created Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Unable to post data", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

package com.clubin.neyber;

import android.app.ActionBar;
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


public class CreateQuestion extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_question, menu);
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
        //Group_model group_model = new Group_model();

        EditText editText;

        @Override
        protected Integer doInBackground(Void... params) {


            editText = (EditText) findViewById(R.id.question);
            String question = editText.getText().toString();

            if (question.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Empty Question", Toast.LENGTH_SHORT);
                return 0;
            }
            try {
                MyConnector connector = new MyConnector();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("question", question);
                Log.i("TAG", "Calling PostJSON");
                int responsecode = connector.postJson(getString(R.string.parentURI) + "/profiles/"+ActiveProfile.getInstance().getId()+"/groups/"+GroupModel.getInstance().getGroupId()+"/questions", jsonObject);
                return responsecode;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("TAG", "Couldn't Call PostJSON");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == HttpURLConnection.HTTP_OK) {
                Toast.makeText(getApplicationContext(), "Question Created Successfully", Toast.LENGTH_SHORT).show();
            } else if (integer == 1) {
                Log.d("TAG", "Caught Exception");
            } else if (integer != 0) {
                Toast.makeText(getApplicationContext(), "Unable to post data, ResponseCode: "+integer, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}


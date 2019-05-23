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


public class AddAnswer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        final ActionBar actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_answer, menu);
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

    class MyJsonParser extends AsyncTask<Void, Void, Integer>
    {
        //Group_model group_model = new Group_model();

        EditText editText;

        @Override
        protected Integer doInBackground(Void... params) {


            editText = (EditText)findViewById(R.id.answer);
            String answer = editText.getText().toString();

            if(answer.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Empty Question", Toast.LENGTH_SHORT);
                return null;
            }
            try {
                MyConnector connector = new MyConnector();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("answer", answer);
                Log.d("TAG","MyID: "+ActiveProfile.getInstance().getId());
                jsonObject.put("profileId", ActiveProfile.getInstance().getId());
                Log.i("TAG", "Calling PutJSON");
                int responseCode=connector.postJson(getString(R.string.parentURI)+"/groups/"+GroupModel.getInstance().getGroupId()+"/questions/"+QuesModel.getInstance().getQuesId()+"/answers", jsonObject);
                return responseCode;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("TAG","Couldn't Call PutJSON");
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer== HttpURLConnection.HTTP_OK)
            {
                Toast.makeText(getApplicationContext(), "Answer Added Successfully", Toast.LENGTH_SHORT).show();
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

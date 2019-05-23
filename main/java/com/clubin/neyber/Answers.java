package com.clubin.neyber;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Answers extends Activity {
    ListView mylistview;
    List<AnswerModel> ansList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        mylistview = (ListView) findViewById(R.id.list);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new MyJsonParser().execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
    //    new FileHandler().createFile(getApplicationContext(), ansList, "AnswerList");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                return true;
            case R.id.action_add:
                Intent intent = new Intent(getApplicationContext(), AddAnswer.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class MyJsonParser extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            MyConnector connector = new MyConnector();
            String data = connector.fetchJson(getString(R.string.parentURI) + "/groups/" + GroupModel.getInstance().getGroupId() + "/questions/" + QuesModel.getInstance().getQuesId() + "/answers");
            AnswerModel mig;
            Log.i("Answers",data);
            if (data != null) {
                try {
                    ansList = new ArrayList<>();
                    JSONArray jarr = new JSONArray(data);
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        mig = new AnswerModel();
                        mig.setAnswer_id(jobj.getString("answer_id"));
                        mig.setAnswer(jobj.getString("answer"));
                        mig.setQuestion_id(jobj.getString("question_id"));
                        mig.setA_date(jobj.getString("a_date"));
                        mig.setProfileId(jobj.getString("profileId"));
                        mig.setProfileName(jobj.getString("profileName"));
                        mig.setPic(jobj.getString("answererImgLink"));
                        ansList.add(mig);
                    }

                } catch (JSONException e) {
                    Log.i("Common", "Exception: " + e);
                    e.printStackTrace();
                }
            }
            Log.i("Common", "Null AnswerList");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AnswerAdapter adapter;
            if (ansList == null) {
                ansList = (List<AnswerModel>) new FileHandler().readFile(getApplicationContext(),GroupModel.getInstance().getGroupId()+ ":" +QuesModel.getInstance().getQuesId());
            }
            try {
                adapter = new AnswerAdapter(getApplicationContext(), ansList);
                mylistview.setAdapter(adapter);
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


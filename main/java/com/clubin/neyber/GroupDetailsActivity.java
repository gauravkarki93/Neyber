package com.clubin.neyber;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailsActivity extends Activity implements OnItemClickListener {
    ListView mylistview;
    List<MemberInGroup> memList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        mylistview = (ListView) findViewById(R.id.list);
        new MyJsonParser().execute();


        Button leavegrp = (Button) findViewById(R.id.leave);
        leavegrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DelGroup().execute();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        new FileHandler().createFile(getApplicationContext(), memList, "MemberInGroupList");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groupdetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_edit) {
            Intent intent = new Intent(getApplicationContext(), EditGroup.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String member_name = memList.get(position).getName();
        Toast.makeText(getApplicationContext(), "" + member_name,
                Toast.LENGTH_SHORT).show();
    }

    class MyJsonParser extends AsyncTask<Void, Void, Void> {

        //MemberInGroup member_inGroups[];

        MemberInGroup mig;


        @Override
        protected Void doInBackground(Void... params) {

            MyConnector connector = new MyConnector();
            String data = connector.fetchJson(getString(R.string.parentURI) + "/groups/" + GroupModel.getInstance().getGroupId() + "/profiles");

            if (data != null)
            //DO Parsing here using Jackson API
            {
                try {
                    //    memberList = new ObjectMapper().readValue(data,MemberList.class);
                    memList = new ArrayList<>();
                    JSONArray jarr = new JSONArray(data);
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        mig = new MemberInGroup();
                        mig.setName(jobj.getString("name"));
                        mig.setImage(jobj.getString("image"));
                        mig.setAbout(jobj.getString("about"));
                        mig.setId(jobj.getString("id"));
                        memList.add(mig);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            GroupDetailAdapter adapter;
            if (memList == null) {
                memList = (List<MemberInGroup>) new FileHandler().readFile(getApplicationContext(),"MemberInGroupList");
            }
            try {
                adapter = new GroupDetailAdapter(GroupDetailsActivity.this, memList);
                mylistview.setAdapter(adapter);
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class DelGroup extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            MyConnector connector = new MyConnector();
            Log.i("common", "Try to remove group");
            connector.deleteJson(getString(R.string.parentURI) + "/profile/" + ActiveProfile.getInstance().getId() + "/groups/" + GroupModel.getInstance().getGroupId());
            return null;
        }
    }

}

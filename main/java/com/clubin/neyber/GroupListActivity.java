package com.clubin.neyber;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GroupListActivity extends Activity implements OnItemClickListener {

    static List<GroupModel> rowItems;
    GroupListAdapter adapter;
    ListView mylistview;
//    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        sharedPreferences= getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.searchFilter);
        rl.setVisibility(View.GONE);
        mylistview = (ListView) findViewById(R.id.list);
        mylistview.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new MyJsonParser().execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new FileHandler().createFile(getApplicationContext(), rowItems, "MyGroupList");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        final Intent intent;
        intent = new Intent(getApplicationContext(), Group.class);
        String groupDP = "groupDP";
        GroupModel.createInstance(rowItems.get(position));

        //TODO: show group image from activeGroup object (from database)
//        intent.putExtra("groupDP", rowItems.get(position).getGroupImage());
        intent.putExtra("groupDP", R.drawable.ic_group_black_24dp);
        intent.putExtra("groupName", rowItems.get(position).getGroupName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
        final MenuItem filter = menu.findItem(R.id.action_filter);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.filter("");
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.searchFilter);
                rl.setVisibility(View.GONE);
                filter.setVisible(false);
                Log.d("TAG", "filter disappeared");
                return false;
            }
        });
        searchView.setOnSearchClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.searchFilter);
                rl.setVisibility(View.VISIBLE);
                Log.d("TAG", "filter appeared");
                filter.setVisible(true);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText.toLowerCase(Locale.getDefault());
                adapter.filter(text);
                TextView tv = (TextView) findViewById(R.id.text);
                if (adapter.rowItems.isEmpty()) {
                    tv.setText("No Match Found!");
                } else tv.setText("");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add:
                intent = new Intent(getApplicationContext(), CreateGroupActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_filter:
                return true;
            default:
                System.out.println("default");
                return super.onOptionsItemSelected(item);
        }
    }

    class MyJsonParser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            MyConnector connector = new MyConnector();
            String data = connector.fetchJson(getString(R.string.parentURI) + "/profiles/" + ActiveProfile.getInstance().getId() + "/groups");
            GroupModel groupModel;

            if (data != null) {
                try {
                    rowItems = new ArrayList<>();
                    JSONArray jarr = new JSONArray(data);
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        groupModel = new GroupModel();
                        groupModel.setGroupName(jobj.getString("name"));
                        groupModel.setGroupImage(jobj.getString("group_image"));
                        groupModel.setGroupCategory(jobj.getString("category"));
                        groupModel.setGroupInfo(jobj.getString("group_info"));
                        groupModel.setGroupId(jobj.getLong("group_id"));
                        groupModel.setAdminId(jobj.getString("admin_id"));
                        groupModel.setGroupImage(jobj.getString("group_image"));
                        Log.i("Link",jobj.getString("group_image"));
                        rowItems.add(groupModel);
                    }

                    //member_inGroups = new ObjectMapper().readValue(data,MemberInGroup[].class);
                    //response = new ObjectMapper().readValue(data, Response.class);
                    //  rowItem = new ObjectMapper().readValue(data,RowItem.class);
                    Log.d("TAG", "returning " + rowItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("TAG", "returning null");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (rowItems == null) {
                rowItems = (List<GroupModel>) new FileHandler().readFile(getApplicationContext(), "MyGroupList");
            }
            Log.d("TAG", "Adding adapter");
            try {
//                GroupModel gm = new GroupModel();
//                gm.setGroupName("Java");
//                gm.setInstitute("MSIT");
//                gm.setDistance(8);
//                gm.setMemCount(new Random().nextInt(4)+1);
//                rowItems = new ArrayList<>();
//                rowItems.add(gm);
                adapter = new GroupListAdapter(getApplicationContext(), rowItems);
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
                adapter = new GroupListAdapter();
            }
            mylistview.setAdapter(adapter);
            Log.d("TAG", "Added adapter");
        }
    }
}
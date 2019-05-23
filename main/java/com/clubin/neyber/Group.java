/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clubin.neyber;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;


public class Group extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */

    String imgName;
    String imgDecodableString;
    private static int RESULT_LOAD_IMAGE = 1;
    Intent intent;
    ViewPager mViewPager;
    static Context appContext;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_fragment);
        appContext=getApplicationContext();
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        setTitle(getIntent().getStringExtra("groupName"));
        // Set up the action bar.
        final ActionBar actionBar;
        actionBar = getActionBar();
        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setIcon(R.drawable.ic_group_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, GroupListActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.action_newQues:
                intent = new Intent(getApplicationContext(), CreateQuestion.class);
                startActivity(intent);
                //startActivityForResult(intent,);
                return true;
            case R.id.action_attach:
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                return true;
            case R.id.details:
                intent = new Intent(getApplicationContext(), GroupDetailsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                imgDecodableString = cursor.getString(columnIndex);
                File f = new File(imgDecodableString);
                imgName=f.getName();
                cursor.close();
                Log.i("PATH:", filePathColumn[0]);
                Log.i("FILE:", imgDecodableString);

     /*           imageButton = (ImageButton) findViewById(R.id.group_image);
                 Set the Image in ImageView after decoding the String
                imageButton.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                imageView = (ImageView) findViewById(R.id.view);
                imageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));*/
                new MySender().execute();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    class MySender extends AsyncTask<Void,Void,Void>
    {
        URL url;
        @Override
        protected Void doInBackground(Void... params) {

            try
            {
                Resources res = getResources();
                //Drawable drawable = res.getDrawable(R.drawable.angelsofallah);
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
               // Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();



                url=new URL("https://www.googleapis.com/upload/storage/v1/b/neyber/o??uploadType=media&name="+imgName);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(150000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-type", "image/jpeg");
                connection.connect();




                Log.i("common", "Connected");
                OutputStream out =  connection.getOutputStream();
                out.write(bitMapData);

                // osw.write(tester);



                out.flush();
                out.close();
                int responseCode = connection.getResponseCode();
                Log.i("Code", "" + responseCode);
                if(responseCode == HttpURLConnection.HTTP_OK)
                {
                    InputStream input=connection.getInputStream();
                    Scanner sc=new Scanner(input);
                    System.out.print("Uploading Image");
                    while(sc.hasNext())
                        Log.d("Common","Returned: "+sc.nextLine());

                    input.close();
                }
                else
                {
                    Log.i("Code:","Error"+responseCode);
                }

            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());


    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);
        // Associate searchable configuration with the SearchView

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */




    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            if (i == 0) {
                fragment = new ChatSectionFragment();
                Bundle args = new Bundle();
                args.putInt(ChatSectionFragment.ARG_SECTION_NUMBER, i + 1);
                fragment.setArguments(args);
            } else {
                fragment = new QASectionFragment();
                Bundle args = new Bundle();
                args.putInt(QASectionFragment.ARG_SECTION_NUMBER, i + 1);
                fragment.setArguments(args);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "Chat";
            else return "Q/A";
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        GroupModel.destroyInstance();
    }
}

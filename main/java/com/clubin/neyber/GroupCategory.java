package com.clubin.neyber;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class GroupCategory extends ActionBarActivity {

    ImageView i1,i2,i3,i4,i5,i6,i7,i8,i9;
    RelativeLayout r1,r2,r3,r4,r5,r6,r7,r8,r9;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category);

        i1 = (ImageView)findViewById(R.id.edu);
        i2 = (ImageView)findViewById(R.id.fashion);
        i3 = (ImageView)findViewById(R.id.politics);
        i4 = (ImageView)findViewById(R.id.c4);
        i5 = (ImageView)findViewById(R.id.c5);
        i6 = (ImageView)findViewById(R.id.c6);
        i7 = (ImageView)findViewById(R.id.c7);
        i8 = (ImageView)findViewById(R.id.c8);
        i9 = (ImageView)findViewById(R.id.c9);

        r1 = (RelativeLayout)findViewById(R.id.r1);
        r2 = (RelativeLayout)findViewById(R.id.r2);
        r3 = (RelativeLayout)findViewById(R.id.r3);
        r4 = (RelativeLayout)findViewById(R.id.r4);
        r5 = (RelativeLayout)findViewById(R.id.r5);
        r6 = (RelativeLayout)findViewById(R.id.r6);
        r7 = (RelativeLayout)findViewById(R.id.r7);
        r8 = (RelativeLayout)findViewById(R.id.r8);
        r9 = (RelativeLayout)findViewById(R.id.r9);

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                category = "Education";
                r1.setBackgroundColor(Color.parseColor("#00E5EE"));
                r2.setBackgroundColor(Color.WHITE);
                r3.setBackgroundColor(Color.WHITE);
                r4.setBackgroundColor(Color.WHITE);
                r5.setBackgroundColor(Color.WHITE);
                r6.setBackgroundColor(Color.WHITE);
                r7.setBackgroundColor(Color.WHITE);
                r8.setBackgroundColor(Color.WHITE);
                r9.setBackgroundColor(Color.WHITE);


              ///  i1.setColorFilter(Color.WHITE);
            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Fashion";
                r1.setBackgroundColor(Color.WHITE);
                r2.setBackgroundColor(Color.parseColor("#00E5EE"));
                r3.setBackgroundColor(Color.WHITE);
                r4.setBackgroundColor(Color.WHITE);
                r5.setBackgroundColor(Color.WHITE);
                r6.setBackgroundColor(Color.WHITE);
                r7.setBackgroundColor(Color.WHITE);
                r8.setBackgroundColor(Color.WHITE);
                r9.setBackgroundColor(Color.WHITE);
              //  i2.setColorFilter(Color.WHITE);

            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Politics";
                r1.setBackgroundColor(Color.WHITE);
                r2.setBackgroundColor(Color.WHITE);
                r3.setBackgroundColor(Color.parseColor("#00E5EE"));
                r4.setBackgroundColor(Color.WHITE);
                r5.setBackgroundColor(Color.WHITE);
                r6.setBackgroundColor(Color.WHITE);
                r7.setBackgroundColor(Color.WHITE);
                r8.setBackgroundColor(Color.WHITE);
                r9.setBackgroundColor(Color.WHITE);
            //    i3.setColorFilter(Color.WHITE);

            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Food";
                r1.setBackgroundColor(Color.WHITE);
                r2.setBackgroundColor(Color.WHITE);
                r3.setBackgroundColor(Color.WHITE);
                r4.setBackgroundColor(Color.parseColor("#00E5EE"));
                r5.setBackgroundColor(Color.WHITE);
                r6.setBackgroundColor(Color.WHITE);
                r7.setBackgroundColor(Color.WHITE);
                r8.setBackgroundColor(Color.WHITE);
                r9.setBackgroundColor(Color.WHITE);
           //     i4.setColorFilter(Color.WHITE);

            }
        });
        i5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Media";
                r1.setBackgroundColor(Color.WHITE);
                r2.setBackgroundColor(Color.WHITE);
                r3.setBackgroundColor(Color.WHITE);
                r4.setBackgroundColor(Color.WHITE);
                r5.setBackgroundColor(Color.parseColor("#00E5EE"));
                r6.setBackgroundColor(Color.WHITE);
                r7.setBackgroundColor(Color.WHITE);
                r8.setBackgroundColor(Color.WHITE);
                r9.setBackgroundColor(Color.WHITE);
          //      i5.setColorFilter(Color.WHITE);
            }
        });
        i6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Social";
                r1.setBackgroundColor(Color.WHITE);
                r2.setBackgroundColor(Color.WHITE);
                r3.setBackgroundColor(Color.WHITE);
                r4.setBackgroundColor(Color.WHITE);
                r5.setBackgroundColor(Color.WHITE);
                r6.setBackgroundColor(Color.parseColor("#00E5EE"));
                r7.setBackgroundColor(Color.WHITE);
                r8.setBackgroundColor(Color.WHITE);
                r9.setBackgroundColor(Color.WHITE);
            //    i6.setColorFilter(Color.WHITE);

            }
        });
        i7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Just for fun";
                r1.setBackgroundColor(Color.WHITE);
                r2.setBackgroundColor(Color.WHITE);
                r3.setBackgroundColor(Color.WHITE);
                r4.setBackgroundColor(Color.WHITE);
                r5.setBackgroundColor(Color.WHITE);
                r6.setBackgroundColor(Color.WHITE);
                r7.setBackgroundColor(Color.parseColor("#00E5EE"));
                r8.setBackgroundColor(Color.WHITE);
                r9.setBackgroundColor(Color.WHITE);
           //     i7.setColorFilter(Color.WHITE);

            }
        });
        i8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Gaming";
                r1.setBackgroundColor(Color.WHITE);
                r2.setBackgroundColor(Color.WHITE);
                r3.setBackgroundColor(Color.WHITE);
                r4.setBackgroundColor(Color.WHITE);
                r5.setBackgroundColor(Color.WHITE);
                r6.setBackgroundColor(Color.WHITE);
                r7.setBackgroundColor(Color.WHITE);
                r8.setBackgroundColor(Color.parseColor("#00E5EE"));
                r9.setBackgroundColor(Color.WHITE);
             //   i8.setColorFilter(Color.WHITE);

            }
        });
        i9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Other";
                r1.setBackgroundColor(Color.WHITE);
                r2.setBackgroundColor(Color.WHITE);
                r3.setBackgroundColor(Color.WHITE);
                r4.setBackgroundColor(Color.WHITE);
                r5.setBackgroundColor(Color.WHITE);
                r6.setBackgroundColor(Color.WHITE);
                r7.setBackgroundColor(Color.WHITE);
                r8.setBackgroundColor(Color.WHITE);
                r9.setBackgroundColor(Color.parseColor("#00E5EE"));
             //   i9.setColorFilter(Color.WHITE);
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            //Call intent here
            Intent intent = new Intent(getApplicationContext(),CreateGroupActivity.class);
            intent.putExtra("Category",category);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

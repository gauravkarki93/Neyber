package com.clubin.neyber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by GAURAV on 13-07-2015.
 */
public class QuesAdapter extends BaseAdapter {

    Context context;
    List<QuesModel> rowItems;

    QuesAdapter(Context context, List<QuesModel> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView user;
        TextView ques;
        TextView date;
        ImageView qpic;
        TextView Auser;
        TextView Ans;
        TextView Adate;
        ImageView Apic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.qa_card, null);
        holder = new ViewHolder();

        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.noans);
        RelativeLayout rLayout = (RelativeLayout) convertView.findViewById(R.id.apart);
        RelativeLayout reLayout = (RelativeLayout) convertView.findViewById(R.id.yesans);



        QuesModel row_pos = rowItems.get(position);

        if(row_pos.isAnswered()==false)
        {
            rLayout.setVisibility(View.GONE);
            reLayout.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
        }

        holder.user = (TextView) convertView.findViewById(R.id.from);
        holder.date = (TextView) convertView.findViewById(R.id.date);
        holder.ques = (TextView) convertView.findViewById(R.id.ques);
        holder.qpic = (ImageView)convertView.findViewById(R.id.ProfilePic);

        holder.Auser = (TextView) convertView.findViewById(R.id.by);
        holder.Adate = (TextView) convertView.findViewById(R.id.Adate);
        holder.Ans = (TextView) convertView.findViewById(R.id.ans);
        holder.Apic = (ImageView)convertView.findViewById(R.id.Pic);

        TextView viewAll = (TextView) convertView.findViewById(R.id.ViewQuestions);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Answers.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });

        TextView addAns = (TextView) convertView.findViewById(R.id.addAns);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAnswer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });


        holder.user.setText(row_pos.getProfileName());
        Log.i("Common", "ID:" + row_pos.getProfileId());
        holder.date.setText(row_pos.getDate());
        holder.ques.setText(row_pos.getQues());
        Log.i("Common", "ques:" + row_pos.getQues());

        Picasso.with(context)
                .load(row_pos.getProfilePic())
                .error(R.drawable.ic_group_black_24dp)
                .centerCrop()
                .resize(50,50)
                .into(holder.qpic);

        holder.Ans.setText(row_pos.topAns.getAnswer());
        holder.Auser.setText(row_pos.topAns.getProfileName());
        holder.Adate.setText(row_pos.topAns.getA_date());

        Picasso.with(context)
                .load(row_pos.topAns.getPic())
                .error(R.drawable.ic_group_black_24dp)
                .centerCrop()
                .resize(50,50)
                .into(holder.Apic);

        convertView.setTag(holder);
        return convertView;
    }

}

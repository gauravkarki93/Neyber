package com.clubin.neyber;

import android.app.Activity;
import android.content.Context;
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
 * Created by GAURAV on 26-07-2015.
 */
public class AnswerAdapter extends BaseAdapter {

    Context context;
    List<AnswerModel> rowItems;

    AnswerAdapter(Context context, List<AnswerModel> rowItems) {
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
        TextView ans;
        TextView date;
        ImageView Apic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.qa_card, null);
        holder = new ViewHolder();

        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.noans);
        RelativeLayout aLayout = (RelativeLayout) convertView.findViewById(R.id.apart);
        RelativeLayout qLayout = (RelativeLayout) convertView.findViewById(R.id.qpart);
        RelativeLayout reLayout = (RelativeLayout) convertView.findViewById(R.id.yesans);

        relativeLayout.setVisibility(View.GONE);
        aLayout.setVisibility(View.VISIBLE);
        qLayout.setVisibility(View.GONE);
        reLayout.setVisibility(View.GONE);

        holder.user = (TextView) convertView.findViewById(R.id.by);
        holder.date = (TextView) convertView.findViewById(R.id.Adate);
        holder.ans = (TextView) convertView.findViewById(R.id.ans);
        holder.Apic = (ImageView)convertView.findViewById(R.id.Pic);

        AnswerModel row_pos = rowItems.get(position);
        holder.user.setText(row_pos.getProfileId());
        Log.i("TAG", "ID:" + row_pos.getProfileId());
        holder.date.setText(row_pos.getA_date());
        holder.ans.setText(row_pos.getAnswer());
        Log.i("TAG", "answer:" + row_pos.getAnswer());

        Picasso.with(context)
                .load(row_pos.getPic())
                .error(R.drawable.ic_group_black_24dp)
                .centerCrop()
                .resize(50,50)
                .into(holder.Apic);


        convertView.setTag(holder);
        return convertView;
    }
}

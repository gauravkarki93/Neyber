package com.clubin.neyber;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GroupDetailAdapter extends BaseAdapter {

    Context context;
    List<MemberInGroup> rowItems;

    GroupDetailAdapter(Context context, List<MemberInGroup> rowItems) {
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

    /* private view holder class */
    private class ViewHolder {
        //ImageView profile_pic;
        TextView member_name;
        TextView status;
        //TextView contactType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.list_view, null);
        holder = new ViewHolder();

        holder.member_name = (TextView) convertView.findViewById(R.id.group_name);
       // holder.profile_pic = (ImageView) convertView.findViewById(R.id.profile_pic);
        holder.status = (TextView) convertView.findViewById(R.id.memCount);
       // holder.contactType = (TextView) convertView.findViewById(R.id.contact_type);

        MemberInGroup row_pos = rowItems.get(position);
        //holder.profile_pic.setImageResource(row_pos.getImage());
        holder.member_name.setText(row_pos.getName());
        holder.status.setText(row_pos.getAbout());
        //holder.contactType.setText(row_pos.getContactType());

        convertView.setTag(holder);
        return convertView;
    }
}
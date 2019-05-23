package com.clubin.neyber;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GroupListAdapter extends BaseAdapter {

    Context context;
    List<GroupModel> rowItems;
    ArrayList<GroupModel> arrayList;

    GroupListAdapter(Context context, List<GroupModel> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
        arrayList=new ArrayList<>();
        arrayList.addAll(this.rowItems);
    }

    public GroupListAdapter() {
        rowItems = new ArrayList<>();
        arrayList = new ArrayList<>();
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
        ImageView groupPic;
        TextView group_name;
        TextView estd;
        TextView category;
        TextView memCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.list_view, null);
        holder = new ViewHolder();

        holder.group_name = (TextView) convertView.findViewById(R.id.group_name);
        holder.estd = (TextView) convertView.findViewById(R.id.institute);
        holder.category = (TextView) convertView.findViewById(R.id.distance);
        holder.memCount = (TextView) convertView.findViewById(R.id.memCount);
        holder.groupPic = (ImageView) convertView.findViewById(R.id.groupPic);



        GroupModel row_pos = rowItems.get(position);
        holder.group_name.setText(row_pos.getGroupName());
        holder.estd.setText(row_pos.getInstitute());
        holder.category.setText(row_pos.getDistance()+"km");
        holder.memCount.setText(row_pos.getMemCount()+" mem");
        Picasso.with(context)
                .load(row_pos.getGroupImage())
                .error(R.drawable.ic_group_black_24dp)
                .centerCrop()
                .resize(50,50)
                .into(holder.groupPic);

        Log.d("Common", "adding groups");
        convertView.setTag(holder);
        return convertView;
    }

    public void filter(String newText) {
        rowItems.clear();
        if (newText.equals("")) {
            rowItems.addAll(arrayList);
        }
        else
        {
            for (GroupModel ri : arrayList) {
                if (ri.getGroupName().toLowerCase(Locale.getDefault()).contains(newText))
                {
                    rowItems.add(ri);
                }
            }
        }
        notifyDataSetChanged();
    }
}
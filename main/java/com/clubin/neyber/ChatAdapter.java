package com.clubin.neyber;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends ArrayAdapter {

    Context context;
    List<MessageModel> messageList;
    ArrayList<MessageModel> tempList;
    private RelativeLayout messageBox;
    private LinearLayout singlechat;

    public ChatAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        messageList = new ArrayList<>();
        tempList =new ArrayList<>();
    }

    public void setListToAdapter(List<MessageModel> messageList){
        this.messageList = messageList;
        tempList = (ArrayList<MessageModel>) messageList;
    }

    public void add(MessageModel object) {
        super.add(object);
        messageList.add(object);
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messageList.indexOf(getItem(position));
    }

    public void setListToAdapter() {
        messageList = new ArrayList<>();
        tempList =new ArrayList<>();
    }

    /* private view holder class */
    private class ViewHolder {
        //ImageView profile_pic;
        TextView senderName;
        TextView message;
        ImageView statusImage;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (row == null) {
         // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.right, parent, false);
        }

        singlechat = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        messageBox = (RelativeLayout) row.findViewById(R.id.messageBox);
        MessageModel chatMessageObj = (MessageModel) getItem(position);

/*        if(chatMessageObj.isLeft()==true)
        {
            row = inflater.inflate(R.layout.left, parent, false);
        }
        else
        {
            row = inflater.inflate(R.layout.right, parent, false);
        }*/


        ViewHolder holder = new ViewHolder();
        holder.senderName = (TextView) messageBox.findViewById(R.id.senderName);
        holder.message = (TextView) messageBox.findViewById(R.id.message);
        holder.statusImage = (ImageView) messageBox.findViewById(R.id.statusImage);

        holder.senderName.setText(chatMessageObj.getSenderName());
        holder.message.setText(chatMessageObj.getMessage());
        if(chatMessageObj.getStat().equals("Sending to Server")){
            holder.statusImage.setImageResource(R.drawable.ic_query_builder_black_24dp);
        }
        else if(chatMessageObj.getStat().equals("Received by Server")){
            holder.statusImage.setImageResource(R.drawable.ic_done_black_24dp);
        }

      //  messageBox.setBackgroundResource(chatMessageObj.isLeft() ? R.drawable.chatleft : R.drawable.chatright);
        Log.i("ChatO", String.valueOf(chatMessageObj.isLeft()));
       // LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      // singlechat.setGravity(chatMessageObj.isLeft() ? Gravity.START : Gravity.END);
     //   llp.gravity = Gravity.RIGHT;
       // messageBox.setLayoutParams(llp);
       // messageBox.setTag(holder);
        return row;
    }

//    public void filter(String newText) {
//        messageList.clear();
//        if (newText.length() == 0) {
//            messageList.addAll(tempList);
//        }
//        else
//        {
//            for (MessageModel ri : tempList) {
//                if (ri.getGroupName().toLowerCase(Locale.getDefault()).contains(newText))
//                {
//                    messageList.add(ri);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
}
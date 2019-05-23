//package com.clubin.neyber;
//
///**
// * Created by GAURAV on 30-06-2015.
// */
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChatArrayAdapter extends ArrayAdapter {
//
//    public TextView chatText;
//    private List chatMessageList = new ArrayList();
//    private RelativeLayout messageBox;
//
//
//    public void add(MessageModel object) {
//        chatMessageList.add(object);
//        super.add(object);
//    }
//
//    public ChatArrayAdapter(Context context, int textViewResourceId) {
//        super(context, textViewResourceId);
//    }
//
//    public int getCount() {
//        return this.chatMessageList.size();
//    }
//
//    public MessageModel getItem(int index) {
//        return (MessageModel) chatMessageList.get(index);
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        if (row == null) {
//            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
//        }
//        messageBox = (RelativeLayout) row.findViewById(R.id.messageBox);
//        MessageModel chatMessageObj = getItem(position);
//        chatText = (TextView) row.findViewById(R.id.message);
//        chatText.setText(chatMessageObj.getMessage());
//        messageBox.setBackgroundResource(chatMessageObj.isLeft() ? R.drawable.chatbox1 : R.drawable.chatbox);
////        chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.btn_default_normal : R.drawable.btn_default_normal);
//        messageBox.setGravity(chatMessageObj.isLeft() ? Gravity.LEFT : Gravity.RIGHT);
//        return row;
//    }
//
//    public Bitmap decodeToBitmap(byte[] decodedByte) {
//        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//    }
//
//}

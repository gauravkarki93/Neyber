/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clubin.neyber;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String action = data.getString("action");
        Log.d("TAG", "From: " + from);
        Log.d("TAG", "action: " + action);
        // Log.d("TAG", "msg_type: " + data.getString("msg_type"));
        Log.d("TAG","bundle"+data.toString()) ;

       /* else if(action.equals("com.clubin.neyber.BROADCAST"))
        {

            String message=data.getString("message");

            Log.i("TAG", "chat message "+message);
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
            sendNotification(message);

        }*/

        if(data.getString("action").equals("com.clubin.neyber.REGISTER")){
        if(data.getString("msg_type").equals("ack")){
//            if(data.getString("action").equals("com.clubin.neyber.REGISTER")){


                if(data.getString("status").equals("REGISTRATION_SUCCESSFUL")){
                    Log.d("TAG","Starting GroupListActivity");
                    Intent intent=new Intent(getApplicationContext(),GroupListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    //TODO: when registration fails
                }
            }
//            else if(data.getString("action").equals("com.clubin.neyber.BROADCAST")){
//
//                Log.i("TAG", "ack Broadcast running");
//                //when ack for chat message (i.e. your message received by server or not)
//                List<MessageModel> msgList = (List<MessageModel>) new FileHandler().readFile(getApplicationContext(), "ChatHistory");
//                for(int i=msgList.size()-1;i>=0;--i){
//                    if(msgList.get(i).getMessageId().equals(data.getString("messageId","null"))){
//                        msgList.get(i).setStat("Received by Server");
//                        break;
//                    }
//                }
//                new FileHandler().createFile(getApplicationContext(),msgList,"ChatHistory");
//
//            }
        }

        else if(data.getString("action").equals("com.clubin.neyber.QUESTION")){

            Log.i("TAG", "A Question message came");

       /*     if(data.getString("senderId").equals(ActiveProfile.getInstance().getId())) {
                List<QuesModel> quesList = (List<QuesModel>) new FileHandler().readFile(getApplicationContext(), data.getString("groupId") + ":"  + data.getString("questionId"));
                for (int i = quesList.size() - 1; i >= 0; --i) {
                    if (quesList.get(i).getQuesId().equals(data.getString("questionId", "null"))) {
                        //msgList.get(i).setStat("Received by Server");
                        break;
                    }
                }
                new FileHandler().createFile(getApplicationContext(), quesList, data.getString("groupId") + ":" + data.getString("questionId"));
            }
            else
            {
                List<QuesModel> quesList = (List<QuesModel>) new FileHandler().readFile(getApplicationContext(),"Questions"+":"+ data.getString("groupId") );
                QuesModel newQues = new QuesModel();
                newQues.setViews(data.getString("views"));
                newQues.setQuesId(data.getString("questionId"));
                newQues.setGroupId((data.getString("groupId")));
                newQues.setProfileId(data.getString("profileId"));
                newQues.setQues(data.getString("question"));
                newQues.setProfileName(data.getString("profileName"));
                newQues.setProfilePic(data.getString("questionerProfileImgLink"));
              //  newMessage.setDate(new java.sql.Date(new java.util.Date().getTime()));

                if (quesList == null) quesList = new ArrayList<>();
                quesList.add(newQues);
                new FileHandler().createFile(getApplicationContext(), quesList, data.getString("groupId") + ":" + data.getString("questionId"));
                String notify = data.getString("profileName")+":"+"\n"+data.getString("question");*/
                sendNotification("Question Aaaya");

           // }

             }
        else if(data.getString("action").equals("com.clubin.neyber.ANSWER"))
        {
            Log.i("TAG", "Answer came");

           /* if(data.getString("senderId").equals(ActiveProfile.getInstance().getId())) {
                List<AnswerModel> ansList = (List<AnswerModel>) new FileHandler().readFile(getApplicationContext(), data.getString("groupId") + ":"  + data.getString("questionId"));
                for (int i = ansList.size() - 1; i >= 0; --i) {
                    if (ansList.get(i).getAnswer_id().equals(data.getString("answer_Id", "null"))) {
                        //msgList.get(i).setStat("Received by Server");
                        break;
                    }
                }
                new FileHandler().createFile(getApplicationContext(), ansList, data.getString("groupId") + ":" + data.getString("questionId"));
            }
            else
            {
                List<AnswerModel> ansList = (List<AnswerModel>) new FileHandler().readFile(getApplicationContext(),"Questions"+":"+ data.getString("groupId") );
                AnswerModel newAnswer = new AnswerModel();

                newAnswer.setAnswer_id(data.getString("answer_id"));
                newAnswer.setAnswer(data.getString("answer"));
                newAnswer.setQuestion_id(data.getString("question_id"));
                newAnswer.setA_date(data.getString("a_date"));
                newAnswer.setProfileId(data.getString("profileId"));
                newAnswer.setProfileName(data.getString("profileName"));
                newAnswer.setPic(data.getString("answererImgLink"));


                if(ansList==null) ansList = new ArrayList<>();
                ansList.add(newAnswer);
                new FileHandler().createFile(getApplicationContext(), ansList, data.getString("groupId") + ":" + data.getString("questionId"));
                String notify = data.getString("profileName") + ":" + "\n" + data.getString("answer");*/
                sendNotification2("Answer Aaaya");

           // }
            }

        else if(data.getString("msg_type").equals("chat")){

            Log.i("TAG", "A chat message came");

            if(data.getString("senderId").equals(ActiveProfile.getInstance().getId())){
                List<MessageModel> msgList = (List<MessageModel>) new FileHandler().readFile(getApplicationContext(), data.getString("groupId") + ":" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                for(int i=msgList.size()-1;i>=0;--i){
                    if(msgList.get(i).getMessageId().equals(data.getString("messageId","null"))){
                        msgList.get(i).setStat("Received by Server");
                        break;
                    }
                }
                new FileHandler().createFile(getApplicationContext(),msgList,data.getString("groupId") + ":" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            }
            else{
                List<MessageModel> msgList = (List<MessageModel>) new FileHandler().readFile(getApplicationContext(), data.getString("groupId") + ":" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

                MessageModel newMessage = new MessageModel();
                newMessage.setMessageId(data.getString("messageId"));
                newMessage.setSenderId(data.getString("senderId"));
                newMessage.setSenderName(data.getString("senderName"));
                newMessage.setGroupId(Long.parseLong(data.getString("groupId")));
                newMessage.setSenderId(data.getString("senderId"));
                newMessage.setMessage(data.getString("message"));
                newMessage.setLeft(true);
                newMessage.setStat("No Status");
                newMessage.setDate(new java.sql.Date(new Date().getTime()));

                if(msgList==null) msgList = new ArrayList<>();
                msgList.add(newMessage);
                new FileHandler().createFile(getApplicationContext(),msgList,data.getString("groupId") + ":" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                sendNotificationChat(data.getString("message"));
            }
        }
        else if(data.getString("action").equals("com.clubin.neyber.GROUP_JOIN_REQUEST"))
        {
        String notify = data.getString("groupName");
        sendNotification3(notify);

        }
        else if(data.getString("action").equals("com.clubin.neyber.GROUP_JOIN_REQUESTED"))
        {
            String notify = data.getString("groupName");
            sendNotification4(notify);

        }

        else{
            //TODO: ERROR (msg type other than expected)
        }

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Storg message in local database.
         *     - Update UI.
         */





        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */

    private void sendNotificationChat(String message) {


        Log.i("TAG","CHAT NOTIFICATION ENTERED + message :"+message);
        Intent intent = new Intent(this, Group.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mylogo)
                .setContentTitle("New Chat Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }




    private void sendNotification(String message) {
        Intent intent = new Intent(this, Group.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.mylogo);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mylogo)
                .setContentTitle("New Question")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

    }

    private void sendNotification2(String message) {
        Intent intent = new Intent(this, Group.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.mylogo);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mylogo)
                .setContentTitle("New Answer")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(2 /* ID of notification */, notificationBuilder.build());

    }

    private void sendNotification3(String message) {
        Intent intent = new Intent(this, Group.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.mylogo);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mylogo)
                .setContentTitle("Request  accepted")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(3 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification4(String message) {
        Intent intent = new Intent(this, Group.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mylogo)
                .setContentTitle("New Member Request")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(4 /* ID of notification */, notificationBuilder.build());
    }

}

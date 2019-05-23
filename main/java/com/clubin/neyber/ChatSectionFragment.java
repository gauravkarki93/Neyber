package com.clubin.neyber;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link /ChatSectionFragment./OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatSectionFragment#/newInstance} factory method to
 * create an instance of this fragment.
 */

public class ChatSectionFragment extends android.support.v4.app.Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "ChatActivity";
    private ChatAdapter chatAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    private List<MessageModel> chatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chat, container, false);
        buttonSend = (Button) rootView.findViewById(R.id.buttonSend);
        listView = (ListView) rootView.findViewById(R.id.list1);

        chatAdapter = new ChatAdapter(getActivity().getApplicationContext(),R.layout.activity_chat_singlemessage);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatAdapter);

        chatText = (EditText) rootView.findViewById(R.id.chatText);
        setHasOptionsMenu(true);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
        Bundle data = new Bundle();
        data.putString("action", "com.clubin.neyber.STATUS");
        data.putString("status", "ONLINE");
        data.putString("profileId", ActiveProfile.getInstance().getId());
        data.putString("groupId",GroupModel.getInstance().getGroupId()+"");
        String id = nextMessageID();
        Log.i("TAG", "id: " + id);
        try {
            gcm.send("663954925967@gcm.googleapis.com", id, data);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //to scroll the list view to bottom on data change
        chatAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatAdapter.getCount() - 1);
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
        Bundle data = new Bundle();
        data.putString("action", "com.clubin.neyber.STATUS");
        data.putString("status", "OFFLINE");

        data.putString("profileId", ActiveProfile.getInstance().getId());
        data.putString("groupId",GroupModel.getInstance().getGroupId()+"");
        String id = nextMessageID();
        Log.i("TAG", "id: " + id);
        try {
            gcm.send("663954925967@gcm.googleapis.com", id, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        chatList = (List<MessageModel>) new FileHandler().readFile(getActivity().getApplicationContext(),GroupModel.getInstance().getGroupId()+":"+new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        if(chatList != null) {
            chatAdapter.setListToAdapter(chatList);
        }else{
            Log.d("TAG","Empty chat list");
            Toast.makeText(getActivity().getApplicationContext(),"Filename: "+GroupModel.getInstance().getGroupId()+":"+new SimpleDateFormat("dd-MM-yyyy").format(new Date()), Toast.LENGTH_LONG).show();
            chatAdapter.setListToAdapter();
        }
    }

    private boolean sendChatMessage() {
        final String id = nextMessageID();
        String msg;
        if (chatText.getText().equals(""))
            return false;

        final MessageModel newMessage = new MessageModel();
        newMessage.setMessage(chatText.getText().toString());
        newMessage.setMessageId(id);
        newMessage.setSenderId(ActiveProfile.getInstance().getId());
        newMessage.setSenderName(ActiveProfile.getInstance().getName());
        newMessage.setGroupId(GroupModel.getInstance().getGroupId());
        newMessage.setStat("Sending to Server");
        newMessage.setLeft(false);
        newMessage.setDate(new java.sql.Date(new java.util.Date().getTime()));

        msg=chatText.getText().toString();
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
                    Bundle data = new Bundle();
                    data.putString("messageId",id+"");
                    data.putString("senderId", ActiveProfile.getInstance().getId());
                    data.putString("senderName", ActiveProfile.getInstance().getName());
                    data.putString("groupId", GroupModel.getInstance().getGroupId() + "");
                    //Toast.makeText(getActivity().getApplicationContext(), GroupModel.getInstance().getGroupId() + "", Toast.LENGTH_SHORT).show();
                    data.putString("action", "com.clubin.neyber.BROADCAST");
                    data.putString("message", params[0]);
                    data.putString("token", RegistrationIntentService.token);
                    Log.i("upstream", id);
                    msg = "Sent registration";

                    chatList= (List<MessageModel>) new FileHandler().readFile(getActivity().getApplicationContext(), GroupModel.getInstance().getGroupId() + ":" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    if(chatList == null) chatList = new ArrayList<>();
                    chatList.add(newMessage);

                    gcm.send(getString(R.string.gcm_defaultSenderId) + "@gcm.googleapis.com", id, data);
                    new FileHandler().createFile(getActivity().getApplicationContext(), chatList, GroupModel.getInstance().getGroupId() + ":" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getActivity().getApplicationContext(),"Created Filename: "+GroupModel.getInstance().getGroupId()+":"+new SimpleDateFormat("dd-MM-yyyy").format(new Date()), Toast.LENGTH_LONG).show();
            }
        }.execute(msg);

        chatAdapter.add(newMessage);
        chatText.setText("");
        return true;
    }


    public String nextMessageID()
    {
        return "m-" + UUID.randomUUID().toString();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_attach);
        item.setVisible(true);
        item = menu.findItem(R.id.action_newQues);
        item.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
//public class ChatSectionFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ChatSectionFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ChatSectionFragment newInstance(String param1, String param2) {
//        ChatSectionFragment fragment = new ChatSectionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public ChatSectionFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat_section, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }
//
//}

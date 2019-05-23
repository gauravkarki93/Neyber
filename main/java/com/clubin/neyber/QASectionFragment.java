package com.clubin.neyber;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link /QASectionFragment./OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QASectionFragment#/newInstance} factory method to
 * create an instance of this fragment.
 */

public class QASectionFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {

    public static final String ARG_SECTION_NUMBER = "section_number";

    ListView mylistview;
    List<QuesModel> quesList;
//    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Common", "on create");
//        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        QuesModel.destroyInstance();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.d("Common", "fragment on stop");
        //new FileHandler().createFile(getActivity().getApplicationContext(), quesList, "QuestionList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.qa_activity, container, false);
        setHasOptionsMenu(true);
        Log.d("Common", "on create view");

        mylistview = (ListView) rootView.findViewById(R.id.list_card);
        mylistview.setOnItemClickListener(this);
        new MyJsonParser().execute();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_newQues);
        item.setVisible(true);
        item = menu.findItem(R.id.action_attach);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), Answers.class);
        QuesModel.createInstance(quesList.get(position));

        //TODO: check if sharedPreference is required here
//        sharedPreferences.edit().putString("questionId", quesList.get(position).getQuesId()).apply();

        intent.putExtra("QuesID", quesList.get(position).getQuesId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    class MyJsonParser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            MyConnector connector = new MyConnector();
            String data = connector.fetchJson(getString(R.string.parentURI) + "/profiles/" + ActiveProfile.getInstance().getId() + "/groups/" + GroupModel.getInstance().getGroupId() + "/questions");

            QuesModel mig;
            Log.i("Common", "data: " + data);

            //DO Parsing here using Jackson API
            if (data != null) {
                try {
                    //    memberList = new ObjectMapper().readValue(data,MemberList.class);
                    quesList = new ArrayList<>();
                    JSONArray jarr = new JSONArray(data);
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        mig = new QuesModel();
                        mig.setGroupId(jobj.getString("groupId"));
                        mig.setQues(jobj.getString("question"));
                        mig.setQuesId(jobj.getString("questionId"));
                        mig.setDate(jobj.getString("qdate"));
                        mig.setProfileId(jobj.getString("profileId"));
                        mig.setViews(jobj.getString("views"));
                        mig.setProfileName(jobj.getString("profileName"));
                        mig.setProfilePic(jobj.getString("questionerProfileImgLink"));

                        if(jobj.getString("answer").equals("null"))
                        {
                            mig.setIsAnswered(false);
                            mig.topAns.setAnswer(null);
                            mig.topAns.setProfileId(null);
                            mig.topAns.setProfileName(null);
                            mig.topAns.setA_date(null);
                            mig.topAns.setPic(null);
                        }
                        else
                        {
                            mig.setIsAnswered(true);
                            JSONObject ans = new JSONObject(jobj.getString("answer"));
                            mig.topAns.setAnswer(ans.getString("answer"));
                            mig.topAns.setProfileId(ans.getString("profileId"));
                            mig.topAns.setProfileName(ans.getString("profileName"));
                            mig.topAns.setA_date(ans.getString("a_date"));
                            mig.topAns.setPic(ans.getString("answererImgLink"));

                        }
                        quesList.add(mig);
                    }
                    Log.d("Common", "do in background completed: " + quesList.toString());


                } catch (JSONException e) {
                    Log.i("Common", "Exception: " + e);
                    e.printStackTrace();
                }
            }
            Log.i("Common", "Null queslist");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Update data on screen here
            //setting the adapter
            if (quesList == null) {
                quesList = (List<QuesModel>) new FileHandler().readFile(getActivity().getApplicationContext(),"Questions"+":"+GroupModel.getInstance().getGroupId());
            }
            QuesAdapter adapter;
            if(quesList == null){
                Toast.makeText(getActivity().getApplicationContext(), "No Questions Added", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                adapter = new QuesAdapter(getActivity().getApplicationContext(), quesList);
                mylistview.setAdapter(adapter);
            } catch (NullPointerException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Server Error:Unable to get data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
//public class QASectionFragment extends Fragment {
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
//     * @return A new instance of fragment QASectionFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static QASectionFragment newInstance(String param1, String param2) {
//        QASectionFragment fragment = new QASectionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public QASectionFragment() {
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
//        return inflater.inflate(R.layout.fragment_qasection, container, false);
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

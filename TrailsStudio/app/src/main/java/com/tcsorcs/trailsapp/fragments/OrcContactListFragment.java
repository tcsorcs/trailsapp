// http://www.vogella.com/tutorials/AndroidListView/article.html

package com.tcsorcs.trailsapp.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.ActionBar;
import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.helpers.OrcContact;
import com.tcsorcs.trailsapp.managers.DisplayManager;
import com.tcsorcs.trailsapp.helpers.OrcContactArrayAdapter;

import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.ListFragment;
import android.widget.Toast;

import java.util.ArrayList;

public class OrcContactListFragment extends ListFragment {

    /**
     * A simple {@link android.support.v4.app.Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link com.tcsorcs.trailsapp.fragments.OrcContactListFragment.OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link com.tcsorcs.trailsapp.fragments.OrcContactListFragment#newInstance} factory method to
     * create an instance of this fragment.
     */

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<OrcContact> orcContacts=new ArrayList<OrcContact>();

    private OnFragmentInteractionListener mListener;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchievementsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrcContactListFragment newInstance(String param1, String param2) {
        OrcContactListFragment fragment = new OrcContactListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OrcContactListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orc_contact_list, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();


        //clear button listener to unselect all contacts from list
        Button clearButton = (Button)
                getActivity().findViewById(R.id.contacts_clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                for(OrcContact orc:orcContacts){
                    orc.setSelected(false);

                      }
                ((OrcContactArrayAdapter<OrcContact>)getListAdapter()).notifyDataSetChanged();

                Toast.makeText(getActivity(), "Contact selection cleared.", Toast.LENGTH_SHORT).show();

            }
        });


        //send sms to selected contacts with no attachments

        Button messageButton = (Button)
                getActivity().findViewById(R.id.send_message_button);
        messageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {



                  String numbers="";
                for(OrcContact orc:orcContacts){

                    if(orc.isSelected()){
                        numbers+=orc.getphoneNumber()+";";
                    }
                }

                if(numbers.length()>0){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + numbers));    //numbers separated with ;
                   // intent.putExtra("sms_body", longPressLink);
                    getActivity().startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "No contacts selected.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        //send sms to selected contacts with copied link attached
        Button locationButton = (Button)
                getActivity().findViewById(R.id.send_location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //contacts must be sepearated by a ; character
                String numbers="";
                for(OrcContact orc:orcContacts){

                    if(orc.isSelected()){
                        numbers+=orc.getphoneNumber()+";";
                    }
                }

                if(numbers.length()>0){

                    //get location link from Display Manager
                    String link=DisplayManager.getInstance().getLongPressLink();
                    if(link!=null &&link.length()>0){
                        //intent to start chooser for sms apps on device
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + numbers));    //numbers separated with ;
                        intent.putExtra("sms_body",link );
                        getActivity().startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "Location not selected.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(), "No contacts selected.", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        orcContacts.add(new OrcContact("Name","555-555-5555"));
        orcContacts.add(new OrcContact("Name2","555-555-5556"));
        orcContacts.add(new OrcContact("Name3","555-555-5557"));
        orcContacts.add(new OrcContact("Name4","555-555-5558"));
        orcContacts.add(new OrcContact("Name5","555-555-5559"));





        OrcContactArrayAdapter<OrcContact> adapter = new OrcContactArrayAdapter<OrcContact>(getActivity(),
                orcContacts);
        setListAdapter(adapter);
        setHasOptionsMenu(true);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OrcContact orc = (OrcContact) getListAdapter().getItem(position);
        //Toast.makeText(this.getActivity(), position + " selected", Toast.LENGTH_LONG).show();


        //set background to portray selection of a contact
        if(orc.isSelected()){
            v.setBackgroundColor(Color.TRANSPARENT);
            orc.setSelected(false);

        }else{
            v.setBackgroundColor(Color.parseColor("#ffffff"));
            orc.setSelected(true);
        }


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.title_orcslounge));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_fragments, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

}


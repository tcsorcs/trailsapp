// http://www.vogella.com/tutorials/AndroidListView/article.html

package com.tcsorcs.trailsapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.ActionBar;
import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.activites.AchievementDetailsActivity;
import com.tcsorcs.trailsapp.helpers.Achievement;
import com.tcsorcs.trailsapp.managers.AchievementManager;
import com.tcsorcs.trailsapp.managers.DisplayManager;
import com.tcsorcs.trailsapp.helpers.AchievementArrayAdapter;

import android.content.Intent;
import android.widget.ListView;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;

public class AchievementHistoryListFragment extends ListFragment {

    /**
     * A simple {@link android.support.v4.app.Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link com.tcsorcs.trailsapp.fragments.AchievementHistoryListFragment.OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link com.tcsorcs.trailsapp.fragments.AchievementHistoryListFragment#newInstance} factory method to
     * create an instance of this fragment.
     */

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public static AchievementHistoryListFragment newInstance(String param1, String param2) {
        AchievementHistoryListFragment fragment = new AchievementHistoryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AchievementHistoryListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievement_history_list, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Achievement> values=null;

        if(DisplayManager.getInstance().getInDevMode()){
        values= DisplayManager.getInstance().getAchievementList();
        }else{
            //TODO get achievement list from achievement manager
            values= DisplayManager.getInstance().getAchievementList();
            // values= AchievementManager.getInstance().getRecentAchievementList();

        }
        AchievementArrayAdapter<Achievement> adapter = new AchievementArrayAdapter<Achievement>(getActivity(),
                values);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Achievement ach = (Achievement) getListAdapter().getItem(position);

        //pass achievement object to achievement details activity
        Intent i = new Intent(getActivity().getApplicationContext(),AchievementDetailsActivity.class);

        i.putExtra("achievement", ach);
        startActivity(i);

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
        actionBar.setTitle(getString(R.string.title_achievements));

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



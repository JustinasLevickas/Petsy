package android.app.petsy.Fragments;

import android.app.petsy.Classies.SmartFragmentStatePagerAdapter;
import android.app.petsy.Classies.User;
import android.app.petsy.R;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainPageView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainPageView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageView extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private SmartFragmentStatePagerAdapter adapterViewPager;
    // TODO: Rename and change types of parameters
    private String id;

    private OnFragmentInteractionListener mListener;

    public MainPageView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainPageView.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageView newInstance(String id) {
        MainPageView fragment = new MainPageView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager vpPager = (ViewPager) getView().findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager(),id);
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Here's your instance
                if (position == 0) {
                    ListOfConversationsFragment fragment = (ListOfConversationsFragment) adapterViewPager.getRegisteredFragment(position);
                    fragment.updateList();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "LobsterTwo-Bold.otf");
        // TextView textPetName = (TextView) view.findViewById(R.id.textViewPetName);
        // textPetName.setTypeface(tf);
        PagerTabStrip strip = (PagerTabStrip) view.findViewById(R.id.pager_header);
        for (int counter = 0 ; counter<strip.getChildCount(); counter++) {

            if (strip.getChildAt(counter) instanceof TextView) {
                ((TextView)strip.getChildAt(counter)).setTypeface(tf);
                ((TextView)strip.getChildAt(counter)).setTextSize(25);
            }

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void deleteContact(User user, Boolean witchList){
        if (witchList) {
            ContactsFragment fragment = (ContactsFragment) adapterViewPager.getRegisteredFragment(1);
            fragment.deleteContact(user);
        }else{
            ListOfConversationsFragment fragment = (ListOfConversationsFragment) adapterViewPager.getRegisteredFragment(0);
            fragment.deleteConversation(user);
        }
    }
    public void addContact(User user){
        ContactsFragment fragment = (ContactsFragment) adapterViewPager.getRegisteredFragment(1);
        fragment.addContact(user);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
        private static int NUM_ITEMS = 3;
        private String id;

        public MyPagerAdapter(FragmentManager fragmentManager, String id) {
            super(fragmentManager);
            this.id = id;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return ListOfConversationsFragment.newInstance(id);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ContactsFragment.newInstance(id);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return SearchFragment.newInstance(id);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)return "Conversations";
            if(position == 1)return "Contacts";
            if(position == 2)return "Search";
            return ";)";
        }

    }


}

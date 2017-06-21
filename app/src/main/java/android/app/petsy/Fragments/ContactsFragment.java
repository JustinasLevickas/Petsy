package android.app.petsy.Fragments;

import android.app.petsy.Classies.User;
import android.app.petsy.Classies.UserAdapter;
import android.app.petsy.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String id;
    UserAdapter adapter;
    ListView listView;
    View view;

    private OnFragmentInteractionListener mListener;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String id) {
        ContactsFragment fragment = new ContactsFragment();
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
        view = inflater.inflate(R.layout.fragment_contacts, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadContacts();
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadContacts() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.get("http://serveris.hol.es/contacts.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        ArrayList<User> users = new ArrayList<User>();
                        try {
                            JSONArray jsonObjects = new JSONArray(str);
                            for (int i = 0; i < jsonObjects.length(); i++) {
                                users.add(new User(jsonObjects.getJSONObject(i),false));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        updateList(users);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }
                }
        );
    }

    public void updateList(ArrayList<User> data){
        adapter = new UserAdapter(getContext(), data,1);
        listView = (ListView) view.findViewById(R.id.contacs_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                User user = (User)adapter.getItemAtPosition(position);
                if(user.getFoto().equals("true"))
                    downloadUserFoto(user, position);
                else
                    showUserDialogFragment(user, "1", position);
            }
        });
    }

    public void downloadUserFoto(final User user, final int position){
        AsyncHttpClient client = new AsyncHttpClient();
        String[] fileType = {

                "image/png",
                "image/jpeg",
                "image/gif"
        };
        client.get("http://serveris.hol.es/users/"+user.getId()+"/profileFoto.png", new BinaryHttpResponseHandler(fileType) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                user.setProfileFoto(BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length));
                showUserDialogFragment(user, "1", position);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
            }
        });
    }

    public void showUserDialogFragment(User user, String contact, int position){
        FragmentManager fm = getFragmentManager();
        ProfileDialogFragment editNameDialogFragment = ProfileDialogFragment.newInstance(user, contact, id, position, true);
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(ContactsFragment.this, 200);
        editNameDialogFragment.show(fm, "showContact");
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

    public void deleteContact(User user){
        int position = adapter.getPosition(user);
                Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_LONG).show();
         adapter.remove(adapter.getItem(position));
         listView.invalidateViews();
    }

    public void addContact(User user){
//        int position = adapter.getPosition(user);
//        Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_LONG).show();
//        adapter.remove(adapter.getItem(position));
        adapter.add(user);
        listView.invalidateViews();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

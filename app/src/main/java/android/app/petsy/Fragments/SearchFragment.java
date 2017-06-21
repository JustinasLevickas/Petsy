package android.app.petsy.Fragments;


import android.app.petsy.Classies.User;
import android.app.petsy.Classies.UserAdapter;
import android.app.petsy.R;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

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
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String id;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String id) {
        SearchFragment fragment = new SearchFragment();
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
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        ImageButton searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                search(view);
            }
        });
        return view;
    }

    public void search(final View view) {
        EditText raktas = (EditText) view.findViewById(R.id.paieska_field);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Key", raktas.getText().toString());


        client.get("http://serveris.hol.es/search.php", params, new TextHttpResponseHandler() {
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
                        updateList(users,view);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.i("failedas", ":(");
                    }
                }
        );

    }

    public void updateList(ArrayList<User> data, View view){
        UserAdapter adapter = new UserAdapter(getContext(), data,0);
        ListView listView = (ListView) view.findViewById(R.id.paieska_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                User user = (User)adapter.getItemAtPosition(position);
                checkContact(user, id);
            }
        });
    }


    public void downloadUserFoto(final User user, final String contact){
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
                showUserDialogFragment(user, contact);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                Log.i("LOAD FOTO","FALSE");
            }
        });
    }

    public void checkContact(final User user, final String id){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Whose", id);
        params.put("Who", user.getId());

        client.get("http://serveris.hol.es/checkContact.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        if(user.getFoto().equals("true")) {
                            downloadUserFoto(user, str);
                        }else {
                            showUserDialogFragment(user, str);
                        }
                        Log.i("dobze", "su kontaktu");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.i("failedas", "su kontaktu");
                    }
                }
        );
    }

    public void showUserDialogFragment(User user, String contact){
        FragmentManager fm = getFragmentManager();
        ProfileDialogFragment editNameDialogFragment = ProfileDialogFragment.newInstance(user, contact, id, 0, true);
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(SearchFragment.this, 300);
       // editNameDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialogas);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

}

package android.app.petsy.Fragments;

import android.app.petsy.Classies.ConversationAdapter;
import android.app.petsy.Classies.Message;
import android.app.petsy.Classies.User;
import android.app.petsy.R;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ConversationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String LAST_ID = "0";
    ConversationAdapter adapter;
    ListView listView;
    Handler handler = new Handler();
    private View view;

    private User user = null;
    private User chatUser = null;

    public ConversationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ConversationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConversationFragment newInstance(User user, User chatUser) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);
        args.putParcelable(ARG_PARAM2, chatUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_PARAM1);
            chatUser = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_conversation, container, false);

        ImageButton searchButton = (ImageButton) view.findViewById(R.id.sendMessage);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage(view);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMessages();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void loadMessages() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("ID1", user.getId());
        params.put("ID2", chatUser.getId());
        client.get("http://serveris.hol.es/messages.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        ArrayList<Message> message = new ArrayList<Message>();
                        try {
                            JSONArray jsonObjects = new JSONArray(str);
                            for (int i = 0; i < jsonObjects.length()-1; i++) {
                                message.add(new Message(jsonObjects.getJSONObject(i)));
                            }
                            LAST_ID = jsonObjects.getJSONObject(jsonObjects.length()-1).getString("LAST_ID");
                            if(LAST_ID.equals("null")) LAST_ID = "0";
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        Log.i("kiekis",Integer.toString(message.size()));
                        putInConversation(message);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.i("failedas", ":(");
                    }
                }
        );
    }

    public void putInConversation(ArrayList<Message> data){
        adapter = new ConversationAdapter(getContext(), data, user.getId());
        listView = (ListView) view.findViewById(R.id.conversation);
        listView.setAdapter(adapter);
        handler.post(runnableCode);
    }

    public void updateConversation(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("ID1", user.getId());
        params.put("ID2", chatUser.getId());
        params.put("LAST_ID", LAST_ID);
        client.get("http://serveris.hol.es/updateConversation.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        ArrayList<Message> message = new ArrayList<Message>();
                        try {
                            JSONArray jsonObjects = new JSONArray(str);
                            for (int i = 0; i < jsonObjects.length()-1; i++) {
                                message.add(new Message(jsonObjects.getJSONObject(i)));
                            }
                            LAST_ID = jsonObjects.getJSONObject(jsonObjects.length()-1).getString("LAST_ID");
                            if(LAST_ID.equals("null")) LAST_ID = "0";
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        addMessages(message);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.i("failedas", ":(");
                    }
                }
        );
    }

    public void addMessages(ArrayList<Message> message){
        adapter.addAll(message);
        adapter.notifyDataSetChanged();
        listView.invalidateViews();
    }

    public void sendMessage(View view){
        EditText message = (EditText) view.findViewById(R.id.message);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("ID1", user.getId());
        params.put("ID2", chatUser.getId());
        params.put("Message", message.getText().toString());
        message.setText(null);
        client.get("http://serveris.hol.es/sendMessage.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.i("failedas", ":(");
                    }
                }
        );
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            updateConversation();
            handler.postDelayed(runnableCode, 2000);
        }
    };

}

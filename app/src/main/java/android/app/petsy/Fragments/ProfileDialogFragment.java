package android.app.petsy.Fragments;


import android.app.petsy.Classies.User;
import android.app.petsy.R;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param4";

    // TODO: Rename and change types of parameters
    private User user;
    private String contact;
    private String id;
    private int position;
    private Boolean witchList;

    private OnFragmentInteractionListener mListener;


    public ProfileDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ProfileDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileDialogFragment newInstance(User user, String contact, String id, int position, Boolean witchList) {
        ProfileDialogFragment fragment = new ProfileDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM2, user);
        args.putString(ARG_PARAM1, contact);
        args.putString(ARG_PARAM3, id);
        args.putInt(ARG_PARAM4, position);
        args.putBoolean(ARG_PARAM5, witchList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            witchList = getArguments().getBoolean(ARG_PARAM5);
            position = getArguments().getInt(ARG_PARAM4);
            id = getArguments().getString(ARG_PARAM3);
            user = getArguments().getParcelable(ARG_PARAM2);
            contact = getArguments().getString(ARG_PARAM1);

        }
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_dialog, container, false);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView petTitle = (TextView) view.findViewById(R.id.titleText);
        petTitle.setText(user.getName());
        TextView petBreed = (TextView) view.findViewById(R.id.petBreed);
        petBreed.setText(user.getBreed());

        TextView petAge = (TextView) view.findViewById(R.id.petAge);
        petAge.setText(user.getAge());
        TextView petGender = (TextView) view.findViewById(R.id.petGender);
        petGender.setText(user.getGender());
        TextView ownerName = (TextView) view.findViewById(R.id.ownerName);
        ownerName.setText(user.getOwner());
        TextView email = (TextView) view.findViewById(R.id.email);
        email.setText(user.getEmail());

        final Button add = (Button) view.findViewById(R.id.addButton);
        final TextView infoText = (TextView) view.findViewById(R.id.infoText);

        Button msg = (Button) view.findViewById(R.id.writeButton);
        Button dlt = (Button) view.findViewById(R.id.deleteButton);

        if(contact.equals("1")){
            add.setVisibility(View.GONE);
            msg.setVisibility(View.VISIBLE);
            msg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mListener.onWritePressed(user);
                }
            });
            dlt.setVisibility(View.VISIBLE);
            dlt.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onDeletePressed();
                }
            });
        } else {
            infoText.setVisibility(View.GONE);
        }

        overrideFonts(getContext(),view);
        ImageView profileFoto = (ImageView) view.findViewById(R.id.dogFoto);
        if(user.getFoto().equals("true"))
            profileFoto.setImageBitmap(user.getProfileFoto());
        else {
            profileFoto.setImageResource(R.drawable.dog_profile);
        }
        Button button = (Button) view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addContact(add, infoText);
            }
        });
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "LobsterTwo-Bold.otf"));
                ((TextView) v).setTextSize(16);
            }
        } catch (Exception e) {
        }
    }

    public void addContact(final Button button, final TextView text){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Whose", id);
        params.put("Who", user.getId());

        client.get("http://serveris.hol.es/addContact.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        button.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        mListener.onAddContactPressed(user);
                        Toast.makeText(getActivity(), "Added to contacts", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Toast.makeText(getActivity(), "Connection problem!", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void onDeletePressed() {
        String link = null;
        if (witchList) link ="http://serveris.hol.es/deleteContact.php";
        else link = "http://serveris.hol.es/deleteConversation.php";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Whose", id);
        params.put("Who", user.getId());
        client.get(link, params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        if (witchList)
                            mListener.onDeleteContactPressed(user, true);
                        else
                            mListener.onDeleteContactPressed(user, false);
                        dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }
                }
        );
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDeleteContactPressed(User user, Boolean witchList);
        void onAddContactPressed(User user);
        void onWritePressed(User user);
    }

}

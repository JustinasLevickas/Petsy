package android.app.petsy.Fragments;

import android.app.ProgressDialog;
import android.app.petsy.Classies.User;
import android.app.petsy.Encrypt.Encrypt;
import android.app.petsy.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
   // private static final String ARG_PARAM2 = "param2";
    private ProgressDialog progress;
    private String encryptedPassword = null;
    SharedPreferences autoSignIn;
    private String message;
    // TODO: Rename and change types of parameters
    private boolean mParam1;


    private OnFragmentInteractionListener mListener;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(Boolean auto) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, auto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "LobsterTwo-Bold.otf");
        TextView appName = (TextView) view.findViewById(R.id.appName);
        appName.setTypeface(tf);
        TextView email = (TextView) view.findViewById(R.id.textViewMail);
        email.setTypeface(tf);
        TextView pass = (TextView) view.findViewById(R.id.textViewPass);
        pass.setTypeface(tf);
        CheckBox stay = (CheckBox) view.findViewById(R.id.stay);
        stay.setTypeface(tf);
        Button signIn = (Button) view.findViewById(R.id.signIn);
        signIn.setTypeface(tf);
        Button signUp = (Button) view.findViewById(R.id.signUp);
        signUp.setTypeface(tf);

        Button button = (Button) view.findViewById(R.id.signIn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkFields(view);
            }
        });
        Button button2 = (Button) view.findViewById(R.id.signUp);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mListener.beginRegistration();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    //    if (mListener != null) {
        //    mListener.onSuccessfulSignIn(User);
       // }
    }

    @Override
    public void onStart() {
        super.onStart();
        autoSignIn = this.getActivity().getSharedPreferences("SignIn", Context.MODE_PRIVATE);
        if(mParam1 == true)
            connect(true);
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

    public void connect(Boolean auto){
       // Toast.makeText(getActivity(), autoSignIn.getString("Email", "null"), Toast.LENGTH_LONG).show();
        progress = ProgressDialog.show(getActivity(), "Connecting", "Please wait..", true);
        String mail = null;
        Boolean stay = false;
        String finalEncryptedPassword = null;
        if (auto == false) {
            EditText aMail = (EditText) getView().findViewById(R.id.email);
            mail = aMail.getText().toString();
            EditText aPass = (EditText) getView().findViewById(R.id.pass);
            CheckBox aStay = (CheckBox) getView().findViewById(R.id.stay);
            stay = aStay.isChecked();
            encryptedPassword = Encrypt.encryptPassword(aPass.getText().toString());
            finalEncryptedPassword = Encrypt.finalEncrypt(encryptedPassword);
        } else{
            mail = autoSignIn.getString("Email", "null");
            encryptedPassword = autoSignIn.getString("Password", "null");
            finalEncryptedPassword = Encrypt.finalEncrypt(encryptedPassword);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Email", mail);
        try {
            params.put("Password", URLEncoder.encode(finalEncryptedPassword, "UTF-8"));
            Log.i("ENCRYPT2",URLEncoder.encode(finalEncryptedPassword, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       // params.put("Password", finalEncryptedPassword);
        final Boolean finalStay = stay;
        client.get("http://serveris.hol.es/signIn.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        try {
                            JSONObject js = new JSONObject(str);
                            if (!(js.getString("Id").equals("null"))) {
                                if(finalStay == true) {
                                    SharedPreferences.Editor signInData = autoSignIn.edit();
                                    signInData.putBoolean("AutoSignIn", true);
                                    signInData.putString("Email", js.getString("Email"));
                                    signInData.putString("Password", encryptedPassword);
                                    signInData.apply();
                                }
                                mListener.onSuccessfulSignIn(new User(js,false));
                            }
                            else
                                Toast.makeText(getActivity(), "Incorrect log in!!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        progress.dismiss();
                        Toast.makeText(getActivity(), "Connection problem!", Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    public void checkFields(View v){
        message = "";

        EditText pass = (EditText)v.findViewById(R.id.pass);
        if(pass.getText().toString().equals(""))
            message += ("Password is empty\n");
        else
        if(pass.getText().toString().length() < 6)
            message += ("Password must contain at least 6 characters\n");

        EditText email = (EditText)v.findViewById(R.id.email);
        if(email.getText().toString().equals(""))
            message += ("Email is empty\n");
        if(!email.getText().toString().contains("@") || !email.getText().toString().contains("."))
            message += ("Wrong email format\n");

        if(!message.isEmpty())
            error();
        else
            connect(false);

    }

    public void error(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setCancelable(false)
                .setTitle("Something wrong")
                .setPositiveButton("Correct", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
        void onSuccessfulSignIn(User user);
        void beginRegistration();
    }
}

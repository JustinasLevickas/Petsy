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
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

import static android.app.petsy.R.id.gender;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] breeds;
    private AutoCompleteTextView autoBreed;
    private String message;
    private ProgressDialog progress;
    private String encryptedPassword = null;
    SharedPreferences autoSignIn;
    ImageView ar = null;
    private OnFragmentInteractionListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "LobsterTwo-Bold.otf");
        TextView textPetName = (TextView) view.findViewById(R.id.textViewPetName);
        textPetName.setTypeface(tf);
        TextView textBreed = (TextView) view.findViewById(R.id.textViewBreed);
        textBreed.setTypeface(tf);
        CheckBox textCrossBreed = (CheckBox) view.findViewById(R.id.crossBreed);
        textCrossBreed.setTypeface(tf);
        final TextView textTypeBreed = (TextView) view.findViewById(R.id.textViewTypeBreed);
        textTypeBreed.setTypeface(tf);
        TextView textAge = (TextView) view.findViewById(R.id.textViewAge);
        textAge.setTypeface(tf);

        TextView textGender = (TextView) view.findViewById(R.id.textViewGender);
        textGender.setTypeface(tf);
        RadioButton textMale = (RadioButton) view.findViewById(R.id.male);
        textMale.setTypeface(tf);
        RadioButton textFemale = (RadioButton) view.findViewById(R.id.female);
        textFemale.setTypeface(tf);

        TextView textOwnerName = (TextView) view.findViewById(R.id.textViewOwnerName);
        textOwnerName.setTypeface(tf);

        TextView textEmail = (TextView) view.findViewById(R.id.textView);
        textEmail.setTypeface(tf);

        TextView textPassword = (TextView) view.findViewById(R.id.textViewPassword);
        textPassword.setTypeface(tf);
        CheckBox stay = (CheckBox) view.findViewById(R.id.stay);
        stay.setTypeface(tf);
        Button signUp = (Button) view.findViewById(R.id.signUp);
        signUp.setTypeface(tf);

        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.main_layout);
        ar = (ImageView) view.findViewById(R.id.image);

        autoBreed = (AutoCompleteTextView) view.findViewById(R.id.autoBreed);

        Button button = (Button) view.findViewById(R.id.signUp);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkFields(view);
            }
        });
        CheckBox breedCheck = (CheckBox) view.findViewById(R.id.crossBreed);
        breedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            boolean visible = true;
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   if (buttonView.isChecked())
                                                       autoBreed.setEnabled(false);
                                                    else
                                                       autoBreed.setEnabled(true);

                                                   TransitionManager.beginDelayedTransition(transitionsContainer);
                                                   visible = !visible;
                                                   textTypeBreed.setVisibility(visible ? View.VISIBLE : View.GONE);
                                                   autoBreed.setVisibility(visible ? View.VISIBLE : View.GONE);
                                                   ar.setVisibility(visible ? View.VISIBLE : View.GONE);

                                               }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
           // mListener.onSuccessfulSignUp(User);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoBreed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == autoBreed.getEditableText()) {
                   // ImageView ar = (ImageView) getView().findViewById(R.id.image);
                    if (!(Arrays.asList(breeds).contains(autoBreed.getText().toString()))) {
                        ar.setImageResource(R.drawable.bad);}
                    else ar.setImageResource(R.drawable.good);
                }
            }
        });
        getBreeds();
    }

    @Override
    public void onStart() {
        super.onStart();
        autoSignIn = this.getActivity().getSharedPreferences("SignIn", Context.MODE_PRIVATE);
    }

    public void getBreeds(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://serveris.hol.es/breedList.json", null, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        try {
                            JSONArray js = new JSONArray(str);
                            breeds = new String[js.length()];
                            for(int i=0;i<js.length();i++){
                                breeds[i] = js.getString(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Create a handler attached to the UI Looper
                        Handler handler = new Handler(Looper.getMainLooper());
                        // Post code to run on the main UI Thread (usually invoked from worker thread)
                        handler.post(new Runnable() {
                            public void run() {
                                // UI code goes here
                                   ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, breeds);
                                autoBreed.setThreshold(1);
                                autoBreed.setAdapter(adapter1);
                            }
                        });


                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Toast.makeText(getActivity(), "Connection problem!", Toast.LENGTH_LONG).show(); Log.i("failedas",":(");
                    }
                }
        );

    }

    public void checkFields(View v){
        message = "";

        EditText petName = (EditText)v.findViewById(R.id.petsName);
        if(petName.getText().toString().equals("")) {
            message += ("Pet's name is empty\n");
            Log.i("vardas", message +"?");
        }

        EditText ownerName = (EditText)v.findViewById(R.id.ownersName);
        if(ownerName.getText().toString().equals(""))
            message += ("Owner's name is empty\n");

        CheckBox crossBreed = (CheckBox)v.findViewById(R.id.crossBreed);
        if(!crossBreed.isChecked()){
            if (!(Arrays.asList(breeds).contains(autoBreed.getText().toString())))
                message += ("There is no such breed\n");
        }

        EditText pass = (EditText)v.findViewById(R.id.password);
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
            checkEmail(email, v);

    }

    public void checkEmail(EditText email, final View v){
        progress = ProgressDialog.show(getContext(), "Connecting", "Please wait...", true);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Email", email.getText().toString());

        client.get("http://serveris.hol.es/email.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        if(str.equals("1")){
                            message += ("This Email is already used\n");
                        }
                        progress.dismiss();
                        if(message.equals("")){
                            createNewUser(v);
                            }
                        else {
                            error();
                        }
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

    public void createNewUser(View v){
        final EditText petName = (EditText)v.findViewById(R.id.petsName);
        EditText ownerName = (EditText)v.findViewById(R.id.ownersName);
        EditText email = (EditText)v.findViewById(R.id.email);
        EditText pass = (EditText)v.findViewById(R.id.password);
        AutoCompleteTextView autoBreed = (AutoCompleteTextView)v.findViewById(R.id.autoBreed);
        CheckBox crossBreed = (CheckBox)v.findViewById(R.id.crossBreed);
        if(crossBreed.isChecked()) autoBreed.setText("crossbreed");
        Spinner age = (Spinner)v.findViewById(R.id.Age);
        RadioGroup genderGroup = (RadioGroup)v.findViewById(gender);
        int genderId = genderGroup.getCheckedRadioButtonId();
        RadioButton gender = (RadioButton)v.findViewById(genderId);

        encryptedPassword = Encrypt.encryptPassword(pass.getText().toString());
        String finalEncryptedPassword = Encrypt.finalEncrypt(encryptedPassword);

        final CheckBox stay = (CheckBox) v.findViewById(R.id.stay);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Email", email.getText().toString());
        params.put("Name", petName.getText().toString());
        params.put("Owner", ownerName.getText().toString());
        params.put("Breed", autoBreed.getText().toString());
        params.put("Age", age.getSelectedItemPosition());
        params.put("Gender", gender.getText().toString());
        try {
            params.put("Password", URLEncoder.encode(finalEncryptedPassword, "UTF-8"));
            Log.i("ENCRYPT2",URLEncoder.encode(finalEncryptedPassword, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("ENCRYPT",finalEncryptedPassword);
        client.get("http://serveris.hol.es/signUp.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        try {
                            JSONObject js = new JSONObject(str);
                            if(stay.isChecked()){
                                SharedPreferences.Editor signInData = autoSignIn.edit();
                                signInData.putBoolean("AutoSignIn", true);
                                signInData.putString("Email", js.getString("Email"));
                                signInData.putString("Password", encryptedPassword);
                                signInData.apply();
                            }
                            mListener.onSuccessfulSignUp(new User(js,false));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Toast.makeText(getActivity(), "Connection problem!", Toast.LENGTH_LONG).show();
                    }
                }
        );
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
        void onSuccessfulSignUp(User user);
    }
}

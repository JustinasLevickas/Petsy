package android.app.petsy.Fragments;

import android.app.petsy.Classies.User;
import android.app.petsy.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragment} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View viv;
    private User user;

    private OnFragment mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(User user, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);
        args.putString(ARG_PARAM2, param2);
       // args.put
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_PARAM1);
            //mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        viv = view;
        TextView petName = (TextView) view.findViewById(R.id.petName);
        petName.setText(user.getName());
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

        Button button = (Button) view.findViewById(R.id.uploadButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mListener.uploadFoto();
            }
        });
        overrideFonts(getContext(),view);
        ImageView profileFoto = (ImageView) view.findViewById(R.id.dogFoto);
        if(user.getFoto().equals("true"))
            //loadProfileFoto();
        profileFoto.setImageBitmap(user.getProfileFoto());

        else {
            //ImageView profileFoto = (ImageView) view.findViewById(R.id.dogFoto);
            profileFoto.setImageResource(R.drawable.dog_profile);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.uploadFoto();
        }
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
                ((TextView) v).setTextSize(18);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragment) {
            mListener = (OnFragment) context;
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
    public interface OnFragment {
        // TODO: Update argument type and name
        void uploadFoto();
    }

    public void fotike(String foto){
        ViewGroup gupe = ((ViewGroup)getView().getParent());
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_profile, gupe, false);
        ImageView img = (ImageView) viv.findViewById(R.id.dogFoto);
        img.setImageBitmap(BitmapFactory.decodeFile(foto));

    }

    public void reloadProfileFoto (){
        //user.loadProfileFoto();
        ImageView profileFoto = (ImageView) viv.findViewById(R.id.dogFoto);
        profileFoto.setImageBitmap(user.getProfileFoto());
    }



}

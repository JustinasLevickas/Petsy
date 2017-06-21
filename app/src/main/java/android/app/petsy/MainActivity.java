package android.app.petsy;

import android.Manifest;
import android.app.Activity;
import android.app.petsy.Classies.User;
import android.app.petsy.Classies.Utility;
import android.app.petsy.Fragments.ContactsFragment;
import android.app.petsy.Fragments.ConversationFragment;
import android.app.petsy.Fragments.ListOfConversationsFragment;
import android.app.petsy.Fragments.LogInFragment;
import android.app.petsy.Fragments.MainPageView;
import android.app.petsy.Fragments.MeniuFragment;
import android.app.petsy.Fragments.ProfileDialogFragment;
import android.app.petsy.Fragments.ProfileFragment;
import android.app.petsy.Fragments.SignUpFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements MainPageView.OnFragmentInteractionListener,
        LogInFragment.OnFragmentInteractionListener,
        MeniuFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragment, ContactsFragment.OnFragmentInteractionListener,
        ProfileDialogFragment.OnFragmentInteractionListener, ListOfConversationsFragment.OnFragmentInteractionListener{

    SharedPreferences autoSignIn = null;
    User user = null;
    User chatUser = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout meniuFrame = (FrameLayout)findViewById(R.id.meniu_fragment);
        meniuFrame.setPadding(0, getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android")), 0, 0);
        FrameLayout contentFrame = (FrameLayout)findViewById(R.id.content_fragment);
        if(!ViewConfiguration.get(MainActivity.this).hasPermanentMenuKey())
            contentFrame.setPadding(0,0,0,getResources().getDimensionPixelSize(getResources().getIdentifier("navigation_bar_height", "dimen", "android")));
        autoSignIn = getSharedPreferences("SignIn", Context.MODE_PRIVATE);
        if (isAutoSignIn() == true)
            manageFragments(0, true);
        else manageFragments(0, false);

    }


    public boolean isAutoSignIn(){
        Boolean auto = autoSignIn.getBoolean("AutoSignIn", false);
        return auto;
    }

    public void manageFragments(int position, boolean auto){
        FragmentTransaction fragments = getSupportFragmentManager().beginTransaction();
        MeniuFragment meniu = (MeniuFragment) getSupportFragmentManager().findFragmentByTag("Meniu");
        if (meniu == null && position >=3)
            fragments.add(R.id.meniu_fragment, MeniuFragment.newInstance("kazkas","aha"), "Meniu");

        if (position >= 3 && meniu != null && meniu.isHidden()) {
            fragments.show(meniu);
        }
        if (position < 3 && meniu != null && meniu.isVisible())
            fragments.hide(meniu);


        switch(position) {
            case 0:
                fragments.replace(R.id.content_fragment, LogInFragment.newInstance(auto), "SingIn");
                break;
            case 1:
                fragments.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.left_right, R.anim.left_right_reg);
                fragments.replace(R.id.content_fragment, android.app.petsy.Fragments.SignUpFragment.newInstance("a","a"), "Register");
                fragments.addToBackStack(null);
                break;
            case 2:
                fragments.replace(R.id.content_fragment, ConversationFragment.newInstance(user, chatUser), "Conversation");
                fragments.addToBackStack(null);
                break;
            case 3:
                fragments.replace(R.id.content_fragment, MainPageView.newInstance(user.getId()), "PageView");
                break;
            case 4:
                fragments.replace(R.id.content_fragment, ProfileFragment.newInstance(user,"a"), "ProfileView");
                break;
        }
        fragments.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSuccessfulSignIn(User user) {
        this.user = user;
        manageFragments(3, false);
     //   Toast.makeText(MainActivity.this, "User sign up", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessfulSignUp(User user) {
        this.user = user;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStack();
        manageFragments(3, false);
      //  Toast.makeText(MainActivity.this, "User created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSingOut() {
        user = null;
        SharedPreferences.Editor signInData = autoSignIn.edit();
        signInData.putBoolean("AutoSignIn", false);
        signInData.putString("Email", "null");
        signInData.putString("Password", "null");
        signInData.apply();
        manageFragments(0, false);
    }

    @Override
    public void showProfile() {
        if(user.getFoto().equals("true")){
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
                    manageFragments(4, false);
                    // Log.i("LOAD FOTO","SUCCES");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                   // Log.i("LOAD FOTO","FALSE");
                }
            });
        } else
            manageFragments(4, false);
    }

    public void reloadFoto(){
        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("ProfileView");
        profileFragment.reloadProfileFoto();
    }


    @Override
    public void showMainView() {
        manageFragments(3, false);
    }

    @Override
    public void uploadFoto() {
        selectImage();
    }

    @Override
    public void beginRegistration() {
        manageFragments(1,false);
    }

    @Override
    public void onDeleteContactPressed(User user, Boolean witchList) {
        MainPageView mainPage = (MainPageView) getSupportFragmentManager().findFragmentByTag("PageView");
        mainPage.deleteContact(user, witchList);
    }

    @Override
    public void onWritePressed(User user) {
        chatUser = user;
        manageFragments(2, false);
    }

    @Override
    public void onAddContactPressed(User user) {
        MainPageView mainPage = (MainPageView) getSupportFragmentManager().findFragmentByTag("PageView");
        mainPage.addContact(user);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
            {
                Bitmap newProfilePic = null;
                try {
                    newProfilePic = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendFoto(modifyBitmap(newProfilePic,true), "profileFotoSmall.png");
                sendFoto(modifyBitmap(newProfilePic,false), "profileFoto.png");
            }
            else if (requestCode == REQUEST_CAMERA){
                File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                sendFoto(modifyBitmap(bitmap,true), "profileFotoSmall.png");
                sendFoto(modifyBitmap(bitmap,false), "profileFoto.png");
            }
        }
    }


    public Bitmap modifyBitmap(Bitmap foto, boolean small){
        Bitmap result = null;
        int width = foto.getWidth();
        int height = foto.getHeight();
        if(width > height){
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            result = Bitmap.createBitmap(foto , 0, 0, width, height, matrix, true);
        }else result = foto;
        if(small == true){
            result = Bitmap.createScaledBitmap(result, 30, 50, true);
            result = Bitmap.createBitmap(result, 0, 10, 30, 30);
        }else {
            result = Bitmap.createScaledBitmap(result, 470, 800, true);
            result = Bitmap.createBitmap(result, 10, 200, 400, 400);
        }
        return getCroppedBitmap(result);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public void sendFoto(final Bitmap foto, final String fotoName){
        AsyncHttpClient client = new AsyncHttpClient();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] myByteArray = out.toByteArray();
        RequestParams params = new RequestParams();
        params.put("uploaded_file", new ByteArrayInputStream(myByteArray), fotoName);
        params.put("id", user.getId());
                    client.post("http://serveris.hol.es/a.php", params, new TextHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String str) {
                            // called when response HTTP status is "200 OK"
                            if(fotoName.equals("profileFoto.png")) {
                                user.setFoto("true");
                                user.setProfileFoto(foto);
                                reloadFoto();
                                Toast.makeText(getApplicationContext(), "Successfully uploaded", Toast.LENGTH_LONG).show();
                                updateUserFotoDatabase();

                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                        }

                    });

    }

    public void updateUserFotoDatabase(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("ID", user.getId());
        client.get("http://serveris.hol.es/setFotoTrue.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String str) {
                        // called when response HTTP status is "200 OK"
                        Log.i("ats",str);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("PRIIMTA", "PRIIIMTAAA");
                } else {
                    Log.i("ATMESTA", "ATMEEESTA");
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MainActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2
            );
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CAMERA);
    }


}

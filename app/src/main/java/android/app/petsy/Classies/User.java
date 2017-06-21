package android.app.petsy.Classies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Justinas on 2017-05-28.
 */

public class User implements Parcelable{

    private String id;
    private String email;
    private String name;
    private String owner;
    private String breed;
    private String age;
    private String gender;
    private String location;
    private String foto;
    private String IfCrossBreed;
    private String lastTime;

    private Bitmap profileFoto;

    public User(JSONObject user, boolean time){
        try {
            id = user.getString("Id");
            email = user.getString("Email");
            name = user.getString("Name");
            owner = user.getString("Owner");
            breed = user.getString("Breed");
            age = user.getString("Age");
            gender = user.getString("Gender");
            location = user.getString("Location");
            foto = user.getString("Foto");
            IfCrossBreed = user.getString("IfCrossBreed");
            if(time)
                lastTime = user.getString("LastTime");
        } catch (JSONException e) {
        e.printStackTrace();
        }
       // if(foto.equals("true"))
         //   loadProfileFoto();
    }

    public void loadProfileFoto (){
        AsyncHttpClient client = new AsyncHttpClient();
        String[] fileType = {

                "image/png",
                "image/jpeg",
                "image/gif"
        };
        client.get("http://serveris.hol.es/users/"+getId()+"/profileFoto.png", new BinaryHttpResponseHandler(fileType) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                profileFoto = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                Log.i("LOAD FOTO","SUCCES");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                Log.i("LOAD FOTO","FALSE");
            }
        });
    }
    public Bitmap getProfileFoto() {
        return profileFoto;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public void setProfileFoto(Bitmap profileFoto) {
        this.profileFoto = profileFoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {return age;}

    public void setAge(String age) {this.age = age;}

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIfCrossBreed() {
        return IfCrossBreed;
    }

    public void setIfCrossBreed(String ifCrossBreed) {
        IfCrossBreed = ifCrossBreed;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.owner);
        dest.writeString(this.breed);
        dest.writeString(this.age);
        dest.writeString(this.gender);
        dest.writeString(this.location);
        dest.writeString(this.foto);
        dest.writeString(this.IfCrossBreed);
        dest.writeString(this.lastTime);
        dest.writeParcelable(this.profileFoto, flags);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.email = in.readString();
        this.name = in.readString();
        this.owner = in.readString();
        this.breed = in.readString();
        this.age = in.readString();
        this.gender = in.readString();
        this.location = in.readString();
        this.foto = in.readString();
        this.IfCrossBreed = in.readString();
        this.lastTime = in.readString();
        this.profileFoto = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

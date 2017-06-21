package android.app.petsy.Classies;

import android.app.petsy.R;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Justinas on 2017-06-06.
 */

public class UserAdapter extends ArrayAdapter<User> {

private int katras;
    private ArrayList<User> list;

public UserAdapter(Context context, ArrayList<User> users, int katras) {
        super(context, 0, users);
        this.katras = katras;
        list = users;
}

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if(katras == 1){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_user, parent, false);
            }
            else convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_user, parent, false);
        }
        // Lookup view for data population
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "LobsterTwo-Bold.otf");
        TextView textName = (TextView) convertView.findViewById(R.id.textName);
        TextView textBreed = (TextView) convertView.findViewById(R.id.textBreed);
        TextView textOwner = (TextView) convertView.findViewById(R.id.textOwner);
        // Populate the data into the template view using the data object
        textName.setText(user.getName());
        textBreed.setText(user.getBreed());
        textOwner.setText(user.getOwner());

        textName.setTypeface(tf);
        textBreed.setTypeface(tf);
        textOwner.setTypeface(tf);
        if (katras == 0){
            TextView textTime = (TextView) convertView.findViewById(R.id.textTime);
            textTime.setVisibility(View.VISIBLE);
            textTime.setText(user.getLastTime());
            textTime.setTypeface(tf);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getPosition(User item) {
        int result = 0;
        for(int i=0;i<list.size();i++){
            if (list.get(i).getId().equals(item.getId()))
                result =  i;
        }
        return result;
    }
}

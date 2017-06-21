package android.app.petsy.Classies;

import android.app.petsy.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Justinas on 2017-06-14.
 */

public class ConversationAdapter extends ArrayAdapter<Message> {

    private String yourId;

    public ConversationAdapter(Context context, ArrayList<Message> zinutes, String id) {
        super(context, 0, zinutes);
        this.yourId = id;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Message message = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_messages, parent, false);

        }
        TextView text = (TextView) convertView.findViewById(R.id.message_text);
        if( !( yourId.equals(message.getID1()) ) ) {
            text.setBackgroundResource(R.drawable.message_left);
            convertView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        else {
            text.setBackgroundResource(R.drawable.message_right);
            convertView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        }
        text.setText(message.getMessage());
        return convertView;
    }
}
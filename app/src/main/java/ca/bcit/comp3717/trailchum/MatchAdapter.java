package ca.bcit.comp3717.trailchum;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MatchAdapter extends ArrayAdapter<UserAccount> {

    Context _context;

    public MatchAdapter(Context context, ArrayList<UserAccount> users) {
        super(context, 0, users);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Activity activity = (Activity) _context;
        // Get the data item for this position
        UserAccount user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.matches_list_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvUser = convertView.findViewById(R.id.tvUser);
        // Populate the data into the template view using the data object
        tvUser.setText(user.getName());

        // Return the completed view to render on screen
        return convertView;
    }

}

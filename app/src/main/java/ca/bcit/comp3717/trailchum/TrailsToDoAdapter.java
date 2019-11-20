package ca.bcit.comp3717.trailchum;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TrailsToDoAdapter extends ArrayAdapter<Trail> {

    Context _context;

    public TrailsToDoAdapter(Context context, ArrayList<Trail> trailsToDo) {
        super(context, 0, trailsToDo);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Activity activity = (Activity) _context;
        // Get the data item for this position
        Trail trail = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trails_to_do_list_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvTrailName = convertView.findViewById(R.id.tvTrailName);
        // Populate the data into the template view using the data object
        tvTrailName.setText(trail.getPATHNAME());

        // Return the completed view to render on screen
        return convertView;
    }

}

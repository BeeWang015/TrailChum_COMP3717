package ca.bcit.comp3717.trailchum;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class TrailStartEndMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<String> pathStart;
    List<String> pathEnd;
    Marker mStart;
    Marker mEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pathStart = getIntent().getStringArrayListExtra("pathStart");
        pathEnd = getIntent().getStringArrayListExtra("pathEnd");
//        Toast.makeText(this, pathStart.toString() + pathEnd.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double pathStartLat = Double.parseDouble(pathStart.get(1));
        double pathStartLong = Double.parseDouble(pathStart.get(0));
        double pathEndLat = Double.parseDouble(pathEnd.get(1));
        double pathEndLong = Double.parseDouble(pathEnd.get(0));

        LatLng start = new LatLng(pathStartLat, pathStartLong);
        LatLng end = new LatLng(pathEndLat, pathEndLong);
        mStart = mMap.addMarker(new MarkerOptions().position(start)
                .title("Trail Start"));
        mEnd = mMap.addMarker(new MarkerOptions().position(end)
                .title("Trail End"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));

        Marker[] markers = {mStart, mEnd};

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

    }


}

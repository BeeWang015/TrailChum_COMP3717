package ca.bcit.comp3717.trailchum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomMapActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map);

        MapFragment mapFragment = MapFragment.newInstance();
        android.app.FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.add(R.id.papa, mapFragment);
        trans.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bcit = new LatLng(49.25, -123);
        mMap.addMarker(new MarkerOptions().position(bcit).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bcit, 6.0f));
    }

}

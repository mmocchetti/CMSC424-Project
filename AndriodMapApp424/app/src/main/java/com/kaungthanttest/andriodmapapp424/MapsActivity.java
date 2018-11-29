package com.kaungthanttest.andriodmapapp424;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnMarkerDragListener{

    private GoogleMap mMap;
    TextView testView;
    Button b;
    MarkerOptions marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //-------------------------------------------------------------------

        //---------------------------------------------------------------

        testView= (TextView) findViewById(R.id.textView3);
        b= (Button) findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callPlaceAutocompleteActivityIntent();
            }
        });



    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady (GoogleMap googleMap){
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        double L1, L2;
        L1=39.018843;
        L2=-76.938433;
        LatLng place = new LatLng(L1, L2);
        marker= new MarkerOptions().position(place).title("Marker in ("+L1+","+L2+")").draggable(true);
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.setOnMarkerDragListener(this);



    }
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng p1=marker.getPosition();
        marker.setTitle("Marker in ("+p1.latitude+","+p1.longitude+")");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p1));
        marker.showInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public boolean isDestroyed() {
        return super.isDestroyed();
    }

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 132;

    private void callPlaceAutocompleteActivityIntent() {

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                testView.setText(place.getAddress());
                marker= new MarkerOptions().position(place.getLatLng());
                mMap.clear();
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                testView.setText(status.toString());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

}

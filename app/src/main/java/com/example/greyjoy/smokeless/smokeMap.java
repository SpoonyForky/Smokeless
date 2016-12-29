package com.example.greyjoy.smokeless;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class smokeMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Button searchBtn;
    private EditText edtAddress;
    private String ADDRESS = "ADDRESS";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke_map);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        searchBtn = (Button) findViewById(R.id.btnSearch);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
       Bundle mapBundle = getIntent().getExtras();
        if (mapBundle !=null) {
            edtAddress.setText(mapBundle.getString("ADDRESS"));
            onSearch(null);
        }
    }

    public void onSearch(View view){

        String location = edtAddress.getText().toString();
        List<Address> addressList = null;
        if (location != null || !(location == " ")){
            Geocoder geocoder = new Geocoder(this);
            try {
               addressList =  geocoder.getFromLocationName(location,4);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for ( int i = 0; i < addressList.size(); i++) {
                Address address = addressList.get(i);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).snippet(location)).setTitle("Click to save");
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }

    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        String address = getCompleteAddressString(marker.getPosition().latitude, marker.getPosition().longitude);
        Intent mapFound = new Intent();
        mapFound.putExtra(ADDRESS,address);
        setResult(RESULT_OK,mapFound);
        finish();
        return true;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    };
}

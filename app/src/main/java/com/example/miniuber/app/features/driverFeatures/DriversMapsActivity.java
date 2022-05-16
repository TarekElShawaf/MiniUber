package com.example.miniuber.app.features.driverFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.app.features.commonFeatures.directions.TaskLoadedCallback;
import com.example.miniuber.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DriversMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener , TaskLoadedCallback {


    private static final String TAG = "MapsActivity";
    private Polyline currentPolyline;
    private static final String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static boolean locationPermissionGranted = false;
    private static final int locationPermissionRequestCode = 1;
    private static final float defaultZoom = 15;
    private GoogleMap googleMap;
    private AppCompatButton currentLocation;
    private ArrayList<LatLng> markers =new ArrayList<>();
    private int ids = 0;
    private HashMap<Integer,LatLng> markersHashMap = new HashMap<>();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapsBinding binding;
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView( R.layout.activity_drivers_maps);

        currentLocation = findViewById(R.id.gpsDriver);



        Objects.requireNonNull(getSupportActionBar()).hide();
        // settingNavigation();
        settingEditText();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driverMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        getLocationPermission();



    }

    private void settingEditText() {
        currentLocation.setOnClickListener(view -> currentLocation.setText(""));
    }

   /* private void settingNavigation() {
        AppCompatButton navigationButton;
        navigationButton = findViewById(R.id.navigationIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.menu_Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationButton.setOnClickListener(v -> {
            actionBarDrawerToggle.syncState();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            navigationView.setNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.navhome:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.navlogOut:
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.navsettings:
                        // drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.navtrips:
                        // drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                }
                return true;
            });
        });


    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMapStyle() {
        googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json_light)));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        this.googleMap = googleMap;
        if (locationPermissionGranted) {

            getDeviceLocation();
            //geoLocate();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            setMapStyle();
            //setLocationMark();
            init();

        }
    }



    private void init() {
        Log.d(TAG, "init: initializing ");


        currentLocation.setOnClickListener(v -> getDeviceLocation());
        hideSoftKeyboard();
    }

    /*private void geoLocate() {


        String searchString = searchMap.getText().toString();
        Geocoder geocoder = new Geocoder(DriversMapsActivity.this);
        List<Address> addresses = new ArrayList<>();
        try {


            addresses = geocoder.getFromLocationName(searchString, 4);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: " + e.getMessage());

        }
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            searchMap.setText(addresses.get(0).getAddressLine(0));
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), defaultZoom, address.getAddressLine(0),1);

        }
    }*/



    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting Device Location");
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (locationPermissionGranted) {
                Task<Location> location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            if (location != null) {
                                Log.d(TAG, "onComplete: found Location");
                                android.location.Location currentLocation = (android.location.Location) task.getResult();
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), defaultZoom, "My Location",0);

                                Geocoder geocoder = new Geocoder(DriversMapsActivity.this);
                                List<Address> addresses = new ArrayList<>();
                                try {
                                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 4);
                                } catch (IOException e) {
                                    Log.d(TAG, "geoLocate: " + e.getMessage());
                                }

                            }

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(DriversMapsActivity.this, "Unable To find Current Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: Security Exception :" + e.getMessage());

        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title,int option) {
        Log.d(TAG, "moveCamera: moving the camera ");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions().position(latLng).title(title);

        int height = 100;
        int width = 100;
        if(ids==0)
        {
            BitmapDrawable bitMapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.marker_icon);
            Bitmap b = bitMapDrawable.getBitmap();
            options.draggable(false);
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            googleMap.addMarker(options);
            markersHashMap.put(ids,latLng);
            ids++;
        }

        if(option==1)
        {
            BitmapDrawable bitMapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.marker_icon);
            Bitmap b = bitMapDrawable.getBitmap();
            options.draggable(false);
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            googleMap.addMarker(options);
            markersHashMap.put(ids,latLng);
            ids++;
        }


        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                marker.setPosition(marker.getPosition());
                marker.setTitle(marker.getTitle());

                Log.d(TAG, "onMarkerDrag: title is :  "+marker.getTitle());
                Log.d(TAG, "onMarkerDrag: position  is :  "+marker.getPosition());
                Geocoder geocoder = new Geocoder(DriversMapsActivity.this);
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses= geocoder.getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude,4);

                    marker.setTitle(addresses.get(0).getAddressLine(0));

                    markersHashMap.put(0,new LatLng(marker.getPosition().latitude,marker.getPosition().longitude));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
        hideSoftKeyboard();

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driverMap);
        mapFragment.getMapAsync(this);

    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission ");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), fineLocation) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), coarseLocation) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, locationPermissionRequestCode);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, locationPermissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: called");
        locationPermissionGranted = false;
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        //locationPermissionGranted = false;
                        //    Log.d(TAG, "onRequestPermissionsResult: permission failed");
                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                locationPermissionGranted = true;
                initMap();
            }
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //searchMap.setText("");
    }


    @Override
    public void onMarkerDrag(@NonNull Marker marker) {



    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
    private  String getRoute( LatLng origin, LatLng dest, String mode) {
        String originString = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationString = "destination=" + dest.latitude + "," + dest.longitude;
        String modeString = "mode=" + mode;
        String param = originString + "&" + destinationString + "&" + modeString;
        String url = "https://maps.googleapis.com/maps/api/directions/json?"+param+"&key="+getString(R.string.maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = googleMap.addPolyline((PolylineOptions) values[0]);
    }
}
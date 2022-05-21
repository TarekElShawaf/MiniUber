package com.example.miniuber.app.features.driverFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.app.features.commonFeatures.ModuleSelectorActivity;
import com.example.miniuber.app.features.commonFeatures.directions.TaskLoadedCallback;


import com.example.miniuber.app.features.riderFeatures.personalInfo.PersonalInfoFragment;
import com.example.miniuber.app.features.riderFeatures.riderMapsActivity.RiderMapsActivity;
import com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment.TripsHistoryFragment;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

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
    AppCompatButton searchRider ;
    private AppCompatButton searchForRiders;
    private ProgressBar  progressBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int fragmentCounter = 0;

    private int searchRadius = 1;
    private String driverPhoneNumber;
    private String riderPhoneNumber;
    private Boolean isRiderFound = false;
    RatingBar ratingBar;
    private Boolean check = true;

    TextView rateRider;
    TextView driverRealRate;
    ImageView starRate;
    TextView textView;
    TextView riderName;
    TextView driverRate;
    CircleImageView profileImage;
    AppCompatButton callRider;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView( R.layout.activity_drivers_maps);
        driverPhoneNumber = getIntent().getStringExtra("phoneNumber");
        if(driverPhoneNumber == null) {
            driverPhoneNumber="+201111111111";

        }
        currentLocation = findViewById(R.id.gpsDriver);



        Objects.requireNonNull(getSupportActionBar()).hide();
         settingNavigation();
        settingEditText();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driverMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        getLocationPermission();
        searchRiders();
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout2,fragment);
        fragmentTransaction.commit();



    }

    private void settingNavigation() {
        navigationView = findViewById(R.id.navigationViewDriver);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.menu_Close);
        drawerLayout= findViewById(R.id.drawerLayoutDriver);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        ConstraintLayout constraintLayout= findViewById(R.id.constraint);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navhome:
                    if(check)
                    {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    else{
                        Intent intent = new Intent(this, DriversMapsActivity.class);

                        overridePendingTransition(1, 1);
                        startActivity(intent);
                        overridePendingTransition(1, 1);
                        check=true;
                    }


                    break;
                case R.id.navlogOut:
                    check=false;
                    //Toast.makeText(this, "Logout Fragment", Toast.LENGTH_SHORT).show();
                    //open moduleselector activity
                    Intent intent = new Intent(this, ModuleSelectorActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    fragmentCounter=1;
                    //drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.navspersonalinfo:
                    check=false;

                    Bundle bundle = new Bundle();
                    bundle.putString("userPhoneNumber", driverPhoneNumber);
                    bundle.putString("userType", "driver");
                    Fragment fragment = new PersonalInfoFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                    constraintLayout.setVisibility(View.INVISIBLE);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    fragmentCounter=2;
                    break;
                case R.id.navtrips:
                    check=false;
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("userPhoneNumber", driverPhoneNumber);
                    bundle2.putString("userType", "driver");
                    Fragment fragment2 = new TripsHistoryFragment();
                    fragment2.setArguments(bundle2);
                    replaceFragment(fragment2);
                    constraintLayout.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "History Fragment", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    fragmentCounter=3;
                    break;
                default: return true;

            }
            return true;
        });


    }
    private void searchRiders(){
        searchRider= findViewById(R.id.searchRiders);
        searchRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();

                getClosetDriver();

            }
        });
    }

    private void settingEditText() {
        currentLocation.setOnClickListener(view -> currentLocation.setText(""));
    }

   private void sendRequest(){
       DatabaseReference userRequest = FirebaseDatabase.getInstance().getReference().child("AvailableDrivers");
       GeoFire geoFire = new GeoFire(userRequest);
       // Toast.makeText(this, "Your Location is - "+userPhoneNumber , Toast.LENGTH_LONG).show();
       Log.d(TAG, "sendRequest: lat  "+markersHashMap.get(0).latitude +" long "+ markersHashMap.get(0).longitude);
       geoFire.setLocation(driverPhoneNumber, new GeoLocation(markersHashMap.get(0).latitude, markersHashMap.get(0).longitude));
       userRequest.child(driverPhoneNumber).child("driverStatus").setValue(0);

       searchRider.setVisibility(View.INVISIBLE);
       progressBar = findViewById(R.id.searchForRiders);
       progressBar.setVisibility(View.VISIBLE);

   }

    private void getClosetDriver(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User Requests");
        //Log.d(TAG, "getClosetDriver:  refrence to String "+ref.toString());
        GeoFire geoFire = new GeoFire(ref);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(markersHashMap.get(0).latitude,markersHashMap.get(0).longitude),searchRadius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                if(!isRiderFound){


                    Log.d(TAG, "onDataEntered: ");
                    isRiderFound = true;
                }

                riderPhoneNumber = dataSnapshot.getKey();

                double longitude = (double) dataSnapshot.child("l").child("1").getValue();
                double latitude = (double) dataSnapshot.child("l").child("0").getValue();
                LatLng riderLocation = new LatLng(latitude,longitude);
                markersHashMap.put(ids,riderLocation);
                ids++;
                moveCamera(riderLocation,defaultZoom, "Your Rider",2 );
                double distance = SphericalUtil.computeDistanceBetween(markersHashMap.get(0),markersHashMap.get(ids-1));
                progressBar.setVisibility(View.INVISIBLE);
               AppCompatButton acceptRider = findViewById(R.id.acceptTrip);
               acceptRider.setVisibility(View.VISIBLE);
               acceptRider.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       //Toast.makeText(DriversMapsActivity.this, "Request Accepted "+riderLocation.toString(), Toast.LENGTH_LONG).show();
                       Log.d(TAG, "onClick: rider location is  "+riderLocation.toString());
                       DatabaseReference userRequest = FirebaseDatabase.getInstance().getReference().child("AvailableDrivers");
                       userRequest.child(driverPhoneNumber).getRef().removeValue();
                       acceptRider.setVisibility(View.INVISIBLE);
                       addView();
                   }
               });


            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!isRiderFound){
                    searchRadius++;
                    Log.d(TAG, "onGeoQueryReady: "+searchRadius);
                    getClosetDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        Log.d(TAG, "getClosetDriver: "+searchRadius);
        Log.d(TAG, "getClosetDriver: "+ isRiderFound);

    }

    private void addView(){
         profileImage = findViewById(R.id.driverImageDialog);
         profileImage.setVisibility(View.VISIBLE);
         driverRate = findViewById(R.id.driverRate);
         driverRate.setVisibility(View.VISIBLE);
         riderName = findViewById(R.id.riderName);
        /* riderName.setText(riderPhoneNumber);
         DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(riderPhoneNumber);
         driverRef.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 if(task.isSuccessful()){
                     String name = task.getResult().getValue().toString();
                     riderName.setText(name);
                 }
             }
         });*/
         riderName.setVisibility(View.VISIBLE);
         textView = findViewById(R.id.textView);
         textView.setVisibility(View.VISIBLE);
         starRate = findViewById(R.id.starRate);
         starRate.setVisibility(View.VISIBLE);
         driverRealRate = findViewById(R.id.driverRealRate);
         driverRate.setVisibility(View.VISIBLE);
         rateRider = findViewById(R.id.textView8);
         rateRider.setVisibility(View.VISIBLE);
         ratingBar = findViewById(R.id.driverRateStars);
         ratingBar.setVisibility(View.VISIBLE);
         callRider = findViewById(R.id.driverPhoneNumber);
         callRider.setVisibility(View.VISIBLE);
         callRider.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(Intent.ACTION_DIAL);
                 riderPhoneNumber.substring(2,riderPhoneNumber.length());
                 intent.setData(Uri.parse("tel:"+riderPhoneNumber.substring(2,riderPhoneNumber.length())));
                 startActivity(intent);
             }
         });

    }

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
            //setMapStyle();
            //*setLocationMark();
            init();

        }
    }



    private void init() {
        Log.d(TAG, "init: initializing ");


        currentLocation.setOnClickListener(v -> getDeviceLocation());
        hideSoftKeyboard();
    }


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
            BitmapDrawable bitMapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.car_rider);
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
        if(option==2)
        {
            height = 75;
            width = 75;
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
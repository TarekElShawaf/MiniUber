package com.example.miniuber.app.features.riderFeatures.riderMapsActivity;

import android.Manifest;

import android.app.Dialog;
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
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import com.example.miniuber.R;

import com.example.miniuber.app.features.commonFeatures.ModuleSelectorActivity;
import com.example.miniuber.app.features.commonFeatures.directions.FetchURL;
import com.example.miniuber.app.features.commonFeatures.directions.TaskLoadedCallback;

import com.example.miniuber.app.features.riderFeatures.personalInfo.PersonalInfoFragment;
import com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment.TripsHistoryFragment;


import com.example.miniuber.databinding.ActivityMapsBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
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
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.http.GET;


public class RiderMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener , TaskLoadedCallback  {


    private static final String TAG = "MapsActivity";
    private Polyline currentPolyline;
    private static final String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static boolean locationPermissionGranted = false;
    private static final int locationPermissionRequestCode = 1;
    private static final float defaultZoom = 15;
    private GoogleMap googleMap;
    private Boolean check = true;
    private EditText searchMap;
    private ImageView markerSearch;
    private int fragmentCounter = 0;
    private AppCompatButton currentLocation;
    private ArrayList<LatLng> markers =new ArrayList<>();
    private int ids = 0;
    private int destinationId = 0;
    private HashMap<Integer,LatLng> markersHashMap = new HashMap<>();
    private TextView pickUpPoint ;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ConstraintLayout constraintLayout ;
    private String userPhoneNumber;
    private AppCompatButton searchDrivers;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ActivityMapsBinding binding;
    private int searchRadius = 1;
    private String driverPhoneNumber;
    private Boolean isDriverFound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userPhoneNumber = getIntent().getStringExtra("phoneNumber");
        userPhoneNumber="+201111111111";
        searchMap = findViewById(R.id.searchMap);
        currentLocation = findViewById(R.id.gpsRider);
        searchDrivers=findViewById(R.id.searchDrivers);;
        pickUpPoint=findViewById(R.id.pickUpPoint);
        markerSearch=findViewById(R.id.markerSearch);
        Objects.requireNonNull(getSupportActionBar()).hide();
        settingNavigation();
        settingEditText();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.riderMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        Toast.makeText(this,"Phone number is : "+ userPhoneNumber, Toast.LENGTH_SHORT).show();

        getLocationPermission();
        searchForDrivers();
        settingTime();


    }
    private void settingDriverInformation(){
        binding.carImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDriverDialog();
            }
        });
    }

    private void sendRequest(){
        DatabaseReference userRequest = FirebaseDatabase.getInstance().getReference().child("User Requests");
        GeoFire geoFire = new GeoFire(userRequest);
       // Toast.makeText(this, "Your Location is - "+userPhoneNumber , Toast.LENGTH_LONG).show();
        geoFire.setLocation("+2001142434195", new GeoLocation(markersHashMap.get(0).latitude, markersHashMap.get(0).longitude));
        userRequest.child("+201142434195").child("userStatus").setValue(0);
    }

    private void searchForDrivers(){

        searchDrivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDrivers = findViewById(R.id.searchDrivers);
                if(searchMap.getText().toString().equals("")){
                    Toast.makeText(RiderMapsActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                    return;
                }
                //LatLng test = new LatLng(30.1083169,31.3275619);


                removeOldView();
                binding.progressBar2.setVisibility(View.VISIBLE);
                sendRequest();

                Log.d(TAG, "getClosetDriver: searching near latt :   "+markersHashMap.get(0).latitude+" long :  "+markersHashMap.get(0).longitude);

                getClosetDriver();


            }
        });


    }

    private void getClosetDriver(){
        binding.progressBar2.setVisibility(View.VISIBLE);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AvailableDrivers");
        //Log.d(TAG, "getClosetDriver:  refrence to String "+ref.toString());
        GeoFire geoFire = new GeoFire(ref);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(markersHashMap.get(0).latitude,markersHashMap.get(0).longitude),searchRadius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                if(!isDriverFound){

                    Log.d(TAG, "onDataEntered: driver found"+dataSnapshot.getKey().toString());

                    isDriverFound = true;
                }

                driverPhoneNumber = dataSnapshot.getKey();
                binding.progressBar2.setVisibility(View.INVISIBLE);
                putTripView();
                double longitude = (double) dataSnapshot.child("l").child("1").getValue();
                double latitude = (double) dataSnapshot.child("l").child("0").getValue();
                    LatLng driverLocation = new LatLng(latitude,longitude);
                    markersHashMap.put(ids,driverLocation);
                    ids++;
                moveCamera(driverLocation,defaultZoom, "Your Driver",2 );
                double distance = SphericalUtil.computeDistanceBetween(markersHashMap.get(0),markersHashMap.get(destinationId));
                binding.tripDistance.setText(String.valueOf(String.format("%.1f",(distance))+" m"));
                binding.tripPrice.setText(String.valueOf(String.format("%.2f",(distance*0.4))+"$"));
                binding.tripTime.setText(String.valueOf(binding.timeChecker.getHour()+":"+binding.timeChecker.getMinute()));
                binding.confirmTrip.setVisibility(View.VISIBLE);
                binding.confirmTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        settingDriverInformation();
                        showDriverDialog();
                        DatabaseReference userRequest = FirebaseDatabase.getInstance().getReference().child("User Requests");
                        userRequest.child("+201142434195").child("userStatus").setValue(1);
                        DatabaseReference driverRequest = FirebaseDatabase.getInstance().getReference().child("AvailableDrivers");
                        Toast.makeText(RiderMapsActivity.this,"Driver Found"+driverRequest.child("+201142434195").child("driverStatus").toString(),Toast.LENGTH_SHORT).show();
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
                if(!isDriverFound){
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
        Log.d(TAG, "getClosetDriver: "+isDriverFound);

    }
    private void settingTripData()
    {



    }
    private void removeOldView(){

        markerSearch.setVisibility(View.INVISIBLE);
        searchDrivers.setVisibility(View.INVISIBLE);
        binding.DistanceTextView.setVisibility(View.INVISIBLE);
        binding.pickupTextView.setVisibility(View.INVISIBLE);
        binding.dropOffTextView.setVisibility(View.INVISIBLE);
        binding.lineHorizontal.setVisibility(View.INVISIBLE);
        binding.lineVertical.setVisibility(View.INVISIBLE);
        binding.pickUpPoint.setVisibility(View.INVISIBLE);
        binding.dropOffTextView.setVisibility(View.INVISIBLE);
        binding.searchMap.setVisibility(View.INVISIBLE);
        binding.timeChecker.setVisibility(View.INVISIBLE);

    }
    private void showDriverDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.driver_details_diaglog);
        dialog.findViewById(R.id.driverRealRate) ;
        dialog.findViewById(R.id.driverPhoneNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                driverPhoneNumber.substring(2,driverPhoneNumber.length());
                intent.setData(Uri.parse("tel:"+driverPhoneNumber.substring(2,driverPhoneNumber.length())));
                startActivity(intent);
            }
        });
        dialog.show();

    }
    private void putTripView(){
        binding.DistanceTextView.setVisibility(View.VISIBLE);
        binding.pickupTextView.setVisibility(View.VISIBLE);
        binding.dropOffTextView.setVisibility(View.VISIBLE);
        binding.lineHorizontal.setVisibility(View.VISIBLE);
        binding.lineVertical.setVisibility(View.VISIBLE);
        binding.pickUpPoint.setVisibility(View.VISIBLE);
        binding.dropOffTextView.setVisibility(View.VISIBLE);
        binding.searchMap.setVisibility(View.VISIBLE);
        binding.timeTextView.setVisibility(View.VISIBLE);
        binding.tripTime.setVisibility(View.VISIBLE);
        binding.tripDistance.setVisibility(View.VISIBLE);
        binding.carImage.setVisibility(View.VISIBLE);

        binding.tripPrice.setVisibility(View.VISIBLE);
        binding.DistanceTextView.setVisibility(View.VISIBLE);
        binding.priceTextview.setVisibility(View.VISIBLE);
      //  binding.cancelTrip.setVisibility(View.VISIBLE);

    }
    private void settingEditText() {
        currentLocation.setOnClickListener(view -> currentLocation.setText(""));
    }
    private void settingTime() {
        binding.timeChecker.setOnClickListener(view -> {
          String time =  binding.timeChecker.getHour() +":"+ binding.timeChecker.getMinute() ;
          binding.tripTime.setText(time);

        });

    }


    private void settingNavigation() {
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.menu_Close);
        drawerLayout= findViewById(R.id.drawerLayoutRider);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        constraintLayout= findViewById(R.id.constraintLayout3);
            navigationView.setNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.navhome:
                       if(check)
                        {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        else{
                            Intent intent = new Intent(this, RiderMapsActivity.class);

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
                        bundle.putString("userPhoneNumber", userPhoneNumber);
                        Fragment fragment = new PersonalInfoFragment();
                        fragment.setArguments(bundle);
                        replaceFragment(fragment);
                        constraintLayout.setVisibility(View.INVISIBLE);
                         drawerLayout.closeDrawer(GravityCompat.START);
                        fragmentCounter=2;
                        break;
                    case R.id.navtrips:
                        check=false;
                       replaceFragment(new TripsHistoryFragment());
                        Toast.makeText(this, "History Fragment", Toast.LENGTH_SHORT).show();
                        constraintLayout.setVisibility(View.INVISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragmentCounter=3;
                        break;
                    default: return true;

                }
                return true;
            });



    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();



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
            setMapStyle();
            //setLocationMark();
            init();

        }
    }



    private void init() {
        Log.d(TAG, "init: initializing ");
        markerSearch=findViewById(R.id.markerSearch);

        markerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(searchMap.getText().toString().equals("")){
                    Toast.makeText(RiderMapsActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
                else{
                    geoLocate();
                    if(ids>1)
                    {
                        String route = getRoute(markersHashMap.get(0), markersHashMap.get(ids-1), "driving");
                        new FetchURL(RiderMapsActivity.this).execute(route, "driving");
                    }

                }

            }
        });
        searchMap.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                geoLocate();


            }
            return false;
        });
        currentLocation.setOnClickListener(v -> getDeviceLocation());
        hideSoftKeyboard();
    }

    private void geoLocate() {


        String searchString = searchMap.getText().toString();
        Geocoder geocoder = new Geocoder(RiderMapsActivity.this);
        List<Address> addresses = new ArrayList<>();
        try {


            addresses = geocoder.getFromLocationName(searchString, 4);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: " + e.getMessage());

        }
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            //searchMap.setText(addresses.get(0).getAddressLine(0));
            destinationId =ids;
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), defaultZoom, address.getAddressLine(0),1);

        }
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
                               
                                Geocoder geocoder = new Geocoder(RiderMapsActivity.this);
                                List<Address> addresses = new ArrayList<>();
                                try {
                                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 4);
                                } catch (IOException e) {
                                    Log.d(TAG, "geoLocate: " + e.getMessage());
                                }

                            }

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(RiderMapsActivity.this, "Unable To find Current Location", Toast.LENGTH_SHORT).show();
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
            options.draggable(true);
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
            BitmapDrawable bitMapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.car_rider);
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
               Geocoder geocoder = new Geocoder(RiderMapsActivity.this);
               List<Address> addresses = new ArrayList<>();
               try {
                   addresses= geocoder.getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude,4);

                   marker.setTitle(addresses.get(0).getAddressLine(0));
                  // pickUpPoint.setText(addresses.get(0).getAddressLine(0));
                   markersHashMap.put(0,new LatLng(marker.getPosition().latitude,marker.getPosition().longitude));
                   Log.d(TAG, "onMarkerDrag: +"+marker.getPosition().latitude+marker.getPosition().longitude);

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
                .findFragmentById(R.id.riderMap);
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
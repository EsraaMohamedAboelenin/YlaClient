package solversteam.familycab;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import solversteam.familycab.Activities.RideLater_Activity;
import solversteam.familycab.Activities.RideNow_Activity;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.CurrentActivity;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.Activities.Rate_Captain_activity;
import solversteam.familycab.Nav.Activities.PlacesActivity;
import solversteam.familycab.Util.GPSTracker;
import solversteam.familycab.Util.GetScreenSize;
import solversteam.familycab.Util.Handler_Cars;

/**
 * Created by mosta on 5/17/2017.
 */

public class Set_Map implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    Context context;
    SupportMapFragment mapFragment;
    public GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP_LOCATION";
    private String adress_string;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private LatLng mCenterLatLong;
    public Thread thread, findcar_thread;
    public Handler handler;
    public Runnable runnable;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private CameraPosition cameraPosition;
    private Marker marker, car_marker;
    private TextView hometext;
    private ArrayList<Marker> markers;

    private Boolean is_marker = false, is_carmarke = false;

    public Set_Map(Context context, SupportMapFragment mapFragment, TextView hometext) {

        this.context = context;
        this.mapFragment = mapFragment;
        this.hometext = hometext;
        sharedPreferences = context.getSharedPreferences("lang_and_count", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Log.d("in_map", "in_map");
       mapFragment.getMapAsync(this);
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!new GPSTracker(context,this).canGetLocation()) {
                // notify user
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(context, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;
         mGoogleApiClient.connect();


        if (context instanceof RideLater_Activity || context instanceof RideNow_Activity){
            Log.d("ride","hi");
        }
        else{
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Log.d("Camera_postion_change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    try {


                        Log.d("logit","dfdff");
                        if (thread!=null)
                            thread.interrupt();
                        if (context instanceof HomeActivity ||context instanceof PlacesActivity)
                           thread = new Thread() {
                           @Override
                            public void run() {

                                try {
                                    while(true) {

                                                Log.d("logit", "here2");

                                                    if (context instanceof RideLater_Activity || context instanceof Rate_Captain_activity) {
                                                        editor.putString("latitude", "0.0");
                                                        editor.putString("longitude", "0.0");
                                                        editor.commit();
                                                    } else if (context instanceof HomeActivity ||context instanceof PlacesActivity) {
                                                        if (context instanceof HomeActivity && sharedPreferences.getString("booking_distination","").isEmpty()) {
                                                            editor.putString("latitude", mCenterLatLong.latitude + "");
                                                            editor.putString("longitude", mCenterLatLong.longitude + "");
                                                            editor.commit();
                                                            Log.d("latitudelongitudehome11", mCenterLatLong.latitude + "\n" + mCenterLatLong.longitude);
                                                        }
                                                        try {
                                                            adress_string= get_data(mCenterLatLong.latitude, mCenterLatLong.longitude);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                            adress_string="";
                                                        }
                                                        if (context instanceof HomeActivity)
                                                        ((Activity) context).runOnUiThread(new Runnable() {
                                                            public void run() {
                                                        hometext.setText(adress_string);
                                                            }});

                                                    }
                                                    sleep(60000);
//}




                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Thread.currentThread().interrupt();
                                }

                            }
                        };

                       thread.start();
                        if (context instanceof HomeActivity)
                        new Handler_Cars(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });}
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }
    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG,"yes");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        if (context instanceof HomeActivity)
            TestModel.setGoogleApiClienthome(mGoogleApiClient);
       else if (context instanceof RideLater_Activity)
            TestModel.setGoogleApiClientridelater(mGoogleApiClient);
       else if (context instanceof RideNow_Activity) {
           TestModel.setGoogleApiClientbooking(mGoogleApiClient);
            }
        else if (context instanceof PlacesActivity ||context instanceof CurrentActivity ||context instanceof Rate_Captain_activity) {
            TestModel.setGoogleApiplacesac(mGoogleApiClient);
        }

        }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            if (context instanceof RideNow_Activity)
            {
                //where_i_ride(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            }
            else
                changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            if (! (context instanceof RideNow_Activity))
            {
                //where_i_ride(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                if (location != null)

                    changeMap(location);

            }

            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //////////////////////////

    public void changeMapsearch(Double lat, Double lang) {

        Log.d(TAG, "Reaching map" + mMap);


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(lat, lang);

            if(context instanceof RideNow_Activity){
                cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(16.5f).tilt(0).build();
            }
            else if (context instanceof CurrentActivity){
                Log.d("ok","ok");
                cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(19f).tilt(30).build();
            }else{
                cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(16.5f).tilt(30).build();}

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            if(context instanceof RideLater_Activity || context instanceof RideNow_Activity ||context instanceof Rate_Captain_activity)
                mMap.getUiSettings().setAllGesturesEnabled(false);//disable moving map


            //    mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            //  startIntentService(location);


        } else {
            Toast.makeText(context,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    ////////////////////////
    private void changeMap(Location location) {

        Log.d(TAG, "Reaching map" + mMap);


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            if(context instanceof RideNow_Activity){
                cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(16.5f).tilt(0).build();
            }else if (context instanceof CurrentActivity){
                cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(16f).tilt(30).build();
            }
            else{
             cameraPosition = new CameraPosition.Builder()
                  .target(latLong).zoom(16f).tilt(30).build();
                   }

            mMap.setMyLocationEnabled(true);
           mMap.getUiSettings().setMyLocationButtonEnabled(false);

            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            if(context instanceof RideLater_Activity || context instanceof RideNow_Activity || context instanceof Rate_Captain_activity)
            mMap.getUiSettings().setAllGesturesEnabled(false);//disable moving map




        } else {
            Toast.makeText(context,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }
    public String get_data(Double latitude, Double longitude) throws IOException {

        try {
            if (latitude==0.0 && longitude==0.0)
                Log.d("logit",latitude+"\n"+longitude+"");
            if(isconnected()) {
                Log.d("logit",latitude+"\n"+longitude+"fdr");
                List<Address> addresses;
                Locale locale= new Locale("ar");
                Geocoder geocoder = new Geocoder(context, locale);

                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
             //   Log.d("checkfinaldata", addresses + "\n"+Locale.getDefault());
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getAddressLine(3);
                String state = addresses.get(0).getAddressLine(1) + "\t" + addresses.get(0).getAddressLine(2);
                String country = addresses.get(0).getCountryName();
                String knownName = addresses.get(0).getFeatureName();
                if (context instanceof HomeActivity && sharedPreferences.getString("booking_distination","").isEmpty())
                editor.putString("streetnamex",address + "\t" );
                else if (sharedPreferences.getString("booking_distination","").equals("booking_distination")
                                || context instanceof PlacesActivity)
                {
                    editor.putString("placenamex", address + "\t");
                    editor.putString("lastlat", latitude + "");
                    editor.putString("lastlong", longitude + "");
                    Log.d("checkfinaldataxxx", "lllllllllllllllllllllll");
                }
                editor.commit();
                String x=sharedPreferences.getString("streetnamex","ss");
              //  if (TestModel.getHome_text()!=null)
                TestModel.setGoogleMap(mMap);

                Log.d("checkfinaldataxxx",sharedPreferences.getString("booking_distination","")+"55");
                Log.d("checkfinaldata",sharedPreferences.getString("placenamex",""));


                return address;
            }

        }catch (Exception e){
            Log.d("error",e.toString());
            e.printStackTrace();
        }
    return "";}
    public boolean isconnected(){

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }
    public void centerMapOnMyLocation() {

       // mMap.setMyLocationEnabled(true);

        try {
            Location location = mMap.getMyLocation();
            Log.d("locationlocation",location+"");
            if (location != null) {
                changeMap(location);
                Log.d("locationlocation",location+"");
            }
        }
        catch (Exception e)
        {
            new GPSTracker(context,this).canGetLocation();
        }
    }

    public void claer_map(){
        mMap.clear();
    }

    public void get_cars(Double latitude ,Double longitude,Boolean enter) {
        Log.d("get_cars","here");
        if (context instanceof RideNow_Activity)
        if (car_marker!=null)
            car_marker.remove();
        else
        is_carmarke=true;
//         if (car_marker!=null)
//            car_marker.remove();
        BitmapDescriptor caricon = BitmapDescriptorFactory.fromResource(R.mipmap.car_icon);
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(caricon);
        markerOptions.draggable(true);


        car_marker = mMap.addMarker(markerOptions);

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        if (enter)
        {
            is_carmarke=false;
            getscreenmap(car_marker,1);
        }

        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
    }

    public void get_places(Double latitude ,Double longitude, String name , int resurce) {
        Log.d("get_places","here");


        if (context instanceof PlacesActivity)
        { mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        }
        BitmapDescriptor caricon = BitmapDescriptorFactory.fromResource(resurce);
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(name);
        markerOptions.icon(caricon);
        markerOptions.draggable(true);

        car_marker = mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));



    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        Log.d("bnbnbnbn", marker.getPosition()+"");
        return true;
    }


    public void where_i_ride(Double latitude ,Double longitude) {
        if (marker!=null)
            marker.remove();
        else
        is_marker=true;

        BitmapDescriptor caricon = BitmapDescriptorFactory.fromResource(R.mipmap.pe_loc);
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("");
        markerOptions.icon(caricon);
        markerOptions.draggable(true);


         marker = mMap.addMarker(markerOptions);
       // changeMapsearch(latitude,longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
       mMap.animateCamera(CameraUpdateFactory.zoomTo(16.5f));


        if (context instanceof RideNow_Activity)
        {
            is_marker=false;
            getscreenmap(marker,0);
        }


        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
    }

    public void getscreenmap (Marker marker1,int postion){
        markers=new ArrayList<>();
       try
       {
           markers.set(postion,marker1);
        }
        catch (Exception e)
        {
            markers.add(postion,marker1);
        }

        Log.d("markers",markers.size()+"");
        if (markers.size()>=2) {
            GetScreenSize getScreenSize = new GetScreenSize(context);
            getScreenSize.getImageSize();
            int margin=(int) context.getResources().getDimension(R.dimen.text_margin30);
            final int height = getScreenSize.getHeight()-margin;
            final int width = getScreenSize.getWidth()-margin;
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();

             for (Marker marker:markers) {
                builder.include(marker.getPosition());
            }
//                    final LatLngBounds bounds = builder.build();
//
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    final float scale = context.getResources().getDisplayMetrics().scaledDensity;
                    float mTextSizeP = (float) ( context.getResources().getDimensionPixelSize(R.dimen.padding_2) / scale);
                    Log.d("dimensionss",mTextSizeP+"");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width,
                            (int) (height / mTextSizeP),100 ));
                }
            });
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

        ((PlacesActivity) context).zoom();
    }


    public void drawPrimaryLinePath( ArrayList<LatLng> listLocsToDraw ) {
        markers=new ArrayList<>();
        if (mMap == null) {
            Log.d("listLocsToDraw",listLocsToDraw+"");
            return;
        }

        Log.d("listLocsToDraw",listLocsToDraw+"");
        if (listLocsToDraw != null) {
            if (listLocsToDraw.size() < 2) {
                return;
            }
            mMap.clear();
            PolylineOptions options = new PolylineOptions();
            options.color(Color.parseColor("#CC0000FF"));

            options.width(8);
            if (context instanceof Rate_Captain_activity||context instanceof RideNow_Activity)
                options.width(4);
                options.visible(true);
            int index=0;
            for (LatLng locRecorded : listLocsToDraw) {
                if(index==listLocsToDraw.size()-1||index==0)
                {
                   // setmarker(new LatLng(locRecorded.latitude,
                       //    locRecorded.longitude),index);

                    if(index==0)
                    {
                        setmarker(new LatLng(locRecorded.latitude,
                                locRecorded.longitude),1);
                    }
                    else {
                        setmarker(new LatLng(locRecorded.latitude,
                                locRecorded.longitude),2);
                    }



                }
                index++;
                options.add(new LatLng(locRecorded.latitude,
                        locRecorded.longitude));

            }
            mMap.addPolyline(options);


        }
    }

















    public void setmarker(LatLng latLng,int position)
    {
      /*  BitmapDescriptor caricon = BitmapDescriptorFactory.fromResource(R.mipmap.car_icon);

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.anchor(.5f,.5f).position(latLng);
        markerOptions.icon(caricon);
        markers.add(mMap.addMarker(markerOptions));

  try{      markers.get(0).setIcon(caricon);}catch (Exception e){e.printStackTrace();}





        getscreenmap(markers.get(0),1);

*/
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.anchor(.5f,.5f)
                .position(latLng);
        if(mMap!=null)
        {
            markers.add(mMap.addMarker(markerOptions));
            switch (position) {
                case 0:
                    markers.get(position).setVisible(false);
                    break;
                case 1:
                    Log.d("car", "car");
                    try {
                        markers.get(0).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car_icon));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Log.d("pss", "pss");
                    try {
                        markers.get(1).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.pe_loc));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


            }}


        }



}

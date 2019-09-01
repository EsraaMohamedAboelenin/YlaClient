package solversteam.familycab.Nav.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import solversteam.familycab.Activities.GooglePlacesAutocompleteActivity;
import solversteam.familycab.Activities.RideLater_Activity;
import solversteam.familycab.Activities.RideNow_Activity;
import solversteam.familycab.Adapter.CarsRVAdapter;
import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Cars_items;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;
import solversteam.familycab.Util.GPSTracker;
import solversteam.familycab.Util.LocaleHelper;

public class HomeActivity extends BaseActivity {


    private ImageView mNavTv;
    private ImageView map_search;
    private ImageView  mGps_loc ;

    private TextView mTitleTv,locationMarkertext ;

    private TextView mAdressTv ;
    private TextView going_to_edittxt;
    private CardView goingto_card;

    private Typeface mBaseTextFont;


    private Handler handler; // declared before onCreate
    private Runnable myRunnable;

    private Button mNow ;
    private Button mLater ;
    private Button confirm_pickup ;
    private LinearLayout buttons_container;

    private RecyclerView linear_taps;
    private RecyclerView cars_items;
    private LinearLayoutManager linearLayoutManager;
    private CarsRVAdapter adapter;
    private ArrayList<Cars_items> car_list,car_list2;


    private LatLng mSelectedLatlng = null;
    private MapView mapView;


    private int counter;


    private Bundle savedInstance;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String iso_code,language,country;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;


    private GPSTracker mGps;

      private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    private LatLng mCenterLatLong;
    private Thread thread;
    private Set_Map set_map;
    private SupportMapFragment mapFragment;
    private String where_i_come,passengerID;
    private Handler handler_premissin,changemap_handler,tabs_handler;
    private Runnable runnable,changemap_runnable,tabs_runnable ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance=savedInstanceState;
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");
        Log.d("countryy",country);
        passengerID=sharedPreferences.getString("passengerID","1");
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this,iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        setContentView(R.layout.activity_home);

         mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
        toolbar=(Toolbar)findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // to know if passenger in trip or not , and create slide menu items
        TestModel.isPassengerInTrip(this,null,"",passengerID);

       // initialize views
        initialization_views();

        // cars category
        taps_creation();// create  recycle of cars items in home activity


        // handler to set map if permission enable
        handler_premissin = new Handler();
        runnable = new Runnable() {
            public void run() {

                if (checkLocationPermission()) {
                    try {
                        set_map=new Set_Map(HomeActivity.this,mapFragment,mAdressTv);

                        TestModel.setSet_map(set_map); // save object of set_map
                        TestModel.getGoogleApiClienthome().connect();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    handler_premissin.removeCallbacks(this);
                    handler_premissin.removeCallbacksAndMessages(null);
                }
                else
                    handler_premissin.postDelayed(this, 1000);
            }
        };
        runnable.run();


        try{
            // to know what is activity  intent to home activity , to do some of operations
            // if booking , or booking_distination >> home activity will have 1 button to choose loction
            // else home activty have 2 buttons  ride now , ride later
            where_i_come=getIntent().getExtras().getString("whereIcome");
        }catch (Exception e){
            where_i_come="0";
        }
        if(where_i_come.equals("splash"))
        {
            confirm_pickup.setVisibility(View.GONE);
            buttons_container.setVisibility(View.VISIBLE);
        }
        else if (where_i_come.equals("booking")||where_i_come.equals("booking_distination"))
        {
            buttons_container.setVisibility(View.GONE);
            confirm_pickup.setVisibility(View.VISIBLE);
            mNavTv.setVisibility(View.GONE);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (where_i_come.equals("booking_distination")) {
            changemap_handler = new Handler();
            changemap_runnable = new Runnable() {
                public void run() {

                    if (set_map.mMap != null) {

                        set_map.changeMapsearch(getIntent().getExtras().getDouble("dist_Latitude"),
                                getIntent().getExtras().getDouble("dist_Longitude"));
                        changemap_handler.removeCallbacks(this);
                        changemap_handler.removeCallbacksAndMessages(null);
                    } else
                        changemap_handler.postDelayed(this, 1000);
                }
            };
            changemap_runnable.run();
        }

        }

        // in case where_i_come.equals(booking or booking_distination
        confirm_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (where_i_come.equals("booking")) {
                    editor.putString("comefrom", "home");
                    editor.commit();
                   // onBackPressed();


                    Intent i= new Intent(HomeActivity.this, RideNow_Activity.class);
                    i.putExtra("whereIcome","home_now");
                    // TestModel.setOn_ride(false);
                    TestModel.updateShared("onride",HomeActivity.this,"false");
                    startActivity(i);

                }
                else if (where_i_come.equals("booking_distination"))
                {
                    editor.putString("backfrom", "home");
                    editor.putString("dist_Latitude", sharedPreferences.getString("lastlat", "0.0"));
                    editor.putString("dist_Longitude", sharedPreferences.getString("lastlong", "0.0"));
                    editor.putString("dist_name_location", sharedPreferences.getString("placenamex", "0.0"));
                    editor.commit();
                    onBackPressed();
                }



            }
        });

        // search
        map_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(HomeActivity.this,GooglePlacesAutocompleteActivity.class);
                i.putExtra("from","home");
                startActivityForResult(i,1);
            }
        });

        // get current location on map
        mGps_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_map.centerMapOnMyLocation();

            }
        });


        // booking car now
        mNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("later","now");
                Intent i= new Intent(HomeActivity.this, RideNow_Activity.class);
                i.putExtra("whereIcome","home_now");

                startActivity(i);



             }
        });

        // booking car later
        mLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("later","later");
                Intent i= new Intent(HomeActivity.this, RideLater_Activity.class);
                i.putExtra("whereIcome","home_later");
                startActivity(i);

            }
        });
    }

    private void initialization_views() {

        mTitleTv = (TextView) toolbar.findViewById(R.id.tv_home_activity_title);

        mNavTv = (ImageView) toolbar.findViewById(R.id.menu_toolbar);//menue toggle


        mAdressTv = (TextView) findViewById(R.id.map_address_Tv);


        locationMarkertext=(TextView)findViewById(R.id.locationMarkertext);
        TestModel.setmAdressTv(locationMarkertext);
        TestModel.setHome_text(mAdressTv);

        mNow = (Button) findViewById(R.id.ride_now);
        mLater = (Button) findViewById(R.id.ride_later);
        mNow.setText(R.string.ride_nw_title);
        mLater.setText(R.string.ride_later_title);

        TestModel.setRide_later(mLater);
        TestModel.setRide_now(mNow);


        mGps_loc = (ImageView) findViewById(R.id.map_location_btn);
        map_search=(ImageView)findViewById(R.id.map_location_search);

        going_to_edittxt=(TextView)findViewById(R.id.going_to_edittxt);
        going_to_edittxt.setText(R.string.going_to);

        goingto_card=(CardView)findViewById(R.id.home_cv);
        buttons_container=(LinearLayout)findViewById(R.id.home_container_buttons);
        linear_taps=(RecyclerView) findViewById(R.id.tab_layout) ;
        confirm_pickup=(Button)findViewById(R.id.confrim_pickup);

    }



    protected void taps_creation(){
        Log.d("state",sharedPreferences.getString("stateID","14"));
        counter =0;
        car_list=new ArrayList<>();
        car_list2=new ArrayList<>();
        Connection connection5 = new Connection(this, "/CarCategory/GetAllCarCategories/"+country+"/"+sharedPreferences.getString("stateID","14")+"/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            public Runnable runnable2;

            @Override
            public void data(String str) throws JSONException {
                try {
                    linear_taps.setVisibility(View.VISIBLE);
                    Log.d("GetAllCarCategories", str + "\n");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("CarCategories");
                    Log.d("GetAllCarCategories2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        String img = jsonObject1.getString("IconURL");

                        JSONArray jsonArray2 = jsonObject1.getJSONArray("CarCategoriesBookingType");
                        for (int position1 = 0; position1 < jsonArray2.length(); position1++) {

                            JSONObject jsonObject2 = jsonArray2.getJSONObject(position1);
                            String bookig_type_number = jsonObject2.getString("BookingTypeNumber");

                            JSONArray jsonArray3 = jsonObject2.getJSONArray("FeeFactors");

                            for (int position2 = 0; position2 < jsonArray3.length(); position2++) {

                                JSONObject jsonObject3 = jsonArray3.getJSONObject(position2);
                                Cars_items cars_model = new Cars_items(jsonObject3.getString("InitialFactor"),
                                        jsonObject3.getString("MovingFactorPerMinute"),jsonObject3.getString("MovingKMFactor"),
                                        jsonObject3.getString("MovingMinSpeed"),jsonObject3.getString("MovingMaxSpeed"),
                                        jsonObject3.getString("RushHourFactor"),jsonObject3.getString("WaitingFactorPerMinute"),
                                        jsonObject3.getString("MinCharge"),jsonObject3.getString("WaitingMinSpeed"),
                                        jsonObject3.getString("WaitingMaxSpeed"),jsonObject3.getString("PeakTimeAmount"),
                                        jsonObject3.getString("AirPortFactor"),jsonObject3.getString("CancelationCharge"),
                                        jsonObject3.getString("AirportAmount"),jsonObject3.getString("MinCancelationTime"),
                                        jsonObject3.getString("MaxCancelationTime"),jsonObject2.getString("BookingTypeNumber")
                                        ,jsonObject3.getString("BookingTypeID"),
                                        jsonObject3.getString("CarCategoryID"),name,img);
                                car_list2.add(cars_model);
                                TestModel.setCars_caegory(car_list2);

                            }

                        }
                        car_list.add(new Cars_items(name,id,img));
                    }


                    adapter = new CarsRVAdapter(HomeActivity.this,car_list);
                    linearLayoutManager=new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);


                                Log.d("heleekek","dkkncnnvnv");
                                linear_taps.setLayoutManager( linearLayoutManager);
                                linear_taps.setAdapter(adapter);
                                adapter.notifyDataSetChanged();


                    Log.d("car_list",car_list.size()+"\n"+car_list.get(0).getImg());
                    editor.putString("car_type_id",car_list.get(0).getId());
                    editor.putString("bookingtype_id_later", car_list2.get(1).getBooking_type_id());
                    editor.putString("bookingtype_id_now", car_list2.get(0).getBooking_type_id());
                    editor.commit();
                    if (car_list.size()>0)
                    {
                        Log.d("car_listcmv",car_list2.get(0).getBooking_type_number()+"\n"+
                                car_list2.get(1).getBooking_type_number());
                       if (car_list2.get(0).getBooking_type_number().equals("1"))
                           mNow.setVisibility(View.VISIBLE);
                        else
                           mNow.setVisibility(View.INVISIBLE);

                        if (car_list2.get(1).getBooking_type_number().equals("2"))
                            mLater.setVisibility(View.VISIBLE);
                        else
                            mLater.setVisibility(View.INVISIBLE);
                    }

                    Log.d("herecar","here");




                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_gender",e.toString());
                    mLater.setVisibility(View.INVISIBLE);
                    mNow.setVisibility(View.INVISIBLE);
                    editor.putString("car_type_id",-1+"").commit();

                    Toast.makeText(HomeActivity.this,R.string.msg_category,Toast.LENGTH_LONG).show();
                   // editor.putString("stateID","14").commit();
                 //  taps_creation();
                }
            }});


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GooglePlacesAutocompleteActivity.RESULT_OK)
        {
            if (!data.getStringExtra("stateID").equals(sharedPreferences.getString("stateID","14")))
            {
                taps_creation();
            }
            Double dist_Latitude=Double.parseDouble(data.getStringExtra("dist_Latitude"));
            Double dist_Longitude=Double.parseDouble(data.getStringExtra("dist_Longitude"));
            set_map.changeMapsearch(dist_Latitude,dist_Longitude);

        }
        // when in GooglePlacesAutocompleteActivity back
        if (resultCode == GooglePlacesAutocompleteActivity.RESULT_CANCELED)
        {

            if (!data.getStringExtra("stateID").equals(sharedPreferences.getString("stateID","14")))
            {
                taps_creation();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("closehers","close");
        close_thread();
    }
    private void close_thread() {
        try{
            set_map.thread.interrupt();
            TestModel.getTimer().cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
       // super.onBackPressed();

    }

    //////////////////////////////////////////////////////////////////////////////
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }


            return false;
        } else {

             return true;
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 5:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.test);
                dialog.getWindow().setBackgroundDrawableResource(R.color.colorBgTransparent);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                LinearLayout relativeLayout=(LinearLayout) dialog.findViewById(R.id.rel_loder);
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // The user just touched the screen

                                dialog.dismiss();
                                break;
                            case MotionEvent.ACTION_UP:
                                // The touch just ended

                                break;
                        }

                        return false;
                    }
                });

                return dialog;
            default:
                return null;
        }
    }
}


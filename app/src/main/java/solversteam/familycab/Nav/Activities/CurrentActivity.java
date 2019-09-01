package solversteam.familycab.Nav.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;
import solversteam.familycab.Util.CancelOrder_dialog;
import solversteam.familycab.Util.GMapV2Direction;
import solversteam.familycab.Util.LocaleHelper;

public class CurrentActivity extends BaseActivity {

    public static CurrentActivity mActivity;
    private Typeface mBaseTextFont;
    private SupportMapFragment mapFragment;
    private Set_Map set_map;
    private ImageView carmarker,captain_img,call_captain,share_tracker;
    private TextView captain_name,captain_rating,toolbar_cancel,plate_num,car_model;
    private LinearLayout linearLayout,details_container;
    private RatingBar captain_raing_bar;
    private GMapV2Direction gMapV2Direction;
    private String language,passengerId,order_id;
    private String driverId,drivername,driverimage,driveremail,drivermobile,driverrate,orderid,DropOffLongtitude,DropOffLatitude,
    PickUpLatitude,PickUpLongtitude;
    private SharedPreferences sharedPreferences;
    private Handler changemap_handler,handler;
    private Runnable changemap_runnable,runnable;
        private String iso_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mActivity = this;
        sharedPreferences=getSharedPreferences("lang_and_count", MODE_PRIVATE);
        language=sharedPreferences.getString("langID","1");
        iso_code=sharedPreferences.getString("iso_code","ar");
        //to set language
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this,iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        setContentView(R.layout.activity_current);
        passengerId=sharedPreferences.getString("passengerID","6");



        try{        order_id=getIntent().getExtras().getString("order_id");}catch (Exception e){


            e.printStackTrace();
        }


        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
        TestModel.isPassengerInTrip(this,null,"",passengerId);

        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        set_map= new Set_Map(CurrentActivity.this,mapFragment,null);
        carmarker=(ImageView)(findViewById(R.id.map)).findViewById(R.id.imageMarker);
        carmarker.setImageResource(R.mipmap.car_icon);
        captain_img=(ImageView)findViewById(R.id.captain_img);
        call_captain=(ImageView)findViewById(R.id.call_captain);
        share_tracker=(ImageView)findViewById(R.id.share_track);
        captain_name=(TextView)findViewById(R.id.captain_name);
        captain_rating=(TextView)findViewById(R.id.captain_rating_txt);
        car_model=(TextView)findViewById(R.id.car_model);
        plate_num=(TextView)findViewById(R.id.platenumber);
        captain_raing_bar=(RatingBar)findViewById(R.id.rating_captain_rating_bar);
        linearLayout=(LinearLayout)findViewById(R.id.container);
        details_container=(LinearLayout)findViewById(R.id.details_container);
        linearLayout.setVisibility(View.GONE);
        details_container.setVisibility(View.GONE);
        toolbar_cancel=(TextView)toolbar.findViewById(R.id.tv_home_save);
        toolbar_cancel.setText(R.string.mdtp_cancel);
        toolbar_cancel.setVisibility(View.INVISIBLE);
        mNavTv.setVisibility(View.INVISIBLE);
        set_imge_size();
        changemap_handler = new Handler();
        changemap_runnable = new Runnable() {
            public void run() {

                track_driver();



                changemap_handler.postDelayed(this, 5000);
            }
        };
        changemap_handler.post(changemap_runnable);





        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
               // TestModel.isPassengerInTrip(CurrentActivity.this,null,"",passengerId);
                handler.postDelayed(runnable,5000);
            }
        };
        handler.postDelayed(runnable,5000);


        toolbar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelOrder_dialog cancelOrder_dialog = new CancelOrder_dialog(order_id, CurrentActivity.this);
                cancelOrder_dialog.show_dialog();
            }
        });
        gMapV2Direction=new GMapV2Direction(this);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_details();
            }
        });
        call_captain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +drivermobile ));
                startActivity(intent);
            }
        });
        share_tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destination="";
                if (DropOffLatitude.equals("0.0"))
                    destination = "&daddr=" + DropOffLatitude + "," + DropOffLongtitude;

                String uri = "http://maps.google.com/maps?saddr=" +PickUpLatitude+","+PickUpLongtitude+destination;

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String ShareSub = "Here is my location \n";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri +"\n"+"captain info : \n"+
                        "captain name : "+captain_name.getText().toString()+"\n"+
                        "car info : "+plate_num.getText().toString()+"\t"+
                        car_model.getText().toString()+"\n"+"captain mobile : "+drivermobile);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    private String up_down="down";
    private void get_details() {
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slidup2);
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slidedown2);

        if (up_down.equals("down"))
        {
            linearLayout.startAnimation(slide_up);

            details_container.setVisibility(View.VISIBLE);
            up_down="up";
        }else {
            up_down="down";
            slide_down.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    details_container.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            linearLayout.startAnimation(slide_down);
        }


    }

    private void set_imge_size() {

        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);  // the values are saved in the screenSize object
        final int width = screenSize.x;
        final int height = screenSize.y;
        ViewGroup.LayoutParams params
                = captain_img.getLayoutParams();
        params.height = (int) (width / 8);
        params.width = (int) (width / 8);
        captain_img.setLayoutParams(params);
    }
    @Override
    protected void onStart() {
        super.onStart();
        try {
            TestModel.getGoogleApiplacesac().connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void track_driver() {





        Connection connection = new Connection(this, "/Order/GetPassengerCurrentRideDetails/"+passengerId+"/"+language, "Get");
        connection.reset();

        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("GetLatestRideTracking", str);

                try {

                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject jsonObject2 =  jsonObject.getJSONObject("RideDetails");
                    set_map.changeMapsearch(jsonObject2.getDouble("RideTrackingLatitude"),
                            jsonObject2.getDouble("RideTrackingLongitude"));

                    handler.removeCallbacks(runnable);
                    driverId=jsonObject2.getString("DriverID");
                    drivername=jsonObject2.getString("DriverFullName");
                    drivermobile=jsonObject2.getString("DriverMobile");
                    driveremail=jsonObject2.getString("DriverEmail");
                    driverimage=jsonObject2.getString("DriverImageURL");
                    driverrate=jsonObject2.getString("DriverRatingPointsAverage");
                    orderid=jsonObject2.getString("OrderID");
                    captain_name.setText(drivername);
                    car_model.setText(jsonObject2.getString("CarBrandName"));
                    plate_num.setText(jsonObject2.getString("CarPlateNumber"));
                    Picasso.with(CurrentActivity.this).load(driverimage).
                            placeholder(R.drawable.prof_placeholder).into(captain_img);
                    captain_raing_bar.setRating(Float.parseFloat(driverrate));
                    captain_rating.setText(driverrate);
                    linearLayout.setVisibility(View.VISIBLE);
                    toolbar_cancel.setVisibility(View.INVISIBLE);
                    mNavTv.setVisibility(View.VISIBLE);
                    TestModel.setArrive(false);
                 //   TestModel.isPassengerInTrip(CurrentActivity.this,null,"",passengerId);
                    PickUpLatitude=jsonObject2.getString("RideTrackingLatitude");
                    PickUpLongtitude=jsonObject2.getString("RideTrackingLongitude");
                    DropOffLatitude=jsonObject2.getString("DropOffLatitude");
                    DropOffLongtitude=jsonObject2.getString("DropOffLongtitude");

                } catch (Exception e) {
                 toolbar_cancel.setVisibility(View.VISIBLE);
                  mNavTv.setVisibility(View.INVISIBLE);
                    TestModel.setArrive(true);

                    e.printStackTrace();
                }

            }
        });

    }
    private void get_trip_data() {
        LatLng source= new LatLng(Float.parseFloat(PickUpLatitude),Float.parseFloat(PickUpLongtitude));
        LatLng distination= new LatLng(Float.parseFloat(DropOffLatitude),Float.parseFloat(DropOffLongtitude));
        gMapV2Direction.getDocument(source ,distination,true,set_map,null,null);
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            changemap_handler.removeCallbacks(changemap_runnable);
            changemap_handler.removeCallbacksAndMessages(null);
        }catch (Exception e){}
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            changemap_handler.postDelayed(changemap_runnable, 1000);
        }catch (Exception e){}
    }


    @Override
    public void onBackPressed() {
       finish();
    }
}

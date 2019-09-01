package solversteam.familycab.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Locale;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;
import solversteam.familycab.Util.GMapV2Direction;
import solversteam.familycab.Util.LocaleHelper;
import solversteam.familycab.Util.NotificationUtils;

public class Rate_Captain_activity extends AppCompatActivity {

    public static Rate_Captain_activity mActivity;
    private Typeface mBaseTextFont;
    private TextView trip_date_text,captain_name_text,trip_cost_text,payment_type_text,toolbar_text;
    private String trip_date_string,captain_name_string,trip_cost_string,payment_type_string,captainimg_url,passengerID,currancy,
    driverid,datetrip,sourcelat,sourcelong,destlat,destlong;
    private ImageView captain_image;
    private RatingBar ratingBar;
    private Button submit_button;
    private Set_Map set_map;
    private SupportMapFragment mapFragment;
    private String rate_captain;
    private GMapV2Direction gMapV2Direction;
    private SharedPreferences sharedPreferences;
    private NotificationUtils notificationUtils;
    private SharedPreferences.Editor editor;
    private String iso_code,country,language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");

        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this,iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        setContentView(R.layout.ratting_driver_dialog);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
         // for make activity as dalog
        this.setFinishOnTouchOutside(false);
        mActivity = this;
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
        notificationUtils=new NotificationUtils(this);
        gMapV2Direction=new GMapV2Direction(this);
        sharedPreferences=getSharedPreferences("lang_and_count",MODE_PRIVATE);
        passengerID=sharedPreferences.getString("passengerID","1");

        try{
            captain_name_string=get_intent("captain_name");
            captainimg_url=get_intent("captain_image");
            trip_date_string=get_intent("endtime");
            trip_cost_string=get_intent("totalridefee");

            driverid=get_intent("driverid");
            datetrip=get_intent("ridedate");
            destlat=get_intent("destinationLat");
            destlong=get_intent("destinationLong");
            sourcelat=get_intent("sourceLat");
            sourcelong=get_intent("sourceLong");
            currancy=get_intent("currancy");
        }catch (Exception e){}
        set_views();
        get_trip_data();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("ratingbarr",rating+"");
               // rate_captain=Math.round(rating)+"";
                rate_captain=rating+"";

            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rate_captain!=null&&!rate_captain.equals("0.0")){
                Log.d("rate",rate_captain);
                    // request
                    add_rate();

                }else
                {

                    Toast.makeText(mActivity,R.string.msg,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String get_intent(String value) {
        return getIntent().getExtras().getString(value);
    }


    private void set_views() {

        trip_date_text=(TextView)findViewById(R.id.dialog_trip_date_text);
        captain_name_text=(TextView)findViewById(R.id.dialog_captain_name);
        trip_cost_text=(TextView)findViewById(R.id.dialog_trip_cost_text);
        payment_type_text=(TextView)findViewById(R.id.dialog_paymenttypae_text);
        captain_image=(ImageView)findViewById(R.id.dialog_captain_image);
        ratingBar=(RatingBar)findViewById(R.id.rating_bar_captain);

        submit_button=(Button)findViewById(R.id.submit_button);
        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        set_map=new Set_Map(this,mapFragment,null);
        ratingBar.setFocusable(false);
        ratingBar.setIsIndicator(false);

        trip_cost_text.setText(trip_cost_string+" "+currancy);
        captain_name_text.setText(captain_name_string);
        trip_date_text.setText(trip_date_string);
        captain_image.setVisibility(View.VISIBLE);

        try {
            Picasso.with(this).load(captainimg_url).placeholder(R.drawable.prof_placeholder).into(captain_image);
        }catch (Exception e){
            Picasso.with(this).load(R.drawable.prof_placeholder).placeholder(R.drawable.prof_placeholder).into(captain_image);

            e.printStackTrace();

        }

    }
    private void get_trip_data() {
        LatLng source= new LatLng(Float.parseFloat(sourcelat),Float.parseFloat(sourcelong));
        LatLng distination= new LatLng(Float.parseFloat(destlat),Float.parseFloat(destlong));
        gMapV2Direction.getDocument(source ,distination,true,set_map,null,null);
    }
    @Override
    protected void onStart() {
        super.onStart();

        TestModel.getGoogleApiplacesac().connect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        set_map.centerMapOnMyLocation();
    }
    private void add_rate() {
        try {
            Connection connection = new Connection(this, "/Order/addRating", "Post");
            connection.reset();
            connection.addParmmter("ReachUsersID",driverid);
            connection.addParmmter("ReachUserEvaluated",passengerID);
            connection.addParmmter("RideID",getIntent().getExtras().getString("orderid"));
            connection.addParmmter("RatingPointsID",rate_captain);///
            connection.addParmmter("RatingTypeID", "1");///
            connection.addParmmter("Date",datetrip);///
            connection.addParmmter("Comment","");///

            connection.Connect(new Connection.Result() {
                @Override
                public void data(String jsonResponse) throws JSONException {

                    Log.d("Login_loginResponse", jsonResponse);
                    notificationUtils.clearNotifications(Rate_Captain_activity.this);
                    Intent intent =new Intent(Rate_Captain_activity.this,HomeActivity.class);

                    startActivity(intent);
                    ActivityCompat.finishAffinity(Rate_Captain_activity.this);
                }
            });

        } catch (Exception e) {e.printStackTrace();  }
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(Rate_Captain_activity.this,HomeActivity.class);

        startActivity(intent);
        ActivityCompat.finishAffinity(Rate_Captain_activity.this);
    }
}

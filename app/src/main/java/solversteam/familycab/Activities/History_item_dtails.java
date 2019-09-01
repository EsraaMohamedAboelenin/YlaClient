package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.R;


/**
 * Created by mosta on 5/14/2017.
 */

public class History_item_dtails extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title,date_text,from_address,from_address_details,
            where_address,where_address_details,car_type_text,duration_address,distance_text,wait_time_text
            ,fare_text,payment_type_text,catain_name_text,car_text,car_plate_text;
    ImageView toolbar_nav_img;
    private String rideID,language;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_item_details);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        language=sharedPreferences.getString("langID","1");
        toolbar=(Toolbar)findViewById(R.id.include);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title.setText(R.string.myride);
        rideID=getIntent().getExtras().getString("order_id");
        set_views();
        set_data();



    }

    private void set_data() {
        Connection connection5 = new Connection(this, "/Order/GetRideDetails/"+rideID+"/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Log.d("GetAllLanguage", str + "");
                    JSONObject jsonObject = new JSONObject(str);
                    String date="";

                    JSONObject jsonObject1 = jsonObject.getJSONObject("RideDetails");
                    try{
                        date= jsonObject1.getString("MobDate")+" "+jsonObject1.getString("StartTime");
                        SimpleDateFormat spf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        Date newDate= spf.parse(date);
                        spf= new  SimpleDateFormat("dd MMM , hh:mm ") ;
                        date = spf.format(newDate);}catch (Exception e){
                        date=jsonObject1.getString("MobDate");
                    }
                    date_text.setText(date);
                    from_address.setText(jsonObject1.getString("PickupAddress"));
                    from_address_details.setText(jsonObject1.getString("PickupAddress"));
                    where_address.setText(jsonObject1.getString("DropoffAddress"));
                    where_address_details.setText(jsonObject1.getString("DropoffAddress"));
                    car_type_text.setText(jsonObject1.getString("CarCategoryLatName"));
                    payment_type_text.setText(jsonObject1.getString("PaymentTypeLatName"));
                    duration_address.setText(jsonObject1.getString("RideDuration")+" "+getResources().getString(R.string.mint));
                    distance_text.setText(jsonObject1.getString("KMNumber")+" "+getResources().getString(R.string.km));
                    car_type_text.setText(jsonObject1.getString("CarCategoryARName"));
                    wait_time_text.setText(jsonObject1.getString("InitialWaitingTime")+" "+getResources().getString(R.string.mint));
                    catain_name_text.setText(jsonObject1.getString("DriverLatFullName"));
                    car_plate_text.setText(jsonObject1.getString("CarARPlateNumber"));
                    car_text.setText(jsonObject1.getString("CarBrandARName"));
                    fare_text.setText(jsonObject1.getString("TotalRideFee")+" "+jsonObject1.getString("CurrencyName"));
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});
    }

    private void set_views() {
        date_text=(TextView)findViewById(R.id.history_date);
        from_address=(TextView)findViewById(R.id.history_iam_here_address);
        from_address_details=(TextView)findViewById(R.id.history_iam_here_address_details);
        where_address=(TextView)findViewById(R.id.history_where_address);
        where_address_details=(TextView)findViewById(R.id.history_where_address_details);
        car_type_text=(TextView)findViewById(R.id.car_type_txt_item_details);
        duration_address=(TextView)findViewById(R.id.duration_txt_item_details);
        distance_text=(TextView)findViewById(R.id.distance_txt_item_details);
        wait_time_text=(TextView)findViewById(R.id.wait_time_txt_item_details);
        fare_text=(TextView)findViewById(R.id.fare_txt_item_details);
        payment_type_text=(TextView)findViewById(R.id.payment_txt_item_details);
        catain_name_text=(TextView)findViewById(R.id.captain_name_txt_item_details);
        car_text=(TextView)findViewById(R.id.car_txt_item_details);
        car_plate_text=(TextView)findViewById(R.id.plate_no_txt_item_details);
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
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



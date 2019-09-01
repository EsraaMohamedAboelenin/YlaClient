
package solversteam.familycab.Activities;


import solversteam.familycab.Util.GMapV2Direction;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Dialogs.Captain_arrive_notification;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.CurrentActivity;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.Nav.Activities.Wallet;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;
import solversteam.familycab.Util.CancelOrder_dialog;
import solversteam.familycab.Util.LocaleHelper;

/**
 * Created by mosta on 5/18/2017.
 */

public class RideNow_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private Document docm;
    private ImageView toolbar_nav_img;
    private GMapV2Direction gMapV2Direction;
    private TextView toolbar_title,toolbar_cancel,ride_from,payment_type,drop_off,
            promo_code,bigtxt,ride_to,add_details,captain_name,captian_rate,car_color,car_model,car_plate_number,estimatefair,estimatefairnote;
    private ImageView captain_image;
    private Button yalla_button,contact_button,share_tracker_captain,call_captain,send_email,cancel_dialog;
    private Set_Map set;
    private String where_i_come,mobile_number,Email,BookingTypeNumber;
    private SharedPreferences sharedPreferences;
    private Double latitude,longitude,dist_Longitude,dist_Latitude;
    private Double latitude_ride,longitude_ride;
    private String dist_name_location;
    private SharedPreferences.Editor editor;
    private LinearLayout container_ride_to_booking,container_of_bigtxt,save_container_after_booktrip,save_container_before_booktrip,container_of_buttons;
    private RatingBar ratingBar;
    private String orderDate,orderTime,language,driver_id="";

    private String whereIcome,orderid="";
    private Dialog dialog,dialog_loading;
    private Boolean intrip=false;
    private Handler handler,handler_premissin;
    private Runnable runnable;
    private boolean lock=false;
    private LinearLayout arrival_time;
    private TextView arrival_time_txt;
    private String iso_code,country,promo_code_string;
    private boolean on_ride=false;
    boolean valid=true;
    private String pickup_address,dropoff_address,payment_type_string;

    private Dialog loading,search_driver_dialog;
    private String order_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);

        promo_code_string="";



        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");
        sharedPreferences=getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        whereIcome=getIntent().getExtras().getString("whereIcome");

        //to set language
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this,iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        setContentView(R.layout.booking_now);

        gMapV2Direction=new GMapV2Direction(this);

        toolbar=(Toolbar)findViewById(R.id.include);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        toolbar_cancel=(TextView)toolbar.findViewById(R.id.tv_home_save);
        toolbar_cancel.setText(R.string.gps_Setting_No);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        TestModel.setRideNow_activity(this);



        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        set=  new Set_Map(this,mapFragment,null);


        //passenger_location
        latitude=Double.parseDouble(sharedPreferences.getString("latitude","0.0"));

        longitude=Double.parseDouble(sharedPreferences.getString("longitude","0.0"));




            //if driver is not in way to passenger

            if(!TestModel.isOn_ride()){

            latitude_ride=latitude;


            TestModel.saveToShared("lat",RideNow_Activity.this,latitude_ride+"");

            longitude_ride=longitude;

            TestModel.saveToShared("long",RideNow_Activity.this,longitude_ride+"");
       }




          //if driver is in way to passenger

        else if(TestModel.isOn_ride())
        {

         try{   latitude_ride=Double.parseDouble(TestModel.getFromShared("lat",RideNow_Activity.this) );
                longitude_ride=Double.parseDouble(TestModel.getFromShared("long",RideNow_Activity.this) );}
         catch (Exception e){


        e.printStackTrace();
         }

        }

        search_driver_dialog = new Dialog(this);
        search_driver_dialog.setContentView(R.layout.loading);
        //passenger_location_address
        ride_from=(TextView)findViewById(R.id.booking_address_ride_from);
        ride_from.setText(sharedPreferences.getString("streetnamex", "ss"));
        payment_type=(TextView)findViewById(R.id.booking_payment_type);

        drop_off=(TextView)findViewById(R.id.booking_dropoff);

        estimatefair=(TextView)findViewById(R.id.booking_estimatefair);
        estimatefairnote=(TextView)findViewById(R.id.booking_estimatefairnote);




               promo_code=(TextView)findViewById(R.id.booking_promo_code);
                //to get prom_code if passenger has promo_code or not
                get_promo_code();

                promo_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //to show dialog of promo_code to able to add promo_code
                add_promo_code();
            }
        });

        bigtxt=(TextView)findViewById(R.id.big_txt_show_date);
        add_details=(TextView)findViewById(R.id.add_details);
        ride_to=(TextView)findViewById(R.id.booking_address_ride_to) ;
        captain_name=(TextView)findViewById(R.id.captain_name) ;
        captian_rate=(TextView)findViewById(R.id.captain_rating_txt) ;
        car_model=(TextView)findViewById(R.id.captain_car_model) ;
        car_color=(TextView)findViewById(R.id.car_captain_color) ;
        car_plate_number=(TextView)findViewById(R.id.captain_car_plate_number) ;
        container_ride_to_booking=(LinearLayout)findViewById(R.id.container_ride_to_booking);
        container_of_bigtxt=(LinearLayout)findViewById(R.id.container_of_bigtxt);
        save_container_after_booktrip=(LinearLayout)findViewById(R.id.save_container_after_booktrip);
        save_container_before_booktrip=(LinearLayout)findViewById(R.id.save_container_before_booktrip);

        container_of_buttons=(LinearLayout)findViewById(R.id.container_of_buttons);
        yalla_button=(Button)findViewById(R.id.yalla_button);
        contact_button=(Button)findViewById(R.id.contact_captian_button);
        share_tracker_captain=(Button)findViewById(R.id.share_tracker_button);
        captain_image=(ImageView)findViewById(R.id.profile_image);
        ratingBar=(RatingBar)findViewById(R.id.rating_captain_rating_bar);

        dialog = new Dialog(RideNow_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.contac_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        call_captain=(Button)dialog.findViewById(R.id.call_button);
        send_email=(Button)dialog.findViewById(R.id.sendemail_button);
        cancel_dialog=(Button)dialog.findViewById(R.id.cancel_button);
        arrival_time=(LinearLayout) findViewById(R.id.arrival_time_container);
        arrival_time_txt=(TextView) findViewById(R.id.arrival_time);

        set_imge_size();

          //if come from places_activity
        if(whereIcome.equals("placesActivity")){
            toolbar_title.setText(R.string.booking);
            container_of_bigtxt.setVisibility(View.GONE);
            BookingTypeNumber="1";
            yalla_button.setVisibility(View.VISIBLE);
             drop_off.setEnabled(false);
             //to calclate estimate fair and duration time
             get_estimate();
        }

        // if come from home_activity
        if(whereIcome.equals("home_now")){
            toolbar_title.setText(R.string.booking);
            container_of_bigtxt.setVisibility(View.GONE);
            BookingTypeNumber="1";
            yalla_button.setVisibility(View.VISIBLE);

        }




        // if driver cancel the trip and wait to another driver accept it
        if(whereIcome.equals("cancel")){
            toolbar_title.setText(R.string.booking);
            container_of_bigtxt.setVisibility(View.GONE);
            BookingTypeNumber="1";
            yalla_button.setVisibility(View.VISIBLE);
            order_id=getIntent().getExtras().getString("order_id");
            //if passenger makes order and waiting driver to accept it
            TestModel.setOrder_back(true);

            show_seacrh_dialog();


            TestModel.search_driver_runnable=new Runnable() {
                @Override
                public void run() {


                    try {
                        Connection connection = new Connection(RideNow_Activity.this, "/Order/IsAnyDriverAcceptOrder/"+order_id+"/"+language, "Get");
                        connection.reset();


                        connection.Connect(new Connection.Result() {
                            @Override
                            public void data(String str) throws JSONException {

                                Log.d("Response", str);

                                try{
                                    JSONObject jsonObject2=new JSONObject(str);
                                    String check=jsonObject2.getString("IsAnyDriverAcceptOrder");
                                    String message=jsonObject2.getString("Message");
                                    if(check.equals("false")){
                                        search_driver_dialog.dismiss();
                                        Toast.makeText(RideNow_Activity.this,message,Toast.LENGTH_LONG).show();
                                        TestModel.setOrder_back(false);

                                    }


                                }catch (Exception e){
                                    e.printStackTrace();}







                            }
                        });

                    } catch (Exception e) {

                        e.printStackTrace();  }

















                }
            };

            TestModel.searach_driver_handler=new Handler() ;
            TestModel.searach_driver_handler.postDelayed(TestModel.search_driver_runnable, (long) (TestModel.getDriver_waiting()*1000));


        }





        //if come from ride_later activity
        else  if(whereIcome.equals("ride_later")){
             toolbar_title.setText(R.string.ride_later_title);
             orderDate= sharedPreferences.getString("OrderDate", "0");
             orderTime=sharedPreferences.getString("OrderTime", "0");
             BookingTypeNumber="2";
              yalla_button.setVisibility(View.VISIBLE);

        }












        //to get driver details if  come from splash and driver is on way to passenger
        else  if(whereIcome.equals("splash")){
            BookingTypeNumber="1";
            toolbar_title.setText(R.string.booking);
            container_of_bigtxt.setVisibility(View.GONE);
            yalla_button.setVisibility(View.VISIBLE);
            get_driver_details(TestModel.getFromShared("driver_id",RideNow_Activity.this),
                    TestModel.getFromShared("order_id",RideNow_Activity.this),
                    TestModel.getFromShared("user_image_url",RideNow_Activity.this),
                    TestModel.getFromShared("user_name",RideNow_Activity.this),
                    TestModel.getFromShared("user_mobile",RideNow_Activity.this),
                    TestModel.getFromShared("email",RideNow_Activity.this),
                    TestModel.getFromShared("rating",RideNow_Activity.this),
                    TestModel.getFromShared("car_color",RideNow_Activity.this),
                    TestModel.getFromShared("car_brand",RideNow_Activity.this)
                    ,TestModel.getFromShared("car_number",RideNow_Activity.this),false);







        }


        //to get driver details when driver accept order
          else  if(whereIcome.equals("firebase")){
            search_driver_dialog.dismiss();
            BookingTypeNumber="1";
            toolbar_title.setText(R.string.booking);
            container_of_bigtxt.setVisibility(View.GONE);


              get_driver_details(getIntent().getExtras().getString("driverid"),
                    getIntent().getExtras().getString("orderid"),
                    getIntent().getExtras().getString("userimageurl"),
                    getIntent().getExtras().getString("userfullname"),
                    getIntent().getExtras().getString("mobile"),
                    getIntent().getExtras().getString("email"),
                    getIntent().getExtras().getString("ratingpointsaverage"),
                    getIntent().getExtras().getString("carcolorname"),
                    getIntent().getExtras().getString("carbrandname"),
                    getIntent().getExtras().getString("carplatenumber"),false);





        }




        try{
        bigtxt.setText(sharedPreferences.getString("bigtxt","ll"));}
        catch (Exception e){e.printStackTrace();
        }

              //when passenger click on cash
               payment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(RideNow_Activity.this, Wallet.class);
                i.putExtra("comefrom","RideNow_Activity");
                startActivity(i);

            }
        });
        //when passenger click on drop_off _fair_estimate to set drop_off_location
        drop_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RideNow_Activity.this,GooglePlacesAutocompleteActivity.class);
                i.putExtra("from","booking");
                startActivityForResult(i,1);

            }
        });


       //when passenger click on ride_from to set pick_up location
        ride_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(RideNow_Activity.this,HomeActivity.class);
                i.putExtra("whereIcome","booking");
                startActivity(i);

            }
        });

        add_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdoalog(R.layout.add_details_dialog,"add_address");
            }
        });

        //when passenger bboking later and press on date box
        container_of_bigtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RideNow_Activity.this,RideLater_Activity.class);
                i.putExtra("class","booking");
                startActivity(i);
            }
        });


        //when passenger click on yalla to make order
        yalla_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        if (validate())
                        {
                            if(whereIcome.equals("home_now")||whereIcome.equals("placesActivity")){
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                orderDate = sdf.format(new Date());
                                orderTime = stf.format(new Date());

                            }
                            else  if(whereIcome.equals("ride_later")){
                                orderDate= sharedPreferences.getString("OrderDate", "0");
                                orderTime=sharedPreferences.getString("OrderTime", "0");

                            }
                            Log.d("orderDateorderDate",orderDate+"\t"+orderTime);
                            //to make order
                             get_taxi();

                        }
                    }
               // };

               // loading_handler.postDelayed(loadig_runnable,60000);





        });
        contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Window window = dialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();

                lp.gravity= Gravity.BOTTOM;

                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);
            }
        });
        share_tracker_captain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destination="";
                if (dist_Longitude!=0.0)
                  destination = "&daddr=" + dist_Latitude + "," + dist_Longitude;

                String uri = "http://maps.google.com/maps?saddr=" +latitude+","+longitude+destination;

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String ShareSub = "Here is my location \n";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri +"\n"+"captain info : \n"+
                        "captain name : "+captain_name.getText().toString()+"\n"+ "car info : "+car_plate_number.getText().toString()+"\t"+
                car_model.getText().toString()+"\t"+car_color.getText().toString()+"\n"+"captain mobile : "+mobile_number+
                "\n"+"captain email : "+Email);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        container_ride_to_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RideNow_Activity.this,HomeActivity.class);
                i.putExtra("whereIcome","booking_distination");
                i.putExtra("dist_Latitude",dist_Latitude);
                i.putExtra("dist_Longitude",dist_Longitude);
                editor.putString("booking_distination","booking_distination").commit();
                startActivity(i);
            }
        });

        call_captain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +mobile_number ));
                startActivity(intent);
            }
        });
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog of contCT CAPTAIN
              dialog.dismiss();
                showdoalog(R.layout.dialog_forget_pw,"sms");
            }
        });
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        toolbar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelOrder_dialog cancelOrder_dialog=new CancelOrder_dialog(orderid,RideNow_Activity.this);
                cancelOrder_dialog.show_dialog();

            }
        });
    }


    private void set_imge_size() {

        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);  // the values are saved in the screenSize object
        final int width = screenSize.x;
        final int height = screenSize.y;
        ViewGroup.LayoutParams params
                = captain_image.getLayoutParams();
        params.height = (int) (width / 4.8);
        params.width = (int) (width / 4.8);
        captain_image.setLayoutParams(params);
    }


    private String add_details_string="";

    private void showdoalog(int res,String from) {
        add_details_string="";
        final Dialog dialog = new Dialog(RideNow_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(res);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (from.equals("add_address")) {
            TextView save = (TextView) dialog.findViewById(R.id.save);
            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
            final EditText add_details_dialog = (EditText) dialog.findViewById(R.id.edit_add_dtails);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (add_details_dialog.getText().toString() != null && !add_details_dialog.getText().toString().isEmpty()) {
                        dialog.dismiss();
                        add_details_dialog.setError(null);
                        add_details_string = add_details_dialog.getText().toString();

                        Log.d("add_details_dialog", add_details_dialog.getText().toString());
                    } else
                        add_details_dialog.setError("");
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }
        else if (from.equals("sms"))
        {
           final EditText smsEdit=(EditText)dialog.findViewById(R.id.forgetpw_email_Tv);
            Button send=(Button)dialog.findViewById(R.id.forgetpw_send_btn);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (smsEdit.getText().toString().isEmpty())
                    {
                        smsEdit.setError(getResources().getString(R.string.reqired));
                    }
                    else{
                    smsEdit.setError(null);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile_number));
                    intent.putExtra("sms_body", smsEdit.getText().toString());
                    startActivity(intent);}
                }
            });
        }
        else if (from.equals("confirm_ride_later"))
        {
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            TextView textView= (TextView)dialog.findViewById(R.id.confirm_text);
            textView.setText("on\t"+sharedPreferences.getString("bigtxt","ll")+"\nhas been confirmed.");
            Button ok_button=(Button) dialog.findViewById(R.id.ok_button);
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent=new Intent(RideNow_Activity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                 }
            });
        }

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }



    private void get_data_of_my_location() {


            latitude=latitude;
            longitude=longitude;


        if (latitude != 0.0 && longitude != 0.0) {

            set.where_i_ride(latitude, longitude);
        }
    }

     private void get_data_of_distination() {

        container_ride_to_booking.setVisibility(View.VISIBLE);
        ride_to.setText(sharedPreferences.getString("dist_name_location","44"));
        Log.d("destination_name",sharedPreferences.getString("dist_name_location","44"));
        dist_Latitude=Double.parseDouble(sharedPreferences.getString("dist_Latitude","22"));
        dist_Longitude=Double.parseDouble(sharedPreferences.getString("dist_Longitude","22"));
        set.get_cars(dist_Latitude,dist_Longitude,true);


    }

    //to show dialog of promo_code to able to add promo_code
     private void add_promo_code(){

    final Dialog dialog = new Dialog(RideNow_Activity.this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.add_promo_code);


    final EditText promo_code_edit = (EditText) dialog.findViewById(R.id.promo_code);


    Button mSend = (Button) dialog.findViewById(R.id.add);




    mSend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(promo_code_edit.getText().toString())) {
                promo_code_edit.setError(RideNow_Activity.this.getResources().
                        getString(R.string.promo_code_message));

                return;
            }



                else{

                   //save promo_code if available
                   add_promo_code(promo_code_edit.getText().toString(),dialog);


            }



        }

    });

    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    dialog.show();
    dialog.getWindow().setAttributes(lp);



}


    private void add_promo_code(final String promo_code_value, final Dialog dialog1){

        try {
            Connection connection = new Connection(RideNow_Activity.this, "/Location/CheckPromoCode", "Post");
            connection.reset();
            connection.addParmmter("PromoCodeString",promo_code_value);
            connection.addParmmter("CustomerID",sharedPreferences.getString("passengerID", "1"));
            connection.addParmmter("CarCategoryID",sharedPreferences.getString("car_type_id", "1"));
            connection.addParmmter("LanguageID ",language);///


            connection.Connect(new Connection.Result() {
                @Override
                public void data(String jsonResponse) throws JSONException {

                    Log.d("Login_loginResponse", jsonResponse);


                    JSONObject jsonObject=new JSONObject(jsonResponse);
                    JSONArray jsonArray=jsonObject.getJSONArray("CheckPromoCode");
                    String message=jsonArray.getJSONObject(0).getString("Message");


                    //if promo_code available
                    if(message.equals("1")){
                        Toast.makeText(RideNow_Activity.this,getResources().getString(R.string.promo_code1),Toast.LENGTH_LONG).show();
                        promo_code.setText(promo_code_value);
                        promo_code_string=promo_code.getText().toString();
                        promo_code.setEnabled(false);


                    }
                    //if promo_code not available
                    else if(message.equals("0")){
                        Toast.makeText(RideNow_Activity.this,getResources().getString(R.string.promo_code0),Toast.LENGTH_LONG).show();

                    }
                    //if promo_code  is used before
                    else  if(message.equals("2")){
                        Toast.makeText(RideNow_Activity.this,getResources().getString(R.string.promo_code2),Toast.LENGTH_LONG).show();

                    }
                    dialog1.dismiss();

                }
            });

        } catch (Exception e) {e.printStackTrace();  }





    }




    //to get promo_code to use it in order
        private void get_promo_code(){

        try {
            Connection connection = new Connection(RideNow_Activity.this, "/Location/CustomerCheckPromoCode", "Post");
            connection.reset();

            connection.addParmmter("CustomerID",sharedPreferences.getString("passengerID", "1"));
            connection.addParmmter("CarCategoryID",sharedPreferences.getString("car_type_id", "1"));
            connection.addParmmter("LanguageID ",language);///


            connection.Connect(new Connection.Result() {
                @Override
                public void data(String jsonResponse) throws JSONException {

                    Log.d("Login_loginResponse", jsonResponse);

            JSONObject jsonObject= new JSONObject(jsonResponse);
            JSONArray jsonArray=jsonObject.getJSONArray("CustomerCheckPromoCode");
            String promo_code_v=jsonArray.getJSONObject(0).getString("PromoCodeString");
            if(!promo_code_v.equals("")){
                promo_code_string=promo_code_v;
                promo_code.setText(promo_code_string);
                promo_code.setEnabled(false);

            }
           else {
           promo_code_string="";
                promo_code.setEnabled(true);

            }

                }
            });

        } catch (Exception e) {e.printStackTrace();
            promo_code_string="";}





    }












       public void onRide(){
        save_container_before_booktrip.setVisibility(View.GONE);
        save_container_after_booktrip.setVisibility(View.VISIBLE);
        yalla_button.setVisibility(View.GONE);
        container_of_buttons.setVisibility(View.VISIBLE);

    }









    public void get_driver_details(String driverid, String orderid, String userimageurl,
                                   String userfullname, String mobile, String email,
                                   String ratingpointsaverage, String carcolorname,

                               String carbrandname, String carplatenumber,Boolean loading){




        try{
            search_driver_dialog.dismiss();
            Log.d("driver_id",driverid);
            if (loading)
                dialog_loading.dismiss();


                onRide();
               intrip=true;
            //driver is still in way to passenger
              TestModel.setOn_ride(true);



           captain_name.setText(userfullname);

           car_model.setText(carbrandname);
           car_color.setText(carcolorname);
           car_plate_number.setText(carplatenumber);

        try {
            Picasso.with(RideNow_Activity.this)
                    .load(userimageurl)
                    .placeholder(R.drawable.prof_placeholder).into(captain_image);
        }catch (Exception e){
            Picasso.with(RideNow_Activity.this)
                    .load(R.drawable.prof_placeholder)
                    .placeholder(R.drawable.prof_placeholder).into(captain_image);
        }
            mobile_number = mobile;
            Email = email;
            driver_id = driverid;
            editor.putString("DriverID", driverid).commit();
            toolbar_cancel.setVisibility(View.VISIBLE);
            this.orderid =orderid;
            handler = new Handler();

          //update track_driver every 1min
            handler.postDelayed(runnable = new Runnable() {

                @Override
                public void run() {
                    track_driver(driver_id);
                    handler.postDelayed(this, 60000);
                }
            }, 0);
            captian_rate.setText(ratingpointsaverage);
            ratingBar.setRating(Float.parseFloat(ratingpointsaverage));

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void track_driver(String driverID) {

        Connection connection = new Connection(this, "/Order/GetLatestRideTrackingByDriver", "Post",2);
        connection.reset();

        connection.addParmmter("DriverID", driverID);
        connection.addParmmter("Lang",language);
        connection.addParmmter("DateTypeID", 1 + "");
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("GetLatestRideTracking", str);

                try {

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray =  jsonObject.getJSONArray("RideTracking");

                    LatLng driver= new LatLng(Float.parseFloat(Double.toString(jsonArray.getJSONObject(0).getDouble("Latitude"))),Float.parseFloat(Double.toString(jsonArray.getJSONObject(0).getDouble("Longitude"))));
                    Log.d("driverlocation",jsonArray.getJSONObject(0).getDouble("Latitude")+"\n" +jsonArray.getJSONObject(0).getDouble("Longitude")+"");


                    LatLng distination= new LatLng(Float.parseFloat(Double.toString(latitude_ride)),Float.parseFloat(Double.toString(longitude_ride)));
                    Log.d("passenger_latitude",latitude+"");
                    Log.d("passenger_longtitude",longitude+"");

                   //to draw line between driver and passenger and calculate arrival time
                    gMapV2Direction.getDocument(driver ,distination,true,set,arrival_time,arrival_time_txt);
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });

    }

    private boolean validate() {
        pickup_address=ride_from.getText().toString()+"\n"+add_details_string;

        if(container_ride_to_booking.getVisibility()==View.VISIBLE)
        {
            dropoff_address=ride_to.getText().toString();
            Log.d("address",dropoff_address);





        }else
            dropoff_address="";

        return valid;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:




                if(TestModel.isOrder_back()){

                }
                //if passenger doesnot book trip and driver is not way to passenger
                else if(!TestModel.isOn_ride()&&!TestModel.isOrder_back()){

                    Intent i=new Intent(RideNow_Activity.this,HomeActivity.class);
                    i.putExtra("whereIcome","splash");
                    startActivity(i);
                    finish();

                }



                //if driver is in way to passenger and press back finish all activity
                else   if(TestModel.isOn_ride()&&!TestModel.isOrder_back()){
                    ActivityCompat.finishAffinity(RideNow_Activity.this);
                    handler.removeCallbacks(runnable);



                }






        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
  if(TestModel.isOrder_back()){

  }
        //if passenger doesnot book trip and driver is not way to passenger
       else if(!TestModel.isOn_ride()&&!TestModel.isOrder_back()){

            Intent i=new Intent(RideNow_Activity.this,HomeActivity.class);
            i.putExtra("whereIcome","splash");
            startActivity(i);
            finish();

        }



        //if driver is in way to passenger and press back finish all activity
     else   if(TestModel.isOn_ride()&&!TestModel.isOrder_back()){
          ActivityCompat.finishAffinity(RideNow_Activity.this);
          handler.removeCallbacks(runnable);



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        payment_type.setText(sharedPreferences.getString("payment_type_name",getResources().getString(R.string.cash)));

    }

    @Override
    protected void onStart() {
        Log.d("start","start");
        super.onStart();
        try {
            TestModel.getGoogleApiClientbooking().connect();
            if (!lock) {
                handler_premissin = new Handler();

                Runnable runnable_premissin = new Runnable() {
                    public void run() {


                        try {
                            get_data_of_my_location();
                            if (whereIcome.equals("placesActivity"))
                                get_data_of_distination();
                            Log.d("kya", "kya");
                            handler_premissin.removeCallbacks(this);
                            handler_premissin.removeCallbacksAndMessages(null);

                            lock = true;
                            Log.d("kya", "kya2");
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler_premissin.postDelayed(this, 100);
                            lock=false;
                            Log.d("kya", "kya3");
                        }


                    }
                };
                runnable_premissin.run();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            handler.post(runnable);


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
      Log.d("stop","stop");
        super.onStop();
        try {
            handler.removeCallbacks(runnable);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }






    @Override
    protected void onRestart() {
        Log.d("restart","yes");
        super.onRestart();

        try{
            bigtxt.setText(sharedPreferences.getString("bigtxt","ll"));
        }catch (Exception e){
            e.printStackTrace();

        }

        String come_from= sharedPreferences.getString("comefrom","0");

        if(come_from.equals("home")) {

            get_data_of_my_location();
        }
        String backfrom= sharedPreferences.getString("backfrom","0");
        if(backfrom.equals("search")){
            Log.d("latitudelongitude",sharedPreferences.getString("dist_name_location","44"));

            get_data_of_distination();

        }
        else if (backfrom.equals("home"))
        {
            get_data_of_distination();


        }
        editor.putString("booking_distination","").commit();
        editor.putString("comefrom","0");
        editor.putString("backfrom","0");
        editor.commit();

    }

private void show_seacrh_dialog(){




  search_driver_dialog.show();
    TextView cancel=(TextView) search_driver_dialog.findViewById(R.id.cancel);
    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CancelOrder_dialog cancelOrder_dialog=new CancelOrder_dialog(order_id,RideNow_Activity.this);

            cancelOrder_dialog.show_dialog();
        }
    });
  search_driver_dialog.getWindow().setBackgroundDrawableResource(R.color.colorBgTransparent);
    search_driver_dialog.setCancelable(false);
   search_driver_dialog.setCanceledOnTouchOutside(false);
    Window window = search_driver_dialog.getWindow();
    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

}
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                dialog_loading = new Dialog(this);
                dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_loading.setContentView(R.layout.test);
                dialog_loading.getWindow().setBackgroundDrawableResource(R.color.colorBgTransparent);
                dialog_loading.setCancelable(true);
                dialog_loading.setCanceledOnTouchOutside(false);
               LinearLayout relativeLayout=(LinearLayout) dialog_loading.findViewById(R.id.rel_loder);
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // The user just touched the screen

                                dialog_loading.dismiss();
                                break;
                            case MotionEvent.ACTION_UP:
                                // The touch just ended

                                break;
                        }

                        return false;
                    }
                });






                return dialog_loading;












            default:
                return null;
        }
    }
    private int getNodeIndex(NodeList nl, String nodename) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GooglePlacesAutocompleteActivity.RESULT_OK)
        {

             dist_Latitude=Double.parseDouble(data.getStringExtra("dist_Latitude"));
             dist_Longitude=Double.parseDouble(data.getStringExtra("dist_Longitude"));
             dist_name_location=(data.getStringExtra("dist_name_location"));

            drop_off.setText(dist_name_location);





            //to calculate arrival time
            String url = "https://maps.googleapis.com/maps/api/directions/xml?"
                    + "origin=" + latitude + "," + longitude
                    + "&destination=" + dist_Latitude + "," + dist_Longitude
                    + "&sensor=false&units=metric&mode=driving"
                    + "&key=AIzaSyDIWvq3r1zXF7biyZueBfsubuJbDlfrFCw"
                    + "&language="+language;
            Log.d("url", url);



            try {
                final Connection[] connection = {new Connection(((Activity) this), url, "Get", 1)};
                connection[0].reset();
                connection[0].Connect(new Connection.Result() {
                    @Override
                    public void data(String str) throws JSONException {
                        Log.d("checklogin",str);

                        try {
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            InputSource is = new InputSource();
                            is.setCharacterStream(new StringReader(str));
                            docm = builder.parse(is);
                            Log.d("docm",docm.toString());
                            //     Log.d("duration",docm.getElementsByTagName("duration").item(0).getNodeValue().toString());


                            try
                            {
                                NodeList nl1;
                                nl1 = docm.getElementsByTagName("duration");

                                Node node1 = nl1.item(nl1.getLength() - 1);
                                NodeList nl2 = null;
                                NodeList nl3 = null;
                                nl2 = node1.getChildNodes();
                                Node node2 = nl2.item(getNodeIndex(nl2, "text"));
                                nl3 = node1.getChildNodes();
                                Node node3 = nl3.item(getNodeIndex(nl3, "value"));











                                Log.d("DurationText", node2.getTextContent());
                                estimatefairnote.setText(getResources().getString(R.string.expected_arrived_time)+node2.getTextContent() );



                               String expexted_arriver_sec =node3.getTextContent();
                               float expected_arrival_time_min=Float.parseFloat(expexted_arriver_sec)/60;

                                Log.d("min",expected_arrival_time_min+"");

                               //to get distance
                                NodeList nl4;
                                nl4 = docm.getElementsByTagName("distance");

                                Node node4 = nl4.item(nl4.getLength() - 1);
                                NodeList nl5 = null;
                                nl5 = node4.getChildNodes();
                                Node node6 = nl5.item(getNodeIndex(nl5, "value"));

                                String string_dis=node6.getTextContent();
                                float distance=Float.parseFloat(string_dis)/1000;
                                Log.d("DistanceText", distance+"");




                                //to calculate estimatefair
                                connection[0] = new Connection(RideNow_Activity.this, "/Location/GetEstimateFair", "Post");
                                connection[0].reset();
                                connection[0].addParmmter("CategoryID", sharedPreferences.getString("car_type_id", "-1"));
                                connection[0].addParmmter("PickUpLatitude", latitude + "");
                                connection[0].addParmmter("PickUpLongtitude", longitude + "");
                                Log.d("pick_up",longitude+"");
                                connection[0].addParmmter("DropOffLatitude", dist_Latitude + "");
                                connection[0].addParmmter("DropOffLongtitude", dist_Longitude + "");
                                Log.d("drop_off",dist_Latitude+dist_Longitude+"");
                                connection[0].addParmmter("TripDuration", expected_arrival_time_min + "");
                                connection[0].addParmmter("KMNumber", distance + "");
                                connection[0].addParmmter("CountryID", sharedPreferences.getString("CountryID", "1"));
                                connection[0].addParmmter("StateID", sharedPreferences.getString("StateID", "14"));
                                connection[0].addParmmter("BookingTypeID", sharedPreferences.getString("BookingTypeID", "1"));


                                connection[0].Connect(new Connection.Result() {

                                    @Override
                                    public void data(String str) throws JSONException {

                                        try {

                                            JSONObject jsonObject = new JSONObject(str);
                                            JSONArray jsonArray = jsonObject.getJSONArray("EstimateFair");
                                            String estimate_fair =Math.round(jsonArray.getJSONObject(0).getDouble("EstimatedFair"))+"-"+ Math.round(jsonArray.getJSONObject(0).getDouble("EstimatedFairAfterPeak")) +" ج.م ";

                                            estimatefair.setText(estimate_fair);



                                        } catch (Exception e) {

                                            e.printStackTrace();

                                        }

                                    }
                                });













                            }
                            catch (Exception ex)
                            {
                                Log.d("Exception",ex.toString());

                            }




                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }});
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
            }























        }
        // when in GooglePlacesAutocompleteActivity back
        if (resultCode == GooglePlacesAutocompleteActivity.RESULT_CANCELED)
        {

            if (!data.getStringExtra("stateID").equals(sharedPreferences.getString("stateID","14")))
            {
              //  taps_creation();
            }
        }
    }






    private void  get_estimate(){
        dist_Latitude=Double.parseDouble(sharedPreferences.getString("dist_Latitude","22"));
        dist_Longitude=Double.parseDouble(sharedPreferences.getString("dist_Longitude","22"));

        Connection connection = new Connection(((Activity) this), "/Location/GetEstimateFair", "Post");
        connection.reset();
        connection.addParmmter("CategoryID", sharedPreferences.getString("car_type_id", "-1"));
        connection.addParmmter("PickUpLatitude", latitude + "");
        connection.addParmmter("PickUpLongtitude", longitude + "");
        Log.d("pick",latitude+longitude+"");
        connection.addParmmter("DropOffLatitude", dist_Latitude + "");
        Log.d("drop",dist_Latitude+dist_Longitude+"");
        connection.addParmmter("DropOffLongtitude", dist_Longitude + "");
        Log.d("drop",dist_Latitude+dist_Longitude+"");
        connection.addParmmter("TripDuration", 20 + "");
        connection.addParmmter("CountryID", sharedPreferences.getString("CountryID", "1"));
        connection.addParmmter("StateID", sharedPreferences.getString("StateID", "14"));
        connection.addParmmter("BookingTypeID", sharedPreferences.getString("BookingTypeID", "1"));


        connection.Connect(new Connection.Result() {

            @Override
            public void data(String str) throws JSONException {

                try {

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray("EstimateFair");
                    String ss =Math.round(jsonArray.getJSONObject(0).getDouble("EstimatedFair"))+"-"+ Math.round(jsonArray.getJSONObject(0).getDouble("EstimatedFairAfterPeak")) +" ج.م ";
                    //   Toast.makeText(RideNow_Activity.this,ss,Toast.LENGTH_LONG).show();
                    estimatefair.setText(ss);



                } catch (Exception e) {

                    e.printStackTrace();

                }

            }
        });
        String url = "https://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + latitude + "," + longitude
                + "&destination=" + dist_Latitude + "," + dist_Longitude
                + "&sensor=false&units=metric&mode=driving"
                + "&key=AIzaSyDIWvq3r1zXF7biyZueBfsubuJbDlfrFCw"
                + "&language="+language;
        Log.d("url", url);



        try {
            connection=new Connection(((Activity) this), url, "Get",1);
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.d("checklogin",str);

                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource is = new InputSource();
                        is.setCharacterStream(new StringReader(str));
                        docm = builder.parse(is);
                        Log.d("docm",docm.toString());



                        try
                        {
                            NodeList nl1;
                            nl1 = docm.getElementsByTagName("duration");

                            Node node1 = nl1.item(nl1.getLength() - 1);
                            NodeList nl2 = null;
                            nl2 = node1.getChildNodes();
                            Node node2 = nl2.item(getNodeIndex(nl2, "text"));
                            Log.d("DurationText", node2.getTextContent());
                            estimatefairnote.setText(getResources().getString(R.string.expected_arrived_time)+node2.getTextContent() );

                        }
                        catch (Exception ex)
                        {
                            Log.d("Exception",ex.toString());

                        }




                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }});
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }

    }








    //to make order
    public void get_taxi(){

        if (dist_Latitude==null||dist_Longitude==null){
            dist_Latitude=0.0;
            dist_Longitude=0.0;
        }

         dropoff_address=drop_off.getText().toString();

        if(dropoff_address.equals(getResources().getString(R.string.dropoff)))
        {
            dropoff_address=getResources().getString(R.string.will_tell_driver);
        }

        else{

            dropoff_address=drop_off.getText().toString();

        }

        Log.d("drop_off_txt",dropoff_address);

        Connection connection = new Connection(this, "/Order/NewOrder", "Post");
        connection.reset();

        connection.addParmmter("CustomerID", sharedPreferences.getString("passengerID", "1"));
        Log.d("customer_id",sharedPreferences.getString("passengerID", "1"));

        connection.addParmmter("CarCategoryID", sharedPreferences.getString("car_type_id", "1"));

        connection.addParmmter("PickUpLatitude", latitude + "");
        connection.addParmmter("PickUpLongtitude", longitude + "");

        connection.addParmmter("DropOffLatitude", dist_Latitude + "");
        connection.addParmmter("DropOffLongtitude", dist_Longitude + "");
        Log.d("drop_off_latitude",dist_Latitude+"");
        Log.d("drop_off_longtitude",dist_Longitude+"");

        connection.addParmmter("CurrencyID", sharedPreferences.getString("currancy_id","1") + "");
        Log.d("currency",sharedPreferences.getString("currancy_id","1"));

        connection.addParmmter("CurrencyRate", sharedPreferences.getString("CurrencyRate","1") + "");

        if (BookingTypeNumber.equals("1")) {
            connection.addParmmter("OrderDate", orderDate);
            connection.addParmmter("OrderTime", orderTime);
            connection.addParmmter("BookingTypeID",sharedPreferences.getString("bookingtype_id_now","1"));
            Log.d("car_list",sharedPreferences.getString("bookingtype_id_now","100"));
        }else{
            connection.addParmmter("OrderLaterDate", orderDate);
            connection.addParmmter("OrderLaterTime", orderTime+":00");
            connection.addParmmter("BookingTypeID",sharedPreferences.getString("bookingtype_id_later","2"));
            Log.d("car_list",sharedPreferences.getString("bookingtype_id_later","100"));
            Log.d("car_list",orderDate+"\n"+orderTime);
        }

        connection.addParmmter("PickupAddress", pickup_address);
        Log.d("pick_address",pickup_address);

        connection.addParmmter("DropoffAddress", dropoff_address);
        Log.d("address",dropoff_address);

        connection.addParmmter("BookingTypeNumber", BookingTypeNumber);
        connection.addParmmter("PaymentTypeId", sharedPreferences.getString("payment_type_id","1"));

        connection.addParmmter("OfferCodeString",promo_code_string);
        Log.d("offer",promo_code_string);



        Log.d("paramter",sharedPreferences.getString("passengerID", "1")+"\n"+sharedPreferences.getString("car_type_id", "-1")+"\n"
                +latitude +"\n"+longitude+"\n"+dropoff_address+"\n"+pickup_address);
            connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("gettaxi", str);

                try {
JSONObject jsonObject=new JSONObject(str);
JSONObject jsonObject1=jsonObject.getJSONObject("OrderReturnResult");
String res=jsonObject1.getString("result");
 order_id=jsonObject1.getString("OrderID");
 String time=jsonObject1.getString("WaitingOrderPotentialDriversTime");
 TestModel.setDriver_waiting(Float.parseFloat(time));

                    if(whereIcome.equals("ride_later"))
                    {
                        showdoalog(R.layout.confirm_msg_ride_later,"confirm_ride_later");
                    }
                    else {
                        if (res.equals("true"))
                        {
                          TestModel.setOrder_back(true);
                           show_seacrh_dialog();
                           TestModel.search_driver_runnable=new Runnable() {
                               @Override
                               public void run() {


                                   try {
                                       Connection connection = new Connection(RideNow_Activity.this, "/Order/IsAnyDriverAcceptOrder/"+order_id+"/"+language, "Get");
                                       connection.reset();


                                       connection.Connect(new Connection.Result() {
                                           @Override
                                           public void data(String str) throws JSONException {

                                               Log.d("Response", str);

                                               try{
JSONObject jsonObject2=new JSONObject(str);
String check=jsonObject2.getString("IsAnyDriverAcceptOrder");
String message=jsonObject2.getString("Message");
if(check.equals("false")){
    search_driver_dialog.dismiss();
    Toast.makeText(RideNow_Activity.this,message,Toast.LENGTH_LONG).show();
    TestModel.setOrder_back(false);

}


                                               }catch (Exception e){
                                                   e.printStackTrace();}







                                           }
                                       });

                                   } catch (Exception e) {

                                       e.printStackTrace();  }

















                               }
                           };

                           TestModel.searach_driver_handler=new Handler() ;
                           TestModel.searach_driver_handler.postDelayed(TestModel.search_driver_runnable, (long) (TestModel.getDriver_waiting()*1000));


                        }

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(RideNow_Activity.this,str,Toast.LENGTH_LONG).show();
                }

            }
        });
    }











}

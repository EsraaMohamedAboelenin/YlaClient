package solversteam.familycab.Nav.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import solversteam.familycab.Activities.LoginActivity;
import solversteam.familycab.Activities.RegistrationActivity;
import solversteam.familycab.Activities.RideNow_Activity;
import solversteam.familycab.Activities.SplashScreenActivity;
import solversteam.familycab.Adapter.Places_adapter;
import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Places_model;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;

public class PlacesActivity extends BaseActivity {

    public static PlacesActivity mActivity;
    private Typeface mBaseTextFont;
    private SupportMapFragment mapFragment;
    private LinearLayout up_down_linear,resturant_linear,cafe_linear,books_linear
            ,atm_linear,gym_linear,hospital_linear,container,sliding_container;
    private ImageView up_down_imageview;
    private String  up_down="up";
    private int container_height;
    private Animation slide_down,slide_up;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set_Map set_map;

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String API_KEY = "AIzaSyDIWvq3r1zXF7biyZueBfsubuJbDlfrFCw";
    private String lattude_="",longitude_="",iso_code,passengerID;
    private int resurce ;

    private ArrayList<Places_model> placesArray;
    private Places_adapter places_adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView toolbar_text;
    private Boolean in_trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        mActivity = this;
        sharedPreferences=getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        passengerID=sharedPreferences.getString("passengerID", "1");
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        set_map= new Set_Map(PlacesActivity.this,mapFragment,null);

        isPassengerInTrip(PlacesActivity.this,passengerID);
        setviews();

         slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

         slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        up_down_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               up_down_method();

            }
        });
        resturant_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               up_down_method();
                resurce=R.mipmap.resturent_icon;
                getDataOfplaces("restaurant");
            }
        });
        cafe_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               up_down_method();
                resurce=R.mipmap.coffe_icon;
                getDataOfplaces("cafe");
            }
        });
        books_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               up_down_method();
                resurce=R.mipmap.book_icon;
                getDataOfplaces("library");
            }
        });
        atm_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               up_down_method();
                resurce=R.mipmap.atm_icon;
                getDataOfplaces("atm");
            }
        });
        gym_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               up_down_method();
                resurce=R.mipmap.gym_icon;
                getDataOfplaces("gym");
            }
        });
        hospital_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_down_method();
                resurce=R.mipmap.hospital_icon;
                getDataOfplaces("hospital");
            }
        });

    }



    private void up_down_method(){
        toolbar_text.setVisibility(View.INVISIBLE);
        if (up_down.equals("up"))
        {
            up_down="down";
            slide_down.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    container.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            sliding_container.startAnimation(slide_down);

            up_down_imageview.setImageResource(R.drawable.up);
            
        }else 
        {
            up_down="up";
            sliding_container.startAnimation(slide_up);


           container.setVisibility(View.VISIBLE);

            up_down_imageview.setImageResource(R.drawable.down);
        }
        
        
    }
    
    private void setviews() {
        toolbar_text=(TextView)toolbar.findViewById(R.id.tv_home_save);
        toolbar_text.setText(getResources().getString(R.string.going_to));
        up_down_linear=(LinearLayout)findViewById(R.id.up_down_linear);
        resturant_linear=(LinearLayout)findViewById(R.id.resturant_linear);
        cafe_linear=(LinearLayout)findViewById(R.id.cafe_linear);
        books_linear=(LinearLayout)findViewById(R.id.books_linear);
        atm_linear=(LinearLayout)findViewById(R.id.atm_linear);
        gym_linear=(LinearLayout)findViewById(R.id.gym_linear);
        hospital_linear=(LinearLayout)findViewById(R.id.hospital_linear);
        container=(LinearLayout)findViewById(R.id.container_of_options);
        sliding_container=(LinearLayout)findViewById(R.id.linear_of_container_of_sliding);
        up_down_imageview=(ImageView)findViewById(R.id.up_down_imageview);

        container_height=container.getHeight();

        recyclerView=(RecyclerView)findViewById(R.id.placese_recycler);
        linearLayoutManager=new LinearLayoutManager(this);

    }




    public  void isPassengerInTrip(final Context context
                                         , String userId)
    {

        Connection connection5 = new Connection((Activity)context, "/order/IsPassengerInTrip/"+userId+"/"+1, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Intent i=null;
                    Log.d("IsPassengerInTrip",str);
                    ((BaseActivity) context).setupNavigationView(context,str);
                        if (str.equals("true"))
                        {
                            in_trip=true;




                        }

                      else{in_trip=false;}
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});


    }















    private void getDataOfplaces(String type) {
        try {
            placesArray=new ArrayList<>();
            lattude_ = sharedPreferences.getString("latitude", "0.0");
            longitude_ = sharedPreferences.getString("longitude", "0.0");
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append("location=" + lattude_ + "," + longitude_ + "&radius=5000");
            sb.append("&types=" + type);
            sb.append("&key=" + API_KEY);
            sb.append("&language=" + iso_code);
            Log.d("add_url", sb.toString());

            set_map.mMap.clear();
            Connection connection=new Connection(PlacesActivity.this,sb.toString(),"Get",1);
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.d("checklogin",str);

                    JSONObject jsonObj = new JSONObject(str);
                    JSONArray predsJsonArray = jsonObj.getJSONArray("results");
                    Log.d("add_url",predsJsonArray.toString());
                    // Extract the Place descriptions from the results

                    for (int i = 0; i < predsJsonArray.length(); i++) {
                      JSONObject jsonObject=  predsJsonArray.getJSONObject(i).getJSONObject("geometry");
                        Log.d("jsonObjectjsonObject",predsJsonArray.getJSONObject(i).getString("name")+"");
                        set_map.get_places(jsonObject.getJSONObject("location").getDouble("lat")
                                ,jsonObject.getJSONObject("location").getDouble("lng"),
                                predsJsonArray.getJSONObject(i).getString("name"),resurce);

                        placesArray.add(new Places_model(jsonObject.getJSONObject("location").getDouble("lat")
                                ,jsonObject.getJSONObject("location").getDouble("lng"),
                                predsJsonArray.getJSONObject(i).getString("name"),resurce));

                    }
                    places_adapter = new Places_adapter(PlacesActivity.this,placesArray,set_map,toolbar_text,in_trip);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(places_adapter);
                    places_adapter.notifyDataSetChanged();
                    if (predsJsonArray.length()!=0)
                    recyclerView.setVisibility(View.VISIBLE);



                }});



        }catch (Exception e)
        {
            e.printStackTrace();
            recyclerView.setVisibility(View.GONE);
        }


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


    public void zoom(){

        Log.d("kkkkkbkbkbk","nnnnnnnnnnnnnnnnnn");
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            set_map.thread.interrupt();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
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

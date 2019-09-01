package solversteam.familycab.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by mosta on 5/22/2017.
 */

public class Handler_Cars {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private  Thread thread;
    private  Runnable myRunnable;
    private  Handler handler;
    private Set_Map set_map;
    private Connection connection;
    private Timer t;
    private double dist_Latitude,dist_Longitude;
    private Context context;
    private LocationManager locationManager;

    public  Handler_Cars(final Context c){

        this.context=c;
        sharedPreferences=c.getSharedPreferences("lang_and_count",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        try {
            TestModel.getTimer().cancel();
        }catch (Exception e){
            Log.d("card",e.toString());
        }


        try{
                     t = new Timer();
                    t.scheduleAtFixedRate(new TimerTask() {

                        @Override
                        public void run() {
                            ((Activity)c).runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    //update ui
                                     dist_Latitude=Double.parseDouble(sharedPreferences.getString("latitude","22"));
                                     dist_Longitude=Double.parseDouble(sharedPreferences.getString("longitude","22"));
                                    if (context instanceof HomeActivity)
                                    updatelocation();
                                    Log.d("handler_car",sharedPreferences.getString("car_type_id","-1")+"\n"+dist_Latitude);

                                }
                            });
                        }


                    }, 0, 5000);

            TestModel.setTimer(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("cardx",e.toString());
                }

//            }
//
//        };thread.start();



    }
    private void updatelocation() {
        Date currentTime = Calendar.getInstance().getTime();
        String time = currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();
        Date d = new Date();
        CharSequence date_ = DateFormat.format("dd/MM/yyyy", d.getTime());
        Log.d("currentTime", time + "\n" + date_);

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Connection connection = new Connection(((Activity) context), "/Location/UserClosestDrivers", "Post");
            connection.reset();

            connection.addParmmter("CategoryID", sharedPreferences.getString("car_type_id", "-1"));
            connection.addParmmter("UserLatitude", dist_Latitude + "");
            connection.addParmmter("UserLongtitude", dist_Longitude + "");
            Log.d("countryid",sharedPreferences.getString("country_id","1"));
            connection.addParmmter("CountryID", sharedPreferences.getString("country_id","1"));


            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.d("checklogin", str);

                    TestModel.getSet_map().claer_map();
                    //    TestModel.getSet_map().get_cars(dist_Latitude,dist_Longitude);


                    try {

                        JSONObject jsonObject = new JSONObject(str);
                        JSONArray jsonArray = jsonObject.getJSONArray("DriversLocations");
                        for (int position = 0; position < jsonArray.length(); position++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                            TestModel.getSet_map().get_cars(jsonObject1.getDouble("DriverLatitude"),
                                    jsonObject1.getDouble("DriverLongtitude"),false);
                            Log.d("jsonObject",position+"");

                        }
                        if (jsonArray.length()<=0)
                        {
                            Toast.makeText(context, R.string.msg_car,Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(context, R.string.msg_car,Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

}

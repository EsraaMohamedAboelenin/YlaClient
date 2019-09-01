package solversteam.familycab.Models;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;


import solversteam.familycab.Activities.LoginActivity;
import solversteam.familycab.Activities.RegistrationActivity;
import solversteam.familycab.Activities.RideNow_Activity;
import solversteam.familycab.Activities.Chat;
import solversteam.familycab.Activities.SplashScreenActivity;
import solversteam.familycab.Adapter.StringRvAdapter;
import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Nav.Activities.CurrentActivity;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.Nav.Activities.PlacesActivity;
import solversteam.familycab.Nav.Activities.SettingsActivity;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mosta on 5/15/2017.
 */

public class TestModel {
    static GoogleApiClient googleApiClienthome,googleApiClientridelater,googleApiClientbooking,googleApiplacesac;
    static TextView mAdressTv;
    static TextView home_text;
    static ArrayList<Cars_items>cars_caegory;
    static Button ride_now,ride_later;
    static GoogleMap googleMap;
    static RideNow_Activity rideNow_activity;
    static Chat chat_activity;
    private static ArrayList <String> emails;
    private static ArrayList <Chat_model>payment_type;
    static boolean on_ride,arrive,order_back;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
  public   static Runnable search_driver_runnable;
   public   static Handler searach_driver_handler;
   private static float driver_waiting;

    public static void setDriver_waiting(float driver_waiting) {
        TestModel.driver_waiting = driver_waiting;
    }

    public static float getDriver_waiting() {
        return driver_waiting;
    }

    public static void setOrder_back(boolean order_back) {
        TestModel.order_back = order_back;
    }

    public static boolean isOrder_back() {
        return order_back;
    }

    public static String getFromShared(String key, Context context)
    {
        sharedPreferences=context.getSharedPreferences("on_ride",MODE_PRIVATE);

        return sharedPreferences.getString(key,"0");
    }
    public static void saveToShared(String key,Context context,String value)
    {
        sharedPreferences=context.getSharedPreferences("on_ride",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void updateShared(String key,Context context,String value)
    {
        sharedPreferences=context.getSharedPreferences("on_ride",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }

    //to set driver status is arrive to passenger or not
    public static void setArrive(boolean arrive) {
        TestModel.arrive = arrive;
    }

    public  static  boolean isArrive() {
        return arrive;
    }
    //to set if driver is in way to passenger or not
    public static void setOn_ride(boolean on_ride) {
        TestModel.on_ride = on_ride;
    }

    public  static  boolean isOn_ride() {
        return on_ride;
    }

    public static void setChat_activity(Chat chat_activity) {
        TestModel.chat_activity = chat_activity;
    }

    public static Chat getChat_activity() {
        return chat_activity;
    }

    public static Button getRide_now() {
        return ride_now;
    }

    public static void setRide_now(Button ride_now) {
        TestModel.ride_now = ride_now;
    }

    public static Button getRide_later() {
        return ride_later;
    }

    public static void setRide_later(Button ride_later) {
        TestModel.ride_later = ride_later;
    }

    public static ArrayList<Cars_items> getCars_caegory() {
        return cars_caegory;
    }

    public static void setCars_caegory(ArrayList<Cars_items> cars_caegory) {
        TestModel.cars_caegory = cars_caegory;
    }

    public static GoogleApiClient getGoogleApiClienthome() {
        return googleApiClienthome;
    }

    public static void setGoogleApiClienthome(GoogleApiClient googleApiClienthome) {
        TestModel.googleApiClienthome = googleApiClienthome;
    }

    public static GoogleApiClient getGoogleApiClientridelater() {
        return googleApiClientridelater;
    }

    public static void setGoogleApiClientridelater(GoogleApiClient googleApiClientridelater) {
        TestModel.googleApiClientridelater = googleApiClientridelater;
    }

    public static GoogleApiClient getGoogleApiClientbooking() {
        return googleApiClientbooking;
    }

    public static void setGoogleApiClientbooking(GoogleApiClient googleApiClientbooking) {
        TestModel.googleApiClientbooking = googleApiClientbooking;
    }

    static ImageView imag_car;
    static Timer timer;
    static String mkan;
    static ArrayList<LatLng>LatLag;

    public static ArrayList<LatLng> getLatLag() {
        return LatLag;
    }

    public static void setLatLag(ArrayList<LatLng> latLag) {
        LatLag = latLag;
    }

    public static String getMkan() {
        return mkan;
    }

    public static void setMkan(String mkan) {
        TestModel.mkan = mkan;
    }

    public static Timer getTimer() {

        return timer;
    }

    public static void setTimer(Timer timer) {
        TestModel.timer = timer;
    }

    public static Set_Map getSet_map() {
        return set_map;
    }

    public static void setSet_map(Set_Map set_map) {
        TestModel.set_map = set_map;
    }

    static Set_Map set_map;

    public static ImageView getImag_car() {
        return imag_car;
    }

    public static void setImag_car(ImageView imag_car) {
        TestModel.imag_car = imag_car;
    }

    public static TextView getmAdressTv() {
        return mAdressTv;
    }

    public static void setmAdressTv(TextView mAdressTv) {
        TestModel.mAdressTv = mAdressTv;
    }

    public static TextView getHome_text() {
        return home_text;
    }

    public static void setHome_text(TextView home_text) {
        TestModel.home_text = home_text;
    }

    public static GoogleMap getGoogleMap() {
        return googleMap;
    }

    public static void setGoogleMap(GoogleMap googleMap) {
        TestModel.googleMap = googleMap;
    }

    public static RideNow_Activity getRideNow_activity() {
        return rideNow_activity;
    }

    public static void setRideNow_activity(RideNow_Activity rideNow_activity) {
        TestModel.rideNow_activity = rideNow_activity;
    }

    public static GoogleApiClient getGoogleApiplacesac() {
        return googleApiplacesac;
    }

    public static void setGoogleApiplacesac(GoogleApiClient googleApiplacesac) {
        TestModel.googleApiplacesac = googleApiplacesac;
    }
    public static void  setarraytojson(ArrayList<Chat_model>list, Context context)
    {
        String json = new Gson().toJson(list);
        SharedPreferences sharedPreferences =context.getSharedPreferences("lang_and_count",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("chatlist",json).commit();
        Log.d("chatlist",json);

    }
    public static ArrayList<Chat_model> getjsontoarray( Context context)
    {

        ArrayList<Chat_model> chat_list =new ArrayList<>();
        SharedPreferences sharedPreferences =context.getSharedPreferences("lang_and_count",MODE_PRIVATE);
        String json = sharedPreferences.getString("chatlist","");
        try {
            JSONArray jsonarray=new JSONArray(json);
            Log.d("chatlist",json);
            for (int i=0;i<jsonarray.length();i++)
            {
                String messge =jsonarray.getJSONObject(i).getString("message");
                String msg_id =jsonarray.getJSONObject(i).getString("msg_id");
                chat_list.add(new Chat_model(messge,msg_id));
            }
            Log.d("chatlist",chat_list.get(0).getMsg_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    return chat_list;
    }

    public static ArrayList<String> getEmails(Context context) {
        if (emails!=null)
            Log.d("emails_",emails.size()+"");

        SharedPreferences sharedPreferences = context.getSharedPreferences("emails_", MODE_PRIVATE);

        ArrayList<String> arrayList=null;
        Gson gson = new Gson();

        try {
            String json = sharedPreferences.getString("email_", "");
            String[] arr= gson.fromJson(json,String[].class );
            List favorites = Arrays.asList(arr);
            arrayList = new ArrayList(favorites);

            //Log.d("checkarraysize2",arrayList.size()+"");

        }
        catch (Exception e) {
            Log.i("wezzashared", "no data");
        }



        return arrayList;

    }
    public static void setEmails(String email,Context context) {
        if( emails ==null )
        {
            emails=new ArrayList<>();
        }
        emails.add(email);


        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(emails);
        emails.clear();
        emails.addAll(hashSet);
        Log.d("emails_",emails.size()+"");
        SharedPreferences sharedPreferences = context.getSharedPreferences("emails_", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        try {

            Gson gson = new Gson();
            String json = gson.toJson(emails);
            editor.putString("email_", json);

            editor.commit();
        } catch (Exception e) {

        }

    }

    public static ArrayList<Chat_model> getPayment_type() {
        return payment_type;
    }

    public static void setPayment_type(ArrayList<Chat_model> payment_type) {
        TestModel.payment_type = payment_type;
    }



















    public static void isPassengerInTrip(final Context context , final Class whereIntent,
                                         final String whereIcome, final String userId)
    {
         //to check if passenger in trip or not
        Connection connection5 = new Connection((Activity)context, "/order/IsPassengerInTrip/"+userId+"/"+1, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Intent i=null;
                    Log.d("IsPassengerInTrip",str);
                    if (context instanceof SplashScreenActivity ||context instanceof LoginActivity
                         || context instanceof RegistrationActivity )
                    {

                        // if passenger is in trip go to current activity
                    if (str.equals("true"))
                    {
                        TestModel.setArrive(false);
                        i=new Intent(context,CurrentActivity.class);
                        i.putExtra("iso",whereIcome);
                        i.putExtra("whereIcome","splash");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        context.startActivity(i);
                        ((Activity)context).finish();

                        }


                   //else if  check if driver in way to passenger or not
                    else if(!str.equals("true")){





                        Connection connection5 = new Connection((Activity) context, "/Order/IsDriverAcceptOrderForPassanger/"+userId+"/"+1, "Get");
                        connection5.reset();
                        connection5.Connect(new Connection.Result() {
                            @Override
                            public void data(String str) throws JSONException {
                                try {
                                    Log.d("Is_driver_in_way",str);

                                    JSONObject jsonObject = new JSONObject(str);
                                    String res=jsonObject.getString("IsDriverAcceptOrder");



                                    //if driver is in way to passenger go to ridenow activity to show track of driver
                                    if (res.equals("true"))
                                    {
                                        TestModel.setArrive(false);
                                        TestModel.setOn_ride(true);
                                        Intent i=new Intent(context,RideNow_Activity.class);

                                        i.putExtra("whereIcome","splash");
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        context.startActivity(i);
                                        ((Activity)context).finish();

                                    }


                                    //else if  check if driver arrive to customer or not but doesnot start thr trip
                                   else if(!res.equals("true")) {




                                        TestModel.setOn_ride(false);





                                        TestModel.saveToShared("driver_id",context,"");
                                        Log.d("driver_id",TestModel.getFromShared("driver_id",context));
                                        TestModel.saveToShared("order_id",context,"");
                                        TestModel.saveToShared("user_image_url",context,"");
                                        TestModel.saveToShared("user_name",context,"");
                                        TestModel.saveToShared("user_mobile",context,"");
                                        TestModel.saveToShared("email",context,"");
                                        TestModel.saveToShared("rating",context,"");
                                        TestModel.saveToShared("car_color",context,"");
                                        TestModel.saveToShared("car_brand",context,"");
                                        TestModel.saveToShared("car_number",context,"");
                                        TestModel.saveToShared("long",context,"");
                                        TestModel.saveToShared("lat",context,"");











                                        Connection connection5 = new Connection((Activity)context, "/Order/IsDriverArrivedatCustomerForPassanger/"+userId+"/"+1, "Get");
                                        connection5.reset();
                                        connection5.Connect(new Connection.Result() {
                                            @Override
                                            public void data(String str) throws JSONException {
                                                try {



                                                 JSONObject jsonObject = new JSONObject(str);
                                               JSONObject jsonObject1=jsonObject.getJSONObject("IsDriverArrivedatCustomer");
                                            String res=jsonObject1.getString("result");
                                                    String order_id=jsonObject1.getString("OrderID");
                                                    if (res.equals("true"))
                                                    {


                                                        TestModel.setArrive(true);
                                                         Intent  i=new Intent(context,CurrentActivity.class);
                                                        i.putExtra("iso",whereIcome);
                                                        i.putExtra("whereIcome","splash");
                                                        i.putExtra("order_id",order_id);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                        context.startActivity(i);
                                                        ((Activity)context).finish();

                                                    }




                                                  // else the passenger doesnot book any trip so got to home page
                                                   else{
                                                        TestModel.setArrive(false);
                                                        Intent  i=new Intent(context,HomeActivity.class);
                                                        i.putExtra("iso",whereIcome);
                                                        i.putExtra("whereIcome","splash");
                                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                                        context.startActivity(i);
                                                        ((Activity)context).finish();



                                                    }



                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                    Log.d("error_GetAllLanguage",e.toString());
                                                }
                                            }});













                                    }





                                }catch (Exception e){
                                    e.printStackTrace();
                                    Log.d("error_GetAllLanguage",e.toString());
                                }
                            }});






                    }

















                    }else {
                        ((BaseActivity) context).setupNavigationView(context,str);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});

       
    }



}

package solversteam.familycab.Service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.List;

import solversteam.familycab.Activities.RideNow_Activity;
import solversteam.familycab.Dialogs.Captain_arrive_notification;
import solversteam.familycab.Activities.Chat;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Activities.Rate_Captain_activity;
import solversteam.familycab.Util.NotificationUtils;

/**
 * Created by Ahmed Ezz on 8/3/2017.
 * Passneger Request
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private String captain_name="",message="",captain_image="",totalridefee,message_chat="kkk",key_var="",endtime="",
            userfullname="",carbrandname="",carcolorname="",carplatenumber="",userimageurl="",mobile="",
            email="",ratingpointsaverage="1",driverid="",orderid="",ridedate=""
            ,destinationLat="0.0",destinationLong="0.0"
             ,sourceLat="0.0"
            ,sourceLong="0.0",currancy="",currencyabbreviation="";
    private Context context;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private int israttingcaptain=0;
    private NotificationUtils notificationUtils;

    public void onReceive(Context context, Intent intent) {
        Log.d("Incoming_Request", "Passenger Request");
        this.context=context;
        this.intent=intent;
          // get Passenger request
        notificationUtils = new NotificationUtils(context);
         getPassengerRequest();

    }
    private void getPassengerRequest() {

        if (intent.getExtras() != null) {
            try {


                 for (String key : intent.getExtras().keySet()) {
                    Object value = intent.getExtras().get(key);
                    Log.d("FirebaseDataReceiver", "Key: " + key + " Value: " + value);


                     if (key.equals("gcm.notification.arrivedatcustomer")||key.equals("gcm.notification.cancelorder")) {
                         key_var=key;

                         israttingcaptain=0;

                     }
                     else if(key.equals("gcm.notification.endtrip")){
                         israttingcaptain=1;
                         Log.d("israttingcaptain", israttingcaptain + "");

                     }
                     else if(value.equals("gcm.notification.captain_chat")){
                         Log.d("value", value + "");
                         israttingcaptain=2;
                     }
                     else if(key.equals("gcm.notification.driveracceptorder")){
                         Log.d("value", value + "");
                         israttingcaptain=4;
                     }
                     else if(key.equals("gcm.notification.addtocustomerwallet")){
                         Log.d("value", value + "");
                         israttingcaptain=10;
                     }
/////////////////////////////////////////////////////////////////////////////////
                          if (key.equals("gcm.notification.message")) {
                              message = value + "";
                              Log.d("cancelMessage", value + "");
                          } else if (key.equals("gcm.notification.name")) {
                              captain_name = value + "";
                              Log.d("cancelCaptainName", value + "");

                          } else if (key.equals("gcm.notification.imageurl")) {
                              captain_image = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.totalridefee")) {
                              totalridefee = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.endtime")) {
                              endtime = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.userfullname")) {
                              userfullname = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.userimageurl")) {
                              userimageurl = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.carplatenumber")) {
                              carplatenumber = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.carbrandname")) {
                              carbrandname = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.carcolorname")) {
                              carcolorname = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.ratingpointsaverage")) {
                              ratingpointsaverage = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.email")) {
                              email = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.mobile")) {
                              mobile = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.orderid")) {
                              orderid = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.driverid")) {
                              driverid = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.ridedate")) {
                              ridedate = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }

                          else if (key.equals("gcm.notification.destinationLat")) {
                              destinationLat = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.destinationLong")) {
                              destinationLong = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.sourceLong")) {
                              sourceLong = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }
                          else if (key.equals("gcm.notification.sourceLat")) {
                              sourceLat = value + "";
                              Log.d("cancelCaptainImage", value + "");
                          }

///////////////////////////////////////////////////////////////////
                         else if (key.equals("gcm.notification.message_chat"))
                          {
                              message_chat=value+"";
                              Log.d("value",message_chat);
                          }
                          else if (key.equals("gcm.notification.currencyname"))
                          {
                              currancy=value+"";
                              Log.d("value",message_chat);
                          }
                          else if (key.equals("gcm.notification.currencyabbreviation"))
                          {
                              currencyabbreviation=value+"";
                              Log.d("value",message_chat);
                          }
                      }
                showPassengerRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }

             // Display Request for Driver
            Log.d("message_chat",message_chat);

        }

    }

    private void showPassengerRequest() {
// check if driver is busy or available

        Log.d("check_status_service","Available!");
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.d("check_status_service","Available!"+cn.getShortClassName());
        if (isAppRunning(context, context.getPackageName())) {


            Log.d("message_chat",message_chat);

            if (israttingcaptain==1)
            {
                intent = new Intent(context, Rate_Captain_activity.class);

                intent.putExtra("message", message);
                intent.putExtra("captain_img", captain_image);
                intent.putExtra("captain_name", captain_name);
                intent.putExtra("totalridefee", totalridefee);
                intent.putExtra("currancy", currancy);
                intent.putExtra("currencyabbreviation", currencyabbreviation);
                intent.putExtra("endtime", endtime);
                intent.putExtra("orderid", orderid);
                intent.putExtra("driverid", driverid);
                intent.putExtra("ridedate", ridedate);
                intent.putExtra("sourceLat", sourceLat);
                intent.putExtra("sourceLong", sourceLong);
                intent.putExtra("destinationLat", destinationLat);
                intent.putExtra("destinationLong", destinationLong);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }else if (israttingcaptain==0){


               TestModel.setOn_ride(false);

                TestModel.setArrive(true);
               TestModel.setOrder_back(false);
               TestModel.searach_driver_handler.removeCallbacks(TestModel.search_driver_runnable);

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







                intent = new Intent(context, Captain_arrive_notification.class);

                if(key_var.equals("gcm.notification.cancelorder")){
                    intent.putExtra("captain_name", "");
}
else{
    intent.putExtra("captain_name", captain_name);
}
                intent.putExtra("classname", cn.getClassName());
                intent.putExtra("message", message);
                intent.putExtra("key_var", key_var);
                intent.putExtra("captain_img", captain_image);

                intent.putExtra("orderid", orderid);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }else if (israttingcaptain==4){

                if (cn.getClassName().equals("solversteam.familycab.Activities.RideNow_Activity"))
                {
                    TestModel.searach_driver_handler.removeCallbacks(TestModel.search_driver_runnable);
                    TestModel.setOn_ride(true);
                    TestModel.setOrder_back(false);

                    TestModel.saveToShared("driver_id",context,driverid);
                    TestModel.saveToShared("order_id",context,orderid);
                    TestModel.saveToShared("user_image_url",context,userimageurl);
                    TestModel.saveToShared("user_name",context,userfullname);
                    TestModel.saveToShared("user_mobile",context,mobile);
                    TestModel.saveToShared("email",context,email);
                    TestModel.saveToShared("rating",context,ratingpointsaverage);
                    TestModel.saveToShared("car_color",context,carcolorname);
                    TestModel.saveToShared("car_brand",context,carbrandname);
                    TestModel.saveToShared("car_number",context,carplatenumber);


                    TestModel.getRideNow_activity().get_driver_details(driverid,orderid,userimageurl,userfullname,mobile,
                        email,ratingpointsaverage,carcolorname,carbrandname,carplatenumber,true);






                }
                else
                {
                    TestModel.searach_driver_handler.removeCallbacks(TestModel.search_driver_runnable);
                    TestModel.setOn_ride(true);
                    TestModel.setOrder_back(false);
                    TestModel.saveToShared("driver_id",context,driverid);

                    TestModel.saveToShared("order_id",context,orderid);
                    TestModel.saveToShared("user_image_url",context,userimageurl);
                    TestModel.saveToShared("user_name",context,userfullname);
                    TestModel.saveToShared("user_mobile",context,mobile);
                    TestModel.saveToShared("email",context,email);
                    TestModel.saveToShared("rating",context,ratingpointsaverage);
                    TestModel.saveToShared("car_color",context,carcolorname);
                    TestModel.saveToShared("car_brand",context,carbrandname);
                    TestModel.saveToShared("car_number",context,carplatenumber);









                    intent = new Intent(context, RideNow_Activity.class);

                    intent.putExtra("whereIcome","firebase");
                    intent.putExtra("driverid", driverid);
                    intent.putExtra("orderid", orderid);
                    intent.putExtra("userfullname", userfullname);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("email", email);
                    intent.putExtra("ratingpointsaverage", ratingpointsaverage);
                    intent.putExtra("carcolorname", carcolorname);
                    intent.putExtra("carbrandname", carbrandname);
                    intent.putExtra("carplatenumber", carplatenumber);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

                }



            }
          //  else {
               else if (israttingcaptain == 2) {

                    Log.d("class_name",cn.getClassName());
                    if (!cn.getClassName().equals("solversteam.familycab.Activities.Chat")) {

                        intent = new Intent(context, Chat.class);
                        intent.putExtra("message_chat", message_chat);
                        intent.putExtra("msg_id", "0");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);


                    }
                    else {

                        TestModel.getChat_activity().setmsg(message_chat,"0");
                    }

                }
           // }
            else if(israttingcaptain == 10) {


            }
        }
        else  {


        }

        push_notify();
    }


    private void  push_notify()
    {
        try {

            notificationUtils.showNotificationMessage(message,captain_name,intent);
        }catch (Exception e){

            Log.d("MKLMKLMKL",e.toString());
            e.printStackTrace();
        }
    }

    public boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
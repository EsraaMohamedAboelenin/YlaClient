package solversteam.familycab.Service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.renderscript.Script;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import solversteam.familycab.Activities.LoginActivity;
import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.HomeActivity;

/**
 * Created by Ahmed Ezz on 8/2/2017.
 */

public class FirebaseIDService extends  FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private String refreshedToken="",passengerId;
    private SharedPreferences sharedPreferences;


    @Override
    public void onTokenRefresh() {
        Log.d("Incoming_Request", "Passenger Request123");
        //Getting registration token
    try {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);



    }catch (Exception e){e.printStackTrace();}







    }

    private void sendRegistrationToServer( String token) {
        //You can implement this method to store the token on your server
        //Not required for current project






        sharedPreferences = getSharedPreferences("lang_and_count", MODE_PRIVATE);

        passengerId = sharedPreferences.getString("passengerID", "");

if(!passengerId.isEmpty()) {
    Connection connection = new Connection(new FirebaseIDService(), "/User/UpdateUserToken", "Post");
    connection.reset();

    connection.addParmmter("DeviceID", token);
    connection.addParmmter("OSTypeID", "1");

    connection.addParmmter("UserID", passengerId + "");
    connection.Connect(new Connection.Result() {
        @Override
        public void data(String str) throws JSONException {
            Log.d("checklogin", str);


            try {


            } catch (Exception e) {


                e.printStackTrace();
            }


        }
    });


}


    }
    public String return_token(){
         return refreshedToken;
    }
}
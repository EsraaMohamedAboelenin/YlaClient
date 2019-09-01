package solversteam.familycab.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import solversteam.familycab.R;
import solversteam.familycab.Set_Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by kholoud on 4/18/2017.
 */
public class GPSTracker   {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    public boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private Set_Map set_map;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    private SharedPreferences sharedPreferences;

    public GPSTracker(Context context, Set_Map  set_map) {
        this.mContext = context;
        this.set_map=set_map;
        sharedPreferences=context.getSharedPreferences("lang_and_count",context.MODE_PRIVATE);
        getLocation();
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    public void getLocation() {
        try {

//            if ( Build.VERSION.SDK_INT >= 23 &&
//                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//            {  }

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
//      GPS STATUS>>
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//      Network STATUS>>
            isNetworkEnabled = isNetworkAvailable();

            if (!isGPSEnabled ) {
              showSettingsAlert();
            }
            else {
                this.canGetLocation = true;
            }

        } catch (Exception e) {

            Log.e("Error : Location",
                    "Impossible to connect to LocationManager", e);
        }

    }



    /**
     * Function to check if best network provider
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle(mContext.getResources().getString(R.string.gps_Settings));

        alertDialog.setMessage(mContext.getResources().getString(R.string.gps_Message));

        alertDialog.setPositiveButton(mContext.getResources().getString(R.string.gps_Settings_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                dialog.cancel();


                //((HomeActivity)mContext).finish();
            }
        });

        alertDialog.setNegativeButton(mContext.getResources().getString(R.string.gps_Setting_No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                dialog.cancel();
                try {
                    set_map.changeMapsearch(Double.parseDouble(sharedPreferences.getString("lastlat", "29.965183939990933")),
                            Double.parseDouble(sharedPreferences.getString("lastlong", "31.24874286353588")));
                    Log.d("bhnbhnbhnb", sharedPreferences.getString("lastlat", "0.0") + "\n"
                            + sharedPreferences.getString("lastlong", "0.0"));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        alertDialog.show();
    }

    public void showSettingsAlert2() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle(mContext.getResources().getString(R.string.net_Settings));

        alertDialog.setMessage(mContext.getResources().getString(R.string.net_Message));




        alertDialog.setNegativeButton(mContext.getResources().getString(R.string.gps_Setting_No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                dialog.cancel();
                try {
                    set_map.changeMapsearch(Double.parseDouble(sharedPreferences.getString("lastlat", "29.965183939990933")),
                            Double.parseDouble(sharedPreferences.getString("lastlong", "31.24874286353588")));
                    Log.d("bhnbhnbhnb", sharedPreferences.getString("lastlat", "0.0") + "\n"
                            + sharedPreferences.getString("lastlong", "0.0"));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        alertDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

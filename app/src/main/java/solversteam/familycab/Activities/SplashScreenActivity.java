package solversteam.familycab.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

import solversteam.familycab.Adapter.StringRvAdapter;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Chat_model;
import solversteam.familycab.Models.Country;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.Nav.Activities.SettingsActivity;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView mLogoTv;
    private Animation animation;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String iso_code,user_id,payment_type_id,country,language,payment_type_name,
            virsioncode,splash,logo,urlstore,defaultimage,resource_content,active,current_version_code,show;
    private int current_version_number=0;
    
    private Intent i;
    private ArrayList <Chat_model> payment_type;
    private Intent intent;
    private Boolean start=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        user_id=sharedPreferences.getString("passengerID","");
        Log.d("user_id",user_id);
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");
        payment_type_id=sharedPreferences.getString("payment_type_id","");
        payment_type_name=sharedPreferences.getString("payment_type_name","");
        Log.d("lange",iso_code+"aplash");

        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this,iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        mLogoTv = (ImageView)findViewById(R.id.splashtext);
        get_cureent_version();
        set_languages();


    }

    private void fadein() {

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeout();



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLogoTv.startAnimation(animation);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lange","restarts");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lange","resumes");
    }
    private void fadeout() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (user_id.isEmpty())
                {
                    i=new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(i);

                    finish();
                }else{

                    TestModel.isPassengerInTrip(SplashScreenActivity.this,null,iso_code,user_id);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLogoTv.startAnimation(animation);
    }


    public void get_paymenttype() {
        payment_type=new ArrayList<>();
        Connection connection5 = new Connection(this, "/paymentType/GetAllPaymentType/"+country+"/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Log.d("GetAllLanguage", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("PaymentType");
                    Log.d("GetAllLanguage2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        if (position==0) {
                            editor.putString("payment_type_id", id).commit();
                            editor.putString("payment_type_name", name).commit();
                        }

                        payment_type.add(new Chat_model(name,id));
                        TestModel.setPayment_type(payment_type);
                    }
                    fadein();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});
    }
    private void set_languages() {

        Connection connection5 = new Connection(this, "/Language/GetAllLanguage", "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {

                    Log.d("GetAllLanguage", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("Languages");
                    Log.d("GetAllLanguage2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        String iso = jsonObject1.getString("IsoCode");
                        if (iso_code.equals(iso))
                        {
                            language=id;
                            editor.putString("langID",language).commit();
                        }
                    }
                    get_paymenttype();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});}

    private void get_mysplash(){

        final String lo=country+"/"+"'logo'"+","+"'splashscreen'"+","+"'defaultimage',"+"'versioncode',"+ "'storeurl'"+"/"+language;

        Connection connection = new Connection(SplashScreenActivity.this, "/appresources/Getappresource/"+lo, "Get");
        connection.reset();
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                JSONObject jsonObject = new JSONObject(str);
                Log.d("checkjsondata",str);
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("AppResources");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String value = jsonArray.getJSONObject(i).getString("ResourceType");

                        if (value.equals("logo")) {
                            logo = jsonArray.getJSONObject(i).getString("ResourceURL");

                        } else if (value.equals("splashscreen")) {
                            splash = jsonArray.getJSONObject(i).getString("ResourceURL");
                            try {
                                Picasso.with(SplashScreenActivity.this).load(splash)
                                        .placeholder(R.drawable.familycap)
                                        .error(R.drawable.familycap)
                                        .fit().into(mLogoTv);

                            } catch (Exception e) {

                                Picasso.with(SplashScreenActivity.this).load(R.drawable.familycap)
                                        .placeholder(R.drawable.familycap)
                                        .error(R.drawable.familycap)
                                        .fit().into(mLogoTv);
                            }
                        } else if (value.equals("defaultimage")) {
                            defaultimage = jsonArray.getJSONObject(i).getString("ResourceURL");
                        } else if (value.equals("versioncode")) {
                            String[] recource_version = jsonArray.getJSONObject(i).getString("ResourceURL").split("-");
                            virsioncode = recource_version[0];
                            active = recource_version[1];
                            resource_content = jsonArray.getJSONObject(i).getString("ResourceContent");
                            Log.d("checkactive_version", active);
                        } else if (value.equals("storeurl")) {
                            urlstore = jsonArray.getJSONObject(i).getString("ResourceURL");
                        }


                    }
                    checkversion();
                }catch (Exception e ){
                    e.printStackTrace();
                }


            }});
    }

    private void checkversion() {
        if (current_version_code.equals(virsioncode)) {
            start = true;
            Log.d("checkanafen","hna1");


        } else {
            if (active.equals("active")) {
                Log.d("checkanafen","hna2");
                if(show.equals("yes"))
                {   start=false;
                    new AlertDialog.Builder(SplashScreenActivity.this)
                            .setIcon(R.drawable.icon_logo)
                            .setTitle(resource_content)
                            .setCancelable(false)
                            .setPositiveButton("تحديث الان", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Stop the activity
                                    try{
                                        Uri uri = Uri.parse(urlstore);
                                        intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);  }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    SplashScreenActivity.this.finish();
                                }

                            })
                            .setNegativeButton("عدم الاظهار مره اخرى", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            //Stop the activity
                                            editor.putString("show","no");
                                            editor.commit();
                                            set_languages();



                                        }

                                    }
                            )
                            .show();
                }
                else {
                    start=true;
                }
            }
            else {
                Log.d("checkanafen","hna3");

                start = false;
                new AlertDialog.Builder(SplashScreenActivity.this)
                        .setIcon(R.drawable.icon_logo)
                        .setTitle(resource_content)
                        .setCancelable(false)
                        .setPositiveButton("تحديث الان", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Stop the activity
                                try{
                                    Uri uri = Uri.parse(urlstore);
                                    intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);  }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                SplashScreenActivity.this.finish();
                            }

                        })
                        .setNegativeButton("خروج", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Stop the activity

                                        SplashScreenActivity.this.finish();
                                    }

                                }
                        )
                        .show();
            }
        }
        if (start)
        {
            set_languages();
        }
    }
    private void get_cureent_version() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        current_version_number = pInfo.versionCode;
        current_version_code=current_version_number+"";
        show=sharedPreferences.getString("show","yes");
    }
}

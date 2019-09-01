package solversteam.familycab.Nav.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import solversteam.familycab.Activities.Change_Pass;
import solversteam.familycab.Activities.Contact_Us;
import solversteam.familycab.Activities.Rates;
import solversteam.familycab.Activities.edit_profile;
import solversteam.familycab.Adapter.StringRvAdapter;
import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Country;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;

public class SettingsActivity extends BaseActivity {

    public static SettingsActivity mActivity;
    private Typeface mBaseTextFont;
    private TextView changelang,rates,change_pass,name,mobile,email,rate_app,feedback;
    private LinearLayout mLinear_cahnageLang;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String iso_code;
    private Dialog dialog;
    private RecyclerView languge_recycler;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Country> languageList;
    private StringRvAdapter adapter;
    private RelativeLayout progressBar;
    private Button cancel ;
    private TextView edit_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        TestModel.isPassengerInTrip(this,null,"",sharedPreferences.getString("passengerID","1"));

        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this, iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        Toolbar toolbar =(Toolbar)findViewById(R.id.include);
        ((TextView)toolbar.findViewById(R.id.tv_home_activity_title)).setText(R.string.nav_setting_title);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        set_view();

    }




    private void set_view(){

        name=(TextView)findViewById(R.id.name_txt_setting);
        mobile=(TextView)findViewById(R.id.mobile_txt_setting);
        email=(TextView)findViewById(R.id.email_txt_setting);
        rate_app=(TextView)findViewById(R.id.ratting_txt_setting);
        edit_profile=(TextView) findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingsActivity.this, solversteam.familycab.Activities.edit_profile.class);
                startActivity(intent);
            }
        });
        feedback=(TextView)findViewById(R.id.notes_txt_setting);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingsActivity.this, Contact_Us.class);

                startActivity(intent);
            }
        });
        rates=(TextView)findViewById(R.id.setting_rates) ;
        // cancel=(Button) findViewById(R.id.cancel) ;
        rate_app=(TextView) findViewById(R.id.ratting_txt_setting);
        rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/details?id=solversteam.yallapassenger";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        change_pass=(TextView)findViewById(R.id.change_pass_txt) ;
        name.setText(sharedPreferences.getString("name",""));
        mobile.setText(sharedPreferences.getString("mobile",""));
        email.setText(sharedPreferences.getString("email",""));

        rates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SettingsActivity.this, Rates.class);
                startActivity(i);
            }
        });
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SettingsActivity.this, Change_Pass.class);
                startActivity(i);
            }
        });
     /*   cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        */
        mActivity = this;
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
        changelang=(TextView)findViewById(R.id.setting_chnage_lang_txt);
        mLinear_cahnageLang=(LinearLayout)findViewById(R.id.setting_chnage_lang_linear);
        changelang.setText(sharedPreferences.getString("langname",""));

        dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lang_dialog);
        languge_recycler=(RecyclerView)dialog.findViewById(R.id.lang_recycler);
        progressBar=(RelativeLayout)dialog.findViewById(R.id.progress_bar);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        set_languages();

        mLinear_cahnageLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLangDialog ();
            }
        });

    }
    private void set_languages() {
        progressBar.setVisibility(View.VISIBLE);
        languageList=new ArrayList<>();
        Connection connection5 = new Connection(this, "/Language/GetAllLanguage", "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.d("GetAllLanguage", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("Languages");
                    Log.d("GetAllLanguage2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        String iso_code = jsonObject1.getString("IsoCode");
                        String img = jsonObject1.getString("LogoURL");

                        Country country_model = new Country(id,name,"",iso_code,img,"");
                        languageList.add(country_model);

                    }
                    adapter=new StringRvAdapter(SettingsActivity.this,languageList,dialog);
                    languge_recycler.setLayoutManager(linearLayoutManager);
                    languge_recycler.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});
    }

    private void showLangDialog () {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    protected void onResume() {
        set_view();
        super.onResume();
    }
}

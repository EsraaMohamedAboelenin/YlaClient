package solversteam.familycab.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Country;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.SettingsActivity;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;
import solversteam.familycab.Util.PrefsManger;
import solversteam.familycab.Util.Validation;

public class edit_profile extends AppCompatActivity {


    private CallbackManager mCallbackManager;


    private TextView phonecodetxt,selected_country,selected_state;

    private EditText mNameInputTv;

    private EditText mEmailInputTv;

    private EditText mPhoneInputTv;


    private AutoCompleteTextView mCountryInputTv,mStateTv;




    private ImageView flag_img;

    private Button save;

    private Typeface mBaseTextFont;


    private LinearLayout mLoadingLayout,phone_container;


    private ArrayList<Country> mCountriesList,mStateList;
    private ArrayList<String> test,phonecode,state_array;



    private String iso_code,country_id,state_id;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String language;
    private boolean validate;
    private String old_username,old_phone,old_email,old_country,old_country_id,old_state_id,old_state;
    private String new_username,new_phone,new_country_id,new_email,new_state_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");


        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this, iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        setContentView(R.layout.activity_edit_profile);

        initializeViews();



    }

    private void initializeViews() {
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    update();
                }
            }
        });
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");

        phonecodetxt = (TextView) findViewById(R.id.post_code_text);
        flag_img =(ImageView)findViewById(R.id.flag);

          selected_country=(TextView) findViewById(R.id.selected_country);
          selected_state=(TextView) findViewById(R.id.selected_state);

        mLoadingLayout = (LinearLayout) findViewById(R.id.layout_loading);
        phone_container = (LinearLayout) findViewById(R.id.phone_container);







        mNameInputTv = (EditText) findViewById(R.id.user_name);
        mNameInputTv.setTypeface(mBaseTextFont);








        mEmailInputTv = (EditText) findViewById(R.id.reg_email_Tv);
        mEmailInputTv.setTypeface(mBaseTextFont);



        mPhoneInputTv = (EditText) findViewById(R.id.reg_phoneNumber_Tv);
        mPhoneInputTv.setTypeface(mBaseTextFont);

        mCountryInputTv = (AutoCompleteTextView) findViewById(R.id.reg_country_Tv);
        mCountryInputTv.setTypeface(mBaseTextFont);
        mStateTv = (AutoCompleteTextView) findViewById(R.id.reg_state_Tv);
        mStateTv.setTypeface(mBaseTextFont);
        //to get countries
        getCountriesList();





    }
    private void getCountriesList() {
        setupEventsList();
    }
    private void setupEventsList() {
        mCountriesList = new ArrayList<>();
        test = new ArrayList<>();
        phonecode = new ArrayList<>();


        Connection connection5 = new Connection(this, "/country/GetAllWorkingCountry/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Log.d("GetAllWorkingCountry", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("Countries");
                    Log.d("GetAllWorkingCountry2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        String iso_code = jsonObject1.getString("ISOCode");
                        String phone_code = jsonObject1.getString("TelephoneCode");
                        String img = jsonObject1.getString("FlagURL");
                        String CurrencyID = jsonObject1.getString("CurrencyID");
                        Country country_model = new Country(id,name,phone_code,iso_code,img,CurrencyID);
                        mCountriesList.add(country_model);
                        test.add(name);
                        phonecode.add(phone_code);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(edit_profile.this, android.R.layout.simple_spinner_dropdown_item,test );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mCountryInputTv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    //to get saved_user_data
                    get_old_user_data();


                    mCountryInputTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mCountryInputTv.showDropDown();


                        }
                    });
                    mCountryInputTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //editor.putString("countrycode",mCountriesList.get(position).getIso_code()).commit();
                            country_id=mCountriesList.get(position).getID();
                            update_states(country_id);
                            if(!old_country_id.equals(country_id)){
                                mPhoneInputTv.setText("");
                            }
                            else{
                                mPhoneInputTv.setText(sharedPreferences.getString("mobile",""));
                            }
                            selected_country.setText(mCountriesList.get(position).getName());

                            phone_container.setVisibility(View.VISIBLE);
                            phonecodetxt.setText(mCountriesList.get(position).getPhonecountryCode());
                            try {
                                Picasso.with(edit_profile.this).
                                        load(mCountriesList.get(position).getCountry_img())
                                        .placeholder(R.drawable.egypt).resize(40,25).into(flag_img);
                            }catch (Exception e){}

                        }
                    });














                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_country",e.toString());
                }
            }});
    }







    private void update_states(String country_id) {
        mStateList=new ArrayList<>();
        state_array=new ArrayList<>();
        Connection connection5 = new Connection(this, "/state/GetAllState/"+country_id+"/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Log.d("GetAllLanguage", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("States");
                    Log.d("GetAllLanguage2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);


                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        String latitude =jsonObject1.getString("Latitude");
                        String longitude =jsonObject1.getString("Longitude");
                        state_array.add(name);
                        mStateList.add(new Country(id,name,latitude,longitude));

                    }
                    Log.d("size",state_array.size()+"");
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(edit_profile.this, android.R.layout.simple_spinner_dropdown_item,state_array );
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mStateTv.setAdapter(adapter1);


                    for(int i=0;i<mStateList.size();i++){

                        if(old_state_id.equals(mStateList.get(i).getID()))
                        {
                            Log.d("stateID",old_state_id);
                            old_state=mStateList.get(i).getName();


                            selected_state.setText(old_state);
                          break;

                        }


                    }
                   mStateTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mStateTv.showDropDown();


                        }
                    });


                   mStateTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //editor.putString("countrycode",mCountriesList.get(position).getIso_code()).commit();
                            state_id=mStateList.get(position).getID();


                            selected_state.setText(mStateList.get(position).getName());



                        }
                    });



















                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});
    }









    private  void  get_old_user_data(){

        old_username=sharedPreferences.getString("name","");
        Log.d("name",old_username);
        old_email=sharedPreferences.getString("email","");
        mEmailInputTv.setText(old_email);
        mNameInputTv.setText(old_username);
        old_phone=sharedPreferences.getString("mobile","");
        old_country_id=sharedPreferences.getString("country_id","1");

        update_states(old_country_id);
        country_id=sharedPreferences.getString("country_id","1");

        Log.d("id",old_country_id);

        old_state_id=sharedPreferences.getString("stateID","14");
        state_id=sharedPreferences.getString("stateID","14");
        Log.d("state_id",old_state_id);


        for(int i=0;i<mCountriesList.size();i++){

            if(old_country_id.equals(mCountriesList.get(i).getID()))
            {
                Log.d("idcc",old_country_id);
                old_country=mCountriesList.get(i).getName();

                phone_container.setVisibility(View.VISIBLE);
                phonecodetxt.setText(mCountriesList.get(i).getPhonecountryCode());
                selected_country.setText(old_country);
                mPhoneInputTv.setText(old_phone);
                try {
                    Picasso.with(edit_profile.this).
                            load(mCountriesList.get(i).getCountry_img())
                            .placeholder(R.drawable.egypt).resize(40,25).into(flag_img);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }









    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }








      private void update(){
       new_username=mNameInputTv.getText().toString();
       new_email=mEmailInputTv.getText().toString();
       new_phone=mPhoneInputTv.getText().toString();
       new_country_id = country_id;
       new_state_id=state_id;
       Log.d("state_id",new_state_id);
        if (new_username.equals(old_username) &&

                new_email.equals(old_email)
                && new_phone.equals(old_phone)&&new_country_id.equals(old_country_id)&&new_state_id.equals(old_state_id)
        ) {

            onBackPressed();

        }else{
           Connection connection = new Connection(edit_profile.this, "/User/EditProfile", "Post");
            connection.reset();


            connection.addParmmter("UserName".trim(), new_email);
            connection.addParmmter("Mobile".trim(), new_phone);
            connection.addParmmter("Email".trim(), new_email);
            connection.addParmmter("ReachUserID".trim(), sharedPreferences.getString("passengerID", "1"));
            connection.addParmmter("ARFirstName".trim(), new_username);
            connection.addParmmter("LatFirstName".trim(), "");


            connection.addParmmter("ARLastName".trim(), new_username);
            connection.addParmmter("LatLastName".trim(), "");
            connection.addParmmter("CountryID".trim(), new_country_id);
            connection.addParmmter("StateID".trim(), new_state_id);
            connection.Connect(new Connection.Result() {

                @Override
                public void data(String str) throws JSONException {


                    Log.d("str",str);


                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        JSONObject jsonObject1=jsonObject.getJSONObject("EditProfile");
                        String result=jsonObject1.getString("result");
                        String message=jsonObject1.getString("Message");
                        if(result.equals("true")){



                        editor.putString("name", new_username);
                        editor.putString("email", new_email);
                            TestModel.setEmails(new_email,edit_profile.this);

                            editor.putString("country_id",new_country_id);
                            Log.d("new",new_country_id);
                           editor.putString("mobile",new_phone);
                            editor.putString("stateID",new_state_id);
                            editor.commit();


                        Toast.makeText(edit_profile.this,message,Toast.LENGTH_LONG).show();
                            TestModel.getTimer().cancel();
                         Intent intent=new Intent(edit_profile.this,SplashScreenActivity.class);
                         startActivity(intent);
                         finish();

                        }
           else if(result.equals("false")){

                            Toast.makeText(edit_profile.this,message,Toast.LENGTH_LONG).show();
                        }

                    }catch (Exception e){
               e.printStackTrace();
                        Toast.makeText(edit_profile.this,getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }


    private boolean validate() {
          validate=true;
        if (TextUtils.isEmpty(mNameInputTv.getText().toString())) {
            mNameInputTv.setError(this.getResources().
                    getString(R.string.reg_name_valid_title));

            validate=false;
        }

        if (TextUtils.isEmpty(mEmailInputTv.getText().toString())||!isValidEmail(mEmailInputTv.getText().toString())) {
            mEmailInputTv.setError(this.getResources().
                    getString(R.string.reg_email_wrong_valid_title));

           validate=false;
        }
        if (country_id==null) {
            mCountryInputTv.setError(getString(R.string.reg_country_valid_title));

            validate=false;
        }else
            mCountryInputTv.setError(null);

        if (state_id==null) {
            mStateTv.setError(getString(R.string.choose_state));

            validate=false;
        }else
           mStateTv.setError(null);



        if (TextUtils.isEmpty(mPhoneInputTv.getText().toString())) {
            mPhoneInputTv.setError(this.getResources().
                    getString(R.string.reg_phone_valid_title));

            validate=false;
        }

        else if (!Validation.validate(mPhoneInputTv,"mobile",getResources().getString(R.string.mobile_error))) {

           validate=false;
        }
      return  validate;

    }

}

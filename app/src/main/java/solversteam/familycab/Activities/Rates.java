package solversteam.familycab.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Cars_items;
import solversteam.familycab.Models.Country;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;

/**
 * Created by mosta on 5/16/2017.
 */

public class Rates extends AppCompatActivity {
   private Toolbar toolbar;
   private TextView toolbar_title,starting_now_txt,starting_later_txt,min_now_txt,min_later_txt,airport_salary_txt,moving_km_txt,waiting_hr_txt;
   private ImageView toolbar_nav_img;
    private Spinner spinner_city,spinner_car_type;
    private ArrayList <String> cite_list,car_type_city;
    private ArrayAdapter<String> cityAdapter,carAdapter;
    private ArrayList<Country> stateList_model;
    private ArrayList<Cars_items>car_list_model;
    private String iso_code,language,country , state_id,car_type_id;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");

        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this,iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        setContentView(R.layout.rates);
        car_type_city=new ArrayList<>();
        cite_list=new ArrayList<>();
        toolbar=(Toolbar)findViewById(R.id.include);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar_title.setText(R.string.rates);
        spinner_car_type=(Spinner)findViewById(R.id.rate_car_type);
        spinner_city=(Spinner)findViewById(R.id.rate_city);
        starting_now_txt=(TextView)findViewById(R.id.starting_now_txt);
        starting_later_txt=(TextView)findViewById(R.id.starting_later_txt);
        min_now_txt=(TextView)findViewById(R.id.min_now_txt);
        min_later_txt=(TextView)findViewById(R.id.min_later_txt);
        airport_salary_txt=(TextView)findViewById(R.id.airport_salary_txt);
        moving_km_txt=(TextView)findViewById(R.id.moving_km_salary_txt);
        waiting_hr_txt=(TextView)findViewById(R.id.waiting_hr_salary_txt);


        get_allcitys();


        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    state_id = stateList_model.get(position).getID();
                    get_allcartypa(state_id);
                }catch (Exception e){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_car_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    car_type_id = car_list_model.get(position).getId();
                    get_alldata();
                }
                catch (Exception e){}            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void get_allcitys() {
        cite_list=new ArrayList<>();
        stateList_model=new ArrayList<>();
        Connection connection5 = new Connection(this, "/state/GetAllState/"+country+"/"+language, "Get");
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
                        cite_list.add(name);
                        stateList_model.add(new Country(id,name,latitude,longitude));

                    }
                    cite_list.add(getResources().getString(R.string.nofilter));
                    stateList_model.add(new Country("0",getResources().getString(R.string.nofilter),"",""));

                    // Creating adapter for spinner
                    cityAdapter = new ArrayAdapter<String>(Rates.this, android.R.layout.simple_spinner_item, cite_list);
                    // Drop down layout style - list view with radio button
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // attaching data adapter to spinner
                    spinner_city.setAdapter(cityAdapter);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});
    }

    private void get_allcartypa(String id){
        car_type_city=new ArrayList<>();
        car_list_model=new ArrayList<>();
        Connection connection5 = new Connection(this, "/CarCategory/GetAllCarCategories/"+country+"/"+id+"/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {


            @Override
            public void data(String str) throws JSONException {
                try {

                    Log.d("GetAllGender", str + "\n");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("CarCategories");
                    Log.d("GetAllGender2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        String img = jsonObject1.getString("IconURL");
                        car_type_city.add(name);
                        car_list_model.add(new Cars_items(name,id,img));
                    }
                    carAdapter = new ArrayAdapter<String>(Rates.this, android.R.layout.simple_spinner_item, car_type_city);
                    // Drop down layout style - list view with radio button
                    carAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // attaching data adapter to spinner
                    spinner_car_type.setAdapter(carAdapter);

                } catch (Exception e) {
                }
            }});
            }

            private void get_alldata(){
                Connection connection5 = new Connection(this, "/CarCategory/GetCarCategoryFactorsByState/"+car_type_id+"/"+state_id+"/"+language, "Get");
                connection5.reset();
                connection5.Connect(new Connection.Result() {


                    @Override
                    public void data(String str) throws JSONException {
                        try {

                            Log.d("GetAllGender", str + "\n");
                            JSONObject jsonObject = new JSONObject(str);
                            JSONArray jsonArray = jsonObject.getJSONArray("CarCategoryFactors");

                            for (int i=0; i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String currency=" "+jsonObject1.getString("CurrencyName");
                                airport_salary_txt.setText(jsonObject1.getString("AirportAmount")+currency);
                                moving_km_txt.setText(jsonObject1.getString("MovingKMFactor")+currency);
                                waiting_hr_txt.setText(jsonObject1.getString("WaitingFactorPerMinute")+currency);
                                String intialfact =jsonObject1.getString("InitialFactor");
                                String mincharge = jsonObject1.getString("MinCharge");
                                if(i==0){
                                    starting_now_txt.setText(intialfact);
                                    min_now_txt.setText(mincharge);
                                }else {
                                    starting_later_txt.setText(intialfact);
                                    min_later_txt.setText(mincharge);
                                }

                            }







                        } catch (Exception e) {
                        }
                    }});
            }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }
    }



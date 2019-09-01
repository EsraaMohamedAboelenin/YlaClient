package solversteam.familycab.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import solversteam.familycab.Adapter.search_adapter;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Country;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;


public class GooglePlacesAutocompleteActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDIWvq3r1zXF7biyZueBfsubuJbDlfrFCw";
    private LinearLayoutManager linearLayoutManager;
    private search_adapter adapter;
    private EditText autoCompView;
    private ArrayList<String> resultList,place_id_List;
    private RecyclerView recyclerView;
    public Toolbar toolbar;
    public TextView mTitleTv;
    private ImageView toolbar_nav_img;
    private ArrayList<LatLng> latlag;
    private Spinner spinner_city;
    private ArrayList<String> stateList;
    private ArrayList<Country> stateList_model;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String iso_code,language,country,lattude_="",longitude_="",stateID;
    private ArrayAdapter<String> cityAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
          autoCompView = (EditText) findViewById(R.id.autoCompleteTextView);
        toolbar=(Toolbar)findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");
        stateID=sharedPreferences.getString("stateID","1");
        Log.d("country",country);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this,iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        mTitleTv = (TextView) toolbar.findViewById(R.id.tv_home_activity_title);
        mTitleTv.setText(R.string.going_to);
        spinner_city=(Spinner)findViewById(R.id.search_city_spinner);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager( linearLayoutManager);

        // get all states
        update_states();

        autoCompView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(autoCompView.getText().length()>0)
                {
                    autocomplete(s.toString());

                }
                else
                {
                    try{
                        if(adapter!=null)
                        {
                            adapter.notifyDataSetChanged();
                                    adapter.notifyItemRangeRemoved(0,5)  ;               }
                    }catch (Exception e){}
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                   // editor.putString("stateID",stateList_model.get(position).getID()).commit();
                   // editor.putString("done_state","done_state").commit();
                    stateID=stateList_model.get(position).getID();
                    lattude_=stateList_model.get(position).getLatitude();
                    longitude_=stateList_model.get(position).getLongitude();

                    autocomplete(autoCompView.getText().toString());
                }catch (Exception e)
                {
                    e.printStackTrace();
                    lattude_="";
                    longitude_="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     }



    public ArrayList autocomplete(final String input) {


        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:"+sharedPreferences.getString("countrycode","eg"));// country
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            if (!lattude_.isEmpty())
            sb.append("&location=" + lattude_+","+longitude_+"&radius=5" +
                    "20000&strictbounds");
            Log.d("add_url", sb.toString());

            Connection connection=new Connection(GooglePlacesAutocompleteActivity.this,sb.toString(),"Get",1);
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.d("checklogin",str);

                    JSONObject jsonObj = new JSONObject(str);
                    JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
                    Log.d("add_url",predsJsonArray.toString());
                    // Extract the Place descriptions from the results
                    resultList = new ArrayList(predsJsonArray.length());
                    place_id_List = new ArrayList(predsJsonArray.length());

                    latlag=new ArrayList<>();
                    for (int i = 0; i < predsJsonArray.length(); i++) {

                     resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                     place_id_List.add(predsJsonArray.getJSONObject(i).getString("place_id"));

                    }

                    adapter = new search_adapter(GooglePlacesAutocompleteActivity.this,
                            resultList,place_id_List,getIntent().getExtras().getString("from"),stateID);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    getlatlag(place_id_List);



                }});




        }catch (Exception e){e.printStackTrace();}
        return resultList;
    }

    private void getlatlag(ArrayList<String> place_id_List) {
        final ArrayList<LatLng> latlag=new ArrayList<>();
 //        Places.GeoDataApi.getPlaceById(TestModel.getGoogleApiClient(), this.place_id_List.get(i))
//                .setResultCallback(new ResultCallback<PlaceBuffer>() {
//                    @Override
//                    public void onResult(PlaceBuffer places) {
//                        if (places.getStatus().isSuccess()) {
//                            final Place myPlace = places.get(0);
//                            LatLng queriedLocation = myPlace.getLatLng();
//                            latlag.add(queriedLocation);
//                            TestModel.setLatLag(latlag);
//                            Log.d("Latitude_is", "" + queriedLocation.latitude);
//                            Log.v("Longitude_is", "" + resultList.get(0));
//
//                        }
//                        places.release();
//                    }
//
//
//                });
    }

    
    private void update_states() {
        stateList=new ArrayList<>();
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
                        stateList.add(name);
                        stateList_model.add(new Country(id,name,latitude,longitude));

                    }
                    stateList.add(getResources().getString(R.string.nofilter));
                    stateList_model.add(new Country("0",getResources().getString(R.string.nofilter),"",""));

                    cityAdapter = new ArrayAdapter<String>(GooglePlacesAutocompleteActivity.this, android.R.layout.simple_spinner_item, stateList);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
               break;

        }
        return (super.onOptionsItemSelected(item));
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onBackPressed() {
        Intent return_tntent=new Intent();
        return_tntent.putExtra("stateID",stateID);
        setResult(GooglePlacesAutocompleteActivity.RESULT_CANCELED,return_tntent);
        finish();
    }
}
package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import solversteam.familycab.Adapter.CountryAdapter;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Country;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;

public class Add_credit extends AppCompatActivity {

    private EditText credit_number_edit,expire_date_edit,cvv_edittext,cardholder_name_edit;
    private ImageView country_imageview,toolbar_back;
    private TextView country_name_textview,toolbar_textview,save_text;
    private LinearLayout country_container_linear;
    private Toolbar toolbar;
    private Boolean valid=true;
    private String credit_number_string,expire_date_string,cvv_string,cardholder_name_string,country_id;
    private ArrayList<Country> mCountriesList;
    private String language,country;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Dialog dialog;
    private RecyclerView country_recycler;
    private LinearLayoutManager linearLayoutManager;
    private RelativeLayout progressBar;
    private CountryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");
        setviews();
        getAllcountry();

        save_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate())
                {
                    try {
                        editor.putString("payment_type_name", TestModel.getPayment_type().get(1).getMessage()).commit();
                        editor.putString("payment_type_id", TestModel.getPayment_type().get(1).getMsg_id()).commit();
                    }catch (Exception e){
                        editor.putString("payment_type_name", getResources().getString(R.string.credit)).commit();
                        editor.putString("payment_type_id", "2").commit();
                    }
                    onBackPressed();
                }
            }
        });
        country_container_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


    }

    private void getAllcountry() {
        mCountriesList=new ArrayList<>();
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

                    }
                    adapter=new CountryAdapter(Add_credit.this,mCountriesList,dialog,country_name_textview,country_imageview,country);
                    country_recycler.setLayoutManager(linearLayoutManager);
                    country_recycler.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_country",e.toString());
                }
            }});
    }


    private void setviews() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar_back=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_back.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_textview=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_textview.setText(R.string.addcredit);
        save_text=(TextView)toolbar.findViewById(R.id.tv_home_save);
        save_text.setVisibility(View.VISIBLE);
        credit_number_edit=(EditText)findViewById(R.id.card_number_edittext);
        expire_date_edit=(EditText)findViewById(R.id.expiration_date_edittext);
        cvv_edittext=(EditText)findViewById(R.id.cvv_edittext);
        cardholder_name_edit=(EditText)findViewById(R.id.cardholder_name_edittext);
        country_name_textview=(TextView)findViewById(R.id.country_name_textview);
        country_imageview=(ImageView)findViewById(R.id.country_image_imageview);
        country_container_linear=(LinearLayout)findViewById(R.id.country_container_linear);
        
        //////////////////////////////////////
        Display display =  getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);  // the values are saved in the screenSize object
        final int width = screenSize.x;
        final int height = screenSize.y;
        ViewGroup.LayoutParams params
                = country_imageview.getLayoutParams();
        params.height = height /22;
        params.width = (int) (height /22);

//////////////////////////////////////////////////////////////////////////
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lang_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        ((TextView)dialog.findViewById(R.id.langugetext)).setText(R.string.country_credit);
        country_recycler=(RecyclerView)dialog.findViewById(R.id.lang_recycler);
        progressBar=(RelativeLayout)dialog.findViewById(R.id.progress_bar);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    }
    private Boolean validate() {
        valid=true;

        if (credit_number_edit.getText().toString().isEmpty())
        {
            credit_number_edit.setError(getResources().getString(R.string.reqired));
            valid=false;
        }
        else
            credit_number_string=credit_number_edit.getText().toString();

        if (expire_date_edit.getText().toString().isEmpty())
        {
            expire_date_edit.setError(getResources().getString(R.string.reqired));
            valid=false;
        }
        else
            expire_date_string=expire_date_edit.getText().toString();

        if (cvv_edittext.getText().toString().isEmpty())
        {
            cvv_edittext.setError(getResources().getString(R.string.reqired));
            valid=false;
        }
        else
            cvv_string=cvv_edittext.getText().toString();

        if (cardholder_name_edit.getText().toString().isEmpty())
        {
            cardholder_name_edit.setError(getResources().getString(R.string.reqired));
            valid=false;
        }
        else
            cardholder_name_string=cardholder_name_edit.getText().toString();

        return valid;
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

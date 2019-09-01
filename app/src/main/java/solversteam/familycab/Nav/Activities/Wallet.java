package solversteam.familycab.Nav.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import solversteam.familycab.Activities.Add_credit;
import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;

public class Wallet extends BaseActivity {

    private TextView cash_textview,add_credie_text,credit_card_textview,redeem_vouch_textview,wallet_value_text;
    private Switch credit_first_switch;
    private SharedPreferences  sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent intent;
    private ImageView check;
    private String wallet_value,use_credit_first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wallet);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();


        TestModel.isPassengerInTrip(this,null,"",sharedPreferences.getString("passengerID","1"));

        setviews();




        mTitleTv.setText(R.string.wallet);
        if (getIntent().getExtras().getString("comefrom").equals("RideNow_Activity"))
        {
            mNavTv.setVisibility(View.GONE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        add_credie_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(Wallet.this,Add_credit.class);
                startActivity(intent);
            }
        });

        cash_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editor.putString("payment_type_name", TestModel.getPayment_type().get(0).getMessage()).commit();
                    editor.putString("payment_type_id", TestModel.getPayment_type().get(0).getMsg_id()).commit();
                }catch (Exception e){
                    editor.putString("payment_type_name", getResources().getString(R.string.cash)).commit();
                    editor.putString("payment_type_id", "1").commit();
                }
                onBackPressed();

            }
        });
credit_first_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
if(b){
    check.setVisibility(View.GONE);
    use_credit_first("1");

}
else{
    check.setVisibility(View.VISIBLE);
    use_credit_first("0");

}
    }
});
    }



private   void use_credit_first(String value) {

    try {
        Connection connection = new Connection(this, "/Order/UpdateUserCreditFirst/"+sharedPreferences.getString("passengerID", "1")+"/"+value, "Get");
        connection.reset();


        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {

                Log.d("Response", str);

                try{


                }catch (Exception e){
                    e.printStackTrace();}







            }
        });

    } catch (Exception e) {

        e.printStackTrace();  }




}
private void get_wallet(){

    try {
        Connection connection = new Connection(this, "/Order/GetCustomerWallet", "Post");
        connection.reset();
        connection.addParmmter("CustomerID",sharedPreferences.getString("passengerID", "1"));


        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {

                Log.d("Login_loginResponse", str);

             try{
                 JSONObject jsonObject = new JSONObject(str);
                 wallet_value=jsonObject.getString("WalletCreditAmount");
                 wallet_value_text.setText(wallet_value);
                 use_credit_first=jsonObject.getString("UseCreditFirst");
                //to check if passenger use credit first or not
                 if(use_credit_first.equals("true")){
                     credit_first_switch.setChecked(true);
                     check.setVisibility(View.GONE);
                 }
                    else{  credit_first_switch.setChecked(false);
                     check.setVisibility(View.VISIBLE);

                 }
             }catch (Exception e){e.printStackTrace();}







            }
        });

    } catch (Exception e) {e.printStackTrace();  }



}
    private void setviews() {

        add_credie_text=(TextView)findViewById(R.id.add_credit_text_wallet);
        cash_textview=(TextView)findViewById(R.id.cash_text_wallet);
        credit_card_textview=(TextView)findViewById(R.id.credit_card_text_wallet);
        redeem_vouch_textview=(TextView)findViewById(R.id.redeem_vouch_text_wallet);
        wallet_value_text=(TextView)findViewById(R.id.wallet_text);

        check=(ImageView)findViewById(R.id.check);
        credit_first_switch=(Switch)findViewById(R.id.credit_switch);





        //to get credit in wallet
         get_wallet();

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

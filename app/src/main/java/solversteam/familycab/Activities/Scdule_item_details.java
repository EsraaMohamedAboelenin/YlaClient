package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.R;
import solversteam.familycab.Util.CancelOrder_dialog;

public class Scdule_item_details extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title,toolbar_cancel,date_text,from_address,from_address_details,
            where_address,where_address_details,car_type_text,payment_type_text;
    private ImageView toolbar_nav_img;
    private String comment;
    private Dialog dialog;
    private String orderid,language;
    private SharedPreferences sharedPreferences;
    private CancelOrder_dialog cancelOrder_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scdule_item_details);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        language=sharedPreferences.getString("langID","1");
        toolbar=(Toolbar)findViewById(R.id.include);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_cancel=(TextView)toolbar.findViewById(R.id.tv_home_save);
        toolbar_cancel.setVisibility(View.VISIBLE);
        toolbar_cancel.setText(R.string.mdtp_cancel);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title.setText(R.string.myride);

        orderid=getIntent().getExtras().getString("order_id");
        cancelOrder_dialog=new CancelOrder_dialog(orderid,this);
        set_views();
        set_data();


        toolbar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder_dialog.show_dialog();
            }
        });
    }

    private void set_data() {
        Connection connection5 = new Connection(this, "/Order/GetOrderDetails/"+orderid+"/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Log.d("GetAllLanguage", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("OrderDetails");
                    String date="";
                    try{
                        date= jsonObject1.getString("MobOrderDate")+" "+jsonObject1.getString("OrderTime");
                        SimpleDateFormat spf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        Date newDate= spf.parse(date);
                        spf= new  SimpleDateFormat("dd MMM , hh:mm ") ;
                        date = spf.format(newDate);}catch (Exception e){
                        date=jsonObject1.getString("MobDate");
                    }
                    date_text.setText(date);
                    from_address.setText(jsonObject1.getString("PickupAddress"));
                    from_address_details.setText(jsonObject1.getString("PickupAddress"));
                    where_address.setText(jsonObject1.getString("DropoffAddress"));
                    where_address_details.setText(jsonObject1.getString("DropoffAddress"));
                    car_type_text.setText(jsonObject1.getString("CarCategoryLatName"));
                    payment_type_text.setText(jsonObject1.getString("PaymentTypeLatName"));
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});
    }


    private void set_views() {
        date_text=(TextView)findViewById(R.id.history_date);
        from_address=(TextView)findViewById(R.id.history_iam_here_address);
        from_address_details=(TextView)findViewById(R.id.history_iam_here_address_details);
        where_address=(TextView)findViewById(R.id.history_where_address);
        where_address_details=(TextView)findViewById(R.id.history_where_address_details);
        car_type_text=(TextView)findViewById(R.id.car_type_txt_item_details);
        payment_type_text=(TextView)findViewById(R.id.payment_txt_item_details);
         }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.test);
                dialog.getWindow().setBackgroundDrawableResource(R.color.colorBgTransparent);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                LinearLayout relativeLayout=(LinearLayout) dialog.findViewById(R.id.rel_loder);
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // The user just touched the screen

                                dialog.dismiss();
                                break;
                            case MotionEvent.ACTION_UP:
                                // The touch just ended

                                break;
                        }

                        return false;
                    }
                });

                return dialog;
            default:
                return null;
        }
    }
}

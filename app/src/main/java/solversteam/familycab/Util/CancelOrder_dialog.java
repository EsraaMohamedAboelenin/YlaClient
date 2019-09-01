package solversteam.familycab.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import solversteam.familycab.Activities.Scdule_item_details;
import solversteam.familycab.Adapter.cancel_reasons_adapter;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Models.cancel_trip_model;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.Nav.Activities.My_Rides;
import solversteam.familycab.Nav.Activities.ScheduledActivity;
import solversteam.familycab.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nashaat on 10/25/2017.
 */

public class CancelOrder_dialog {
    String orderid,comment,reason_id;
    Context context;
    private Dialog dialog;
    private RecyclerView reasons_list;
    private Button send;
    private ArrayList<cancel_trip_model>reasons_array;
    private String country,language;
    private SharedPreferences sharedPreferences;
    private LinearLayoutManager llm;
    private cancel_reasons_adapter cancel_reasons_adapter;
    public CancelOrder_dialog(String orderid, Context context) {
        this.orderid = orderid;
        this.context = context;
        sharedPreferences =context.getSharedPreferences("lang_and_count",MODE_PRIVATE);

        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");
        llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        get_cancel_reasons();
    }

    public void show_dialog() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.why_trip_cancel_dialog);
        reasons_list=(RecyclerView) dialog.findViewById(R.id.reasons_list);

        send=(Button)dialog.findViewById(R.id.dialog_send);
        comment="";

     /*   RadioGroup radioGroup=(RadioGroup)dialog.findViewById(R.id.group1);
        comment="";
        // other.toggle();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton=(RadioButton)group.findViewById(checkedId);
                Log.d("henenenene",checkedId+"\n"+R.id.reason_other);

                Log.d("henenenene",radioButton.getText().toString());
                comment=radioButton.getText().toString();

            }
        });
        */

     /*   send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!comment.isEmpty())
                    delet_order();
                else
                    Toast.makeText(context,R.string.msg2,Toast.LENGTH_SHORT).show();
            }
        });
*/
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
      //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);



    }


    private void get_cancel_reasons(){
        reasons_array=new ArrayList<>();



        Connection connection = new Connection((Activity) context, "/CancelReason/getAll/"+country+"/"+language, "Get");
        connection.reset();


        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("", str);

                try {

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray =  jsonObject.getJSONArray("CancelReason");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("ID");
                        String name=jsonObject1.getString("Name");
                        reasons_array.add(new cancel_trip_model(id,name));
                    }

                    cancel_reasons_adapter=new cancel_reasons_adapter(context,reasons_array,dialog,orderid);
                    reasons_list.setLayoutManager(llm);
                    reasons_list.setAdapter(cancel_reasons_adapter);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });













    }
    private void delet_order() {
        Connection connection = new Connection((Activity) context, "/Order/CancelOrderByCustomer", "Post");
        connection.reset();

        connection.addParmmter("ID",orderid);
        connection.addParmmter("CancelReasonID", reason_id);
        connection.addParmmter("Comment",comment);
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("gettaxi", str);
                if (str.equals("true"))
                {
                    dialog.dismiss();
                    Toast.makeText(context,R.string.tripcanceled,Toast.LENGTH_SHORT).show();
                    if (context instanceof ScheduledActivity)
                    ((Activity)context).finish();
                    else {
                        Intent intent =new Intent(context, HomeActivity.class);
                        intent.putExtra("whereIcome","splash");

                        context.startActivity(intent);
                        ((Activity) context).finish();

                        TestModel.setOn_ride(false);
                    }
                }else{
                    dialog.dismiss();
                    Toast.makeText(context,R.string.tripnotcanceled,Toast.LENGTH_SHORT).show();
                }

            }});
    }
}

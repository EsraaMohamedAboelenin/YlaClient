package solversteam.familycab.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Models.cancel_trip_model;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.Nav.Activities.ScheduledActivity;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;

public class cancel_reasons_adapter  extends RecyclerView.Adapter<cancel_reasons_adapter.ViewHolder>     {
    ArrayList<cancel_trip_model>reasons_array;
    Context context;
 Dialog dialog;
 String order_id,value,reason_id;


    public cancel_reasons_adapter(Context context, ArrayList<cancel_trip_model>reasons_array, Dialog dialog, String order_id){
        this.reasons_array=reasons_array;
        this.context=context;
     this.dialog=dialog;
       this.order_id=order_id;


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancel_trip_reasons,parent, false);
       ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.reason_txt.setText(reasons_array.get(position).getContent());
        holder.reason_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                value=reasons_array.get(position).getContent();
                reason_id=reasons_array.get(position).getId();
                delet_order();

            }
        });


    }
    private void delet_order() {
        Connection connection = new Connection((Activity) context, "/Order/CancelOrderByCustomer", "Post");
        connection.reset();

        connection.addParmmter("ID",order_id);
        Log.d("id_order",order_id);
        connection.addParmmter("CancelReasonID", reason_id);
        Log.d("id_reason",reason_id);
        connection.addParmmter("Comment",value);
        Log.d("value",value);
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("gettaxi", str);
                if (str.equals("true"))
                {
                    TestModel.searach_driver_handler.removeCallbacks(TestModel.search_driver_runnable);
                    dialog.dismiss();

                    if (context instanceof ScheduledActivity)
                        ((Activity)context).finish();
                    else {

                        Intent intent =new Intent(context, HomeActivity.class);
                        intent.putExtra("whereIcome","splash");

                        context.startActivity(intent);
                        ((Activity) context).finish();

                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        TestModel.setOn_ride(false);
                        TestModel.setOrder_back(false);
                       // TestModel.updateShared("onride",context,"false");
                    }
                }else{
                    dialog.dismiss();
                    Toast.makeText(context,R.string.tripnotcanceled,Toast.LENGTH_SHORT).show();
                }

            }});
    }
    @Override
    public int getItemCount() {
        return reasons_array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    TextView reason_txt;
        public ViewHolder(View itemView) {
            super(itemView);
           reason_txt=(TextView)itemView.findViewById(R.id.reason_edit_booking);
        }
    }

}

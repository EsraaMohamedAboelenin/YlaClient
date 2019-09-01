package solversteam.familycab.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import solversteam.familycab.Activities.RideNow_Activity;
import solversteam.familycab.Models.Places_model;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mosta on 9/17/2017.
 */

public class Places_adapter extends  RecyclerView.Adapter<Places_adapter.ViewHolder> {
    private ArrayList<Places_model> placesarray;
    private Context context;
    private Set_Map set_map;
    private TextView toolbar_text;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Boolean in_trip;

    public Places_adapter(Context context, ArrayList<Places_model> placesarray, Set_Map set_map, TextView toolbar_text,Boolean in_trip){
        this.context = context;
        this.placesarray=placesarray;
        this.set_map=set_map;
        this.toolbar_text=toolbar_text;
        sharedPreferences =context.getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
       this. in_trip=in_trip;
       if(in_trip){
           this.toolbar_text.setVisibility(View.INVISIBLE);
       }
       else {
           this.toolbar_text.setVisibility(View.VISIBLE);
       }
    }
    @Override
    public Places_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
         ViewHolder pvh = new  ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(Places_adapter.ViewHolder holder, final int position) {
        holder.name.setText(placesarray.get(position).getName());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_map.changeMapsearch(placesarray.get(position).getLatitude()
                                       ,placesarray.get(position).getLongitude());
                toolbar_text.setVisibility(View.VISIBLE);
            }
        });
        toolbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("dist_Latitude", sharedPreferences.getString("lastlat", "0.0"));
                editor.putString("dist_Longitude", sharedPreferences.getString("lastlong", "0.0"));
                editor.putString("dist_name_location", sharedPreferences.getString("placenamex", "0.0"));
                editor.commit();
                //TestModel.isPassengerInTrip(context,RideNow_Activity.class,
                //        "placesActivity",sharedPreferences.getString("passengerID",""));


              Intent      i=new Intent(context, RideNow_Activity.class);



               // i.putExtra("iso",whereIcome);
                i.putExtra("whereIcome","placesActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                context.startActivity(i);
                ((Activity)context).finish();



            }
        });

    }

    @Override
    public int getItemCount() {
        return placesarray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.txt_list_item);
            cv=(CardView)itemView.findViewById(R.id.cv);
        }
    }
}

package solversteam.familycab.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import solversteam.familycab.Activities.GooglePlacesAutocompleteActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;

/**
 * Created by mosta on 5/15/2017.
 */

public class search_adapter extends  RecyclerView.Adapter<search_adapter.ViewHolder> {
    Context context;

    ArrayList<String> resultList = null;
    ArrayList <String> place_id_List = null;
    private  String input,stateID;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LatLng queriedLocation;

    private GoogleApiClient googleApiClient;
    private String from;
    private static final String PLACES_API_BASE="https://maps.googleapis.com/maps/api/place/details";

    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDIWvq3r1zXF7biyZueBfsubuJbDlfrFCw";

    public search_adapter(Context context, ArrayList resultList, ArrayList place_id_list, String from,String stateID) {
        this.context=context;
        this.resultList=resultList;
        this.place_id_List=place_id_list;
        this.from=from;
        this.stateID=stateID;
        sharedPreferences=context.getSharedPreferences("lang_and_count",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        if(from.equals("home"))
            googleApiClient=TestModel.getGoogleApiClienthome();
        else if(from.equals("booking"))
            googleApiClient=TestModel.getGoogleApiClientbooking();

        googleApiClient.connect();
    }


    @Override
    public search_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                ViewHolder pvh = new ViewHolder(v);
                return pvh;
    }

    @Override
    public void onBindViewHolder(search_adapter.ViewHolder holder, final int position) {
        holder.name.setText(resultList.get(position));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Place_id",place_id_List.get(position)+"\n"+googleApiClient);

                get_lat_long_from_place_id(place_id_List.get(position),resultList.get(position));



            }
        });

    }

    private void get_lat_long_from_place_id(String place_id, final String place_name) {

        StringBuilder sb = new StringBuilder(PLACES_API_BASE +OUT_JSON);
        sb.append("?placeid=" + place_id);
        sb.append("&key=" + API_KEY);

        Log.d("add_url", sb.toString());

        Connection connection=new Connection(((Activity)context),sb.toString(),"Get",1);
        connection.reset();
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("checklogin",str);

                JSONObject jsonObj = new JSONObject(str);
                JSONObject result = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
                Log.d("add_url",result.toString());
                // Extract the Place descriptions from the results
                Double longitude  = result.getDouble("lng");
                Double latitude =  result.getDouble("lat");

                Log.d("Latitude_is", "" + latitude);
                Log.v("Longitude_is", "" + longitude);
                editor.putString("dist_Latitude",latitude+"");
                editor.putString("dist_Longitude",longitude+"");
               editor.putString("dist_name_location",place_name);

                editor.commit();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("dist_Latitude",latitude+"");
                returnIntent.putExtra("dist_Longitude",longitude+"");
                returnIntent.putExtra("dist_name_location",place_name);
                returnIntent.putExtra("stateID",stateID);
                ((Activity) context).setResult(GooglePlacesAutocompleteActivity.RESULT_OK,returnIntent);
                ((Activity) context).finish();


            }});

    }


    @Override
    public int getItemCount() {
        return resultList.size();
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

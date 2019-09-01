package solversteam.familycab.Fragments;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import solversteam.familycab.Adapter.History_Adapter;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.History_model;
import solversteam.familycab.R;

/**
 * Created by mosta on 5/4/2017.
 */
public class History_frag extends Fragment {


     private SharedPreferences sharedPreferences;
     private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private ArrayList<History_model> list;
    private History_model history_model;
    private RelativeLayout container;
     private View view;
    private String language,passengerId;
    private boolean isLoaded =false,isVisibleToUser;
    private Boolean key=true,Visable=false;

    public History_frag() {
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(isVisibleToUser && (!isLoaded)){
            isLoaded=true;


                setHistoryData();

        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(Visable);
        this.isVisibleToUser=isVisibleToUser;
        if(isVisibleToUser && isAdded() ){
            isLoaded =true;


                setHistoryData();


        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_history, container, false);

        sharedPreferences=view.getContext().getSharedPreferences("lang_and_count", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putInt("tabnum",0);
        editor.commit();
        list=new ArrayList<>();
        language=sharedPreferences.getString("langID","1");
        passengerId=sharedPreferences.getString("passengerID","6");


        setViews();


    return view;}

    private void setViews() {
        container=(RelativeLayout)view.findViewById(R.id.container_of_no_items);
        recyclerView=(RecyclerView)view.findViewById(R.id.history_recycle);
        llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void setHistoryData() {
        list=new ArrayList<>();

        Connection connection = new Connection(getActivity(), "/Order/GetPassengerRideDetails/"+passengerId+"/"+language, "Get");
        connection.reset();
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("PassengerScheduledTrips", str);

                try {

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray =  jsonObject.getJSONArray("RideDetails");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        Boolean  cancel=false;
                        if (jsonObject1.getString("RideStatusNumber").equals("2")||jsonObject1.getString("RideStatusNumber").equals("2"))
                        {
                            cancel=true;
                        }else  cancel=false;
                        String date="";
                        try{
                         date= jsonObject1.getString("MobDate")+" "+jsonObject1.getString("StartTime");
                        SimpleDateFormat spf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        Date newDate= spf.parse(date);
                        spf= new  SimpleDateFormat("dd MMM , hh:mm ") ;
                        date = spf.format(newDate);}catch (Exception e){
                            date=jsonObject1.getString("MobDate");
                        }
                        Log.d("dkhkdkdhdhk",date);
                        history_model = new History_model(date,
                                jsonObject1.getString("PickupAddress"),
                                jsonObject1.getString("TotalRideFee")+" "+jsonObject1.getString("CurrencyName")
                                , jsonObject1.getString("PickupAddress"),
                                jsonObject1.getString("DropoffAddress"),
                                jsonObject1.getString("DropoffAddress"), jsonObject1.getString("RideID"), cancel);

                        list.add(history_model);
                    }
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(new History_Adapter(view.getContext(),list,"history"));
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });





    }
}
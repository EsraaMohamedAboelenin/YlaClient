package solversteam.familycab.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.text.SimpleDateFormat;
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
public class schdul_frag extends Fragment {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private ArrayList<History_model> list;
    private History_model history_model;
    private RelativeLayout container;
    private String language,passengerId;
    private View view;
    private boolean isLoaded =false,isVisibleToUser;
    private Boolean key=true,Visable=false;

    public schdul_frag() {
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(isVisibleToUser && (!isLoaded)){
            isLoaded=true;
             
            try {
                setSchdualData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(Visable);
        this.isVisibleToUser=isVisibleToUser;
        if(isVisibleToUser && isAdded() ){
            isLoaded =true;
             
            try {
                setSchdualData();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_scheduled, container, false);
        sharedPreferences=view.getContext().getSharedPreferences("lang_and_count", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putInt("tabnum",1);
        editor.commit();

        language=sharedPreferences.getString("langID","1");
        passengerId=sharedPreferences.getString("passengerID","6");

         setViews();

        return view;
    }

    private void setViews() {
        container=(RelativeLayout)view.findViewById(R.id.container_of_no_items);
        recyclerView=(RecyclerView)view.findViewById(R.id.history_recycle);
        llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        container.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.GONE);
    }

    private void setSchdualData() throws ParseException {
        list=new ArrayList<>();

        Connection connection = new Connection(getActivity(), "/driver/GetPassengerScheduledTrips", "Post");
        connection.reset();
        connection.addParmmter("passengerID",passengerId.trim());
        connection.addParmmter("Lang",language.trim());
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("PassengerScheduledTrips", str);

                try {

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray =  jsonObject.getJSONArray("Orders");
                    for (int i=0;i<jsonArray.length();i++) {
                         JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String date= jsonObject1.getString("MobOrderDate")+" "+jsonObject1.getString("OrderTime");
                        SimpleDateFormat spf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        Date newDate= spf.parse(date);
                        spf= new  SimpleDateFormat("dd MMM , hh:mm ") ;
                        date = spf.format(newDate);
                        Log.d("dkhkdkdhdhk",date);
                        history_model = new History_model(date,
                                jsonObject1.getString("PickupAddress"),"", jsonObject1.getString("PickupAddress"),
                                jsonObject1.getString("DropoffAddress"), jsonObject1.getString("DropoffAddress"), jsonObject1.getString("ID"), true);

                        list.add(history_model);
                    }
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(new History_Adapter(view.getContext(),list,"scdule"));
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });


        Log.d("size",list.size()+"");
    }

}

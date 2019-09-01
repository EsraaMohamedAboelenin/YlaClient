package solversteam.familycab.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.SupportMapFragment;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;
import solversteam.familycab.Util.LocaleHelper;

import static solversteam.familycab.R.drawable.lock;

public class RideLater_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener
       {


    private int cDay,cMonth,cYear,cHour,cMinute;
    private SimpleDateFormat sdf,month_date;
    private Date d;
    private String dayOfTheWeek,month_name,am_pm;
    private Double latitude,longitude;
    private TextView big_text,month_txt,firstday,sec_day,third_day,forth_day,time_start,time_later,time_am_pm,toolbar_title;
    private TextView [] txts;
    private ImageView get_clander;
    private LinearLayout get_clock;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog mTimePicker;
    private Calendar calander;
    private int forth,first,sec,third,month,daynum,year;//last days in clander
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private ImageView toolbar_nav_img;
    private Button save;
    private String dayname;
    private Set_Map set_map;
    private Handler handler_premissin;
    private Boolean lockd=false;
           private String iso_code,language,country ;
           @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
               editor=sharedPreferences.edit();
               iso_code=sharedPreferences.getString("iso_code","ar");
               language=sharedPreferences.getString("langID","1");
               country=sharedPreferences.getString("country_id","1");

               Configuration configuration = getResources().getConfiguration();
               configuration.setLayoutDirection(new Locale(iso_code.trim()));
               Context context = LocaleHelper.setLocale(this,iso_code.trim());
               Resources resources = context.getResources();
               getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        setContentView(R.layout.ride_later);
        toolbar=(Toolbar)findViewById(R.id.include);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title.setText(R.string.ride_later_title);
        sharedPreferences=getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        set_map=new Set_Map(this,mapFragment,null);
        // set views
        big_text=(TextView)findViewById(R.id.big_txt_show_date);
        month_txt=(TextView)findViewById(R.id.ride_later_month_txt);
        firstday=(TextView)findViewById(R.id.first_day);
        sec_day=(TextView)findViewById(R.id.sec_day);
        third_day=(TextView)findViewById(R.id.third_day);
        forth_day=(TextView)findViewById(R.id.forth_day);
        time_start=(TextView)findViewById(R.id.time_start);
        time_later=(TextView)findViewById(R.id.time_later);
        time_am_pm=(TextView)findViewById(R.id.time_am_pm);
        get_clander=(ImageView)findViewById(R.id.get_calender);
        get_clock=(LinearLayout)findViewById(R.id.get_clock);
        save=(Button)findViewById(R.id.save_btn) ;
        ///////////////////////////////////////////////////////

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("bigtxt",big_text.getText().toString());
                editor.putString("OrderTime",time_start.getText().toString());
                editor.putString("OrderDate",year+"-"+(month)+"-"+daynum);
                editor.commit();
                try{

                   // intent from ride now to change time of trip
                    if(getIntent().getExtras().get("class").equals("booking"))
                    {
                        onBackPressed();
                    }

                }
                catch (Exception e)
                {
                    // intent from home activity
                Intent i=new Intent(RideLater_Activity.this,RideNow_Activity.class);
                i.putExtra("whereIcome","ride_later");
                startActivity(i);}

            }
        });

        get_clander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        get_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mTimePicker.show();
            }
        });

        calander = Calendar.getInstance();
        cDay = calander.get(Calendar.DAY_OF_MONTH);
        cMonth =  calander.get(Calendar.MONTH);
        cYear =  calander.get(Calendar.YEAR);
        daynum=cDay;
        month=cMonth+1;
        year=cYear;
        cHour =  calander.get(Calendar.HOUR_OF_DAY);
        cMinute =  calander.get(Calendar.MINUTE);
        sdf = new SimpleDateFormat("EEE");
        d = new Date();
        dayOfTheWeek = sdf.format(d);
        Time time = new Time();
        time.setToNow();
        long t=  time.toMillis(false);
        Date afterAddingTenMins=new Date(t + (15 * 60000));
        if (calander.get(Calendar.AM_PM)== Calendar.AM)
            am_pm="AM";
        if (calander.get(Calendar.AM_PM)==Calendar.PM)
            am_pm="PM";

        txts= new TextView[]{firstday, sec_day, third_day, forth_day};
        setbackgroung(0,1,2,3);
        // textView is that TextView  should display it
        for (int i = 0; i < 4; i++) {
            Log.i("dateTag", sdf.format(calander.getTime()));
            txts[i].setText(sdf.format(calander.getTime())+"\n"+
                    calander.get(Calendar.DAY_OF_MONTH)+"/"+(calander.get(Calendar.MONTH)+1));
             if(i==0){
                savefirst(sdf.format(calander.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),
                        sdf.getCalendar().get(Calendar.MONTH)+1,(sdf.getCalendar().get(Calendar.YEAR)));
            }
            else if(i==1){
                savesec(sdf.format(calander.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
            }
            else if(i==2)
            {
                savethird(sdf.format(calander.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
            }
            else if(i==3){
                saveforth(sdf.format(calander.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
            }
            calander.add(Calendar.DAY_OF_WEEK, 1);
        }

        String hournow=""+time.hour;
        String minutenow=""+time.minute;
        String hourlater=""+afterAddingTenMins.getHours();
        String minutelater=""+afterAddingTenMins.getMinutes();
        if (time.hour<10)
            hournow="0"+time.hour;
        if (afterAddingTenMins.getHours()<10)
            hourlater="0"+time.hour;
        if (time.minute<10)
            minutenow="0"+time.minute;
        if (afterAddingTenMins.getMinutes()<10)
            minutelater="0"+afterAddingTenMins.getMinutes();

        time_start.setText(hournow+":"+minutenow);
        time_later.setText(hourlater+":"+minutelater);
        time_am_pm.setText(am_pm);

         Log.d("TIME_TEST", time.hour+"\n"+time.minute+"\n"+afterAddingTenMins.getHours()+afterAddingTenMins.getMinutes());
       month_name = new SimpleDateFormat("MMM").format(calander.getTime()).toString();
//        month_txt.setText(month_name);
        big_text.setText(dayOfTheWeek+","+"\t"+
                          cDay+getResources().getString(R.string.th)+"\t"+month_name+"\t"+getResources().getString(R.string.between)+"\t"+time.hour+":"+time.minute+"\t-\t"+
                afterAddingTenMins.getHours()+":"+afterAddingTenMins.getMinutes());
        editor.putString("starttime",time.hour+":"+time.minute);
        editor.putString("latertime",afterAddingTenMins.getHours()+":"+afterAddingTenMins.getMinutes());
        editor.commit();

        datePickerDialog = new DatePickerDialog(
                this, RideLater_Activity.this, cYear, cMonth, cDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+(24*24 * 60 * 60 * 1000));


        mTimePicker = new TimePickerDialog( this,this ,time.hour, time.minute, false);//Yes 12 hour time

        ///////////////////////////////////////////////////////
        // get last days of calender
        Date dt = new Date();
       calander.setTime(dt);
        calander.add(Calendar.DATE, 24);  // last day in clander
         forth=calander.get(Calendar.DAY_OF_MONTH);
        third=calander.get(Calendar.DAY_OF_MONTH)-1;
         sec=calander.get(Calendar.DAY_OF_MONTH)-2;
         first=calander.get(Calendar.DAY_OF_MONTH)-3;
        Log.d("days",forth+"\n"+first+"\n"+sec+"\n"+third);
///////////////////////////////////////////////////////////////////////

        firstday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbackgroung(0,1,2,3);
                editor.putInt("textnum",1);
                editor.commit();
                setbigtx(1);
            }
        });
        sec_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbackgroung(1,0,2,3);
                editor.putInt("textnum",2);
                editor.commit();
                setbigtx(2);
            }
        });
        third_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbackgroung(2,1,0,3);
                editor.putInt("textnum",3);
                editor.commit();
                setbigtx(3);
            }
        });
        forth_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbackgroung(3,1,2,0);
                editor.putInt("textnum",4);
                editor.commit();
                setbigtx(4);
            }
        });
    }

////////////////////////////////////////////








    /////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////


    @Override
    protected void onStart() {
        super.onStart();

            try {
                TestModel.getGoogleApiClientridelater().connect();
                if (!lockd) {
                    handler_premissin = new Handler();

                    Runnable runnable_premissin = new Runnable() {
                        public void run() {


                            try {
                                get_data_of_my_location();

                                Log.d("kya", "kya11111");
                                handler_premissin.removeCallbacks(this);
                                handler_premissin.removeCallbacksAndMessages(null);
                                lockd = true;
                                Log.d("kya", "kya2222222");
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler_premissin.postDelayed(this, 100);
                                lockd=false;
                                Log.d("kya", "kyaxxxxxx");
                            }


                        }
                    };
                    runnable_premissin.run();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (TestModel.getGoogleApiClientridelater() != null && TestModel.getGoogleApiClientridelater().isConnected()) {
            TestModel.getGoogleApiClientridelater().disconnect();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String dateString = String.format("%d-%d-%d", year, month+1, day);
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("EEE");

        ////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////
         if(day==sec)//last second day of calnder
        {
            setbackgroung(1,0,2,3);
            txts[1].setBackground(getResources().getDrawable(R.drawable.pressedcircle));
            txts[1].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
            savesec(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));

            c.add(Calendar.DATE, -1);
            txts[0].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
            savefirst(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),
                    sdf.getCalendar().get(Calendar.MONTH)+1,(sdf.getCalendar().get(Calendar.YEAR)));

            c.add(Calendar.DATE, 2);  // number of days to add
                txts[2].setText(sdf.format(c.getTime())+"\n"+
                        sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
            savethird(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));

            c.add(Calendar.DATE, 1);  // number of days to add
            txts[3].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
            saveforth(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));

            editor.putInt("textnum",2);
            editor.commit();
            setbigtx(2);
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        else if(day==third){
             setbackgroung(2,1,0,3);
            txts[2].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
             savethird(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
            c.add(Calendar.DATE, -1);
            txts[1].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
             savesec(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
            c.add(Calendar.DATE, -1);  // number of days to add
            txts[0].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
             savefirst(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),
                     sdf.getCalendar().get(Calendar.MONTH)+1,(sdf.getCalendar().get(Calendar.YEAR)));
            c.add(Calendar.DATE, 3);  // number of days to add
            txts[3].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
             saveforth(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
             editor.putInt("textnum",3);
             editor.commit();
             setbigtx(3);
        }
        /////////////////////////////////////////////////////////////////
        else if(day==forth)
        {
            setbackgroung(3,1,2,0);
            txts[3].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
            saveforth(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
            c.add(Calendar.DATE, -1);
            txts[2].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
            savethird(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
            c.add(Calendar.DATE, -1);  // number of days to add
            txts[1].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
            savesec(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
            c.add(Calendar.DATE, -1);  // number of days to add
            txts[0].setText(sdf.format(c.getTime())+"\n"+
                    sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
            savefirst(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),
                    sdf.getCalendar().get(Calendar.MONTH)+1,(sdf.getCalendar().get(Calendar.YEAR)));
            editor.putInt("textnum",4);
            editor.commit();
            setbigtx(4);
        }
        else   {
             setbackgroung(0,1,2,3);
             txts[0].setText(sdf.format(c.getTime())+"\n"+
                     sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
             savefirst(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),
                     (sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
             for (int i=1;i<4;i++){
                 c.add(Calendar.DATE, 1);  // number of days to add
                 txts[i].setText(sdf.format(c.getTime())+"\n"+
                         sdf.getCalendar().get(Calendar.DAY_OF_MONTH)+"/"+(sdf.getCalendar().get(Calendar.MONTH)+1));
                 if(i==1){
                     savesec(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
                 }
                 else if(i==2)
                 {
                     savethird(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),(sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
                 }
                 else if(i==3){
                     saveforth(sdf.format(c.getTime()),sdf.getCalendar().get(Calendar.DAY_OF_MONTH),
                             (sdf.getCalendar().get(Calendar.MONTH)+1),(sdf.getCalendar().get(Calendar.YEAR)));
                 }
                 }
             editor.putInt("textnum",1);
             editor.commit();
             setbigtx(1);
             }

         }



    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        Log.d("yalla_here","yalla_here"+hourOfDay+minute);
        //Display a toast or something to inform the user that he can't pick a past time.
        if (hourOfDay < 12 ) {
             am_pm = "AM";
        }
        else {
            am_pm = "PM";

        }
        editor.putString("am_pm",am_pm);
        String hours_sring=hourOfDay+"";
        String minutes_string =""+minute;
        if (hourOfDay<10)
         hours_sring = "0"+hourOfDay+"";
        if (minute<10)
         minutes_string ="0"+(minute);

        String myTime = hours_sring+":"+minutes_string;
        int minsToAdd = 15;
        Date date = new Date();
        date.setTime((((Integer.parseInt(myTime.split(":")[0]))*60 + (Integer.parseInt(myTime.split(":")[1])))+ date.getTimezoneOffset())*60000);
        System.out.println(date.getHours() + ":"+date.getMinutes());
        date.setTime(date.getTime()+ minsToAdd *60000);
      String hourlater=""+date.getHours();
        String minute_later=""+date.getMinutes();
        if (date.getHours()<10)
            hourlater="0"+date.getHours();
        if (date.getMinutes()<10)
            minute_later="0"+date.getMinutes();
        Log.d("yalla_here","yalla_here"+date.getHours() + ":"+date.getMinutes());
        time_start.setText(hours_sring+":"+minutes_string);
        time_later.setText(hourlater + ":"+minute_later);
        time_am_pm.setText(am_pm);
        editor.putString("starttime",hours_sring+":"+minutes_string);
        editor.putString("latertime",hourlater+ ":"+minute_later);
        editor.commit();
        setbigtx(sharedPreferences.getInt("textnum",1));
    }
    private void setbackgroung(int indx,int i1,int i2,int i3)
    {
        txts[indx].setBackground(getResources().getDrawable(R.drawable.pressedcircle));
        txts[i1].setBackground(getResources().getDrawable(R.drawable.circle));
        txts[i2].setBackground(getResources().getDrawable(R.drawable.circle));
        txts[i3].setBackground(getResources().getDrawable(R.drawable.circle));

    }
    private void setbigtx(int i){
         dayname=sharedPreferences.getString(i+"dayname","sun");
         daynum=sharedPreferences.getInt(i+"daynum",1);
         month=sharedPreferences.getInt(i+"month",1);
         year=sharedPreferences.getInt(i+"year",1);

        String month_name= new DateFormatSymbols().getMonths()[month-1];
        String starttime=sharedPreferences.getString("starttime","20");
        String latertime=sharedPreferences.getString("latertime","2");
        am_pm=sharedPreferences.getString("am_pm","PM");

        big_text.setText(dayname+","+"\t"+
                daynum+getResources().getString(R.string.th)+"\t"+month_name+"\t"+getResources().getString(R.string.between)+"\t"+starttime+"\t-\t"+
                latertime);


    }
    private void savefirst(String dayname,int daynum,int month,int year){
        editor.putString("1dayname",dayname);
        editor.putInt("1daynum",daynum);
        editor.putInt("1month",month);
        editor.putInt("1year",year);
        editor.commit();


    }

    private void savesec(String dayname,int daynum,int month,int year){

        editor.putString("2dayname",dayname);
        editor.putInt("2daynum",daynum);
        editor.putInt("2month",month);
        editor.putInt("2year",year);
        editor.commit();
    }
    private void savethird(String dayname,int daynum,int month,int year){

        editor.putString("3dayname",dayname);
        editor.putInt("3daynum",daynum);
        editor.putInt("3month",month);
        editor.putInt("3year",year);
        editor.commit();
    }
    private void saveforth(String dayname,int daynum,int month,int year){

        editor.putString("4dayname",dayname);
        editor.putInt("4daynum",daynum);
        editor.putInt("4month",month);
        editor.putInt("4year",year);
        editor.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }
           private void get_data_of_my_location() {
               latitude = Double.parseDouble(sharedPreferences.getString("latitude", "0.0"));
               longitude = Double.parseDouble(sharedPreferences.getString("longitude", "0.0"));

               Log.d("latitudelongitude11551",sharedPreferences.getString("streetnamex","44"));
               Log.d("latitudelongitude11551",sharedPreferences.getString("latitude", "0.0"));

               if (latitude != 0.0 && longitude != 0.0) {

                   set_map.where_i_ride(latitude, longitude);
               }
           }
}

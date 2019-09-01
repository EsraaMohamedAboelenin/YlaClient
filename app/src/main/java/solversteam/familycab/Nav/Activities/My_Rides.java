package solversteam.familycab.Nav.Activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import solversteam.familycab.Adapter.ViewPagerAdapter;
import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;

public class My_Rides extends BaseActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    public static My_Rides mActivity;
    private Typeface mBaseTextFont;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_rides);

        sharedPreferences=getSharedPreferences("lang_and_count",MODE_PRIVATE);
        TestModel.isPassengerInTrip(this,null,"",sharedPreferences.getString("passengerID","1"));
        Toolbar toolbar =(Toolbar)findViewById(R.id.include);
        ((TextView)toolbar.findViewById(R.id.tv_home_activity_title)).setText(R.string.myride);
        mActivity = this;
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Log.d("tabs","create");
        int tab_indx=sharedPreferences.getInt("tabnum",0);
        Log.d("tabs",tab_indx+"");
        TabLayout.Tab tab = tabLayout.getTabAt(tab_indx);
        tab.select();
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


package solversteam.familycab.Base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import solversteam.familycab.Adapter.Nav_Menu_adapter;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;


public class BaseActivity extends AppCompatActivity {

    private static final int mDirRight = View.LAYOUT_DIRECTION_RTL;
    private static final int mDirLeft = View.LAYOUT_DIRECTION_LTR;
    private static final int mGravityRight = Gravity.RIGHT;
    private static final int mGravityLeft = Gravity.LEFT;

    protected DrawerLayout mDrawerLayout;
   protected NavigationView mNavigationView;

    protected Typeface mBaseTextFont;

    protected LinearLayout mHomeLayout;
    private TextView mHomeTv;
    private ImageView mHomeIv;

    protected LinearLayout mCurrentLayout;
    private TextView mCurrentTv;
    private ImageView mCurrentIv;

    protected LinearLayout mScheduledLayout;
    private TextView mScheduledTv;
    private ImageView mScheduledIv;

    protected LinearLayout mHistoryLayout;
    private TextView mHistoryTv;
    private ImageView mHistoryIv;

    protected LinearLayout mPromoLayout;
    private TextView mPromoTv;
    private ImageView mPromoIv;

    protected LinearLayout mSettingsLayout;
    private TextView mSettingsTv;
    private ImageView mSettingsIv;

    protected LinearLayout mPlacesLayout;
    private TextView mPlacesTv;
    private ImageView mPlacesIv;

    protected LinearLayout mInterestLayout;
    private TextView mInterestTv;
    private ImageView mInterestIv;

    protected LinearLayout mHelpLayout;
    private TextView mHelpTv;
    private ImageView mHelpIv;

    protected LinearLayout mLogOutLayout;
    private TextView mLogOutTv;
    private ImageView mLogOutIv;
    protected ListView listView;
    private LinearLayout mLoadingLayout;
    private ProgressBar mLoadingProgressBar;
    private ArrayList<String>menu_texts;
    private ArrayList<Integer>menu_icons;
    public Toolbar toolbar;
    public TextView mTitleTv;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String iso_code,language,country;

//    private BaseApi mServiceApi;

    private String mLang;
    public ImageView mNavTv;

    @Override
    public void setContentView(int layoutResID) {


        Log.d("layout",layoutResID+"\n" + R.layout.activity_home);
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");
        if(layoutResID== R.layout.activity_home) {
            Configuration configuration = getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale(iso_code.trim()));
            Context context = LocaleHelper.setLocale(this,iso_code.trim());
            Resources resources = context.getResources();
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }
        super.setContentView(R.layout.activity_base);
        toolbar=(Toolbar)findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

         mTitleTv = (TextView) toolbar.findViewById(R.id.tv_home_activity_title);

        mNavTv = (ImageView) toolbar.findViewById(R.id.menu_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        menu_icons=new ArrayList<>();
        menu_texts=new ArrayList<>();

        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
//        mServiceApi = BaseApiHandler.setupBaseApi().create(BaseApi.class);


        mNavigationView = (NavigationView) findViewById(R.id.navigationView);

        setnav();
        if (mNavigationView != null) {

        }
        mLoadingLayout = (LinearLayout) findViewById(R.id.layout_loading);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loading);

        mNavTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDrawerLayout.isDrawerOpen(GravityCompat.START              ))
                    mDrawerLayout.closeDrawers();
                else
                 mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void setupNavigationView(Context context,String intrip) {
          listView = (ListView) findViewById(R.id.Menu_list);

       // menu_texts.add(getResources().getString(R.string.nav_home_title));

        menu_texts.add(getResources().getString(R.string.my_trips));

        menu_texts.add(getResources().getString(R.string.nav_promo_title));
        menu_texts.add(getResources().getString(R.string.wallet));
        menu_texts.add(getResources().getString(R.string.nav_setting_title));
        menu_texts.add(getResources().getString(R.string.nav_places_title));
        menu_texts.add(getResources().getString(R.string.nav_issus_inbox));
        menu_texts.add(getResources().getString(R.string.contactUs));
        menu_texts.add(getResources().getString(R.string.nav_logout_title));

      //  menu_icons.add(R.drawable.home);

        menu_icons.add(R.drawable.scheduled);

        menu_icons.add(R.drawable.promo);
        menu_icons.add(R.mipmap.wallet);
        menu_icons.add(R.drawable.setting);
        menu_icons.add(R.drawable.places);
        menu_icons.add(R.drawable.issue);
        menu_icons.add(R.drawable.help);
        menu_icons.add(R.drawable.logout);

        listView.setAdapter(new Nav_Menu_adapter(context, menu_texts, menu_icons, mDrawerLayout,intrip));


    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }
    @Override
    protected void onResume() {
        super.onResume();


     }



    private void setnav(){
        Display display =   getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);  // the values are saved in the screenSize object
        final int width = screenSize.x;
        final int height = screenSize.y;
        ViewGroup.LayoutParams params
                = mNavigationView.getLayoutParams();

        params.height = params.MATCH_PARENT;
        params.width = (int) ((width*3) / 4);
        mNavigationView.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {

if(mNavTv.getVisibility() == View.INVISIBLE){}
       else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    protected void showLoading() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);

    }

    protected void hideLoading() {
        mLoadingLayout.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawerLayout.closeDrawers();
    }
}

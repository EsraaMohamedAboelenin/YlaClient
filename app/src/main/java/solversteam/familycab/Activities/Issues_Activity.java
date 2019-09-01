package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import solversteam.familycab.Adapter.Issues_adapter;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Issues;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;


public class Issues_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView toolbarmenue;
    private RecyclerView issues_reRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Issues> issuesArray;
    private Issues_adapter issues_adapter;
    private Connection connection;
    private static final String tag="Issues";
    private String UserID,lang;
    private final  static String toolbarColor="#fcc400";
    private TextView toolbar_title;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String iso_code,language,country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// set application language
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
        setContentView(R.layout.activity_issues);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbarmenue=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);

        setSupportActionBar(toolbar);
        toolbarmenue.setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.parseColor(toolbarColor));
        toolbar_title.setText(getResources().getString(R.string.nav_issus_inbox));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

// intialize views
        setViews();





    }
    // for back in toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();


        return true;
    }


    public void setViews() {
        issuesArray=new ArrayList<>();
        UserID= sharedPreferences.getString("passengerID", "1");
        lang=language;

        issues_reRecyclerView=(RecyclerView)findViewById(R.id.Issues_list_recyclerview);
// get Issues driver
        getIssues();
    }

    private void getIssues() {




        connection=new Connection(this,"/Message/GetAllUserMessage/"+UserID+"/"+lang+"/5","Get");
        connection.reset();
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String jsonResponse) throws JSONException {
                Log.d(tag,"_issuesResponse"+jsonResponse);
                JSONObject jsonObject=new JSONObject(jsonResponse);
                JSONArray jsonArray=jsonObject.getJSONArray("Messages");
                if(jsonArray.length()>0)
                {
                    for (int counter=0;counter<jsonArray.length();counter++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(counter);
                        String issuesHeader=jsonObject1.getString("MessageSubject");
                        String issuesMesage=jsonObject1.getString("MessageBody");
                        String issuesDate=jsonObject1.getString("MobCreationDate");
                        String issuesId=jsonObject1.getString("MessageID");
                        Boolean isRead=jsonObject1.getBoolean("IsRead");
                        issuesArray.add(new Issues(issuesDate,issuesHeader,issuesMesage,issuesId,isRead));
                    }
                    issues_adapter=new Issues_adapter(Issues_Activity.this,issuesArray);
                    linearLayoutManager=new LinearLayoutManager(Issues_Activity.this);
                    issues_reRecyclerView.setLayoutManager(linearLayoutManager);
                    issues_reRecyclerView.setAdapter(issues_adapter);
                }

                else {
                    Log.d(tag,"_issuesResponse"+"Error");

                }


            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                final Dialog  dialog_loading = new Dialog(this);
                dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_loading.setContentView(R.layout.test);
                dialog_loading.getWindow().setBackgroundDrawableResource(R.color.colorBgTransparent);
                dialog_loading.setCancelable(true);
                dialog_loading.setCanceledOnTouchOutside(false);
                LinearLayout relativeLayout=(LinearLayout) dialog_loading.findViewById(R.id.rel_loder);
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // The user just touched the screen

                                dialog_loading.dismiss();
                                break;
                            case MotionEvent.ACTION_UP:
                                // The touch just ended

                                break;
                        }

                        return false;
                    }
                });

                return dialog_loading;
            default:
                return null;
        }
    }
}


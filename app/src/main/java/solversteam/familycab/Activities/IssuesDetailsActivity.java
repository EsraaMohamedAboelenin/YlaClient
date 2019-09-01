package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;


public class IssuesDetailsActivity extends AppCompatActivity  {
    private static final String tag = "IssuesDetails";
    private Toolbar toolbar;
    private TextView toolbar_title,issueSubject_textview,issueDetails_textview,replySubject_textview,replyMessage_textview;
    private ImageView toolbar_nav_img;
    private final  static String toolbarColor="#fcc400";
    private String issueID,issueSubject,issueDetails,replySubject="",replyMessage="",lang,UserID;
    private Connection connection;
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
// get Issue Id
        getIssuesID();


        setContentView(R.layout.activity_issues_details);

// set toolbar attributes
        toolbar=(Toolbar)findViewById(R.id.IssuesDetails_toolbar);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title.setText(getResources().getString(R.string.issuesDetails));
        toolbar.setBackgroundColor(Color.parseColor(toolbarColor));
        UserID= sharedPreferences.getString("passengerID", "1");
// intialize views
        setViews();

    }

    private void getIssuesID() {
        try{
            issueID=getIntent().getExtras().getString("issuesID");
        }
        catch (Exception e )
        {
            e.printStackTrace();
            Log.d(tag,"_issueID: "+issueID);
        }
    }


    public void setViews() {
        issueSubject_textview=(TextView)findViewById(R.id.IssuesDetails_issueSubject_textview);
        issueDetails_textview=(TextView)findViewById(R.id.IssuesDetails_issueDetails_textview);
        replySubject_textview=(TextView)findViewById(R.id.IssuesDetails_replySubject_textview);
        replyMessage_textview=(TextView)findViewById(R.id.IssuesDetails_replyMessage_textview);
        replySubject_textview.setVisibility(View.GONE);
        replyMessage_textview.setVisibility(View.GONE);
        lang=language;
// get Issues Details
        getIssuesDetails();


    }
    // for back in toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getIssuesDetails() {
        issueDetails="";
        issueSubject="";
        replySubject="";
        replyMessage="";
//
        connection=new Connection(this,"/Message/GetMessageDetails/"+issueID+"/"+lang+"/1","Get");
        connection.reset();
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String jsonResposne) throws JSONException {
                Log.d(tag,"_issueDetailsResponse:"+jsonResposne);
                JSONObject jsonObject=new JSONObject(jsonResposne);
                JSONArray jsonArray=jsonObject.getJSONArray("Messages");
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.getString("FromUserID").equals(UserID)) {
                        issueSubject = jsonObject1.getString("MessageSubject");
                        issueDetails = jsonObject1.getString("MessageBody");
                    }
                    else {
                        replySubject = jsonObject1.getString("MessageSubject");
                        replyMessage = jsonObject1.getString("MessageBody");
                    }
                }

// set reuslt
                setIssueDetauls();

            }
        });
    }

    private void setIssueDetauls() {
        issueSubject_textview.setText(issueSubject);
        issueDetails_textview.setText(issueDetails);
        if (replySubject.isEmpty() ||replySubject.equals("null")) {
            replySubject_textview.setVisibility(View.GONE);
            replyMessage_textview.setVisibility(View.GONE);
            Toast.makeText(this,R.string.noreplay,Toast.LENGTH_LONG).show();
        }else {
            replySubject_textview.setVisibility(View.VISIBLE);
            replyMessage_textview.setVisibility(View.VISIBLE);
            replySubject_textview.setText(replySubject);
            replyMessage_textview.setText(replyMessage);
        }

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


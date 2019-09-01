package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.R;

public class Contact_Us extends AppCompatActivity implements View.OnClickListener {
    private EditText name_editext,subject_editext,message_edittext;
    private Button send_button;
    private String name,subject,message,tripID,passengerID,toUser;
    private Connection connection;
    private static final String Tag="ContactUS";
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_nav_img;
    private final  static String toolbarColor="#fcc400";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);
        sharedPreferences=getSharedPreferences("lang_and_count",MODE_PRIVATE);
// set toolbar attributes
        toolbar=(Toolbar)findViewById(R.id.include);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar_title.setText(getResources().getString(R.string.notes));
// intialize
        setViews();
    }
    // for back in toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private void setViews() {
        passengerID=sharedPreferences.getString("passengerID", "1");
        name_editext=(EditText)findViewById(R.id.ContactUs_name_edittext);
        subject_editext=(EditText)findViewById(R.id.ContactUs_subject_edittext);
        message_edittext=(EditText)findViewById(R.id.ContactUs_message_edittext);
        send_button=(Button)findViewById(R.id.ContactUs_send_button);
        send_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==send_button)
        {
            try{
                name=name_editext.getText().toString();
                subject=subject_editext.getText().toString();
                message=message_edittext.getText().toString();
                Log.d(Tag+"_jsonResponse","name:"+name+"\n"+"subject:"+subject+"\n"+"message:"+message);

                if(name.isEmpty()||subject.isEmpty()||message.isEmpty())
                {
                    Toast.makeText(this,getResources().getString(R.string.messageReply),Toast.LENGTH_SHORT).show();
                }
                else {
                    SendMessage(name,subject,message);
                }

            }
            catch (Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    private void SendMessage(String name,String subject,String message) {
        try {
            //admin id
            toUser = "1086";

            connection = new Connection(Contact_Us.this, "/Message/insert", "Post");
            connection.reset();
            connection.addParmmter("FromUserID", passengerID);
            connection.addParmmter("ToUserID", toUser);
            connection.addParmmter("Body", message);
            connection.addParmmter("Subject",subject);
            connection.addParmmter("RideID","");
            connection.addParmmter("MessageCategoryID","5");
            connection.addParmmter("MessagetypeID","2");
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String jsonResponse) throws JSONException {
                    Log.d(Tag+"_jsonResponse", jsonResponse);
                    Toast.makeText(Contact_Us.this,getResources().getString(R.string.message_send),Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
        catch (Exception e)
        {
//          Log.d(Tag+"_jsonResponse", "Error);
            e.printStackTrace();
        }
    }


}

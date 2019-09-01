package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
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

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.R;
import solversteam.familycab.Util.Validation;


/**
 * Created by mosta on 5/16/2017.
 */

public class Change_Pass extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView toolbar_title  ;
    private ImageView toolbar_nav_img;
    private EditText currentPass_editext,newPass_editext,confirmPass_editext;
    private String currentPass,newPass,confirmPass,userID,old_pass;
    private Button edit_button;
    private Connection connection;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// set application language
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        setContentView(R.layout.change_pass);
// set toolbar attributes
        toolbar=(Toolbar)findViewById(R.id.include);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title.setText(R.string.changepass);

        old_pass=sharedPreferences.getString("Password","0");
        userID=sharedPreferences.getString("passengerID","1");

// intialize views
        setViews();
    }

    private void setViews() {
        currentPass_editext=(EditText)findViewById(R.id.current_pass);
        newPass_editext=(EditText)findViewById(R.id.new_pass);
        confirmPass_editext=(EditText)findViewById(R.id.confirm_pass);
        edit_button=(Button) findViewById(R.id.save_btn);
        edit_button.setOnClickListener(this);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(View v) {
        if(v==edit_button)
        {
            try{
                currentPass=currentPass_editext.getText().toString();
                newPass=newPass_editext.getText().toString();
                confirmPass=confirmPass_editext.getText().toString();
                Log.d("ChangePassword","current:"+currentPass+"\n"+"newPass:"+newPass+"\n"+"confirmPass:"+confirmPass);

                if(currentPass.equals(old_pass))
                {
                    if(Validation.validate(newPass_editext,"password",getResources().getString(R.string.passwordReply)))
                    {
                        if(Validation.validate(confirmPass_editext,"confirm_password",getResources().getString(R.string.edit_pass)))
                        {
                            changePassword(newPass,confirmPass,currentPass);
                        }
                    }
                }
                else {
                    currentPass_editext.setError(getResources().getString(R.string.curentPasswordReply));
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void changePassword(final String newPass, String confirmPass, String currentPass) {
        try {
            connection = new Connection(this, "/user/changePassword", "Post");
            connection.reset();
            connection.addParmmter("ReachUserID", userID);
            connection.addParmmter("CurrentPassword", currentPass);
            connection.addParmmter("NewPassword", newPass);
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String jsonResponse) throws JSONException {
                    Log.d("jsonResponse", jsonResponse);
                   editor.putString("Password",newPass).commit();
                    Toast.makeText(Change_Pass.this,R.string.pass_change,Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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


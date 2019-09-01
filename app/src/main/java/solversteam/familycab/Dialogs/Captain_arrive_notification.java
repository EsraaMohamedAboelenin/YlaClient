package solversteam.familycab.Dialogs;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import solversteam.familycab.Activities.RideNow_Activity;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.CurrentActivity;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.R;
import solversteam.familycab.Util.NotificationUtils;

/**
 * Created by mosta on 9/27/2017.
 */


public class Captain_arrive_notification extends AppCompatActivity {
    private ImageView captain_image,close_button;
    private TextView message,captain_name;
    private String message_string,captain_name_string,captain_img_string,key_var,order_id;
    private Intent intent;
    private NotificationUtils notificationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_captain_arrive);
        TestModel.setOn_ride(false);

        order_id=getIntent().getExtras().getString("orderid");
        // for make activity as dalog
        this.setFinishOnTouchOutside(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notificationUtils=new NotificationUtils (this);
        captain_image=(ImageView)findViewById(R.id.captainimage);
        message=(TextView)findViewById(R.id.message);
        captain_name=(TextView)findViewById(R.id.captainname);
        close_button=(ImageView)findViewById(R.id.close_button);
        setimage();
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (key_var.equals("gcm.notification.cancelorder")) {

                    TestModel.setOn_ride(false);
                    TestModel.setArrive(false);

                    intent= new Intent(Captain_arrive_notification.this, RideNow_Activity.class);
                    intent.putExtra("whereIcome","cancel");
                    intent.putExtra("order_id",order_id);
                    startActivity(intent);

                     // intent = new Intent(Captain_arrive_notification.this, HomeActivity.class);

                }



                else{
                    TestModel.setOn_ride(false);
                    TestModel.setArrive(true);

                    intent = new Intent(Captain_arrive_notification.this, CurrentActivity.class);
                    intent.putExtra("order_id",order_id);
                    Log.d("current","current");
                }


                notificationUtils.clearNotifications(Captain_arrive_notification.this);
                startActivity(intent);
                ActivityCompat.finishAffinity(Captain_arrive_notification.this);
            }
        });
        // Alert
         final MediaPlayer mp = MediaPlayer.create(this, R.raw.hangout);
         mp.start();

        try
        {
            captain_name_string=getIntent().getExtras().getString("captain_name");
            message_string=getIntent().getExtras().getString("message");
            captain_img_string=getIntent().getExtras().getString("captain_img");
            key_var=getIntent().getExtras().getString("key_var");


        }catch (Exception e){

            e.printStackTrace();
        }
        Log.d("vbvbvbvb",message_string+"\t"+captain_name_string);
        try {
            Picasso.with(this).load(captain_img_string).placeholder(R.drawable.prof_placeholder).into(captain_image);

        }catch (Exception e)
        {
            Picasso.with(this).load(R.drawable.prof_placeholder).into(captain_image);

            e.printStackTrace();
        }
        try {
            captain_name.setText(captain_name_string);
        }catch (Exception e)
        {

            e.printStackTrace();
        }
        try {
            message.setText(message_string);
        }catch (Exception e)
        {

            e.printStackTrace();
        }
    }

    private void setimage(){
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);  // the values are saved in the screenSize object
        final int width = screenSize.x;
        final int height = screenSize.y;
        ViewGroup.LayoutParams params
                = captain_image.getLayoutParams();
        params.height = (int) (width /7);
        params.width = (int) (width / 7);
        captain_image.setLayoutParams(params);
    }


    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(Captain_arrive_notification.this);

    }
}

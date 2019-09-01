package solversteam.familycab.Nav.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import solversteam.familycab.Base.BaseActivity;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;

public class PromoActivity extends BaseActivity {

    public static PromoActivity mActivity;
    private Typeface mBaseTextFont;
    private TextView toolbar_title,promo_headline_text,promo_body_text,promo_code_text,copy_code_text;
    private ImageView promo_image;
    private LinearLayout invite_button;
    private Toolbar toolbar;
    private ImageView toolbar_nav_img;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        mActivity = this;
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");

        setviews();
        sharedPreferences=getSharedPreferences("lang_and_count",MODE_PRIVATE);
        TestModel.isPassengerInTrip(this,null,"",sharedPreferences.getString("passengerID","1"));
        toolbar_title.setText(R.string.nav_promo_title);
        copy_code_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(PromoActivity.this.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", promo_code_text.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

        invite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "https://play.google.com/store/apps/details?id=solversteam.yallapassenger";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Family cap");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent,"share"));
            }
        });


    }

    private void setviews() {
        toolbar=(Toolbar)findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        promo_headline_text=(TextView)findViewById(R.id.promo_headline_text);
        promo_body_text=(TextView)findViewById(R.id.promo_body_text);
        promo_code_text=(TextView)findViewById(R.id.promo_code_text);
        copy_code_text=(TextView)findViewById(R.id.copy_code_text);
        promo_image=(ImageView)findViewById(R.id.promo_image);
        invite_button=(LinearLayout)findViewById(R.id.invite_friend_button);
    }

}

package solversteam.familycab.Nav.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import solversteam.familycab.R;

public class HelpActivity extends AppCompatActivity {

    public static HelpActivity mActivity;
    private Typeface mBaseTextFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mActivity = this;
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");

    }
 }

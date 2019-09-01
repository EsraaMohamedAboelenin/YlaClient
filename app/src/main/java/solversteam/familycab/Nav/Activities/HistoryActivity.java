package solversteam.familycab.Nav.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import solversteam.familycab.R;

public class HistoryActivity extends AppCompatActivity {

    public static HistoryActivity mActivity;
    private Typeface mBaseTextFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mActivity = this;
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");

    }

    public static void close(){
        mActivity.finish();
    }

}

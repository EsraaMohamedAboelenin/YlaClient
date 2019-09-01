package solversteam.familycab.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import solversteam.familycab.R;


/**
 * Created by mosta on 4/24/2017.
 */

public class TextViewWithFont extends TextView {
    private int defaultDimension = 0;
    private int TYPE_BOLD = 1;
    private int TYPE_ITALIC = 2;
    private int FONT_ARIAL = 1;
    private int FONT_OPEN_SANS = 2;
    private int fontType;
    private int fontName;


    private static final String TAG = "TextView";

    public TextViewWithFont(Context context) {
        super(context);
    }

    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewWithFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        String customFont = a.getString(R.styleable.TextViewPlus_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), "29LTBukra_Regular.ttf");
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

}
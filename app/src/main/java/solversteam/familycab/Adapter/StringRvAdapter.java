package solversteam.familycab.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import solversteam.familycab.Activities.SplashScreenActivity;
import solversteam.familycab.Models.Country;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;

/**
 */

public class StringRvAdapter extends RecyclerView.Adapter<StringRvAdapter.ViewHolder> {

    private List<Country> mList;
    private Context mContext;
    private Typeface mTextFont;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Dialog dialog;

    public StringRvAdapter(Context context, List<Country> list, Dialog dialog) {
        mContext = context;
        mList = list;
        this.dialog=dialog;
        mTextFont = Typeface.createFromAsset(mContext.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
        sharedPreferences=context.getSharedPreferences("lang_and_count",mContext.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
          View mView;
          TextView tvName;
        ImageView tvImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tvName = (TextView) view.findViewById(R.id.lang_name);
            tvImage= (ImageView) view.findViewById(R.id.lang_image);
        }

    }
    @Override
    public StringRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.langauge_item, parent, false);
        return new StringRvAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(final StringRvAdapter.ViewHolder holder, int position) {
        final int pos = position;
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);  // the values are saved in the screenSize object
        final int width = screenSize.x;
        final int height = screenSize.y;
        ViewGroup.LayoutParams params
                = holder.tvImage.getLayoutParams();
        params.height = height /13;
        params.width = (int) (width /2);
        holder.tvImage.setLayoutParams(params);

        if (sharedPreferences.getString("langID","1").equals(mList.get(pos).getID())){
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        holder.tvName.setText(mList.get(pos).getName());
        try {
            Picasso.with(mContext).
                    load(mList.get(position).getCountry_img())
                    .placeholder(R.drawable.egypt).into(holder.tvImage);
        }catch (Exception e){}
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedPreferences.getString("langID","1").equals(mList.get(pos).getID())){
                editor.putString("iso_code",mList.get(pos).getIso_code());
                editor.putString("langID",mList.get(pos).getID());
                editor.putString("langname",mList.get(pos).getName());
                editor.commit();
                refresh(mList.get(pos).getIso_code());
                }
                else
                    dialog.dismiss();

            }
        });

    }
    private void refresh(String iso_code) {

        Log.d("isocode",iso_code);
        Configuration configuration = mContext.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code));
        Context context = LocaleHelper.setLocale(mContext, iso_code);
        Resources resources = context.getResources();
        mContext.getResources().updateConfiguration(configuration, mContext.getResources().getDisplayMetrics());

        Intent i = new Intent(mContext, SplashScreenActivity.class);
        i.putExtra("iso",iso_code);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      try{  ActivityCompat.finishAffinity((Activity) context);}catch (Exception e){e.printStackTrace();}

        mContext.startActivity(i);
        ((Activity) mContext).finish();
    }
}

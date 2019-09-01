package solversteam.familycab.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import solversteam.familycab.Activities.Issues_Activity;
import solversteam.familycab.Activities.LoginActivity;
import solversteam.familycab.Activities.Chat;
import solversteam.familycab.Models.Issues;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.Wallet;
import solversteam.familycab.Nav.Activities.CurrentActivity;
import solversteam.familycab.Nav.Activities.HelpActivity;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.Activities.Rate_Captain_activity;
import solversteam.familycab.Nav.Activities.My_Rides;
import solversteam.familycab.Nav.Activities.PlacesActivity;
import solversteam.familycab.Nav.Activities.PromoActivity;
import solversteam.familycab.Nav.Activities.SettingsActivity;
import solversteam.familycab.R;

/**
 * Created by mosta on 5/1/2017.
 */

public class Nav_Menu_adapter extends BaseAdapter  {
    private ArrayList<String> menu_txts;
    private ArrayList<Integer> menu_icons;
    private Context context;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TabLayout tabLayout;
    private MediaPlayer mMediaPlayer;
    private String intrip,passengerId;

    public Nav_Menu_adapter(Context context, ArrayList<String> menu_txts,
                            ArrayList<Integer> menu_icons, DrawerLayout mDrawerLayout,String intrip)
    {
        this.context=context;
        this.menu_icons=menu_icons;
        this.menu_txts=menu_txts;
        this.mDrawerLayout=mDrawerLayout;
        sharedPreferences=context.getSharedPreferences("lang_and_count",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        this.intrip=intrip;
        passengerId=sharedPreferences.getString("passengerID","6");


    }
    @Override
    public int getCount() {
        return menu_icons.size();
    }

    @Override
    public Object getItem(int i) {
        return menu_txts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View row = convertView;
        ViewHold holder = null;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.menue_item, viewGroup, false);
            holder = new ViewHold(row);
            row.setTag(holder);
        } else {
            holder = (ViewHold) row.getTag();
        }

        holder.txts.setText(menu_txts.get(i));
        holder.icons.setImageResource(menu_icons.get(i));


        final Class [] arry={HomeActivity.class, CurrentActivity.class, My_Rides.class, My_Rides.class, PromoActivity.class
        , SettingsActivity.class, PlacesActivity.class, Rate_Captain_activity.class, HelpActivity.class};
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("ymosahel",intrip);
              /*  if(i==0)
                {



                    if (intrip.equals("true")){
                        if (context instanceof CurrentActivity){
                            mDrawerLayout.closeDrawers();
                        }else {

                            Intent intent=new Intent(context,CurrentActivity.class);
                            context.startActivity(intent);

                        }
                    }


                    else {if (context instanceof HomeActivity){
                        mDrawerLayout.closeDrawers();
                    }
                    else {

                        Intent intent=new Intent(context,HomeActivity.class);
                        context.startActivity(intent);

                    }


                    }

                }
                 */

                 if (i==0){

                    editor.putInt("tabnum",1);
                    editor.commit();
                    if (context instanceof My_Rides){
                        mDrawerLayout.closeDrawers();
                        get_tab(0);
                    }else {

                        Intent intent=new Intent(context,My_Rides.class);
                        context.startActivity(intent);

                         }
                }

                else if (i==1){

                    if (context instanceof PromoActivity){
                        mDrawerLayout.closeDrawers();
                    }else {

                        Intent intent=new Intent(context,PromoActivity.class);
                        context.startActivity(intent);

                        }}
                else if (i==2){

                    if (context instanceof Wallet){
                        mDrawerLayout.closeDrawers();
                    }else {

                        Intent intent=new Intent(context,Wallet.class);
                        intent.putExtra("comefrom","Nav_Menu_adapter");
                        context.startActivity(intent);

                        }}
                else if (i==3){

                    if (context instanceof SettingsActivity){
                        mDrawerLayout.closeDrawers();
                    }else {

                        Intent intent=new Intent(context,SettingsActivity.class);
                        context.startActivity(intent);

                    }}
                else if (i==4){

                    if (context instanceof PlacesActivity){
                        mDrawerLayout.closeDrawers();
                    }else {

                        Intent intent=new Intent(context,PlacesActivity.class);
                        context.startActivity(intent);

                        }}
                else {
                    if (i == 5) {

                        if (context instanceof Rate_Captain_activity) {
                            mDrawerLayout.closeDrawers();
                        } else {

                            Intent intent=new Intent(context, Issues_Activity.class);
                            context.startActivity(intent);
                         /*   String url = "https://soundcloud.com/discover";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                           context. startActivity(i);
*/


                        }

                    } else if (i == 6) {

                        if (context instanceof HelpActivity) {
                            mDrawerLayout.closeDrawers();
                        } else {

                            show_help_dialog();
                            mDrawerLayout.closeDrawers();
                        }
                    } else if (i == 7) {

                        if (context instanceof LoginActivity) {
                            mDrawerLayout.closeDrawers();
                        } else {



                           log_out(context);

                        }
                    } else {
                        mDrawerLayout.closeDrawers();
                    }
                }

            }
        });

        return row;
    }
    private void log_out(final Context context){


        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context).create();
        View customView = LayoutInflater.from(context).inflate(
                R.layout.custom_dialog, null, false);
        Button mOk = (Button) customView.findViewById(R.id.yes);
        Button mNo = (Button) customView.findViewById(R.id.no);
        TextView mQuesition = (TextView) customView.findViewById(R.id.question);
        mOk.setText(context.getResources().getString(R.string.yes));
        mNo.setText(context.getResources().getString(R.string.No));
        mQuesition.setText(context.getResources().getString(R.string.question));

        dialog.setView(customView);
        dialog.show();
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("navigate", "helllo");
                Intent intent = new Intent(((Activity) context), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                editor.putString("passengerID","").commit();
                context.startActivity(intent);
                ActivityCompat.finishAffinity((Activity) context);
                Log.d("navigate", "helllo");
            }
        });

mNo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        dialog.dismiss();
    }
});
    }

    private void show_help_dialog() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_help);
        TextView callus=(TextView)dialog.findViewById(R.id.call_us_text) ;
        TextView emailus=(TextView)dialog.findViewById(R.id.email_us_text) ;
        ImageButton close=(ImageButton) dialog.findViewById(R.id.close_button) ;
        ImageView image_contact=(ImageView) dialog.findViewById(R.id.image_contact) ;
         setimage(image_contact,0.0,1.5);
         setimage(close,10.0,10.0);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +"011012345617" ));
                context.startActivity(intent);
            }
        });

        emailus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


private void setimage(ImageView imageView,Double widt,Double high){
    Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
    Point screenSize = new Point();
    display.getSize(screenSize);  // the values are saved in the screenSize object
    final int width = screenSize.x;
    final int height = screenSize.y;
    ViewGroup.LayoutParams params
            = imageView.getLayoutParams();
    params.height = (int) (width /high);
    if (widt==0.0)
    params.width = params.WRAP_CONTENT;
    else
        params.width =(int) (width /widt);
                imageView.setLayoutParams(params);
}
    class ViewHold {
        ImageView icons;
        TextView txts;

        ViewHold(View v) {
            icons = (ImageView) v.findViewById(R.id.nav_home_img);
            txts = (TextView) v.findViewById(R.id.nav_home_tv);


        }
    }
 public void get_tab(int index){
     tabLayout = (TabLayout) ((Activity)context).findViewById(R.id.tabs);
     TabLayout.Tab tab = tabLayout.getTabAt(index);
     tab.select();
 }
}

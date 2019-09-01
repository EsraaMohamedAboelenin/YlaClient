package solversteam.familycab.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import solversteam.familycab.Models.Country;
import solversteam.familycab.R;

/**
 * Created by mosta on 9/12/2017.
 */

public class CountryAdapter  extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private List<Country> mList;
    private Context mContext;
    private Typeface mTextFont;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Dialog dialog;
    private TextView countryName;
    private  ImageView country_image;
    private String country;

    public CountryAdapter(Context mContext, List<Country> mList, Dialog dialog,
                          TextView countryName, ImageView country_image, String country){
       this.mContext=mContext;
        this.mList=mList;
        this.countryName=countryName;
        this.country_image=country_image;
        this.dialog=dialog;
        this.country=country;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.country_name.setText(mList.get(position).getName());
        if (mList.get(position).getID().equals(country))
        {
            setdata(position);
        }

        holder.country_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               setdata(position);
            }
        });

    }

    private void setdata(int position) {
        countryName.setText(mList.get(position).getName());
        try {
            Picasso.with(mContext).load(mList.get(position).getCountry_img())
                    .placeholder(R.drawable.egypt).into(country_image);
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView country_name;
        public ViewHolder(View itemView) {
            super(itemView);
            country_name=(TextView)itemView.findViewById(R.id.country_item_text);
        }
    }
}

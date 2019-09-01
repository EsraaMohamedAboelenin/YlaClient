package solversteam.familycab.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import solversteam.familycab.Models.Country;
import solversteam.familycab.R;

/**
 * Created by mosta on 5/2/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private  ArrayList<Country> contentArray;



    public SpinnerAdapter(Context context, ArrayList<Country> objects, ArrayList<String> code) {
        super(context,R.layout.post_code_item, code);
        this.ctx = context;
        this.contentArray = objects;
        Log.d("codecode",code.get(0));
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.post_code_item, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.post_code_text);
        ImageView img =(ImageView)row.findViewById(R.id.flag);
        textView.setText(contentArray.get(position).getPhonecountryCode());
        try {
            Picasso.with(ctx).
                    load(contentArray.get(position).getCountry_img())
                    .placeholder(R.drawable.egypt).resize(40,25).into(img);
        }catch (Exception e){}


        return row;
    }
}
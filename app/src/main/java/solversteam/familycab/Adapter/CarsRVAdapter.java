package solversteam.familycab.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import solversteam.familycab.Models.Cars_items;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;
import solversteam.familycab.Util.Handler_Cars;

/**
 * Created by mosta on 5/1/2017.
 */

public class CarsRVAdapter extends RecyclerView.Adapter<CarsRVAdapter.ViewHolder>  {

    Context context ;
    ArrayList<Cars_items> cars_list;
    int pos=-1;
    Set_Map set_map;
    private Boolean first_one=true;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Boolean displayed=false;
    public CarsRVAdapter(Context context, ArrayList<Cars_items> cars_list){
       this.context=context;
        this.cars_list=cars_list;


        sharedPreferences=context.getSharedPreferences("lang_and_count",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        Log.d("CarsAdapter",cars_list.size()+"");
    }

    public Boolean getDisplayed() {
        return displayed;
    }


    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d("CarsAdapter","viewHolder");

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.car_item, viewGroup, false);
        ViewHolder pvh = new  ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final CarsRVAdapter.ViewHolder holder, final int position) {
        final Cars_items temp=cars_list.get(position);

        Log.d("CarsAdapter","onBind!");

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);  // the values are saved in the screenSize object
        final int width = screenSize.x;
        final int height = screenSize.y;
        ViewGroup.LayoutParams params
                = holder.car_img.getLayoutParams();
        ViewGroup.LayoutParams params2
                = holder.cv.getLayoutParams();
        params.height = height /13;
        params.width = (int) (width / 4.5);
        holder.car_img.setLayoutParams(params);


        try {
            holder.car_txt.setText(temp.getName());
         }
        catch (Exception e){
            e.printStackTrace();
            Log.d("contexterror",e.toString());
        }
        try {
            Picasso.with(context).load(temp.getImg())
                    .placeholder(R.drawable.normal)
                    .into(holder.car_img);

            if (position !=pos) {
                Log.d("pos1123",pos+"\n"+position);


                holder.car_img.setColorFilter(ContextCompat.getColor(context,R.color.bgLayoutBorderColor));


            }else
            {
                holder.car_img.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
                TestModel.setImag_car(holder.car_img);
            }


            if (first_one){
                // first one for implemnt adapter >> position 0 is defult
                holder.car_img.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
                TestModel.setImag_car(holder.car_img);
                first_one=false;
                pos=position;

            }

       }

        catch (Exception e){
            e.printStackTrace();
            Log.d("contexterror",e.toString());
        }
        displayed=true;

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler_Cars(context);
                try {
                    TestModel.getImag_car().setColorFilter(ContextCompat.getColor(context,R.color.bgLayoutBorderColor));
                }catch (Exception e){}
                pos=position;
                holder.car_img.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
                TestModel.setImag_car(holder.car_img);
                TestModel.getRide_now().setVisibility(View.INVISIBLE);
                TestModel.getRide_later().setVisibility(View.INVISIBLE);
                if (TestModel.getCars_caegory()!=null)
                for (int i=0;i<TestModel.getCars_caegory().size();i++)
                {
                    if (temp.getId().equals(TestModel.getCars_caegory().get(i).getId()))
                    {
                        Log.d("clickhere123",TestModel.getCars_caegory().get(i).getBooking_type_number() );
                        if (TestModel.getCars_caegory().get(i).getBooking_type_number().equals("1"))
                            TestModel.getRide_now().setVisibility(View.VISIBLE);
                        else  if (TestModel.getCars_caegory().get(i).getBooking_type_number().equals("2"))
                            TestModel.getRide_later().setVisibility(View.VISIBLE);




                    }
                }

                 Log.d("clickhere",temp.getId()+"\n"+temp.getName());
                editor.putString("car_type_id",temp.getId());
                try {
                    editor.putString("bookingtype_id_later", TestModel.getCars_caegory().get(1).getBooking_type_id());
                    editor.putString("bookingtype_id_now", TestModel.getCars_caegory().get(0).getBooking_type_id());
                }catch (Exception e){}
                editor.commit();
                //new Handler_Cars(context);
            }
        });



    }

    @Override
    public int getItemCount() {

        return cars_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView car_img;
        TextView car_txt;
        public ViewHolder(View itemView) {
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.cv);
            car_img=(ImageView)itemView.findViewById(R.id.car_img_recycle);
            car_txt=(TextView) itemView.findViewById(R.id.car_txt_recycle);
        }
    }
}

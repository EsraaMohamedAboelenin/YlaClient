package solversteam.familycab.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import solversteam.familycab.Activities.Scdule_item_details;
import solversteam.familycab.Models.History_model;
import solversteam.familycab.Activities.History_item_dtails;
import solversteam.familycab.R;

/**
 * Created by mosta on 5/11/2017.
 */

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.MyViewHolder> {
   private Context context;
   private ArrayList<History_model> list;
    private String from;
    private Intent intent;

    public History_Adapter(Context context, ArrayList<History_model> list, String from) {
        this.context=context;
        this.list=list;
        this.from=from;
    }

    @Override
    public History_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(History_Adapter.MyViewHolder holder, final int position) {
        holder.date.setText(list.get(position).getDate());
        holder.here_add.setText(list.get(position).getHere_add());
        holder.here_add_details.setText(list.get(position).getHere_add_details());
        holder.where_add.setText(list.get(position).getWhere_add());
        holder.where_add_details.setText(list.get(position).getWhere_add_details());

        if (from.equals("history")){
        holder.cost.setText(list.get(position).getCost());
        if (list.get(position).getcanceled())
        {
            holder.status.setVisibility(View.INVISIBLE);
            holder.warnning_img.setVisibility(View.INVISIBLE);
        }
        else {
            holder.status.setVisibility(View.INVISIBLE);
            holder.warnning_img.setVisibility(View.INVISIBLE);
        }
        }
        else {
            holder.status.setVisibility(View.INVISIBLE);
            holder.warnning_img.setVisibility(View.INVISIBLE);
            holder.cost.setVisibility(View.INVISIBLE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equals("history")) {
                     intent = new Intent(context, History_item_dtails.class);
                }else {
                    intent = new Intent(context, Scdule_item_details.class);
                }
                intent.putExtra("order_id",list.get(position).getOrder_id());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder {
        TextView date,cost,here_add,here_add_details,where_add,where_add_details,status;
        CardView cardView;
        ImageView warnning_img;
        public  MyViewHolder(View item){
            super(item);
            date=(TextView)item.findViewById(R.id.history_date);
            cost=(TextView)item.findViewById(R.id.cost_text);
            here_add=(TextView)item.findViewById(R.id.history_iam_here_address);
            here_add_details=(TextView)item.findViewById(R.id.history_iam_here_address_details);
            where_add=(TextView)item.findViewById(R.id.history_where_address);
            where_add_details=(TextView)item.findViewById(R.id.history_where_address_details);
            status=(TextView)item.findViewById(R.id.cancel_text);
            warnning_img=(ImageView)item.findViewById(R.id.warning_img) ;

            cardView=(CardView)item.findViewById(R.id.cv);


        }
    }
}

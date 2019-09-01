package solversteam.familycab.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import solversteam.familycab.Models.Chat_model;
import solversteam.familycab.R;

/**
 * Created by Nashaat on 10/30/2017.
 */

public class Chat_adapter extends RecyclerView.Adapter<Chat_adapter.ViewHolder>  {
    private ArrayList <Chat_model> chat_list ;
    public Chat_adapter(ArrayList <Chat_model> chat_list){
        this.chat_list=chat_list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (chat_list.get(position).getMsg_id().equals("0"))
        {
            holder.cntainer.setVisibility(View.VISIBLE);
            holder.sendto.setVisibility(View.GONE);
            holder.sendfrom.setText(chat_list.get(position).getMessage());
        }else {
            holder.cntainer.setVisibility(View.GONE);
            holder.sendto.setVisibility(View.VISIBLE);
            holder.sendto.setText(chat_list.get(position).getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cntainer;
        TextView sendfrom,sendto;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            cntainer=(LinearLayout)itemView.findViewById(R.id.send_from_container);
            sendfrom=(TextView)itemView.findViewById(R.id.chat_item_buble_out);
            sendto=(TextView)itemView.findViewById(R.id.chat_item_buble_in);
            imageView=(ImageView)itemView.findViewById(R.id.sender_image);
        }
    }
}

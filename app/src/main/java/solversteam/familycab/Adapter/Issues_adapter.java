package solversteam.familycab.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import solversteam.familycab.Activities.IssuesDetailsActivity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Issues;
import solversteam.familycab.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by ahmed ezz on 10/17/2017.
 */

public class Issues_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Issues> issues_array;
    private Connection connection;
    private SharedPreferences sharedPreferences;
    public Issues_adapter(Context  context, ArrayList<Issues> issues_array)
    {
        this.context=context;
        this.issues_array=issues_array;
        sharedPreferences =context.getSharedPreferences("lang_and_count",MODE_PRIVATE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.issue_item, parent, false);
        viewHolder = new Issues_adapter.IssueHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                Issues_adapter.IssueHolder vh1 = (Issues_adapter.IssueHolder) holder;
                setIssuesView(vh1, position);
                break;
            case 1:



        }



    }

    private void setIssuesView(IssueHolder holder, final int position) {
        holder.date.setText(issues_array.get(position).getDate());
        holder.header.setText(issues_array.get(position).getHeader());
        holder.messege.setText(issues_array.get(position).getMessege());
        if(!issues_array.get(position).getRead())
        {
            holder.date.setTypeface((Typeface.DEFAULT_BOLD));
            holder.header.setTypeface((Typeface.DEFAULT_BOLD));
            holder.messege.setTypeface((Typeface.DEFAULT_BOLD));
        }
        holder.issuesContainer_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!issues_array.get(position).getRead())
                {
                    updateMessageStatus(issues_array.get(position).getId());
                }
                Intent intent=new Intent(context, IssuesDetailsActivity.class);
                intent.putExtra("issuesID",issues_array.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    private void updateMessageStatus(String messegeID) {
        connection=new Connection((Activity) context,"/Message/UpdateMessageReceivedStatus/"+messegeID+"/"+sharedPreferences.getString("langID","1"),"Get");
        connection.reset();
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String jsonResponse) throws JSONException {
                Log.d("IssuesAdapter_updateMessageStatusResponse",jsonResponse);
            }
        });
    }


    @Override
    public int getItemCount() {
        return issues_array.size();
    }

    private class IssueHolder extends RecyclerView.ViewHolder {
        public TextView date,header,messege;
        public LinearLayout issuesContainer_linear;
        public IssueHolder(View view) {
            super(view);
            date=(TextView) view.findViewById(R.id.issue_date);
            header=(TextView) view.findViewById(R.id.issue_hidder);
            messege=(TextView) view.findViewById(R.id.issue_message);
            issuesContainer_linear=(LinearLayout)view.findViewById(R.id.issues_container_linear);

        }
    }
}

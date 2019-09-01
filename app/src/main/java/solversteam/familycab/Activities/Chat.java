package solversteam.familycab.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import solversteam.familycab.Adapter.Chat_adapter;
import solversteam.familycab.Models.Chat_model;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.R;

/**
 * Created by Nashaat on 10/30/2017.
 */

public class Chat  extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Chat_adapter chat_adapter;
    private ArrayList<Chat_model> chat_list ;
    private FloatingActionButton sendmsg;
    private EditText write_msg_edit;
    private ImageView close_button;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_nav_img;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar_title=(TextView)toolbar.findViewById(R.id.tv_home_activity_title);
        toolbar_title.setText(R.string.chat);
        toolbar_nav_img=(ImageView)toolbar.findViewById(R.id.menu_toolbar);
        toolbar_nav_img.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TestModel.setChat_activity(this);

       // set_ringtone(R.raw.thecalling);

        chat_list=new ArrayList<>();
        chat_list=TestModel.getjsontoarray (this);
        try {
            chat_list.add(new Chat_model(getIntent().getExtras().getString("message_chat")
                    ,getIntent().getExtras().getString("msg_id")));
            Log.d("message_chat",getIntent().getExtras().getString("message_chat"));
            TestModel.setarraytojson(chat_list,Chat.this);
        }
        catch (Exception e){
            Log.d("Exception",e.getLocalizedMessage());
        }
        recyclerView=(RecyclerView)findViewById(R.id.chat_recycle_sender) ;
        sendmsg=(FloatingActionButton)findViewById(R.id.send_button);
        write_msg_edit=(EditText)findViewById(R.id.write_msg_edittext) ;
        close_button=(ImageView)findViewById(R.id.close_button) ;
        //write_msg_edit.setFocusable(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        linearLayoutManager=new LinearLayoutManager(this);




        chat_adapter=new Chat_adapter(chat_list);
        recyclerView.setLayoutManager(linearLayoutManager);
         recyclerView.setAdapter(chat_adapter);
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
        write_msg_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_msg_edit.setFocusableInTouchMode (true);

            }
        });

        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg=write_msg_edit.getText().toString();

                Log.d("message",msg);
                setmsg(msg,"1");
            }
        });
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void set_ringtone(int ring) {
        final MediaPlayer mp = MediaPlayer.create(this, ring);
        mp.start();
    }

    public void setmsg(String msg,String id) {
        if (!msg.isEmpty())
        {
            chat_list.add(new Chat_model(msg,id));
            chat_adapter.notifyItemInserted(chat_list.size()-1);
            chat_adapter.notifyDataSetChanged();
            linearLayoutManager.scrollToPosition(chat_list.size() - 1);
            write_msg_edit.setText(null);
            TestModel.setarraytojson(chat_list,Chat.this);
            set_ringtone(R.raw.solemn);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }
}

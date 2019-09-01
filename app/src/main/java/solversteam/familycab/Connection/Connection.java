package solversteam.familycab.Connection;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import solversteam.familycab.R;
import solversteam.familycab.Service.FirebaseIDService;


public class Connection {


    private  LayoutInflater inflater;
    private Handler mainHandler;
    private OkHttpClient client;
    private Request requesturl;
    private Activity _context;
    private String json, method, url;
    private FormEncodingBuilder formBody;
    private Result DelegateMethod;
    private ConnectionDetector testConnection;
    private static final String baseurl = "http://www.gulfmix.com:86";
    private Boolean check=true;
    private View layout;
    private int flag =1;
    boolean found=true;
    FirebaseIDService firebaseIDService;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public Connection(Activity context, String URL, String method) {
        sharedPreferences=context.getSharedPreferences("LanguageAndCountry", Context.MODE_PRIVATE);
          editor = sharedPreferences.edit();

        url = baseurl + URL;
        Log.i("wezza1", url);
        this._context = context;
        mainHandler = new Handler(_context.getMainLooper());
        client = new OkHttpClient();
         this.method = method;
        testConnection = new ConnectionDetector(_context);
          inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.flag=1;

    }


    public Connection(FirebaseIDService context, String URL, String method) {


        url = baseurl + URL;
        Log.i("wezza1", url);
        this.firebaseIDService = context;
        mainHandler = new Handler(_context.getMainLooper());
        client = new OkHttpClient();
        this.method = method;
        testConnection = new ConnectionDetector(_context);
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.flag=1;

    }















    public Connection(Activity context, String URL, String method,int flag) {
        sharedPreferences=context.getSharedPreferences("LanguageAndCountry", Context.MODE_PRIVATE);
         editor = sharedPreferences.edit();

        this.flag=flag;
        if (flag == 2)
        url = baseurl + URL;
        else
            url = URL;
        Log.i("url", url);
        this._context = context;
        mainHandler = new Handler(_context.getMainLooper());
        client = new OkHttpClient();
//        this.loading = (ProgressBar) _context.findViewById(progressBar);
        this.method = method;
        testConnection = new ConnectionDetector(_context);
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         

    }

    // ===================== Deleage Method ==============================================
    public interface Result {
        public void data(String str) throws JSONException;
    }

    public void Connect(Result dlg) {
        DelegateMethod = dlg;

        if (testConnection.isConnectingToInternet()) {
            if (method.equals("Post")) {
                ReadFromServer();
            } else if (method.equals("Get")) {
                ReadFromServer1();
            } else if (method.equals("Put"))
                ReadFromServer2();
        } else {
            if(sharedPreferences.getBoolean("found",true) ){
            found=false;
                editor.putBoolean("found",false);
                editor.commit();
            connectionToast();
               }
            repeat();

        }


    }
    public void Connect2(Result dlg) {
        DelegateMethod = dlg;

        if (testConnection.isConnectingToInternet()) {
            if (method.equals("Post")) {
                ReadFromServer();
            } else if (method.equals("Get")) {
                ReadFromSer1();
            } else if (method.equals("Put"))
                ReadFromServer2();
        } else {
            connectionToast();
            repeat();

        }


    }

    public void executeNow(String result) {
        try {
            DelegateMethod.data(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("fgfg",e+"");
        }
    }

    // ===================== End Deleage Method ==============================================
    public void reset() {
        formBody = new FormEncodingBuilder();
    }

    public void addParmmter(String key, String value) {
        formBody.add(key, value);
    }



    private void ReadFromServer() {


        try {
            _context.showDialog(flag);
        } catch (Exception ex) {
        }
        RequestBody body = formBody.build();
        try {
            requesturl = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();


            client.newCall(requesturl).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    try {
                        _context.dismissDialog(1);
                    } catch (Exception ex) {
                    }
                    json = request.toString();
                    Log.i("Failuremsg", json);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (testConnection.isConnectingToInternet()) {
                                check=true;
                               repeat();
                            } else {
                                check=true;
                                repeat();
//                                connectionToast();
                            }
                        }
                    });


                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        _context.dismissDialog(1);
                    } catch (Exception ex) {
                    }
                    int code = response.code();
                    json = response.body().string();
                    Log.i("ResponseMsg", json);
                    Log.i("ResponseCode", code + "");
                    //if(code==404)
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            executeNow(json);
                        }
                    });

                }

            });
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(_context,"The resource you are looking for is temporarily unavailable ",Toast.LENGTH_LONG).show();

        }


    }

    private void ReadFromServer1() {
        //Log.i("12555" , progressBar+"");
        //loading = (ProgressBar) _context.findViewById(progressBar);
        //loading.setVisibility(View.VISIBLE);
        try {
            _context.showDialog(flag);
        } catch (Exception e) {
        }

        try {
            requesturl = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            client.newCall(requesturl).enqueue(new Callback() {


                @Override
                public void onFailure(Request request, IOException e) {
                    try {
                        _context.dismissDialog(1);
                    } catch (Exception ex) {
                    }
                    json = request.toString();
                    Log.i("Failuremsg", json);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (testConnection.isConnectingToInternet()) {
                                check=true;
                                 repeat2();
                            } else {
                                check=true;
                                repeat();
//                                try{
//                                    layout.setVisibility(View.GONE);}catch (Exception e){}
//                                connectionToast();
                            }
                        }
                    });


                }

                @Override
                public void onResponse(Response response) throws IOException {
//                    loading.setVisibility(View.INVISIBLE);
                    try {
                       _context.dismissDialog(1);
                    } catch (Exception e) {
                    }
                    int code = response.code();
                    json = response.body().string();
                    Log.i("ResponseMsg", json);
                    Log.i("ResponseCode", code + "");
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            executeNow(json);
                        }
                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void ReadFromSer1() {
        //Log.i("12555" , progressBar+"");
        //loading = (ProgressBar) _context.findViewById(progressBar);
        //loading.setVisibility(View.VISIBLE);


        try {
            requesturl = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            client.newCall(requesturl).enqueue(new Callback() {


                @Override
                public void onFailure(Request request, IOException e) {

                    json = request.toString();
                    Log.i("Failuremsg", json);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (testConnection.isConnectingToInternet()) {
                                check=true;
                                repeat2();
                            } else {
                                check=true;
                                repeat();
//                                try{
//                                    layout.setVisibility(View.GONE);}catch (Exception e){}
//                                connectionToast();
                            }
                        }
                    });


                }

                @Override
                public void onResponse(Response response) throws IOException {
//                    loading.setVisibility(View.INVISIBLE);

                    int code = response.code();
                    json = response.body().string();
                    Log.i("ResponseMsg", json);
                    Log.i("ResponseCode", code + "");
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            executeNow(json);
                        }
                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void connectionToast() {

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.my_customtoast, null);
        LinearLayout linear = (LinearLayout) layout.findViewById(R.id.Forget_customlayout_LinearLayout);
        Toast toast = new Toast(_context);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    private void ReadFromServer2() {

        _context.showDialog(flag);
        RequestBody body = formBody.build();
        try {
            requesturl = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();

            client.newCall(requesturl).enqueue(new Callback() {


                @Override
                public void onFailure(Request request, IOException e) {
                    _context.dismissDialog(1);
                    json = request.toString();
                    Log.i("Failuremsg", json);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (testConnection.isConnectingToInternet()) {
                                check=true;
                                 repeat();
                            } else {
                                check=true;
                                repeat();
//                                try{
//                                layout.setVisibility(View.GONE);}catch (Exception e){}
//                                connectionToast();
                            }
                        }
                    });


                }

                @Override
                public void onResponse(Response response) throws IOException {
                    _context.dismissDialog(1);
                    int code = response.code();
                    json = response.body().string();
                    Log.i("ResponseMsg", json);
                    Log.i("ResponseCode", code + "");
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            executeNow(json);
                        }
                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public void repeat()
    {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(check) {
                    Log.d("checkconn","first");
                    repeat();
                    if (testConnection.isConnectingToInternet()) {
                        editor.putBoolean("found",true);
                        editor.commit();
                        try{
                            ((ViewGroup)layout.getParent()).removeView(layout);}catch (Exception e){}                        check=false;
                        Log.d("checkconn","second");

                        if (method.equals("Post")) {
                            ReadFromServer();
                        } else if (method.equals("Get")) {
                            ReadFromServer1();
                        } else if (method.equals("Put"))

                        {
                            ReadFromServer2();
                        }
                    }
                    else
                    if(sharedPreferences.getBoolean("found",true) ){
                        found=false;
                        editor.putBoolean("found",false);
                        editor.commit();
                        connectionToast();}

                }
            }
        },3000);

    }

    public void repeat2()
    {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(check) {
                    Log.d("checkconn","first");
                    repeat2();
                    if (testConnection.isConnectingToInternet()) {
//                        editor.putBoolean("found",true);
//                        editor.commit();
//                        layout.setVisibility(View.GONE);
                        check=false;
                        Log.d("checkconn","second");

                        if (method.equals("Post")) {
                            ReadFromServer();
                        } else if (method.equals("Get")) {
                            ReadFromSer1();
                        } else if (method.equals("Put"))

                        {
                            ReadFromServer2();
                        }
                    }
                }
            }
        },3000);

    }



}

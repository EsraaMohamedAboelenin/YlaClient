package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.TestModel;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;
import solversteam.familycab.Util.PrefsManger;

public class LoginActivity extends AppCompatActivity {

    private static final int ERROR_DEFAULT = 0;
    private static final int ERROR_SEND_LOGIN = 1;
    private static final int CHAR_NUMBER = 4;

    private TextView mLoginTitleTv;
    private TextView mForgetPWTitleTv;
    private TextView mRegisterTitleTv;
    private TextView mEmailInputTitle;
    private TextView mPasswordInputTitle;

    private AutoCompleteTextView mEmailInputTv;
    private EditText mPasswordInputTv;

    private LinearLayout mRootLayout;
    private LinearLayout mLoadingLayout;
    private ProgressBar mLoadingProgressBar;
    private ImageView show_pass;

    private Button mLoginBtn;

    private Typeface mBaseTextFont;

    private PrefsManger mPrefsManger;
    private SharedPreferences sharedPreferences;
    private String iso_code,language,country;


    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
            "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\." +
            "[0-9]{1,3}\\.[0-9]{1,3}\\])" +
            "|(([a-zA-Z\\-0-9]+\\.)" +
            "+[a-zA-Z]{2,}))$";
    private static Matcher matcher;
    private static Pattern pattern;
    private String phone_hardware_id;
    private ImageView mFacebookIv;
    private CallbackManager mCallbackManager;
    private boolean show_pass_txt=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");
        country=sharedPreferences.getString("country_id","1");


        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code));
        Context context = LocaleHelper.setLocale(this, iso_code);
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        setContentView(R.layout.activity_login);
        initializeViews();
        mFacebookIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFaceBookProfileData();
            }
        });
    }

    private void initializeViews() {

        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");
        mPrefsManger = new PrefsManger(this);


        mRootLayout = (LinearLayout) findViewById(R.id.root_layout);
        setupHideKeyboard(mRootLayout);

        mLoadingLayout = (LinearLayout) findViewById(R.id.layout_loading);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loading);

        mForgetPWTitleTv = (TextView) findViewById(R.id.login_forgetpw_Title);

        mForgetPWTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgetPasswordDialog();
            }
        });

        mRegisterTitleTv = (TextView) findViewById(R.id.login_register_Title);

        mRegisterTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });

        mEmailInputTv = (AutoCompleteTextView) findViewById(R.id.login_email_Tv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, TestModel.getEmails(this));
        //Getting the instance of AutoCompleteTextView
        mEmailInputTv.setThreshold(1);//will start working from first character
        mEmailInputTv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        mEmailInputTv.setTextColor(Color.BLACK);

        mPasswordInputTv = (EditText) findViewById(R.id.login_password_Tv);

       mPasswordInputTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   login();
                    return true;
                }
                return false;
            }
        });


        mLoginBtn = (Button) findViewById(R.id.login_btn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if information validate send request to server to make login
                login();
            }
        });
        mFacebookIv = (ImageView) findViewById(R.id.reg_byFacebook_btn);
        show_pass=(ImageView)findViewById(R.id.show_pass);
        show_pass.setVisibility(View.VISIBLE);


        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!show_pass_txt){
                    mPasswordInputTv.setInputType(InputType.TYPE_CLASS_TEXT);
                    show_pass_txt=true;

                }
                else if(show_pass_txt){
                    mPasswordInputTv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show_pass_txt=false;


                }
            }
        });

    }


    private void login() {
        if (TextUtils.isEmpty(mEmailInputTv.getText().toString()) ||!(validate_information(mEmailInputTv.getText().toString().trim(), "email"))) {
            mEmailInputTv.setError(this.getResources().
                    getString(R.string.login_emailorphone_valid));
            return;
        }
        if (TextUtils.isEmpty(mPasswordInputTv.getText().toString())) {
            mPasswordInputTv.setError(this.getResources().
                    getString(R.string.login_pw_valid));
            return;
        }
        //send user_data to server to able to login
        sendLoginRequest();
    }

     //to validate information
    private boolean validate_information(String name, String kind) {

       if (kind.equals("email")) {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(name);
        }
        return matcher.matches();
    }



    private void sendLoginRequest() {

        phone_hardware_id= FirebaseInstanceId.getInstance().getToken();

        Log.d("hardware123456", FirebaseInstanceId.getInstance().getToken());

        showLoading();
        Connection connection = new Connection(this, "/passenger/Login", "Post");
        connection.reset();
        connection.addParmmter("Email",mEmailInputTv.getText().toString().trim());
        connection.addParmmter("username",mEmailInputTv.getText().toString().trim());
        connection.addParmmter("password",mPasswordInputTv.getText().toString().trim());
        connection.addParmmter("DeviceID",phone_hardware_id);
        connection.addParmmter("OSTypeID","1");

        connection.addParmmter("lang",language+"");
        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                Log.d("checklogin", str);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                try {
                    hideLoading();
                    JSONObject jsonObject = new JSONObject(str);

                    String customer_id = jsonObject.getString("ReachUserID");
                    editor.putString("passengerID", customer_id);
                    editor.putString("name", jsonObject.getString("UserFullName"));
                    editor.putString("email", jsonObject.getString("Email"));
                    TestModel.setEmails(jsonObject.getString("Email").trim(),LoginActivity.this);
                    editor.putString("Password", jsonObject.getString("Password"));
                    editor.putString("UserTypeNumber", jsonObject.getString("UserTypeNumber"));
                    editor.putString("currancy_id", jsonObject.getString("CurrencyID"));
                    editor.putString("UserTypeID",jsonObject.getString("UserTypeID"));
                    editor.putString("CurrencyName",jsonObject.getString("CurrencyName"));
                    editor.putString("CurrencyRate",jsonObject.getString("CurrencyRate"));
                    editor.putString("langID",jsonObject.getString("LanguageID"));
                    editor.putString("country_id",jsonObject.getString("CountryID"));
                    editor.putString("DateTypeID", jsonObject.getString("DateTypeID"));
                    editor.commit();
                    TestModel.isPassengerInTrip(LoginActivity.this,null,"",customer_id);

                } catch (Exception e) {

                    Toast.makeText(LoginActivity.this, str, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        });

    }

    private void showForgetPasswordDialog() {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forget_pw);

        final LinearLayout loadingLayout = (LinearLayout) dialog.findViewById(R.id.layout_loading);
        final ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loading);

        final EditText email = (EditText) dialog.findViewById(R.id.forgetpw_email_Tv);


        Button mSend = (Button) dialog.findViewById(R.id.forgetpw_send_btn);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString())||!(validate_information(email.getText().toString(),"email"))) {
                    email.setError(LoginActivity.this.getResources().
                            getString(R.string.forget_pw_email_valid));
                    return;
                }
                 sendForgetPasswordRequest(email.getText().toString(), loadingLayout, loadingProgressBar, dialog);

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void sendForgetPasswordRequest(final String email, final LinearLayout loadingLayout, final ProgressBar loadingProgressBar, final Dialog dialog) {

        try {
            Connection connection = new Connection(this, "/User/ForgetPassword", "Post");
            connection.reset();
            connection.addParmmter("email", email);

            connection.Connect(new Connection.Result() {
                @Override
                public void data(String jsonResponse) throws JSONException {

                    Log.d("check_forget_password", jsonResponse);

                    dialog.dismiss();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();


        }

    }


    private void setupHideKeyboard(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    mEmailInputTv.clearFocus();
                    mPasswordInputTv.clearFocus();
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupHideKeyboard(innerView);
            }
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showLoading() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideLoading() {
        mLoadingLayout.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.GONE);
    }

    private void showErrorListDialog(List<String> errors) {
        String error = "";
        for (int i = 0; i < errors.size(); i++) {
            if (error.equals("")) {
                error = error + errors.get(i);
            } else {
                error = error + "\n" + errors.get(i);
            }
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(error);
        alertDialog.setCancelable(false);
        alertDialog.setNeutralButton(getString(R.string.connection_error_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showErrorDialog(final int type) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(getResources().getString(R.string.connection_error_msg));
        alertDialog.setCancelable(false);
        if (type == ERROR_DEFAULT) {
            alertDialog.setNeutralButton(getString(R.string.connection_error_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });
        } else {
            alertDialog.setNegativeButton(getString(R.string.connection_error_back),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                            arg0.dismiss();
                        }
                    });
            alertDialog.setPositiveButton(getString(R.string.connection_error_tryAgain),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            login();
                            arg0.dismiss();
                        }
                    });
        }
        alertDialog.show();
    }
    private void getFaceBookProfileData() {

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                mFacebookIv.setVisibility(View.GONE);

                                try {
                                   Log.d("object",object.toString());
                                    mEmailInputTv.setText(object.optString("email"));

                                    if (getResources().getString(R.string.facebook_male).equals(object.optString("gender"))) {

                                    } else if (getResources().getString(R.string.facebook_female).equals(object.optString("gender"))) {

                                    }

                                    mCallbackManager = null;
                                } catch (Exception e) {
                                    Toast.makeText(LoginActivity.this, getResources().
                                            getString(R.string.reg_failed_to_connect_facebook), Toast.LENGTH_LONG).show();
                                    Log.e("Exception", e.getMessage() + "");
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,birthday,gender,picture");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, getResources().
                                getString(R.string.reg_failed_to_connect_facebook), Toast.LENGTH_LONG).show();
                        Log.e("FacebookException", exception.getMessage() + "");
                    }
                });

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mCallbackManager != null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}

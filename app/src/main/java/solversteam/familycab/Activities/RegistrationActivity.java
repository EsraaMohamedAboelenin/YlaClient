package solversteam.familycab.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import solversteam.familycab.Connection.Connection;
import solversteam.familycab.Models.Country;
import solversteam.familycab.Nav.Activities.HomeActivity;
import solversteam.familycab.R;
import solversteam.familycab.Util.LocaleHelper;
import solversteam.familycab.Util.PrefsManger;
import solversteam.familycab.Util.Validation;


public class RegistrationActivity extends AppCompatActivity {

    private static final int DEVICE_ANDROID = 0;
    private static final int ERROR_GET_COUNTRY = 0;
    private static final int ERROR_GET_TERMS = 1;
    private static final int ERROR_SEND_REG = 2;

    private static final int CHAR_NUMBER = 4;
    private static final int RC_SIGN_IN = 9001;
    private int mRegUserId;

    private CallbackManager mCallbackManager;

    private TextView mRegWithTitleTv;
    private TextView mHaveAccTitleTv;
    private TextView phonecodetxt;

    private EditText mNameInputTv;
    private EditText mPasswordInputTv;
    private EditText mPasswordAgainInputTv;
    private EditText mEmailInputTv;
    private EditText mAgeInputTv;
    private EditText mPhoneInputTv;

    private LinearLayout mCountryLinearTv;
    private AutoCompleteTextView mCountryInputTv,mStateTv;
    private Spinner mCountryCodeTv;

    private LinearLayout mGenderLinearTv;
    private AutoCompleteTextView mGenderInputTv;

    private ImageView mFacebookIv,show_pass,flag_img;

    private Button mRegisterBtn;

    private Typeface mBaseTextFont;

    private ScrollView mScrollView;
    private LinearLayout mRootLayout;
    private LinearLayout mLoadingLayout,phone_container;
    private ProgressBar mLoadingProgressBar;

    private ArrayList<Country> mCountriesList,mStateList;
    private ArrayList<String> test,phonecode,arra,state_array;
    private Country mSelectedCountry;

    private List<Country> mGenderList;
    private String mSelectedGender;

    private PrefsManger mPrefsManger;
    private String iso_code,country_id,gender_id,state_id;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String language;
    private boolean show_pass_txt=false;
    private String user_id,user_email;

//    private BaseApi mServiceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sharedPreferences =getSharedPreferences("lang_and_count",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        iso_code=sharedPreferences.getString("iso_code","ar");
        language=sharedPreferences.getString("langID","1");
        country_id="1";
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(iso_code.trim()));
        Context context = LocaleHelper.setLocale(this, iso_code.trim());
        Resources resources = context.getResources();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        setContentView(R.layout.activity_registration);
        Log.d("login",iso_code);


        initializeViews();
        mHaveAccTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });

        mCountryInputTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCountryInputTv.showDropDown();


            }
        });
        mCountryInputTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("countrycode",mCountriesList.get(position).getIso_code()).commit();
                country_id=mCountriesList.get(position).getID();
                update_states(country_id);
                //editor.putString("country_id",country_id).commit();
                phone_container.setVisibility(View.VISIBLE);
                phonecodetxt.setText(mCountriesList.get(position).getPhonecountryCode());
                try {
                    Picasso.with(RegistrationActivity.this).
                            load(mCountriesList.get(position).getCountry_img())
                            .placeholder(R.drawable.egypt).resize(40,25).into(flag_img);
                }catch (Exception e){}

            }
        });

       mStateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mStateTv.showDropDown();


            }
        });


        mStateTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                state_id=mStateList.get(position).getID();

               // editor.putString("stateID",state_id).commit();



            }
        });






        mGenderInputTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGenderInputTv.showDropDown();

            }
        });
        mGenderInputTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gender_id=mGenderList.get(position).getID();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mFacebookIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFaceBookProfileData();
            }
        });
    }




    private void update_states(String country_id) {
       mStateList=new ArrayList<>();
        state_array=new ArrayList<>();
        Connection connection5 = new Connection(this, "/state/GetAllState/"+country_id+"/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Log.d("GetAllLanguage", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("States");
                    Log.d("GetAllLanguage2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);


                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        String latitude =jsonObject1.getString("Latitude");
                        String longitude =jsonObject1.getString("Longitude");
                       state_array.add(name);
                        mStateList.add(new Country(id,name,latitude,longitude));

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item,state_array );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   mStateTv.setAdapter(adapter);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_GetAllLanguage",e.toString());
                }
            }});
    }















    private void initializeViews() {
        mPrefsManger = new PrefsManger(this);
        mBaseTextFont = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "29LTBukra_Regular.ttf");

        phonecodetxt = (TextView) findViewById(R.id.post_code_text);
        flag_img =(ImageView)findViewById(R.id.flag);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mRootLayout = (LinearLayout) findViewById(R.id.root_layout);
        setupHideKeyboard(mRootLayout);

        mLoadingLayout = (LinearLayout) findViewById(R.id.layout_loading);
        phone_container = (LinearLayout) findViewById(R.id.phone_container);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loading);

        mRegWithTitleTv = (TextView) findViewById(R.id.reg_regWith_Title);
        mRegWithTitleTv.setTypeface(mBaseTextFont);
        //   mCountryCodeTv = (Spinner) findViewById(R.id.reg_countryCode_Tv);
////////////////////////////////////////////////////////////////////

        mHaveAccTitleTv = (TextView) findViewById(R.id.reg_haveAcc_Title);
        mHaveAccTitleTv.setTypeface(mBaseTextFont);


        mNameInputTv = (EditText) findViewById(R.id.reg_name_Tv);
        mNameInputTv.setTypeface(mBaseTextFont);

        mPasswordInputTv = (EditText) findViewById(R.id.reg_password_Tv);
        mPasswordInputTv.setTypeface(mBaseTextFont);
        show_pass=(ImageView)findViewById(R.id.show_pass) ;
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

        mPasswordAgainInputTv = (EditText) findViewById(R.id.reg_password_again_Tv);
        mPasswordAgainInputTv.setTypeface(mBaseTextFont);

        mEmailInputTv = (EditText) findViewById(R.id.reg_email_Tv);
        mEmailInputTv.setTypeface(mBaseTextFont);

        mAgeInputTv = (EditText) findViewById(R.id.reg_age_Tv);
        mAgeInputTv.setTypeface(mBaseTextFont);

        mPhoneInputTv = (EditText) findViewById(R.id.reg_phoneNumber_Tv);
        mPhoneInputTv.setTypeface(mBaseTextFont);

        mCountryInputTv = (AutoCompleteTextView) findViewById(R.id.reg_country_Tv); ////////////////////
        mCountryInputTv.setTypeface(mBaseTextFont);
        getCountriesList();
        mStateTv=(AutoCompleteTextView) findViewById(R.id.reg_state_Tv);
        mStateTv.setTypeface(mBaseTextFont);






        mGenderInputTv = (AutoCompleteTextView) findViewById(R.id.reg_gender_Tv);
        mGenderInputTv.setTypeface(mBaseTextFont);
        initializeGenderList();

        mRegisterBtn = (Button) findViewById(R.id.reg_btn);
        mRegisterBtn.setTypeface(mBaseTextFont);


        mFacebookIv = (ImageView) findViewById(R.id.reg_byFacebook_btn);

    }

    private void register() {

        if (TextUtils.isEmpty(mNameInputTv.getText().toString())) {
            mNameInputTv.setError(this.getResources().
                    getString(R.string.reg_name_valid_title));
            scrollToPosition((View) mNameInputTv.getParent());
            return;
        }
        if (TextUtils.isEmpty(mEmailInputTv.getText().toString())||!isValidEmail(mEmailInputTv.getText().toString())) {
            mEmailInputTv.setError(this.getResources().
                    getString(R.string.reg_email_wrong_valid_title));
            scrollToPosition((View) mEmailInputTv.getParent());
            return;
        }

        if (state_id==null) {
            mStateTv.setError(getString(R.string.choose_state));
            scrollToPosition((View) mStateTv.getParent());
            return;
        }else
            mStateTv.setError(null);


        if (country_id==null) {
            mCountryInputTv.setError(getString(R.string.reg_country_valid_title));
            scrollToPosition((View) mCountryInputTv.getParent());
            return;
        }else
            mCountryInputTv.setError(null);
        if (TextUtils.isEmpty(mPhoneInputTv.getText().toString())) {
            mPhoneInputTv.setError(this.getResources().
                    getString(R.string.reg_phone_valid_title));
            scrollToPosition((View) mPhoneInputTv.getParent());
            return;
        }
        if (gender_id==null) {
            mGenderInputTv.setError(getString(R.string.reg_gender_valid_title));
            scrollToPosition((View) mGenderInputTv.getParent());
            return;
        }
        else
            mGenderInputTv.setError(null);
        if (mAgeInputTv.getText().toString().isEmpty()) {
            mAgeInputTv.setError(getString(R.string.reg_age_valid_title));
            scrollToPosition((View) mAgeInputTv.getParent());
            return;
        }
        if (TextUtils.isEmpty(mPasswordInputTv.getText().toString())|| (mPasswordInputTv.getText().toString().length() < CHAR_NUMBER)) {
            mPasswordInputTv.setError(this.getResources().
                    getString(R.string.reg_pw_less_than4_valid_title));
            scrollToPosition((View) mPasswordInputTv.getParent());
            return;
        }


        if (TextUtils.isEmpty(mPasswordAgainInputTv.getText().toString())||
                !mPasswordInputTv.getText().toString().equals(mPasswordAgainInputTv.getText().toString())) {
            mPasswordAgainInputTv.setError(this.getResources().
                    getString(R.string.reg_pw_notMatch_repw_valid_title));
            scrollToPosition((View) mPasswordAgainInputTv.getParent());
            return;
        }
        if (TextUtils.isEmpty(mPhoneInputTv.getText().toString())) {
            mPhoneInputTv.setError(this.getResources().
                    getString(R.string.reg_phone_valid_title));

            return;
        }

        else if (!Validation.validate(mPhoneInputTv,"mobile",getResources().getString(R.string.mobile_error))) {

           return;
        }

        getTerms();
    }

    private void getTerms() {
        showLoading();
        Connection connection = new Connection(this, "/Passenger/SignUP", "Post");
        connection.reset();

        connection.addParmmter("FirstName".trim(), mNameInputTv.getText().toString().trim());
        connection.addParmmter("LastName".trim(), "");
        connection.addParmmter("Email".trim(),mEmailInputTv.getText().toString().trim());
        connection.addParmmter("username".trim(),mEmailInputTv.getText().toString().trim());
        connection.addParmmter("EmailConfirmed".trim(), mEmailInputTv.getText().toString().trim());
        connection.addParmmter("Mobile".trim(), mPhoneInputTv.getText().toString().trim());
        connection.addParmmter("PasswordHash".trim(), mPasswordInputTv.getText().toString().trim());
        connection.addParmmter("Age".trim(), mAgeInputTv.getText().toString().trim());
        connection.addParmmter("Gender".trim(), gender_id.toString().trim());
        connection.addParmmter("CountryID".trim(), country_id + "".trim());
        connection.addParmmter("StateID".trim(), state_id+ "".trim());
        connection.addParmmter("LanguageID".trim(), language + "".trim());


        connection.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {


                Log.d("checkregister111",str+"\n");
                hideLoading();
                try {
                    JSONObject jsonObject = new JSONObject(str);

                    String customer_id = jsonObject.getString("UserID");
                    editor.putString("UserID", customer_id);
                    editor.putString("passengerID",jsonObject.getString("ReachUserID") );
                    editor.putString("name", jsonObject.getString("UserFullName"));
                    editor.putString("email", jsonObject.getString("Email"));
                    editor.putString("password", jsonObject.getString("Password"));
                    editor.putString("UserTypeNumber", jsonObject.getString("UserTypeNumber"));
                    editor.putString("UserTypeID", jsonObject.getString("UserTypeID"));
                    editor.putString("CurrencyID", jsonObject.getString("CurrencyID"));
                    editor.putString("CurrencyRate", jsonObject.getString("CurrencyRate"));
                    editor.putString("CurrencyName", jsonObject.getString("CurrencyName"));
                    editor.putString("langID",jsonObject.getString("LanguageID"));
                    editor.putString("country_id",jsonObject.getString("CountryID"));
                    editor.putString("DateTypeID", jsonObject.getString("DateTypeID"));
                    editor.putString("mobile",mPhoneInputTv.getText().toString());
                    editor.putString("stateID",jsonObject.getString("StateID"));


                    editor.commit();
                    Intent in = new Intent(RegistrationActivity.this, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(in);
                    finish();
                }catch (Exception e){

                    Toast.makeText(RegistrationActivity.this, str, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showTermsDialog () {
        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_terms);

        final LinearLayout loadingLayout = (LinearLayout) dialog.findViewById(R.id.layout_loading);
        final ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loading);

        WebView webView = (WebView) dialog.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://google.com");

        final AppCompatCheckBox mAgree = (AppCompatCheckBox) dialog.findViewById(R.id.reg_iAgree_checkbox);
        mAgree.setTypeface(mBaseTextFont);

        Button mRegister = (Button) dialog.findViewById(R.id.checked_reg_btn);
        mRegister.setTypeface(mBaseTextFont);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAgree.isChecked()) {
                    Toast.makeText(RegistrationActivity.this, getResources().
                            getString(R.string.reg_iAgree_checked_valid), Toast.LENGTH_LONG).show();
                } else {
                    sendRegisterRequest(loadingLayout, loadingProgressBar, dialog);
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void sendRegisterRequest(final LinearLayout loadinglayout, final ProgressBar loadingProgressBar, final Dialog dialog) {

    }

    private void showActivationCodeDialog() {

        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_code);
        dialog.setCancelable(false);

        final LinearLayout loadingLayout = (LinearLayout) dialog.findViewById(R.id.layout_loading);
        final ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loading);

        final EditText mCodeInput = (EditText) dialog.findViewById(R.id.reg_code_Tv);
        mCodeInput.setTypeface(mBaseTextFont);

        TextView mResendCode = (TextView) dialog.findViewById(R.id.reg_reSendCode_Title);
        mResendCode.setTypeface(mBaseTextFont);
        mResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        Button mSend = (Button) dialog.findViewById(R.id.reg_sendCode_btn);
        mSend.setTypeface(mBaseTextFont);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mCodeInput.getText().toString())) {
                    mCodeInput.setError(RegistrationActivity.this.getResources().
                            getString(R.string.reg_code_title));
                    return;
                }

                // sendActivationCode(Integer.parseInt(mCodeInput.getText().toString()), loadingLayout, loadingProgressBar, dialog);
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void getCountriesList() {
        setupEventsList();
    }

    private void setupEventsList() {
        mCountriesList = new ArrayList<>();
        test = new ArrayList<>();
        phonecode = new ArrayList<>();


        Connection connection5 = new Connection(this, "/country/GetAllWorkingCountry/"+language, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Log.d("GetAllWorkingCountry", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("Countries");
                    Log.d("GetAllWorkingCountry2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        String iso_code = jsonObject1.getString("ISOCode");
                        String phone_code = jsonObject1.getString("TelephoneCode");
                        String img = jsonObject1.getString("FlagURL");
                        String CurrencyID = jsonObject1.getString("CurrencyID");
                        Country country_model = new Country(id,name,phone_code,iso_code,img,CurrencyID);
                        mCountriesList.add(country_model);
                        test.add(name);
                        phonecode.add(phone_code);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item,test );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mCountryInputTv.setAdapter(adapter);


                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_country",e.toString());
                }
            }});
    }



    private void getFaceBookProfileData() {
        LoginManager.getInstance().logOut();
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                mFacebookIv.setVisibility(View.GONE);
                                mRegWithTitleTv.setVisibility(View.GONE);
                                try {
                                    mNameInputTv.setText(object.optString("name"));

                                    try {
                                        user_id = object.getString("id");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        user_email = object.getString("email");
                                        Log.d("ehab_123", "email is : "+user_email);
                                    } catch (Exception E) {
                                        Log.d("ehab_123", "email is : "+user_id);
                                        user_email = user_id+"@facebook.com";
                                    }

                                    mEmailInputTv.setText(user_email);

                                 try{   if (getResources().getString(R.string.facebook_male).equals(object.getString("gender"))) {
                                        mSelectedGender = RegistrationActivity.this.getString(R.string.reg_gender_0);
                                        mGenderInputTv.setText(mSelectedGender);
                                    } else if (getResources().getString(R.string.facebook_female).equals(object.getString("gender"))) {

                                        mSelectedGender = RegistrationActivity.this.getString(R.string.reg_gender_1);
                                        mGenderInputTv.setText(mSelectedGender);
                                    }}catch (Exception e){e.printStackTrace();}
                                    scrollToPosition((View) mNameInputTv.getParent());

                                    mCallbackManager = null;
                                } catch (Exception e) {
                                    Toast.makeText(RegistrationActivity.this, getResources().
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

                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        Log.e("FacebookException", exception.getMessage() + "");
                    }
                });



        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void initializeGenderList() {
        mGenderList = new ArrayList<>();
        arra = new ArrayList<>();
        Connection connection5 = new Connection(this, "/gender/GetAllGender/"+language+"/"+country_id, "Get");
        connection5.reset();
        connection5.Connect(new Connection.Result() {
            @Override
            public void data(String str) throws JSONException {
                try {
                    Log.d("GetAllGender", str + "");
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = jsonObject.getJSONArray("Genders");
                    Log.d("GetAllGender2", jsonArray.length() + "");
                    for (int position = 0; position < jsonArray.length(); position++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                        String name = jsonObject1.getString("NAME");
                        String id = jsonObject1.getString("ID");
                        Country country_model = new Country(id,name);
                        mGenderList.add(country_model);
                        arra.add(name);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this
                            , android.R.layout.simple_spinner_dropdown_item,arra );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mGenderInputTv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView




                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("error_gender",e.toString());
                }
            }});
    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void scrollToPosition(final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, view.getTop());
            }
        });
    }

    private void setupHideKeyboard(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    mNameInputTv.clearFocus();
                    mPasswordInputTv.clearFocus();
                    mEmailInputTv.clearFocus();
                    mPhoneInputTv.clearFocus();
                    mAgeInputTv.clearFocus();
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
                        if (type == ERROR_GET_COUNTRY)
                            getCountriesList();
                        else if (type == ERROR_GET_TERMS)
                            getTerms();
                        else if (type == ERROR_SEND_REG)
                            register();
                        arg0.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mCallbackManager != null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
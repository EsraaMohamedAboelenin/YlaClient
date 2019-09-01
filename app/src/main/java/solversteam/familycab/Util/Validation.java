package solversteam.familycab.Util;

import android.content.Context;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by ahmed ezz on 21/04/2017.
 */

public class Validation {
    private static final String Password_PATTERN ="^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~ \\\\u0600-\\\\u06FF][0-9a-zA-Z0-9.!#$%&'*+/=?^_`{|}~ \\\\u0600-\\\\u06FF/s ,_-]*$";
    private static final String Name_PATTERN ="^[a-zA-Z\\u0600-\\u06FF][a-zA-Z\\u0600-\\u06FF/s ,_-]*$";
    private static final String EMAIL_PATTERN ="^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
            "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\." +
            "[0-9]{1,3}\\.[0-9]{1,3}\\])" +
            "|(([a-zA-Z\\-0-9]+\\.)" +
            "+[a-zA-Z]{2,}))$";
    private static final String MOBILE_PATTERN="^\\+[1-9]{1}[0-9]{3,14}$";
    private static Matcher matcher;
    private static Pattern pattern;
    private static EditText password_edittetx;
    private static Context context;

    public static boolean validate_information(String name,String kind) {
        if(kind.equals("name"))
        {pattern = Pattern.compile(Name_PATTERN);
            matcher = pattern.matcher(name);}
        else if(kind.equals("password"))
        {pattern = Pattern.compile(Password_PATTERN);
            matcher = pattern.matcher(name);
        }

        else if(kind.equals("email"))
        {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(name);
        }
        else if(kind.equals("mobile"))
        {
            pattern = Pattern.compile(MOBILE_PATTERN);
            matcher = pattern.matcher(name);
        }

        return matcher.matches();
    }
    public static boolean validate(EditText user_input_text, String type, String message){
        boolean valid = true;
         switch (type) {
            case "name":
              if(user_input_text.getText().toString().length()>=2&&validate_information(user_input_text.getText().toString().trim(),"name"))
                {
                    user_input_text.setError(null);
                }
                else {
                    set_error(user_input_text,message);
                    valid=false;
                }
                break;

             case "email":
                if(user_input_text.getText().toString().length()>6&&validate_information(user_input_text.getText().toString().trim(),"email"))
                {
                    user_input_text.setError(null);

                }
                else {
                    set_error(user_input_text,message);
                    valid=false;
                }

                 break;
             case "password":
                 password_edittetx=user_input_text;
                 if(user_input_text.getText().toString().length()>5&&validate_information(user_input_text.getText().toString().trim(),"password"))
                {
                    user_input_text.setError(null);


                }
                else {
                   set_error(user_input_text,message);
                    valid=false;
                }
                 break;
             case "confirm_password":
                 if(user_input_text.getText().toString().length()>5&&user_input_text.getText().toString().trim().equals(password_edittetx.getText().toString().trim()))
                {
                    user_input_text.setError(null);

                }
                else {
                    set_error(user_input_text,message);
                    valid=false;
                }
                 break;
             case "mobile":
                 if(user_input_text.getText().toString().trim().length()>6)
                {
                    user_input_text.setError(null);

                }
                else {
                    set_error(user_input_text,message);
                    valid=false;
                }
                 break;
             case "city":
//                 if(user_input_text.getText().toString().equals(context.getResources().getString(R.string.city)))
//                {
//                    set_error(user_input_text,message);
//                    valid=false;
//
//                }
//                else {
//                    user_input_text.setError(null);
//
//                }
                 break;
             case "postalcode":
                 if(user_input_text.getText().toString().trim().length()==5)
                 {

                     user_input_text.setError(null);


                 }
                 else {
                     set_error(user_input_text,message);
                     valid=false;
                 }
                 break;
             case "optional":
                 user_input_text.setError(null);
                 break;
             default:
                 if(user_input_text.getText().toString().trim().equals(type)||user_input_text.getText().toString().trim().isEmpty())
                 {
                     set_error(user_input_text,message);
                        valid=false;

                 }
                 else {

                     user_input_text.setError(null);
                 }
                 break;
         }
         return valid;
    }

    private static void set_error(EditText user_input_text,String message)
    {
        user_input_text.setError(message);
    }








}

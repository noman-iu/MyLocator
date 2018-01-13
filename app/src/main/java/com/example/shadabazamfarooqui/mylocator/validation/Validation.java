package com.example.shadabazamfarooqui.mylocator.validation;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Shadab Azam Farooqui on 17-Dec-17.
 */

public class Validation {

    Context context;

    public Validation(Context context){
        this.context=context;
    }

    public boolean isValidName(EditText name, TextInputLayout inputLayoutName) {
        if (name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Name cannot be left blank");
            requestFocus(name,(Activity)context);
            return false;
        } else if (name.getText().toString().trim().length() < 5) {
            inputLayoutName.setError("Name should not be less than 5 character");
            requestFocus(name,(Activity)context);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isValidMobile(EditText mobile, TextInputLayout inputLayoutMobile) {
        if (mobile.getText().toString().trim().isEmpty()) {
            inputLayoutMobile.setError("Mobile cannot be left blank");
            requestFocus(mobile,(Activity)context);
            return false;
        } else if (mobile.getText().toString().length() != 10) {
            inputLayoutMobile.setError("Mobile should be of 10 digits");
            requestFocus(mobile,(Activity)context);
            return false;
        } else {
            inputLayoutMobile.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isValidEmail(EditText email ,TextInputLayout inputLayoutEmail) {
        String emailAdd = email.getText().toString().trim();
        if (emailAdd.isEmpty()){
            return true;
        }
        if (emailAdd.isEmpty() || !isValidEmail(emailAdd)) {
            inputLayoutEmail.setError("Invalid Email");
            requestFocus(email,(Activity)context);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isValidPassword(EditText password, TextInputLayout inputLayoutPassword) {
        if (password.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Password cannot be left blank");
            requestFocus(password,(Activity)context);
            return false;
        } else if (password.getText().toString().trim().length() < 5) {
            inputLayoutPassword.setError("Password should not be less than 5 character");
            requestFocus(password,(Activity)context);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isValidConfirmPassword(EditText confirmPassword,TextInputLayout inputLayoutConfirmPassword,EditText password) {
        if (!confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())) {
            inputLayoutConfirmPassword.setError("Passwords does not match");
            requestFocus(confirmPassword,(Activity)context);
            return false;
        } else {
            inputLayoutConfirmPassword.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void requestFocus(View view, Activity context) {
        if (view.requestFocus()) {
            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}

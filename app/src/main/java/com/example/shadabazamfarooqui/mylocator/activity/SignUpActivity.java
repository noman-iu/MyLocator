package com.example.shadabazamfarooqui.mylocator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.validation.Validation;
import com.example.shadabazamfarooqui.mylocator.bean.ReferenceWrapper;
import com.example.shadabazamfarooqui.mylocator.bean.UserBean;
import com.example.shadabazamfarooqui.mylocator.utils.ParameterConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.mobile)
    EditText mobile;
    @Bind(R.id.confirm_password)
    EditText confirmPassword;
    @Bind(R.id.submit)
    LinearLayout submit;
    @Bind(R.id.inputLayoutName)
    TextInputLayout inputLayoutName;
    @Bind(R.id.inputLayoutEmail)
    TextInputLayout inputLayoutEmail;
    @Bind(R.id.inputLayoutMobile)
    TextInputLayout inputLayoutMobile;
    @Bind(R.id.inputLayoutPassword)
    TextInputLayout inputLayoutPassword;
    @Bind(R.id.inputLayoutConfirmPassword)
    TextInputLayout inputLayoutConfirmPassword;
    ProgressDialog progressDialog;
    ReferenceWrapper referenceWrapper;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        initActionbar();
        referenceWrapper = ReferenceWrapper.getRefrenceWrapper(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(ParameterConstants.USERS);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldValidation();
            }
        });

    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_tittle_text_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009DE0")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(viewActionBar, params);
        TextView actionbarTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        actionbarTitle.setText("SIGN UP PAGE");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void fieldValidation() {

        Validation validation=new Validation(this);
        if (!validation.isValidName(name,inputLayoutName)) {
            return;
        }
        if (!validation.isValidEmail(email,inputLayoutEmail)) {
            return;
        }
        if (!validation.isValidMobile(mobile,inputLayoutMobile)) {
            return;
        }
        if (!validation.isValidPassword(password,inputLayoutPassword)) {
            return;
        }
        if (!validation.isValidConfirmPassword(confirmPassword,inputLayoutConfirmPassword,password)) {
            return;
        }
        retrieveKey(mobile.getText().toString());

    }

    private void retrieveKey(String mobile) {
        progressDialog.show();

        databaseReference.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserBean userBean = dataSnapshot.getValue(UserBean.class);
                if (userBean == null) {
                    register();
                } else {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, "Already Registered", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });


    }

    private void register() {

        final UserBean userBean = new UserBean();
        userBean.setName(name.getText().toString());
        userBean.setEmail(email.getText().toString());
        userBean.setMobile(mobile.getText().toString());
        userBean.setPassword(password.getText().toString());
        progressDialog.show();


        databaseReference.child(mobile.getText().toString()).setValue(userBean, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    progressDialog.dismiss();
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    intent.putExtra("mobile",mobile.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }


}

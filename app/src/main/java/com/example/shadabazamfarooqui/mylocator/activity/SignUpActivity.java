package com.example.shadabazamfarooqui.mylocator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.local_db.DatabaseHandler;
import com.example.shadabazamfarooqui.mylocator.utils.Preferences;
import com.example.shadabazamfarooqui.mylocator.validation.Validation;
import com.example.shadabazamfarooqui.mylocator.bean.ReferenceWrapper;
import com.example.shadabazamfarooqui.mylocator.bean.UserBean;
import com.example.shadabazamfarooqui.mylocator.utils.ParameterConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.mobile)
    EditText mobile;
    @Bind(R.id.submit)
    LinearLayout submit;
    @Bind(R.id.inputLayoutName)
    TextInputLayout inputLayoutName;
    @Bind(R.id.inputLayoutEmail)
    TextInputLayout inputLayoutEmail;
    @Bind(R.id.inputLayoutMobile)
    TextInputLayout inputLayoutMobile;
    @Bind(R.id.enter_otp_edit_text)
    EditText otpEditText;
    @Bind(R.id.submit_otp_btn)
    Button submitOptBtn;

    @Bind(R.id.otp_layout)
    LinearLayout otpLayout;
    @Bind(R.id.register_layout)
    LinearLayout registerLayout;

    ProgressDialog progressDialog;
    ReferenceWrapper referenceWrapper;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    // [END declare_auth]

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    UserBean userBean;

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
        mAuth = FirebaseAuth.getInstance();
        submitOptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpEditText.getText().toString().length() > 5) {
                    progressDialog.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpEditText.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(SignUpActivity.this, "invalid OTP", Toast.LENGTH_SHORT).show();
                }
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

        Validation validation = new Validation(this);
        if (!validation.isValidName(name, inputLayoutName)) {
            return;
        }
        if (!validation.isValidEmail(email, inputLayoutEmail)) {
            return;
        }
        if (!validation.isValidMobile(mobile, inputLayoutMobile)) {
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
                    Snackbar.make(coordinatorLayout, "Sorry ! You have Already Registered, Please proceed ", Snackbar.LENGTH_SHORT).show();
                    Preferences.getInstance(getApplicationContext()).setLogin(true);
                    Preferences.getInstance(getApplicationContext()).setName(userBean.getName());
                    Preferences.getInstance(getApplicationContext()).setEmail(userBean.getEmail());
                    Preferences.getInstance(getApplicationContext()).setMobile(userBean.getMobile());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
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
        userBean = new UserBean();
        userBean.setName(name.getText().toString());
        userBean.setEmail(email.getText().toString());
        userBean.setMobile(mobile.getText().toString());
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + userBean.getMobile(),             // Phone number to verify
                120,                      // Timeout duration
                TimeUnit.SECONDS,        // Unit of timeout
                SignUpActivity.this,   // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(SignUpActivity.this, "verification done line No :198", Toast.LENGTH_LONG).show();
                        saveData();
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Verification code sent to mobile", Toast.LENGTH_LONG).show();
                        mVerificationId = verificationId;
                        mResendToken = token;
                        registerLayout.setVisibility(View.GONE);
                        otpLayout.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "verification fail", Toast.LENGTH_LONG).show();
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(SignUpActivity.this, "invalid mob no", Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(SignUpActivity.this, "quota over", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveData();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignUpActivity.this, "Verification failed code invalid", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void saveData() {
        databaseReference.child(mobile.getText().toString()).setValue(userBean, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Verification done !", Toast.LENGTH_SHORT).show();
                    DatabaseHandler databaseHandler = new DatabaseHandler(SignUpActivity.this);
                    if (!databaseHandler.isAlreadyLoggedIn()) {
                        databaseHandler.insertRecord(userBean);
                    }
                    Preferences.getInstance(getApplicationContext()).setLogin(true);
                    Preferences.getInstance(getApplicationContext()).setName(userBean.getName());
                    Preferences.getInstance(getApplicationContext()).setEmail(userBean.getEmail());
                    Preferences.getInstance(getApplicationContext()).setMobile(userBean.getMobile());
                    Snackbar.make(coordinatorLayout, "Your have registered successfully", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 5000);
                    finish();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }
}

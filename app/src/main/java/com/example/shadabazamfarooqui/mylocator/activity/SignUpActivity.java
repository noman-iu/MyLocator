package com.example.shadabazamfarooqui.mylocator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
//        userBean.setPassword(password.getText().toString());
        progressDialog.show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + userBean.getMobile(),             // Phone number to verify
                120,                      // Timeout duration
                TimeUnit.SECONDS,        // Unit of timeout
                SignUpActivity.this,   // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(SignUpActivity.this, "verification done line No :153", Toast.LENGTH_LONG).show();
                        Preferences.getInstance(getApplicationContext()).setLogin(true);
                        databaseReference.child(mobile.getText().toString()).setValue(userBean, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    Preferences.getInstance(getApplicationContext()).setLogin(true);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        //Log.d(TAG, "onCodeSent:" + verificationId);
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Verification code sent to mobile", Toast.LENGTH_LONG).show();
                        // Save verification ID and resending token so we can use them later
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
                            // Invalid request
                            // [START_EXCLUDE]
                            Toast.makeText(SignUpActivity.this, "invalid mob no", Toast.LENGTH_LONG).show();


                            // [END_EXCLUDE]
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            // [START_EXCLUDE]
                            Toast.makeText(SignUpActivity.this, "quota over", Toast.LENGTH_LONG).show();
                            // [END_EXCLUDE]
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
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(SignUpActivity.this, "Verification done line No :227", Toast.LENGTH_LONG).show();
                            FirebaseUser user = task.getResult().getUser();
                            databaseReference = FirebaseDatabase.getInstance().getReference(ParameterConstants.USERS);
                            Preferences.getInstance(getApplicationContext()).setLogin(true);
                            databaseReference.child(userBean.getMobile()).setValue(userBean, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        progressDialog.dismiss();
                                        referenceWrapper.setUserBean(userBean);
                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                        Preferences.getInstance(getApplicationContext()).setLogin(true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } else {
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SignUpActivity.this, "Verification failed code invalid", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}

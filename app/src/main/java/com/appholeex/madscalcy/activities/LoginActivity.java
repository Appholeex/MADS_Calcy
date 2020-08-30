package com.appholeex.madscalcy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.appholeex.madscalcy.R;
import com.appholeex.madscalcy.databinding.ActivityLoginBinding;
import com.appholeex.madscalcy.utils.PrefUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends BaseActivity {

    private String TAG  = this.getClass().getName();
    private ActivityLoginBinding binding;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthCredential phoneAuthCredential;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullScreen();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        prefManager.connectDB();
        isLogin = prefManager.getBoolean(PrefUtils.IS_LOGIN);
        prefManager.closeDB();

        if (isLogin) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

        binding.txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.btnLogin.getText().toString().toUpperCase().equals(getString(R.string.send_otp))) {
                    sendOTP();
                }else {
                    //verify otp
                    String code = binding.edtPin1.getText().toString()+
                            binding.edtPin2.getText().toString()+
                            binding.edtPin3.getText().toString()+
                            binding.edtPin4.getText().toString()+
                            binding.edtPin5.getText().toString()+
                            binding.edtPin6.getText().toString();

                        verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                phoneAuthCredential = credential;
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    binding.edtMobile.setError("Invalid Credentials.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;

                binding.lytOTP.setVisibility(View.VISIBLE);
                binding.btnLogin.setText(R.string.verify_otp);
                binding.edtPin1.requestFocus();
            }
        };


        binding.edtPin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()==1)
                {
                    binding.edtPin2.requestFocus();
                }
            }
        });

        binding.edtPin2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()==1)
                {
                    binding.edtPin3.requestFocus();
                }
                if (s.toString().length()==0)
                {
                    binding.edtPin1.requestFocus();
                }
            }
        });

        binding.edtPin3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()==1)
                {
                    binding.edtPin4.requestFocus();
                }
                if (s.toString().length()==0)
                {
                    binding.edtPin2.requestFocus();
                }
            }
        });
        binding.edtPin4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()==1)
                {
                    binding.edtPin5.requestFocus();
                }
                if (s.toString().length()==0)
                {
                    binding.edtPin3.requestFocus();
                }
            }
        });

        binding.edtPin5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()==1)
                {
                    binding.edtPin6.requestFocus();
                }
                if (s.toString().length()==0)
                {
                    binding.edtPin4.requestFocus();
                }
            }
        });

        binding.edtPin6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }

    private boolean validatePhoneNumber() {
        if (TextUtils.isEmpty(binding.edtMobile.getText().toString())  ||
                binding.edtMobile.getText().toString().length() !=10 ) {
            binding.edtMobile.setError(getString(R.string.enter_valid_phone));
            return false;
        }
        return true;
    }

    private void sendOTP()
    {
        if (!validatePhoneNumber()) {
            return;
        }

        startPhoneNumberVerification(binding.edtMobile.getText().toString());
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();

                            prefManager.connectDB();
                            prefManager.setBoolean(PrefUtils.IS_LOGIN,true);
                            prefManager.setString(PrefUtils.MOBILE,user.getPhoneNumber());
                            prefManager.closeDB();

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(findViewById(android.R.id.content), "Invalid Verification Code.",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


}

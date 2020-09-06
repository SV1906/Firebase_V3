package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {
    String verificationID;
    PhoneAuthProvider.ForceResendingToken token;
    EditText phoneNumber, codeEnter;
    FirebaseAuth fAuth;
    Button nextbut;
    ProgressBar progressBar;
    TextView state;
    CountryCodePicker codePicker;
    Boolean VerificationInProgress = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   rPhoneNo= findViewById(R.id.edit_text_Pno)  ;
        setContentView(R.layout.activity_otp);
        fAuth = FirebaseAuth.getInstance();
        codePicker = findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.phone);
        codeEnter = findViewById(R.id.codeEnter);
        nextbut = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);


        nextbut.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().toString().length() != 10){
                    phoneNumber.setError("Phone no is not valid");
                    return; }
                if (!VerificationInProgress) {
                    String phoneNum = "+" + codePicker.getSelectedCountryCode() + phoneNumber.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    state.setText("Sending OTP...");
                    state.setVisibility(View.VISIBLE);
                    requestOTP(phoneNum);
                }
                else{
                    String userOTP= codeEnter.getText().toString();
                    if(!userOTP.isEmpty() && userOTP.length()==6){
                        PhoneAuthCredential credential =PhoneAuthProvider.getCredential(verificationID,userOTP);
                        verifyAuth(credential);
                    }
                    else {
                        codeEnter.setError("Valid OTP is required");
                    }

                }
            }


        });
    }
    private void verifyAuth(PhoneAuthCredential credential) {
     fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful() ){
                 Toast.makeText(Otp.this, "Authentication is successful", Toast.LENGTH_SHORT ).show();
                 startActivity(new Intent (getApplicationContext(), Login.class));
             }
             else{
                 Toast.makeText(Otp.this, "Authentication failed", Toast.LENGTH_SHORT ).show();
             }
         }
     });
    }


    private void requestOTP(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);
                codeEnter.setVisibility(View.VISIBLE);
                state.setVisibility(View.GONE);
                verificationID = s;
                token = forceResendingToken;
                nextbut.setText("Verify");
                VerificationInProgress = true;

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Otp.this, "Can't create Account" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}

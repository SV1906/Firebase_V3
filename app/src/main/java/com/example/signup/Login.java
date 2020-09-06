package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button Login, ForgotPassword1;

    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.edit_text_Eid);
        password = findViewById(R.id.edit_text_Pwd);
         Login = findViewById(R.id.button_Login);
        fAuth = FirebaseAuth.getInstance();
        ForgotPassword1 = findViewById(R.id.ForgotPassword);


        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();


                if(TextUtils.isEmpty(Email)){
                    email.setError("Email Id is mandatory");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Password is mandatory");
                    return;
                }
                if(Password.length() < 6) {
                    password.setError("Password should be longer than 6 characters");
                    return;
                }
                fAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId = fAuth.getCurrentUser().getUid();
                            FirebaseUser user = fAuth.getCurrentUser();
                            if(!user.isEmailVerified()){
                                Toast.makeText(Login.this, "Email isn't verified, try again after verification of Email ", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(Login.this, "You have Logged in", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), EmailVerification.class));
                            }

                        }
                        else{
                            Toast.makeText(Login.this, "Incorrect Email or Password"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                );



            }

        });



        ForgotPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail= new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog =new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to receive reset link");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link sent to your Emai", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error!Reset Link is not sent" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                    }

                });

                passwordResetDialog.create().show();
            }
        }
        );


    }
}

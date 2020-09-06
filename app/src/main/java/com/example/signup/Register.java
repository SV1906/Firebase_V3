package com.example.signup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class Register extends AppCompatActivity {

    EditText rEmailId, rPassword;
    Button rNext;
    FirebaseAuth fAuth;
    Button OldAccount;
    private static final String TAG = "Register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rEmailId = findViewById(R.id.edit_text_Eid);
        rPassword = findViewById(R.id.edit_text_Pwd);
        rNext = findViewById(R.id.button_Next);
        fAuth = FirebaseAuth.getInstance();
        OldAccount = findViewById(R.id.oldAccount) ;

        OldAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){startActivity(new Intent (getApplicationContext(), Login.class)); }});
        rNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = rEmailId.getText().toString().trim();
                String password = rPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    rEmailId.setError("Email Id is mandatory");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    rPassword.setError("Password is mandatory");
                    return;
                }
                if(password.length() < 6) {
                    rPassword.setError("Password should be longer than 6 characters");
                    return;
                }
                startActivity(new Intent (getApplicationContext(), Otp.class));

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Message has been sent", Toast.LENGTH_LONG).show();
                                }
                            }
                            ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG , "On Failure: Message not sent"+e.getMessage() );
                                    Toast.makeText(Register.this, "Verification Message not sent", Toast.LENGTH_LONG).show();
                                }
                            });
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_LONG).show();
                            // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}

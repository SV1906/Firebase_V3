package com.example.signup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonBD = findViewById(R.id.button_register);
        buttonBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BD();
            }
        });
        Button buttonLI =findViewById(R.id.button_loginIn);
        buttonLI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LI();
            }
        });
    }
    private void BD() {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }
    private void LI(){
        Intent intent = new Intent(this, Login.class );
        startActivity(intent);
    }
}


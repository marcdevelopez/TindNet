package com.marcdevelopez.tindnet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginRegisterActivity extends AppCompatActivity {
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        btnRegister = findViewById(R.id.bt_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // evitar que vuelva a llamar a checkIntroShown para no entrar en bucle
                Bundle bundle = new Bundle();
                bundle.putBoolean(getString(R.string.intro_shown_checked), true);
                Intent intent = new Intent(LoginRegisterActivity.this, AuthActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }
}
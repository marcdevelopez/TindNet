package com.marcdevelopez.tindnet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity {

    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        btnContinuar = findViewById(R.id.bt_continue_intro);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // iniciar actividad LoginRegister
                startActivity(new Intent(IntroActivity.this,AuthActivity.class));
                // guardar en SharedPreferences que la actividad de introducción ya se ha mostrado
                SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file),MODE_PRIVATE);
                SharedPreferences.Editor editor= prefs.edit();
                editor.putBoolean(getString(R.string.intro_shown),true);
                editor.apply();
            }
        });
    }

}
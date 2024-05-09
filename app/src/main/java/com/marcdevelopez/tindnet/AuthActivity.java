package com.marcdevelopez.tindnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcdevelopez.tindnet.provider.ProviderType;
import com.marcdevelopez.tindnet.util.Constants;

public class AuthActivity extends AppCompatActivity {

    private static FirebaseAuth mAuth;
    private EditText mEmailRegisterEditText, mPasswordEditText;
    private Button mRegisterWithPassword;
    private RadioGroup mRadioGroup;
    private boolean soyCliente;
    // necesario provider para saber como se autenticó el usuario en la home (google, correo y contraseña, facebook...):
    private ProviderType provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mEmailRegisterEditText = findViewById(R.id.etEmailRegister);
        mPasswordEditText = findViewById(R.id.etPassword);
        mRegisterWithPassword = findViewById(R.id.buttonRegister);
        mRadioGroup = findViewById(R.id.radioGroupRegisterEmpClient);
        // necesario para saber si está logeado usuario y para crear usuario nuevo.
        mAuth = FirebaseAuth.getInstance();

        // autenticamos los datos
        mRegisterWithPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailRegisterEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                // comprobamos datos correctos
                if (!email.isEmpty() && !password.isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(email, password).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (soyCliente) {
                                            // para indicar que el proveedor es de correo
                                            provider = ProviderType.BASIC;
                                            showHomeClient(email);
                                        } else {
                                            showHomeCompany(email);
                                        }
                                    } else {
                                        // La autenticación falló
                                        showAlert();
                                    }
                                }
                            });
                }
            }
        });

        // guardamos la selección de usuario Cliente o Empresa
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCliente) {
                    soyCliente = true;
                } else soyCliente = false;
            }
        });

        // esta función se encarga de ver si existe sesión iniciada
        session();

    }

    private void session() {
        // comprobamos si tenemos iniciada sesión con el gestor de preferencias
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString(getString(R.string.prefs_email), null);
        String provider = prefs.getString(getString(R.string.prefs_provider), null);
        // tenemos iniciada sesión si:
        if (email != null && provider != null) {
            // en este caso ocultamos la activity de autenticación si tenemos inicada sesión
            ConstraintLayout layout = findViewById(R.id.authLayout);
            layout.setVisibility(View.INVISIBLE); // en onstart() le damos visibilidad si hacemos logout...
            // actualizamos el valor del provider para que se pase en el bundle al home...
            this.provider = ProviderType.valueOf(provider);
            // abrimos el home que corresponda
            if (soyCliente) {
                showHomeClient(email);
            } else {
                showHomeCompany(email);
            }
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción al hacer clic en el botón "Aceptar"
                dialog.dismiss(); // Cierra el diálogo
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showHomeClient(String email) {
        Intent homeIntent = new Intent(this, HomeClientActivity.class);
        homeIntent.putExtra(Constants.EMAIL_AUTH, email);
        homeIntent.putExtra(Constants.PROVIDER, provider);
        startActivity(homeIntent);

    }

    private void showHomeCompany(String email) {
        // TODO: como el de cliente...
    }

    @Override
    public void onStart() {
        super.onStart();
        // volvemos a dar visibilidad al layout si no tenemos sesión abierta, que será cuando se muestre esta actividad
        ConstraintLayout layout = findViewById(R.id.authLayout);
        layout.setVisibility(View.VISIBLE);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // TODO: identificar usuario y enviar a empres o cliente
            currentUser.reload();
        }
    }

}
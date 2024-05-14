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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcdevelopez.tindnet.provider.ProviderType;
import com.marcdevelopez.tindnet.util.Constants;

import android.widget.Toast;

import com.google.android.gms.auth.api.signin.*;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.marcdevelopez.tindnet.util.IntroHelper;

public class AuthActivity extends AppCompatActivity {

    // para cuenta de google:
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private static FirebaseAuth mAuth;

    private EditText mEmailRegisterEditText, mPasswordEditText;
    private Button buttonRegisterWithPassword;
    private Button buttonRegisterWithGoogle;
    private RadioGroup radioGroupClientCompany;
    private boolean soyCliente;
    // necesario provider para saber como se autenticó el usuario en la home (google, correo y contraseña, facebook...):
    private ProviderType provider;
    private String errorMessageAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mEmailRegisterEditText = findViewById(R.id.etEmailRegister);
        mPasswordEditText = findViewById(R.id.etPassword);
        buttonRegisterWithPassword = findViewById(R.id.buttonRegister);
        buttonRegisterWithGoogle = findViewById(R.id.imageButtonGoogle);
        radioGroupClientCompany = findViewById(R.id.radioGroupRegisterEmpClient);

        // necesario para saber si está logeado usuario y para crear usuario nuevo.
        mAuth = FirebaseAuth.getInstance();

        // Si viene de LoginRegisterActivity no se checkea si en la app se mostró la intro ya...
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean introShownChecked = bundle.getBoolean(getString(R.string.intro_shown_checked));
            if (!introShownChecked) {
                IntroHelper.checkIntroShown(this); // de esta forma se queda en AuthActivity y no entra en bucle

            }
        }

        // comprueba si ya se abrió la app por primera vez y si es así envía a LoginRegister

        // hace el login en general escuchando los botones de login y la elección de tipo de usuario
        setup();
        // esta función se encarga de ver si existe sesión iniciada
        session();
    }

    private void setup() {

        // ponemos por defecto usuario en cliente para evitar dudas
        RadioButton radioButtonCliente = findViewById(R.id.radioButtonCliente);
        radioButtonCliente.setChecked(true);
        soyCliente = true;
        // guardamos la selección de usuario Cliente o Empresa
        radioGroupClientCompany.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCliente) {
                    soyCliente = true;
                } else soyCliente = false;
            }
        });

        // autenticamos los datos para inicio de sesión con contraseña
        buttonRegisterWithPassword.setOnClickListener(new View.OnClickListener() {
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
                                        showAlert(getString(R.string.fallo_autenticacion_password));
                                    }
                                }
                            });
                } else {
                    showAlert(getString(R.string.fallo_campos_email_password_vacios));
                }
            }
        });

        // autenticamos los datos para inicio de sesión con Google
        buttonRegisterWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Configura el cliente de inicio de sesión con Google
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(AuthActivity.this, gso);
                mAuth = FirebaseAuth.getInstance();

                // Comprueba si el usuario ya ha iniciado sesión
                if (mAuth.getCurrentUser() != null) {
                    Toast.makeText(AuthActivity.this, "iniciada sesión con anterioridad", Toast.LENGTH_SHORT).show();
                    // El usuario ya ha iniciado sesión, puedes redirigir a la siguiente actividad
                    // o realizar cualquier acción que necesites aquí
                } else {
                    // El usuario no ha iniciado sesión, muestra el botón de inicio de sesión con Google
                    signInWithGoogle();
                }
            }
        });

    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Resultado retornado por el inicio de sesión con Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google inicio de sesión exitoso, autentica con Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google inicio de sesión fallido, muestra un mensaje al usuario
                Toast.makeText(this, "Inicio de sesión con Google fallido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión con Google exitoso
                            // Puedes obtener el usuario actual y redirigir a la siguiente actividad aquí
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // Inicio de sesión con Google fallido, muestra un mensaje al usuario
                            Toast.makeText(AuthActivity.this, "Autenticación con Firebase fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void session() {
        // comprobamos si tenemos iniciada sesión con el gestor de preferencias
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString(getString(R.string.prefs_email), null);
        String provider = prefs.getString(getString(R.string.prefs_provider), null);
        // tenemos iniciada sesión si:
        if (email != null && provider != null) {
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

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
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
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // TODO: identificar usuario y enviar a empres o cliente
            currentUser.reload();
        }
    }

}
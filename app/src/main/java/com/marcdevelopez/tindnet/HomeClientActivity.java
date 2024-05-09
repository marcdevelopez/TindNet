package com.marcdevelopez.tindnet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.marcdevelopez.tindnet.databinding.ActivityHomeClientBinding;
import com.marcdevelopez.tindnet.provider.ProviderType;
import com.marcdevelopez.tindnet.util.Constants;

public class HomeClientActivity extends AppCompatActivity {

    private String email;
    private ProviderType provider;
    private ActivityHomeClientBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // recupero email y provider de AuthActivity
        Bundle bundle = this.getIntent().getExtras();
        // validamos que no está vacio el bundle
        if (bundle != null) {
            email = bundle.getString(Constants.EMAIL_AUTH);
            provider = (ProviderType) bundle.get(Constants.PROVIDER);
        }

        // guardamos datos de email y provider en inicio de sesión para usar en caso de cierre de aplicación
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.prefs_email), email);
        editor.putString(getString(R.string.prefs_provider), provider.toString());
        editor.apply();

        /**TODO: en menú superior del home, en cerrar sesión, llamaremos al sharedprerferences
         * R.string.prefs_file y llamaremos a clear() para eliminar estos datos
          */


        binding = ActivityHomeClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Configura la ActionBar

        // Configura la navegación con el NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home_client);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}

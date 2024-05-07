package com.marcdevelopez.tindnet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.marcdevelopez.tindnet.databinding.ActivityHomeClientBinding;

public class HomeClientActivity extends AppCompatActivity {

    private ActivityHomeClientBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar =findViewById(R.id.toolbar);
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

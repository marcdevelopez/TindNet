package com.marcdevelopez.tindnet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.marcdevelopez.tindnet.databinding.ActivityHomeClientBinding;

public class HomeClientActivity extends AppCompatActivity {

    private ActivityHomeClientBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNavigation();

    }

    private void initNavigation() {
        // se referencia o instancia con
        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_container);
        navController = navHostFragment.getNavController();
        // pasamos el id de nuestro bottonnavigation del xml de la actividad
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
    }

}
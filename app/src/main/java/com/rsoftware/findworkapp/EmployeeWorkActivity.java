package com.rsoftware.findworkapp;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.rsoftware.findworkapp.databinding.ActivityEmployeeWorkBinding;

public class EmployeeWorkActivity extends AppCompatActivity {
    private ActivityEmployeeWorkBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmployeeWorkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_favourite, R.id.navigation_responses, R.id.navigation_messages, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_work);

        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    @Override
    public void onBackPressed() {

            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                this.finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), "Нажмите дважды для выхода",
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
    }


}
package com.rsoftware.findworkapp;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.rsoftware.findworkapp.databinding.ActivityEmployeeWorkBinding;
import com.rsoftware.findworkapp.databinding.ActivityEmployerWorkBinding;

public class EmployerWorkActivity extends AppCompatActivity {
    private ActivityEmployerWorkBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmployerWorkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view_employer);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_vacancy_employer, R.id.navigation_resume_database_employer, R.id.navigation_messages_employer, R.id.navigation_messages, R.id.navigation_profile_employer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_work_employer);

        NavigationUI.setupWithNavController(binding.navViewEmployer, navController);
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
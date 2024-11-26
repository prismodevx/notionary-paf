package com.example.notionary_v1;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.NotesFragment) {
                navController.navigate(R.id.NotesFragment, null);
                return true;
            }
            if(item.getItemId() == R.id.RemindersFragment) {
                navController.navigate(R.id.RemindersFragment, null);
                return true;
            }
            if(item.getItemId() == R.id.OptionsFragment) {
                navController.navigate(R.id.OptionsFragment, null);
                return true;
            }
            return true;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("Prismodevx", "se presiono el boton atras");
                finish();
            }
        });
    }

    // Método para ocultar el BottomNavigationView
    public void hideBottomNav() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    // Método para mostrar el BottomNavigationView
    public void showBottomNav() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
package com.example.notionary_v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.notionary_v1.fragments.data.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TokenManager tokenManager = new TokenManager(this);
        if (tokenManager.getToken() == null) {
            // Si no hay token, redirigir al LoginFragment
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);  // Asegúrate de que LoginActivity esté configurada correctamente
            startActivity(intent);
            finish(); // Cierra MainActivity para evitar que el usuario regrese a esta pantalla sin estar autenticado
            return; // Evita que se cargue el resto de la actividad
        }

        ImageButton btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

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
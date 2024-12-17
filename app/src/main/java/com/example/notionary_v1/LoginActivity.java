package com.example.notionary_v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notionary_v1.fragments.data.TokenManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TokenManager tokenManager = new TokenManager(LoginActivity.this);

        if (tokenManager.getToken() == null || tokenManager.getToken().isEmpty()) {
//            Log.e("API Error", "Token no disponible");
//            Toast.makeText(LoginActivity.this, "Token no disponible", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
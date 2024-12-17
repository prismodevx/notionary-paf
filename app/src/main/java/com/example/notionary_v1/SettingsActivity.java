package com.example.notionary_v1;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notionary_v1.fragments.components.CustomDialog;
import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.RetrofitInstance;
import com.example.notionary_v1.fragments.data.TokenManager;
import com.example.notionary_v1.fragments.data.Usuario;
import com.example.notionary_v1.interf.NotesApi;
import com.example.notionary_v1.interf.UsuarioApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ConstraintLayout btnToProfile;
    private ConstraintLayout btnOpenChangePassword;
    private ConstraintLayout btnOpenAbout;
    private Button btnToLogout;

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tokenManager = new TokenManager(this);

        btnToLogout = findViewById(R.id.btn_to_logout);
        btnToProfile = findViewById(R.id.btn_to_profile);
        btnOpenChangePassword = findViewById(R.id.btn_open_change_password);
        btnOpenAbout = findViewById(R.id.btn_open_about);

        btnToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnOpenChangePassword.setOnClickListener(v -> {
            openChangePasswordDialog(Integer.parseInt(tokenManager.getId()));
        });

        btnOpenAbout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        btnToLogout.setOnClickListener(v -> {
            CustomDialog dialog = new CustomDialog(this)
                    .setTitle("Desea cerrar sesion?")
                    .setIcon(R.drawable.ic_error)
                    .setCancelButton("Cancelar", null)
                    .setOkButton("Salir", view -> {
                        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("access_token");
                        editor.apply();
                        tokenManager.clearToken();

                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    });
            dialog.show();
        });
    }

    private void openChangePasswordDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText edtPassword1 = dialogView.findViewById(R.id.edt_password1);
        EditText edtPassword2 = dialogView.findViewById(R.id.edt_password2);
        Button btnChange = dialogView.findViewById(R.id.btn_change);

        btnChange.setOnClickListener(v -> {
            String password1 = edtPassword1.getText().toString().trim();
            String password2 = edtPassword2.getText().toString().trim();

            if (password1.isEmpty()) {
                edtPassword1.setError("La contraseña no puede estar vacía");
                edtPassword1.requestFocus();
                return;
            }

            if (password2.isEmpty()) {
                edtPassword2.setError("Confirma tu contraseña");
                edtPassword2.requestFocus();
                return;
            }

            if (!password1.equals(password2)) {
                edtPassword2.setError("Las contraseñas no coinciden");
                edtPassword2.requestFocus();
                return;
            }

            changePassword(String.valueOf(edtPassword2.getText()));

            dialog.dismiss();
        });

        dialog.show();
    }

    private void changePassword(String password) {
        String token = "JWT " + tokenManager.getToken();
        UsuarioApi apiService = RetrofitInstance.getRetrofitInstance().create(UsuarioApi.class);

        Usuario usuario = new Usuario("", password, "");

        Call<ApiResponse> call = apiService.changePassword(Integer.parseInt(tokenManager.getId()), usuario, token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Cambiaste tu contraseña con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
//                        Toast.makeText(AddNoteActivity.this, username, Toast.LENGTH_SHORT).show()
                        Log.e("API Error", "Error al guardar la nota: " + errorMessage);

                        Log.d("API Response", "Error response: " + response.body());

                        Toast.makeText(SettingsActivity.this, "Error al guardar la nota: " + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Error al obtener el mensaje de error: " + e.getMessage());
                        Toast.makeText(SettingsActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Error de conexión: " + t.getMessage());
                Toast.makeText(SettingsActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
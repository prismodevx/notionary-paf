package com.example.notionary_v1;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notionary_v1.fragments.components.LoadingFragment;
import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.ApiResponseUsuario;
import com.example.notionary_v1.fragments.data.RetrofitInstance;
import com.example.notionary_v1.fragments.data.TokenManager;
import com.example.notionary_v1.fragments.data.Usuario;
import com.example.notionary_v1.interf.UsuarioApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private TextView txtUsuario, txtNombres;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtUsuario = findViewById(R.id.txt_usuario);
        txtNombres = findViewById(R.id.txt_nombre_completo);

        obtenerDatosUsuario();
    }

    private void obtenerDatosUsuario() {
        LoadingFragment loadingDialog = new LoadingFragment();
        loadingDialog.show(getSupportFragmentManager(), "loading");
        TokenManager tokenManager = new TokenManager(ProfileActivity.this);
        String token = "JWT " + tokenManager.getToken();

        if (token == null || token.isEmpty()) {
            Log.e("API Error", "Token no disponible");
            Toast.makeText(ProfileActivity.this, "Token no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = Integer.parseInt(tokenManager.getId());

        UsuarioApi apiService = RetrofitInstance.getRetrofitInstance().create(UsuarioApi.class);
        Call<ApiResponseUsuario> call = apiService.getConfiguraciones(userId, token);

        call.enqueue(new Callback<ApiResponseUsuario>() {
            @Override
            public void onResponse(Call<ApiResponseUsuario> call, Response<ApiResponseUsuario> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body().getData();

                    txtUsuario.setText(usuario.getEmail());
                    txtNombres.setText(usuario.getNombresCompletos());
                }
                else {
                    Log.e("API Error", "La lista de notas está vacía.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponseUsuario> call, Throwable t) {
                Log.e("API Error", "Fallo en la solicitud: " + t.getMessage());
                Toast.makeText(ProfileActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
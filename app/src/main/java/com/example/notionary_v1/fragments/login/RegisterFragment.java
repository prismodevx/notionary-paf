package com.example.notionary_v1.fragments.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.notionary_v1.AddNoteActivity;
import com.example.notionary_v1.MainActivity;
import com.example.notionary_v1.R;
import com.example.notionary_v1.databinding.FragmentLoginBinding;
import com.example.notionary_v1.databinding.FragmentRegisterBinding;
import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.RetrofitInstance;
import com.example.notionary_v1.fragments.data.Usuario;
import com.example.notionary_v1.interf.NotesApi;
import com.example.notionary_v1.interf.UsuarioApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnRegister.setOnClickListener(v -> {
//            Intent intent = new Intent(requireContext(), MainActivity.class);
//            startActivity(intent);
//            requireActivity().finish();
        });

        binding.btnToLogin.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
        });

        binding.btnRegister.setOnClickListener(v -> {
            final String email = String.valueOf(binding.edtUsername.getText()).trim();
            final String fullName = String.valueOf(binding.edtFullname.getText()).trim();
            final String password = String.valueOf(binding.edtPassword.getText()).trim();

            boolean isValid = true;

            String validPattern = "^[a-zA-Z0-9_]+$";

            if (email.isEmpty()) {
                binding.edtUsername.setError("El campo es obligatorio");
                isValid = false;
            } else if (!email.matches(validPattern)) {
                binding.edtUsername.setError("El Usuario solo puede contener letras, números y _");
                isValid = false;
            } else {
                binding.edtUsername.setError(null);
            }

            if (fullName.isEmpty()) {
                binding.edtFullname.setError("El campo es obligatorio");
                isValid = false;
            } else if (!fullName.matches(validPattern)) {
                binding.edtFullname.setError("El Nombre completo solo puede contener letras");
                isValid = false;
            } else {
                binding.edtFullname.setError(null);
            }

            if (password.isEmpty()) {
                binding.edtPassword.setError("El campo es obligatorio");
                isValid = false;
            } else {
                binding.edtPassword.setError(null);
            }

            if (!isValid) return;

            saveUsuario(email, password, fullName);
        });
    }

    private void saveUsuario(String email, String fullName, String password) {
        UsuarioApi apiService = RetrofitInstance.getRetrofitInstance().create(UsuarioApi.class);
        Usuario usuario = new Usuario(email, fullName, password);
        Call<ApiResponse> call = apiService.createUser(usuario);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Usuario registrado, inicia sesion", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(RegisterFragment.this).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
                }
                else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("API", "Error al guardar la nota: " + errorMessage);
                        Toast.makeText(getContext(), "El usuario ya existe!, intenta con otro", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error desconocido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
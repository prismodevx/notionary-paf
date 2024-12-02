package com.example.notionary_v1.fragments.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.notionary_v1.MainActivity;
import com.example.notionary_v1.databinding.FragmentLoginBinding;
import com.example.notionary_v1.interf.MyApi;
import com.example.notionary_v1.model.AuthRequest;
import com.example.notionary_v1.model.AuthResponse;
import com.example.notionary_v1.model.LeerUsuarioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> {
//            Intent intent = new Intent(requireContext(), MainActivity.class);
//            startActivity(intent);
//            requireActivity().finish();

            final String username = String.valueOf(binding.edtUsername.getText());
            final String password = String.valueOf(binding.edtPassword.getText());
            getAuth(username, password);
        });

//        binding.btnToRegister.setOnClickListener(v -> {
//            NavHostFragment.findNavController(this).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
//        });
    }

    private void getAuth(String username, String password) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        Retrofit retrofit = new Retrofit. Builder()
                .baseUrl("https://alexismendoza.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory. create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        AuthRequest authRequest = new AuthRequest();

        Log.d("req", username);
        Log.d("req", password);

        authRequest.setUsername(username);
        authRequest.setPassword(password);
        Call<AuthResponse> call = myApi.autenticar(authRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);

                binding.edtUsername.setEnabled(false);
                binding.edtPassword.setEnabled(false);

                Log.d("req", response.toString());
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Credenciales Invalidas", Toast.LENGTH_SHORT).show();
                    binding.edtUsername.setEnabled(true);
                    binding.edtPassword.setEnabled(true);
                }
                else {
                    AuthResponse authResponse = response.body();
                    Toast.makeText(getActivity(), authResponse.getAccess_token(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });

    }
}
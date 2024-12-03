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
import androidx.navigation.fragment.NavHostFragment;

import com.example.notionary_v1.MainActivity;
import com.example.notionary_v1.databinding.FragmentLoginBinding;
import com.example.notionary_v1.fragments.components.ErrorFragment;
import com.example.notionary_v1.fragments.components.LoadingFragment;
import com.example.notionary_v1.fragments.data.ApiClient;
import com.example.notionary_v1.fragments.data.AuthInterceptor;
import com.example.notionary_v1.fragments.data.TokenManager;
import com.example.notionary_v1.interf.MyApi;
import com.example.notionary_v1.model.AuthRequest;
import com.example.notionary_v1.model.AuthResponse;
import com.example.notionary_v1.model.LeerUsuarioResponse;

import okhttp3.OkHttpClient;
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
            final String username = String.valueOf(binding.edtUsername.getText());
            final String password = String.valueOf(binding.edtPassword.getText());
            if (username.isEmpty() || password.isEmpty()) {
                showErrorDialog("Por favor, complete ambos campos.");
                return;
            }
            getAuth(username, password);
        });

        binding.btnToRegister.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
        });
    }

    private void getAuth(String username, String password) {
        LoadingFragment loadingDialog = new LoadingFragment();
        loadingDialog.show(getParentFragmentManager(), "loading");

//        TokenManager tokenManager = new TokenManager(requireContext());
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new AuthInterceptor(tokenManager))
//                .build();
//
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://alexismendoza.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory. create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        AuthRequest authRequest = new AuthRequest();

        authRequest.setUsername(username);
        authRequest.setPassword(password);

        Call<AuthResponse> call = myApi.autenticar(authRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                loadingDialog.dismiss();

                Log.d("req", response.toString());
                if (!response.isSuccessful()) {
                    showErrorDialog("Credenciales inválidas. Por favor, intente de nuevo.");
                    binding.edtUsername.setText("");
                    binding.edtPassword.setText("");
                }
                else {
                    AuthResponse authResponse = response.body();
                    String token = authResponse.getAccess_token();
                    TokenManager tokenManager = new TokenManager(requireContext());
                    tokenManager.saveToken(token);
                    Log.d("token", token);

                    Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();

                }

            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loadingDialog.dismiss();
                showErrorDialog("Error de conexión. Verifique su red e intente nuevamente.");
            }
        });

    }

    private void showErrorDialog(String errorMessage) {
        ErrorFragment errorDialog = new ErrorFragment(errorMessage);
        errorDialog.show(getParentFragmentManager(), "errorDialog");
    }
}
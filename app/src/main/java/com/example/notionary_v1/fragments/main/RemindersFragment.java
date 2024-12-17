package com.example.notionary_v1.fragments.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notionary_v1.AddReminderActivity;
import com.example.notionary_v1.databinding.FragmentRemindersBinding;
import com.example.notionary_v1.fragments.adapter.RemindersAdapter;
import com.example.notionary_v1.fragments.components.LoadingFragment;
import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.ApiResponseReminder;
import com.example.notionary_v1.fragments.data.Reminder;
import com.example.notionary_v1.fragments.data.RetrofitInstance;
import com.example.notionary_v1.fragments.data.TokenManager;
import com.example.notionary_v1.interf.RemindersApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RemindersFragment extends Fragment {
    private FragmentRemindersBinding binding;
    private RemindersAdapter adapter;
    private List<Reminder> recordatorios = new ArrayList<>();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RemindersAdapter(recordatorios);
        binding.recyclerView.setAdapter(adapter);

        binding.btnAddReminders.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddReminderActivity.class);
            startActivity(intent);
        });
    }

    private void obtenerRecordatorios() {
        LoadingFragment loadingDialog = new LoadingFragment();
        loadingDialog.show(getParentFragmentManager(), "loading");
        TokenManager tokenManager = new TokenManager(requireContext());
        String token = "JWT " + tokenManager.getToken();

        if (token == null || token.isEmpty()) {
            Log.e("API Error", "Token no disponible");
            Toast.makeText(getContext(), "Token no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = Integer.parseInt(tokenManager.getId());

        RemindersApi apiService = RetrofitInstance.getRetrofitInstance().create(RemindersApi.class);
        Call<ApiResponseReminder> call = apiService.getReminders(userId, token);

        call.enqueue(new Callback<ApiResponseReminder>() {
            @Override
            public void onResponse(Call<ApiResponseReminder> call, Response<ApiResponseReminder> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    List<Reminder> recordatorios = response.body().getData();
                    if (recordatorios != null) {
                        Log.d("Notas", "Número de notas: " + recordatorios.size());
                        adapter.updateReminders(recordatorios);
                    } else {
                        Log.e("API Error", "La lista de notas está vacía.");
                    }
                } else {
                    Log.e("API Error", "Error al obtener notas: " + response.message());
                    Toast.makeText(getContext(), "Error al cargar las notas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseReminder> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        obtenerRecordatorios();
    }
}
package com.example.notionary_v1.fragments.main;

import static androidx.core.util.TypedValueCompat.dpToPx;

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

import com.example.notionary_v1.AddNoteActivity;
import com.example.notionary_v1.MainActivity;
import com.example.notionary_v1.databinding.FragmentNotesBinding;
import com.example.notionary_v1.fragments.adapter.GridSpacingItemDecoration;
import com.example.notionary_v1.fragments.adapter.NotesAdapter;
import com.example.notionary_v1.fragments.components.LoadingFragment;
import com.example.notionary_v1.fragments.data.ApiClient;
import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.Note;
import com.example.notionary_v1.fragments.data.NoteManager;
import com.example.notionary_v1.fragments.data.RetrofitInstance;
import com.example.notionary_v1.fragments.data.TokenManager;
import com.example.notionary_v1.interf.NotesApi;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NotesFragment extends Fragment {
    private FragmentNotesBinding binding;
    private NotesAdapter adapter;
    private List<Note> notas = new ArrayList<>();;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        adapter = new NotesAdapter(notas);
//        binding.recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2); // 2 columnas
        binding.recyclerView.setLayoutManager(gridLayoutManager);

        int spacing = dpToPx(14);
        binding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));

        adapter = new NotesAdapter(notas);
        binding.recyclerView.setAdapter(adapter);

        binding.btnAddNotes.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddNoteActivity.class);
            startActivity(intent);
//            NavHostFragment.findNavController(this).navigate(NotesFragmentDirections.actionNotesFragmentToTestFragment());
        });

        obtenerNotas();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * requireContext().getResources().getDisplayMetrics().density);
    }

    private void obtenerNotas() {
        LoadingFragment loadingDialog = new LoadingFragment();
        loadingDialog.show(getParentFragmentManager(), "loading");
        TokenManager tokenManager = new TokenManager(requireContext());
        String token = "JWT " + tokenManager.getToken();

        if (token == null || token.isEmpty()) {
            return;
        }

        int userId = Integer.parseInt(tokenManager.getId());

        NotesApi apiService = RetrofitInstance.getRetrofitInstance().create(NotesApi.class);
        Call<ApiResponse> call = apiService.getNotes(userId, token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    List<Note> notas = response.body().getData();
                    if (notas != null) {
                        adapter.updateNotes(notas);
                    } else {
                        Log.e("API Error", "La lista de notas está vacía.");
                    }
                } else {
                    Log.e("API Error", "Error al obtener notas: " + response.message());
                    Toast.makeText(getContext(), "Error al cargar las notas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Fallo en la solicitud: " + t.getMessage());
                Toast.makeText(getContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        obtenerNotas();
    }
}
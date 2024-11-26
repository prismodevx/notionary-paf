package com.example.notionary_v1.fragments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notionary_v1.MainActivity;
import com.example.notionary_v1.R;
import com.example.notionary_v1.databinding.FragmentTestBinding;

public class TestFragment extends Fragment {

    private FragmentTestBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Ocultar el Bottom Navigation cuando este fragmento se muestre
        if (getActivity() != null) {
            // Asegúrate de que la actividad esté configurada
            ((MainActivity) getActivity()).hideBottomNav();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Mostrar el Bottom Navigation nuevamente cuando el fragmento se oculte
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showBottomNav();
        }
    }
}
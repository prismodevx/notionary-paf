package com.example.notionary_v1.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notionary_v1.AddNoteActivity;
import com.example.notionary_v1.MainActivity;
import com.example.notionary_v1.databinding.FragmentNotesBinding;
import com.example.notionary_v1.fragments.adapter.NotesAdapter;
import com.example.notionary_v1.fragments.data.NoteManager;

import androidx.recyclerview.widget.LinearLayoutManager;


public class NotesFragment extends Fragment {
    private FragmentNotesBinding binding;
    private NotesAdapter adapter;

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

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NotesAdapter(NoteManager.getNotes());
        binding.recyclerView.setAdapter(adapter);

        binding.btnAddNotes.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddNoteActivity.class);
            startActivity(intent);
//            NavHostFragment.findNavController(this).navigate(NotesFragmentDirections.actionNotesFragmentToTestFragment());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
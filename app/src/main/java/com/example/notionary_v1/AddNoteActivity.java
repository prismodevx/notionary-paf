package com.example.notionary_v1;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notionary_v1.fragments.data.Note;
import com.example.notionary_v1.fragments.data.NoteManager;

public class AddNoteActivity extends AppCompatActivity {
    private EditText edtTitle;
    private EditText edtBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtTitle = findViewById(R.id.edt_title);
        edtBody = findViewById(R.id.edt_body);

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = edtTitle.getText().toString();
        String description = edtBody.getText().toString();

        if (!title.isEmpty() && !description.isEmpty()) {
            NoteManager.addNote(new Note(title, description));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.notionary_v1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notionary_v1.fragments.data.Note;
import com.example.notionary_v1.fragments.data.NoteManager;

public class AddNoteActivity extends AppCompatActivity {
    private EditText edtTitle;
    private EditText edtBody;
    private int selectedColor;
    private long noteId = -1;
    private Toolbar toolbar;
    private ImageButton deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        deleteBtn = findViewById(R.id.action_delete);

        edtTitle = findViewById(R.id.edt_title);
        edtBody = findViewById(R.id.edt_body);

        Intent intent = getIntent();
        noteId = intent.getLongExtra("note_id", -1);
        String title = intent.getStringExtra("note_title");
        String description = intent.getStringExtra("note_description");

        if (noteId != -1) {
            edtTitle.setText(title);
            edtBody.setText(description);
            setTitle("Editar Nota");
        }

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> saveNote());

        Button btnPastel1 = findViewById(R.id.btn_pastel_1);
        Button btnPastel2 = findViewById(R.id.btn_pastel_2);
        Button btnPastel3 = findViewById(R.id.btn_pastel_3);


        btnPastel1.setOnClickListener(v -> {
            changeToolbarColor(R.color.color_pastel_1);
            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_1);
            Log.d("c", String.valueOf(selectedColor));
        });
        btnPastel2.setOnClickListener(v -> {
            changeToolbarColor(R.color.color_pastel_2);
            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_2);
            Log.d("selected", String.valueOf(selectedColor));
        });
        btnPastel3.setOnClickListener(v -> {
            changeToolbarColor(R.color.color_pastel_3);
            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_3);
            Log.d("selected", String.valueOf(selectedColor));
        });

        deleteBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add_menu, menu);
        return true;
    }

    private void changeToolbarColor(int colorResId) {
        toolbar.setBackgroundColor(ContextCompat.getColor(this, colorResId));
    }

    private void saveNote() {
        String title = edtTitle.getText().toString();
        String description = edtBody.getText().toString();

        if (!title.isEmpty() && !description.isEmpty()) {
            if (noteId == -1) {
                Note newNote = new Note(title, description, Integer.valueOf(selectedColor));
                NoteManager.addNote(newNote);
            } else {
                Note updatedNote = new Note(noteId, title, description, Integer.valueOf(selectedColor));
                NoteManager.updateNote(updatedNote);
            }
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

    private int getSelectedColor() {
        return Color.RED;
    }
}
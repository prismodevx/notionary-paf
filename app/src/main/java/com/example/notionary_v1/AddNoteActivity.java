package com.example.notionary_v1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.Note;
import com.example.notionary_v1.fragments.data.NoteManager;
import com.example.notionary_v1.fragments.data.RetrofitInstance;
import com.example.notionary_v1.fragments.data.TokenManager;
import com.example.notionary_v1.interf.NotesApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {
    private EditText edtTitle;
    private EditText edtBody;
    private int selectedColor;
    private long noteId = -1;
    private Toolbar toolbar;
    private ImageButton deleteBtn;

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        deleteBtn = findViewById(R.id.action_delete);
//        deleteBtn.setVisibility(View.INVISIBLE);

        edtTitle = findViewById(R.id.edt_title);
        edtBody = findViewById(R.id.edt_body);
        tokenManager = new TokenManager(this);

        Log.d("API Error", edtTitle.getText().toString());
        Log.d("API Error", edtBody.getText().toString());

//        Intent intent = getIntent();
//        noteId = intent.getLongExtra("note_id", -1);
//        String title = intent.getStringExtra("note_title");
//        String description = intent.getStringExtra("note_description");

//        if (noteId != -1) {
//            edtTitle.setText(title);
//            edtBody.setText(description);
//            deleteBtn.setVisibility(View.VISIBLE);
//            setTitle("Editar Nota");
//        }

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            Log.d("API Error", String.valueOf(edtTitle.getText()));
            Log.d("API Error", String.valueOf(edtBody.getText()));
            String t = String.valueOf(edtTitle.getText());
            String d = String.valueOf(edtBody.getText());
            saveNote(String.valueOf(edtTitle.getText()), String.valueOf(edtBody.getText()));
//
//            if (!t.isEmpty() && !d.isEmpty()) {
//                saveNote(t, d);
//            } else {
//                Toast.makeText(this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
//            }
        });

        Button btnPastel1 = findViewById(R.id.btn_pastel_1);
        Button btnPastel2 = findViewById(R.id.btn_pastel_2);
        Button btnPastel3 = findViewById(R.id.btn_pastel_3);


        btnPastel1.setOnClickListener(v -> {
//            changeToolbarColor(R.color.color_pastel_1);
            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_1);
            Log.d("c", String.valueOf(selectedColor));
        });
        btnPastel2.setOnClickListener(v -> {
//            changeToolbarColor(R.color.color_pastel_2);
            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_2);
            Log.d("selected", String.valueOf(selectedColor));
        });
        btnPastel3.setOnClickListener(v -> {
//            changeToolbarColor(R.color.color_pastel_3);
            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_3);
            Log.d("selected", String.valueOf(selectedColor));
        });

//        deleteBtn.setOnClickListener(v -> {
//            new AlertDialog.Builder(this)
//                    .setTitle("Eliminar notas")
//                    .setMessage("¿Eliminar esta nota?")
//                    .setPositiveButton("Eliminar", (dialog, which) -> {
//                        deleteNote(noteId);
//                        dialog.dismiss();
//                    })
//                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
//                    .show();
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add_menu, menu);
        return true;
    }

    private void saveNote(String title, String description) {
        String token = "JWT " + tokenManager.getToken();
        Log.d("token", tokenManager.getToken());

        NotesApi apiService = RetrofitInstance.getRetrofitInstance().create(NotesApi.class);
        Note newNote = new Note(title, description);

        Call<ApiResponse> call = apiService.createNote(newNote, token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Nota guardada exitosamente");
                    Toast.makeText(AddNoteActivity.this, "Nota guardada exitosamente", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    try {
                        String errorMessage = response.errorBody().string();  // Obtener el mensaje de error
                        Log.e("API Error", "Error al guardar la nota: " + errorMessage);

                        // Intenta imprimir el cuerpo de la respuesta, aunque sea un error
                        Log.d("API Response", "Error response: " + response.body());

                        Toast.makeText(AddNoteActivity.this, "Error al guardar la nota: " + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Error al obtener el mensaje de error: " + e.getMessage());
                        Toast.makeText(AddNoteActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Error de conexión: " + t.getMessage());
                Toast.makeText(AddNoteActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void saveNote() {
//        String title = edtTitle.getText().toString();
//        String description = edtBody.getText().toString();
//
//        if (!title.isEmpty() && !description.isEmpty()) {
//            if (noteId == -1) {
//                Note newNote = new Note(title, description, Integer.valueOf(selectedColor));
//                NoteManager.addNote(newNote);
//            } else {
//                Note updatedNote = new Note(noteId, title, description, Integer.valueOf(selectedColor));
//                NoteManager.updateNote(updatedNote);
//            }
//            finish();
//        }
//    }

//    private void deleteNote(long noteId) {
//        if (noteId != -1) {
//            NoteManager.deleteNote(noteId);
//        }
//        finish();
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    private int getSelectedColor() {
//        return Color.RED;
//    }
}
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
import android.widget.TextView;
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

import com.example.notionary_v1.fragments.components.CustomDialog;
import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.Note;
import com.example.notionary_v1.fragments.data.NoteManager;
import com.example.notionary_v1.fragments.data.RetrofitInstance;
import com.example.notionary_v1.fragments.data.TokenManager;
import com.example.notionary_v1.interf.NotesApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {
    private EditText edtTitle;
    private EditText edtBody;
    private int selectedColor;
    private long id = -1;
    private Toolbar toolbar;
    private ImageButton deleteBtn;
    private Button selectedButton = null;

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

        TextView txtFecha = findViewById(R.id.txt_fecha);

        deleteBtn = findViewById(R.id.action_delete);
//        deleteBtn.setVisibility(View.INVISIBLE);



        edtTitle = findViewById(R.id.edt_title);
        edtBody = findViewById(R.id.edt_body);
        tokenManager = new TokenManager(this);

        Intent intent = getIntent();
        id = intent.getLongExtra("note_id", -1);
        String title = intent.getStringExtra("note_title");
        String description = intent.getStringExtra("note_description");

        String fechaActual = obtenerFechaActual();
        txtFecha.setText(fechaActual);

        if (id != -1) {
            edtTitle.setText(title);
            edtBody.setText(description);
            deleteBtn.setVisibility(View.VISIBLE);
            setTitle("Editar Nota");
        }

        deleteBtn.setOnClickListener(v -> {
            CustomDialog dialog = new CustomDialog(this)
                    .setTitle("Eliminar nota")
                    .setIcon(R.drawable.ic_error)
                    .setCancelButton("Cancelar", null)
                    .setOkButton("Eliminar", view -> {
                        deleteNote(id);
                    });
            dialog.show();
        });

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            saveNote(String.valueOf(edtTitle.getText()), String.valueOf(edtBody.getText()));
//
//            if (!t.isEmpty() && !d.isEmpty()) {
//                saveNote(t, d);
//            } else {
//                Toast.makeText(this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
//            }
        });

//        Button btnPastel1 = findViewById(R.id.btn_pastel_1);
//        Button btnPastel2 = findViewById(R.id.btn_pastel_2);
//        Button btnPastel3 = findViewById(R.id.btn_pastel_3);
//
//
//        btnPastel1.setOnClickListener(v -> {
////            changeToolbarColor(R.color.color_pastel_1);
//            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_1);
////            updateSelectedButton(btnPastel1);
//            Log.d("c", String.valueOf(selectedColor));
//        });
//        btnPastel2.setOnClickListener(v -> {
////            changeToolbarColor(R.color.color_pastel_2);
//            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_2);
////            updateSelectedButton(btnPastel2);
//            Log.d("selected", String.valueOf(selectedColor));
//        });
//        btnPastel3.setOnClickListener(v -> {
////            changeToolbarColor(R.color.color_pastel_3);
//            selectedColor = ContextCompat.getColor(this, R.color.color_pastel_3);
////            updateSelectedButton(btnPastel3);
//            Log.d("selected", String.valueOf(selectedColor));
//        });

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
    private String obtenerFechaActual() {
//        SimpleDateFormat sdf = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


//    private void updateSelectedButton(Button button) {
//        if (selectedButton != null) {
//            // Restablecer el fondo del botón previamente seleccionado
//            selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.default_button_color));
//        }
//
//        // Cambiar el fondo del nuevo botón seleccionado
//        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.selected_button_color));
//
//        // Actualizar el botón seleccionado
//        selectedButton = button;
//    }

    private void saveNote(String title, String description) {
        String token = "JWT " + tokenManager.getToken();
        NotesApi apiService = RetrofitInstance.getRetrofitInstance().create(NotesApi.class);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.d("elid", String.valueOf(id));
        if (id == -1) {
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
                            String errorMessage = response.errorBody().string();
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
        else {
            Note updatedNote = new Note(title, description);
            Call<ApiResponse> call = apiService.updateNote((int) id, updatedNote, token);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddNoteActivity.this, "Nota actualizada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            String errorMessage = response.errorBody().string();
                            Log.e("API Error", "Error al actualizar la nota: " + errorMessage);

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
    }

    private void deleteNote(long noteId) {
        if (noteId == -1) {
            Toast.makeText(this, "Nota no válida para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "JWT " + tokenManager.getToken();
        Log.d("token", token);

        NotesApi apiService = RetrofitInstance.getRetrofitInstance().create(NotesApi.class);

        Call<ApiResponse> call = apiService.deleteNote((int) noteId, token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Nota eliminada exitosamente");
                    Toast.makeText(AddNoteActivity.this, "Nota eliminada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error al eliminar la nota: " + errorMessage);
                        Toast.makeText(AddNoteActivity.this, "Error al eliminar la nota: " + errorMessage, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
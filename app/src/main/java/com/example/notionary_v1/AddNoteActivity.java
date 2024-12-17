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

import org.json.JSONObject;
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
import com.example.notionary_v1.fragments.data.UsuarioNote;
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

    private ImageButton shareBtn;

    private TokenManager tokenManager;

    private boolean esFavorito = false;
    private ImageButton favBtn;

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
        shareBtn = findViewById(R.id.action_share);

        deleteBtn.setVisibility(View.INVISIBLE);
        shareBtn.setVisibility(View.INVISIBLE);

        favBtn = findViewById(R.id.action_fav);
        updateFavoriteIcon();


        edtTitle = findViewById(R.id.edt_title);
        edtBody = findViewById(R.id.edt_body);
        tokenManager = new TokenManager(this);

        Intent intent = getIntent();
        id = intent.getLongExtra("note_id", -1);
        String title = intent.getStringExtra("note_title");
        String description = intent.getStringExtra("note_description");
        esFavorito = intent.getIntExtra("note_like", 0) == 1;

        String fechaActual = obtenerFechaActual();
        txtFecha.setText(fechaActual);

        if (id != -1) {
            edtTitle.setText(title);
            edtBody.setText(description);
            deleteBtn.setVisibility(View.VISIBLE);
            shareBtn.setVisibility(View.VISIBLE);
            setTitle("Editar Nota");
            updateFavoriteIcon();
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

        shareBtn.setOnClickListener(v -> {
            // Mostrar el diálogo para compartir la nota
            openShareNoteDialog((int) id);
        });

        favBtn.setOnClickListener(v -> {
            esFavorito = !esFavorito; // Alternar el estado
            updateFavoriteIcon();
//            likeNote((int) id);
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

    private void updateFavoriteIcon() {
        if (esFavorito) {
            favBtn.setImageResource(R.drawable.ic_fav_filled); // Ícono lleno
        } else {
            favBtn.setImageResource(R.drawable.ic_fav); // Ícono vacío
        }
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

    private void openShareNoteDialog(int notaId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_share_note, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Inicializar vistas del diálogo
        EditText etUsername = dialogView.findViewById(R.id.et_username);
        Button btnSend = dialogView.findViewById(R.id.btn_send);

        btnSend.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (!username.isEmpty()) {
                shareNoteWithUser(username, notaId);
                dialog.dismiss();
            } else {
                boolean isValid = true;

                String validPattern = "^[a-zA-Z0-9_]+$";
                if (!username.matches(validPattern)) {
                    etUsername.setError("El Usuario solo puede contener letras, números y _");
                    isValid = false;
                } else {
                    etUsername.setError(null);
                }
                if (!isValid) return;
            }
        });

        dialog.show();
    }

    private void likeNote(int noteId) {

    }

    private void shareNoteWithUser(String username, int notaId) {
        String token = "JWT " + tokenManager.getToken();
        NotesApi apiService = RetrofitInstance.getRetrofitInstance().create(NotesApi.class);

        Log.d("API", tokenManager.getId());
        Log.d("API", tokenManager.getId());
        UsuarioNote usuarioNote = new UsuarioNote(username, notaId);
        Call<ApiResponse> call = apiService.shareNote(usuarioNote, token);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Nota compartida exitosamente");
                    Toast.makeText(AddNoteActivity.this, "Nota compartida exitosamente", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();

                        JSONObject errorJson = new JSONObject(errorBody);
                        String message = errorJson.optString("message", "Error desconocido");

                        Toast.makeText(AddNoteActivity.this, message, Toast.LENGTH_SHORT).show();
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


//    private void updateSelectedButton(Button button) {
//        if (selectedButton != null) {
//            selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.default_button_color));
//        }
//
//        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.selected_button_color));
//
//        selectedButton = button;
//    }

    private void saveNote(String title, String description) {
        String token = "JWT " + tokenManager.getToken();
        NotesApi apiService = RetrofitInstance.getRetrofitInstance().create(NotesApi.class);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.d("elid", String.valueOf(id));
        if (id == -1) {
            Log.d("API", tokenManager.getId());
            Note newNote = new Note(title, description, currentDate, Integer.parseInt(tokenManager.getId()), esFavorito ? 1 : 0);
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
            Note updatedNote = new Note(title, description, currentDate, Integer.parseInt(tokenManager.getId()), esFavorito ? 1 : 0);
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
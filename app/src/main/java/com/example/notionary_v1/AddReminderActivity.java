package com.example.notionary_v1;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notionary_v1.fragments.components.CustomDialog;
import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.Reminder;
import com.example.notionary_v1.fragments.data.RetrofitInstance;
import com.example.notionary_v1.fragments.data.TokenManager;
import com.example.notionary_v1.fragments.data.UsuarioReminder;
import com.example.notionary_v1.interf.NotesApi;
import com.example.notionary_v1.interf.RemindersApi;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReminderActivity extends AppCompatActivity {
    private EditText edtTitulo;
    private Button btnSelectDate, btnSelectTime, btnSetReminder;
    private TextView txtSelectedDateTime;
    private Calendar calendar;

    private long id = -1;

    private ImageButton deleteBtn;
    private ImageButton shareBtn;
    private Toolbar toolbar;

    private TokenManager tokenManager;

    private boolean esFavorito = false;
    private ImageButton favBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        deleteBtn = findViewById(R.id.action_delete);
        shareBtn = findViewById(R.id.action_share);

        deleteBtn.setVisibility(View.INVISIBLE);
        shareBtn.setVisibility(View.INVISIBLE);

        favBtn = findViewById(R.id.action_fav);
        updateFavoriteIcon();

        edtTitulo = findViewById(R.id.edt_title);
        tokenManager = new TokenManager(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("reminder_id", -1);
        String title = intent.getStringExtra("reminder_titulo");
        String dateTime = intent.getStringExtra("reminder_fecha_hora");
        esFavorito = intent.getIntExtra("reminder_like", 0) == 1;

        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSelectTime = findViewById(R.id.btn_select_time);
        txtSelectedDateTime = findViewById(R.id.txt_selected_datetime);
        btnSetReminder = findViewById(R.id.btn_set_reminder);

        calendar = Calendar.getInstance();

        btnSelectDate.setOnClickListener(v -> openDatePicker());
        btnSelectTime.setOnClickListener(v -> openTimePicker());

        if (id != -1) {
            edtTitulo.setText(title);
//            edtBody.setText(description);
            txtSelectedDateTime.setText(formatDateToReadable(dateTime));

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                calendar.setTime(dateFormat.parse(dateTime)); // Cargar la fecha al objeto Calendar
            } catch (Exception e) {
                Log.e("DATE_PARSE", "Error al parsear fecha: " + e.getMessage());
            }

            deleteBtn.setVisibility(View.VISIBLE);
            shareBtn.setVisibility(View.VISIBLE);
            setTitle("Editar Recordatorio");
            updateFavoriteIcon();
        }

        deleteBtn.setOnClickListener(v -> {
            CustomDialog dialog = new CustomDialog(this)
                    .setTitle("Eliminar recordatorio")
                    .setIcon(R.drawable.ic_error)
                    .setCancelButton("Cancelar", null)
                    .setOkButton("Eliminar", view -> {
                        deleteReminder(id);
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

        btnSetReminder.setOnClickListener(v -> {
//            setAlarm();
            String fechaHora = getFormattedDateTimeForDB();
            saveReminder(String.valueOf(edtTitulo.getText()), fechaHora);
        });
    }

    private void openShareNoteDialog(int recordatorioId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_share_note, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        EditText etUsername = dialogView.findViewById(R.id.et_username);
        Button btnSend = dialogView.findViewById(R.id.btn_send);

        btnSend.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (!username.isEmpty()) {
                shareReminderWithUser(username, recordatorioId);
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

    private void shareReminderWithUser(String username, int recordatorioId) {
        String token = "JWT " + tokenManager.getToken();
        RemindersApi apiService = RetrofitInstance.getRetrofitInstance().create(RemindersApi.class);

        UsuarioReminder usuarioReminder = new UsuarioReminder(username, recordatorioId);
        Call<ApiResponse> call = apiService.shareReminder(usuarioReminder, token);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Recordatorio compartido exitosamente");
                    Toast.makeText(AddReminderActivity.this, "Recordatorio compartido exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();

                        JSONObject errorJson = new JSONObject(errorBody);
                        String message = errorJson.optString("message", "Error desconocido");

                        Toast.makeText(AddReminderActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Error al obtener el mensaje de error: " + e.getMessage());
                        Toast.makeText(AddReminderActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Error de conexión: " + t.getMessage());
                Toast.makeText(AddReminderActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatDateToReadable(String dateTime) {
        try {
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            SimpleDateFormat readableFormat = new SimpleDateFormat("E, d 'de' MMM 'de' yyyy HH:mm", new Locale("es", "ES"));
            return readableFormat.format(dbFormat.parse(dateTime));
        } catch (Exception e) {
            Log.e("DATE_FORMAT", "Error al formatear fecha: " + e.getMessage());
            return dateTime; // Si falla, devuelve el valor original
        }
    }

    private void openDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTimeText();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void openTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    updateDateTimeText();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePicker.show();
    }

    private String getFormattedDateTimeForDB() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void updateDateTimeText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d 'de' MMM 'de' yyyy HH:mm", new Locale("es", "ES"));
        String formattedDate = dateFormat.format(calendar.getTime());
        txtSelectedDateTime.setText(formattedDate);
    }

    private void updateFavoriteIcon() {
        if (esFavorito) {
            favBtn.setImageResource(R.drawable.ic_fav_filled); // Ícono lleno
        } else {
            favBtn.setImageResource(R.drawable.ic_fav); // Ícono vacío
        }
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//        Intent intent = new Intent(this, ReminderReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//
//        Toast.makeText(this, "Recordatorio guardado", Toast.LENGTH_SHORT).show();
    }

    private void saveReminder(String titulo, String fechaHora) {
        String token = "JWT " + tokenManager.getToken();
        RemindersApi apiService = RetrofitInstance.getRetrofitInstance().create(RemindersApi.class);



        if (id == -1) {
            Reminder newReminder = new Reminder(titulo, fechaHora, Integer.parseInt(tokenManager.getId()), esFavorito ? 1 : 0);
            Call<ApiResponse> call = apiService.createReminder(newReminder, token);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d("API", "Recordatorio guardado exitosamente");
                        Toast.makeText(AddReminderActivity.this, "Recordatorio guardado exitosamente", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                    else {
                        try {
                            String errorMessage = response.errorBody().string();
                            Log.e("API Error", "Error al guardar la nota: " + errorMessage);

                            Log.d("API Response", "Error response: " + response.body());

                            Toast.makeText(AddReminderActivity.this, "Error al guardar la nota: " + errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("API Error", "Error al obtener el mensaje de error: " + e.getMessage());
                            Toast.makeText(AddReminderActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("API Error", "Error de conexión: " + t.getMessage());
                    Toast.makeText(AddReminderActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Reminder updatedReminder = new Reminder(titulo, fechaHora, Integer.parseInt(tokenManager.getId()), esFavorito ? 1 : 0);
            Call<ApiResponse> call = apiService.updateNote((int) id, updatedReminder, token);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d("API", "Recordatorio actualizado exitosamente");
                        Toast.makeText(AddReminderActivity.this, "Recordatorio actualizado exitosamente", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                    else {
                        try {
                            String errorMessage = response.errorBody().string();
                            Log.e("API Error", "Error al guardar la nota: " + errorMessage);

                            Log.d("API Response", "Error response: " + response.body());

                            Toast.makeText(AddReminderActivity.this, "Error al guardar la nota: " + errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("API Error", "Error al obtener el mensaje de error: " + e.getMessage());
                            Toast.makeText(AddReminderActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("API Error", "Error de conexión: " + t.getMessage());
                    Toast.makeText(AddReminderActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteReminder(long reminderId) {
        if (reminderId == -1) {
            Toast.makeText(this, "Recordatorio no válida para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "JWT " + tokenManager.getToken();

        RemindersApi apiService = RetrofitInstance.getRetrofitInstance().create(RemindersApi.class);
        Call<ApiResponse> call = apiService.deleteReminder((int) reminderId, token);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Recordatorio eliminado exitosamente");
                    Toast.makeText(AddReminderActivity.this, "Recordatorio eliminado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error al eliminar el recordatorio: " + errorMessage);
                        Toast.makeText(AddReminderActivity.this, "Error al eliminar el recordatorio: " + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Error al obtener el mensaje de error: " + e.getMessage());
                        Toast.makeText(AddReminderActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Error de conexión: " + t.getMessage());
                Toast.makeText(AddReminderActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
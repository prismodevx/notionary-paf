package com.example.notionary_v1.interf;

import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.ApiResponseReminder;
import com.example.notionary_v1.fragments.data.Note;
import com.example.notionary_v1.fragments.data.Reminder;
import com.example.notionary_v1.fragments.data.UsuarioNote;
import com.example.notionary_v1.fragments.data.UsuarioReminder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemindersApi {
    @GET("recordatorios")
    Call<ApiResponseReminder> getReminders(@Query("idUsuario") int idUsuario, @Header("Authorization") String token);

    @POST("recordatorios")
    Call<ApiResponse> createReminder(@Body Reminder reminder, @Header("Authorization") String token);

    @DELETE("recordatorios/{id}")
    Call<ApiResponse> deleteReminder(@Path("id") int id, @Header("Authorization") String token);

    @PUT("recordatorios/{id}")
    Call<ApiResponse> updateNote(@Path("id") int id, @Body Reminder reminder, @Header("Authorization") String token);

    @POST("recordatorios-share")
    Call<ApiResponse> shareReminder(@Body UsuarioReminder usuarioReminder, @Header("Authorization") String token);
}

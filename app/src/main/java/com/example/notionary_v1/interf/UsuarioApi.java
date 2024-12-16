package com.example.notionary_v1.interf;

import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioApi {
    @POST("usuarios")
    Call<ApiResponse> createUser(@Body Usuario usuario);
}

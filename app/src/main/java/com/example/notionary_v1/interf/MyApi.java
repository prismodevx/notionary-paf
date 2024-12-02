package com.example.notionary_v1.interf;

import com.example.notionary_v1.model.AuthRequest;
import com.example.notionary_v1.model.AuthResponse;
import com.example.notionary_v1.model.LeerUsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyApi {
    @POST("auth")
    Call<AuthResponse> autenticar(@Body AuthRequest authRequest);
}

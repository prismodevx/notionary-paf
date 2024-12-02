package com.example.notionary_v1.fragments.data;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            // Crear el TokenManager
            TokenManager tokenManager = new TokenManager(context);

            // Configurar el OkHttpClient con el interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenManager))
                    .build();

            // Crear la instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://alexismendoza.pythonanywhere.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}

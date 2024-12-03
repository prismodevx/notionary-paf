package com.example.notionary_v1.fragments.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String BASE_URL = "https://alexismendoza.pythonanywhere.com/api/v1/";

    private static Retrofit retrofit;

    // Método para obtener la instancia de Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)  // Configura la URL base
                    .addConverterFactory(GsonConverterFactory.create())  // Conversor de JSON a objetos Java
                    .build();
        }
        return retrofit;
    }
}

package com.example.notionary_v1.utils;

import android.util.Base64;

import org.json.JSONObject;

public class JwtDecoder {
    public static String getIdentityFromToken(String jwtToken) {
        try {
            // Dividir el token en sus partes
            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("El token no tiene el formato correcto.");
            }

            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));

            JSONObject jsonObject = new JSONObject(payload);

            return jsonObject.optString("identity", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

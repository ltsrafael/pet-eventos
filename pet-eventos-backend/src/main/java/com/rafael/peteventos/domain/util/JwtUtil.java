package com.rafael.peteventos.domain.util;

import com.google.gson.Gson;
import com.rafael.peteventos.domain.dto.PayloadDto;

import java.util.Base64;

public class JwtUtil {

    public static String getEmail(String bearer) {
        String[] chunks = getToken(bearer).split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Gson gson = new Gson();
        return gson.fromJson(payload, PayloadDto.class).getSub();
    }

    public static String getToken(String bearer) {
        if (bearer != null && bearer.startsWith("Bearer")) {
            return bearer.substring(7);
        } else {
            throw new RuntimeException("Token inválido.");
        }
    }


}

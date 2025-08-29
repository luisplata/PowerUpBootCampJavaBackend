package com.peryloth.jwtvalidation;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JwtTokenProvider {

    // ⚠️ En real lo sacamos del application.yml
    private static final String SECRET_KEY = "bootcamp_java_super_secure_secret_key_123456";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()   // ✅ en 0.13.0
                    .setSigningKey(KEY) // usar la clave secreta
                    .build()
                    .parseClaimsJws(token); // parsea y valida firma
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("❌ Token inválido: " + e.getMessage());
            return false;
        }
    }
}

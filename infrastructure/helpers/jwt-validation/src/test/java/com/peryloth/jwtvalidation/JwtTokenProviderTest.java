package com.peryloth.jwtvalidation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

class JwtTokenProviderTest {

    @Test
    void testValidateTokenReactiveValid() {
        // ⚠️ Usa la misma SECRET_KEY definida en JwtTokenProvider
        String secret = "bootcamp_java_super_secure_secret_key_123456";
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String validToken = Jwts.builder()
                .setSubject("valid@test.com")
                .setIssuer("test-service")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        StepVerifier.create(JwtTokenProvider.validateTokenReactive(validToken))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testValidateTokenReactiveInvalid() {
        String invalidToken = "tokenInvalido";

        StepVerifier.create(JwtTokenProvider.validateTokenReactive(invalidToken))
                .expectNext(false)
                .verifyComplete();
    }
}

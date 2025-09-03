package com.peryloth.jwtvalidation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class JwtValidacionAdapterTest {

    private JwtValidacionAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JwtValidacionAdapter();
    }

    @Test
    void testEncodeAndMatchesPassword() {
        String rawPassword = "password123";
        String encoded = adapter.encode(rawPassword);

        System.out.println("Encoded: " + encoded);

        assertNotNull(encoded);
        assertTrue(adapter.matches(rawPassword, encoded));
        assertFalse(adapter.matches("otraClave", encoded));
    }

    @Test
    void testCreateTokenAndValidate() {
        String email = "test@example.com";

        Mono<String> tokenMono = adapter.createToken(email);

        StepVerifier.create(tokenMono.flatMap(token -> {
                    System.out.println("Token generado: " + token);
                    return adapter.validate("Bearer " + token);
                }))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testPasswordMatches() {
        JwtValidacionAdapter adapter = new JwtValidacionAdapter();
        String rawPassword = "password";
        String encoded = adapter.encode(rawPassword);

        System.out.println("Raw: " + rawPassword);
        System.out.println("Encoded: " + encoded);
        System.out.println("Encoded: $2a$10$qEGSRlLBw1iN2I08/4tJGeLW48dYXH4eJjlMgA1EsNUG7KiLHrJTW");

        assertTrue(adapter.matches(rawPassword, encoded)); // ✅ debe pasar
        assertFalse(adapter.matches("otro", encoded));     // ❌ debe fallar
    }

    @Test
    void testValidateInvalidToken() {
        String invalidToken = "Bearer tokenInvalido";

        StepVerifier.create(adapter.validate(invalidToken))
                .expectNext(false)
                .verifyComplete();
    }
}

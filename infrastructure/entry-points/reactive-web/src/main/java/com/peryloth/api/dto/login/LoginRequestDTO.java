package com.peryloth.api.dto.login;

public record LoginRequestDTO(
        String email,
        String password
) {}

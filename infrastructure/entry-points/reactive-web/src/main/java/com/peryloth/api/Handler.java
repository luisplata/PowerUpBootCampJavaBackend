package com.peryloth.api;

import com.peryloth.api.dto.registry.UserValidationRequest;
import com.peryloth.api.dto.registry.UsuarioRequestDTO;
import com.peryloth.api.mapper.registry.UserDTOMapper;
import com.peryloth.usecase.registry_user.IRegistryUserUseCase;
import com.peryloth.usecase.validationclient.IValidationClientUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final IRegistryUserUseCase registryUserUseCase;
    private final UserDTOMapper userDTOMapper;
    private final IValidationClientUseCase validationClientUseCase;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Recibe un objeto UsuarioRequestDTO y guarda un usuario en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario guardado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UsuarioRequestDTO.class)
                .flatMap(dto ->
                        registryUserUseCase.registryUser(userDTOMapper.mapToEntity(dto))
                                .then(ServerResponse.ok().bodyValue("Usuario guardado correctamente"))
                )
                .onErrorResume(IllegalArgumentException.class,
                        e -> ServerResponse.badRequest().bodyValue("Error de validación: " + e.getMessage()))
                .onErrorResume(e -> ServerResponse.status(500).bodyValue("Error interno: " + e.getMessage()));
    }

    public Mono<ServerResponse> validateUser(ServerRequest request) {

        return request.bodyToMono(UserValidationRequest.class)
                .flatMap(usuarioRequest ->
                        validationClientUseCase.IsUserValid(request.headers().firstHeader("Authorization"), usuarioRequest.getId(), usuarioRequest.getEmail())
                                .flatMap(isValid -> {
                                    if (Boolean.TRUE.equals(isValid)) {
                                        return ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(true);
                                    } else {
                                        return ServerResponse.status(401)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(false);
                                    }
                                }));
    }
}

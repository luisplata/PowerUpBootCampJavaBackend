package com.peryloth.api;

import com.peryloth.api.DTO.registry.RegistryUserDTO;
import com.peryloth.api.DTO.registry.UsuarioRequestDTO;
import com.peryloth.api.mapper.registry.UserDTOMapper;
import com.peryloth.usecase.registryuser.IRegistryUserUseCase;
import com.peryloth.usecase.solicitud.ISolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class Handler {
//private  final UseCase useCase;
//private  final UseCase2 useCase2;

    private final IRegistryUserUseCase registryUserUseCase;
    private final ISolicitudUseCase solicitudUseCase;
    private final UserDTOMapper userDTOMapper;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UsuarioRequestDTO.class)
                .flatMap(dto ->
                        registryUserUseCase.RegistryUser(userDTOMapper.mapToEntity(dto))
                                .then(ServerResponse.ok().bodyValue("Usuario guardado correctamente"))
                )
                .onErrorResume(IllegalArgumentException.class,
                        e -> ServerResponse.badRequest().bodyValue("Error de validación: " + e.getMessage()))
                .onErrorResume(e -> ServerResponse.status(500).bodyValue("Error interno: " + e.getMessage()));
    }

    public Mono<ServerResponse> loanRequest(ServerRequest serverRequest){
        return ServerResponse.ok().bodyValue("OK");
    }
}

package com.peryloth.api;

import com.peryloth.api.DTO.registry.RegistryUserDTO;
import com.peryloth.api.mapper.registry.UserDTOMapper;
import com.peryloth.usecase.registryuser.IRegistryUserUseCase;
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
        // Logic to save user
        registryUserUseCase.RegistryUser(userDTOMapper.mapToEntity(new RegistryUserDTO(
                Long.valueOf(1), "John", "Doe", "asasas@sasas.asas",
                LocalDate.of(1990, 1, 1),
                "calle", 123456
        )));
        return ServerResponse.ok().bodyValue("User saved successfully");
    }
}

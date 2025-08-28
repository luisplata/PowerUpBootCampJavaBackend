package com.peryloth.api;

import com.peryloth.api.dto.registry.UsuarioRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    beanClass = Handler.class,
                    beanMethod = "saveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Registrar un nuevo usuario",
                            description = "Recibe un objeto UsuarioRequestDTO y guarda un usuario en el sistema",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos del usuario a registrar",
                                    content = @Content(schema = @Schema(implementation = UsuarioRequestDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario guardado correctamente",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = String.class))),
                                    @ApiResponse(responseCode = "400", description = "Error de validación",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = String.class))),
                                    @ApiResponse(responseCode = "500", description = "Error interno",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = String.class)))
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::saveUser);
    }
}

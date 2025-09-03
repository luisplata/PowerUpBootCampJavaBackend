package com.peryloth.api.mapper.registry;

import com.peryloth.api.dto.registry.UsuarioRequestDTO;
import com.peryloth.api.dto.registry.UsuarioResponseDTO;
import com.peryloth.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "intentosFallidos", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    Usuario mapToEntity(UsuarioRequestDTO userDTO);

    UsuarioRequestDTO mapToDTO(Usuario usuario);

    UsuarioRequestDTO mapToRequestDTO(Usuario usuario);

    UsuarioResponseDTO mapToResponseDTO(Usuario usuario);

    Usuario mapToEntity(UsuarioResponseDTO usuarioResponseDTO);
}

package com.peryloth.api.mapper.registry;

import com.peryloth.api.dto.registry.RegistryUserDTO;
import com.peryloth.api.dto.registry.UsuarioRequestDTO;
import com.peryloth.api.dto.registry.UsuarioResponseDTO;
import com.peryloth.model.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    
    Usuario mapToEntity(RegistryUserDTO userDTO);

    RegistryUserDTO mapToDTO(Usuario usuario);

    UsuarioRequestDTO mapToRequestDTO(Usuario usuario);

    Usuario mapToEntity(UsuarioRequestDTO usuarioRequestDTO);

    UsuarioResponseDTO mapToResponseDTO(Usuario usuario);

    Usuario mapToEntity(UsuarioResponseDTO usuarioResponseDTO);

}

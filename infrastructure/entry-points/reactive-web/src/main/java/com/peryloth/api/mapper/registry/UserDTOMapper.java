package com.peryloth.api.mapper.registry;

import com.peryloth.api.DTO.registry.RegistryUserDTO;
import com.peryloth.model.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    // Method to convert a UserDTO to a User entity
    Usuario mapToEntity(RegistryUserDTO userDTO);

    // Method to convert a User entity to a UserDTO
    RegistryUserDTO mapToDTO(Usuario usuario);
}

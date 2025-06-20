package com.organizo.organizobackend.mapper;

import com.organizo.organizobackend.dto.ClienteDTO;
import com.organizo.organizobackend.model.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteDTO toDto(Cliente entity);
    Cliente toEntity(ClienteDTO dto);
}
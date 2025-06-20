package com.organizo.organizobackend.mapper;

import com.organizo.organizobackend.dto.SalaoDTO;
import com.organizo.organizobackend.model.Salao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Converte entidade Salao <-> DTO usando MapStruct.
 */
@Mapper(
        componentModel = "spring",
        imports = { java.util.stream.Collectors.class }
)
public interface SalaoMapper {

    // quando for entidade → DTO, pegamos o owner.id e colocamos em ownerId
    @Mapping(source = "owner.id", target = "ownerId")
    SalaoDTO toDto(Salao entity);

    // quando formos DTO → entidade, IGNORAR o owner (será preenchido pela service)
    @Mapping(target = "owner", ignore = true)
    Salao toEntity(SalaoDTO dto);
}
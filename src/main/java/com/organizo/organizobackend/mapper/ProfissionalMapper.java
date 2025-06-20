package com.organizo.organizobackend.mapper;

import com.organizo.organizobackend.dto.ProfissionalDTO;
import com.organizo.organizobackend.model.Profissional;
import com.organizo.organizobackend.model.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        imports = { java.util.stream.Collectors.class }
)
public interface ProfissionalMapper {


    @Mapping(target = "salaoId", source = "salao.id")
    @Mapping(target = "servicoIds", expression = "java(entity.getServicos().stream().map(s -> s.getId()).collect(Collectors.toSet()))")
    // Mapeia enum diretamente
    @Mapping(target = "cargo", source = "cargo")
    ProfissionalDTO toDto(Profissional entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salao", ignore = true)
    @Mapping(target = "servicos", ignore = true)
        // O campo cargo é mapeado automaticamente pelo MapStruct
    Profissional toEntity(ProfissionalDTO dto);

    /**
     * Método auxiliar para MapStruct converter Set<Servico> em Set<Long> (IDs).
     */
    @Named("servicoEntityParaIds")
    default Set<Long> mapServicosParaIds(Set<Servico> servicos) {
        if (servicos == null) {
            return null;
        }
        return servicos.stream()
                .map(Servico::getId)
                .collect(Collectors.toSet());
    }
}

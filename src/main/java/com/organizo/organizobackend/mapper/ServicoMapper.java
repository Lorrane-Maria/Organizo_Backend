package com.organizo.organizobackend.mapper;

import com.organizo.organizobackend.dto.ServicoDTO;
import com.organizo.organizobackend.model.Profissional;
import com.organizo.organizobackend.model.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Profissional.class, Collectors.class})
public interface ServicoMapper {

    @Mapping(target = "salaoId", source = "salao.id")
    @Mapping(target = "profissionalIds",
            expression = "java(entity.getProfissionais().stream()"
                    + ".map(Profissional::getId)"
                    + ".collect(Collectors.toSet()))")
    ServicoDTO toDto(Servico entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salao", ignore = true)
    @Mapping(target = "profissionais",
            expression = "java(dto.getProfissionalIds().stream().map(id -> { "
                    + "Profissional p = new Profissional(); "
                    + "p.setId(id); "
                    + "return p; "
                    + "}).collect(Collectors.toSet()))")
    Servico toEntity(ServicoDTO dto);
}

package com.organizo.organizobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(name = "Cliente", description = "Dados de um cliente")
@Getter
@Setter
@Builder // Adiciona o padrão Builder
public class ClienteDTO {

    @Schema(description = "ID do cliente", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nome do cliente", example = "Maria")
    @NotBlank(message = "nome é obrigatório")
    @Size(max = 50, message = "nome não pode exceder 50 caracteres")
    private String nome;

    @Schema(description = "Sobrenome do cliente", example = "Silva")
    @NotBlank(message = "sobrenome é obrigatório")
    @Size(max = 50, message = "sobrenome não pode exceder 50 caracteres")
    private String sobrenome;

    @Schema(description = "E-mail de contato", example = "maria@ex.com")
    @NotBlank(message = "email é obrigatório")
    @Email(message = "email deve ser válido")
    private String email;

    @Schema(description = "Telefone de contato", example = "31988887777")
    @Size(max = 20, message = "telefone não pode exceder 20 caracteres")
    private String telefone;

    @Schema(description = "Data de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime criadoEm;

    @Schema(description = "Data de última atualização do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime atualizadoEm;
}
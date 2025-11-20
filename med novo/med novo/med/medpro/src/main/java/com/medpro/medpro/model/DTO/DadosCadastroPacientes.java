package com.medpro.medpro.model.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroPacientes(
    @NotBlank String nome,
    @NotBlank String email, 
    @NotBlank String telefone,
    @NotBlank @Pattern(regexp = "\\d(11)")String cpf, 
    @NotNull @Valid DadosEndereco endereco) {
    
}

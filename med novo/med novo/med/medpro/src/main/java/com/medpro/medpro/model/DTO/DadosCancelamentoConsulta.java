package com.medpro.medpro.model.DTO;

import com.medpro.medpro.enums.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
        @NotNull(message = "ID da consulta é obrigatório")
        Long consultaId,
        
        @NotNull(message = "Motivo do cancelamento é obrigatório")
        MotivoCancelamento motivo
) {}
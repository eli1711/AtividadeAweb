package com.medpro.medpro.model.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(
        @NotNull Long pacienteId,
        Long medicoId, // Médico é opcional
        @NotNull LocalDateTime dataHoraConsulta
) {}

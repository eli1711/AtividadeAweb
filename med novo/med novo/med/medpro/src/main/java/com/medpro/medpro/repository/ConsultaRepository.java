package com.medpro.medpro.repository;

import com.medpro.medpro.model.entity.Consulta;
import com.medpro.medpro.model.entity.Medico;
import com.medpro.medpro.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByPacienteAndDataHoraConsultaBetween(Paciente paciente, LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByMedicoAndDataHoraConsulta(Medico medico, LocalDateTime dataHoraConsulta);

    // Para verificar conflito de horário (intervalo de 1h)
    boolean existsByMedicoAndDataHoraConsultaBetween(Medico medico, LocalDateTime inicio, LocalDateTime fim);
}

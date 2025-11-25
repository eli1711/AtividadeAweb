package com.medpro.medpro.service;

import com.medpro.medpro.model.DTO.DadosCancelamentoConsulta;
import com.medpro.medpro.model.entity.Consulta;
import com.medpro.medpro.repository.ConsultaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class CancelamentoConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional
    public void cancelar(DadosCancelamentoConsulta dados) {
        
        // 1. Buscar a consulta
        Consulta consulta = consultaRepository.findById(dados.consultaId())
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));

        // 2. Validar se a consulta já está cancelada
        if (consulta.getStatus() == Consulta.StatusConsulta.CANCELADA) {
            throw new IllegalArgumentException("Esta consulta já foi cancelada anteriormente");
        }

        // 3. Validar se a consulta já foi realizada
        if (consulta.getStatus() == Consulta.StatusConsulta.REALIZADA) {
            throw new IllegalArgumentException("Não é possível cancelar uma consulta já realizada");
        }

        // 4. Validar antecedência mínima de 24 horas
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataConsulta = consulta.getDataHoraConsulta();
        long horasDeAntecedencia = ChronoUnit.HOURS.between(agora, dataConsulta);

        if (horasDeAntecedencia < 24) {
            throw new IllegalArgumentException(
                "A consulta deve ser cancelada com antecedência mínima de 24 horas. " +
                "Você está tentando cancelar com apenas " + horasDeAntecedencia + " hora(s) de antecedência."
            );
        }

        // 5. Realizar o cancelamento
        consulta.cancelar(dados.motivo());
        consultaRepository.save(consulta);
    }
}
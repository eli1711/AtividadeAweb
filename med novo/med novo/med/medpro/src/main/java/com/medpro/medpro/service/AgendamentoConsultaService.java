package com.medpro.medpro.service;

import com.medpro.medpro.enums.StatusPaciente;
import com.medpro.medpro.model.DTO.DadosAgendamentoConsulta;
import com.medpro.medpro.model.entity.Consulta;
import com.medpro.medpro.model.entity.Medico;
import com.medpro.medpro.model.entity.Paciente;
import com.medpro.medpro.model.entity.Consulta.StatusConsulta;
import com.medpro.medpro.repository.ConsultaRepository;
import com.medpro.medpro.repository.MedicoRepository;
import com.medpro.medpro.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class AgendamentoConsultaService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional
    public void agendar(DadosAgendamentoConsulta dados) {

        // 1. Buscar e validar paciente
        Paciente paciente = pacienteRepository.findById(dados.pacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

        if (paciente.getStatus() == StatusPaciente.INATIVO) {
            throw new IllegalArgumentException("Paciente inativo. Não é possível agendar consulta.");
        }

        // 2. Validar data/hora básica
        LocalDateTime dataHora = dados.dataHoraConsulta();

        // 2.1 Pelo menos 30 minutos de antecedência
        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("A consulta deve ser agendada com pelo menos 30 minutos de antecedência.");
        }

        // 2.2 Horário de funcionamento: segunda a sábado, 07:00 às 19:00
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        int hora = dataHora.getHour();

        boolean domingo = diaSemana == DayOfWeek.SUNDAY;
        if (domingo || hora < 7 || hora > 18) { // última consulta começa às 18h e termina às 19h
            throw new IllegalArgumentException("Consulta fora do horário de funcionamento da clínica (seg a sáb, 07h às 19h).");
        }

        // 3. Regra: paciente não pode ter mais de uma consulta no mesmo dia
        boolean pacienteComConsultaNoMesmoDia = consultaRepository
                .existsByPacienteAndDataHoraConsultaBetween(
                        paciente,
                        dataHora.toLocalDate().atStartOfDay(),
                        dataHora.toLocalDate().atTime(23, 59, 59)
                );

        if (pacienteComConsultaNoMesmoDia) {
            throw new IllegalArgumentException("Paciente já possui uma consulta agendada para este dia.");
        }

        // 4. Selecionar / validar médico
        Medico medico;

        // Intervalo da consulta (1h de duração)
        LocalDateTime inicio = dataHora;
        LocalDateTime fim = dataHora.plusHours(1);

        if (dados.medicoId() != null) {
            // Médico informado
            medico = medicoRepository.findById(dados.medicoId())
                    .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

            if (Boolean.FALSE.equals(medico.getAtivo())) {
                throw new IllegalArgumentException("Médico inativo. Não é possível agendar consulta.");
            }

            // Verificar se o médico já possui consulta nesse intervalo de 1h
            boolean medicoOcupado = consultaRepository
                    .existsByMedicoAndDataHoraConsultaBetween(medico, inicio, fim);

            if (medicoOcupado) {
                throw new IllegalArgumentException("Médico já possui uma consulta agendada nesse horário.");
            }

        } else {
            // Médico não informado → escolher aleatoriamente um disponível
            List<Medico> medicosAtivos = medicoRepository.findAllByAtivoTrue();
            if (medicosAtivos.isEmpty()) {
                throw new IllegalArgumentException("Não há médicos ativos cadastrados.");
            }

            // Embaralha a lista para escolha aleatória
            Collections.shuffle(medicosAtivos);

            medico = medicosAtivos.stream()
                    .filter(m -> !consultaRepository.existsByMedicoAndDataHoraConsultaBetween(m, inicio, fim))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Não há médicos disponíveis para esse horário."));
        }

        // 5. Criar e salvar a consulta
        Consulta consulta = new Consulta(null, paciente, medico, dataHora, StatusConsulta.AGENDADA);
        consultaRepository.save(consulta);
    }
}

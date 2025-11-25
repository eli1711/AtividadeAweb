package com.medpro.medpro.model.entity;

import com.medpro.medpro.enums.MotivoCancelamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @Column(name = "data_hora_consulta")
    private LocalDateTime dataHoraConsulta;

    @Enumerated(EnumType.STRING)
    private StatusConsulta status;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_cancelamento")
    private MotivoCancelamento motivoCancelamento;

    public enum StatusConsulta {
        AGENDADA, REALIZADA, CANCELADA
    }

    // Construtor auxiliar para agendamento (sem motivo de cancelamento)
    public Consulta(Long id, Paciente paciente, Medico medico, LocalDateTime dataHoraConsulta, StatusConsulta status) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataHoraConsulta = dataHoraConsulta;
        this.status = status;
        this.motivoCancelamento = null;
    }

    public void cancelar(MotivoCancelamento motivo) {
        this.status = StatusConsulta.CANCELADA;
        this.motivoCancelamento = motivo;
    }
}
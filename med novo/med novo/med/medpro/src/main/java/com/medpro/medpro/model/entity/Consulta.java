package com.medpro.medpro.model.entity;

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

    @Column(name = "dataHoraConsulta")  // <-- ESSA LINHA É CRÍTICA
    private LocalDateTime dataHoraConsulta;

    @Enumerated(EnumType.STRING)
    private StatusConsulta status;

    public enum StatusConsulta {
        AGENDADA, REALIZADA, CANCELADA
    }
}
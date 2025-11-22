package com.medpro.medpro.repository;

import com.medpro.medpro.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}

package com.caroba.fiap.hospital.agendamento_service.repository;

import com.caroba.fiap.hospital.agendamento_service.model.Consulta;
import com.caroba.fiap.hospital.agendamento_service.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByMedicoAndDataConsulta(Usuario medico, LocalDateTime dataConsulta);

    List<Consulta> findAllByPaciente(Usuario paciente);
}

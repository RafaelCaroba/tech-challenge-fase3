package com.caroba.fiap.hospital.agendamento_service.repository;

import com.caroba.fiap.hospital.agendamento_service.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

}

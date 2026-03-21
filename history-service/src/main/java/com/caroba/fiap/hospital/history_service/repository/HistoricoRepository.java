package com.caroba.fiap.hospital.history_service.repository;

import com.caroba.fiap.hospital.history_service.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {

}

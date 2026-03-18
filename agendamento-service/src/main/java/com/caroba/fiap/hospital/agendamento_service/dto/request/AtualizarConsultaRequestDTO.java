package com.caroba.fiap.hospital.agendamento_service.dto.request;

import com.caroba.fiap.hospital.agendamento_service.model.StatusConsulta;

import java.time.LocalDateTime;

public record AtualizarConsultaRequestDTO(
        LocalDateTime dataConsulta,
        String descricao,
        StatusConsulta status
) {}

package com.caroba.fiap.hospital.agendamento_service.dto;

import com.caroba.fiap.hospital.agendamento_service.model.StatusConsulta;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        String pacienteNome,
        String medicoNome,
        LocalDateTime dataConsulta,
        String descricao,
        StatusConsulta status
) {}

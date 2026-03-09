package com.caroba.fiap.hospital.agendamento_service.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarConsultaRequestDTO(
        @NotNull(message = "O ID do paciente não pode estar nulo.")
        Long pacienteId,
        @NotNull(message = "O ID do médico não pode estar nulo.")
        Long medicoId,
        @NotNull(message = "A data da Consulta precisa ser informada.")
        LocalDateTime dataConsulta,
        @NotNull(message = "A descrição ou motivo da consulta devem ser informados.")
        String descricao
) {}

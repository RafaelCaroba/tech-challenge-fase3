package com.caroba.fiap.hospital.agendamento_service.dto.response;

import com.caroba.fiap.hospital.agendamento_service.model.Role;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Role role,
        Boolean ativo
) {}

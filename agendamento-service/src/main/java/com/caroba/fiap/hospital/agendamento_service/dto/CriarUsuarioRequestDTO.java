package com.caroba.fiap.hospital.agendamento_service.dto;

import com.caroba.fiap.hospital.agendamento_service.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioRequestDTO(
   @NotBlank(message = "O campo 'nome' não pode estar vazio.")
   String nome,
   @Email
   @NotBlank(message = "O campo 'email' não pode estar vazio")
   String email,
   @NotBlank(message = "O campo 'password' não pode estar vazio")
   String password,
   @NotNull(message = "O campo 'role' não pode estar vazio")
   Role role
) {}

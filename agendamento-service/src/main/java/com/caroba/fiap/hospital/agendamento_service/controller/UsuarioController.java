package com.caroba.fiap.hospital.agendamento_service.controller;

import com.caroba.fiap.hospital.agendamento_service.dto.CriarUsuarioRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.UsuarioResponseDTO;
import com.caroba.fiap.hospital.agendamento_service.model.Usuario;
import com.caroba.fiap.hospital.agendamento_service.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid CriarUsuarioRequestDTO dto){
        UsuarioResponseDTO usuario = service.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar(){
        return service.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscar(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.status(200).build();
    }
}

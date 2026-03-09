package com.caroba.fiap.hospital.agendamento_service.controller;

import com.caroba.fiap.hospital.agendamento_service.dto.AtualizarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.CriarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.model.Consulta;
import com.caroba.fiap.hospital.agendamento_service.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    // CRIAR CONSULTA - MÉDICO/ENFERMEIRO
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    @PostMapping
    public ResponseEntity<Consulta> criar(@Valid @RequestBody CriarConsultaRequestDTO dto){
        Consulta consulta = service.criarConsulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
    }

    // REGRA POR ROLE:
    // ENFERMEIRO - TODAS
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    @GetMapping
    public ResponseEntity<List<Consulta>> listarConsultas(){
        List<Consulta> consultas = service.listar();

        return ResponseEntity.ok(consultas);
    }

    // PACIENTE - APENAS AS PRÓPRIAS
    @PreAuthorize("hasAnyRole('PACIENTE')")
    @GetMapping("/minhas")
    public ResponseEntity<List<Consulta>> listarConsultasDoPaciente(Authentication authentication) {

        List<Consulta> consultas = service.listarConsultasDoPaciente(authentication);

        return ResponseEntity.ok(consultas);
    }

    // APENAS MÉDICOS PODEM ALTERAR
    @PreAuthorize("hasAnyRole('MEDICO')")
    @PutMapping("/{id}")
    public ResponseEntity<Consulta> editarConsulta(@RequestBody @Valid AtualizarConsultaRequestDTO dto,
                                                   @RequestParam Long id) {
        Consulta consulta = service.atualizarConsulta(dto, id);

        return ResponseEntity.ok(consulta);
    }

    // PATCH /consultas/{id}/cancelar
    @PatchMapping("/{id}/cancelar")
    public Consulta cancelarConsulta(@RequestParam Long id) {
        return null;
    }

    // HISTÓRICO
    // GET /historico/paciente/{id}
    // TODO: IMPLEMENTAR ENDPOINT DE HISTÓRICO
}

package com.caroba.fiap.hospital.agendamento_service.controller;

import com.caroba.fiap.hospital.agendamento_service.dto.request.AtualizarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.response.ConsultaResponseDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.request.CriarConsultaRequestDTO;
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
    public ResponseEntity<ConsultaResponseDTO> criar(@Valid @RequestBody CriarConsultaRequestDTO dto){
        ConsultaResponseDTO consultaResponseDTO = service.criarConsulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaResponseDTO);
    }

    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listarConsultas(){
        List<ConsultaResponseDTO> consultas = service.listar();

        return ResponseEntity.ok(consultas);
    }

    // PACIENTE - APENAS AS PRÓPRIAS
    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/minhas")
    public ResponseEntity<List<ConsultaResponseDTO>> listarConsultasDoPaciente(Authentication authentication) {

        List<ConsultaResponseDTO> consultas = service.listarConsultasDoPaciente(authentication);

        return ResponseEntity.ok(consultas);
    }

    // APENAS MÉDICOS PODEM ALTERAR
    @PreAuthorize("hasAnyRole('MEDICO')")
    @PatchMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> editarConsulta(@RequestBody @Valid AtualizarConsultaRequestDTO dto,
                                                   @PathVariable Long id) {
        ConsultaResponseDTO consulta = service.atualizarConsulta(dto, id);

        return ResponseEntity.ok(consulta);
    }

    // PATCH /consultas/{id}/cancelar
    // TODO: Endpoint de cancelar consulta
    @PatchMapping("/{id}/cancelar")
    public Consulta cancelarConsulta(@RequestParam Long id) {
        return null;
    }

    // HISTÓRICO
    // GET /historico/paciente/{id}
    // TODO: IMPLEMENTAR ENDPOINT DE HISTÓRICO
}

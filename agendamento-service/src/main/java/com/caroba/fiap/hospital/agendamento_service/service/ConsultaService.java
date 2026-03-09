package com.caroba.fiap.hospital.agendamento_service.service;

import com.caroba.fiap.hospital.agendamento_service.dto.AtualizarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.CriarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.model.Consulta;
import com.caroba.fiap.hospital.agendamento_service.model.Role;
import com.caroba.fiap.hospital.agendamento_service.model.StatusConsulta;
import com.caroba.fiap.hospital.agendamento_service.model.Usuario;
import com.caroba.fiap.hospital.agendamento_service.repository.ConsultaRepository;
import com.caroba.fiap.hospital.agendamento_service.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public ConsultaService(ConsultaRepository consultaRepository, UsuarioRepository usuarioRepository) {
        this.repository = consultaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Consulta criarConsulta(CriarConsultaRequestDTO dto){
        Usuario paciente = usuarioRepository.findById((dto.pacienteId()))
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Usuario medico = usuarioRepository.findById(dto.medicoId())
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        if (paciente.getRole() != Role.PACIENTE) {
            throw new RuntimeException("Usuário informado não é um Paciente.");
        }
        if (medico.getRole() != Role.MEDICO) {
            throw new RuntimeException("Usuário informado não é um Médico.");
        }
        if (repository.existsByMedicoAndDataConsulta(medico, dto.dataConsulta())) {
            throw new RuntimeException("Médico já possui consulta nessa data e horário");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataConsulta(dto.dataConsulta());
        consulta.setDescricao(dto.descricao());
        consulta.setStatus(StatusConsulta.AGENDADA);

        return repository.save(consulta);
    }

    public List<Consulta> listar(){
        return repository.findAll();
    }

    public List<Consulta> listarConsultasDoPaciente(Authentication authentication) {

        String email = authentication.getName();
        Usuario paciente = usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        return repository.findAllByPaciente(paciente);
    }

    public Consulta atualizarConsulta(@Valid AtualizarConsultaRequestDTO dto, Long id) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        if (dto.dataConsulta() != null && !dto.dataConsulta().equals(consulta.getDataConsulta())) {
            if (repository.existsByMedicoAndDataConsulta(
                    consulta.getMedico(), dto.dataConsulta()))
            {
                throw new RuntimeException("Médico já possui consulta agendada nesse horário");
            }
            consulta.setDataConsulta(dto.dataConsulta());
        }

        if (dto.descricao() != null) {
            consulta.setDescricao(dto.descricao());
        }

        if (dto.status() != null) {
            consulta.setStatus(dto.status());
        }

        return repository.save(consulta);
    }

    // TODO: definir uma regra para consultas que forem canceladas
}

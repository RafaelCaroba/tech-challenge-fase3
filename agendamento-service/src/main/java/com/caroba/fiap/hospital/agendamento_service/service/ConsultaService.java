package com.caroba.fiap.hospital.agendamento_service.service;

import com.caroba.fiap.hospital.agendamento_service.dto.event.ConsultaEvent;
import com.caroba.fiap.hospital.agendamento_service.dto.request.AtualizarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.response.ConsultaResponseDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.request.CriarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.exception.BusinessException;
import com.caroba.fiap.hospital.agendamento_service.exception.ResourceNotFoundException;
import com.caroba.fiap.hospital.agendamento_service.model.*;
import com.caroba.fiap.hospital.agendamento_service.producer.ConsultaEventProducer;
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
    private final ConsultaEventProducer consultaEventProducer;

    public ConsultaService(ConsultaRepository consultaRepository, UsuarioRepository usuarioRepository, ConsultaEventProducer consultaEventProducer) {
        this.repository = consultaRepository;
        this.usuarioRepository = usuarioRepository;
        this.consultaEventProducer = consultaEventProducer;
    }

    @Transactional
    public ConsultaResponseDTO criarConsulta(@Valid CriarConsultaRequestDTO dto){
        Usuario paciente = usuarioRepository.findById((dto.pacienteId()))
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        Usuario medico = usuarioRepository.findById(dto.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));

        if (paciente.getRole() != Role.PACIENTE) {
            throw new BusinessException("Usuário informado não é um Paciente.");
        }
        if (medico.getRole() != Role.MEDICO) {
            throw new BusinessException("Usuário informado não é um Médico.");
        }
        if (repository.existsByMedicoAndDataConsulta(medico, dto.dataConsulta())) {
            throw new BusinessException("Médico já possui consulta nessa data e horário");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataConsulta(dto.dataConsulta());
        consulta.setDescricao(dto.descricao());
        consulta.setStatus(StatusConsulta.AGENDADA);

        consulta = repository.save(consulta);

        ConsultaEvent event = new ConsultaEvent(
                EventType.CONSULTA_CRIADA,
                consulta.getId(),
                consulta.getPaciente().getId(),
                consulta.getMedico().getId(),
                consulta.getDataConsulta(),
                consulta.getStatus().name()
        );

        consultaEventProducer.enviarEvento(event);

        ConsultaResponseDTO responseDTO = toResponseDTO(consulta);

        return responseDTO;
    }

    public List<ConsultaResponseDTO> listar(){
        return repository
                .findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ConsultaResponseDTO> listarConsultasDoPaciente(Authentication authentication) {

        String email = authentication.getName();
        Usuario paciente = usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        return repository.findAllByPaciente(paciente)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public ConsultaResponseDTO atualizarConsulta(AtualizarConsultaRequestDTO dto, Long id) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        if (dto.dataConsulta() != null && !dto.dataConsulta().equals(consulta.getDataConsulta())) {
            if (repository.existsByMedicoAndDataConsulta(
                    consulta.getMedico(), dto.dataConsulta()))
            {
                throw new BusinessException("Médico já possui consulta agendada nesse horário");
            }
            consulta.setDataConsulta(dto.dataConsulta());
        }

        if (dto.descricao() != null) {
            consulta.setDescricao(dto.descricao());
        }

        if (dto.status() != null) {
            consulta.setStatus(dto.status());
        }

        consulta = repository.save(consulta);

        EventType tipo = consulta.getStatus() == StatusConsulta.CANCELADA
               ? EventType.CONSULTA_CANCELADA
                : EventType.CONSULTA_ATUALIZADA;

        ConsultaEvent event = new ConsultaEvent(
                tipo,
                consulta.getId(),
                consulta.getPaciente().getId(),
                consulta.getMedico().getId(),
                consulta.getDataConsulta(),
                consulta.getStatus().name()
        );

        consultaEventProducer.enviarEvento(event);

        return toResponseDTO(consulta);
    }

    private ConsultaResponseDTO toResponseDTO(Consulta consulta) {
        return new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getPaciente().getNome(),
                consulta.getMedico().getNome(),
                consulta.getDataConsulta(),
                consulta.getDescricao(),
                consulta.getStatus()
        );
    }
}

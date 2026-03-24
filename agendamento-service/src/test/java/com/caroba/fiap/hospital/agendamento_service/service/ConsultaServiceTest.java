package com.caroba.fiap.hospital.agendamento_service.service;

import com.caroba.fiap.hospital.agendamento_service.dto.request.AtualizarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.request.CriarConsultaRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.response.ConsultaResponseDTO;
import com.caroba.fiap.hospital.agendamento_service.exception.BusinessException;
import com.caroba.fiap.hospital.agendamento_service.model.Consulta;
import com.caroba.fiap.hospital.agendamento_service.model.Role;
import com.caroba.fiap.hospital.agendamento_service.model.StatusConsulta;
import com.caroba.fiap.hospital.agendamento_service.model.Usuario;
import com.caroba.fiap.hospital.agendamento_service.producer.ConsultaEventProducer;
import com.caroba.fiap.hospital.agendamento_service.repository.ConsultaRepository;
import com.caroba.fiap.hospital.agendamento_service.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @InjectMocks
    private ConsultaService service;

    @Mock
    private ConsultaRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ConsultaEventProducer eventProducer;

    private Usuario paciente;
    private Usuario medico;

    @BeforeEach
    void setup() {
        paciente = new Usuario();
        paciente.setId(1L);
        paciente.setRole(Role.PACIENTE);
        paciente.setNome("Paciente");

        medico = new Usuario();
        medico.setId(2L);
        medico.setRole(Role.MEDICO);
        medico.setNome("Medico");
    }

    @Test
    void deveCriarConsultaComSucesso() {

        CriarConsultaRequestDTO dto = new CriarConsultaRequestDTO(
                1L,
                2L,
                LocalDateTime.now().plusDays(1),
                "Consulta teste"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(repository.existsByMedicoAndDataConsulta(any(), any())).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> {
            Consulta c = invocation.getArgument(0);
            c.setId(10L);
            return c;
        });

        ConsultaResponseDTO response = service.criarConsulta(dto);

        assertNotNull(response);
        assertEquals(StatusConsulta.AGENDADA, response.status());

        verify(eventProducer, times(1)).enviarEvento(any());
    }

    @Test
    void naoDeveCriarConsultaSePacienteInvalido() {

        paciente.setRole(Role.MEDICO);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(medico));

        CriarConsultaRequestDTO dto = new CriarConsultaRequestDTO(
                1L, 2L, LocalDateTime.now().plusDays(1), "Teste"
        );

        assertThrows(BusinessException.class, () -> service.criarConsulta(dto));
    }

    @Test
    void naoDeveCriarConsultaSeHorarioOcupado() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(repository.existsByMedicoAndDataConsulta(any(), any())).thenReturn(true);

        CriarConsultaRequestDTO dto = new CriarConsultaRequestDTO(
                1L, 2L, LocalDateTime.now().plusDays(1), "Teste"
        );

        assertThrows(BusinessException.class, () -> service.criarConsulta(dto));
    }

    @Test
    void deveAtualizarConsulta() {

        Consulta consulta = new Consulta();
        consulta.setId(1L);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataConsulta(LocalDateTime.now().plusDays(1));
        consulta.setStatus(StatusConsulta.AGENDADA);

        when(repository.findById(1L)).thenReturn(Optional.of(consulta));
        when(repository.existsByMedicoAndDataConsulta(any(), any())).thenReturn(false);
        when(repository.save(any())).thenReturn(consulta);

        AtualizarConsultaRequestDTO dto = new AtualizarConsultaRequestDTO(
                LocalDateTime.now().plusDays(2),
                "Atualizada",
                StatusConsulta.REALIZADA
        );

        ConsultaResponseDTO response = service.atualizarConsulta(dto, 1L);

        assertNotNull(response);
        assertEquals(StatusConsulta.REALIZADA, response.status());

        verify(eventProducer, times(1)).enviarEvento(any());
    }
}
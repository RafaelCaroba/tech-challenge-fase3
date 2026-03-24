package com.caroba.fiap.hospital.agendamento_service.service;

import com.caroba.fiap.hospital.agendamento_service.dto.request.CriarUsuarioRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.response.UsuarioResponseDTO;
import com.caroba.fiap.hospital.agendamento_service.exception.ResourceNotFoundException;
import com.caroba.fiap.hospital.agendamento_service.model.Role;
import com.caroba.fiap.hospital.agendamento_service.model.Usuario;
import com.caroba.fiap.hospital.agendamento_service.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveCriarUsuarioComSucesso() {

        CriarUsuarioRequestDTO dto = new CriarUsuarioRequestDTO(
                "Rafael",
                "rafael@email.com",
                "123456",
                Role.PACIENTE
        );

        when(passwordEncoder.encode("123456")).thenReturn("senha-criptografada");
        when(repository.save(any())).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponseDTO response = service.criarUsuario(dto);

        assertNotNull(response);
        assertEquals("Rafael", response.nome());
        assertEquals(Role.PACIENTE, response.role());
    }

    @Test
    void deveBuscarUsuarioPorId() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setRole(Role.MEDICO);
        usuario.setAtivo(true);

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = service.buscarPorId(1L);

        assertNotNull(response);
        assertEquals("Teste", response.nome());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(1L));
    }
}
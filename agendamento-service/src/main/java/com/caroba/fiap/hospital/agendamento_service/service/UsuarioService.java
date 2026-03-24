package com.caroba.fiap.hospital.agendamento_service.service;

import com.caroba.fiap.hospital.agendamento_service.dto.request.CriarUsuarioRequestDTO;
import com.caroba.fiap.hospital.agendamento_service.dto.response.UsuarioResponseDTO;
import com.caroba.fiap.hospital.agendamento_service.exception.ResourceNotFoundException;
import com.caroba.fiap.hospital.agendamento_service.model.Usuario;
import com.caroba.fiap.hospital.agendamento_service.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO criarUsuario(CriarUsuarioRequestDTO dto){
        log.info("Criando usuário - nome={}, email={}, role={}",
                dto.nome(), dto.email(), dto.role().name());

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setRole(dto.role());
        usuario.setAtivo(true);

        usuario = repository.save(usuario);

        log.info("Usuário criado com sucesso - ID={}", usuario.getId());

        return toResponseDTO(usuario);
    }

    public List<UsuarioResponseDTO> listarUsuarios(){
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id){
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return toResponseDTO(usuario);
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getAtivo()
        );
    }
}

package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.AdministradorDTO;
import br.com.teamxp.futscore_api.model.Administrador;
import br.com.teamxp.futscore_api.model.Usuario;
import br.com.teamxp.futscore_api.repository.AdministradorRepository;
import br.com.teamxp.futscore_api.repository.UsuarioRepository;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;

    public AdministradorService(
            AdministradorRepository administradorRepository,
            UsuarioRepository usuarioRepository) {

        this.administradorRepository = administradorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Administrador> listarTodos() {
        return administradorRepository.findAll();
    }

    public Administrador buscarPorId(Long id) {
        return administradorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Administrador não encontrado"));
    }

    public Administrador salvar(AdministradorDTO dto) {

        Usuario usuario = usuarioRepository
                .findById(dto.getUsuarioId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"));

        if (administradorRepository
                .existsByUsuarioId(dto.getUsuarioId())) {

            throw new RuntimeException(
                    "Este usuário já está cadastrado como administrador");
        }

        Administrador administrador = new Administrador();

        administrador.setPerfil(dto.getPerfil());
        administrador.setUsuario(usuario);

        return administradorRepository.save(administrador);
    }

    public Administrador atualizar(
            Long id,
            AdministradorDTO dto) {

        Administrador administrador = buscarPorId(id);

        Usuario usuario = usuarioRepository
                .findById(dto.getUsuarioId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"));

        if (administradorRepository
                .existsByUsuarioIdAndIdNot(
                        dto.getUsuarioId(), id)) {

            throw new RuntimeException(
                    "Este usuário já está vinculado a outro administrador");
        }

        administrador.setPerfil(dto.getPerfil());
        administrador.setUsuario(usuario);

        return administradorRepository.save(administrador);
    }

    public void excluir(Long id) {
        Administrador administrador = buscarPorId(id);
        administradorRepository.delete(administrador);
    }
}
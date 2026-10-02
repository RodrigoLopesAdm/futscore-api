package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.UsuarioDTO;
import br.com.teamxp.futscore_api.model.Usuario;
import br.com.teamxp.futscore_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"));
    }

    public Usuario salvar(UsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException(
                    "Já existe um usuário cadastrado com este e-mail");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {

        Usuario usuario = buscarPorId(id);

        if (usuarioRepository.existsByEmailAndIdNot(
                dto.getEmail(), id)) {

            throw new RuntimeException(
                    "Já existe outro usuário cadastrado com este e-mail");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}

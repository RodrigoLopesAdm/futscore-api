package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.Usuario;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(
            String email,
            Long id);
}

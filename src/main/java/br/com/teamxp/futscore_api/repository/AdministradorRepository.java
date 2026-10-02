package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.Administrador;

public interface AdministradorRepository
        extends JpaRepository<Administrador, Long> {

    boolean existsByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndIdNot(
            Long usuarioId,
            Long id);
}

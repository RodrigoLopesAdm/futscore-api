package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.EquipePartida;

public interface EquipePartidaRepository
        extends JpaRepository<EquipePartida, Long> {

    boolean existsByEquipeIdAndPartidaId(
            Long equipeId,
            Long partidaId);
}
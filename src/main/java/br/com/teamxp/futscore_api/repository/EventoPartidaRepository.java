package br.com.teamxp.futscore_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.EventoPartida;

public interface EventoPartidaRepository
        extends JpaRepository<EventoPartida, Long> {

    List<EventoPartida> findByPartidaId(Long partidaId);

    List<EventoPartida> findByPartidaCampeonatoIdAndTipoIgnoreCase(
            Long campeonatoId,
            String tipo);
}

package br.com.teamxp.futscore_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.Partida;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    // Lista as partidas de determinado campeonato
    List<Partida> findByCampeonatoId(Long campeonatoId);

    // Verifica se existem partidas cadastradas no campeonato
    boolean existsByCampeonatoId(Long campeonatoId);
}


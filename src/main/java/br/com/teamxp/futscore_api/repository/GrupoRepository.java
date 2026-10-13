
package br.com.teamxp.futscore_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    List<Grupo> findByCampeonatoId(Long campeonatoId);

    boolean existsByCampeonatoIdAndNomeIgnoreCase(
            Long campeonatoId,
            String nome
    );

    // Verifica se existem grupos vinculados ao campeonato
    boolean existsByCampeonatoId(Long campeonatoId);
}




package br.com.teamxp.futscore_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.CampeonatoEquipe;

public interface CampeonatoEquipeRepository
        extends JpaRepository<CampeonatoEquipe, Long> {

    // Verifica se a equipe já está inscrita no campeonato
    boolean existsByCampeonatoIdAndEquipeId(
            Long campeonatoId,
            Long equipeId);

    // Lista todas as equipes inscritas em um campeonato
    List<CampeonatoEquipe> findByCampeonatoId(
            Long campeonatoId);

    // Lista as equipes de um grupo específico
    List<CampeonatoEquipe> findByCampeonatoIdAndGrupoId(
            Long campeonatoId,
            Long grupoId);

    // Verifica se uma equipe pertence a determinado grupo
    boolean existsByCampeonatoIdAndEquipeIdAndGrupoId(
            Long campeonatoId,
            Long equipeId,
            Long grupoId);

    // Verifica se o campeonato possui equipes inscritas
    boolean existsByCampeonatoId(Long campeonatoId);

    // Verifica se a equipe está inscrita em algum campeonato
    boolean existsByEquipeId(Long equipeId);        
}


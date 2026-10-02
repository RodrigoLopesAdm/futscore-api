package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.CampeonatoEquipe;

public interface CampeonatoEquipeRepository
        extends JpaRepository<CampeonatoEquipe, Long> {

    boolean existsByCampeonatoIdAndEquipeId(
            Long campeonatoId,
            Long equipeId);
}

package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.teamxp.futscore_api.model.Equipe;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

}
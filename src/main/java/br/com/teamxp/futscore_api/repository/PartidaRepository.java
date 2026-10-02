package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.teamxp.futscore_api.model.Partida;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

}

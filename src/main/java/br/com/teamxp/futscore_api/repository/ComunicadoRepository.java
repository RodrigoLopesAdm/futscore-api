package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.teamxp.futscore_api.model.Comunicado;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {

}

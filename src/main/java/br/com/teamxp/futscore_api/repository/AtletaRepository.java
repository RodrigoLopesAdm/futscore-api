
package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.teamxp.futscore_api.model.Atleta;

public interface AtletaRepository extends JpaRepository<Atleta, Long> {

    // Verifica se existem atletas cadastrados em uma equipe
    boolean existsByEquipeId(Long equipeId);

}



package br.com.teamxp.futscore_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.Classificacao;

public interface ClassificacaoRepository
        extends JpaRepository<Classificacao, Long> {

    // Lista os registros de classificação de um campeonato
    List<Classificacao> findByCampeonatoId(Long campeonatoId);

    // Verifica se existem registros de classificação no campeonato
    boolean existsByCampeonatoId(Long campeonatoId);
}

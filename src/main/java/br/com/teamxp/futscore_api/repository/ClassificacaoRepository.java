package br.com.teamxp.futscore_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teamxp.futscore_api.model.Classificacao;

public interface ClassificacaoRepository
        extends JpaRepository<Classificacao, Long> {

    List<Classificacao> findByCampeonatoId(Long campeonatoId);
}
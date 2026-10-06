package br.com.teamxp.futscore_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.teamxp.futscore_api.model.Classificacao;
import br.com.teamxp.futscore_api.service.ClassificacaoService;

@RestController
@RequestMapping("/api/classificacao")
public class ClassificacaoController {

    private final ClassificacaoService classificacaoService;

    public ClassificacaoController(
            ClassificacaoService classificacaoService) {

        this.classificacaoService = classificacaoService;
    }

    @PostMapping("/campeonato/{campeonatoId}/gerar")
    public List<Classificacao> gerarClassificacao(
            @PathVariable Long campeonatoId) {

        return classificacaoService.gerarClassificacao(campeonatoId);
    }

    @GetMapping("/campeonato/{campeonatoId}")
    public List<Classificacao> buscarPorCampeonato(
            @PathVariable Long campeonatoId) {

        return classificacaoService.buscarPorCampeonato(campeonatoId);
    }
}
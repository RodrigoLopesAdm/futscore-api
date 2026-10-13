package br.com.teamxp.futscore_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import br.com.teamxp.futscore_api.model.Classificacao;
import br.com.teamxp.futscore_api.service.ClassificacaoService;

@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
@RestController
@RequestMapping("/api/classificacao")
public class ClassificacaoController {

    private final ClassificacaoService classificacaoService;

    public ClassificacaoController(
            ClassificacaoService classificacaoService) {

        this.classificacaoService = classificacaoService;
    }

    // Gera a classificação geral do campeonato
    @PostMapping("/campeonato/{campeonatoId}/gerar")
    public List<Classificacao> gerarClassificacao(
            @PathVariable Long campeonatoId) {

        return classificacaoService.gerarClassificacao(campeonatoId);
    }

    // Consulta a classificação geral do campeonato
    @GetMapping("/campeonato/{campeonatoId}")
    public List<Classificacao> buscarPorCampeonato(
            @PathVariable Long campeonatoId) {

        return classificacaoService.buscarPorCampeonato(campeonatoId);
    }

    // Consulta a classificação de um grupo específico
    @GetMapping("/campeonato/{campeonatoId}/grupo/{grupoId}")
    public List<Classificacao> buscarClassificacaoPorGrupo(
            @PathVariable Long campeonatoId,
            @PathVariable Long grupoId) {

        return classificacaoService.buscarClassificacaoPorGrupo(
                campeonatoId,
                grupoId);
    }
}

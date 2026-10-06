package br.com.teamxp.futscore_api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.model.Classificacao;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.model.Partida;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.ClassificacaoRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;

@Service
public class ClassificacaoService {

    private final ClassificacaoRepository classificacaoRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final PartidaRepository partidaRepository;

    public ClassificacaoService(
            ClassificacaoRepository classificacaoRepository,
            CampeonatoRepository campeonatoRepository,
            PartidaRepository partidaRepository) {

        this.classificacaoRepository = classificacaoRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.partidaRepository = partidaRepository;
    }

    public List<Classificacao> gerarClassificacao(Long campeonatoId) {

        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() ->
                        new RuntimeException("Campeonato não encontrado"));

        List<Partida> partidas =
                partidaRepository.findByCampeonatoId(campeonatoId);

        Map<Long, Classificacao> classificacoes = new LinkedHashMap<>();

        for (Partida partida : partidas) {

            if (!"FINALIZADA".equalsIgnoreCase(partida.getStatus())) {
                continue;
            }

            if (partida.getPlacarMandante() == null
                    || partida.getPlacarVisitante() == null) {
                continue;
            }

            Equipe mandante = partida.getMandante();
            Equipe visitante = partida.getVisitante();

            Classificacao classificacaoMandante =
                    classificacoes.computeIfAbsent(
                            mandante.getId(),
                            id -> criarClassificacao(campeonato, mandante));

            Classificacao classificacaoVisitante =
                    classificacoes.computeIfAbsent(
                            visitante.getId(),
                            id -> criarClassificacao(campeonato, visitante));

            atualizarEstatisticas(
                    classificacaoMandante,
                    partida.getPlacarMandante(),
                    partida.getPlacarVisitante());

            atualizarEstatisticas(
                    classificacaoVisitante,
                    partida.getPlacarVisitante(),
                    partida.getPlacarMandante());
        }

        classificacaoRepository.deleteAll(
                classificacaoRepository.findByCampeonatoId(campeonatoId));

        List<Classificacao> resultado =
                new ArrayList<>(classificacoes.values());

        resultado.sort(
                Comparator.comparing(Classificacao::getPontos).reversed()
                        .thenComparing(
                                Classificacao::getSaldoGols,
                                Comparator.reverseOrder())
                        .thenComparing(
                                Classificacao::getGolsPro,
                                Comparator.reverseOrder()));

        return classificacaoRepository.saveAll(resultado);
    }

    public List<Classificacao> buscarPorCampeonato(Long campeonatoId) {

        if (!campeonatoRepository.existsById(campeonatoId)) {
            throw new RuntimeException("Campeonato não encontrado");
        }

        List<Classificacao> classificacao =
                classificacaoRepository.findByCampeonatoId(campeonatoId);

        classificacao.sort(
                Comparator.comparing(Classificacao::getPontos).reversed()
                        .thenComparing(
                                Classificacao::getSaldoGols,
                                Comparator.reverseOrder())
                        .thenComparing(
                                Classificacao::getGolsPro,
                                Comparator.reverseOrder()));

        return classificacao;
    }

    private Classificacao criarClassificacao(
            Campeonato campeonato,
            Equipe equipe) {

        Classificacao classificacao = new Classificacao();

        classificacao.setCampeonato(campeonato);
        classificacao.setEquipe(equipe);
        classificacao.setJogos(0);
        classificacao.setPontos(0);
        classificacao.setVitorias(0);
        classificacao.setEmpates(0);
        classificacao.setDerrotas(0);
        classificacao.setGolsPro(0);
        classificacao.setGolsContra(0);
        classificacao.setSaldoGols(0);

        return classificacao;
    }

    private void atualizarEstatisticas(
            Classificacao classificacao,
            Integer golsPro,
            Integer golsContra) {

        classificacao.setJogos(
                classificacao.getJogos() + 1);

        classificacao.setGolsPro(
                classificacao.getGolsPro() + golsPro);

        classificacao.setGolsContra(
                classificacao.getGolsContra() + golsContra);

        classificacao.setSaldoGols(
                classificacao.getGolsPro()
                        - classificacao.getGolsContra());

        if (golsPro > golsContra) {

            classificacao.setVitorias(
                    classificacao.getVitorias() + 1);

            classificacao.setPontos(
                    classificacao.getPontos() + 3);

        } else if (golsPro.equals(golsContra)) {

            classificacao.setEmpates(
                    classificacao.getEmpates() + 1);

            classificacao.setPontos(
                    classificacao.getPontos() + 1);

        } else {

            classificacao.setDerrotas(
                    classificacao.getDerrotas() + 1);
        }
    }
}

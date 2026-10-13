
package br.com.teamxp.futscore_api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.model.CampeonatoEquipe;
import br.com.teamxp.futscore_api.model.Classificacao;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.model.Grupo;
import br.com.teamxp.futscore_api.model.Partida;

import br.com.teamxp.futscore_api.repository.CampeonatoEquipeRepository;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.ClassificacaoRepository;
import br.com.teamxp.futscore_api.repository.GrupoRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;

@Service
public class ClassificacaoService {

    private final ClassificacaoRepository classificacaoRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final PartidaRepository partidaRepository;
    private final CampeonatoEquipeRepository campeonatoEquipeRepository;
    private final GrupoRepository grupoRepository;

    public ClassificacaoService(
            ClassificacaoRepository classificacaoRepository,
            CampeonatoRepository campeonatoRepository,
            PartidaRepository partidaRepository,
            CampeonatoEquipeRepository campeonatoEquipeRepository,
            GrupoRepository grupoRepository) {

        this.classificacaoRepository = classificacaoRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.partidaRepository = partidaRepository;
        this.campeonatoEquipeRepository = campeonatoEquipeRepository;
        this.grupoRepository = grupoRepository;
    }

    // CLASSIFICAÇÃO GERAL EXISTENTE

    public List<Classificacao> gerarClassificacao(Long campeonatoId) {

        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Campeonato não encontrado"));

        List<Partida> partidas =
                partidaRepository.findByCampeonatoId(campeonatoId);

        Map<Long, Classificacao> classificacoes =
                new LinkedHashMap<>();

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

        ordenarClassificacao(resultado);

        return classificacaoRepository.saveAll(resultado);
    }

    public List<Classificacao> buscarPorCampeonato(Long campeonatoId) {

        if (!campeonatoRepository.existsById(campeonatoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Campeonato não encontrado");
        }

        List<Classificacao> classificacao =
                classificacaoRepository.findByCampeonatoId(campeonatoId);

        ordenarClassificacao(classificacao);

        return classificacao;
    }

    // NOVO: CLASSIFICAÇÃO POR GRUPO

    public List<Classificacao> buscarClassificacaoPorGrupo(
            Long campeonatoId,
            Long grupoId) {

        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Campeonato não encontrado"));

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Grupo não encontrado"));

        if (!grupo.getCampeonato().getId().equals(campeonatoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O grupo não pertence ao campeonato informado");
        }

        // Recupera todas as equipes inscritas no grupo
        List<CampeonatoEquipe> inscricoes =
                campeonatoEquipeRepository.findByCampeonatoIdAndGrupoId(
                        campeonatoId,
                        grupoId);

        Map<Long, Classificacao> classificacoes =
                new LinkedHashMap<>();

        // Inclui também as equipes que ainda não jogaram
        for (CampeonatoEquipe inscricao : inscricoes) {

            Equipe equipe = inscricao.getEquipe();

            classificacoes.putIfAbsent(
                    equipe.getId(),
                    criarClassificacao(campeonato, equipe));
        }

        // Recupera as partidas do campeonato
        List<Partida> partidas =
                partidaRepository.findByCampeonatoId(campeonatoId);

        for (Partida partida : partidas) {

            // Considera apenas partidas da fase de grupos
            if (!"GRUPOS".equalsIgnoreCase(partida.getFase())) {
                continue;
            }

            // Considera apenas partidas do grupo solicitado
            if (partida.getGrupo() == null
                    || !grupoId.equals(partida.getGrupo().getId())) {
                continue;
            }

            // Considera apenas partidas finalizadas
            if (!"FINALIZADA".equalsIgnoreCase(partida.getStatus())) {
                continue;
            }

            if (partida.getPlacarMandante() == null
                    || partida.getPlacarVisitante() == null) {
                continue;
            }

            Classificacao classificacaoMandante =
                    classificacoes.get(partida.getMandante().getId());

            Classificacao classificacaoVisitante =
                    classificacoes.get(partida.getVisitante().getId());

            // Ignora partidas com equipes não inscritas no grupo
            if (classificacaoMandante == null
                    || classificacaoVisitante == null) {
                continue;
            }

            atualizarEstatisticas(
                    classificacaoMandante,
                    partida.getPlacarMandante(),
                    partida.getPlacarVisitante());

            atualizarEstatisticas(
                    classificacaoVisitante,
                    partida.getPlacarVisitante(),
                    partida.getPlacarMandante());
        }

        List<Classificacao> resultado =
                new ArrayList<>(classificacoes.values());

        ordenarClassificacao(resultado);

        // A classificação do grupo é calculada sem alterar o banco
        return resultado;
    }

    // MÉTODOS AUXILIARES

    private void ordenarClassificacao(
            List<Classificacao> classificacao) {

        classificacao.sort(
                Comparator.comparing(Classificacao::getPontos).reversed()
                        .thenComparing(
                                Classificacao::getSaldoGols,
                                Comparator.reverseOrder())
                        .thenComparing(
                                Classificacao::getGolsPro,
                                Comparator.reverseOrder()));
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



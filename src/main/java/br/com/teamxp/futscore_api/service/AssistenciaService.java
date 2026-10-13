package br.com.teamxp.futscore_api.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.teamxp.futscore_api.dto.AssistenciaDTO;
import br.com.teamxp.futscore_api.model.Atleta;
import br.com.teamxp.futscore_api.model.EventoPartida;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.EventoPartidaRepository;

@Service
public class AssistenciaService {

    private final EventoPartidaRepository eventoPartidaRepository;
    private final CampeonatoRepository campeonatoRepository;

    public AssistenciaService(
            EventoPartidaRepository eventoPartidaRepository,
            CampeonatoRepository campeonatoRepository) {

        this.eventoPartidaRepository = eventoPartidaRepository;
        this.campeonatoRepository = campeonatoRepository;
    }

    public List<AssistenciaDTO> gerarAssistencias(Long campeonatoId) {

        if (!campeonatoRepository.existsById(campeonatoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Campeonato não encontrado"
            );
        }

        List<EventoPartida> eventos =
                eventoPartidaRepository
                        .findByPartidaCampeonatoIdAndTipoIgnoreCase(
                                campeonatoId,
                                "ASSISTENCIA"
                        );

        Map<Long, AssistenciaDTO> jogadores =
                new LinkedHashMap<>();

        for (EventoPartida evento : eventos) {

            Atleta atleta = evento.getAtleta();

            AssistenciaDTO jogador =
                    jogadores.get(atleta.getId());

            if (jogador == null) {

                jogador = new AssistenciaDTO(
                        atleta.getId(),
                        atleta.getNome(),
                        atleta.getEquipe().getId(),
                        atleta.getEquipe().getNome(),
                        0L
                );

                jogadores.put(atleta.getId(), jogador);
            }

            jogador.setAssistencias(
                    jogador.getAssistencias() + 1
            );
        }

        List<AssistenciaDTO> resultado =
                new ArrayList<>(jogadores.values());

        resultado.sort(
                (a, b) -> Long.compare(
                        b.getAssistencias(),
                        a.getAssistencias()
                )
        );

        return resultado;
    }
}



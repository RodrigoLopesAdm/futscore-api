package br.com.teamxp.futscore_api.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.ArtilhariaDTO;
import br.com.teamxp.futscore_api.model.Atleta;
import br.com.teamxp.futscore_api.model.EventoPartida;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.EventoPartidaRepository;

@Service
public class ArtilhariaService {

    private final EventoPartidaRepository eventoPartidaRepository;
    private final CampeonatoRepository campeonatoRepository;

    public ArtilhariaService(
            EventoPartidaRepository eventoPartidaRepository,
            CampeonatoRepository campeonatoRepository) {

        this.eventoPartidaRepository = eventoPartidaRepository;
        this.campeonatoRepository = campeonatoRepository;
    }

    public List<ArtilhariaDTO> gerarArtilharia(Long campeonatoId) {

        if (!campeonatoRepository.existsById(campeonatoId)) {
            throw new RuntimeException("Campeonato não encontrado");
        }

        List<EventoPartida> gols =
                eventoPartidaRepository
                        .findByPartidaCampeonatoIdAndTipoIgnoreCase(
                                campeonatoId,
                                "GOL");

        Map<Long, ArtilhariaDTO> artilheiros =
                new LinkedHashMap<>();

        for (EventoPartida evento : gols) {

            Atleta atleta = evento.getAtleta();

            ArtilhariaDTO artilheiro =
                    artilheiros.get(atleta.getId());

            if (artilheiro == null) {

                artilheiro = new ArtilhariaDTO(
                        atleta.getId(),
                        atleta.getNome(),
                        atleta.getEquipe().getId(),
                        atleta.getEquipe().getNome(),
                        0L);

                artilheiros.put(
                        atleta.getId(),
                        artilheiro);
            }

            artilheiro.setGols(
                    artilheiro.getGols() + 1);
        }

        List<ArtilhariaDTO> resultado =
                new ArrayList<>(artilheiros.values());

        resultado.sort(
                (a, b) -> Long.compare(
                        b.getGols(),
                        a.getGols()));

        return resultado;
    }
}
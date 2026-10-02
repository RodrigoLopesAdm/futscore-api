package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.EventoPartidaDTO;
import br.com.teamxp.futscore_api.model.Atleta;
import br.com.teamxp.futscore_api.model.EventoPartida;
import br.com.teamxp.futscore_api.model.Partida;
import br.com.teamxp.futscore_api.repository.AtletaRepository;
import br.com.teamxp.futscore_api.repository.EventoPartidaRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;

@Service
public class EventoPartidaService {

    private final EventoPartidaRepository eventoPartidaRepository;
    private final PartidaRepository partidaRepository;
    private final AtletaRepository atletaRepository;

    public EventoPartidaService(
            EventoPartidaRepository eventoPartidaRepository,
            PartidaRepository partidaRepository,
            AtletaRepository atletaRepository) {

        this.eventoPartidaRepository = eventoPartidaRepository;
        this.partidaRepository = partidaRepository;
        this.atletaRepository = atletaRepository;
    }

    public List<EventoPartida> listarTodos() {
        return eventoPartidaRepository.findAll();
    }

    public EventoPartida buscarPorId(Long id) {
        return eventoPartidaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Evento da partida não encontrado"));
    }

    public EventoPartida salvar(EventoPartidaDTO dto) {

        Partida partida = partidaRepository
                .findById(dto.getPartidaId())
                .orElseThrow(() ->
                        new RuntimeException("Partida não encontrada"));

        Atleta atleta = atletaRepository
                .findById(dto.getAtletaId())
                .orElseThrow(() ->
                        new RuntimeException("Atleta não encontrado"));

        validarEvento(dto, partida, atleta);

        EventoPartida evento = new EventoPartida();

        evento.setTipo(dto.getTipo());
        evento.setMinuto(dto.getMinuto());
        evento.setPartida(partida);
        evento.setAtleta(atleta);

        return eventoPartidaRepository.save(evento);
    }

    public EventoPartida atualizar(
            Long id,
            EventoPartidaDTO dto) {

        EventoPartida evento = buscarPorId(id);

        Partida partida = partidaRepository
                .findById(dto.getPartidaId())
                .orElseThrow(() ->
                        new RuntimeException("Partida não encontrada"));

        Atleta atleta = atletaRepository
                .findById(dto.getAtletaId())
                .orElseThrow(() ->
                        new RuntimeException("Atleta não encontrado"));

        validarEvento(dto, partida, atleta);

        evento.setTipo(dto.getTipo());
        evento.setMinuto(dto.getMinuto());
        evento.setPartida(partida);
        evento.setAtleta(atleta);

        return eventoPartidaRepository.save(evento);
    }

    public void excluir(Long id) {
        EventoPartida evento = buscarPorId(id);
        eventoPartidaRepository.delete(evento);
    }

    private void validarEvento(
            EventoPartidaDTO dto,
            Partida partida,
            Atleta atleta) {

        if (dto.getMinuto() != null && dto.getMinuto() < 0) {
            throw new RuntimeException(
                    "O minuto do evento não pode ser negativo");
        }

        if (atleta.getEquipe() == null) {
            throw new RuntimeException(
                    "O atleta não está associado a uma equipe");
        }

        Long equipeAtletaId = atleta.getEquipe().getId();
        Long mandanteId = partida.getMandante().getId();
        Long visitanteId = partida.getVisitante().getId();

        if (!equipeAtletaId.equals(mandanteId)
                && !equipeAtletaId.equals(visitanteId)) {

            throw new RuntimeException(
                    "O atleta não pertence a uma das equipes da partida");
        }
    }
}
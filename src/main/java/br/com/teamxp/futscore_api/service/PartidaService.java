package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.PartidaDTO;
import br.com.teamxp.futscore_api.dto.ResultadoPartidaDTO;
import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.model.Partida;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.EquipeRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final EquipeRepository equipeRepository;

    public PartidaService(
            PartidaRepository partidaRepository,
            CampeonatoRepository campeonatoRepository,
            EquipeRepository equipeRepository) {

        this.partidaRepository = partidaRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.equipeRepository = equipeRepository;
    }

    public List<Partida> listarTodos() {
        return partidaRepository.findAll();
    }

    public Partida buscarPorId(Long id) {
        return partidaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Partida não encontrada"));
    }

    public Partida salvar(PartidaDTO dto) {

        Campeonato campeonato = campeonatoRepository
                .findById(dto.getCampeonatoId())
                .orElseThrow(() ->
                        new RuntimeException("Campeonato não encontrado"));

        Equipe mandante = equipeRepository
                .findById(dto.getMandanteId())
                .orElseThrow(() ->
                        new RuntimeException("Equipe mandante não encontrada"));

        Equipe visitante = equipeRepository
                .findById(dto.getVisitanteId())
                .orElseThrow(() ->
                        new RuntimeException("Equipe visitante não encontrada"));

        validarPartida(dto, campeonato);

        Partida partida = new Partida();

        partida.setData(dto.getData());
        partida.setHora(dto.getHora());
        partida.setRodada(dto.getRodada());
        partida.setPlacarMandante(dto.getPlacarMandante());
        partida.setPlacarVisitante(dto.getPlacarVisitante());
        partida.setStatus(dto.getStatus());
        partida.setCampeonato(campeonato);
        partida.setMandante(mandante);
        partida.setVisitante(visitante);

        return partidaRepository.save(partida);
    }

    public Partida atualizar(Long id, PartidaDTO dto) {

        Partida partida = buscarPorId(id);

        Campeonato campeonato = campeonatoRepository
                .findById(dto.getCampeonatoId())
                .orElseThrow(() ->
                        new RuntimeException("Campeonato não encontrado"));

        Equipe mandante = equipeRepository
                .findById(dto.getMandanteId())
                .orElseThrow(() ->
                        new RuntimeException("Equipe mandante não encontrada"));

        Equipe visitante = equipeRepository
                .findById(dto.getVisitanteId())
                .orElseThrow(() ->
                        new RuntimeException("Equipe visitante não encontrada"));

        validarPartida(dto, campeonato);

        partida.setData(dto.getData());
        partida.setHora(dto.getHora());
        partida.setRodada(dto.getRodada());
        partida.setPlacarMandante(dto.getPlacarMandante());
        partida.setPlacarVisitante(dto.getPlacarVisitante());
        partida.setStatus(dto.getStatus());
        partida.setCampeonato(campeonato);
        partida.setMandante(mandante);
        partida.setVisitante(visitante);

        return partidaRepository.save(partida);
    }

    public Partida registrarResultado(Long id, ResultadoPartidaDTO dto) {
        Partida partida = buscarPorId(id);

        partida.setPlacarMandante(dto.getPlacarMandante());
        partida.setPlacarVisitante(dto.getPlacarVisitante());
        partida.setStatus(dto.getStatus());

        return partidaRepository.save(partida);
    }

    public void excluir(Long id) {
        Partida partida = buscarPorId(id);
        partidaRepository.delete(partida);
    }

    private void validarPartida(
            PartidaDTO dto,
            Campeonato campeonato) {

        if (dto.getMandanteId().equals(dto.getVisitanteId())) {
            throw new RuntimeException(
                    "A equipe mandante e a visitante não podem ser iguais");
        }

        if (dto.getPlacarMandante() != null
                && dto.getPlacarMandante() < 0) {

            throw new RuntimeException(
                    "O placar da equipe mandante não pode ser negativo");
        }

        if (dto.getPlacarVisitante() != null
                && dto.getPlacarVisitante() < 0) {

            throw new RuntimeException(
                    "O placar da equipe visitante não pode ser negativo");
        }

        if (dto.getData() != null
                && campeonato.getDataInicio() != null
                && campeonato.getDataFim() != null
                && (dto.getData().isBefore(campeonato.getDataInicio())
                || dto.getData().isAfter(campeonato.getDataFim()))) {

            throw new RuntimeException(
                    "A data da partida deve estar dentro do período do campeonato");
        }
    }
}

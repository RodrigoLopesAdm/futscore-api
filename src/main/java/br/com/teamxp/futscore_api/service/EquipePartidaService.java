package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.model.EquipePartida;
import br.com.teamxp.futscore_api.model.Partida;
import br.com.teamxp.futscore_api.repository.EquipePartidaRepository;
import br.com.teamxp.futscore_api.repository.EquipeRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;

@Service
public class EquipePartidaService {

    private final EquipePartidaRepository equipePartidaRepository;
    private final EquipeRepository equipeRepository;
    private final PartidaRepository partidaRepository;

    public EquipePartidaService(
            EquipePartidaRepository equipePartidaRepository,
            EquipeRepository equipeRepository,
            PartidaRepository partidaRepository) {

        this.equipePartidaRepository = equipePartidaRepository;
        this.equipeRepository = equipeRepository;
        this.partidaRepository = partidaRepository;
    }

    public List<EquipePartida> listarTodos() {
        return equipePartidaRepository.findAll();
    }

    public EquipePartida buscarPorId(Long id) {
        return equipePartidaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vínculo entre equipe e partida não encontrado"));
    }

    public EquipePartida salvar(
            Long equipeId,
            Long partidaId) {

        Equipe equipe = equipeRepository
                .findById(equipeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipe não encontrada"));

        Partida partida = partidaRepository
                .findById(partidaId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Partida não encontrada"));

        boolean equipeParticipaDaPartida =
                equipeId.equals(partida.getMandante().getId())
                || equipeId.equals(partida.getVisitante().getId());

        if (!equipeParticipaDaPartida) {
            throw new RuntimeException(
                    "A equipe não participa desta partida");
        }

        boolean vinculoExistente =
                equipePartidaRepository
                        .existsByEquipeIdAndPartidaId(
                                equipeId,
                                partidaId);

        if (vinculoExistente) {
            throw new RuntimeException(
                    "A equipe já está vinculada a esta partida");
        }

        EquipePartida equipePartida =
                new EquipePartida();

        equipePartida.setEquipe(equipe);
        equipePartida.setPartida(partida);

        return equipePartidaRepository.save(equipePartida);
    }

    public void excluir(Long id) {
        EquipePartida equipePartida = buscarPorId(id);
        equipePartidaRepository.delete(equipePartida);
    }
}
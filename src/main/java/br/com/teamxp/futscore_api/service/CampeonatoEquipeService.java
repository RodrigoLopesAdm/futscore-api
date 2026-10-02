package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.model.CampeonatoEquipe;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.repository.CampeonatoEquipeRepository;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.EquipeRepository;

@Service
public class CampeonatoEquipeService {

    private final CampeonatoEquipeRepository campeonatoEquipeRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final EquipeRepository equipeRepository;

    public CampeonatoEquipeService(
            CampeonatoEquipeRepository campeonatoEquipeRepository,
            CampeonatoRepository campeonatoRepository,
            EquipeRepository equipeRepository) {

        this.campeonatoEquipeRepository = campeonatoEquipeRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.equipeRepository = equipeRepository;
    }

    public List<CampeonatoEquipe> listarTodos() {
        return campeonatoEquipeRepository.findAll();
    }

    public CampeonatoEquipe buscarPorId(Long id) {
        return campeonatoEquipeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vínculo entre campeonato e equipe não encontrado"));
    }

    public CampeonatoEquipe salvar(
            Long campeonatoId,
            Long equipeId) {

        Campeonato campeonato = campeonatoRepository
                .findById(campeonatoId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Campeonato não encontrado"));

        Equipe equipe = equipeRepository
                .findById(equipeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipe não encontrada"));

        boolean vinculoExistente =
                campeonatoEquipeRepository
                        .existsByCampeonatoIdAndEquipeId(
                                campeonatoId,
                                equipeId);

        if (vinculoExistente) {
            throw new RuntimeException(
                    "A equipe já está inscrita neste campeonato");
        }

        CampeonatoEquipe campeonatoEquipe =
                new CampeonatoEquipe();

        campeonatoEquipe.setCampeonato(campeonato);
        campeonatoEquipe.setEquipe(equipe);

        return campeonatoEquipeRepository.save(campeonatoEquipe);
    }

    public void excluir(Long id) {
        CampeonatoEquipe campeonatoEquipe = buscarPorId(id);
        campeonatoEquipeRepository.delete(campeonatoEquipe);
    }
}

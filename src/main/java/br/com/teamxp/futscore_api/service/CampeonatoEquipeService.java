package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.model.CampeonatoEquipe;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.model.Grupo;
import br.com.teamxp.futscore_api.repository.CampeonatoEquipeRepository;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.EquipeRepository;
import br.com.teamxp.futscore_api.repository.GrupoRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;
import br.com.teamxp.futscore_api.repository.ClassificacaoRepository;

@Service
public class CampeonatoEquipeService {

    private final CampeonatoEquipeRepository campeonatoEquipeRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final EquipeRepository equipeRepository;
    private final GrupoRepository grupoRepository;
    private final PartidaRepository partidaRepository;
    private final ClassificacaoRepository classificacaoRepository;

    public CampeonatoEquipeService(
            CampeonatoEquipeRepository campeonatoEquipeRepository,
            CampeonatoRepository campeonatoRepository,
            EquipeRepository equipeRepository,
            GrupoRepository grupoRepository,
            PartidaRepository partidaRepository,
            ClassificacaoRepository classificacaoRepository) {

        this.campeonatoEquipeRepository = campeonatoEquipeRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.equipeRepository = equipeRepository;
        this.grupoRepository = grupoRepository;
        this.partidaRepository = partidaRepository;
        this.classificacaoRepository = classificacaoRepository;
    }

    public List<CampeonatoEquipe> listarTodos() {
        return campeonatoEquipeRepository.findAll();
    }

    public CampeonatoEquipe buscarPorId(Long id) {
        return campeonatoEquipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vínculo entre campeonato e equipe não encontrado"));
    }

    // Mantém compatibilidade com o cadastro anterior
    public CampeonatoEquipe salvar(
            Long campeonatoId,
            Long equipeId) {

        return salvar(campeonatoId, equipeId, null);
    }

    // Cadastro com grupo opcional
    public CampeonatoEquipe salvar(
            Long campeonatoId,
            Long equipeId,
            Long grupoId) {

        Campeonato campeonato = campeonatoRepository
                .findById(campeonatoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Campeonato não encontrado"));

        Equipe equipe = equipeRepository
                .findById(equipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipe não encontrada"));

        boolean vinculoExistente = campeonatoEquipeRepository
                .existsByCampeonatoIdAndEquipeId(
                        campeonatoId,
                        equipeId);

        if (vinculoExistente) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A equipe já está inscrita neste campeonato");
        }

        Grupo grupo = null;

        if (grupoId != null) {

            // Impede atribuir grupos a campeonatos
            // que não possuem fase de grupos
            if (!"GRUPOS_ELIMINATORIAS".equals(
                    campeonato.getTipoDisputa())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Este campeonato não utiliza fase de grupos."
                );
            }

            grupo = grupoRepository.findById(grupoId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Grupo não encontrado"));

            if (!grupo.getCampeonato().getId().equals(campeonatoId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "O grupo não pertence ao campeonato informado");
            }
        }

        CampeonatoEquipe campeonatoEquipe = new CampeonatoEquipe();

        campeonatoEquipe.setCampeonato(campeonato);
        campeonatoEquipe.setEquipe(equipe);
        campeonatoEquipe.setGrupo(grupo);

        return campeonatoEquipeRepository.save(campeonatoEquipe);
    }

    public CampeonatoEquipe atualizarGrupo(
            Long vinculoId,
            Long grupoId) {

        CampeonatoEquipe campeonatoEquipe = buscarPorId(vinculoId);

        // Permite retirar a equipe de um grupo
        if (grupoId == null) {
            campeonatoEquipe.setGrupo(null);
            return campeonatoEquipeRepository.save(campeonatoEquipe);
        }

        // Verifica se o campeonato utiliza fase de grupos
        Campeonato campeonato = campeonatoEquipe.getCampeonato();

        if (!"GRUPOS_ELIMINATORIAS".equals(
                campeonato.getTipoDisputa())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Este campeonato não utiliza fase de grupos."
            );
        }

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Grupo não encontrado"));

        Long campeonatoId = campeonatoEquipe
                .getCampeonato()
                .getId();

        // Impede associar a equipe a um grupo de outro campeonato
        if (!grupo.getCampeonato().getId().equals(campeonatoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O grupo não pertence ao campeonato da equipe");
        }

        campeonatoEquipe.setGrupo(grupo);

        return campeonatoEquipeRepository.save(campeonatoEquipe);
    }

    // Exclui a inscrição e sua respectiva classificação
    @Transactional
    public void excluir(Long id) {

        CampeonatoEquipe campeonatoEquipe = buscarPorId(id);

        Long campeonatoId = campeonatoEquipe
                .getCampeonato()
                .getId();

        Long equipeId = campeonatoEquipe
                .getEquipe()
                .getId();

        // Verifica se a equipe possui partidas neste campeonato
        boolean possuiPartidas = partidaRepository
                .existsByCampeonatoIdAndMandanteIdOrCampeonatoIdAndVisitanteId(
                        campeonatoId,
                        equipeId,
                        campeonatoId,
                        equipeId
                );

        if (possuiPartidas) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir a inscrição, pois a equipe possui partidas cadastradas neste campeonato."
            );
        }

        // Exclui os registros de classificação da equipe
        // somente no campeonato correspondente
        classificacaoRepository.deleteByCampeonatoIdAndEquipeId(
                campeonatoId,
                equipeId
        );

        classificacaoRepository.flush();

        // Exclui a inscrição da equipe
        campeonatoEquipeRepository.delete(campeonatoEquipe);
    }
}


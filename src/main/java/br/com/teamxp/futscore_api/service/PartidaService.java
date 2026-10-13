
package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.teamxp.futscore_api.dto.PartidaDTO;
import br.com.teamxp.futscore_api.dto.ResultadoPartidaDTO;
import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.model.Grupo;
import br.com.teamxp.futscore_api.model.Partida;

import br.com.teamxp.futscore_api.repository.CampeonatoEquipeRepository;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.EquipeRepository;
import br.com.teamxp.futscore_api.repository.GrupoRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final EquipeRepository equipeRepository;
    private final GrupoRepository grupoRepository;
    private final CampeonatoEquipeRepository campeonatoEquipeRepository;

    public PartidaService(
            PartidaRepository partidaRepository,
            CampeonatoRepository campeonatoRepository,
            EquipeRepository equipeRepository,
            GrupoRepository grupoRepository,
            CampeonatoEquipeRepository campeonatoEquipeRepository) {

        this.partidaRepository = partidaRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.equipeRepository = equipeRepository;
        this.grupoRepository = grupoRepository;
        this.campeonatoEquipeRepository = campeonatoEquipeRepository;
    }

    public List<Partida> listarTodos() {
        return partidaRepository.findAll();
    }

    public Partida buscarPorId(Long id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Partida não encontrada"));
    }

    public Partida salvar(PartidaDTO dto) {

        Campeonato campeonato = buscarCampeonato(dto.getCampeonatoId());
        Equipe mandante = buscarEquipe(dto.getMandanteId());
        Equipe visitante = buscarEquipe(dto.getVisitanteId());

        validarPartida(dto, campeonato);

        Grupo grupo = validarGrupo(
                dto.getFase(),
                dto.getGrupoId(),
                campeonato.getId(),
                mandante.getId(),
                visitante.getId());

        Partida partida = new Partida();

        partida.setData(dto.getData());
        partida.setHora(dto.getHora());
        partida.setRodada(dto.getRodada());

        partida.setPlacarMandante(0);
        partida.setPlacarVisitante(0);
        partida.setStatus("NAO_INICIADA");

        partida.setCampeonato(campeonato);
        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setFase(normalizarFase(dto.getFase()));
        partida.setGrupo(grupo);

        return partidaRepository.save(partida);
    }

    public Partida atualizar(Long id, PartidaDTO dto) {

        Partida partida = buscarPorId(id);

        Campeonato campeonato = buscarCampeonato(dto.getCampeonatoId());
        Equipe mandante = buscarEquipe(dto.getMandanteId());
        Equipe visitante = buscarEquipe(dto.getVisitanteId());

        validarPartida(dto, campeonato);

        // Preserva a fase atual quando não informada
        String fase = dto.getFase() != null
                ? normalizarFase(dto.getFase())
                : partida.getFase();

        // Preserva o grupo atual quando não informado
        Long grupoId = dto.getGrupoId() != null
                ? dto.getGrupoId()
                : partida.getGrupo() != null
                    ? partida.getGrupo().getId()
                    : null;

        Grupo grupo = validarGrupo(
                fase,
                grupoId,
                campeonato.getId(),
                mandante.getId(),
                visitante.getId());

        partida.setData(dto.getData());
        partida.setHora(dto.getHora());
        partida.setRodada(dto.getRodada());

        if (dto.getPlacarMandante() != null) {
            partida.setPlacarMandante(dto.getPlacarMandante());
        }

        if (dto.getPlacarVisitante() != null) {
            partida.setPlacarVisitante(dto.getPlacarVisitante());
        }

        if (dto.getStatus() != null) {
            partida.setStatus(dto.getStatus());
        }

        partida.setCampeonato(campeonato);
        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setFase(fase);
        partida.setGrupo(grupo);

        return partidaRepository.save(partida);
    }

    public Partida registrarResultado(
            Long id,
            ResultadoPartidaDTO dto) {

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

    private Campeonato buscarCampeonato(Long id) {
        return campeonatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Campeonato não encontrado"));
    }

    private Equipe buscarEquipe(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipe não encontrada"));
    }

    private String normalizarFase(String fase) {
        if (fase == null || fase.isBlank()) {
            return null;
        }

        return fase.trim().toUpperCase();
    }

    private Grupo validarGrupo(
            String faseInformada,
            Long grupoId,
            Long campeonatoId,
            Long mandanteId,
            Long visitanteId) {

        String fase = normalizarFase(faseInformada);

        if (grupoId == null) {
            if ("GRUPOS".equals(fase)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Partidas da fase de grupos precisam informar grupoId");
            }

            return null;
        }

        if (!"GRUPOS".equals(fase)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O grupoId só pode ser informado na fase GRUPOS");
        }

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Grupo não encontrado"));

        if (!grupo.getCampeonato().getId().equals(campeonatoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O grupo não pertence ao campeonato informado");
        }

        // Verifica diretamente no banco se o mandante pertence ao grupo
        boolean mandanteNoGrupo = campeonatoEquipeRepository
                .existsByCampeonatoIdAndEquipeIdAndGrupoId(
                        campeonatoId,
                        mandanteId,
                        grupoId);

        // Verifica diretamente no banco se o visitante pertence ao grupo
        boolean visitanteNoGrupo = campeonatoEquipeRepository
                .existsByCampeonatoIdAndEquipeIdAndGrupoId(
                        campeonatoId,
                        visitanteId,
                        grupoId);

        if (!mandanteNoGrupo || !visitanteNoGrupo) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As duas equipes precisam estar inscritas no grupo informado");
        }

        return grupo;
    }

    private void validarPartida(
            PartidaDTO dto,
            Campeonato campeonato) {

        if (dto.getMandanteId() == null
                || dto.getVisitanteId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As equipes são obrigatórias");
        }

        if (dto.getMandanteId().equals(dto.getVisitanteId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A equipe mandante e a visitante não podem ser iguais");
        }

        if (dto.getPlacarMandante() != null
                && dto.getPlacarMandante() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O placar da equipe mandante não pode ser negativo");
        }

        if (dto.getPlacarVisitante() != null
                && dto.getPlacarVisitante() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O placar da equipe visitante não pode ser negativo");
        }

        if (dto.getData() != null
                && campeonato.getDataInicio() != null
                && campeonato.getDataFim() != null
                && (dto.getData().isBefore(campeonato.getDataInicio())
                || dto.getData().isAfter(campeonato.getDataFim()))) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data da partida deve estar dentro do período do campeonato");
        }
    }
}




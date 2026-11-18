package br.com.teamxp.futscore_api.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import br.com.teamxp.futscore_api.repository.EquipePartidaRepository;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final EquipeRepository equipeRepository;
    private final GrupoRepository grupoRepository;
    private final CampeonatoEquipeRepository campeonatoEquipeRepository;
    private final EquipePartidaRepository equipePartidaRepository;

    public PartidaService(
            PartidaRepository partidaRepository,
            CampeonatoRepository campeonatoRepository,
            EquipeRepository equipeRepository,
            GrupoRepository grupoRepository,
            CampeonatoEquipeRepository campeonatoEquipeRepository,
            EquipePartidaRepository equipePartidaRepository) {

        this.partidaRepository = partidaRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.equipeRepository = equipeRepository;
        this.grupoRepository = grupoRepository;
        this.campeonatoEquipeRepository = campeonatoEquipeRepository;
        this.equipePartidaRepository = equipePartidaRepository;
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

    // =========================================
    // CADASTRAR PARTIDA
    // =========================================

    public Partida salvar(PartidaDTO dto) {

        Campeonato campeonato = buscarCampeonato(dto.getCampeonatoId());
        Equipe mandante = buscarEquipe(dto.getMandanteId());
        Equipe visitante = buscarEquipe(dto.getVisitanteId());

        validarPartida(dto, campeonato);

        String fase = normalizarFase(dto.getFase());

        Integer rodada = validarRodada(fase, dto.getRodada());

        Grupo grupo = validarGrupo(
                fase,
                dto.getGrupoId(),
                campeonato.getId(),
                mandante.getId(),
                visitante.getId());

        Partida partida = new Partida();

        partida.setData(dto.getData());
        partida.setHora(dto.getHora());
        partida.setRodada(rodada);

        partida.setPlacarMandante(0);
        partida.setPlacarVisitante(0);

        partida.setPenaltisMandante(null);
        partida.setPenaltisVisitante(null);

        partida.setStatus("NAO_INICIADA");

        partida.setCampeonato(campeonato);
        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setFase(fase);
        partida.setGrupo(grupo);

        return partidaRepository.save(partida);
    }

    // =========================================
    // ATUALIZAR PARTIDA
    // =========================================

    public Partida atualizar(Long id, PartidaDTO dto) {

        Partida partida = buscarPorId(id);

        Campeonato campeonato = buscarCampeonato(dto.getCampeonatoId());
        Equipe mandante = buscarEquipe(dto.getMandanteId());
        Equipe visitante = buscarEquipe(dto.getVisitanteId());

        validarPartida(dto, campeonato);

        // Preserva a fase atual quando não informada
        String fase = dto.getFase() != null
                ? normalizarFase(dto.getFase())
                : normalizarFase(partida.getFase());

        Integer rodada = validarRodada(fase, dto.getRodada());

        // Preserva o grupo somente na fase de grupos
        Long grupoId = dto.getGrupoId();

        if ("GRUPOS".equals(fase)
                && grupoId == null
                && partida.getGrupo() != null) {

            grupoId = partida.getGrupo().getId();
        }

        Grupo grupo = validarGrupo(
                fase,
                grupoId,
                campeonato.getId(),
                mandante.getId(),
                visitante.getId());

        // Define os placares e o status resultantes da edição
        Integer placarMandante = dto.getPlacarMandante() != null
                ? dto.getPlacarMandante()
                : partida.getPlacarMandante();

        Integer placarVisitante = dto.getPlacarVisitante() != null
                ? dto.getPlacarVisitante()
                : partida.getPlacarVisitante();

        String status = dto.getStatus() != null
                ? dto.getStatus()
                : partida.getStatus();

        Integer penaltisMandante = partida.getPenaltisMandante();
        Integer penaltisVisitante = partida.getPenaltisVisitante();

        // Remove os pênaltis quando não forem mais aplicáveis
        if (!necessitaPenaltis(
                fase,
                status,
                placarMandante,
                placarVisitante)) {

            penaltisMandante = null;
            penaltisVisitante = null;
        }

        validarPenaltis(
                fase,
                status,
                placarMandante,
                placarVisitante,
                penaltisMandante,
                penaltisVisitante);

        partida.setData(dto.getData());
        partida.setHora(dto.getHora());
        partida.setRodada(rodada);

        partida.setPlacarMandante(placarMandante);
        partida.setPlacarVisitante(placarVisitante);

        partida.setPenaltisMandante(penaltisMandante);
        partida.setPenaltisVisitante(penaltisVisitante);

        partida.setStatus(status);

        partida.setCampeonato(campeonato);
        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setFase(fase);
        partida.setGrupo(grupo);

        return partidaRepository.save(partida);
    }

    // =========================================
    // REGISTRAR RESULTADO DA PARTIDA
    // =========================================

    public Partida registrarResultado(
            Long id,
            ResultadoPartidaDTO dto) {

        Partida partida = buscarPorId(id);

        String fase = normalizarFase(partida.getFase());

        String status = dto.getStatus().trim().toUpperCase();

        validarPenaltis(
                fase,
                status,
                dto.getPlacarMandante(),
                dto.getPlacarVisitante(),
                dto.getPenaltisMandante(),
                dto.getPenaltisVisitante());

        partida.setPlacarMandante(dto.getPlacarMandante());
        partida.setPlacarVisitante(dto.getPlacarVisitante());

        partida.setPenaltisMandante(dto.getPenaltisMandante());
        partida.setPenaltisVisitante(dto.getPenaltisVisitante());

        partida.setStatus(status);

        return partidaRepository.save(partida);
    }

    // =========================================
    // EXCLUIR PARTIDA
    // =========================================

    @Transactional
    public void excluir(Long id) {

        Partida partida = buscarPorId(id);

        // Exclui os vínculos entre equipes e partida
        equipePartidaRepository.deleteByPartidaId(id);
        equipePartidaRepository.flush();

        // Exclui a partida
        partidaRepository.delete(partida);
    }

    // =========================================
    // BUSCAR CAMPEONATO
    // =========================================

    private Campeonato buscarCampeonato(Long id) {
        return campeonatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Campeonato não encontrado"));
    }

    // =========================================
    // BUSCAR EQUIPE
    // =========================================

    private Equipe buscarEquipe(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipe não encontrada"));
    }

    // =========================================
    // NORMALIZAR FASE
    // =========================================

    private String normalizarFase(String fase) {

        if (fase == null || fase.isBlank()) {
            return null;
        }

        return fase.trim().toUpperCase();
    }

    // =========================================
    // IDENTIFICAR FASE ELIMINATÓRIA
    // =========================================

    private boolean faseEliminatoria(String fase) {

        return "OITAVAS".equals(fase)
                || "QUARTAS".equals(fase)
                || "SEMIFINAL".equals(fase)
                || "FINAL".equals(fase);
    }

    // =========================================
    // VALIDAR RODADA
    // =========================================

    private Integer validarRodada(
            String fase,
            Integer rodada) {

        // Fases eliminatórias não possuem rodada
        if (faseEliminatoria(fase)) {
            return null;
        }

        // Valida as demais fases permitidas
        if (!"GRUPOS".equals(fase)
                && !"PONTOS_CORRIDOS".equals(fase)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe uma fase válida para a partida");
        }

        // Fases de grupos e pontos corridos exigem rodada
        if (rodada == null || rodada <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A rodada é obrigatória e deve ser maior que zero");
        }

        return rodada;
    }

    // =========================================
    // VERIFICAR NECESSIDADE DE PÊNALTIS
    // =========================================

    private boolean necessitaPenaltis(
            String fase,
            String status,
            Integer placarMandante,
            Integer placarVisitante) {

        return faseEliminatoria(fase)
                && "FINALIZADA".equals(status)
                && placarMandante != null
                && placarVisitante != null
                && Objects.equals(
                        placarMandante,
                        placarVisitante);
    }

    // =========================================
    // VALIDAR DISPUTA DE PÊNALTIS
    // =========================================

    private void validarPenaltis(
            String fase,
            String status,
            Integer placarMandante,
            Integer placarVisitante,
            Integer penaltisMandante,
            Integer penaltisVisitante) {

        boolean possuiPenaltis =
                penaltisMandante != null
                || penaltisVisitante != null;

        boolean obrigatorio = necessitaPenaltis(
                fase,
                status,
                placarMandante,
                placarVisitante);

        // Pênaltis não são permitidos nesta situação
        if (!obrigatorio) {

            if (possuiPenaltis) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Os pênaltis só são permitidos em partidas "
                        + "eliminatórias finalizadas com empate");
            }

            return;
        }

        // Exige os dois placares dos pênaltis
        if (penaltisMandante == null
                || penaltisVisitante == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe os placares da disputa de pênaltis");
        }

        // Impede placares negativos
        if (penaltisMandante < 0
                || penaltisVisitante < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Os placares dos pênaltis não podem ser negativos");
        }

        // A disputa precisa ter um vencedor
        if (Objects.equals(
                penaltisMandante,
                penaltisVisitante)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A disputa de pênaltis não pode terminar empatada");
        }
    }

    // =========================================
    // VALIDAR GRUPO
    // =========================================

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

        // Verifica se o mandante pertence ao grupo
        boolean mandanteNoGrupo = campeonatoEquipeRepository
                .existsByCampeonatoIdAndEquipeIdAndGrupoId(
                        campeonatoId,
                        mandanteId,
                        grupoId);

        // Verifica se o visitante pertence ao grupo
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

    // =========================================
    // VALIDAR DADOS DA PARTIDA
    // =========================================

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
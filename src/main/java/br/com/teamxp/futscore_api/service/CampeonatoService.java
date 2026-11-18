
package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.teamxp.futscore_api.dto.CampeonatoDTO;
import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.CampeonatoEquipeRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;
import br.com.teamxp.futscore_api.repository.ClassificacaoRepository;
import br.com.teamxp.futscore_api.repository.GrupoRepository;

@Service
public class CampeonatoService {

    private final CampeonatoRepository campeonatoRepository;
    private final CampeonatoEquipeRepository campeonatoEquipeRepository;
    private final PartidaRepository partidaRepository;
    private final ClassificacaoRepository classificacaoRepository;
    private final GrupoRepository grupoRepository;

    public CampeonatoService(
            CampeonatoRepository campeonatoRepository,
            CampeonatoEquipeRepository campeonatoEquipeRepository,
            PartidaRepository partidaRepository,
            ClassificacaoRepository classificacaoRepository,
            GrupoRepository grupoRepository) {

        this.campeonatoRepository = campeonatoRepository;
        this.campeonatoEquipeRepository = campeonatoEquipeRepository;
        this.partidaRepository = partidaRepository;
        this.classificacaoRepository = classificacaoRepository;
        this.grupoRepository = grupoRepository;
    }

    public List<Campeonato> listarTodos() {
        return campeonatoRepository.findAll();
    }

    public Campeonato buscarPorId(Long id) {
        return campeonatoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Campeonato não encontrado"
                        ));
    }

    // Validação das configurações da fase de grupos
    private void validarConfiguracaoGrupos(CampeonatoDTO dto) {

        if (!"GRUPOS_ELIMINATORIAS".equals(dto.getTipoDisputa())) {
            return;
        }

        Integer grupos = dto.getQuantidadeGrupos();
        Integer classificados = dto.getClassificadosPorGrupo();
        Integer vagas = dto.getVagasAdicionais();

        if (grupos == null || grupos < 1
                || classificados == null || classificados < 1
                || vagas == null || vagas < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe quantidades válidas de grupos, classificados e vagas adicionais."
            );
        }

        long total = (long) grupos * classificados + vagas;

        String fase = dto.getPrimeiraFaseEliminatoria();

        int equipesNecessarias;

        if ("OITAVAS".equals(fase)) {
            equipesNecessarias = 16;
        } else if ("QUARTAS".equals(fase)) {
            equipesNecessarias = 8;
        } else if ("SEMIFINAIS".equals(fase)) {
            equipesNecessarias = 4;
        } else if ("FINAL".equals(fase)) {
            equipesNecessarias = 2;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione uma primeira fase eliminatória válida."
            );
        }

        if (total != equipesNecessarias) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A fase selecionada exige " + equipesNecessarias
                    + " equipes, mas a configuração possui "
                    + total + " classificados."
            );
        }
    }
    
    // Verifica se a quantidade de classificados é compatível
    // com as equipes já inscritas no campeonato
    private void validarQuantidadeEquipes(
            Long campeonatoId,
            CampeonatoDTO dto) {

        if (!"GRUPOS_ELIMINATORIAS".equals(dto.getTipoDisputa())) {
            return;
        }

        long totalInscritas =
                campeonatoEquipeRepository.countByCampeonatoId(
                        campeonatoId);

        // Permite configurar campeonatos sem inscrições
        if (totalInscritas == 0) {
            return;
        }

        long totalClassificados =
                (long) dto.getQuantidadeGrupos()
                * dto.getClassificadosPorGrupo()
                + dto.getVagasAdicionais();

        if (totalClassificados > totalInscritas) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A configuração prevê "
                    + totalClassificados
                    + " classificados, mas o campeonato possui apenas "
                    + totalInscritas
                    + " equipes inscritas."
            );
        }
    }

    public Campeonato salvar(CampeonatoDTO dto) {

        validarDatas(dto);

        String tipoDisputa = dto.getTipoDisputa();

        if (tipoDisputa == null || tipoDisputa.isBlank()) {
            tipoDisputa = "GRUPOS_ELIMINATORIAS";
        }

        if (!tipoDisputa.equals("GRUPOS_ELIMINATORIAS")
                && !tipoDisputa.equals("PONTOS_CORRIDOS")
                && !tipoDisputa.equals("ELIMINATORIAS")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de disputa inválido."
            );
        }

        // Valida as configurações antes de salvar
        dto.setTipoDisputa(tipoDisputa);
        validarConfiguracaoGrupos(dto);

        Campeonato campeonato = new Campeonato();

        // Dados básicos
        campeonato.setNome(dto.getNome());
        campeonato.setAno(dto.getAno());
        campeonato.setRegulamento(dto.getRegulamento());
        campeonato.setDataInicio(dto.getDataInicio());
        campeonato.setDataFim(dto.getDataFim());

        // Regras de pontuação
        campeonato.setPontosVitoria(
                dto.getPontosVitoria() != null
                        ? dto.getPontosVitoria() : 3);

        campeonato.setPontosEmpate(
                dto.getPontosEmpate() != null
                        ? dto.getPontosEmpate() : 1);

        campeonato.setPontosDerrota(
                dto.getPontosDerrota() != null
                        ? dto.getPontosDerrota() : 0);

        // Formato de disputa
        campeonato.setTipoDisputa(tipoDisputa);

        if ("GRUPOS_ELIMINATORIAS".equals(tipoDisputa)) {

            campeonato.setQuantidadeGrupos(dto.getQuantidadeGrupos());
            campeonato.setClassificadosPorGrupo(dto.getClassificadosPorGrupo());
            campeonato.setVagasAdicionais(dto.getVagasAdicionais());
            campeonato.setPrimeiraFaseEliminatoria(
                    dto.getPrimeiraFaseEliminatoria());

        } else if ("ELIMINATORIAS".equals(tipoDisputa)) {

            validarFaseEliminatoria(dto.getPrimeiraFaseEliminatoria());

            campeonato.setQuantidadeGrupos(null);
            campeonato.setClassificadosPorGrupo(null);
            campeonato.setVagasAdicionais(0);
            campeonato.setPrimeiraFaseEliminatoria(
                    dto.getPrimeiraFaseEliminatoria());

        } else {

            campeonato.setQuantidadeGrupos(null);
            campeonato.setClassificadosPorGrupo(null);
            campeonato.setVagasAdicionais(0);
            campeonato.setPrimeiraFaseEliminatoria(null);
        }

        return campeonatoRepository.save(campeonato);
    }

    public Campeonato atualizar(Long id, CampeonatoDTO dto) {

        validarDatas(dto);

        Campeonato campeonato = buscarPorId(id);

        // Validação do tipo de disputa
        String tipoDisputa = dto.getTipoDisputa();

        if (tipoDisputa == null) {
            tipoDisputa = campeonato.getTipoDisputa();
        }

        if (!"GRUPOS_ELIMINATORIAS".equals(tipoDisputa)
                && !"PONTOS_CORRIDOS".equals(tipoDisputa)
                && !"ELIMINATORIAS".equals(tipoDisputa)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de disputa inválido."
            );
        }

        // Valida as configurações antes de alterar
        dto.setTipoDisputa(tipoDisputa);
        validarConfiguracaoGrupos(dto);

        if ("ELIMINATORIAS".equals(tipoDisputa)) {
            validarFaseEliminatoria(dto.getPrimeiraFaseEliminatoria());
        }

        // Dados básicos
        campeonato.setNome(dto.getNome());
        campeonato.setAno(dto.getAno());
        campeonato.setRegulamento(dto.getRegulamento());
        campeonato.setDataInicio(dto.getDataInicio());
        campeonato.setDataFim(dto.getDataFim());

        // Regras de pontuação
        if (dto.getPontosVitoria() != null) {
            campeonato.setPontosVitoria(dto.getPontosVitoria());
        }

        if (dto.getPontosEmpate() != null) {
            campeonato.setPontosEmpate(dto.getPontosEmpate());
        }

        if (dto.getPontosDerrota() != null) {
            campeonato.setPontosDerrota(dto.getPontosDerrota());
        }

        campeonato.setTipoDisputa(tipoDisputa);

        // Configurações conforme o tipo de disputa
        if ("GRUPOS_ELIMINATORIAS".equals(tipoDisputa)) {

            campeonato.setQuantidadeGrupos(dto.getQuantidadeGrupos());
            campeonato.setClassificadosPorGrupo(dto.getClassificadosPorGrupo());
            campeonato.setVagasAdicionais(dto.getVagasAdicionais());
            campeonato.setPrimeiraFaseEliminatoria(
                    dto.getPrimeiraFaseEliminatoria());

        } else if ("ELIMINATORIAS".equals(tipoDisputa)) {

            campeonato.setQuantidadeGrupos(null);
            campeonato.setClassificadosPorGrupo(null);
            campeonato.setVagasAdicionais(0);
            campeonato.setPrimeiraFaseEliminatoria(
                    dto.getPrimeiraFaseEliminatoria());

        } else if ("PONTOS_CORRIDOS".equals(tipoDisputa)) {

            campeonato.setQuantidadeGrupos(null);
            campeonato.setClassificadosPorGrupo(null);
            campeonato.setVagasAdicionais(0);
            campeonato.setPrimeiraFaseEliminatoria(null);
        }

        return campeonatoRepository.save(campeonato);
    }

    // Validação das eliminatórias diretas
    private void validarFaseEliminatoria(String fase) {

        if (!"OITAVAS".equals(fase)
                && !"QUARTAS".equals(fase)
                && !"SEMIFINAIS".equals(fase)
                && !"FINAL".equals(fase)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione uma primeira fase eliminatória válida."
            );
        }
    }

    public void excluir(Long id) {

        Campeonato campeonato = buscarPorId(id);

        boolean possuiEquipes =
                campeonatoEquipeRepository.existsByCampeonatoId(id);

        boolean possuiPartidas =
                partidaRepository.existsByCampeonatoId(id);

        boolean possuiClassificacao =
                classificacaoRepository.existsByCampeonatoId(id);

        boolean possuiGrupos =
                grupoRepository.existsByCampeonatoId(id);

        if (possuiEquipes || possuiPartidas
                || possuiClassificacao || possuiGrupos) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir este campeonato, pois possui registros vinculados."
            );
        }

        campeonatoRepository.delete(campeonato);
    }

    private void validarDatas(CampeonatoDTO dto) {

        if (dto.getDataInicio() != null
                && dto.getDataFim() != null
                && dto.getDataFim().isBefore(dto.getDataInicio())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data final do campeonato não pode ser anterior à data inicial."
            );
        }
    }
}

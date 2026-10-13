
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

    public Campeonato salvar(CampeonatoDTO dto) {

        validarDatas(dto);

        Campeonato campeonato = new Campeonato();

        campeonato.setNome(dto.getNome());
        campeonato.setAno(dto.getAno());
        campeonato.setRegulamento(dto.getRegulamento());
        campeonato.setDataInicio(dto.getDataInicio());
        campeonato.setDataFim(dto.getDataFim());

        return campeonatoRepository.save(campeonato);
    }

    public Campeonato atualizar(Long id, CampeonatoDTO dto) {

        validarDatas(dto);

        Campeonato campeonato = buscarPorId(id);

        campeonato.setNome(dto.getNome());
        campeonato.setAno(dto.getAno());
        campeonato.setRegulamento(dto.getRegulamento());
        campeonato.setDataInicio(dto.getDataInicio());
        campeonato.setDataFim(dto.getDataFim());

        return campeonatoRepository.save(campeonato);
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

            throw new RuntimeException(
                    "A data final do campeonato não pode ser anterior à data inicial");
        }
    }
}



package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.model.Grupo;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.GrupoRepository;
import br.com.teamxp.futscore_api.repository.CampeonatoEquipeRepository;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final CampeonatoEquipeRepository campeonatoEquipeRepository;

    public GrupoService(
            GrupoRepository grupoRepository,
            CampeonatoRepository campeonatoRepository,
            CampeonatoEquipeRepository campeonatoEquipeRepository) {

        this.grupoRepository = grupoRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.campeonatoEquipeRepository = campeonatoEquipeRepository;
    }

    public List<Grupo> listarPorCampeonato(Long campeonatoId) {

        if (!campeonatoRepository.existsById(campeonatoId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Campeonato não encontrado"
            );
        }

        return grupoRepository.findByCampeonatoId(campeonatoId);
    }

    public Grupo buscarPorId(Long id) {

        return grupoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Grupo não encontrado"
            ));
    }

    public Grupo salvar(Long campeonatoId, String nome) {

        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Campeonato não encontrado"
            ));

        if (nome == null || nome.trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O nome do grupo é obrigatório"
            );
        }

        String nomeFormatado = nome.trim();

        if (grupoRepository.existsByCampeonatoIdAndNomeIgnoreCase(
                campeonatoId, nomeFormatado)) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Já existe um grupo com esse nome no campeonato"
            );
        }
        
        // Verifica o formato de disputa do campeonato
        if (!"GRUPOS_ELIMINATORIAS".equals(
                campeonato.getTipoDisputa())) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Este campeonato não utiliza fase de grupos."
            );
        }

        // Verifica o limite de grupos configurado
        Integer quantidadePermitida =
                campeonato.getQuantidadeGrupos();

        if (quantidadePermitida == null
                || quantidadePermitida < 1) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Configure a quantidade de grupos do campeonato."
            );
        }

        long quantidadeAtual =
                grupoRepository.findByCampeonatoId(campeonatoId).size();

        if (quantidadeAtual >= quantidadePermitida) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "O campeonato já atingiu o limite de "
                + quantidadePermitida + " grupos."
            );
        }

        Grupo grupo = new Grupo();
        grupo.setNome(nomeFormatado);
        grupo.setCampeonato(campeonato);

        return grupoRepository.save(grupo);
    }
    
    public Grupo atualizar(Long id, String nome) {

        // Busca o grupo existente
        Grupo grupo = buscarPorId(id);

        // Valida o nome informado
        if (nome == null || nome.trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O nome do grupo é obrigatório"
            );
        }

        String nomeFormatado = nome.trim();

        // Verifica se o nome já pertence a outro grupo
        // do mesmo campeonato
        Long campeonatoId = grupo.getCampeonato().getId();

        if (!grupo.getNome().equalsIgnoreCase(nomeFormatado)
                && grupoRepository.existsByCampeonatoIdAndNomeIgnoreCase(
                    campeonatoId, nomeFormatado)) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Já existe outro grupo com esse nome no campeonato"
            );
        }

        // Atualiza o nome do grupo
        grupo.setNome(nomeFormatado);

        return grupoRepository.save(grupo);
    }

    public void excluir(Long id) {

        // Verifica se o grupo existe
        Grupo grupo = buscarPorId(id);

        // Impede a exclusão de grupos com equipes vinculadas
        if (campeonatoEquipeRepository.existsByGrupoId(id)) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Não é possível excluir este grupo, pois existem equipes vinculadas."
            );
        }

        // Exclui o grupo somente se não possuir equipes vinculadas
        grupoRepository.delete(grupo);
    }

}


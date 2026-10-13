
package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.model.Grupo;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;
import br.com.teamxp.futscore_api.repository.GrupoRepository;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final CampeonatoRepository campeonatoRepository;

    public GrupoService(
            GrupoRepository grupoRepository,
            CampeonatoRepository campeonatoRepository) {

        this.grupoRepository = grupoRepository;
        this.campeonatoRepository = campeonatoRepository;
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

        Grupo grupo = new Grupo();
        grupo.setNome(nomeFormatado);
        grupo.setCampeonato(campeonato);

        return grupoRepository.save(grupo);
    }
}


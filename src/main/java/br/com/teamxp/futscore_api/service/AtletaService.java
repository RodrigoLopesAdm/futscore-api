package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.AtletaDTO;
import br.com.teamxp.futscore_api.model.Atleta;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.repository.AtletaRepository;
import br.com.teamxp.futscore_api.repository.EquipeRepository;

@Service
public class AtletaService {

    private final AtletaRepository atletaRepository;
    private final EquipeRepository equipeRepository;

    public AtletaService(
            AtletaRepository atletaRepository,
            EquipeRepository equipeRepository) {

        this.atletaRepository = atletaRepository;
        this.equipeRepository = equipeRepository;
    }

    public List<Atleta> listarTodos() {
        return atletaRepository.findAll();
    }

    public Atleta buscarPorId(Long id) {
        return atletaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Atleta não encontrado"));
    }

    public Atleta salvar(AtletaDTO dto) {

        validarNumeroCamisa(dto);

        Equipe equipe = equipeRepository.findById(dto.getEquipeId())
                .orElseThrow(() ->
                        new RuntimeException("Equipe não encontrada"));

        Atleta atleta = new Atleta();

        atleta.setNome(dto.getNome());
        atleta.setPosicao(dto.getPosicao());
        atleta.setDataNascimento(dto.getDataNascimento());
        atleta.setNumero(dto.getNumero());
        atleta.setEquipe(equipe);

        return atletaRepository.save(atleta);
    }

    public Atleta atualizar(Long id, AtletaDTO dto) {

        validarNumeroCamisa(dto);

        Atleta atleta = buscarPorId(id);

        Equipe equipe = equipeRepository.findById(dto.getEquipeId())
                .orElseThrow(() ->
                        new RuntimeException("Equipe não encontrada"));

        atleta.setNome(dto.getNome());
        atleta.setPosicao(dto.getPosicao());
        atleta.setDataNascimento(dto.getDataNascimento());
        atleta.setNumero(dto.getNumero());
        atleta.setEquipe(equipe);

        return atletaRepository.save(atleta);
    }

    public void excluir(Long id) {
        Atleta atleta = buscarPorId(id);
        atletaRepository.delete(atleta);
    }

    private void validarNumeroCamisa(AtletaDTO dto) {

        if (dto.getNumero() != null
                && (dto.getNumero() < 1 || dto.getNumero() > 99)) {

            throw new RuntimeException(
                    "O número da camisa deve estar entre 1 e 99");
        }
    }
}
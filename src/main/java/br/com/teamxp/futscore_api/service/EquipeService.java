package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.EquipeDTO;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.repository.EquipeRepository;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;

    public EquipeService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    public List<Equipe> listarTodos() {
        return equipeRepository.findAll();
    }

    public Equipe buscarPorId(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada"));
    }

    public Equipe salvar(EquipeDTO dto) {

        Equipe equipe = new Equipe();

        equipe.setNome(dto.getNome());
        equipe.setBairro(dto.getBairro());
        equipe.setTecnico(dto.getTecnico());
        equipe.setContato(dto.getContato());

        return equipeRepository.save(equipe);
    }

    public Equipe atualizar(Long id, EquipeDTO dto) {

        Equipe equipe = buscarPorId(id);

        equipe.setNome(dto.getNome());
        equipe.setBairro(dto.getBairro());
        equipe.setTecnico(dto.getTecnico());
        equipe.setContato(dto.getContato());

        return equipeRepository.save(equipe);
    }

    public void excluir(Long id) {
        Equipe equipe = buscarPorId(id);
        equipeRepository.delete(equipe);
    }
}
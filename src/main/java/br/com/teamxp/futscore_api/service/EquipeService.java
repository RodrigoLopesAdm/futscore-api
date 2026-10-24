
package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.teamxp.futscore_api.dto.EquipeDTO;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.repository.EquipeRepository;
import br.com.teamxp.futscore_api.repository.CampeonatoEquipeRepository;
import br.com.teamxp.futscore_api.repository.AtletaRepository;
import br.com.teamxp.futscore_api.repository.PartidaRepository;
import br.com.teamxp.futscore_api.repository.ClassificacaoRepository;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final CampeonatoEquipeRepository campeonatoEquipeRepository;
    private final AtletaRepository atletaRepository;
    private final PartidaRepository partidaRepository;
    private final ClassificacaoRepository classificacaoRepository;

    // Construtor com injeção de dependências
    public EquipeService(
            EquipeRepository equipeRepository,
            CampeonatoEquipeRepository campeonatoEquipeRepository,
            AtletaRepository atletaRepository,
            PartidaRepository partidaRepository,
            ClassificacaoRepository classificacaoRepository) {

        this.equipeRepository = equipeRepository;
        this.campeonatoEquipeRepository = campeonatoEquipeRepository;
        this.atletaRepository = atletaRepository;
        this.partidaRepository = partidaRepository;
        this.classificacaoRepository = classificacaoRepository;
    }

    // Lista todas as equipes cadastradas
    public List<Equipe> listarTodos() {
        return equipeRepository.findAll();
    }

    // Busca uma equipe pelo ID
    public Equipe buscarPorId(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Equipe não encontrada"
                        ));
    }

    // Cadastra uma nova equipe
    public Equipe salvar(EquipeDTO dto) {

        Equipe equipe = new Equipe();

        equipe.setNome(dto.getNome());
        equipe.setBairro(dto.getBairro());
        equipe.setTecnico(dto.getTecnico());
        equipe.setContato(dto.getContato());

        return equipeRepository.save(equipe);
    }

    // Atualiza uma equipe existente
    public Equipe atualizar(Long id, EquipeDTO dto) {

        Equipe equipe = buscarPorId(id);

        equipe.setNome(dto.getNome());
        equipe.setBairro(dto.getBairro());
        equipe.setTecnico(dto.getTecnico());
        equipe.setContato(dto.getContato());

        return equipeRepository.save(equipe);
    }

    // Exclui uma equipe somente se não possuir vínculos
    public void excluir(Long id) {

        // Verifica se a equipe existe
        Equipe equipe = buscarPorId(id);

        // Verifica se participa de algum campeonato
        if (campeonatoEquipeRepository.existsByEquipeId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir esta equipe, pois ela está inscrita em um campeonato."
            );
        }

        // Verifica se possui atletas cadastrados
        if (atletaRepository.existsByEquipeId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir esta equipe, pois ela possui atletas cadastrados."
            );
        }

        // Verifica se possui partidas como mandante ou visitante
        if (partidaRepository.existsByMandanteId(id)
                || partidaRepository.existsByVisitanteId(id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir esta equipe, pois ela possui partidas cadastradas."
            );
        }

        // Verifica se possui registros de classificação
        if (classificacaoRepository.existsByEquipeId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir esta equipe, pois ela possui registros de classificação."
            );
        }

        // Realiza a exclusão após verificar os vínculos conhecidos
        equipeRepository.delete(equipe);
    }
}


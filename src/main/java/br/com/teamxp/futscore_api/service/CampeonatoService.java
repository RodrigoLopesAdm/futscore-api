package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.CampeonatoDTO;
import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.repository.CampeonatoRepository;

@Service
public class CampeonatoService {

    private final CampeonatoRepository campeonatoRepository;

    public CampeonatoService(CampeonatoRepository campeonatoRepository) {
        this.campeonatoRepository = campeonatoRepository;
    }

    public List<Campeonato> listarTodos() {
        return campeonatoRepository.findAll();
    }

    public Campeonato buscarPorId(Long id) {
        return campeonatoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Campeonato não encontrado"));
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

package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.ComunicadoDTO;
import br.com.teamxp.futscore_api.model.Comunicado;
import br.com.teamxp.futscore_api.repository.ComunicadoRepository;

@Service
public class ComunicadoService {

    private final ComunicadoRepository comunicadoRepository;

    public ComunicadoService(ComunicadoRepository comunicadoRepository) {
        this.comunicadoRepository = comunicadoRepository;
    }

    public List<Comunicado> listarTodos() {
        return comunicadoRepository.findAll();
    }

    public Comunicado buscarPorId(Long id) {
        return comunicadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comunicado não encontrado"));
    }

    public Comunicado salvar(ComunicadoDTO dto) {

        Comunicado comunicado = new Comunicado();

        comunicado.setTitulo(dto.getTitulo());
        comunicado.setMensagem(dto.getMensagem());
        comunicado.setDataPublicacao(dto.getDataPublicacao());

        return comunicadoRepository.save(comunicado);
    }

    public Comunicado atualizar(Long id, ComunicadoDTO dto) {

        Comunicado comunicado = buscarPorId(id);

        comunicado.setTitulo(dto.getTitulo());
        comunicado.setMensagem(dto.getMensagem());
        comunicado.setDataPublicacao(dto.getDataPublicacao());

        return comunicadoRepository.save(comunicado);
    }

    public void excluir(Long id) {
        Comunicado comunicado = buscarPorId(id);
        comunicadoRepository.delete(comunicado);
    }
}

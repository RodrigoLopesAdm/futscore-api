package br.com.teamxp.futscore_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.teamxp.futscore_api.dto.FeedbackDTO;
import br.com.teamxp.futscore_api.model.Feedback;
import br.com.teamxp.futscore_api.repository.FeedbackRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> listarTodos() {
        return feedbackRepository.findAll();
    }

    public Feedback buscarPorId(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback não encontrado"));
    }

    public Feedback salvar(FeedbackDTO dto) {

        Feedback feedback = new Feedback();

        feedback.setMensagem(dto.getMensagem());
        feedback.setAvaliacao(dto.getAvaliacao());

        return feedbackRepository.save(feedback);
    }

    public Feedback atualizar(Long id, FeedbackDTO dto) {

        Feedback feedback = buscarPorId(id);

        feedback.setMensagem(dto.getMensagem());
        feedback.setAvaliacao(dto.getAvaliacao());

        return feedbackRepository.save(feedback);
    }

    public void excluir(Long id) {
        Feedback feedback = buscarPorId(id);
        feedbackRepository.delete(feedback);
    }
}
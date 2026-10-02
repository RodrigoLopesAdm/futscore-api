package br.com.teamxp.futscore_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.teamxp.futscore_api.dto.FeedbackDTO;
import br.com.teamxp.futscore_api.model.Feedback;
import br.com.teamxp.futscore_api.service.FeedbackService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public List<Feedback> listarTodos() {
        return feedbackService.listarTodos();
    }

    @GetMapping("/{id}")
    public Feedback buscarPorId(@PathVariable Long id) {
        return feedbackService.buscarPorId(id);
    }

    @PostMapping
    public Feedback salvar(
            @Valid @RequestBody FeedbackDTO dto) {
        return feedbackService.salvar(dto);
    }

    @PutMapping("/{id}")
    public Feedback atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackDTO dto) {
        return feedbackService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        feedbackService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
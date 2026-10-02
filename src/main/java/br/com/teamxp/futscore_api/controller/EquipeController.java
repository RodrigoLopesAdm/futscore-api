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

import br.com.teamxp.futscore_api.dto.EquipeDTO;
import br.com.teamxp.futscore_api.model.Equipe;
import br.com.teamxp.futscore_api.service.EquipeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @GetMapping
    public List<Equipe> listarTodos() {
        return equipeService.listarTodos();
    }

    @GetMapping("/{id}")
    public Equipe buscarPorId(@PathVariable Long id) {
        return equipeService.buscarPorId(id);
    }

    @PostMapping
    public Equipe salvar(
            @Valid @RequestBody EquipeDTO dto) {
        return equipeService.salvar(dto);
    }

    @PutMapping("/{id}")
    public Equipe atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipeDTO dto) {
        return equipeService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        equipeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

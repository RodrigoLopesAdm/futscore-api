package br.com.teamxp.futscore_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.teamxp.futscore_api.model.EquipePartida;
import br.com.teamxp.futscore_api.service.EquipePartidaService;

@RestController
@RequestMapping("/api/equipe-partidas")
public class EquipePartidaController {

    private final EquipePartidaService equipePartidaService;

    public EquipePartidaController(
            EquipePartidaService equipePartidaService) {
        this.equipePartidaService = equipePartidaService;
    }

    @GetMapping
    public List<EquipePartida> listarTodos() {
        return equipePartidaService.listarTodos();
    }

    @GetMapping("/{id}")
    public EquipePartida buscarPorId(@PathVariable Long id) {
        return equipePartidaService.buscarPorId(id);
    }

    @PostMapping
    public EquipePartida salvar(
            @RequestParam Long equipeId,
            @RequestParam Long partidaId) {

        return equipePartidaService.salvar(
                equipeId,
                partidaId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        equipePartidaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

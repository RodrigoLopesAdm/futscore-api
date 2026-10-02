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

import br.com.teamxp.futscore_api.dto.EventoPartidaDTO;
import br.com.teamxp.futscore_api.model.EventoPartida;
import br.com.teamxp.futscore_api.service.EventoPartidaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/eventos-partida")
public class EventoPartidaController {

    private final EventoPartidaService eventoPartidaService;

    public EventoPartidaController(EventoPartidaService eventoPartidaService) {
        this.eventoPartidaService = eventoPartidaService;
    }

    @GetMapping
    public List<EventoPartida> listarTodos() {
        return eventoPartidaService.listarTodos();
    }

    @GetMapping("/{id}")
    public EventoPartida buscarPorId(@PathVariable Long id) {
        return eventoPartidaService.buscarPorId(id);
    }

    @PostMapping
    public EventoPartida salvar(
            @Valid @RequestBody EventoPartidaDTO dto) {
        return eventoPartidaService.salvar(dto);
    }

    @PutMapping("/{id}")
    public EventoPartida atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoPartidaDTO dto) {
        return eventoPartidaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        eventoPartidaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

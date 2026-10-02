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

import br.com.teamxp.futscore_api.dto.PartidaDTO;
import br.com.teamxp.futscore_api.model.Partida;
import br.com.teamxp.futscore_api.service.PartidaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/partidas")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @GetMapping
    public List<Partida> listarTodos() {
        return partidaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Partida buscarPorId(@PathVariable Long id) {
        return partidaService.buscarPorId(id);
    }

    @PostMapping
    public Partida salvar(
            @Valid @RequestBody PartidaDTO dto) {
        return partidaService.salvar(dto);
    }

    @PutMapping("/{id}")
    public Partida atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PartidaDTO dto) {
        return partidaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        partidaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

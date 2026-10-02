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

import br.com.teamxp.futscore_api.dto.CampeonatoDTO;
import br.com.teamxp.futscore_api.model.Campeonato;
import br.com.teamxp.futscore_api.service.CampeonatoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {

    private final CampeonatoService campeonatoService;

    public CampeonatoController(CampeonatoService campeonatoService) {
        this.campeonatoService = campeonatoService;
    }

    @GetMapping
    public List<Campeonato> listarTodos() {
        return campeonatoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Campeonato buscarPorId(@PathVariable Long id) {
        return campeonatoService.buscarPorId(id);
    }

    @PostMapping
    public Campeonato salvar(
            @Valid @RequestBody CampeonatoDTO dto) {

        return campeonatoService.salvar(dto);
    }

    @PutMapping("/{id}")
    public Campeonato atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CampeonatoDTO dto) {

        return campeonatoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        campeonatoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

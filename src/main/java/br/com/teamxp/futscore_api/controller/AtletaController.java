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

import br.com.teamxp.futscore_api.dto.AtletaDTO;
import br.com.teamxp.futscore_api.model.Atleta;
import br.com.teamxp.futscore_api.service.AtletaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/atletas")
public class AtletaController {

    private final AtletaService atletaService;

    public AtletaController(AtletaService atletaService) {
        this.atletaService = atletaService;
    }

    @GetMapping
    public List<Atleta> listarTodos() {
        return atletaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Atleta buscarPorId(@PathVariable Long id) {
        return atletaService.buscarPorId(id);
    }

    @PostMapping
    public Atleta salvar(
            @Valid @RequestBody AtletaDTO dto) {
        return atletaService.salvar(dto);
    }

    @PutMapping("/{id}")
    public Atleta atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtletaDTO dto) {
        return atletaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        atletaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

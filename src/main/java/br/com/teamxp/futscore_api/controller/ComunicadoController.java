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

import br.com.teamxp.futscore_api.dto.ComunicadoDTO;
import br.com.teamxp.futscore_api.model.Comunicado;
import br.com.teamxp.futscore_api.service.ComunicadoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comunicados")
public class ComunicadoController {

    private final ComunicadoService comunicadoService;

    public ComunicadoController(ComunicadoService comunicadoService) {
        this.comunicadoService = comunicadoService;
    }

    @GetMapping
    public List<Comunicado> listarTodos() {
        return comunicadoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Comunicado buscarPorId(@PathVariable Long id) {
        return comunicadoService.buscarPorId(id);
    }

    @PostMapping
    public Comunicado salvar(
            @Valid @RequestBody ComunicadoDTO dto) {
        return comunicadoService.salvar(dto);
    }

    @PutMapping("/{id}")
    public Comunicado atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ComunicadoDTO dto) {
        return comunicadoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        comunicadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

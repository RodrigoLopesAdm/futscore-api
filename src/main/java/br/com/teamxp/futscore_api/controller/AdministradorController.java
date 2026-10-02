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

import br.com.teamxp.futscore_api.dto.AdministradorDTO;
import br.com.teamxp.futscore_api.model.Administrador;
import br.com.teamxp.futscore_api.service.AdministradorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping
    public List<Administrador> listarTodos() {
        return administradorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Administrador buscarPorId(@PathVariable Long id) {
        return administradorService.buscarPorId(id);
    }

    @PostMapping
    public Administrador salvar(
            @Valid @RequestBody AdministradorDTO dto) {
        return administradorService.salvar(dto);
    }

    @PutMapping("/{id}")
    public Administrador atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AdministradorDTO dto) {
        return administradorService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        administradorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

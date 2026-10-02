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

import br.com.teamxp.futscore_api.model.CampeonatoEquipe;
import br.com.teamxp.futscore_api.service.CampeonatoEquipeService;

@RestController
@RequestMapping("/api/campeonato-equipes")
public class CampeonatoEquipeController {

    private final CampeonatoEquipeService campeonatoEquipeService;

    public CampeonatoEquipeController(
            CampeonatoEquipeService campeonatoEquipeService) {
        this.campeonatoEquipeService = campeonatoEquipeService;
    }

    @GetMapping
    public List<CampeonatoEquipe> listarTodos() {
        return campeonatoEquipeService.listarTodos();
    }

    @GetMapping("/{id}")
    public CampeonatoEquipe buscarPorId(@PathVariable Long id) {
        return campeonatoEquipeService.buscarPorId(id);
    }

    @PostMapping
    public CampeonatoEquipe salvar(
            @RequestParam Long campeonatoId,
            @RequestParam Long equipeId) {

        return campeonatoEquipeService.salvar(
                campeonatoId,
                equipeId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        campeonatoEquipeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
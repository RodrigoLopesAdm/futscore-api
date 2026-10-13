
package br.com.teamxp.futscore_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.teamxp.futscore_api.model.CampeonatoEquipe;
import br.com.teamxp.futscore_api.service.CampeonatoEquipeService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/campeonato-equipes")
@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
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
            @RequestParam Long equipeId,
            @RequestParam(required = false) Long grupoId) {

        return campeonatoEquipeService.salvar(
                campeonatoId,
                equipeId,
                grupoId);
    }

    
    @PutMapping("/{id}/grupo")
    public CampeonatoEquipe atualizarGrupo(
            @PathVariable Long id,
            @RequestParam(required = false) Long grupoId) {

        return campeonatoEquipeService.atualizarGrupo(
                id,
                grupoId);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        campeonatoEquipeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

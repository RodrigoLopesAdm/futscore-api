
package br.com.teamxp.futscore_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.teamxp.futscore_api.model.Grupo;
import br.com.teamxp.futscore_api.service.GrupoService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/grupos")
@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    // Consultar um grupo pelo ID
    @GetMapping("/{id}")
    public Grupo buscarPorId(@PathVariable Long id) {
        return grupoService.buscarPorId(id);
    }

    // Listar grupos de um campeonato
    @GetMapping("/campeonato/{campeonatoId}")
    public List<Grupo> listarPorCampeonato(
            @PathVariable Long campeonatoId) {

        return grupoService.listarPorCampeonato(campeonatoId);
    }

    // Cadastrar um grupo em determinado campeonato
    @PostMapping("/campeonato/{campeonatoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Grupo salvar(
            @PathVariable Long campeonatoId,
            @RequestBody Map<String, String> dados) {

        return grupoService.salvar(
            campeonatoId,
            dados.get("nome")
        );
    }

    
    // Atualizar o nome de um grupo
    @PutMapping("/{id}")
    public Grupo atualizar(
            @PathVariable Long id,
            @RequestBody Map<String, String> dados) {

        return grupoService.atualizar(
            id,
            dados.get("nome")
        );
    }

    // Excluir um grupo
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {

        grupoService.excluir(id);
    }

}


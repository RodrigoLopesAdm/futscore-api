package br.com.teamxp.futscore_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.teamxp.futscore_api.dto.AssistenciaDTO;
import br.com.teamxp.futscore_api.service.AssistenciaService;

@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
@RestController
@RequestMapping("/api/assistencias")
public class AssistenciaController {

    private final AssistenciaService assistenciaService;

    public AssistenciaController(
            AssistenciaService assistenciaService) {

        this.assistenciaService = assistenciaService;
    }

    @GetMapping("/campeonato/{campeonatoId}")
    public List<AssistenciaDTO> buscarAssistencias(
            @PathVariable Long campeonatoId) {

        return assistenciaService.gerarAssistencias(campeonatoId);
    }
}

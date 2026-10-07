package br.com.teamxp.futscore_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.teamxp.futscore_api.dto.ArtilhariaDTO;
import br.com.teamxp.futscore_api.service.ArtilhariaService;

@RestController
@RequestMapping("/api/artilharia")
public class ArtilhariaController {

    private final ArtilhariaService artilhariaService;

    public ArtilhariaController(
            ArtilhariaService artilhariaService) {

        this.artilhariaService = artilhariaService;
    }

    @GetMapping("/campeonato/{campeonatoId}")
    public List<ArtilhariaDTO> buscarArtilharia(
            @PathVariable Long campeonatoId) {

        return artilhariaService.gerarArtilharia(campeonatoId);
    }
}

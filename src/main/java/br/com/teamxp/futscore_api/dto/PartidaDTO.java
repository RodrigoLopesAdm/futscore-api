package br.com.teamxp.futscore_api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public class PartidaDTO {

    private Long id;

    @NotNull(message = "A data da partida é obrigatória")
    private LocalDate data;

    @NotNull(message = "O horário da partida é obrigatório")
    private LocalTime hora;

    @NotNull(message = "A rodada é obrigatória")
    private Integer rodada;

    private Integer placarMandante;
    private Integer placarVisitante;

    private String status;

    @NotNull(message = "O campeonato é obrigatório")
    private Long campeonatoId;

    @NotNull(message = "A equipe mandante é obrigatória")
    private Long mandanteId;

    @NotNull(message = "A equipe visitante é obrigatória")
    private Long visitanteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Integer getRodada() {
        return rodada;
    }

    public void setRodada(Integer rodada) {
        this.rodada = rodada;
    }

    public Integer getPlacarMandante() {
        return placarMandante;
    }

    public void setPlacarMandante(Integer placarMandante) {
        this.placarMandante = placarMandante;
    }

    public Integer getPlacarVisitante() {
        return placarVisitante;
    }

    public void setPlacarVisitante(Integer placarVisitante) {
        this.placarVisitante = placarVisitante;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(Long campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public Long getMandanteId() {
        return mandanteId;
    }

    public void setMandanteId(Long mandanteId) {
        this.mandanteId = mandanteId;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
    }
}

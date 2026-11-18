package br.com.teamxp.futscore_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResultadoPartidaDTO {

    @NotNull(message = "O placar da equipe mandante é obrigatório")
    @Min(value = 0, message = "O placar da equipe mandante não pode ser negativo")
    private Integer placarMandante;

    @NotNull(message = "O placar da equipe visitante é obrigatório")
    @Min(value = 0, message = "O placar da equipe visitante não pode ser negativo")
    private Integer placarVisitante;

    // Placares da disputa de pênaltis
    @Min(value = 0, message = "O placar de pênaltis da equipe mandante não pode ser negativo")
    private Integer penaltisMandante;

    @Min(value = 0, message = "O placar de pênaltis da equipe visitante não pode ser negativo")
    private Integer penaltisVisitante;

    @NotBlank(message = "O status da partida é obrigatório")
    private String status;

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

    public Integer getPenaltisMandante() {
        return penaltisMandante;
    }

    public void setPenaltisMandante(Integer penaltisMandante) {
        this.penaltisMandante = penaltisMandante;
    }

    public Integer getPenaltisVisitante() {
        return penaltisVisitante;
    }

    public void setPenaltisVisitante(Integer penaltisVisitante) {
        this.penaltisVisitante = penaltisVisitante;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
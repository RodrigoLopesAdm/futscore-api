package br.com.teamxp.futscore_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventoPartidaDTO {

    private Long id;

    @NotBlank(message = "O tipo do evento é obrigatório")
    private String tipo;

    @NotNull(message = "O minuto do evento é obrigatório")
    private Integer minuto;

    @NotNull(message = "A partida é obrigatória")
    private Long partidaId;

    @NotNull(message = "O atleta é obrigatório")
    private Long atletaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }

    public Long getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Long partidaId) {
        this.partidaId = partidaId;
    }

    public Long getAtletaId() {
        return atletaId;
    }

    public void setAtletaId(Long atletaId) {
        this.atletaId = atletaId;
    }
}

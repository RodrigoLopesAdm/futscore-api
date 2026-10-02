package br.com.teamxp.futscore_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeedbackDTO {

    private Long id;

    @NotBlank(message = "A mensagem do feedback é obrigatória")
    private String mensagem;

    @NotNull(message = "A avaliação é obrigatória")
    @Min(value = 1, message = "A avaliação mínima é 1")
    @Max(value = 5, message = "A avaliação máxima é 5")
    private Integer avaliacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Integer getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }
}
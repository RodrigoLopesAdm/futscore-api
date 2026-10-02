package br.com.teamxp.futscore_api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class ComunicadoDTO {

    private Long id;

    @NotBlank(message = "O título do comunicado é obrigatório")
    private String titulo;

    @NotBlank(message = "A mensagem do comunicado é obrigatória")
    private String mensagem;

    private LocalDateTime dataPublicacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
}

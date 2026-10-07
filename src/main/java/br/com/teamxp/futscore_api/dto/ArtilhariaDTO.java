package br.com.teamxp.futscore_api.dto;

public class ArtilhariaDTO {

    private Long atletaId;
    private String atletaNome;
    private Long equipeId;
    private String equipeNome;
    private Long gols;

    public ArtilhariaDTO(
            Long atletaId,
            String atletaNome,
            Long equipeId,
            String equipeNome,
            Long gols) {

        this.atletaId = atletaId;
        this.atletaNome = atletaNome;
        this.equipeId = equipeId;
        this.equipeNome = equipeNome;
        this.gols = gols;
    }

    public Long getAtletaId() {
        return atletaId;
    }

    public void setAtletaId(Long atletaId) {
        this.atletaId = atletaId;
    }

    public String getAtletaNome() {
        return atletaNome;
    }

    public void setAtletaNome(String atletaNome) {
        this.atletaNome = atletaNome;
    }

    public Long getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(Long equipeId) {
        this.equipeId = equipeId;
    }

    public String getEquipeNome() {
        return equipeNome;
    }

    public void setEquipeNome(String equipeNome) {
        this.equipeNome = equipeNome;
    }

    public Long getGols() {
        return gols;
    }

    public void setGols(Long gols) {
        this.gols = gols;
    }
}

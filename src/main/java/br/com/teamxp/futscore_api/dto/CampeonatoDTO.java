
package br.com.teamxp.futscore_api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public class CampeonatoDTO {

    private Long id;

    @NotBlank(message = "O nome do campeonato é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotNull(message = "O ano do campeonato é obrigatório")
    private Integer ano;

    private String regulamento;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "A data de término é obrigatória")
    private LocalDate dataFim;

    // =========================================
    // REGRAS DE PONTUAÇÃO DO CAMPEONATO
    // =========================================

    @Min(value = 0, message = "Os pontos por vitória não podem ser negativos")
    private Integer pontosVitoria;

    @Min(value = 0, message = "Os pontos por empate não podem ser negativos")
    private Integer pontosEmpate;

    @Min(value = 0, message = "Os pontos por derrota não podem ser negativos")
    private Integer pontosDerrota;
    
    // =========================================
    // CONFIGURAÇÃO DO FORMATO DE DISPUTA
    // =========================================

    private String tipoDisputa;

    @Min(value = 1, message = "A quantidade de grupos deve ser maior que zero")
    private Integer quantidadeGrupos;

    @Min(value = 1, message = "Deve haver pelo menos um classificado por grupo")
    private Integer classificadosPorGrupo;

    @Min(value = 0, message = "As vagas adicionais não podem ser negativas")
    private Integer vagasAdicionais;

    private String primeiraFaseEliminatoria;

    // =========================================
    // GETTERS E SETTERS
    // =========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getRegulamento() {
        return regulamento;
    }

    public void setRegulamento(String regulamento) {
        this.regulamento = regulamento;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    // Pontos por vitória

    public Integer getPontosVitoria() {
        return pontosVitoria;
    }

    public void setPontosVitoria(Integer pontosVitoria) {
        this.pontosVitoria = pontosVitoria;
    }

    // Pontos por empate

    public Integer getPontosEmpate() {
        return pontosEmpate;
    }

    public void setPontosEmpate(Integer pontosEmpate) {
        this.pontosEmpate = pontosEmpate;
    }

    // Pontos por derrota

    public Integer getPontosDerrota() {
        return pontosDerrota;
    }

    public void setPontosDerrota(Integer pontosDerrota) {
        this.pontosDerrota = pontosDerrota;
    }

    public String getTipoDisputa() {
        return tipoDisputa;
    }

    public void setTipoDisputa(String tipoDisputa) {
        this.tipoDisputa = tipoDisputa;
    }

    public Integer getQuantidadeGrupos() {
        return quantidadeGrupos;
    }

    public void setQuantidadeGrupos(Integer quantidadeGrupos) {
        this.quantidadeGrupos = quantidadeGrupos;
    }

    public Integer getClassificadosPorGrupo() {
        return classificadosPorGrupo;
    }

    public void setClassificadosPorGrupo(Integer classificadosPorGrupo) {
        this.classificadosPorGrupo = classificadosPorGrupo;
    }

    public Integer getVagasAdicionais() {
        return vagasAdicionais;
    }

    public void setVagasAdicionais(Integer vagasAdicionais) {
        this.vagasAdicionais = vagasAdicionais;
    }

    public String getPrimeiraFaseEliminatoria() {
        return primeiraFaseEliminatoria;
    }

    public void setPrimeiraFaseEliminatoria(String primeiraFaseEliminatoria) {
        this.primeiraFaseEliminatoria = primeiraFaseEliminatoria;
    }

}

package br.com.teamxp.futscore_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;

    private Integer ano;

    private String regulamento;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    // Pontuação definida pelo regulamento do campeonato
    private Integer pontosVitoria = 3;

    private Integer pontosEmpate = 1;

    private Integer pontosDerrota = 0;
    
    // Formato de disputa do campeonato
    private String tipoDisputa = "GRUPOS_ELIMINATORIAS";
    
    // Quantidade de grupos prevista para o campeonato
    private Integer quantidadeGrupos;
    
    // Quantidade de equipes classificadas diretamente por grupo
    private Integer classificadosPorGrupo;
    
    // Vagas adicionais para os melhores colocados entre os grupos
    private Integer vagasAdicionais = 0;
    
    // Primeira fase eliminatória do campeonato
    private String primeiraFaseEliminatoria;
    
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
   
    public Integer getPontosVitoria() {
        return pontosVitoria;
    }

    public void setPontosVitoria(Integer pontosVitoria) {
        this.pontosVitoria = pontosVitoria;
    }

    public Integer getPontosEmpate() {
        return pontosEmpate;
    }

    public void setPontosEmpate(Integer pontosEmpate) {
        this.pontosEmpate = pontosEmpate;
    }

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

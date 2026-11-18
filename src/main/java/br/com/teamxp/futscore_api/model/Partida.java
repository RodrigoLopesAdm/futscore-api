package br.com.teamxp.futscore_api.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private LocalTime hora;

    private Integer rodada;

    private Integer placarMandante;

    private Integer placarVisitante;

    // Resultado da disputa de pênaltis
    private Integer penaltisMandante;

    private Integer penaltisVisitante;

    private String status;

    // Identifica a fase da competição
    private String fase;

    @ManyToOne
    @JoinColumn(name = "campeonato_id")
    private Campeonato campeonato;

    @ManyToOne
    @JoinColumn(name = "mandante_id")
    private Equipe mandante;

    @ManyToOne
    @JoinColumn(name = "visitante_id")
    private Equipe visitante;

    // Grupo ao qual a partida pertence, quando aplicável
    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

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

    // Getters e setters dos pênaltis
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

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public Equipe getMandante() {
        return mandante;
    }

    public void setMandante(Equipe mandante) {
        this.mandante = mandante;
    }

    public Equipe getVisitante() {
        return visitante;
    }

    public void setVisitante(Equipe visitante) {
        this.visitante = visitante;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
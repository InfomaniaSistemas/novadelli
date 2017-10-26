/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "diaria")
public class Diaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtIn;
    @Column
    private BigDecimal dia;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtFin;
    @ManyToOne
    @JoinColumn
    private Funcionario funcionario;
    @ManyToOne
    @JoinColumn
    private Empresa empresa;
    @Column
    private BigDecimal valor;

    @Column
    private String descricaoCredito;
    @Column
    private String descricaoDebito;
    @Column
    private BigDecimal desconto = BigDecimal.ZERO;
    @Column
    private BigDecimal aux = BigDecimal.ZERO;
    @Column
    private BigDecimal aux1 = BigDecimal.ZERO;
    @Column
    private BigDecimal credito = BigDecimal.ZERO;

    @Column
    private BigDecimal valorTotalDiaria;
    @Column
    private BigDecimal totalDiariasExtras = BigDecimal.ZERO;

    @Column
    private BigDecimal horaExtra;
    @Column
    private BigDecimal uniHoraExtra;
    @Column
    private BigDecimal finalHoraExtra;
    @Column
    private BigDecimal valorHoraExtra;
    @Column
    private BigDecimal uni;

    public void calculaTotal() {

        valor = uni.multiply(dia);
        valorTotalDiaria = uni.multiply(dia);
        uniHoraExtra = uni.divide(new BigDecimal("8"));
        valorHoraExtra = horaExtra.multiply(uniHoraExtra);
        finalHoraExtra = valorHoraExtra;
        valor = valor.add(finalHoraExtra).add(credito).subtract(desconto);
        aux = valorTotalDiaria.add(valorHoraExtra);
    }

    public BigDecimal getTotalDiariasExtras() {
        return totalDiariasExtras;
    }

    public BigDecimal getAux() {
        return aux;
    }

    public void setAux(BigDecimal aux) {
        this.aux = aux;
    }

    public void setTotalDiariasExtras(BigDecimal totalDiariasExtras) {
        this.totalDiariasExtras = totalDiariasExtras;
    }

    public String getDescricaoCredito() {
        return descricaoCredito;
    }

    public void setDescricaoCredito(String descricaoCredito) {
        this.descricaoCredito = descricaoCredito;
    }

    public String getDescricaoDebito() {
        return descricaoDebito;
    }

    public void setDescricaoDebito(String descricaoDebito) {
        this.descricaoDebito = descricaoDebito;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public BigDecimal getValorTotalDiaria() {
        return valorTotalDiaria;
    }

    public void setValorTotalDiaria(BigDecimal valorTotalDiaria) {
        this.valorTotalDiaria = valorTotalDiaria;
    }

    public BigDecimal getFinalHoraExtra() {
        return finalHoraExtra;
    }

    public void setFinalHoraExtra(BigDecimal finalHoraExtra) {
        this.finalHoraExtra = finalHoraExtra;
    }

    public BigDecimal getUniHoraExtra() {
        return uniHoraExtra;
    }

    public void setUniHoraExtra(BigDecimal uniHoraExtra) {
        this.uniHoraExtra = uniHoraExtra;
    }

    public BigDecimal getValorHoraExtra() {
        return valorHoraExtra;
    }

    public void setValorHoraExtra(BigDecimal valorHoraExtra) {
        this.valorHoraExtra = valorHoraExtra;
    }

    public BigDecimal getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(BigDecimal horaExtra) {
        this.horaExtra = horaExtra;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getUni() {
        return uni;
    }

    public void setUni(BigDecimal uni) {
        this.uni = uni;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getDia() {
        return dia;
    }

    public void setDia(BigDecimal dia) {
        this.dia = dia;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getDtIn() {
        return dtIn;
    }

    public void setDtIn(Date dtIn) {
        this.dtIn = dtIn;
    }

    public Date getDtFin() {
        return dtFin;
    }

    public void setDtFin(Date dtFin) {
        this.dtFin = dtFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Diaria)) {
            return false;
        }
        Diaria other = (Diaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}

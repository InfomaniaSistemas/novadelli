/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "lacamentoBancario")
public class LancamentoBancario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private BigDecimal LacamentoSaida;
    @Column
    private BigDecimal LacamentoEntrada;
    @Column
    private String tipo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtEntrada;
    @Column
    private String obs;
    @Column
    private String documento;

    @ManyToOne
    private Pessoa pessoa;

    @ManyToOne(cascade = CascadeType.MERGE)
    private ContaBancaria contaBancaria;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void maiuscula() {

        obs = obs.toUpperCase();
//        documento = documento.toUpperCase();
    }

    public void entrada() {

        BigDecimal novoSaldo = LacamentoEntrada.add(contaBancaria.getSaldo());
        contaBancaria.setSaldo(novoSaldo);

    }

    public void saida() {

        BigDecimal novoSaldo = contaBancaria.getSaldo().subtract(LacamentoSaida);
        contaBancaria.setSaldo(novoSaldo);

    }

    public Date getDtEntrada() {
        return dtEntrada;
    }

    public void setDtEntrada(Date dtEntrada) {
        this.dtEntrada = dtEntrada;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getLacamentoSaida() {
        return LacamentoSaida;
    }

    public void setLacamentoSaida(BigDecimal LacamentoSaida) {
        this.LacamentoSaida = LacamentoSaida;
    }

    public BigDecimal getLacamentoEntrada() {
        return LacamentoEntrada;
    }

    public void setLacamentoEntrada(BigDecimal LacamentoEntrada) {
        this.LacamentoEntrada = LacamentoEntrada;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof LancamentoBancario)) {
            return false;
        }
        LancamentoBancario other = (LancamentoBancario) object;
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

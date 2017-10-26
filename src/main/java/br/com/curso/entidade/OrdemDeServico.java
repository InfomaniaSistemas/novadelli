/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
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
@Table(name = "ordemDeServico")
public class OrdemDeServico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manu_id", nullable = false)
    private Long id;
    @Column(name = "manu_datain")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataIn;
    @Column(name = "manu_datafin")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataFi;
    @Column(name = "manu_dataprev")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataPrev;
    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
    @ManyToOne
    @JoinColumn(name = "situacao_id")
    private SituacaoOs situacaoOs;
    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoDeManutencao tipoDeManutencao;
    @Column
    private String carro;
    @Column
    private String placa;
    @Column(name = "manu_obs", length = 1000)
    private String os = "".toUpperCase();
    @Column(name = "manu_status")
    private String status = "Aberta";
    @Column(name = "manu_sol", length = 1000)
    private String sol = "".toUpperCase();

    public void maiusculo() {
        os = os.toUpperCase();
        sol = sol.toUpperCase();

        
    }

    public String getStatus() {
        return status;
    }

    public String getCarro() {
        return carro;
    }

    public void setCarro(String carro) {
        this.carro = carro;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSol() {
        return sol.toUpperCase();
    }

    public void setSol(String sol) {
        this.sol = sol.toUpperCase();
    }

    public String getOs() {
        return os.toUpperCase();
    }

    public void setOs(String os) {
        this.os = os.toUpperCase();
    }

    public Date getDataPrev() {
        return dataPrev;
    }

    public void setDataPrev(Date dataPrev) {
        this.dataPrev = dataPrev;
    }

    public SituacaoOs getSituacaoOs() {
        return situacaoOs;
    }

    public void setSituacaoOs(SituacaoOs situacaoOs) {
        this.situacaoOs = situacaoOs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataIn() {
        return dataIn;
    }

    public void setDataIn(Date dataIn) {
        this.dataIn = dataIn;
    }

    public Date getDataFi() {
        return dataFi;
    }

    public void setDataFi(Date dataFi) {
        this.dataFi = dataFi;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public TipoDeManutencao getTipoDeManutencao() {
        return tipoDeManutencao;
    }

    public void setTipoDeManutencao(TipoDeManutencao tipoDeManutencao) {
        this.tipoDeManutencao = tipoDeManutencao;
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
        if (!(object instanceof OrdemDeServico)) {
            return false;
        }
        OrdemDeServico other = (OrdemDeServico) object;
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

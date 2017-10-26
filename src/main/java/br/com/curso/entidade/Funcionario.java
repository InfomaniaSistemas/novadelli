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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "funcionario")
@PrimaryKeyJoinColumn(name = "pes_id")
public class Funcionario extends PessoaFisica implements Serializable {

    @Column
    private String pis;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtAdmicao; 
    @ManyToOne
    @JoinColumn
    private Cargo cargo;

    public Date getDtAdmicao() {
        return dtAdmicao;
    }

    public void setDtAdmicao(Date dtAdmicao) {
        this.dtAdmicao = dtAdmicao;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}

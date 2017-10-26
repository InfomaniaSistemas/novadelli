/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import br.com.caelum.stella.bean.validation.CNPJ;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "pes_juridica")
@PrimaryKeyJoinColumn(name = "pes_id")
public class PessoaJuridica extends Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column
    @CNPJ
    String cnpj;
    @Column
    String ie;
    @Column
    private String razaoSocial = " ".toUpperCase();
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataAbertura;

    public void maiusculo() {
        razaoSocial = razaoSocial.toUpperCase();

    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial.toUpperCase();
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    @Override
    public String getDocumentoFederal() {
        return cnpj;
    }

    @Override
    public String getRazao() {
        return razaoSocial;
    }

    @Override
    public String getDocumentoEstadual() {
        return ie;
    }

}

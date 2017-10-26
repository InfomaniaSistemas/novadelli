/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "pes_fornecedor")
@PrimaryKeyJoinColumn(name = "pes_id")
public class Fornecedor extends PessoaJuridica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column
    private String site;
    @Column
    private String contato = "".toUpperCase();
    @Column
    private String celular;
    @Column
    private String formaPgto = "".toUpperCase();
    @Column
    private String agencia = "".toUpperCase();
    @Column
    private String prazo = "".toUpperCase();
    @Column
    private String banco = "".toUpperCase();
    @Column
    private String conta;
    @Column
    private String titular = "".toUpperCase();
    @Column
    private String ramoAtividade = "".toUpperCase();

   

    public String getPrazo() {
        return prazo;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo.toUpperCase();
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco.toUpperCase();
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato.toUpperCase();
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFormaPgto() {
        return formaPgto;
    }

    public void setFormaPgto(String formaPgto) {
        this.formaPgto = formaPgto.toUpperCase();
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular.toUpperCase();
    }

    public String getRamoAtividade() {
        return ramoAtividade;
    }

    public void setRamoAtividade(String ramoAtividade) {
        this.ramoAtividade = ramoAtividade.toUpperCase();
    }

    @Override
    public String getDocumentoFederal() {
        return cnpj;
    }

    @Override
    public String getDocumentoEstadual() {
        return ie;
    }

}

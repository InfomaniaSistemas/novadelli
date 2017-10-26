/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "restricao")
public class Restricao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String tipo;
    @Column
    private String descricao;
    @JoinColumn
    @ManyToOne
    private Pessoa pessoa;
    @Column
    private Boolean vendaAvista = true;
    @Column
    private Boolean vendaPrazo;
    @Column
    private Boolean permiteVenda;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Boolean getPermiteVenda() {
        return permiteVenda;
    }

    public void setPermiteVenda(Boolean permiteVenda) {
        this.permiteVenda = permiteVenda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getVendaAvista() {
        return vendaAvista;
    }

    public void setVendaAvista(Boolean vendaAvista) {
        this.vendaAvista = vendaAvista;
    }

    public Boolean getVendaPrazo() {
        return vendaPrazo;
    }

    public void setVendaPrazo(Boolean vendaPrazo) {
        this.vendaPrazo = vendaPrazo;
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
        if (!(object instanceof Restricao)) {
            return false;
        }
        Restricao other = (Restricao) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}

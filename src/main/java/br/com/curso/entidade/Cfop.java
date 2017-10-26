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
import javax.persistence.Table;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "Cfop")
public class Cfop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long CFOP;
    @Column 
    private String CFOPNatureza;
    @Column(length = 500)
    private String CFOPDescricao;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (CFOP != null ? CFOP.hashCode() : 0);
        return hash;
    }

    public Long getCFOP() {
        return CFOP;
    }

    public void setCFOP(Long CFOP) {
        this.CFOP = CFOP;
    }

    public String getCFOPNatureza() {
        return CFOPNatureza;
    }

    public void setCFOPNatureza(String CFOPNatureza) {
        this.CFOPNatureza = CFOPNatureza;
    }

    public String getCFOPDescricao() {
        return CFOPDescricao;
    }

    public void setCFOPDescricao(String CFOPDescricao) {
        this.CFOPDescricao = CFOPDescricao;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cfop)) {
            return false;
        }
        Cfop other = (Cfop) object;
        if ((this.CFOP == null && other.CFOP != null) || (this.CFOP != null && !this.CFOP.equals(other.CFOP))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return CFOP.toString();
    }
}

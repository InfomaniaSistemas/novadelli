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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "ajuste_estoque")
public class AjusteEstoque implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aju_id")
    private Long id;
    @Column(name = "aju_data")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataAjuste = new Date();
    @Column(name = "aju_tipo")
    private String tipo = "ENTRADA";
    @Column(name = "aju_qtdatual")
    private BigDecimal qtdAtual;
    @Column(name = "aju_qtdanterior")
    private BigDecimal qtdAnterior;
    @Column(name = "aju_obs")
    private String obs;
//    @ManyToOne
//    @JoinColumn(name = "usu_id", nullable = false)
//    private Usuario usuario;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "prod_id")
    private Produto produto;

    public void maiusculo() {

        obs = obs.toUpperCase();
    }

    public void ajustarEstoque() throws Exception {
        this.qtdAnterior = produto.getEstoque();
        if (tipo.equals("ENTRADA")) {
            BigDecimal novoEstoque = produto.getEstoque().add(qtdAtual);
            produto.setEstoque(novoEstoque);
        } else {
            if (qtdAtual.compareTo(produto.getEstoque()) > 0) {
                throw new Exception("Quantidade insuficiente no estoque");
            }
            BigDecimal novoEstoque = produto.getEstoque().subtract(qtdAtual);
            produto.setEstoque(novoEstoque);

        }
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataAjuste() {
        return dataAjuste;
    }

    public void setDataAjuste(Date dataAjuste) {
        this.dataAjuste = dataAjuste;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getQtdAtual() {
        return qtdAtual;
    }

    public void setQtdAtual(BigDecimal qtdAtual) {
        this.qtdAtual = qtdAtual;
    }

    public BigDecimal getQtdAnterior() {
        return qtdAnterior;
    }

    public void setQtdAnterior(BigDecimal qtdAnterior) {
        this.qtdAnterior = qtdAnterior;
    }
//
//    public Usuario getUsuario() {
//        return usuario;
//    }
//
//    public void setUsuario(Usuario usuario) {
//        this.usuario = usuario;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AjusteEstoque)) {
            return false;
        }
        AjusteEstoque other = (AjusteEstoque) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.SisAgro.entidades.AjusteEstoque[ id=" + id + " ]";
    }

}

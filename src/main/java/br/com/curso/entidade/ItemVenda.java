/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "item_venda")
public class ItemVenda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    @Min(value = 0)
    private BigDecimal quantidade;
    @Column
    @Min(value = 0)
    private BigDecimal subTotalFiscal;
    @Column
    @Min(value = 0)
    private BigDecimal subTotal;
    @Column
    @Min(value = 0)
    private BigDecimal valor;
    @Column
    @Min(value = 0)
    private BigDecimal valorFiscal;
    @ManyToOne
    @JoinColumn
    private Venda venda;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Produto plantio;

    public void estornaEstoque() {

        BigDecimal novoEstoque = plantio.getEstoque().add(quantidade);

        plantio.setEstoque(novoEstoque);

    }

    public void baixaEstoque() throws Exception {
        BigDecimal novoEstoque = plantio.getEstoque().subtract(quantidade);
        if (novoEstoque.compareTo(BigDecimal.ZERO) == 1) {
            plantio.setEstoque(novoEstoque);
        } else {
            throw new Exception("Estoque insuficiente!");

        }
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Produto getPlantio() {
        return plantio;
    }

    public BigDecimal getSubTotalFiscal() {
        return subTotalFiscal;
    }

    public void setSubTotalFiscal(BigDecimal subTotalFiscal) {
        this.subTotalFiscal = subTotalFiscal;
    }

    public BigDecimal getValorFiscal() {
        return valorFiscal;
    }

    public void setValorFiscal(BigDecimal valorFiscal) {
        this.valorFiscal = valorFiscal;
    }

    public void setPlantio(Produto plantio) {
        this.plantio = plantio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plantio != null ? plantio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemVenda)) {
            return false;
        }
        ItemVenda other = (ItemVenda) object;
        if ((this.plantio == null && other.plantio != null)
                || (this.plantio != null && !this.plantio.equals(other.plantio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}

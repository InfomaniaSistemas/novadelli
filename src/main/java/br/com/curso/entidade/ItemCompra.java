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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "item_compra")
public class ItemCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    @Min(value = 0)
    private BigDecimal quantidade;
    @Column
    @Min(value = 0)
    private BigDecimal subTotal;
    @Column
    @Min(value = 0)
    private BigDecimal valor;
    @ManyToOne
    @JoinColumn
    private Compra compra;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Produto produto;
    @Column
    private BigDecimal ipi;
    @Column
    private BigDecimal icms;
    @Column
    private BigDecimal desconto;
    @Column
    private BigDecimal seguro;

    @Column
    private BigDecimal frete;
    @Column
    private BigDecimal st;
    @JoinColumn
    @ManyToOne
    private Cfop cfopE;
    @JoinColumn
    @ManyToOne
    private Cfop cfopS;
    @JoinColumn
    @ManyToOne
    private Ncm ncm;
    private String ncmString;
    private String cfopD;
    private String cfopF;
    @Column
    private BigDecimal custo;
    @Column
    private BigDecimal custoTotal;
    @Column
    private BigDecimal custoTotalUnitario;
    @Column
    private BigDecimal percentualVendaAvista;
    @Column
    private BigDecimal percentualfrete;
    @Column
    private BigDecimal percentualseguro;
    @Column
    private BigDecimal percentualdesconto;
    @Column
    private BigDecimal percentualacessorias;
    @Column
    private BigDecimal valorVendaAvista;
    @Column
    private BigDecimal valorVendaAprazo;
    @Column
    private BigDecimal percentualVendaAprazo;
    @Column
    private BigDecimal despassessorias = BigDecimal.ZERO;
    @Column
    private BigDecimal extras;
    @Column
    private BigDecimal comissao;
    @Column
    private BigDecimal margemLucro;
    @Column
    private BigDecimal margemLucroAprazo;
    @Column
    private BigDecimal lucroAvista;
    @Column
    private BigDecimal lucroAprazo;

    public BigDecimal getIcms() {
        return icms;
    }

    public String getNcmString() {
        return ncmString;
    }

    public void setNcmString(String ncmString) {
        this.ncmString = ncmString;
    }

    public void setIcms(BigDecimal icms) {
        this.icms = icms;
    }

    public BigDecimal getPercentualseguro() {
        return percentualseguro;
    }

    public String getCfopD() {
        return cfopD;
    }

    public void setCfopD(String cfopD) {
        this.cfopD = cfopD;
    }

    public String getCfopF() {
        return cfopF;
    }

    public void setCfopF(String cfopF) {
        this.cfopF = cfopF;
    }

    
    public void setPercentualseguro(BigDecimal percentualseguro) {
        this.percentualseguro = percentualseguro;
    }

    public BigDecimal getPercentualdesconto() {
        return percentualdesconto;
    }

    public void setPercentualdesconto(BigDecimal percentualdesconto) {
        this.percentualdesconto = percentualdesconto;
    }

    public BigDecimal getPercentualacessorias() {
        return percentualacessorias;
    }

    public void setPercentualacessorias(BigDecimal percentualacessorias) {
        this.percentualacessorias = percentualacessorias;
    }

    public void entraEstoque() {

        if (produto.getEstoque() == null) {

            produto.setEstoque(BigDecimal.ZERO);
        }

        BigDecimal novoEstoque = produto.getEstoque().add(quantidade);
        produto.setEstoque(novoEstoque);

    }

    public BigDecimal getCustoTotalUnitario() {
        return custoTotalUnitario;
    }

    public void setCustoTotalUnitario(BigDecimal custoTotalUnitario) {
        this.custoTotalUnitario = custoTotalUnitario;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getSeguro() {
        return seguro;
    }

    public void setSeguro(BigDecimal seguro) {
        this.seguro = seguro;
    }

    public Cfop getCfopE() {
        return cfopE;
    }

    public void setCfopE(Cfop cfopE) {
        this.cfopE = cfopE;
    }

    public Cfop getCfopS() {
        return cfopS;
    }

    public void setCfopS(Cfop cfopS) {
        this.cfopS = cfopS;
    }

    public Ncm getNcm() {
        return ncm;
    }

    public void setNcm(Ncm ncm) {
        this.ncm = ncm;
    }

    public void estornaEstoque() {
        BigDecimal novoEstoque = produto.getEstoque().subtract(quantidade);
        produto.setEstoque(novoEstoque);
    }

    public BigDecimal getPercentualfrete() {
        return percentualfrete;
    }

    public void setPercentualfrete(BigDecimal percentualfrete) {
        this.percentualfrete = percentualfrete;
    }

    public BigDecimal getMargemLucroAprazo() {
        return margemLucroAprazo;
    }

    public void setMargemLucroAprazo(BigDecimal margemLucroAprazo) {
        this.margemLucroAprazo = margemLucroAprazo;
    }

    public BigDecimal getLucroAvista() {
        return lucroAvista;
    }

    public void setLucroAvista(BigDecimal lucroAvista) {
        this.lucroAvista = lucroAvista;
    }

    public BigDecimal getLucroAprazo() {
        return lucroAprazo;
    }

    public void setLucroAprazo(BigDecimal lucroAprazo) {
        this.lucroAprazo = lucroAprazo;
    }

    public BigDecimal getValorVendaAvista() {
        return valorVendaAvista;
    }

    public void setValorVendaAvista(BigDecimal valorVendaAvista) {
        this.valorVendaAvista = valorVendaAvista;
    }

    public BigDecimal getValorVendaAprazo() {
        return valorVendaAprazo;
    }

    public void setValorVendaAprazo(BigDecimal valorVendaAprazo) {
        this.valorVendaAprazo = valorVendaAprazo;
    }

    public BigDecimal getPercentualVendaAvista() {
        return percentualVendaAvista;
    }

    public void setPercentualVendaAvista(BigDecimal percentualVendaAvista) {
        this.percentualVendaAvista = percentualVendaAvista;
    }

    public BigDecimal getPercentualVendaAprazo() {
        return percentualVendaAprazo;
    }

    public void setPercentualVendaAprazo(BigDecimal percentualVendaAprazo) {
        this.percentualVendaAprazo = percentualVendaAprazo;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public BigDecimal getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(BigDecimal margemLucro) {
        this.margemLucro = margemLucro;
    }

    public BigDecimal getDespassessorias() {
        return despassessorias;
    }

    public void setDespassessorias(BigDecimal despassessorias) {
        this.despassessorias = despassessorias;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getIpi() {
        return ipi;
    }

    public void setIpi(BigDecimal ipi) {
        this.ipi = ipi;
    }

    public BigDecimal getFrete() {
        return frete;
    }

    public void setFrete(BigDecimal frete) {
        this.frete = frete;
    }

    public BigDecimal getSt() {
        return st;
    }

    public void setSt(BigDecimal st) {
        this.st = st;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getExtras() {
        return extras;
    }

    public void setExtras(BigDecimal extras) {
        this.extras = extras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (produto != null ? produto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemCompra)) {
            return false;
        }
        ItemCompra other = (ItemCompra) object;
        if ((this.produto == null && other.produto != null)
                || (this.produto != null && !this.produto.equals(other.produto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}

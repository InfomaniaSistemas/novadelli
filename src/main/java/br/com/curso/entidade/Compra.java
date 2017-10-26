/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Min;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "compra")
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataEntrada = new Date();

    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataNota;
    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dtEntradaSaida;

    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataVencimento = new Date();
    @Column
    private BigDecimal totalProdutos = BigDecimal.ZERO;
    @Column
    private BigDecimal total = BigDecimal.ZERO;
    @Column
    private BigDecimal totalFrete = BigDecimal.ZERO;
    @Column
    private BigDecimal totalDesconto = BigDecimal.ZERO;
    @Column
    private BigDecimal acrescimo = BigDecimal.ZERO;
    @Column
    private BigDecimal totalSeguro = BigDecimal.ZERO;
    @Column
    private BigDecimal totalNota = BigDecimal.ZERO;
    @Column
    private BigDecimal totalIcms = BigDecimal.ZERO;
    @Column
    private BigDecimal totalDesc = BigDecimal.ZERO;
    @Column
    private BigDecimal totalDespesasAssessorias = BigDecimal.ZERO;
    @Column
    private BigDecimal desconto = BigDecimal.ZERO;
    @Column
    private BigDecimal valorDesconto = BigDecimal.ZERO;
    @Column
    private String valida;

    @Column
    @Min(value = 1)
    private Integer qtdParcela = 1;
    @Column
    private String tipoPagamento = "A VISTA";
    @Column
    private String situacao;

    @Column
    private String tipoDocumento;

    private String documento;

    @Column
    private String numDoc;
    @Column
    private String numDocValida;
    @Column
    private String numPedido;
    @Column
    private String serie;

    @ManyToOne
    @JoinColumn(name = "trans_id")
    private Transportadora transportadora;

    @Column(length = 600)
    private String obs = "".toUpperCase();
    @ManyToOne
    @JoinColumn
    private Fornecedor fornecedor;
    @ManyToOne
    @JoinColumn
    private Empresa empresa;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(BigDecimal acrescimo) {
        this.acrescimo = acrescimo;
    }

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "compra")
    private List<ItemCompra> itensCompra = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "compra",
            orphanRemoval = true)
    private List<Parcela> parcelas;

    public void addItem(ItemCompra item) throws Exception {

        item.setCompra(this);
        if (!itensCompra.contains(item)) {
            itensCompra.add(item);
            calculaTotal1();
        } else {
            throw new Exception("O Item já foi adicionado");
        }
//        item.entraEstoque();

    }

    public void addItem1(ItemCompra item) throws Exception {

        item.setCompra(this);
        if (!itensCompra.contains(item)) {
            itensCompra.add(item);
        } else {
            throw new Exception("O Item já foi adicionado");
        }
        item.setValor(item.getProduto().getValorVendaPrazo());
//        item.entraEstoque();
        calculaTotal1();

    }

    public void removeItem(ItemCompra item) throws Exception {
        if (itensCompra.contains(item)) {
            itensCompra.remove(item);

            calculaTotal1();
//            item.estornaEstoque();
        } else {
            throw new Exception("O item selecionado não contem na lista de itens");
        }
    }

    public void recalcularParcela() {

        Integer par = qtdParcela;
        par = par - 1;
        BigDecimal retotal = BigDecimal.ZERO;
        BigDecimal ultimaParcela = BigDecimal.ZERO;

        for (int i = 0; i < qtdParcela; i++) {
            System.out.println("Estou recalculando");
            if (par.equals(i)) {
                System.out.println("Ultima parcela");
                ultimaParcela = totalDesc.subtract(retotal);
                parcelas.get(i).setValor(ultimaParcela);
                System.out.println("Ultima parcela" + ultimaParcela);
            } else {

                retotal = retotal.add(parcelas.get(i).getValor());
                System.out.println(retotal);
                System.out.println(i);

            }

        }

    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void removeItemPacela() throws Exception {

        for (int i = 0; i < parcelas.size(); i++) {
            parcelas.remove(i);

        }

    }

    public BigDecimal getTotalDesconto() {
        return totalDesconto;
    }

    public void setTotalDesconto(BigDecimal totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    public BigDecimal getTotalSeguro() {
        return totalSeguro;
    }

    public void setTotalSeguro(BigDecimal totalSeguro) {
        this.totalSeguro = totalSeguro;
    }

    public BigDecimal getTotalIcms() {
        return totalIcms;
    }

    public void setTotalIcms(BigDecimal totalIcms) {
        this.totalIcms = totalIcms;
    }

    public void gerarParcelas() {
        Integer qtdParcela2;
        parcelas = new ArrayList<>();
        BigDecimal valorParcela
                = totalDesc.divide(new BigDecimal(qtdParcela.toString()), 2, RoundingMode.HALF_DOWN);
        for (int i = 1; i <= qtdParcela; i++) {
            Parcela parcela = new Parcela();
            parcela.setCompra(this);
            System.out.println("Estou aqui1");
            if (qtdParcela.equals(1)) {
                qtdParcela2 = qtdParcela - 0;
            } else {

                qtdParcela2 = qtdParcela - 1;
            }

            if (qtdParcela2.equals(parcelas.size())) {
                System.out.println("Estou aqui");
                System.out.println(valorParcela);
                valorParcela = valorParcela.multiply(new BigDecimal(qtdParcela2.toString()));
                System.out.println(valorParcela);
                valorParcela = totalDesc.subtract(valorParcela);

                System.out.println(valorParcela);
                parcela.setValor(valorParcela);

            } else {

                parcela.setValor(valorParcela);

            }

            Calendar vencimento = Calendar.getInstance();
            vencimento.add(Calendar.DATE, 30 * i);
            parcela.setVencimento(vencimento.getTime());
            parcelas.add(parcela);
        }
    }

    public void calculaDesconto() {

        totalDesc = BigDecimal.ZERO;
        if (desconto == null) {
            desconto = BigDecimal.ZERO;
            totalDesc = totalNota;

        } else {

            valorDesconto = BigDecimal.ZERO;
            totalDesc = totalNota;

            valorDesconto = totalDesc.multiply(desconto.divide(new BigDecimal("100")));
            totalDesc = totalDesc.subtract(valorDesconto);
        }
    }

    public void calculaTotal1() {

        total = BigDecimal.ZERO;
        totalNota = BigDecimal.ZERO;
        totalDesc = BigDecimal.ZERO;

        for (ItemCompra i : itensCompra) {

            total = total.add(i.getSubTotal());
            totalNota = totalNota.add(i.getCustoTotal());
            totalDesc = totalNota;

        }
        totalDesc = totalDesc.add(acrescimo);

    }

    public BigDecimal getTotalDespesasAssessorias() {
        return totalDespesasAssessorias;
    }

    public void setTotalDespesasAssessorias(BigDecimal totalDespesasAssessorias) {
        this.totalDespesasAssessorias = totalDespesasAssessorias;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getTotalProdutos() {
        return totalProdutos;
    }

    public void setTotalProdutos(BigDecimal totalProdutos) {
        this.totalProdutos = totalProdutos;
    }

    public Date getDataNota() {
        return dataNota;
    }

    public void setDataNota(Date dataNota) {
        this.dataNota = dataNota;
    }

    public Date getDtEntradaSaida() {
        return dtEntradaSaida;
    }

    public void setDtEntradaSaida(Date dtEntradaSaida) {
        this.dtEntradaSaida = dtEntradaSaida;
    }

    public BigDecimal getTotalFrete() {
        return totalFrete;
    }

    public void setTotalFrete(BigDecimal totalFrete) {
        this.totalFrete = totalFrete;
    }

    public BigDecimal getTotalNota() {
        return totalNota;
    }

    public void setTotalNota(BigDecimal totalNota) {
        this.totalNota = totalNota;
    }

    public String getValida() {
        return valida;
    }

    public void setValida(String valida) {
        this.valida = valida;
    }

    public BigDecimal getTotalDesc() {
        return totalDesc;
    }

    public void setTotalDesc(BigDecimal totalDesc) {
        this.totalDesc = totalDesc;
    }

    public String getObs() {
        return obs.toUpperCase();
    }

    public void setObs(String obs) {
        this.obs = obs.toUpperCase();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Long getId() {
        return id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public List<ItemCompra> getItensCompra() {
        return itensCompra;
    }

    public void setItensCompra(List<ItemCompra> itensCompra) {
        this.itensCompra = itensCompra;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public Integer getQtdParcela() {
        return qtdParcela;
    }

    public void setQtdParcela(Integer qtdParcela) {
        this.qtdParcela = qtdParcela;
    }

    public String getNumDocValida() {
        return numDocValida;
    }

    public void setNumDocValida(String numDocValida) {
        this.numDocValida = numDocValida;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
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
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
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

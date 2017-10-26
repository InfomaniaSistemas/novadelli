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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "contrato")
public class Contrato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private String motivo;
    private String categoria;
    @ManyToOne
    private Pessoa pessoa;
    private BigDecimal custo;
    private String pagamentoCusto;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimentoCusto;
    private BigDecimal totalCusto;
    private BigDecimal qtdParcelaCusto;
    private BigDecimal valorparcela;
    private BigDecimal servico;
    private String pagamentoServico;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimentoServico;
    private BigDecimal taxaDetran;
    private BigDecimal taxaDetran2;
    private String pagamentoTaxaDetran;
    private String pagamentoTaxaDetran2;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimentoTaxaDetran;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimentoTaxaDetran2;
    private BigDecimal teoricas;
    private String pagamentoTeoricas;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimentoTeoricas;
    private BigDecimal praticas;
    private String pagamentoPraticas;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimentoPraticas;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtEmissao;
    private BigDecimal desconto = BigDecimal.ZERO;
    private Integer qtdParcela = 1;
    private BigDecimal total;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "contrato",
            orphanRemoval = true)
    private List<Parcela> parcelas;

    public void ma() {
        motivo = motivo.toUpperCase();
        categoria = categoria.toUpperCase();

    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getQtdParcela() {
        return qtdParcela;
    }

    public void setQtdParcela(Integer qtdParcela) {
        this.qtdParcela = qtdParcela;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public void removeItemPacela() throws Exception {

        for (int i = 0; i < parcelas.size(); i++) {
            parcelas.remove(i);

        }

    }

    public void gerarParcelas() {
        Integer qtdParcela2;
        parcelas = new ArrayList<>();
        BigDecimal valorParcela
                = total.divide(new BigDecimal(qtdParcela.toString()), 2, RoundingMode.HALF_DOWN);
        for (int i = 1; i <= qtdParcela; i++) {
            Parcela parcela = new Parcela();
            parcela.setContrato(this);
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
                valorParcela = total.subtract(valorParcela);

                System.out.println(valorParcela);
                parcela.setValor(valorParcela);

            } else {

                parcela.setValor(valorParcela);

            }

            Calendar vencimento = Calendar.getInstance();
            vencimento.add(Calendar.DATE, 30 * i);
            parcela.setVencimento(vencimento.getTime());
            parcela.setDoc("A VISTA");
            parcelas.add(parcela);
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
                ultimaParcela = total.subtract(retotal);
                parcelas.get(i).setValor(ultimaParcela);
                System.out.println("Ultima parcela" + ultimaParcela);
            } else {

                retotal = retotal.add(parcelas.get(i).getValor());
                System.out.println(retotal);
                System.out.println(i);

            }

        }

    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public void calculaTotal() {

        total = taxaDetran.add(taxaDetran2).add(servico).add(teoricas).add(praticas).subtract(desconto);

    }

    public String getPagamentoTaxaDetran2() {
        return pagamentoTaxaDetran2;
    }

    public void setPagamentoTaxaDetran2(String pagamentoTaxaDetran2) {
        this.pagamentoTaxaDetran2 = pagamentoTaxaDetran2;
    }

    public Date getVencimentoTaxaDetran2() {
        return vencimentoTaxaDetran2;
    }

    public void setVencimentoTaxaDetran2(Date vencimentoTaxaDetran2) {
        this.vencimentoTaxaDetran2 = vencimentoTaxaDetran2;
    }

    public BigDecimal getTaxaDetran2() {
        return taxaDetran2;
    }

    public void setTaxaDetran2(BigDecimal taxaDetran2) {
        this.taxaDetran2 = taxaDetran2;
    }

    public BigDecimal getTotalCusto() {
        return totalCusto;
    }

    public void setTotalCusto(BigDecimal totalCusto) {
        this.totalCusto = totalCusto;
    }

    public BigDecimal getQtdParcelaCusto() {
        return qtdParcelaCusto;
    }

    public void setQtdParcelaCusto(BigDecimal qtdParcelaCusto) {
        this.qtdParcelaCusto = qtdParcelaCusto;
    }

    public BigDecimal getValorparcela() {
        return valorparcela;
    }

    public void setValorparcela(BigDecimal valorparcela) {
        this.valorparcela = valorparcela;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public String getPagamentoCusto() {
        return pagamentoCusto;
    }

    public void setPagamentoCusto(String pagamentoCusto) {
        this.pagamentoCusto = pagamentoCusto;
    }

    public Date getVencimentoCusto() {
        return vencimentoCusto;
    }

    public void setVencimentoCusto(Date vencimentoCusto) {
        this.vencimentoCusto = vencimentoCusto;
    }

    public BigDecimal getServico() {
        return servico;
    }

    public void setServico(BigDecimal servico) {
        this.servico = servico;
    }

    public String getPagamentoServico() {
        return pagamentoServico;
    }

    public void setPagamentoServico(String pagamentoServico) {
        this.pagamentoServico = pagamentoServico;
    }

    public Date getVencimentoServico() {
        return vencimentoServico;
    }

    public void setVencimentoServico(Date vencimentoServico) {
        this.vencimentoServico = vencimentoServico;
    }

    public BigDecimal getTaxaDetran() {
        return taxaDetran;
    }

    public void setTaxaDetran(BigDecimal taxaDetran) {
        this.taxaDetran = taxaDetran;
    }

    public String getPagamentoTaxaDetran() {
        return pagamentoTaxaDetran;
    }

    public void setPagamentoTaxaDetran(String pagamentoTaxaDetran) {
        this.pagamentoTaxaDetran = pagamentoTaxaDetran;
    }

    public Date getVencimentoTaxaDetran() {
        return vencimentoTaxaDetran;
    }

    public void setVencimentoTaxaDetran(Date vencimentoTaxaDetran) {
        this.vencimentoTaxaDetran = vencimentoTaxaDetran;
    }

    public BigDecimal getTeoricas() {
        return teoricas;
    }

    public void setTeoricas(BigDecimal teoricas) {
        this.teoricas = teoricas;
    }

    public String getPagamentoTeoricas() {
        return pagamentoTeoricas;
    }

    public void setPagamentoTeoricas(String pagamentoTeoricas) {
        this.pagamentoTeoricas = pagamentoTeoricas;
    }

    public Date getVencimentoTeoricas() {
        return vencimentoTeoricas;
    }

    public void setVencimentoTeoricas(Date vencimentoTeoricas) {
        this.vencimentoTeoricas = vencimentoTeoricas;
    }

    public BigDecimal getPraticas() {
        return praticas;
    }

    public void setPraticas(BigDecimal praticas) {
        this.praticas = praticas;
    }

    public String getPagamentoPraticas() {
        return pagamentoPraticas;
    }

    public void setPagamentoPraticas(String pagamentoPraticas) {
        this.pagamentoPraticas = pagamentoPraticas;
    }

    public Date getVencimentoPraticas() {
        return vencimentoPraticas;
    }

    public void setVencimentoPraticas(Date vencimentoPraticas) {
        this.vencimentoPraticas = vencimentoPraticas;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Contrato)) {
            return false;
        }
        Contrato other = (Contrato) object;
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

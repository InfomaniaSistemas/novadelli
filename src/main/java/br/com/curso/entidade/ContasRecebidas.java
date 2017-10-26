/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Temporal;
import javax.validation.constraints.Min;

/**
 *
 * @author Miguel Gustavo
 */
@Entity
public class ContasRecebidas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    private Pessoa pessoa;
    @ManyToOne
    private Banco banco;
    @ManyToOne
    private Bandeira bandeira;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column
    private Date dtMovimento;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column
    private Date dtVencimento;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column
    private Date dtVencimentoFatura;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column
    private Date dtaux;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column
    private Date dtaux2;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column
    private Date dtPagamento;
    @Column
    private BigDecimal valor;
    @Column
    @Min(value = 1)
    private Integer qtdParcela = 1;

    @Column
    private BigDecimal valorPrincipal;
    @Column
    private BigDecimal valorAPagar;
    private BigDecimal ultimoPagamento;
    @Column
    private BigDecimal desconto = BigDecimal.ZERO;
    @Column
    private BigDecimal valorPago;
    @Column
    private BigDecimal restosAPagar;
    @Column
    private String status;
    @Column
    private String tipoDocumento;
    @Column
    private String numeroDoc;

    @Column
    private BigDecimal juros = BigDecimal.ZERO;
    @Column(length = 300)
    private String descricao = "".toUpperCase();
    private String turma;

    @ManyToOne
    private Venda venda;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "contasReceber",
            orphanRemoval = true)
    private List<Parcela> parcelas;

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Bandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeira bandeira) {
        this.bandeira = bandeira;
    }

    public Date getDtVencimentoFatura() {
        return dtVencimentoFatura;
    }

    public void setDtVencimentoFatura(Date dtVencimentoFatura) {
        this.dtVencimentoFatura = dtVencimentoFatura;
    }

    public BigDecimal getUltimoPagamento() {
        return ultimoPagamento;
    }

    public void setUltimoPagamento(BigDecimal ultimoPagamento) {
        this.ultimoPagamento = ultimoPagamento;
    }

    public Integer getQtdParcela() {
        return qtdParcela;

    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
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

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getDtaux2() {
        return dtaux2;
    }

    public void setDtaux2(Date dtaux2) {
        this.dtaux2 = dtaux2;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public Date getDtaux() {
        return dtaux;
    }

    public void setDtaux(Date dtaux) {
        this.dtaux = dtaux;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getValorPrincipal() {
        return valorPrincipal;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public void setValorPrincipal(BigDecimal valorPrincipal) {
        this.valorPrincipal = valorPrincipal;
    }

    public BigDecimal getRestosAPagar() {
        return restosAPagar;
    }

    public void setRestosAPagar(BigDecimal restosAPagar) {
        this.restosAPagar = restosAPagar;
    }

    public void validaP() {

        System.out.println(restosAPagar);

        if (restosAPagar.compareTo(BigDecimal.ZERO) == 0) {

            status = "RECEBIDO";
            ultimoPagamento = valorPago;
        } else if (restosAPagar.compareTo(BigDecimal.ZERO) == 1) {
            status = "RESTOS A PAGAR";
            BigDecimal aux;

            aux = valorPago;

            valorPago = valorPago.add(ultimoPagamento);

            ultimoPagamento = aux;

            valorAPagar = valorPrincipal.subtract(valorPago);
        }

    }

    public void calculaTotal() {

        restosAPagar = restosAPagar.add(juros).subtract(desconto);
        valor = valor.add(juros).subtract(desconto);

    }
//     

    public void restos() {

        restosAPagar = BigDecimal.ZERO;
        restosAPagar = valor.subtract(valorPago);

    }

    public BigDecimal getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(BigDecimal valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public String getDescricao() {
        return descricao.toUpperCase();
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.toUpperCase();
    }

    public Date getDtMovimento() {
        return dtMovimento;
    }

    public void setDtMovimento(Date dtMovimento) {
        this.dtMovimento = dtMovimento;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Date getDtPagamento() {
        return dtPagamento;
    }

    public void setDtPagamento(Date dtPagamento) {
        this.dtPagamento = dtPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getJuros() {
        return juros;
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
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
        if (!(object instanceof ContasRecebidas)) {
            return false;
        }
        ContasRecebidas other = (ContasRecebidas) object;
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

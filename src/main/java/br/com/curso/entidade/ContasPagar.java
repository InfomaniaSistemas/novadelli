/*
 * To change this template, choose Tools | Templates
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
public class ContasPagar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id

    @Column
    private Long id;
    private BigDecimal valor1;
    private BigDecimal valor2;
    private Boolean extorno = false;
    @ManyToOne
    private Pessoa pessoa;

    @ManyToOne
    private Banco banco;
    @ManyToOne
    private Bandeira bandeira;
    @ManyToOne
    private Empresa empresa;
    @ManyToOne
    private Diaria diaria;
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
    private Date dtPagamento;
    @Column
    private BigDecimal valor;
    @Column
    private String tipoDocumento;
    @Column
    private String numDoc;
    @Column
    private BigDecimal valorPrincipal;
    @Column
    private BigDecimal ultimoPagamento;
    @Column
    private BigDecimal valorAPagar;
    @Column
    private BigDecimal valorPago;
    @Column
    @Min(value = 1)
    private Integer parcela;
    @Column
    private BigDecimal restosAPagar;
    @Column
    private String status;
    @Column
    private BigDecimal juros;
    @Column
    private BigDecimal desconto;
    @Column(length = 300)
    private String descricao = "".toUpperCase();
    @ManyToOne
    private Compra compra;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "contasPagar",
            orphanRemoval = true)
    private List<Parcela> parcelas;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public BigDecimal getValor1() {
        return valor1;
    }

    public void setValor1(BigDecimal valor1) {
        this.valor1 = valor1;
    }

    public BigDecimal getValor2() {
        return valor2;
    }

    public void setValor2(BigDecimal valor2) {
        this.valor2 = valor2;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public Bandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeira bandeira) {
        this.bandeira = bandeira;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public BigDecimal getUltimoPagamento() {
        return ultimoPagamento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void setUltimoPagamento(BigDecimal ultimoPagamento) {
        this.ultimoPagamento = ultimoPagamento;
    }

    public Date getDtVencimentoFatura() {
        return dtVencimentoFatura;
    }

    public void setDtVencimentoFatura(Date dtVencimentoFatura) {
        this.dtVencimentoFatura = dtVencimentoFatura;
    }

    public void gerarParcelas() {
        Integer qtdParcela2;
        parcelas = new ArrayList<>();
        BigDecimal valorParcela
                = valorPrincipal.divide(new BigDecimal(parcela.toString()), 2, RoundingMode.HALF_DOWN);
        for (int i = 1; i <= parcela; i++) {
            Parcela parcela1 = new Parcela();
            parcela1.setContasPagar(this);
            System.out.println("Estou aqui1");
            if (parcela.equals(1)) {
                qtdParcela2 = parcela - 0;
            } else {

                qtdParcela2 = parcela - 1;
            }

            if (qtdParcela2.equals(parcelas.size())) {
                System.out.println("Estou aqui");
                System.out.println(valorParcela);
                valorParcela = valorParcela.multiply(new BigDecimal(qtdParcela2.toString()));
                System.out.println(valorParcela);
                valorParcela = valorPrincipal.subtract(valorParcela);

                System.out.println(valorParcela);
                parcela1.setValor(valorParcela);

            } else {

                parcela1.setValor(valorParcela);

            }

            Calendar vencimento = Calendar.getInstance();
            vencimento.add(Calendar.DATE, 30 * i);
            parcela1.setVencimento(vencimento.getTime());
            parcelas.add(parcela1);
        }
    }

    public Boolean getExtorno() {
        return extorno;
    }

    public void setExtorno(Boolean extorno) {
        this.extorno = extorno;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public Integer getParcela() {
        return parcela;
    }

    public void setParcela(Integer parcela) {
        this.parcela = parcela;
    }

    public Diaria getDiaria() {
        return diaria;
    }

    public void setDiaria(Diaria diaria) {
        this.diaria = diaria;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public BigDecimal getValorPrincipal() {
        return valorPrincipal;
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

            status = "PAGO";
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
        if (!(object instanceof ContasPagar)) {
            return false;
        }
        ContasPagar other = (ContasPagar) object;
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

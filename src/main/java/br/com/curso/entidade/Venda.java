/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import br.com.caelum.stella.bean.validation.CNPJ;
import br.com.caelum.stella.bean.validation.CPF;
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
@Table(name = "venda")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtVenda = new Date();
    @Column
    private String nomeFun;
    @Column
    @CPF
    private String cpfNota;
    @Column
    @CNPJ
    private String cnpjNota;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento = new Date();
    @Column
    private BigDecimal total = BigDecimal.ZERO;
    @Column
    private BigDecimal totalFiscal = BigDecimal.ZERO;
    @Column
    private Boolean fiscal;
    @Column
    private BigDecimal entrada = BigDecimal.ZERO;
    @Column
    private BigDecimal totalDesc = BigDecimal.ZERO;
    @Column
    private BigDecimal totalDescFiscal = BigDecimal.ZERO;
    @Column
    private BigDecimal totalDescEn = BigDecimal.ZERO;
    @Column
    private BigDecimal totalDescEnFiscal = BigDecimal.ZERO;
    @Column
    private BigDecimal acrescimo = BigDecimal.ZERO;
    @Column
    private BigDecimal desconto = BigDecimal.ZERO;
    @Column
    private BigDecimal valorDesconto = BigDecimal.ZERO;
    @Column
    private Boolean venda = Boolean.FALSE;
    @Column
    private String valida;
    @Column(columnDefinition = "TEXT")
    private String xml;
    private Boolean naoTransmitida;
    @ManyToOne
    private Bandeira bandeira;
    @Column
    private String numeroDoc;
    @Column
    @Min(value = 1)
    private Integer qtdParcela = 1;
    @Column
    private String tipoPagamento = "A VISTA";
    @Column(length = 600)
    private String obs = "".toUpperCase();
    private String tipoDoc;
    @ManyToOne
    @JoinColumn
    private Pessoa pessoa;
    @ManyToOne
    @JoinColumn
    private Funcionario funcionario;
    @ManyToOne
    @JoinColumn
    private Empresa empresa;
    @ManyToOne
    @JoinColumn
    private Banco banco;
    Calendar dataabertura;
    Calendar datafechamento;
    Calendar tempo;

    public Calendar getDataabertura() {
        return dataabertura;
    }

    public void setDataabertura(Calendar dataabertura) {
        this.dataabertura = dataabertura;
    }

    public Calendar getDatafechamento() {
        return datafechamento;
    }

    public void setDatafechamento(Calendar datafechamento) {
        this.datafechamento = datafechamento;
    }

    public Calendar getTempo() {
        return tempo;
    }

    public void setTempo(Calendar tempo) {
        this.tempo = tempo;
    }



    
    
    
    
    private String modeloNota;

  

  
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "venda")
    private List<ItemVenda> itensVenda = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "venda",
            orphanRemoval = true)
    private List<Parcela> parcelas;

    public void addItem(ItemVenda item) throws Exception {
        BigDecimal estoqueProd = item.getPlantio().getEstoque();
        if (item.getPlantio() == null) {

            throw new Exception("É necessário adicionar um item!");
        } else if (item.getValor() == null) {
            throw new Exception("É Informar o valor unitário do produto!");

        } else if (item.getQuantidade() == null) {
            throw new Exception("É necessário iformar uma quantidade para o item!");
        } else if (itensVenda.contains(item)) {

            throw new Exception("O Item já foi adicionado");

        } else {

            itensVenda.add(item);

            item.getPlantio().setDtUltimaVenda(dtVenda);
            item.setVenda(this);
            calculaTotal();
        }

    }

    public String getModeloNota() {
        return modeloNota;
    }

    public void setModeloNota(String modeloNota) {
        this.modeloNota = modeloNota;
    }

    public void removeItem(ItemVenda item) throws Exception {
        if (itensVenda.contains(item)) {
            itensVenda.remove(item);
//            item.estornaEstoque();
            calculaTotal();
//            calculaTotalFiscal();
        } else {
            throw new Exception("O item selecionado não contem na lista de itens");
        }
    }

    public Boolean getNaoTransmitida() {
        return naoTransmitida;
    }

    public void setNaoTransmitida(Boolean naoTransmitida) {
        this.naoTransmitida = naoTransmitida;
    }

    public String getXml() {
        return xml;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getCpfNota() {
        return cpfNota;
    }

    public void setCpfNota(String cpfNota) {
        this.cpfNota = cpfNota;
    }

    public void removeItemPacela() throws Exception {

        for (int i = 0; i < parcelas.size(); i++) {
            parcelas.remove(i);

        }

    }

    public String getCnpjNota() {
        return cnpjNota;
    }

    public void setCnpjNota(String cnpjNota) {
        this.cnpjNota = cnpjNota;
    }

    public void gerarParcelas() {
        Integer qtdParcela2;
        parcelas = new ArrayList<>();
        BigDecimal valorParcela
                = totalDescEn.divide(new BigDecimal(qtdParcela.toString()), 2, RoundingMode.HALF_DOWN);
        for (int i = 1; i <= qtdParcela; i++) {
            Parcela parcela = new Parcela();
            parcela.setVenda(this);
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
                valorParcela = totalDescEn.subtract(valorParcela);

                System.out.println(valorParcela);
                parcela.setValor(valorParcela);

            } else {

                parcela.setValor(valorParcela);

            }

            if (tipoDoc.equals("CARTÃO DÉBITO")) {

                Calendar vencimento = Calendar.getInstance();
                vencimento.add(Calendar.DATE, 1 * i);
                parcela.setVencimento(vencimento.getTime());
                parcelas.add(parcela);

            } else {
                Calendar vencimento = Calendar.getInstance();
                vencimento.add(Calendar.DATE, 30 * i);
                parcela.setVencimento(vencimento.getTime());
                parcelas.add(parcela);
            }
        }
    }

    public Boolean getFiscal() {
        return fiscal;
    }

    public void setFiscal(Boolean fiscal) {
        this.fiscal = fiscal;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getNomeFun() {
        return nomeFun;
    }

    public void setNomeFun(String nomeFun) {
        this.nomeFun = nomeFun;
    }

    public Bandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeira bandeira) {
        this.bandeira = bandeira;
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
                ultimaParcela = totalDescEn.subtract(retotal);
                parcelas.get(i).setValor(ultimaParcela);
                System.out.println("Ultima parcela" + ultimaParcela);
            } else {

                retotal = retotal.add(parcelas.get(i).getValor());
                System.out.println(retotal);
                System.out.println(i);

            }

        }

    }

    public void calculaDescontoInverso() {

        if (desconto == null) {
            desconto = BigDecimal.ZERO;
        }

        desconto = total.subtract(totalDesc);
        desconto = desconto.multiply(new BigDecimal("100"));
        desconto = desconto.divide(total, 6, RoundingMode.UP);
        totalDescEn = totalDesc;
    }

    public void calculaTotal() {
        if (desconto == null) {
            desconto = BigDecimal.ZERO;
        }
        total = BigDecimal.ZERO;
        for (ItemVenda i : itensVenda) {
            i.setSubTotal(i.getValor().multiply(i.getQuantidade()));
            total = total.add(i.getSubTotal());
            totalDesc = total;
        }

        valorDesconto = total.multiply(desconto.divide(new BigDecimal("100")));
        totalDesc = total.subtract(valorDesconto);

        if (entrada == BigDecimal.ZERO) {

            totalDescEn = totalDesc;
        } else {

            totalDescEn = totalDesc.subtract(entrada);

        }

        if (tipoPagamento.equals("A PRAZO")) {

            gerarParcelas();
        }
    }

    public void calculaTotalFiscal() {
        if (desconto == null) {
            desconto = BigDecimal.ZERO;
        }
        totalFiscal = BigDecimal.ZERO;
        for (ItemVenda i : itensVenda) {
            i.setSubTotalFiscal(i.getValorFiscal().multiply(i.getQuantidade()));
            totalFiscal = totalFiscal.add(i.getSubTotalFiscal());
            totalDescFiscal = totalFiscal;
        }

        valorDesconto = desconto;
        totalDescFiscal = totalFiscal.subtract(valorDesconto);
        if (entrada == BigDecimal.ZERO) {

            totalDescEnFiscal = totalDescFiscal;
        } else {

            totalDescEnFiscal = totalDescFiscal.subtract(entrada);

        }
    }

    public BigDecimal getTotalFiscal() {
        return totalFiscal;
    }

    public void setTotalFiscal(BigDecimal totalFiscal) {
        this.totalFiscal = totalFiscal;
    }

    public BigDecimal getTotalDescFiscal() {
        return totalDescFiscal;
    }

    public void setTotalDescFiscal(BigDecimal totalDescFiscal) {
        this.totalDescFiscal = totalDescFiscal;
    }

    public BigDecimal getTotalDescEnFiscal() {
        return totalDescEnFiscal;
    }

    public void setTotalDescEnFiscal(BigDecimal totalDescEnFiscal) {
        this.totalDescEnFiscal = totalDescEnFiscal;
    }

    public void calculaTotalEditado() {
        if (desconto == null) {
            desconto = BigDecimal.ZERO;
        }
        total = BigDecimal.ZERO;
        for (ItemVenda i : itensVenda) {
            i.setSubTotal(i.getValor().multiply(i.getQuantidade()));
            total = total.add(i.getSubTotal());
            totalDesc = total;
        }

        valorDesconto = desconto;
        totalDesc = total.subtract(valorDesconto);
        totalDesc = totalDesc.add(acrescimo);
        totalDesc = totalDesc.subtract(entrada);
    }

    public BigDecimal getTotalDescEn() {
        return totalDescEn;
    }

    public void setTotalDescEn(BigDecimal totalDescEn) {
        this.totalDescEn = totalDescEn;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public Boolean getVenda() {
        return venda;
    }

    public BigDecimal getEntrada() {
        return entrada;
    }

    public void setEntrada(BigDecimal entrada) {
        this.entrada = entrada;
    }

    public void setVenda(Boolean venda) {
        this.venda = venda;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getValida() {
        return valida;
    }

    public void setValida(String valida) {
        this.valida = valida;
    }

    public BigDecimal getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(BigDecimal acrescimo) {
        this.acrescimo = acrescimo;
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

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public List<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(List<ItemVenda> itensVenda) {
        this.itensVenda = itensVenda;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDtVenda() {
        return dtVenda;
    }

    public void setDtVenda(Date dtVenda) {
        this.dtVenda = dtVenda;
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

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
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
        if (!(object instanceof Venda)) {
            return false;
        }
        Venda other = (Venda) object;
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

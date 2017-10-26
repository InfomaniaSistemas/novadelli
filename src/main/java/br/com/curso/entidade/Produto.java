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

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "produto")
public class Produto implements Serializable {
      
    private static final long serialVersionUID = 1L;
    @Id

    @Column
    private Long id;
    @Column
    private String idstring;
    @Column
    private String nome;
    @Column
    private String nomeCodBarras;
    @Column
    private String marcaString;
    @Column
    private String fornecedorString;
    @Column
    private String modelo;
    @Column
    private String tamanho;
    @Column
    private String letra;
    @Column
    private String numeroPosicao;
    @ManyToOne
    private Marca marca;
    @ManyToOne
    private GrupoProduto grupo;
    @ManyToOne
    private Fornecedor ultimoFornecedor;
    @ManyToOne
    private UnidadeMedida unidadeMedida;
    @ManyToOne
    private AjusteNcm ajusteNcm;
    @Column
    private BigDecimal estoque = BigDecimal.ZERO;
    @Column
    private BigDecimal emCondicional = BigDecimal.ZERO;
    @Column
    private BigDecimal estoqueLoja = BigDecimal.ZERO;
    @Column
    private BigDecimal estoqueMin;
    @Column
    private String codFornecedor;
    @Column
    private String codBarras;
    @Column
    private BigDecimal valorCusto;
    @Column
    private BigDecimal ipi;
    @Column
    private BigDecimal ipiReais;
    @Column
    private BigDecimal frete;
    @Column
    private BigDecimal subTrib;
    @Column
    private BigDecimal valorVenda;
    @Column
    private BigDecimal lucroVendaAvista = BigDecimal.ZERO;
    @Column
    private BigDecimal lucroVendaPrazo = BigDecimal.ZERO;
    @Column
    private BigDecimal lucroVendaPrazoPorcentagem = BigDecimal.ZERO;
    @Column
    private BigDecimal lucroVendaAvistaPorcentagem = BigDecimal.ZERO;
    @Column
    private BigDecimal custoMaisImpostos;
    @Column
    private BigDecimal custoOp;
    @Column
    private BigDecimal custoOpReais;
    @Column
    private BigDecimal despesasAssessorias;
    @Column
    private BigDecimal custoOpMaisEncargos;

    @Column
    private BigDecimal valorVendaAvista;
    @Column
    private BigDecimal despesaExtra;
    @Column
    private BigDecimal despesaExtraReais;
    @Column
    private BigDecimal aux5 = BigDecimal.ZERO;
    @Column
    private BigDecimal aux6 = BigDecimal.ZERO;
    @Column
    private BigDecimal valorUni;
    @Column
    private BigDecimal valorVendaPrazo;
    @Column
    private BigDecimal percentualAvista;
    @Column
    private BigDecimal percentualAprazo;
    @Column
    private BigDecimal comissao;
    @Column
    private BigDecimal comissaoReais;
    private String cfopD;
    @ManyToOne
    private Ncm ncm;
    @ManyToOne
    private Fornecedor fornecedor;
    @Column
    private String ncmString;
    @Column
    private String percentualDeIcms;
    private String percentualDeIcmsBaseC;
    private String percentualDeCofins;
    private String icmsCst;
    private String origem;
    private String motivoDesoneracao;
    private String percentualIpi;
    private String percentualPis;
    private String codEnquadramentoIpi;
    private String ipiCst;
    private String pisCst;
    private String cofinsCst;
    private String modalidade;
    private String cfopF;
    @ManyToOne
    private Icms icms;
    @ManyToOne
    private Ipi ipi1;
    @ManyToOne
    private Pis pis;
    @ManyToOne
    private Cofins cofins;
    @Column
    private Boolean ativo = true;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtUltimaCompra;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtCadastro;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtValidade;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtUltimaVenda;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "produto"
    )
    private List<ItemMateriaPrima> itensMateriaPrima = new ArrayList<>();

    public void addItem(ItemMateriaPrima item) throws Exception {
        if (item.getProduto() == null) {

            throw new Exception("É necessário adicionar um item!");

        } else if (itensMateriaPrima.contains(item)) {

            throw new Exception("O Item já foi adicionado");

        } else {

            itensMateriaPrima.add(item);

            item.setProdutoId(this);
        }

    }

    public String getPercentualDeCofins() {
        return percentualDeCofins;
    }

    public void setPercentualDeCofins(String percentualDeCofins) {
        this.percentualDeCofins = percentualDeCofins;
    }

    public String getCofinsCst() {
        return cofinsCst;
    }

    public void setCofinsCst(String cofinsCst) {
        this.cofinsCst = cofinsCst;
    }

    public String getPercentualPis() {
        return percentualPis;
    }

    public void setPercentualPis(String percentualPis) {
        this.percentualPis = percentualPis;
    }

    public String getPisCst() {
        return pisCst;
    }

    public void setPisCst(String pisCst) {
        this.pisCst = pisCst;
    }

    public String getIpiCst() {
        return ipiCst;
    }

    public void setIpiCst(String ipiCst) {
        this.ipiCst = ipiCst;
    }

    public String getMotivoDesoneracao() {
        return motivoDesoneracao;
    }

    public void setMotivoDesoneracao(String motivoDesoneracao) {
        this.motivoDesoneracao = motivoDesoneracao;
    }

    public String getPercentualDeIcmsBaseC() {
        return percentualDeIcmsBaseC;
    }

    public void setPercentualDeIcmsBaseC(String percentualDeIcmsBaseC) {
        this.percentualDeIcmsBaseC = percentualDeIcmsBaseC;
    }

    public String getPercentualIpi() {
        return percentualIpi;
    }

    public void setPercentualIpi(String percentualIpi) {
        this.percentualIpi = percentualIpi;
    }

    public String getCodEnquadramentoIpi() {
        return codEnquadramentoIpi;
    }

    public void setCodEnquadramentoIpi(String codEnquadramentoIpi) {
        this.codEnquadramentoIpi = codEnquadramentoIpi;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getIdstring() {
        return idstring;
    }

    public void setIdstring(String idstring) {
        this.idstring = idstring;
    }

    public String getPercentualDeIcms() {
        return percentualDeIcms;
    }

    public void setPercentualDeIcms(String percentualDeIcms) {
        this.percentualDeIcms = percentualDeIcms;
    }

    public String getIcmsCst() {
        return icmsCst;
    }

    public void setIcmsCst(String icmsCst) {
        this.icmsCst = icmsCst;
    }

    public String getIdString() {
        return idstring;
    }

    public void setIdString(String idString) {
        this.idstring = idString;
    }

    public String getNomeCodBarras() {
        return nomeCodBarras;
    }

    public void setNomeCodBarras(String nomeCodBarras) {
        this.nomeCodBarras = nomeCodBarras;
    }

    public BigDecimal getIpiReais() {
        return ipiReais;
    }

    public void setIpiReais(BigDecimal ipiReais) {
        this.ipiReais = ipiReais;
    }

    public BigDecimal getCustoOpReais() {
        return custoOpReais;
    }

    public void setCustoOpReais(BigDecimal custoOpReais) {
        this.custoOpReais = custoOpReais;
    }

    public BigDecimal getDespesaExtraReais() {
        return despesaExtraReais;
    }

    public void setDespesaExtraReais(BigDecimal despesaExtraReais) {
        this.despesaExtraReais = despesaExtraReais;
    }

    public BigDecimal getComissaoReais() {
        return comissaoReais;
    }

    public void setComissaoReais(BigDecimal comissaoReais) {
        this.comissaoReais = comissaoReais;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public BigDecimal getPercentualAvista() {
        return percentualAvista;
    }

    public void setPercentualAvista(BigDecimal percentualAvista) {
        this.percentualAvista = percentualAvista;
    }

    public BigDecimal getPercentualAprazo() {
        return percentualAprazo;
    }

    public void setPercentualAprazo(BigDecimal percentualAprazo) {
        this.percentualAprazo = percentualAprazo;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void removeItem(ItemMateriaPrima item) throws Exception {
        if (itensMateriaPrima.contains(item)) {
            itensMateriaPrima.remove(item);
//            item.estornaEstoque();
//            calculaTotalFiscal();
        } else {
            throw new Exception("O item selecionado não contem na lista de itens");
        }
    }

    public BigDecimal getLucroVendaAvista() {
        return lucroVendaAvista;
    }

    public void setLucroVendaAvista(BigDecimal lucroVendaAvista) {
        this.lucroVendaAvista = lucroVendaAvista;
    }

    public BigDecimal getLucroVendaPrazo() {
        return lucroVendaPrazo;
    }

    public void setLucroVendaPrazo(BigDecimal lucroVendaPrazo) {
        this.lucroVendaPrazo = lucroVendaPrazo;
    }

    public BigDecimal getLucroVendaPrazoPorcentagem() {
        return lucroVendaPrazoPorcentagem;
    }

    public void setLucroVendaPrazoPorcentagem(BigDecimal lucroVendaPrazoPorcentagem) {
        this.lucroVendaPrazoPorcentagem = lucroVendaPrazoPorcentagem;
    }

    public BigDecimal getLucroVendaAvistaPorcentagem() {
        return lucroVendaAvistaPorcentagem;
    }

    public void setLucroVendaAvistaPorcentagem(BigDecimal lucroVendaAvistaPorcentagem) {
        this.lucroVendaAvistaPorcentagem = lucroVendaAvistaPorcentagem;
    }

    public void calculaLucro1() {
        lucroVendaAvista = valorVendaAvista.subtract(custoOpMaisEncargos);

        lucroVendaAvistaPorcentagem = lucroVendaAvista.multiply(new BigDecimal("100")).divide(custoOpMaisEncargos, 3, RoundingMode.UP);

    }

    public void calculaLucro12() {
        lucroVendaPrazo = valorVendaPrazo.subtract(custoOpMaisEncargos);

        lucroVendaPrazoPorcentagem = lucroVendaPrazo.multiply(new BigDecimal("100")).divide(custoOpMaisEncargos, 3, RoundingMode.UP);

    }

    public void calculaLucro2() {

        valorVendaAvista = lucroVendaAvistaPorcentagem.divide(new BigDecimal("100"), 3, RoundingMode.UP).multiply(custoOpMaisEncargos);
        valorVendaAvista = valorVendaAvista.add(custoOpMaisEncargos);
        lucroVendaAvista = valorVendaAvista.subtract(custoOpMaisEncargos);

    }

    public void calculaLucro21() {

        valorVendaPrazo = lucroVendaPrazoPorcentagem.divide(new BigDecimal("100"), 3, RoundingMode.UP).multiply(custoOpMaisEncargos);
        valorVendaPrazo = valorVendaPrazo.add(custoOpMaisEncargos);
        lucroVendaPrazo = valorVendaPrazo.subtract(custoOpMaisEncargos);

    }

    public void calculaLucro3() {
        valorVendaAvista = lucroVendaAvista.add(custoOpMaisEncargos);
        lucroVendaAvistaPorcentagem = lucroVendaAvista.multiply(new BigDecimal("100")).divide(custoOpMaisEncargos, 3, RoundingMode.UP);

    }

    public void calculaLucro31() {
        valorVendaPrazo = lucroVendaPrazo.add(custoOpMaisEncargos);
        lucroVendaPrazoPorcentagem = lucroVendaPrazo.multiply(new BigDecimal("100")).divide(custoOpMaisEncargos, 3, RoundingMode.UP);
    }

    public void calculaCustoTotal() {
        comissaoReais = comissao.multiply(valorUni.divide(new BigDecimal("100")));
        despesaExtraReais = despesaExtra.multiply(valorUni.divide(new BigDecimal("100")));
        custoOpReais = custoOp.multiply(valorUni.divide(new BigDecimal("100")));
        ipiReais = ipi.multiply(valorUni.divide(new BigDecimal("100")));
        custoOpMaisEncargos = valorUni.add(ipiReais).add(subTrib).add(custoOpReais).add(despesaExtraReais).add(comissaoReais);

    }

    public void calculaIpi() {

        custoOpMaisEncargos = valorUni.add(ipiReais).add(subTrib).add(custoOpReais).add(despesaExtraReais).add(comissaoReais);
    }

    public void calculaCustoOp() {

        custoOpMaisEncargos = valorUni.add(ipiReais).add(subTrib).add(custoOpReais).add(despesaExtraReais).add(comissaoReais);
    }

    public void calculaDespesaExtra() {

        custoOpMaisEncargos = valorUni.add(ipiReais).add(subTrib).add(custoOpReais).add(despesaExtraReais).add(comissaoReais);
    }

    public void calculaComissao() {

        custoOpMaisEncargos = valorUni.add(ipiReais).add(subTrib).add(custoOpReais).add(despesaExtraReais).add(comissaoReais);
    }

    public BigDecimal getAux5() {
        return aux5;
    }

    public void setAux5(BigDecimal aux5) {
        this.aux5 = aux5;
    }

    public BigDecimal getAux6() {
        return aux6;
    }

    public void setAux6(BigDecimal aux6) {
        this.aux6 = aux6;
    }

    public String getFornecedorString() {
        return fornecedorString;
    }

    public void setFornecedorString(String fornecedorString) {
        this.fornecedorString = fornecedorString;
    }

    public String getMarcaString() {
        return marcaString;
    }

    public void setMarcaString(String marcaString) {
        this.marcaString = marcaString;
    }

    public List<ItemMateriaPrima> getItensMateriaPrima() {
        return itensMateriaPrima;
    }

    public void setItensMateriaPrima(List<ItemMateriaPrima> itensMateriaPrima) {
        this.itensMateriaPrima = itensMateriaPrima;
    }

    public AjusteNcm getAjusteNcm() {
        return ajusteNcm;
    }

    public void setAjusteNcm(AjusteNcm ajusteNcm) {
        this.ajusteNcm = ajusteNcm;
    }

    public BigDecimal getEstoque() {
        return estoque;
    }

    public Icms getIcms() {
        return icms;
    }

    public void setIcms(Icms icms) {
        this.icms = icms;
    }

    public Ipi getIpi1() {
        return ipi1;
    }

    public void setIpi1(Ipi ipi1) {
        this.ipi1 = ipi1;
    }

    public Pis getPis() {
        return pis;
    }

    public void setPis(Pis pis) {
        this.pis = pis;
    }

    public Cofins getCofins() {
        return cofins;
    }

    public void setCofins(Cofins cofins) {
        this.cofins = cofins;
    }

    public void setEstoque(BigDecimal estoque) {
        this.estoque = estoque;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    @ManyToOne
    @JoinColumn(name = "cfop_id")
    private Cfop cfop;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getLetra() {
        return letra;
    }

    public void ma() {
        nome = nome.toUpperCase();

    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getNumeroPosicao() {
        return numeroPosicao;
    }

    public void setNumeroPosicao(String numeroPosicao) {
        this.numeroPosicao = numeroPosicao;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public GrupoProduto getGrupo() {
        return grupo;
    }

    public Ncm getNcm() {
        return ncm;
    }

    public void setNcm(Ncm ncm) {
        this.ncm = ncm;
    }

    public String getNcmString() {
        return ncmString;
    }

    public void setNcmString(String ncmString) {
        this.ncmString = ncmString;
    }

    public void setGrupo(GrupoProduto grupo) {
        this.grupo = grupo;
    }

    public Fornecedor getUltimoFornecedor() {
        return ultimoFornecedor;
    }

    public void setUltimoFornecedor(Fornecedor ultimoFornecedor) {
        this.ultimoFornecedor = ultimoFornecedor;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getEmCondicional() {
        return emCondicional;
    }

    public void setEmCondicional(BigDecimal emCondicional) {
        this.emCondicional = emCondicional;
    }

    public BigDecimal getEstoqueLoja() {
        return estoqueLoja;
    }

    public void setEstoqueLoja(BigDecimal estoqueLoja) {
        this.estoqueLoja = estoqueLoja;
    }

    public BigDecimal getEstoqueMin() {
        return estoqueMin;
    }

    public void setEstoqueMin(BigDecimal estoqueMin) {
        this.estoqueMin = estoqueMin;
    }

    public String getCodFornecedor() {
        return codFornecedor;
    }

    public void setCodFornecedor(String codFornecedor) {
        this.codFornecedor = codFornecedor;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
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

    public BigDecimal getSubTrib() {
        return subTrib;
    }

    public void setSubTrib(BigDecimal subTrib) {
        this.subTrib = subTrib;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public BigDecimal getCustoMaisImpostos() {
        return custoMaisImpostos;
    }

    public void setCustoMaisImpostos(BigDecimal custoMaisImpostos) {
        this.custoMaisImpostos = custoMaisImpostos;
    }

    public BigDecimal getCustoOp() {
        return custoOp;
    }

    public void setCustoOp(BigDecimal custoOp) {
        this.custoOp = custoOp;
    }

    public BigDecimal getDespesasAssessorias() {
        return despesasAssessorias;
    }

    public void setDespesasAssessorias(BigDecimal despesasAssessorias) {
        this.despesasAssessorias = despesasAssessorias;
    }

    public BigDecimal getCustoOpMaisEncargos() {
        return custoOpMaisEncargos;
    }

    public void setCustoOpMaisEncargos(BigDecimal custoOpMaisEncargos) {
        this.custoOpMaisEncargos = custoOpMaisEncargos;
    }

    public BigDecimal getValorVendaAvista() {
        return valorVendaAvista;
    }

    public void setValorVendaAvista(BigDecimal valorVendaAvista) {
        this.valorVendaAvista = valorVendaAvista;
    }

    public BigDecimal getDespesaExtra() {
        return despesaExtra;
    }

    public void setDespesaExtra(BigDecimal despesaExtra) {
        this.despesaExtra = despesaExtra;
    }

    public BigDecimal getValorUni() {
        return valorUni;
    }

    public void setValorUni(BigDecimal valorUni) {
        this.valorUni = valorUni;
    }

    public BigDecimal getValorVendaPrazo() {
        return valorVendaPrazo;
    }

    public void setValorVendaPrazo(BigDecimal valorVendaPrazo) {
        this.valorVendaPrazo = valorVendaPrazo;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Date getDtUltimaCompra() {
        return dtUltimaCompra;
    }

    public void setDtUltimaCompra(Date dtUltimaCompra) {
        this.dtUltimaCompra = dtUltimaCompra;
    }

    public Date getDtValidade() {
        return dtValidade;
    }

    public void setDtValidade(Date dtValidade) {
        this.dtValidade = dtValidade;
    }

    public Date getDtUltimaVenda() {
        return dtUltimaVenda;
    }

    public void setDtUltimaVenda(Date dtUltimaVenda) {
        this.dtUltimaVenda = dtUltimaVenda;
    }

    public Cfop getCfop() {
        return cfop;
    }

    public void setCfop(Cfop cfop) {
        this.cfop = cfop;
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
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
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

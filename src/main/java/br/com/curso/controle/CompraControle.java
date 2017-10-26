/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CompraFacade;
import br.com.curso.ejb.ProdutoFacade;
import br.com.curso.entidade.Compra;
import br.com.curso.entidade.ItemCompra;
import br.com.curso.entidade.Parcela;
import br.com.curso.entidade.Produto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Miguel
 */
@ManagedBean
@SessionScoped
public class CompraControle implements Serializable {

    private Compra compra;
    private Produto produto;
    private Parcela parcela;
    private String tipo;
    private BigDecimal aux;
    private Date novaData;
    private BigDecimal aux1;
    private BigDecimal aux2;
    private BigDecimal aux9;
    private BigDecimal aux3;
    private BigDecimal aux4;
    private BigDecimal aux5;
    private BigDecimal aux6;
    private ItemCompra itemCompra;
    @EJB
    private CompraFacade compraFacade;
    @EJB
    private ProdutoFacade produtoFacade;
    private GenericConverter converter;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public void setValorServicoSelecionado() {
        itemCompra.setValor(itemCompra.getProduto().getValorUni());
        itemCompra.setNcmString(itemCompra.getProduto().getNcmString());
        itemCompra.setQuantidade(BigDecimal.ONE);
        itemCompra.setIpi(BigDecimal.ZERO);
        itemCompra.setSt(BigDecimal.ZERO);
        itemCompra.setValorVendaAprazo(itemCompra.getProduto().getValorVendaPrazo());
        itemCompra.setValorVendaAvista(itemCompra.getProduto().getValorVendaAvista());

    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(compraFacade);
        }
        return converter;
    }

    public List<Produto> listaProduto(String query) {

        if (Character.isDigit(query.charAt(0)) == true) {
            System.out.println("Possui numeros");

            return produtoFacade.autoCompletePrimeiraLetra("codFornecedor", query);
        } else {
            return produtoFacade.autoCompletePrimeiraLetra("nome", query);

        }
    }

    public boolean validaParcelas() {
        try {
            System.out.println("Teste, entrou aqui");
            if (compra.getTipoPagamento().equals("AVISTA")) {
                compra.setQtdParcela(1);
                compra.gerarParcelas();
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public void removeItemPacela() {
        try {

            compra.removeItemPacela();

            parcela = new Parcela();

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void troca() {
        
        if (compra.getTipoPagamento().equals("A VISTA")) {

            if (compra.getId() == null) {
                compra.setParcelas(null);
                compra.setQtdParcela(1);
                compra.calculaTotal1();
            } else {
                removeItemPacela();
                compra.setQtdParcela(1);
                compra.calculaTotal1();
            }

        }
    }

    public void troca1() {

        removeItemPacela();

        compra.setQtdParcela(1);
        compra.setTipoPagamento("A VISTA");
        compra.calculaTotal1();

    }

    public void calcular1() {
        try {

            itemCompra.setIpi(itemCompra.getIpi().multiply(itemCompra.getValor().divide(new BigDecimal("100"))));

            itemCompra.setSt(itemCompra.getSt().divide(itemCompra.getQuantidade(), 2, RoundingMode.UP));

            itemCompra.setPercentualfrete(compra.getTotalFrete().multiply(new BigDecimal("100").divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));

            itemCompra.setPercentualacessorias(compra.getTotalDespesasAssessorias().multiply(new BigDecimal("100").
                    divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));

            itemCompra.setPercentualseguro(compra.getTotalSeguro().multiply(new BigDecimal("100").divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));

            itemCompra.setPercentualdesconto(compra.getTotalDesconto().multiply(new BigDecimal("100").divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));

            itemCompra.setSubTotal(itemCompra.getQuantidade().multiply(itemCompra.getValor()));

            itemCompra.setFrete(itemCompra.getValor().multiply(itemCompra.getPercentualfrete().divide(new BigDecimal("100"))));
            itemCompra.setSeguro(itemCompra.getValor().multiply(itemCompra.getPercentualseguro().divide(new BigDecimal("100"))));
            itemCompra.setDesconto(itemCompra.getValor().multiply(itemCompra.getPercentualdesconto().divide(new BigDecimal("100"))));
            itemCompra.setDespassessorias(itemCompra.getValor().multiply(itemCompra.getPercentualacessorias().divide(new BigDecimal("100"))));

            itemCompra.setCustoTotalUnitario(itemCompra.getValor().add(itemCompra.getIpi().add(itemCompra.getSt().add(itemCompra.getFrete()
                    .add(itemCompra.getDespassessorias().add(itemCompra.getSeguro().subtract(itemCompra.getDesconto())))))));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Não foi possível calcular, verifique se todos os campos foram preenchidos!", ""));

        }

    }

    public String editar() {

        if (compra.getSituacao().equals("EM ABERTO")) {

            itemCompra = new ItemCompra();
            itemCompra.setIpi(BigDecimal.ZERO);
            itemCompra.setSt(BigDecimal.ZERO);

            compraFacade.carregaItensOs(compra);
            return "form?faces-redirect=true";

        } else {

            compraFacade.carregaItensOs(compra);
            return "form_1?faces-redirect=true";

        }

    }

    public void calcular2() {

        itemCompra.setIpi(itemCompra.getIpi().multiply(itemCompra.getSubTotal().divide(new BigDecimal("100"))));

        itemCompra.setPercentualfrete(compra.getTotalFrete().multiply(new BigDecimal("100").divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));
        itemCompra.setIcms(compra.getTotalIcms().multiply(new BigDecimal("100").divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));

        itemCompra.setPercentualacessorias(compra.getTotalDespesasAssessorias().multiply(new BigDecimal("100").
                divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));

        itemCompra.setPercentualseguro(compra.getTotalSeguro().multiply(new BigDecimal("100").divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));

        itemCompra.setPercentualdesconto(compra.getTotalDesconto().multiply(new BigDecimal("100").divide(compra.getTotalProdutos(), 3, RoundingMode.UP)));

        itemCompra.setSubTotal(itemCompra.getQuantidade().multiply(itemCompra.getValor()));

        itemCompra.setFrete(itemCompra.getSubTotal().multiply(itemCompra.getPercentualfrete().divide(new BigDecimal("100"))));
        itemCompra.setSeguro(itemCompra.getSubTotal().multiply(itemCompra.getPercentualseguro().divide(new BigDecimal("100"))));
        itemCompra.setDesconto(itemCompra.getSubTotal().multiply(itemCompra.getPercentualdesconto().divide(new BigDecimal("100"))));
        itemCompra.setDespassessorias(itemCompra.getSubTotal().multiply(itemCompra.getPercentualacessorias().divide(new BigDecimal("100"))));

        itemCompra.setCustoTotal(itemCompra.getSubTotal().add(itemCompra.getIpi().add(itemCompra.getSt().add(itemCompra.getFrete()
                .add(itemCompra.getDespassessorias().add(itemCompra.getSeguro().subtract(itemCompra.getDesconto())))))));

    }

    public void calcularFrete() {

    }

    public void t() throws Exception {

        itemCompra.setIpi(aux.multiply(new BigDecimal("100").divide(itemCompra.getValor())));
        aux = null;

    }

    public void precoAvista() throws Exception {

        itemCompra.setValorVendaAvista(itemCompra.getCustoTotalUnitario().add(itemCompra.getCustoTotalUnitario().multiply(aux5.divide(new BigDecimal("100")))));

        aux5 = null;

    }

    public void precoAprazo() throws Exception {

        itemCompra.setValorVendaAprazo(new BigDecimal(BigInteger.ZERO));
        itemCompra.setValorVendaAprazo(itemCompra.getCustoTotalUnitario().add(itemCompra.getCustoTotalUnitario().multiply(aux6.divide(new BigDecimal("100")))));

        aux6 = null;

    }

    public void a() throws Exception {

        itemCompra.setSt(aux1.multiply(new BigDecimal("100").divide(itemCompra.getValor())));
        aux1 = BigDecimal.ZERO;
    }

    public void b() throws Exception {

        itemCompra.setDespassessorias(aux2.multiply(new BigDecimal("100").divide(itemCompra.getValor())));
        aux2 = BigDecimal.ZERO;
    }

    public void c() throws Exception {

        itemCompra.setCusto(aux3.multiply(new BigDecimal("100").divide(itemCompra.getValor())));
        aux3 = BigDecimal.ZERO;
    }

    public void cc() throws Exception {

        itemCompra.setComissao(aux4.multiply(new BigDecimal("100").divide(itemCompra.getValor())));
        aux4 = BigDecimal.ZERO;
    }

    public void despExtra() throws Exception {

        itemCompra.setExtras(aux9.multiply(new BigDecimal("100").divide(itemCompra.getValor())));
        aux9 = BigDecimal.ZERO;
    }

    public void addItem() {
        try {

            calcular2();

            compra.addItem(itemCompra);

            itemCompra = new ItemCompra();

            if (compra.getTipoPagamento().equals("A PRAZO")) {
                compra.gerarParcelas();
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Produto não encontrado!", ""));
        }
    }

    public void addItem1() {
        try {
            compra.addItem1(itemCompra);
            itemCompra = new ItemCompra();
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), " rarwasasd");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void removeItem() {
        try {
            compra.removeItem(itemCompra);
            itemCompra = new ItemCompra();
            if (compra.getTipoPagamento().equals("A PRAZO")) {
                compra.gerarParcelas();
            }
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public String novo() {
        compra = new Compra();
        compra.setDataEntrada(new Date());
        compra.setDocumento("DINHEIRO");
        itemCompra = new ItemCompra();
        tipo = "nome";

        return "form?faces-redirect=true";
    }

    public List<Produto> listaProdCodNome(String query) {

        if (Character.isDigit(query.charAt(0)) == true) {
            System.out.println("Possui numeros");

            return produtoFacade.autoCompletePrimeiraLetra("codFornecedor", query);
        } else {
            return produtoFacade.autoCompletePrimeiraLetra("nome", query);

        }
    }

    public void teste() {

        System.out.println(compra.getParcelas());

    }

    public String salvar() {

        try {
            compra.setSituacao("EM ABERTO");
            compraFacade.salvar(compra);

            return "list?faces-redirect=true";
        } catch (Exception e) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Preencha todos os campos!", ""));

        }

        return "";

    }

    public String faturar() {

        compra.setSituacao("FINALIZADO");

        try {

            compraFacade.faturar1(compra);

            return "list?faces-redirect=true";
        } catch (Exception e) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Preencha todos os campos!", ""));

        }

        return "";

    }

    public void excluir(Compra v) {

        try {

            compraFacade.carregaItensOs(v);
            compraFacade.extornarProd(v);
            compraFacade.excluir(v);

        } catch (Exception e) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Náo é possível excluir esse movimento, pois possui movimentação financeira!", ""));

        }

    }

    public List<Compra> getListaCompras() {
        return compraFacade.listarTodosCompleto();
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public ItemCompra getItemCompra() {
        return itemCompra;
    }

    public void setItemCompra(ItemCompra itemCompra) {
        this.itemCompra = itemCompra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getAux() {
        return aux;
    }

    public void setAux(BigDecimal aux) {
        this.aux = aux;
    }

    public CompraFacade getCompraFacade() {
        return compraFacade;
    }

    public void setCompraFacade(CompraFacade compraFacade) {
        this.compraFacade = compraFacade;
    }

    public ProdutoFacade getProdutoFacade() {
        return produtoFacade;
    }

    public void setProdutoFacade(ProdutoFacade produtoFacade) {
        this.produtoFacade = produtoFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    public BigDecimal getAux1() {
        return aux1;
    }

    public void setAux1(BigDecimal aux1) {
        this.aux1 = aux1;
    }

    public BigDecimal getAux2() {
        return aux2;
    }

    public void setAux2(BigDecimal aux2) {
        this.aux2 = aux2;
    }

    public BigDecimal getAux3() {
        return aux3;
    }

    public void setAux3(BigDecimal aux3) {
        this.aux3 = aux3;
    }

    public BigDecimal getAux4() {
        return aux4;
    }

    public void setAux4(BigDecimal aux4) {
        this.aux4 = aux4;
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

    public Date getNovaData() {
        return novaData;
    }

    public void setNovaData(Date novaData) {
        this.novaData = novaData;
    }

    public BigDecimal getAux9() {
        return aux9;
    }

    public void setAux9(BigDecimal aux9) {
        this.aux9 = aux9;
    }

}

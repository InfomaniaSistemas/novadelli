/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ProdutoFacade;
import br.com.curso.entidade.ItemMateriaPrima;
import br.com.curso.entidade.Produto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;

/**
 *
 * @author Miguel
 */
@ManagedBean
@SessionScoped
public class ProdutoControle implements Serializable {

    private Produto produto;
    private ItemMateriaPrima itemMateriaPrima;

    private String id2 = "produto";
    private String id3;
    private String tipo2 = "todos";
    private BigDecimal aux;
    private BigDecimal aux1;
    private BigDecimal aux2;
    private BigDecimal aux3;
    private BigDecimal aux4;
    private BigDecimal aux5;
    private BigDecimal aux6;
    private BigDecimal aux7;
    private BigDecimal aux8;
    private BigDecimal aux9;
    private List<Produto> listaDeCodigos;
    private String cod;
    private String cod2;
    private String cod3;
    private String cod4;
    private String cod5;
    private BigDecimal valor;
    private BigDecimal valor2;
    private BigDecimal valor3;
    private BigDecimal valor4;
    private BigDecimal valor5;
    @EJB
    private ProdutoFacade produtoFacade;
    private GenericConverter converter;

    private String codBarras;

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public void selecionaProduto() {

        List<Produto> aux = produtoFacade.listaProdutosPorCod(codBarras);

        if (!aux.get(0).getCodBarras().equals("")) {

            itemMateriaPrima.setProduto(aux.get(0));
        }

        codBarras = "";
    }

    public String getCod() {
        return cod;
    }

    public ItemMateriaPrima getItemMateriaPrima() {
        return itemMateriaPrima;
    }

    public void setItemMateriaPrima(ItemMateriaPrima itemMateriaPrima) {
        this.itemMateriaPrima = itemMateriaPrima;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCod2() {
        return cod2;
    }

    public void setCod2(String cod2) {
        this.cod2 = cod2;
    }

    public String getCod3() {
        return cod3;
    }

    public void setCod3(String cod3) {
        this.cod3 = cod3;
    }

    public String getCod4() {
        return cod4;
    }

    public void setCod4(String cod4) {
        this.cod4 = cod4;
    }

    public String getCod5() {
        return cod5;
    }

    public void setCod5(String cod5) {
        this.cod5 = cod5;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor2() {
        return valor2;
    }

    public void setValor2(BigDecimal valor2) {
        this.valor2 = valor2;
    }

    public BigDecimal getValor3() {
        return valor3;
    }

    public void setValor3(BigDecimal valor3) {
        this.valor3 = valor3;
    }

    public BigDecimal getValor4() {
        return valor4;
    }

    public void setValor4(BigDecimal valor4) {
        this.valor4 = valor4;
    }

    public BigDecimal getValor5() {
        return valor5;
    }

    public void setValor5(BigDecimal valor5) {
        this.valor5 = valor5;
    }

    public List<Produto> getListaDeCodigos() {
        return listaDeCodigos;
    }

    public void setListaDeCodigos(List<Produto> listaDeCodigos) {
        this.listaDeCodigos = listaDeCodigos;
    }

    public void remover(Produto prod) {

        listaDeCodigos.remove(prod);
    }

    public BigDecimal getAux7() {
        return aux7;
    }

    public void setAux7(BigDecimal aux7) {
        this.aux7 = aux7;
    }

    public BigDecimal getAux8() {
        return aux8;
    }

    public void setAux8(BigDecimal aux8) {
        this.aux8 = aux8;
    }

    public BigDecimal getAux9() {
        return aux9;
    }

    public void setAux9(BigDecimal aux9) {
        this.aux9 = aux9;
    }

    public BigDecimal getAux() {
        return aux;
    }

    public void addItem() {
        try {
            produto.addItem(itemMateriaPrima);
            itemMateriaPrima = new ItemMateriaPrima();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Produto não encontrado!", ""));
        }
    }

    public void removeItem() {
        try {

            produto.removeItem(itemMateriaPrima);
            itemMateriaPrima = new ItemMateriaPrima();

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }
    private int first;

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void onPageChange(PageEvent event) {
        this.setFirst(((DataTable) event.getSource()).getFirst());
    }

    public void calcular1() {

        produto.setIpi(produto.getIpi().multiply(produto.getValorUni().divide(new BigDecimal("100"))));
        produto.setCustoOp(produto.getCustoOp().multiply(produto.getValorUni().divide(new BigDecimal("100"))));
        produto.setDespesaExtra(produto.getDespesaExtra().multiply(produto.getValorUni().divide(new BigDecimal("100"))));
        produto.setComissao(produto.getComissao().multiply(produto.getValorUni().divide(new BigDecimal("100"))));

        produto.setCustoOpMaisEncargos(produto.getValorUni().add(produto.getIpi().add(produto.getSubTrib().add(produto.getCustoOp().add(produto.getDespesaExtra()
                .add(produto.getComissao()))))));
    }

    public String editar() {
        itemMateriaPrima = new ItemMateriaPrima();
        produtoFacade.carregaItensOs(produto);
        return "form2?faces-redirect=true";
    }

    public void setAux(BigDecimal aux) {
        this.aux = aux;
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

    public String getTipo2() {
        return tipo2;
    }

    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    public void despExtra() throws Exception {

        produto.setDespesaExtra(aux9.multiply(new BigDecimal("100").divide(produto.getValorUni())));
        aux9 = BigDecimal.ZERO;
    }

    public void precoAprazo() throws Exception {

        produto.setValorVendaPrazo(new BigDecimal(BigInteger.ZERO));
        produto.setValorVendaPrazo(produto.getCustoOp().add(produto.getCustoOpMaisEncargos().multiply(aux6.divide(new BigDecimal("100")))));

        aux6 = null;

    }

    public void precoAvista() throws Exception {

        produto.setValorVendaAvista(produto.getCustoOp().add(produto.getCustoOpMaisEncargos().multiply(aux5.divide(new BigDecimal("100")))));

        aux5 = null;

    }

    public void cc() throws Exception {

        produto.setComissao(aux4.multiply(new BigDecimal("100").divide(produto.getValorUni())));
        aux4 = BigDecimal.ZERO;
    }

    public void c() throws Exception {

        produto.setCustoOpMaisEncargos(aux3.multiply(new BigDecimal("100").divide(produto.getValorUni())));
        aux3 = BigDecimal.ZERO;
    }

    public void a() throws Exception {

        produto.setSubTrib(aux1.multiply(new BigDecimal("100").divide(produto.getValorUni())));
        aux1 = BigDecimal.ZERO;
    }

    public void t() throws Exception {

        produto.setIpi(aux.multiply(new BigDecimal("100").divide(produto.getValorUni())));
        aux = null;

    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(produtoFacade);
        }
        return converter;
    }

    public String novo() {
        produto = new Produto();
        itemMateriaPrima = new ItemMateriaPrima();

        produto.setSubTrib(BigDecimal.ZERO);
        produto.setValorUni(BigDecimal.ZERO);
        produto.setIpi(BigDecimal.ZERO);
        produto.setComissao(BigDecimal.ZERO);
        produto.setCustoOp(BigDecimal.ZERO);
        produto.setDespesaExtra(BigDecimal.ZERO);

        produto.setPercentualDeIcms("0");
        produto.setPercentualDeIcmsBaseC("0");
        produto.setMotivoDesoneracao("0");
        produto.setIcmsCst("103");
        produto.setModalidade("0");
        produto.setOrigem("0");

        return "form2?faces-redirect=true";
    }

    public String novoCompra() {
        produto = new Produto();
        itemMateriaPrima = new ItemMateriaPrima();

        produto.setSubTrib(BigDecimal.ZERO);
        produto.setValorUni(BigDecimal.ZERO);
        produto.setIpi(BigDecimal.ZERO);
        produto.setComissao(BigDecimal.ZERO);
        produto.setCustoOp(BigDecimal.ZERO);
        produto.setDespesaExtra(BigDecimal.ZERO);

        return "form1?faces-redirect=true";
    }

    public String novo1() {
        produto = new Produto();
        produto.setSubTrib(BigDecimal.ZERO);
        produto.setValorUni(BigDecimal.ZERO);
        produto.setIpi(BigDecimal.ZERO);
        produto.setComissao(BigDecimal.ZERO);
        produto.setCustoOp(BigDecimal.ZERO);
        produto.setDespesaExtra(BigDecimal.ZERO);
        return "form1?faces-redirect=true";
    }

    public List<Produto> autoComplete(String query) {
        return produtoFacade.autoComplete("codBarras", query);
    }

    public List<Produto> teste(String query) {

        if (Character.isDigit(query.charAt(0)) == true) {
            System.out.println("Possui numeros");

            return produtoFacade.autoCompletePrimeiraLetra("codFornecedor", query);
        } else {
            return produtoFacade.autoCompletePrimeiraLetra("nome", query);

        }
    }

    public List<Produto> autoCompleteNome(String query) {
        return produtoFacade.autoCompletePrimeiraLetra("nome", query);
    }

    public List<Produto> autoCompleteCodFor(String query) {
        return produtoFacade.autoCompletePrimeiraLetra("codFor", query);
    }

    public List<Produto> autoCompleteNome2(String query) {
        return produtoFacade.autoComplete2("nome", query);
    }

    public List<Produto> autoCompleteNcm(String query) {
        return produtoFacade.autoComplete("nome", query);
    }

    public List<Produto> autoCompleteFor(String query) {
        return produtoFacade.autoComplete("codFornecedor", query);
    }

    public void verificaCod() {

    }

    public void validaLista() {

        Boolean auxB = Boolean.TRUE;
        String validaListagemProduto;

        if (tipo2.equals("ativo")) {

            auxB = Boolean.TRUE;
            validaListagemProduto = "ativo";

            produtoFacade.listarRapidoCadastro("nome", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("inativo")) {

            auxB = Boolean.FALSE;
            validaListagemProduto = "inativo";
            produtoFacade.listarRapidoCadastro("nome", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("pos")) {

            validaListagemProduto = "pos";
            produtoFacade.listarRapidoCadastro("idstring", id2, auxB, validaListagemProduto);
        } else if (tipo2.equals("Marca")) {
            validaListagemProduto = "pos";
            produtoFacade.listarRapidoCadastro("marca.nome", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("cod")) {
            validaListagemProduto = "cod";
            produtoFacade.listarRapidoCadastro("codBarras", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("codFornecedor")) {
            validaListagemProduto = "for";
            produtoFacade.listarRapidoCadastro("codFornecedor", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("todos")) {
            validaListagemProduto = "todos";
            produtoFacade.listarRapidoCadastro("nome", id2, auxB, validaListagemProduto);

        }

    }

    public void limpaCampo() {

        id2 = "";
    }

    public void validaLista1() {

        produtoFacade.listarRapido("codBarras", id2);

    }

    public void validaLista2() {

        produtoFacade.listarRapido("codFornecedor", id2);

    }

    public String salvar() {

        if (produto.getId() == null) {
            Long id1 = produtoFacade.listarTodosCompleto().get(0).getId();
            produto.setId(id1 + 1);
            produto.setIdString(produto.getId().toString());
        }

        produto.ma();
        produtoFacade.salvar(produto);
        return "list?faces-redirect=true";

    }

    public String salvar1() {
        if (produto.getId() == null) {
            Long id1 = produtoFacade.listarTodosCompleto().get(0).getId();
            produto.setId(id1 + 1);
        }

        produto.ma();
        produtoFacade.salvar(produto);
        return "list1?faces-redirect=true";

    }

    public String salvarEditar() {
        if (produto.getId() == null) {
            Long id1 = produtoFacade.listarTodosCompleto().get(0).getId();
            produto.setId(id1 + 1);
        }

        produto.ma();
        produtoFacade.salvar(produto);
        return "list?faces-redirect=true";

    }

    public String excluir(Produto g) {

        try {
            produtoFacade.excluir(g);

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Item Excluido com Sucesso!", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Não é possivel excluir itens com dependência!", ""));

        }

        return "list";

    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public List<Produto> getListaProdutos2() {
        Boolean auxB = Boolean.TRUE;
        String validaListagemProduto;

        if (tipo2.equals("ativo")) {

            auxB = Boolean.TRUE;
            validaListagemProduto = "ativo";

            return produtoFacade.listarRapidoCadastro("nome", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("inativo")) {

            auxB = Boolean.FALSE;
            validaListagemProduto = "inativo";
            return produtoFacade.listarRapidoCadastro("nome", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("pos")) {

            validaListagemProduto = "pos";
            return produtoFacade.listarRapidoCadastro("idstring", id2, auxB, validaListagemProduto);
        } else if (tipo2.equals("Marca")) {
            validaListagemProduto = "pos";
            return produtoFacade.listarRapidoCadastro("marca.nome", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("cod")) {
            validaListagemProduto = "cod";
            return produtoFacade.listarRapidoCadastro("codBarras", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("codFornecedor")) {
            validaListagemProduto = "for";
            return produtoFacade.listarRapidoCadastro("codFornecedor", id2, auxB, validaListagemProduto);

        } else if (tipo2.equals("todos")) {
            validaListagemProduto = "todos";
            return produtoFacade.listarRapidoCadastro("nome", id2, auxB, validaListagemProduto);

        } else {
            validaListagemProduto = "todos";
            return produtoFacade.listarRapidoCadastro("nome", id2, auxB, validaListagemProduto);
        }

    }

    public List<Produto> getlistaTodos() {
        return produtoFacade.listarRapido("nome", id2);
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}

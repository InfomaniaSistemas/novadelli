/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContasReceberFacade;
import br.com.curso.ejb.FuncionarioFacade;
import br.com.curso.ejb.PessoaFacade;
import br.com.curso.ejb.ProdutoFacade;
import br.com.curso.ejb.VendaFacade;
import br.com.curso.entidade.ContasReceber;
import br.com.curso.entidade.Funcionario;
import br.com.curso.entidade.ItemVenda;
import br.com.curso.entidade.Parcela;
import br.com.curso.entidade.Pessoa;
import br.com.curso.entidade.PessoaFisica;
import br.com.curso.entidade.Produto;
import br.com.curso.entidade.Usuario;
import br.com.curso.entidade.Venda;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Miguel
 */
@ManagedBean
@SessionScoped
public class VendaControleAntigo implements Serializable {

    private Venda venda;

    Date aux1;
    private String tipo;
    Boolean auxB = Boolean.FALSE;
    private String id2 = " ";
    private String tipo2;
    private RelatorioControle relatorioControle;
    private ItemVenda itemVenda;
    private Parcela parcela;
    @EJB
    private VendaFacade vendaFacade;
    @EJB
    private ProdutoFacade produtoFacade;
    @EJB
    private ContasReceberFacade contasReceberFacade;
    @EJB
    private PessoaFacade pessoaFacade;
    @EJB
    private FuncionarioFacade funcionarioFacade;

    @EJB
    private ContasReceberFacade receberFacade;
    private GenericConverter converter;

    public VendaControleAntigo() {
        this.aux1 = new Date();
    }

    public Date getAux1() {
        return aux1;
    }

    public ProdutoFacade getProdutoFacade() {
        return produtoFacade;
    }

    public void setProdutoFacade(ProdutoFacade produtoFacade) {
        this.produtoFacade = produtoFacade;
    }

    public ContasReceberFacade getContasReceberFacade() {
        return contasReceberFacade;
    }

    public void setContasReceberFacade(ContasReceberFacade contasReceberFacade) {
        this.contasReceberFacade = contasReceberFacade;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public void setAux1(Date aux1) {
        this.aux1 = aux1;
    }

    public Boolean getAuxB() {
        return auxB;
    }

    public void setAuxB(Boolean auxB) {
        this.auxB = auxB;
    }

    public RelatorioControle getRelatorioControle() {
        return relatorioControle;
    }

    public void setRelatorioControle(RelatorioControle relatorioControle) {
        this.relatorioControle = relatorioControle;
    }

    public VendaFacade getVendaFacade() {
        return vendaFacade;
    }

    public void setVendaFacade(VendaFacade vendaFacade) {
        this.vendaFacade = vendaFacade;
    }

    public PessoaFacade getPessoaFacade() {
        return pessoaFacade;
    }

    public void setPessoaFacade(PessoaFacade pessoaFacade) {
        this.pessoaFacade = pessoaFacade;
    }

    public FuncionarioFacade getFuncionarioFacade() {
        return funcionarioFacade;
    }

    public void setFuncionarioFacade(FuncionarioFacade funcionarioFacade) {
        this.funcionarioFacade = funcionarioFacade;
    }

    public ContasReceberFacade getReceberFacade() {
        return receberFacade;
    }

    public void setReceberFacade(ContasReceberFacade receberFacade) {
        this.receberFacade = receberFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    public void enviaProd(Produto prod) {

        itemVenda.setPlantio(prod);

        itemVenda.setValor(prod.getValorVendaPrazo());
        itemVenda.setQuantidade(BigDecimal.ONE);
    }

    public String getId2() {
        return id2;

    }

    public void validaLista() {

        vendaFacade.listarRapido("nome", id2);

    }

    public void validaLista1() {

        vendaFacade.listarRapido("cpf", id2);

    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(vendaFacade);
        }
        return converter;
    }

    public void addItem() {
        try {
            venda.addItem(itemVenda);

            itemVenda = new ItemVenda();
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), "Aqui");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public BigDecimal getCredito() {

        BigDecimal credito = BigDecimal.ZERO;

        List<Venda> lista = vendaFacade.validaVendaDia(aux1);
        for (Venda lista1 : lista) {
            credito = credito.add(lista1.getTotalDescEn());
        }

        return credito;
    }

    public void removeItem() {
        try {

            venda.removeItem(itemVenda);
            itemVenda = new ItemVenda();

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void removeItemPacela() {
        try {

            venda.removeItemPacela();

            parcela = new Parcela();

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    String codBarras;
//
//    public void selecionaProduto() {
//
//        List<Produto> aux = produtoFacade.listaProdutosPorCod(codBarras);
//
//        if (!aux.get(0).getCodBarras().equals("")) {
//
//            itemVenda.setPlantio(aux.get(0));
//            setValorServicoSelecionado();
//        }
//
//        codBarras = "";
//    }

    public void validaCliente() {
        verificaValor();
        validaDadosCliente();
        if (venda.getPessoa().getRestricao().equals(true)) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Cliente com restrição, verifique!", ""));

        } else {

        }

    }

    public void validaDadosCliente() {
        verificaValor();
        validaCliente();
        if (venda.getPessoa().getCompleto().equals(false)) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Cliente com cadastro incompleto!", ""));

        } else {

        }

    }

    public String novo() {
        venda = new Venda();
        itemVenda = new ItemVenda();
        venda.setTipoDoc("DINHEIRO");
        auxB = Boolean.FALSE;
        venda.setFuncionario(getFuncionarios().get(0));
        tipo = "nome";
        tipo2 = "nome";
        return "form?faces-redirect=true";
    }

    public void verificaValor() {

        if (venda.getPessoa().getLimite().compareTo(BigDecimal.ZERO) == 0) {

            auxB = false;

        } else {
            BigDecimal valida = venda.getTotalDescEn().add(getValidaCredito());

            if (valida.compareTo(venda.getPessoa().getLimite()) == 1) {
                auxB = true;
            } else {
                auxB = false;

            }

        }

    }

    public BigDecimal getValidaCredito() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasReceber> lista = contasReceberFacade.validaMensalidadeAPagar(venda.getPessoa());
        for (ContasReceber lista1 : lista) {
            credito = credito.add(lista1.getValorAPagar());
        }

        return credito;
    }

    public String faturarOrcamento() {

        itemVenda = new ItemVenda();
        parcela = new Parcela();
        venda.setDtVenda(new Date());
        vendaFacade.carregaItensOs(venda);

        return "/venda/form?faces-redirect=true";
    }

    public void troca() {

        verificaValor();
        if (venda.getTipoPagamento().equals("A VISTA")) {

            if (venda.getId() == null) {
                venda.setParcelas(null);
                venda.setEntrada(BigDecimal.ZERO);

                venda.setQtdParcela(1);
                venda.calculaTotal();
            } else {
                removeItemPacela();
                venda.setEntrada(BigDecimal.ZERO);

                venda.setQtdParcela(1);
                venda.calculaTotal();
            }

        }
    }

    public void troca1() {

        removeItemPacela();

        venda.setEntrada(BigDecimal.ZERO);

        venda.setQtdParcela(1);
        venda.setTipoPagamento("A VISTA");
        venda.calculaTotal();

    }

    public String salvar() {

//        try {
        if (venda.getItensVenda().isEmpty()) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "O valor da venda não pode ser igual a 0,00, verifique!", ""));

            return "/form?faces-redirect=true";

        } else if (venda.getPessoa().getRestricao().equals(true) && venda.getTipoPagamento().equals("A PRAZO")) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Cliente com restrição, verifique!", ""));

            return "/form?faces-redirect=true";

        } else {

            if (venda.getId() == null) {
                Long id1 = vendaFacade.listarTodosCompleto().get(0).getId();
                venda.setId(id1 + 1);
            }

            if (venda.getPessoa() == null) {

                FacesContext.getCurrentInstance().
                        addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                        "Cliente com restrição, verifique!", ""));

                return "/form?faces-redirect=true";
            }

            vendaFacade.baixaProd(venda);
            if (venda.getPessoa() instanceof PessoaFisica) {
                venda.setValida("PF");
                venda.setNomeFun(venda.getFuncionario().getNome());
                venda.setVenda(Boolean.TRUE);
                vendaFacade.salvar(venda);
            } else {
                venda.setVenda(Boolean.TRUE);
                venda.setValida("PJ");
                venda.setNomeFun(venda.getFuncionario().getNome());
                vendaFacade.salvar(venda);
            }
        }
        return "list?faces-redirect=true";

//        } catch (Exception e) {
//            FacesContext.getCurrentInstance().
//                    addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
//                                    "Ocorreu um erro no parcelamento, verifique!", ""));
//            return "form?faces-redirect=true";
//        }
    }

    public String faturarOrc() {

        try {

            if (venda.getPessoa() instanceof PessoaFisica) {
                venda.setValida("PF");

                venda.setVenda(Boolean.FALSE);
                vendaFacade.salvar(venda);
            } else {
                venda.setVenda(Boolean.FALSE);
                venda.setValida("PJ");

                vendaFacade.salvar(venda);
            }

            return "list?faces-redirect=true";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Ocorreu um erro no parcelamento, verifique!", ""));
            return "form?faces-redirect=true";
        }

    }

    public String salvar1() {

        if (venda.getItensVenda().isEmpty()) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Não é possível gerar um orçamento sem adicionar produtos!", ""));
            return "/list?faces-redirect=true";
        } else {

            try {

                if (venda.getId() == null) {
                    Long id1 = vendaFacade.listarTodos().get(0).getId();
                    venda.setId(id1 + 1);
                }

                if (venda.getPessoa() instanceof PessoaFisica) {
                    venda.setVenda(Boolean.FALSE);
                    venda.setValida("PF");
                    venda.setNomeFun(venda.getFuncionario().getNome());

                    vendaFacade.salvar(venda);
                } else {
                    venda.setValida("PJ");
                    venda.setNomeFun(venda.getFuncionario().getNome());
                    venda.setVenda(Boolean.FALSE);

                    vendaFacade.salvar(venda);
                }

                return "/orcamento/list?faces-redirect=true";

            } catch (Exception e) {
                FacesContext.getCurrentInstance().
                        addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                        "Ocorreu um erro no parcelamento, verifique!", ""));
                return "form?faces-redirect=true";
            }

        }
    }

    public String excluir(Venda g) {

        try {

            vendaFacade.carregaItensOs(g);

            vendaFacade.excluir(g);

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Item Excluido com Sucesso!", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Não é possivel, primeiramente é necessário excluir as movimentações financeiras"
                                    + ""
                                    + "!", ""));

        }

        return "list_1?faces-redirect=true";

    }

    public List<Venda> getListaVendas() {
        return vendaFacade.validaVendaDia(aux1);
    }

    public List<Venda> getListaVenda() {
        return vendaFacade.validaVenda();
    }

    public List<Venda> getListaTodas() {
        return vendaFacade.listaVendas();
    }

    public List<Pessoa> getClientes() {
        return pessoaFacade.listaUm();
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarioFacade.funcio();
    }

    public List<Venda> getListaRapido() {
        return vendaFacade.listarRapido("pessoa.nome", id2);
    }

    public List<Venda> getListaOrcamentos() {
        return vendaFacade.listaOrcamentos();
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public ItemVenda getItemVenda() {
        return itemVenda;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTipo2() {
        return tipo2;
    }

    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    public void setValorServicoSelecionado() {
        itemVenda.setValor(itemVenda.getPlantio().getValorVendaPrazo());
        itemVenda.setQuantidade(BigDecimal.ONE);

    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setItemVenda(ItemVenda itemVenda) {
        this.itemVenda = itemVenda;
    }

}

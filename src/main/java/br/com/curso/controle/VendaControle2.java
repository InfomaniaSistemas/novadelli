/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContasReceberFacade;
import br.com.curso.ejb.VendaFacade;
import br.com.curso.entidade.ItemVenda;
import br.com.curso.entidade.Parcela;
import br.com.curso.entidade.PessoaFisica;
import br.com.curso.entidade.Produto;
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

/**
 *
 * @author Miguel
 */
@ManagedBean
@SessionScoped
public class VendaControle2 implements Serializable {

    private Venda venda;

    Date aux1;
    private String tipo;
    private RelatorioControle relatorioControle;
    private ItemVenda itemVenda;
    private Parcela parcela;
    @EJB
    private VendaFacade vendaFacade;
    @EJB
    private ContasReceberFacade receberFacade;
    private GenericConverter converter;

    public VendaControle2() {
        this.aux1 = new Date();
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(vendaFacade);
        }
        return converter;
    }

//    public void addItem() {
////        try {
//        venda.addItem(itemVenda);
//        itemVenda = new ItemVenda();
////        } catch (Exception ex) {
////            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
////                    ex.getMessage(), "Aqui");
////            FacesContext.getCurrentInstance().addMessage(null, message);
////        }
//    }

    public BigDecimal getCredito() {

        BigDecimal credito = BigDecimal.ZERO;

        List<Venda> lista = vendaFacade.validaVendaDia(aux1);
        for (Venda lista1 : lista) {
            credito = credito.add(lista1.getTotalDesc());
        }

        return credito;
    }

    public void enviaProd(Produto prod) {

        itemVenda.setPlantio(prod);

        itemVenda.setValor(prod.getValorVendaPrazo());
        itemVenda.setQuantidade(BigDecimal.ONE);
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

    public void validaCliente() {

        if (venda.getPessoa().getRestricao().equals(true)) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Cliente com restrição, verifique!", ""));

        } else {

        }

    }

    public String novo() {
        venda = new Venda();
        itemVenda = new ItemVenda();
        tipo = "nome";
        return "form?faces-redirect=true";
    }

    public String faturarOrcamento() {

        itemVenda = new ItemVenda();
        parcela = new Parcela();

        vendaFacade.carregaItensOs(venda);

        return "/venda/form?faces-redirect=true";
    }

    public String salvar() {

        try {

            vendaFacade.baixaProd(venda);

            if (!venda.getVenda().equals(false)) {
//                vendaFacade.extornarProdCond(venda);
            }

            if (venda.getPessoa() instanceof PessoaFisica) {
                venda.setValida("PF");
//                if (venda.getTipoPagamento().equals("A VISTA")) {
//                    vendaFacade.lancarCaixa(venda);
//                }

                venda.setVenda(Boolean.TRUE);
                vendaFacade.salvar(venda);
            } else {
                venda.setVenda(Boolean.FALSE);
                venda.setValida("PJ");
//                if (venda.getTipoPagamento().equals("A VISTA")) {
//
//                    vendaFacade.lancarCaixa(venda);
//                }

             
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

    public String faturarOrc() {

        try {

//            vendaFacade.extornarProdCond(venda);

            if (venda.getPessoa() instanceof PessoaFisica) {
                venda.setValida("PF");
//                if (venda.getTipoPagamento().equals("A VISTA")) {
//
//                    vendaFacade.lancarCaixa(venda);
//                }
                venda.setVenda(Boolean.TRUE);
                vendaFacade.salvar(venda);
            } else {
                venda.setVenda(Boolean.TRUE);
                venda.setValida("PJ");
//                if (venda.getTipoPagamento().equals("A VISTA")) {
//
//                    vendaFacade.lancarCaixa(venda);
//                }
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

        try {

            if (venda.getPessoa() instanceof PessoaFisica) {
                venda.setVenda(Boolean.FALSE);
                venda.setValida("PF");
//                vendaFacade.baixaProdCond(venda);
                vendaFacade.salvar(venda);
            } else {
                venda.setValida("PJ");
                venda.setVenda(Boolean.FALSE);
//                vendaFacade.baixaProdCond(venda);
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

    public String excluir(Venda g) {

        try {

            vendaFacade.carregaItensOs(g);
//            vendaFacade.extornarProdCond(g);
            vendaFacade.excluir(g);

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

        return "extornar";

    }

    public List<Venda> getListaVendas() {
        return vendaFacade.validaVendaDia(aux1);
    }

    public List<Venda> getListaTodas() {
        return vendaFacade.listaVendas();
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

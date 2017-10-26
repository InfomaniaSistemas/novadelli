/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CartaoCreditoFacade;
import br.com.curso.entidade.CartaoCredito;
import br.com.curso.entidade.ContaBancaria;
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
public class CartaoCreditoControle implements Serializable {

    private CartaoCredito cartaoCredito;
    private ContaBancaria contaBancaria;
    @EJB
    private CartaoCreditoFacade cartaoCreditoFacade;
    private GenericConverter converter;
    Date aux1;
    Date aux2;

    String valida = "Conta Bancária";

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public Date getAux2() {
        return aux2;
    }

    public void setAux2(Date aux2) {
        this.aux2 = aux2;
    }

    public String getValida() {
        return valida;
    }

    public void setValida(String valida) {
        this.valida = valida;
    }

    public Date getAux1() {
        return aux1;
    }

    public void setAux1(Date aux1) {
        this.aux1 = aux1;
    }

    public BigDecimal getCreditoEntradaNoDia() {

        BigDecimal credito = BigDecimal.ZERO;

        List<CartaoCredito> lista = cartaoCreditoFacade.validaEntradaDia(aux2);
        for (CartaoCredito lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public BigDecimal getCreditoEntradaGeral() {

        BigDecimal credito = BigDecimal.ZERO;

        List<CartaoCredito> lista = cartaoCreditoFacade.validaTotalGeral();
        for (CartaoCredito lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(cartaoCreditoFacade);
        }
        return converter;
    }

    public void validaLista() {

        cartaoCreditoFacade.validaVendaDia(aux1);

    }

    public BigDecimal getCreditoEntrada() {

        BigDecimal credito = BigDecimal.ZERO;

        List<CartaoCredito> lista = cartaoCreditoFacade.validaVendaDia(aux1);
        for (CartaoCredito lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public BigDecimal getSaldo() {

        BigDecimal credito = BigDecimal.ZERO;

        List<CartaoCredito> lista = cartaoCreditoFacade.validaTotal();
        for (CartaoCredito lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public CartaoCreditoControle() {
        this.aux1 = new Date();
        this.aux2 = new Date();

    }

    public String novo() {
        cartaoCredito = new CartaoCredito();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        cartaoCredito = new CartaoCredito();
        return "form1?faces-redirect=true";
    }

    public List<CartaoCredito> autoComplete(String query) {
        return cartaoCreditoFacade.autoComplete("nome", query);
    }

    public void debitar() {

        
        cartaoCreditoFacade.lancarConta(cartaoCredito, cartaoCredito.getValor(), cartaoCredito.getValorSaida(), contaBancaria.getId());
        salvar();
    }

    public String salvar() {

        cartaoCredito.setDtpagamento(new Date());
        cartaoCredito.setSituacao("DEBITADO");
        cartaoCreditoFacade.salvar(cartaoCredito);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        cartaoCreditoFacade.salvar(cartaoCredito);
        return "list1?faces-redirect=true";
    }

    public String excluir(CartaoCredito g) {

        try {
            cartaoCreditoFacade.excluir(g);

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Item Excluido com Sucessor!", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Não é possivel excluir itens com dependência!", ""));

        }

        return "list";

    }

    public List<CartaoCredito> getListaCartaoCreditos() {
        return cartaoCreditoFacade.listarTodos();
    }

    public List<CartaoCredito> getListaPorDia() {
        return cartaoCreditoFacade.validaVendaDia(aux1);
    }

    public CartaoCredito getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(CartaoCredito cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }
}

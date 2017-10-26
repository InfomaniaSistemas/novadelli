/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CartaoDebitoFacade;
import br.com.curso.entidade.CartaoDebito;
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
public class CartaoDebitoControle implements Serializable {

    private CartaoDebito cartaoDebito;
    private ContaBancaria contaBancaria;
    @EJB
    private CartaoDebitoFacade cartaoDebitoFacade;
    private GenericConverter converter;

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public CartaoDebitoFacade getCartaoDebitoFacade() {
        return cartaoDebitoFacade;
    }

    public void setCartaoDebitoFacade(CartaoDebitoFacade cartaoDebitoFacade) {
        this.cartaoDebitoFacade = cartaoDebitoFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    Date aux1;
    Date aux2;
    String valida = "Conta Bancária";

    public String getValida() {
        return valida;
    }

    public void setValida(String valida) {
        this.valida = valida;
    }

    public Date getAux2() {
        return aux2;
    }

    public void setAux2(Date aux2) {
        this.aux2 = aux2;
    }

    public Date getAux1() {
        return aux1;
    }

    public void setAux1(Date aux1) {
        this.aux1 = aux1;
    }

    public CartaoDebitoControle() {
        this.aux1 = new Date();
        this.aux2 = new Date();
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(cartaoDebitoFacade);
        }
        return converter;
    }

    public void validaLista() {

        cartaoDebitoFacade.validaVendaDia(aux1);

    }

    public String debitar() {

        System.out.println("Meu id" + contaBancaria.getId());
        cartaoDebitoFacade.lancarConta(cartaoDebito, cartaoDebito.getValor(), cartaoDebito.getValorSaida(), contaBancaria.getId());
        salvar();
        return "list?faces-redirect=true";
        
    }

    public BigDecimal getCreditoEntradaNoDia() {

        BigDecimal credito = BigDecimal.ZERO;

        List<CartaoDebito> lista = cartaoDebitoFacade.validaEntradaDia(aux2);
        for (CartaoDebito lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public BigDecimal getCreditoEntradaGeral() {

        BigDecimal credito = BigDecimal.ZERO;

        List<CartaoDebito> lista = cartaoDebitoFacade.validaTotalGeral();
        for (CartaoDebito lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public BigDecimal getCreditoEntrada() {

        BigDecimal credito = BigDecimal.ZERO;

        List<CartaoDebito> lista = cartaoDebitoFacade.validaVendaDia(aux1);
        for (CartaoDebito lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public String novo() {
        cartaoDebito = new CartaoDebito();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        cartaoDebito = new CartaoDebito();
        return "form1?faces-redirect=true";
    }

    public List<CartaoDebito> autoComplete(String query) {
        return cartaoDebitoFacade.autoComplete("nome", query);
    }

    public String salvar() {
        cartaoDebito.setDtpagamento(new Date());
        cartaoDebito.setSituacao("DEBITADO");
        cartaoDebitoFacade.salvar(cartaoDebito);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        cartaoDebitoFacade.salvar(cartaoDebito);
        return "list1?faces-redirect=true";
    }

    public String excluir(CartaoDebito g) {

        try {
            cartaoDebitoFacade.excluir(g);

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

    public List<CartaoDebito> getListaCartaoDebitos() {
        return cartaoDebitoFacade.listarTodos();
    }

    public List<CartaoDebito> getListadia() {
        return cartaoDebitoFacade.validaVendaDia(aux1);
    }

    public CartaoDebito getCartaoDebito() {
        return cartaoDebito;
    }

    public void setCartaoDebito(CartaoDebito cartaoDebito) {
        this.cartaoDebito = cartaoDebito;
    }
}

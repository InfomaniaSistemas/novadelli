/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ChequeFacade;
import br.com.curso.entidade.Cheque;
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
public class ChequeControle implements Serializable {

    private Cheque cheque;
    private String tipo;
    private ContaBancaria contaBancaria;
    @EJB
    private ChequeFacade chequeFacade;
    private GenericConverter converter;
    Date aux1;
    Date aux2;
    String valida = "Conta Caixa";

    public String getValida() {
        return valida;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String salvarEntrada() {
        cheque.setValorSaida(BigDecimal.ZERO);

        cheque.setSituacao("A DEBITAR");
        cheque.maiusculo();
        chequeFacade.salvar(cheque);
        return "list?faces-redirect=true";
    }

    public String salvarSaida() {
        cheque.setValor(BigDecimal.ZERO);

        cheque.setSituacao("A DEBITAR");
        cheque.maiusculo();
        chequeFacade.salvar(cheque);
        return "list?faces-redirect=true";
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public ChequeFacade getChequeFacade() {
        return chequeFacade;
    }

    public void setChequeFacade(ChequeFacade chequeFacade) {
        this.chequeFacade = chequeFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    public Date getAux2() {
        return aux2;
    }

    public void setAux2(Date aux2) {
        this.aux2 = aux2;
    }

    public void setValida(String valida) {
        this.valida = valida;
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(chequeFacade);
        }
        return converter;
    }

    public void debitar() {

        if (valida.equals("Conta Caixa")) {

            chequeFacade.lancarCaixa(cheque, cheque.getValor(), cheque.getValorSaida());
            salvar();
        } else {
            chequeFacade.lancarConta(cheque, cheque.getValor(), cheque.getValorSaida(), contaBancaria.getId());
            salvar();
        }

    }

    public String novo() {
        cheque = new Cheque();
        tipo = "ENTRADA";
        return "form_2?faces-redirect=true";
    }

    public String novo1() {
        cheque = new Cheque();
        return "form1?faces-redirect=true";
    }

    public List<Cheque> autoComplete(String query) {
        return chequeFacade.autoComplete("nome", query);
    }

    public void validaLista() {

        chequeFacade.validaVendaDia(aux1);

    }

    public BigDecimal getCreditoEntrada() {

        BigDecimal credito = BigDecimal.ZERO;

        List<Cheque> lista = chequeFacade.validaTotalADebitar();
        for (Cheque lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }
    
    public BigDecimal getCreditoSaida() {

        BigDecimal credito = BigDecimal.ZERO;

        List<Cheque> lista = chequeFacade.validaTotalADebitar();
        for (Cheque lista1 : lista) {

            credito = credito.add(lista1.getValorSaida());
        }
        return credito;
    }

    public BigDecimal getCreditoEntradaNoDia() {

        BigDecimal credito = BigDecimal.ZERO;

        List<Cheque> lista = chequeFacade.validaEntradaDia(aux2);
        for (Cheque lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public BigDecimal getCreditoEntradaGeral() {

        BigDecimal credito = BigDecimal.ZERO;

        List<Cheque> lista = chequeFacade.validaTotalGeral();
        for (Cheque lista1 : lista) {

            credito = credito.add(lista1.getValor());
        }
        return credito;
    }

    public Date getAux1() {
        return aux1;
    }

    public void setAux1(Date aux1) {
        this.aux1 = aux1;
    }

    public ChequeControle() {
        this.aux1 = new Date();
        this.aux2 = new Date();
    }

    public String salvar() {
        cheque.setDtpagamento(new Date());
        cheque.setSituacao("DEBITADO");
        chequeFacade.salvar(cheque);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        chequeFacade.salvar(cheque);
        return "list1?faces-redirect=true";
    }

    public String excluir(Cheque g) {

        try {
            chequeFacade.excluir(g);

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

    public List<Cheque> getListaCheques() {
        return chequeFacade.validaVendaDia(aux1);
    }

    public Cheque getCheque() {
        return cheque;
    }

    public void setCheque(Cheque cheque) {
        this.cheque = cheque;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CaixaFacade;
import br.com.curso.ejb.ContaBancariaFacade;
import br.com.curso.ejb.LancamentoBancarioFacade;
import br.com.curso.entidade.Caixa;
import br.com.curso.entidade.ContaBancaria;
import br.com.curso.entidade.LancamentoBancario;
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
public class LancamentoContaControle implements Serializable {

    private LancamentoBancario lancamentoBancario;
    private Caixa caixa;
    private ContaBancaria contaBancaria;
    Long id = Long.MIN_VALUE;
    private String tipo;
    Date aux1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @EJB
    private LancamentoBancarioFacade lancamentoBancarioFacade;
    @EJB
    private ContaBancariaFacade contaBancariaFacade;
    private GenericConverter converter;

    public Date getAux1() {
        return aux1;
    }

    public void setAux1(Date aux1) {
        this.aux1 = aux1;
    }

    public LancamentoContaControle() {
        this.aux1 = new Date();
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(lancamentoBancarioFacade);
        }
        return converter;
    }

    public void pegaId() {

        id = contaBancaria.getId();

    }

    public BigDecimal getSaldoTotal() {

        BigDecimal valor = BigDecimal.ZERO;

        List<ContaBancaria> lista = contaBancariaFacade.validaVendaDia2(id);

        if (!lista.isEmpty()) {

            valor = lista.get(0).getSaldo();

        } else {

            valor = BigDecimal.ZERO;
        }

        return valor;
    }

    public BigDecimal getCreditoEntrada() {

        BigDecimal credito = BigDecimal.ZERO;

        List<LancamentoBancario> lista = lancamentoBancarioFacade.validaVendaDia2(aux1, contaBancaria);
        for (LancamentoBancario lista1 : lista) {

            credito = credito.add(lista1.getLacamentoEntrada());
        }
        return credito;
    }

    public BigDecimal getSaldo() {

        BigDecimal saldo = getCreditoEntrada().subtract(getDebitoSaida());
        return saldo;
    }

    public BigDecimal getDebitoSaida() {

        BigDecimal credito = BigDecimal.ZERO;

        List<LancamentoBancario> lista = lancamentoBancarioFacade.validaVendaDia2(aux1, contaBancaria);
        for (LancamentoBancario lista1 : lista) {

            credito = credito.add(lista1.getLacamentoSaida());
        }
        return credito;
    }

    public BigDecimal getCreditoSaida() {

        BigDecimal debito = BigDecimal.ZERO;

        List<LancamentoBancario> lista = lancamentoBancarioFacade.validaVendaDia(aux1);
        for (LancamentoBancario lista1 : lista) {

            debito = debito.add(lista1.getLacamentoSaida());
        }

        return debito;
    }

    public BigDecimal saldo() {

        BigDecimal saldo = getCreditoEntrada().subtract(getCreditoSaida());

        return saldo;

    }

    public String novo1() {
        lancamentoBancario = new LancamentoBancario();
        return "form_1?faces-redirect=true";
    }

    public String novo() {
        lancamentoBancario = new LancamentoBancario();
        tipo = "ENTRADA";
        return "form_1?faces-redirect=true";
    }

    public List<LancamentoBancario> autoComplete(String query) {
        return lancamentoBancarioFacade.autoComplete("nome", query);
    }

    public String salvarEntrada() {
        lancamentoBancario.setLacamentoSaida(BigDecimal.ZERO);
        lancamentoBancario.entrada();
    
        lancamentoBancario.maiuscula();
        lancamentoBancario.setTipo("ENTRADA");

        lancamentoBancarioFacade.salvar(lancamentoBancario);
        return "list?faces-redirect=true";
    }

    public String salvarSaida() {
        lancamentoBancario.setLacamentoEntrada(BigDecimal.ZERO);
        lancamentoBancario.saida();
    
        lancamentoBancario.maiuscula();
        lancamentoBancario.setTipo("SAÍDA");

        lancamentoBancarioFacade.salvar(lancamentoBancario);
        return "list?faces-redirect=true";
    }

    public String salvar1() {

        lancamentoBancarioFacade.salvar(lancamentoBancario);
        return "list1?faces-redirect=true";
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public ContaBancariaFacade getContaBancariaFacade() {
        return contaBancariaFacade;
    }

    public void setContaBancariaFacade(ContaBancariaFacade contaBancariaFacade) {
        this.contaBancariaFacade = contaBancariaFacade;
    }

    public List<LancamentoBancario> getLancamentoDia() {
        return lancamentoBancarioFacade.validaVendaDia2(aux1, contaBancaria);
    }

    public void validaLista() {

        lancamentoBancarioFacade.validaVendaDia(aux1);
        lancamentoBancarioFacade.validaVendaDia2(aux1, contaBancaria);
        pegaId();

    }

    public String excluir(LancamentoBancario g) {

        try {

            lancamentoBancario.saida();

            lancamentoBancarioFacade.excluir(g);

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

    public List<LancamentoBancario> getListaLancamentoBancarios() {
        return lancamentoBancarioFacade.listarTodos();
    }

    public LancamentoBancario getLancamentoBancario() {
        return lancamentoBancario;
    }

    public void setLancamentoBancario(LancamentoBancario lancamentoBancario) {
        this.lancamentoBancario = lancamentoBancario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LancamentoBancarioFacade getLancamentoBancarioFacade() {
        return lancamentoBancarioFacade;
    }

    public void setLancamentoBancarioFacade(LancamentoBancarioFacade lancamentoBancarioFacade) {
        this.lancamentoBancarioFacade = lancamentoBancarioFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

}

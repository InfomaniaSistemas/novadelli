/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CaixaFacade;
import br.com.curso.ejb.LancamentoCaixaFacade;
import br.com.curso.entidade.Caixa;
import br.com.curso.entidade.LancamentoCaixa;
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
public class LancamentoCaixaControle implements Serializable {

    private LancamentoCaixa lancamentoCaixa;
    private Caixa caixa;
    private String tipo;
    Date aux1;

    @EJB
    private LancamentoCaixaFacade lancamentoCaixaFacade;
    @EJB
    private CaixaFacade caixaFacade;
    private GenericConverter converter;

    public Date getAux1() {
        return aux1;
    }

    public void setAux1(Date aux1) {
        this.aux1 = aux1;
    }

    public LancamentoCaixaControle() {
        this.aux1 = new Date();
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(lancamentoCaixaFacade);
        }
        return converter;
    }

    public BigDecimal getCreditoEntrada() {

        BigDecimal credito = BigDecimal.ZERO;

        List<LancamentoCaixa> lista = lancamentoCaixaFacade.validaVendaDia(aux1);
        for (LancamentoCaixa lista1 : lista) {

            credito = credito.add(lista1.getLacamentoEntrada());
        }
        return credito;
    }

    public BigDecimal getCreditoSaida() {

        BigDecimal debito = BigDecimal.ZERO;

        List<LancamentoCaixa> lista = lancamentoCaixaFacade.validaVendaDia(aux1);
        for (LancamentoCaixa lista1 : lista) {

            debito = debito.add(lista1.getLacamentoSaida());
        }

        return debito;
    }

    
        public BigDecimal getCreditoEntradaAnterior() {

        BigDecimal credito = BigDecimal.ZERO;

        List<LancamentoCaixa> lista = lancamentoCaixaFacade.validaSaldoAnterior(aux1);
        for (LancamentoCaixa lista1 : lista) {

            credito = credito.add(lista1.getLacamentoEntrada());
        }
        return credito;
    }

    public BigDecimal getCreditoSaidaAnterior() {

        BigDecimal debito = BigDecimal.ZERO;

        List<LancamentoCaixa> lista = lancamentoCaixaFacade.validaSaldoAnterior(aux1);
        for (LancamentoCaixa lista1 : lista) {

            debito = debito.add(lista1.getLacamentoSaida());
        }

        return debito;
    }

    
    public BigDecimal getSaldoTotal() {

        BigDecimal valor = BigDecimal.ZERO;

        List<Caixa> lista = caixaFacade.listarTodos();

        valor = lista.get(0).getSaldo();

        return valor;
    }

    public BigDecimal saldo() {

        BigDecimal saldo = getCreditoEntrada().subtract(getCreditoSaida());

        return saldo;

    }

    public BigDecimal saldoAteDia() {

         BigDecimal saldo = getCreditoEntradaAnterior().subtract(getCreditoSaidaAnterior());

        return saldo;

    }

    public String novo1() {
        lancamentoCaixa = new LancamentoCaixa();
        return "form1?faces-redirect=true";
    }

    public String novo() {
        lancamentoCaixa = new LancamentoCaixa();
        tipo = "ENTRADA";
        return "form?faces-redirect=true";
    }

    public List<LancamentoCaixa> autoComplete(String query) {
        return lancamentoCaixaFacade.autoComplete("nome", query);
    }

    public String salvarEntrada() {
        lancamentoCaixa.setLacamentoSaida(BigDecimal.ZERO);
        lancamentoCaixa.entrada();
        lancamentoCaixa.maiuscula();
        lancamentoCaixa.setTipo("ENTRADA");

        lancamentoCaixaFacade.salvar(lancamentoCaixa);
        return "list?faces-redirect=true";
    }

    public String salvarSaida() {
        lancamentoCaixa.setLacamentoEntrada(BigDecimal.ZERO);
        lancamentoCaixa.saida();

        lancamentoCaixa.maiuscula();
        lancamentoCaixa.setTipo("SAÍDA");

        lancamentoCaixaFacade.salvar(lancamentoCaixa);
        return "list?faces-redirect=true";
    }

    public String salvar1() {

        lancamentoCaixaFacade.salvar(lancamentoCaixa);
        return "list1?faces-redirect=true";
    }

    public List<LancamentoCaixa> getLancamentoDia() {
        return lancamentoCaixaFacade.validaVendaDia(aux1);
    }

    public void validaLista() {

        lancamentoCaixaFacade.validaVendaDia(aux1);

    }

    public String excluir(LancamentoCaixa g) {

        try {

            lancamentoCaixa.saida();

            lancamentoCaixaFacade.excluir(g);

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

    public List<LancamentoCaixa> getListaLancamentoCaixas() {
        return lancamentoCaixaFacade.listarTodos();
    }

    public LancamentoCaixa getLancamentoCaixa() {
        return lancamentoCaixa;
    }

    public void setLancamentoCaixa(LancamentoCaixa lancamentoCaixa) {
        this.lancamentoCaixa = lancamentoCaixa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LancamentoCaixaFacade getLancamentoCaixaFacade() {
        return lancamentoCaixaFacade;
    }

    public void setLancamentoCaixaFacade(LancamentoCaixaFacade lancamentoCaixaFacade) {
        this.lancamentoCaixaFacade = lancamentoCaixaFacade;
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

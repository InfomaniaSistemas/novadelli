/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContasPagarFacade;
import br.com.curso.entidade.ContaBancaria;
import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.Empresa;
import br.com.curso.entidade.Fornecedor;
import br.com.curso.entidade.LancamentoCaixa;
import br.com.curso.entidade.Pessoa;
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
public class ContasPagarControle implements Serializable {

    Date aux1;
    private Date dtvencimento;
    private Date dtvencimento2;
    private ContasPagar contasPagar;
    private Pessoa pessoa;
    private Empresa empresa;
    private String tipoBusca;
    private String nota;
    private ContaBancaria contaBancaria;
    private LancamentoCaixa caixa;
    @EJB
    private ContasPagarFacade contasPagarFacade;
    private GenericConverter converter;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Date getDtvencimento2() {
        return dtvencimento2;
    }

    public void setDtvencimento2(Date dtvencimento2) {
        this.dtvencimento2 = dtvencimento2;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Date getDtvencimento() {
        return dtvencimento;
    }

    public void setDtvencimento(Date dtvencimento) {
        this.dtvencimento = dtvencimento;
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(contasPagarFacade);
        }
        return converter;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getAux1() {
        return aux1;
    }

    public void setAux1(Date aux1) {
        this.aux1 = aux1;
    }

    public String getTipoBusca() {
        return tipoBusca;
    }

    public void setTipoBusca(String tipoBusca) {
        this.tipoBusca = tipoBusca;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public LancamentoCaixa getCaixa() {
        return caixa;
    }

    public void setCaixa(LancamentoCaixa caixa) {
        this.caixa = caixa;
    }

    public ContasPagarControle() {
        this.aux1 = new Date();
    }

    public String editar1() {

        if (contasPagar.getRestosAPagar() == null) {

            contasPagar.setRestosAPagar(contasPagar.getValor());
            contasPagar.setJuros(BigDecimal.ZERO);
            contasPagar.setDesconto(BigDecimal.ZERO);
            return "form_1_1?faces-redirect=true";

        } else {

            contasPagar.setValorPago(BigDecimal.ZERO);
            contasPagar.setJuros(BigDecimal.ZERO);

            contasPagar.setDtPagamento(null);
            contasPagar.setValor(contasPagar.getRestosAPagar());
            contasPagar.setRestosAPagar(contasPagar.getValor());
            return "form_1_1?faces-redirect=true";
        }

    }

    public String editar() {
        contasPagar.setDtPagamento(null);
        contasPagar.setTipoDocumento("DINHEIRO");
        if (contasPagar.getRestosAPagar() == null) {

            contasPagar.setRestosAPagar(contasPagar.getValor());
            contasPagar.setJuros(BigDecimal.ZERO);
            contasPagar.setDesconto(BigDecimal.ZERO);
            contasPagar.setUltimoPagamento(BigDecimal.ZERO);
            return "form_1?faces-redirect=true";

        } else {

            contasPagar.setUltimoPagamento(contasPagar.getValorPago());
            contasPagar.setValorPago(BigDecimal.ZERO);
            contasPagar.setJuros(BigDecimal.ZERO);
            contasPagar.setDesconto(BigDecimal.ZERO);

            contasPagar.setDtPagamento(null);
            contasPagar.setValor(contasPagar.getRestosAPagar());
            contasPagar.setRestosAPagar(contasPagar.getValor());
            return "form_1?faces-redirect=true";
        }

    }

    public String novo() {
        contasPagar = new ContasPagar();
        return "form?faces-redirect=true";
    }

    public String novo2() {
        contasPagar = new ContasPagar();
        return "form_2?faces-redirect=true";
    }

    public String novo1() {
        contasPagar = new ContasPagar();
        return "form1?faces-redirect=true";
    }

    public List<ContasPagar> autoComplete(String query) {
        return contasPagarFacade.autoComplete("nome", query);
    }

    public String salvar() {

        Long id1 = contasPagarFacade.listarTodosCompleto().get(0).getId();
        contasPagar.setId(id1 + 1);
        contasPagar.setValorPrincipal(contasPagar.getValor());
        contasPagar.setValorAPagar(contasPagar.getValor());
        contasPagar.setStatus("A PAGAR");
        contasPagarFacade.salvar(contasPagar);
        return "list?faces-redirect=true";
    }

    public BigDecimal getTotal() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasPagar> lista = contasPagarFacade.listarApagar();
        for (ContasPagar lista1 : lista) {

            credito = credito.add(lista1.getValorPrincipal());
        }
        return credito;
    }

    public BigDecimal getAreceber() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasPagar> lista = contasPagarFacade.listarApagar();
        for (ContasPagar lista1 : lista) {

            credito = credito.add(lista1.getValorPrincipal());
        }
        return credito;
    }

    public BigDecimal getVencimento() {

        BigDecimal vencimento = BigDecimal.ZERO;

        List<ContasPagar> lista = contasPagarFacade.contasVencidas(aux1);
        for (ContasPagar lista1 : lista) {

            vencimento = vencimento.add(lista1.getValorAPagar());
        }
        return vencimento;
    }

    public String salvar1() {

        contasPagar.validaP();

        if (contasPagar.getTipoDocumento().equals("DINHEIRO")) {
            contasPagarFacade.lancarCaixa(contasPagar);

        } else if (contasPagar.getTipoDocumento().equals("CARTÃO CRÉDITO")) {

            contasPagarFacade.lancarCartao(contasPagar);

        } else if (contasPagar.getTipoDocumento().equals("CARTÃO DÉBITO")) {

            contasPagarFacade.lancarCartaoDebito(contasPagar);

        } else if (contasPagar.getTipoDocumento().equals("CHEQUE")) {

            contasPagarFacade.lancarCheque(contasPagar);
        } else if (contasPagar.getTipoDocumento().equals("CONTA BANCÁRIA")) {

            contasPagarFacade.lancarConta(contasPagar, contasPagar.getUltimoPagamento(), contaBancaria.getId());
        }

        contasPagarFacade.salvar(contasPagar);
        return "list?faces-redirect=true";

    }

//    public String salvar2() {
//
//        String aux = contasPagar.validaP();
//
//        if (aux.equals("pago")) {
//
//            contasPagar.setStatus("PAGO");
//            contasPagarFacade.salvar(contasPagar);
//            return "list?faces-redirect=true";
//        } else {
//            contasPagar.setStatus("A PAGAR");
//            contasPagarFacade.salvar(contasPagar);
//            return "list?faces-redirect=true";
//
//        }
//
//    }
    public String excluir(ContasPagar g) {

        try {

            contasPagarFacade.excluir(g);

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Item Excluido com Sucesso!", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "A OS deve conter pelo menos um item de serviço!", ""));

        }

        return "list";

    }

    public ContasPagarFacade getContasPagarFacade() {
        return contasPagarFacade;
    }

    public void setContasPagarFacade(ContasPagarFacade contasPagarFacade) {
        this.contasPagarFacade = contasPagarFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    public List<ContasPagar> getListaContasPagars() {
        return contasPagarFacade.listarApagar();
    }

    public List<ContasPagar> getListaContasPagarsPorCliente() {
        return contasPagarFacade.listarApagarCliente(pessoa);
    }

    public List<ContasPagar> getListaContasPagarsPorFilial() {
        return contasPagarFacade.listarApagarFilial(empresa);
    }

    public List<ContasPagar> getListaContasPagarsPorVencimento() {
        return contasPagarFacade.listarApagarDtVendimento(dtvencimento, dtvencimento2);
    }

    public List<ContasPagar> getListaContasPagarsPorNota() {
        return contasPagarFacade.listarApagarNota(nota);
    }

    public List<ContasPagar> getListaContasPagars1() {
        return contasPagarFacade.listarApagar1();
    }

    public List<ContasPagar> getListaRestos() {
        return contasPagarFacade.listarRestoaApagar();
    }

    public List<ContasPagar> getListarTodas() {
        return contasPagarFacade.listarTodos();
    }

    public ContasPagar getContasPagar() {
        return contasPagar;
    }

    public void validaListaPessoa() {

        contasPagarFacade.listarApagarCliente(pessoa);

    }

    public void validaListaFilial() {

        contasPagarFacade.listarApagarFilial(empresa);

    }

    public void validaListaVencimento() {

        contasPagarFacade.listarApagarDtVendimento(dtvencimento, dtvencimento2);

    }

    public void validaListaNota() {

        contasPagarFacade.listarApagarNota(nota);

    }

    public void setContasPagar(ContasPagar contasPagar) {
        this.contasPagar = contasPagar;
    }
}

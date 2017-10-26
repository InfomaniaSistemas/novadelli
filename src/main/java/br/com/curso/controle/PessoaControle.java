/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContasPagarFacade;
import br.com.curso.ejb.ContasReceberFacade;
import br.com.curso.ejb.ContasRecebidasFacade;
import br.com.curso.ejb.PessoaFacade;
import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.ContasReceber;
import br.com.curso.entidade.ContasRecebidas;
import br.com.curso.entidade.Pessoa;
import br.com.curso.entidade.PessoaFisica;
import br.com.curso.entidade.PessoaJuridica;
import br.com.curso.entidade.Restricao;
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
public class PessoaControle implements Serializable {

    private Pessoa pessoa;
    private Restricao restricao;
    @EJB
    private PessoaFacade pessoaFacade;
    @EJB
    private ContasPagarFacade contasPagarFacade;
    @EJB
    private ContasReceberFacade contasReceberFacade;
    @EJB
    private ContasRecebidasFacade contasRecebidasFacade;
    private GenericConverter converter;
    private String tipoPessoa;
    private String tipo2 = "nome";
    private String id2 = "";

    public String getTipo2() {
        return tipo2;
    }

    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(pessoaFacade);
        }
        return converter;
    }

    public String novo() {
        tipoPessoa = "PF";
        criaPessoa();
        return "form?faces-redirect=true";
    }

    public List<Pessoa> autoComplete(String query) {
        return pessoaFacade.autoCompletePessoa("nome", query);
    }

    public BigDecimal getValidaMensalidadePago() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasRecebidas> lista = contasRecebidasFacade.validaMensalidadePago(pessoa);
        for (ContasRecebidas lista1 : lista) {
            credito = credito.add(lista1.getValorPrincipal());
        }

        return credito;
    }

    public BigDecimal getValidaAPagar() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasPagar> lista = contasPagarFacade.validaAPagar(pessoa);
        for (ContasPagar lista1 : lista) {
            credito = credito.add(lista1.getValorPrincipal());
        }

        return credito;
    }

    public BigDecimal getValidaMensalidadeApagar() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasReceber> lista = contasReceberFacade.validaMensalidadeAPagar(pessoa);
        for (ContasReceber lista1 : lista) {
            credito = credito.add(lista1.getValorAPagar());
        }

        return credito;
    }

    public BigDecimal getValidaContasPagas() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasPagar> lista = contasPagarFacade.validaPago(pessoa);
        for (ContasPagar lista1 : lista) {
            credito = credito.add(lista1.getValorPago());
        }

        return credito;
    }

    public void removeItem() {
        try {
            pessoa.removeItem(restricao);
            pessoa.setRestricao(Boolean.FALSE);
            restricao = new Restricao();
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void addItem() {
        try {
            pessoa.addItem(restricao);
            restricao = new Restricao();
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), " Há algo errado!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public String editar() {

        pessoaFacade.carregaItensOs(pessoa);
        restricao = new Restricao();

        return "form_1?faces-redirect=true";
    }

    public void criaPessoa() {
        if (tipoPessoa.equals("PF")) {
            pessoa = new PessoaFisica();
            pessoa.setDtCadasrto(new Date());
        } else {
            pessoa = new PessoaJuridica();
            pessoa.setDtCadasrto(new Date());
        }
    }

    public String salvar() {

        if (pessoa.getId() == null) {
            Long id1 = pessoaFacade.listarTodosCompleto().get(0).getId();
            pessoa.setId(id1 + 1);
        }

        pessoa.maiusculo();
        pessoaFacade.salvar(pessoa);
        return "list?faces-redirect=true";
    }

    public String excluir(Pessoa g) {

        try {
            pessoaFacade.excluir(g);

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

    public List<Pessoa> getListaPessoas() {
        return pessoaFacade.listarTodosCompletoPessoa();
    }

    public List<ContasRecebidas> getListaContasRecebidas() {
        return contasRecebidasFacade.validaMensalidadePago(pessoa);
    }

//    public List<ContasReceber> getTotalRecebido() {
//        return contasReceberFacade.somaRecebidas(pessoa);
//    }
    public List<ContasPagar> getListaContasAPagar() {
        return contasPagarFacade.validaAPagar(pessoa);
    }

    public List<ContasPagar> getListaContasPagas() {
        return contasPagarFacade.validaPago(pessoa);
    }

    public List<ContasReceber> getListaContasAreceber() {
        return contasReceberFacade.validaMensalidadeAPagar(pessoa);
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        if (pessoa instanceof PessoaFisica) {
            tipoPessoa = "PF";
        } else {
            tipoPessoa = "PJ";
        }
        this.pessoa = pessoa;
    }

    public PessoaFacade getPessoaFacade() {
        return pessoaFacade;
    }

    public void setPessoaFacade(PessoaFacade pessoaFacade) {
        this.pessoaFacade = pessoaFacade;
    }

    public ContasReceberFacade getContasReceberFacade() {
        return contasReceberFacade;
    }

    public void setContasReceberFacade(ContasReceberFacade contasReceberFacade) {
        this.contasReceberFacade = contasReceberFacade;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Restricao getRestricao() {
        return restricao;
    }

    public void setRestricao(Restricao restricao) {
        this.restricao = restricao;
    }

}

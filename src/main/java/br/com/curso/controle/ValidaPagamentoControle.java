/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ValidaPagamentoFacade;
import br.com.curso.entidade.ValidaPagamento;
import java.io.Serializable;
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
public class ValidaPagamentoControle implements Serializable {

    private ValidaPagamento validaPagamento;
    @EJB
    private ValidaPagamentoFacade validaPagamentoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(validaPagamentoFacade);
        }
        return converter;
    }

    public String novo() {
        validaPagamento = new ValidaPagamento();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        validaPagamento = new ValidaPagamento();
        return "form1?faces-redirect=true";
    }

    public List<ValidaPagamento> autoComplete(String query) {
        return validaPagamentoFacade.autoComplete("nome", query);
    }

    public String salvar() {

        validaPagamentoFacade.salvar(validaPagamento);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        validaPagamentoFacade.salvar(validaPagamento);
        return "list1?faces-redirect=true";
    }

    public String excluir(ValidaPagamento g) {

        try {
            validaPagamentoFacade.excluir(g);

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

    public List<ValidaPagamento> getListaValidaPagamentos() {
        return validaPagamentoFacade.listarTodos();
    }

    public ValidaPagamento getValidaPagamento() {
        return validaPagamento;
    }

    public void setValidaPagamento(ValidaPagamento validaPagamento) {
        this.validaPagamento = validaPagamento;
    }
}

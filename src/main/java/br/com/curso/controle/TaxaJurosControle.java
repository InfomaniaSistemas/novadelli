/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.TaxaJurosFacade;
import br.com.curso.entidade.TaxaJuros;
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
public class TaxaJurosControle implements Serializable {

    private TaxaJuros taxaJuros;
    @EJB
    private TaxaJurosFacade taxaJurosFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(taxaJurosFacade);
        }
        return converter;
    }

    public String novo() {
        taxaJuros = new TaxaJuros();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        taxaJuros = new TaxaJuros();
        return "form1?faces-redirect=true";
    }

    public List<TaxaJuros> autoComplete(String query) {
        return taxaJurosFacade.autoComplete("nome", query);
    }

    public String salvar() {

        taxaJurosFacade.salvar(taxaJuros);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        taxaJurosFacade.salvar(taxaJuros);
        return "list1?faces-redirect=true";
    }

    public String excluir(TaxaJuros g) {

        try {
            taxaJurosFacade.excluir(g);

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

    public List<TaxaJuros> getListaTaxaJuross() {
        return taxaJurosFacade.listarTodos();
    }

    public TaxaJuros getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(TaxaJuros taxaJuros) {
        this.taxaJuros = taxaJuros;
    }
}

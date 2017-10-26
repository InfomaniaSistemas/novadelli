/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CofinsFacade;
import br.com.curso.entidade.Cofins;
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
public class CofinsControle implements Serializable {

    private Cofins cofins;
    @EJB
    private CofinsFacade cofinsFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(cofinsFacade);
        }
        return converter;
    }

    public String novo() {
        cofins = new Cofins();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        cofins = new Cofins();
        return "form1?faces-redirect=true";
    }

    public List<Cofins> autoComplete(String query) {
        return cofinsFacade.autoComplete("cst", query);
    }

    public String salvar() {

        cofinsFacade.salvar(cofins);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        cofinsFacade.salvar(cofins);
        return "list1?faces-redirect=true";
    }

    public String excluir(Cofins g) {

        try {
            cofinsFacade.excluir(g);

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

    public List<Cofins> getListaCofinss() {
        return cofinsFacade.listarTodos();
    }

    public Cofins getCofins() {
        return cofins;
    }

    public void setCofins(Cofins cofins) {
        this.cofins = cofins;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.PisFacade;
import br.com.curso.entidade.Pis;
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
public class PisControle implements Serializable {

    private Pis pis;
    @EJB
    private PisFacade pisFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(pisFacade);
        }
        return converter;
    }

    public String novo() {
        pis = new Pis();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        pis = new Pis();
        return "form1?faces-redirect=true";
    }

    public List<Pis> autoComplete(String query) {
        return pisFacade.autoComplete("cst", query);
    }

    public String salvar() {

        pisFacade.salvar(pis);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        pisFacade.salvar(pis);
        return "list1?faces-redirect=true";
    }

    public String excluir(Pis g) {

        try {
            pisFacade.excluir(g);

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

    public List<Pis> getListaPiss() {
        return pisFacade.listarTodos();
    }

    public Pis getPis() {
        return pis;
    }

    public void setPis(Pis pis) {
        this.pis = pis;
    }
}

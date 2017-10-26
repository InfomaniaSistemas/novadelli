/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.IcmsFacade;
import br.com.curso.entidade.Icms;
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
public class IcmsControle implements Serializable {

    private Icms icms;
    @EJB
    private IcmsFacade icmsFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(icmsFacade);
        }
        return converter;
    }

    public String novo() {
        icms = new Icms();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        icms = new Icms();
        return "form1?faces-redirect=true";
    }

    public List<Icms> autoComplete(String query) {
        return icmsFacade.autoComplete("modalidade", query);
    }

    public String salvar() {

        icmsFacade.salvar(icms);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        icmsFacade.salvar(icms);
        return "list1?faces-redirect=true";
    }

    public String excluir(Icms g) {

        try {
            icmsFacade.excluir(g);

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

    public List<Icms> getListaIcmss() {
        return icmsFacade.listarTodosCompleto();
    }

    public Icms getIcms() {
        return icms;
    }

    public void setIcms(Icms icms) {
        this.icms = icms;
    }
}

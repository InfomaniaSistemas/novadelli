/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CfopFacade;
import br.com.curso.entidade.Cfop;
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
public class CfopControle implements Serializable {

    private Cfop cfop;
    @EJB
    private CfopFacade cfopFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(cfopFacade);
        }
        return converter;
    }

    public String novo() {
        cfop = new Cfop();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        cfop = new Cfop();
        return "form1?faces-redirect=true";
    }

    public List<Cfop> autoComplete(String query) {
        return cfopFacade.autoComplete("CFOPNatureza", query);
    }

    public String salvar() {
               
        cfopFacade.salvar(cfop);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        cfopFacade.salvar(cfop);
        return "list1?faces-redirect=true";
    }

    public String excluir(Cfop g) {

        try {
            cfopFacade.excluir(g);

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

    public List<Cfop> getListaCfops() {
        return cfopFacade.listarTodos();
    }

    public Cfop getCfop() {
        return cfop;
    }

    public void setCfop(Cfop cfop) {
        this.cfop = cfop;
    }
}

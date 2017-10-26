/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.IpiFacade;
import br.com.curso.entidade.Ipi;
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
public class IpiControle implements Serializable {

    private Ipi ipi;
    @EJB
    private IpiFacade ipiFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(ipiFacade);
        }
        return converter;
    }

    public String novo() {
        ipi = new Ipi();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        ipi = new Ipi();
        return "form1?faces-redirect=true";
    }

    public List<Ipi> autoComplete(String query) {
        return ipiFacade.autoComplete("codigo", query);
    }

    public String salvar() {

        ipiFacade.salvar(ipi);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        ipiFacade.salvar(ipi);
        return "list1?faces-redirect=true";
    }

    public String excluir(Ipi g) {

        try {
            ipiFacade.excluir(g);

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

    public List<Ipi> getListaIpis() {
        return ipiFacade.listarTodosCompleto();
    }

    public Ipi getIpi() {
        return ipi;
    }

    public void setIpi(Ipi ipi) {
        this.ipi = ipi;
    }
}

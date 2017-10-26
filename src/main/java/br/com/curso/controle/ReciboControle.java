/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ReciboFacade;
import br.com.curso.entidade.Recibo;
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
public class ReciboControle implements Serializable {

    private Recibo recibo;
    @EJB
    private ReciboFacade reciboFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(reciboFacade);
        }
        return converter;
    }

    public String novo() {
        recibo = new Recibo();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        recibo = new Recibo();
        return "form1?faces-redirect=true";
    }

    public List<Recibo> autoComplete(String query) {
        return reciboFacade.autoComplete("nome", query);
    }

    public String salvar() {

        reciboFacade.salvar(recibo);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        reciboFacade.salvar(recibo);
        return "list1?faces-redirect=true";
    }

    public String excluir(Recibo g) {

        try {
            reciboFacade.excluir(g);

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

    public List<Recibo> getListaRecibos() {
        return reciboFacade.listarTodos();
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }
}

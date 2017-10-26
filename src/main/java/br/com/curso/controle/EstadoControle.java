/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.EstadoFacade;
import br.com.curso.entidade.Estado;
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
public class EstadoControle implements Serializable {

    private Estado estado;
    @EJB
    private EstadoFacade estadoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(estadoFacade);
        }
        return converter;
    }

    public List<Estado> autoComplete(String query) {
        return estadoFacade.autoComplete("nome", query);
    }

    public String novo() {
        estado = new Estado();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        estado = new Estado();
        return "form1?faces-redirect=true";
    }

    public String salvar() {
        estado.ma();
        estadoFacade.salvar(estado);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        estadoFacade.salvar(estado);
        return "list1?faces-redirect=true";
    }

    public String excluir(Estado g) {

        try {
            estadoFacade.excluir(g);

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

    public List<Estado> getListaEstados() {
        return estadoFacade.listarTodosCompleto();
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}

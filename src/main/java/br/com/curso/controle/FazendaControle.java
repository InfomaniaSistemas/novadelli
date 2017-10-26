/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.EmpresaFacade;
import br.com.curso.entidade.Empresa;
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
public class FazendaControle implements Serializable {

    private Empresa fazenda;
    @EJB
    private EmpresaFacade fazendaFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(fazendaFacade);
        }
        return converter;
    }

    public String novo() {
        fazenda = new Empresa();
        return "form?faces-redirect=true";
    }

    public List<Empresa> autoComplete(String query) {
        return fazendaFacade.autoComplete("nome", query);
    }

    public String salvar() {
        fazenda.ma();
        fazendaFacade.salvar(fazenda);
        return "list?faces-redirect=true";
    }

    public String excluir(Empresa g) {

        try {
            fazendaFacade.excluir(g);
        } catch (Exception e) {
        }

        FacesContext.getCurrentInstance().
                addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                "Não é possivel excluir itens com dependência", ""));
        return "list";

    }

    public List<Empresa> getListaFazendas() {
        return fazendaFacade.listarTodos();
    }

    public Empresa getFazenda() {
        return fazenda;
    }

    public void setFazenda(Empresa fazenda) {
        this.fazenda = fazenda;
    }
}

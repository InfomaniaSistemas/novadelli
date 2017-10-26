/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.AjusteEstoqueFacade;
import br.com.curso.entidade.AjusteEstoque;
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
public class AjusteEstoqueControle implements Serializable {

    private AjusteEstoque ajusteEstoque;
    @EJB
    private AjusteEstoqueFacade ajusteEstoqueFacade;
    private GenericConverter converter;
//    @Inject
//    private LoginControle loginControle;

    public AjusteEstoqueControle() {
        ajusteEstoque = new AjusteEstoque();
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(ajusteEstoqueFacade);
        }
        return converter;
    }

    public List<AjusteEstoque> autoComplete(String query) {
        return ajusteEstoqueFacade.autoComplete("nome", query);
    }

    private String aux(){
    
    return "list?faces-redirect=true";
    }
    
    
    public void salvar() {
        try {
//            ajusteEstoque.setUsuario(loginControle.getUsuario());
            ajusteEstoque.ajustarEstoque();
            ajusteEstoque.maiusculo();
            
            ajusteEstoqueFacade.salvar(ajusteEstoque);
            ajusteEstoque = new AjusteEstoque();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Ajuste salvo com sucesso", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            aux();
            
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public List<AjusteEstoque> getListaAjusteEstoques() {
        return ajusteEstoqueFacade.listarTodos();
    }

    public AjusteEstoque getAjusteEstoque() {
        return ajusteEstoque;
    }

    public void setAjusteEstoque(AjusteEstoque ajusteEstoque) {
        this.ajusteEstoque = ajusteEstoque;
    }

}

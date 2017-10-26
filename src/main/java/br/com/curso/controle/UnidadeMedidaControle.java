/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.UnidadeMedidaFacade;
import br.com.curso.entidade.UnidadeMedida;
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
public class UnidadeMedidaControle implements Serializable {

    private UnidadeMedida unidadeMedida;
    @EJB
    private UnidadeMedidaFacade unidadeMedidaFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(unidadeMedidaFacade);
        }
        return converter;
    }

    public String novo() {
        unidadeMedida = new UnidadeMedida();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        unidadeMedida = new UnidadeMedida();
        return "form1?faces-redirect=true";
    }

    public List<UnidadeMedida> autoComplete(String query) {
        return unidadeMedidaFacade.autoComplete("nome", query);
    }

    public String salvar() {
        
          if (unidadeMedida.getId() == null) {
                Long id1 = unidadeMedidaFacade.listarTodosCompleto().get(0).getId();
                unidadeMedida.setId(id1 + 1);
            }
        
        unidadeMedida.ma();
               
        unidadeMedidaFacade.salvar(unidadeMedida);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        
        if (unidadeMedida.getId() == null) {
                Long id1 = unidadeMedidaFacade.listarTodosCompleto().get(0).getId();
                unidadeMedida.setId(id1 + 1);
            }
        
        unidadeMedida.ma();
        
        unidadeMedidaFacade.salvar(unidadeMedida);
        return "list1?faces-redirect=true";
    }

    public String excluir(UnidadeMedida g) {

        try {
            unidadeMedidaFacade.excluir(g);

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

    public List<UnidadeMedida> getListaUnidadeMedidas() {
        return unidadeMedidaFacade.listarTodos();
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}

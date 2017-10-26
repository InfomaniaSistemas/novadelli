/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.DiariaFacade;
import br.com.curso.entidade.Diaria;
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
public class DiariaControle implements Serializable{
    private Diaria diaria;
    @EJB
    private DiariaFacade diariaFacade;
    private GenericConverter converter;
    
    public GenericConverter converter(){
        if(converter==null){
            converter = new GenericConverter(diariaFacade);
        }
        return converter;
    }
    
    public String novo(){
        diaria = new Diaria();
        return "form?faces-redirect=true";
    }    

    public List<Diaria> autoComplete(String query) {
        return diariaFacade.autoComplete("nome", query);
    }

    
    public String salvar(){
        
        diariaFacade.salvar(diaria);
        return "list?faces-redirect=true";
    }
    
   public String excluir(Diaria g) {

        try {
            diariaFacade.excluir(g);

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
    
    public List<Diaria> getListaDiarias(){
        return diariaFacade.listarTodos();
    }

    public Diaria getDiaria() {
        return diaria;
    }

    public void setDiaria(Diaria diaria) {
        this.diaria = diaria;
    }
    
}

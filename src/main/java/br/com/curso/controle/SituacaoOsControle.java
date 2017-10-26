/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.SituacaoOsFacade;
import br.com.curso.entidade.SituacaoOs;
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
public class SituacaoOsControle implements Serializable{
    private SituacaoOs situacaoOs;
    @EJB
    private SituacaoOsFacade situacaoOsFacade;
    private GenericConverter converter;
    
    public GenericConverter converter(){
        if(converter==null){
            converter = new GenericConverter(situacaoOsFacade);
        }
        return converter;
    }
    
    public String novo(){
        situacaoOs = new SituacaoOs();
        return "form?faces-redirect=true";
    }    

    public List<SituacaoOs> autoComplete(String query) {
        return situacaoOsFacade.autoComplete("nome", query);
    }

    
    public String salvar(){
        situacaoOs.ma();
        situacaoOsFacade.salvar(situacaoOs);
        return "list?faces-redirect=true";
    }
    
   public String excluir(SituacaoOs g) {

        try {
            situacaoOsFacade.excluir(g);

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
    
    public List<SituacaoOs> getListaSituacaoOss(){
        return situacaoOsFacade.listarTodos();
    }

    public SituacaoOs getSituacaoOs() {
        return situacaoOs;
    }

    public void setSituacaoOs(SituacaoOs situacaoOs) {
        this.situacaoOs = situacaoOs;
    }
    
}

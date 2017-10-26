/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CaixaFacade;
import br.com.curso.entidade.Caixa;
import br.com.curso.entidade.LancamentoCaixa;
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
public class CaixaControle implements Serializable {

    private Caixa caixa;
    private LancamentoCaixa lancamentoCaixa;
    private String tipo;
    @EJB
    private CaixaFacade caixaFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(caixaFacade);
        }
        return converter;
    }

    public String novo() {
        caixa = new Caixa();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        caixa = new Caixa();
        return "form1?faces-redirect=true";
    }

    public List<Caixa> autoComplete(String query) {
        return caixaFacade.autoComplete("nome", query);
    }

    public String salvar() {

        caixaFacade.salvar(caixa);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        caixaFacade.salvar(caixa);
        return "list1?faces-redirect=true";
    }

    
    public String excluir(Caixa g) {

        try {
            caixaFacade.excluir(g);

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

    public List<Caixa> getListaCaixas() {
        return caixaFacade.listarTodos();
    }
   
    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public CaixaFacade getCaixaFacade() {
        return caixaFacade;
    }

    public void setCaixaFacade(CaixaFacade caixaFacade) {
        this.caixaFacade = caixaFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

}

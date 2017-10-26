/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ServicoFacade;
import br.com.curso.entidade.Servico;
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
public class ServicoControle implements Serializable {

    private Servico servico;
    @EJB
    private ServicoFacade servicoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(servicoFacade);
        }
        return converter;
    }

    public String novo() {
        servico = new Servico();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        servico = new Servico();
        return "form1?faces-redirect=true";
    }

    public List<Servico> autoComplete(String query) {
        return servicoFacade.autoComplete("nome", query);
    }

    public String salvar() {
        servico.ma();
        servicoFacade.salvar(servico);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        servicoFacade.salvar(servico);
        return "list1?faces-redirect=true";
    }

  public String excluir(Servico g) {

        try {
            servicoFacade.excluir(g);

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
    public List<Servico> getListaServicos() {
        return servicoFacade.listarTodos();
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}

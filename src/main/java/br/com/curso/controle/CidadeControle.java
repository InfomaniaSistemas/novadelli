/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CidadeFacade;
import br.com.curso.entidade.Cidade;
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
public class CidadeControle implements Serializable {

    private Cidade cidade;
    @EJB
    private CidadeFacade cidadeFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(cidadeFacade);
        }
        return converter;
    }

    public String novo() {
        cidade = new Cidade();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        cidade = new Cidade();
        return "form1?faces-redirect=true";
    }

    public List<Cidade> autoComplete(String query) {
        return cidadeFacade.autoComplete("nome", query);
    }

    public String salvar() {

        if (cidade.getId() == null) {
            Long id1 = cidadeFacade.listarTodosCompleto().get(0).getId();
            cidade.setId(id1 + 1);
        }

        cidade.ma();

        cidadeFacade.salvar(cidade);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        if (cidade.getId() == null) {
            Long id1 = cidadeFacade.listarTodosCompleto().get(0).getId();
            cidade.setId(id1 + 1);
        }

        cidade.ma();
        cidadeFacade.salvar(cidade);
        return "list1?faces-redirect=true";
    }

    public String excluir(Cidade g) {

        try {
            cidadeFacade.excluir(g);

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

    public List<Cidade> getListaCidades() {
        return cidadeFacade.listarTodos();
    }

    public List<Cidade> getListaCidades1() {
        return cidadeFacade.listarTodosCompleto();
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}

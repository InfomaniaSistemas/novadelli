/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.GrupoProdutoFacade;
import br.com.curso.entidade.GrupoProduto;
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
public class GrupoProdutoControle implements Serializable {

    private GrupoProduto crupoProduto;
    @EJB
    private GrupoProdutoFacade crupoProdutoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(crupoProdutoFacade);
        }
        return converter;
    }

    public String novo() {
        crupoProduto = new GrupoProduto();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        crupoProduto = new GrupoProduto();
        return "form1?faces-redirect=true";
    }

    public List<GrupoProduto> autoComplete(String query) {
        return crupoProdutoFacade.autoComplete("nome", query);
    }

    public String salvar() {

        if (crupoProduto.getId() == null) {
            Long id1 = crupoProdutoFacade.listarTodosCompleto().get(0).getId();
            crupoProduto.setId(id1 + 1);
        }
        crupoProduto.ma();

        crupoProdutoFacade.salvar(crupoProduto);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        if (crupoProduto.getId() == null) {
            Long id1 = crupoProdutoFacade.listarTodosCompleto().get(0).getId();
            crupoProduto.setId(id1 + 1);
        }
        crupoProduto.ma();
        crupoProdutoFacade.salvar(crupoProduto);
        return "list1?faces-redirect=true";
    }

    public String excluir(GrupoProduto g) {

        try {
            crupoProdutoFacade.excluir(g);

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

    public List<GrupoProduto> getListaGrupoProdutos() {
        return crupoProdutoFacade.listarTodos();
    }

    public GrupoProduto getGrupoProduto() {
        return crupoProduto;
    }

    public void setGrupoProduto(GrupoProduto crupoProduto) {
        this.crupoProduto = crupoProduto;
    }
}

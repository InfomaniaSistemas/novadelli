/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.MarcaFacade;
import br.com.curso.entidade.Marca;
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
public class MarcaControle implements Serializable {

    private Marca marca;
    @EJB
    private MarcaFacade marcaFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(marcaFacade);
        }
        return converter;
    }

    public String novo() {
        marca = new Marca();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        marca = new Marca();
        return "form1?faces-redirect=true";
    }

    public List<Marca> autoComplete(String query) {
        return marcaFacade.autoComplete("nome", query);
    }

    public String salvar() {

        if (marca.getId() == null) {
            Long id1 = marcaFacade.listarTodosCompleto().get(0).getId();
            marca.setId(id1 + 1);
        }
        marca.ma();

        marcaFacade.salvar(marca);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        if (marca.getId() == null) {
            Long id1 = marcaFacade.listarTodosCompleto().get(0).getId();
            marca.setId(id1 + 1);
        }

        marca.ma();

        marcaFacade.salvar(marca);
        return "list1?faces-redirect=true";
    }

    public String excluir(Marca g) {

        try {
            marcaFacade.excluir(g);

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

    public List<Marca> getListaMarcas() {
        return marcaFacade.listarTodos();
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}

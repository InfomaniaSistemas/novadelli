/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContaBancariaFacade;
import br.com.curso.entidade.ContaBancaria;
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
public class ContaCorrenteControle implements Serializable {

    private ContaBancaria contaBancaria;
    @EJB
    private ContaBancariaFacade contaBancariaFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(contaBancariaFacade);
        }
        return converter;
    }

    public String novo() {
        contaBancaria = new ContaBancaria();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        contaBancaria = new ContaBancaria();
        return "form1?faces-redirect=true";
    }

    public List<ContaBancaria> autoComplete(String query) {
        return contaBancariaFacade.autoComplete("nome", query);
    }

    public String salvar() {
        contaBancaria.ma();

        contaBancariaFacade.salvar(contaBancaria);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        contaBancaria.ma();
        contaBancariaFacade.salvar(contaBancaria);
        return "list1?faces-redirect=true";
    }

    public String excluir(ContaBancaria g) {

        try {
            contaBancariaFacade.excluir(g);

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

    public List<ContaBancaria> getListaContaBancarias() {
        return contaBancariaFacade.listarTodos();
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.BancoFacade;
import br.com.curso.entidade.Banco;
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
public class BancoControle implements Serializable {

    private Banco banco;
    @EJB
    private BancoFacade bancoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(bancoFacade);
        }
        return converter;
    }

    public String novo() {
        banco = new Banco();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        banco = new Banco();
        return "form1?faces-redirect=true";
    }

    public List<Banco> autoComplete(String query) {
        return bancoFacade.autoComplete("nome", query);
    }

    public String salvar() {
        banco.ma();

        bancoFacade.salvar(banco);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        banco.ma();
        bancoFacade.salvar(banco);
        return "list1?faces-redirect=true";
    }

    public String excluir(Banco g) {

        try {
            bancoFacade.excluir(g);

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

    public List<Banco> getListaBancos() {
        return bancoFacade.listarTodosCompleto();
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
}

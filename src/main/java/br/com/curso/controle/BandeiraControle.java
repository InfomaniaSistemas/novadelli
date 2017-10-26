/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.BandeiraFacade;
import br.com.curso.entidade.Bandeira;
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
public class BandeiraControle implements Serializable {

    private Bandeira bandeira;
    @EJB
    private BandeiraFacade bandeiraFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(bandeiraFacade);
        }
        return converter;
    }

    public String novo() {
        bandeira = new Bandeira();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        bandeira = new Bandeira();
        return "form1?faces-redirect=true";
    }

    public List<Bandeira> autoComplete(String query) {
        return bandeiraFacade.autoComplete("nome", query);
    }

    public String salvar() {
        bandeira.ma();

        bandeiraFacade.salvar(bandeira);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        bandeira.ma();
        bandeiraFacade.salvar(bandeira);
        return "list1?faces-redirect=true";
    }

    public String excluir(Bandeira g) {

        try {
            bandeiraFacade.excluir(g);

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

    public List<Bandeira> getListaBandeiras() {
        return bandeiraFacade.listarTodosCompleto();
    }

    public Bandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeira bandeira) {
        this.bandeira = bandeira;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.AgendaFacade;
import br.com.curso.entidade.Agenda;
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
public class AgendaControle implements Serializable {

    private Agenda agenda;
    @EJB
    private AgendaFacade agendaFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(agendaFacade);
        }
        return converter;
    }

    public String novo() {
        agenda = new Agenda();
        return "form1?faces-redirect=true";
    }

    public String novo1() {
        agenda = new Agenda();
        return "form1?faces-redirect=true";
    }

    public List<Agenda> autoComplete(String query) {
        return agendaFacade.autoComplete("nome", query);
    }

    public String salvar() {
        if (agenda.getId() == null) {
            Long iDaux = agendaFacade.listarTodosCompleto().get(0).getId();
            agenda.setId(iDaux + 1);
        }

        agenda.ma();

        agendaFacade.salvar(agenda);
        return "list1?faces-redirect=true";
    }

    public String salvar1() {
        if (agenda.getId() == null) {
            Long iDaux = agendaFacade.listarTodosCompleto().get(0).getId();
            agenda.setId(iDaux + 1);
        }
        agenda.ma();
        agendaFacade.salvar(agenda);
        return "list1?faces-redirect=true";
    }

    public String excluir(Agenda g) {

        try {
            agendaFacade.excluir(g);

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

        return "list1?faces-redirect=true";

    }

    public List<Agenda> getListaAgendas() {
        return agendaFacade.listarTodosCompleto();
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }
}

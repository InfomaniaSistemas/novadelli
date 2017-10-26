/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContasPagarFacade;
import br.com.curso.ejb.ContasReceberFacade;
import br.com.curso.ejb.TransportadoraFacade;
import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.ContasReceber;
import br.com.curso.entidade.Transportadora;
import java.io.Serializable;
import java.util.Date;
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
public class TransportadoraControle implements Serializable {

    private Transportadora transportadora;
    @EJB
    private TransportadoraFacade transportadoraFacade;
    @EJB
    private ContasPagarFacade contasPagarFacade;
    @EJB
    private ContasReceberFacade contasReceberFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(transportadoraFacade);
        }
        return converter;
    }

    public String novo() {
        transportadora = new Transportadora();
        transportadora.setDtCadasrto(new Date());
        return "form?faces-redirect=true";
    }

    public List<Transportadora> autoComplete(String query) {
        return transportadoraFacade.autoComplete("nome", query);
    }

    public List<ContasReceber> getListaContasRecebidas() {
        return contasReceberFacade.validaMensalidadePago(transportadora);
    }

//    public List<ContasReceber> getTotalRecebido() {
//        return contasReceberFacade.somaRecebidas(pessoa);
//    }
    public List<ContasPagar> getListaContasAPagar() {
        return contasPagarFacade.validaAPagar(transportadora);
    }

    public List<ContasPagar> getListaContasPagas() {
        return contasPagarFacade.validaPago(transportadora);
    }

    public List<ContasReceber> getListaContasAreceber() {
        return contasReceberFacade.validaMensalidadeAPagar(transportadora);
    }

    public String salvar() {

        if (transportadora.getId() == null) {
            Long id1 = transportadoraFacade.listarTodosCompleto().get(0).getId();
            transportadora.setId(id1 + 1);

        }
        transportadoraFacade.salvar(transportadora);
        return "list?faces-redirect=true";
    }

    public String excluir(Transportadora g) {

        try {
            transportadoraFacade.excluir(g);

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

    public List<Transportadora> getListaTransportadoras() {
        return transportadoraFacade.listarTodos();
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContratoFacade;
import br.com.curso.entidade.Contrato;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class ContratoControle implements Serializable {

    private Contrato contrato;
    @EJB
    private ContratoFacade contratoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(contratoFacade);
        }
        return converter;
    }

    public String novo() {
        contrato = new Contrato();
        contrato.setDtEmissao(new Date());
        contrato.setTaxaDetran(BigDecimal.ZERO);
        contrato.setTaxaDetran2(BigDecimal.ZERO);
        contrato.setServico(BigDecimal.ZERO);
        contrato.setTeoricas(BigDecimal.ZERO);
        contrato.setPraticas(BigDecimal.ZERO);
        contrato.setTotal(BigDecimal.ZERO);
        return "form?faces-redirect=true";
    }

    public String novo1() {
        contrato = new Contrato();
        return "form1?faces-redirect=true";
    }

    public List<Contrato> autoComplete(String query) {
        return contratoFacade.autoComplete("nome", query);
    }

    public String salvar() {

        for (int i = 0; i < contrato.getParcelas().size(); i++) {

            String ms = contrato.getParcelas().get(i).getDoc();
            ms = ms.toUpperCase();
            contrato.getParcelas().get(i).setDoc(ms);

        }
        contrato.ma();
        contratoFacade.salvar(contrato);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        contratoFacade.salvar(contrato);
        return "list1?faces-redirect=true";
    }

    public String excluir(Contrato g) {

        try {
            contratoFacade.excluir(g);

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

    public List<Contrato> getListaContratos() {
        return contratoFacade.listarTodos();
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}

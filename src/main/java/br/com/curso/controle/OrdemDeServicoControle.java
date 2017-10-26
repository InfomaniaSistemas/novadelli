/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.OrdemDeServicoFacade;
import br.com.curso.entidade.OrdemDeServico;
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
public class OrdemDeServicoControle implements Serializable {

    private OrdemDeServico ordemDeServico;
    private RelatorioControle relatorioControle;
    private String tipoOs = "Aberta";
    @EJB
    private OrdemDeServicoFacade ordemDeServicoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(ordemDeServicoFacade);
        }
        return converter;
    }

    public String novo() {
        ordemDeServico = new OrdemDeServico();
        ordemDeServico.setDataIn(new Date());
        ordemDeServico.setOs("sem observações");
        return "form?faces-redirect=true";
    }

    public String editar() {


        return "form_1?faces-redirect=true";
    }

    public String editar1() {


        return "form_2?faces-redirect=true";
    }

    public String salvar() {
        ordemDeServico.maiusculo();
        ordemDeServicoFacade.salvar(ordemDeServico);
        return "list?faces-redirect=true";
    }

    public String finalizar() {

        ordemDeServico.setStatus("Fechada");
        ordemDeServicoFacade.salvar(ordemDeServico);
        return "list?faces-redirect=true";
    }

    public String abrir() {

        ordemDeServico.setStatus("Aberta");
        ordemDeServicoFacade.salvar(ordemDeServico);
        return "list?faces-redirect=true";
    }

    public String outras() {

        ordemDeServico.setStatus("Outras");
        ordemDeServicoFacade.salvar(ordemDeServico);
        return "list?faces-redirect=true";
    }

    public String excluir(OrdemDeServico g) {

        try {
            ordemDeServicoFacade.excluir(g);
        } catch (Exception e) {
        }

        FacesContext.getCurrentInstance().
                addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL,
                "Não é possivel excluir itens com dependência!", ""));
        return "list";

    }

    public List<OrdemDeServico> getListaAbertas() {

        return ordemDeServicoFacade.listarAbertas();
    }

    public List<OrdemDeServico> getListaFechadas() {

        return ordemDeServicoFacade.listarFechadas();

//        }
    }
 
    public List<OrdemDeServico> getListaOutras() {

        return ordemDeServicoFacade.listarOutras();

//        }
    }

    public List<OrdemDeServico> getListaTodas() {

        return ordemDeServicoFacade.listarTodos();

//        }
    }

    public OrdemDeServico getOrdemDeServico() {
        return ordemDeServico;
    }

    public void setOrdemDeServico(OrdemDeServico ordemDeServico) {

        if (ordemDeServico.getStatus().equals("Aberta")) {
        }


        this.ordemDeServico = ordemDeServico;
    }

    public RelatorioControle getRelatorioControle() {
        return relatorioControle;
    }

    public void setRelatorioControle(RelatorioControle relatorioControle) {
        this.relatorioControle = relatorioControle;
    }

    public String getTipoOs() {
        return tipoOs;
    }

    public void setTipoOs(String tipoOs) {
        this.tipoOs = tipoOs;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.TipoDeManutencaoFacade;
import br.com.curso.entidade.TipoDeManutencao;
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
public class TipoDeManutencaoControle implements Serializable {

    private TipoDeManutencao tipoDeManutencao;
    @EJB
    private TipoDeManutencaoFacade tipoDeManutencaoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(tipoDeManutencaoFacade);
        }
        return converter;
    }

    public String novo() {
        tipoDeManutencao = new TipoDeManutencao();
        return "form?faces-redirect=true";
    }

    public List<TipoDeManutencao> autoComplete(String query) {
        return tipoDeManutencaoFacade.autoComplete("nome", query);
    }

    public String salvar() {
        tipoDeManutencao.ma();
        tipoDeManutencaoFacade.salvar(tipoDeManutencao);
        return "list?faces-redirect=true";
    }

    public String excluir(TipoDeManutencao g) {

        try {
            tipoDeManutencaoFacade.excluir(g);

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

    public List<TipoDeManutencao> getListaTipoDeManutencaos() {
        return tipoDeManutencaoFacade.listarTodos();
    }

    public TipoDeManutencao getTipoDeManutencao() {
        return tipoDeManutencao;
    }

    public void setTipoDeManutencao(TipoDeManutencao tipoDeManutencao) {
        this.tipoDeManutencao = tipoDeManutencao;
    }

}

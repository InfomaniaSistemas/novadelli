/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.NcmFacade;
import br.com.curso.entidade.ItemNcm;
import br.com.curso.entidade.ItemVenda;
import br.com.curso.entidade.Ncm;
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
public class NcmControle implements Serializable {

    private Ncm ncm;
    private ItemNcm itemNcm;
    @EJB
    private NcmFacade ncmFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(ncmFacade);
        }
        return converter;
    }

    public String novo() {
        ncm = new Ncm();
        return "form_1?faces-redirect=true";
    }

    public NcmControle() {
        itemNcm = new ItemNcm();

    }

    public ItemNcm getItemNcm() {
        return itemNcm;
    }

    public void setItemNcm(ItemNcm itemNcm) {
        this.itemNcm = itemNcm;
    }

    public NcmFacade getNcmFacade() {
        return ncmFacade;
    }

    public void setNcmFacade(NcmFacade ncmFacade) {
        this.ncmFacade = ncmFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    public String novo1() {
        ncm = new Ncm();
        return "form1?faces-redirect=true";
    }

    public List<Ncm> autoComplete(String query) {
        return ncmFacade.autoComplete("codNcm", query);
    }

    public String salvar() {
        ncm.ma();
        ncmFacade.salvar(ncm);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        ncmFacade.salvar(ncm);
        return "list1?faces-redirect=true";
    }

    public String excluir(Ncm g) {

        try {
            ncmFacade.excluir(g);

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

    public List<Ncm> getListaNcms() {
        return ncmFacade.listarTodos();
    }

    public Ncm getNcm() {
        return ncm;
    }

    public void setNcm(Ncm ncm) {
        this.ncm = ncm;
    }
}

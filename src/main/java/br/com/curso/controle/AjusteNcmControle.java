/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.AjusteNcmFacade;
import br.com.curso.ejb.ProdutoFacade;
import br.com.curso.entidade.AjusteNcm;
import br.com.curso.entidade.ItemNcm;
import br.com.curso.entidade.Produto;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class AjusteNcmControle implements Serializable {

    private AjusteNcm ajusteNcm;
    private ItemNcm itemNcm;
    @EJB
    private AjusteNcmFacade ajusteNcmFacade;
    @EJB
    private ProdutoFacade produtoFacade;
    private GenericConverter converter;
//    @Inject
//    private LoginControle loginControle;

    public AjusteNcmControle() {
        ajusteNcm = new AjusteNcm();
        itemNcm = new ItemNcm();
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(ajusteNcmFacade);
        }
        return converter;
    }

    public List<AjusteNcm> autoComplete(String query) {
        return ajusteNcmFacade.autoComplete("nome", query);
    }

    public void addItem() throws Exception {
        try {
            List<Produto> p = produtoFacade.autoCompleteNcm("nome", ajusteNcm.getDescricao());

            for (int i = 0; i < p.size(); i++) {

                ajusteNcm.addItem(p.get(i));
                itemNcm = new ItemNcm();
            }

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), "Aqui");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public ItemNcm getItemNcm() {
        return itemNcm;
    }

    public void setItemNcm(ItemNcm itemNcm) {
        this.itemNcm = itemNcm;
    }

//    public void removeItem() {
//        try {
//
//            ajusteNcm.removeItem(itemNcm);
//            itemNcm = new ItemNcm();
//
//        } catch (Exception ex) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
//                    ex.getMessage(), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//
//    }

    private String aux() {

        return "list?faces-redirect=true";
    }

    public String cancelar() {

        return "produto/list?faces-redirect=true";
    }

    public void enviaProd(Produto prod) {

        itemNcm.setPlantio(prod);

    }

    public void salvar() {
        try {
//            ajusteNcm.setUsuario(loginControle.getUsuario());

            ajusteNcmFacade.geraNcm(ajusteNcm);
            ajusteNcmFacade.salvar(ajusteNcm);
            ajusteNcm = new AjusteNcm();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Ajuste salvo com sucesso", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            aux();

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public List<AjusteNcm> getListaAjusteNcms() {
        return ajusteNcmFacade.listarTodos();
    }

    public AjusteNcm getAjusteNcm() {
        return ajusteNcm;
    }

    public void setAjusteNcm(AjusteNcm ajusteNcm) {
        this.ajusteNcm = ajusteNcm;
    }

}

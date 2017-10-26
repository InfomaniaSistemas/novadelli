/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.converter;

import br.com.curso.ejb.AbstractFacade;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ricardo
 */
public class GenericConverter implements Converter {

    private AbstractFacade facade;

    public GenericConverter(AbstractFacade facade) {
        this.facade = facade;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        try {
            return facade.pesquisar(Long.valueOf(value));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Busca não encontrada!", ""));
            return "";
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            return value.toString();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Busca não encontrada!", ""));
            return "";
        }

    }

}

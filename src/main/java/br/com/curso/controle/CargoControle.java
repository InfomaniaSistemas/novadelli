/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.CargoFacade;
import br.com.curso.entidade.Cargo;
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
public class CargoControle implements Serializable {

    private Cargo cargo;
    @EJB
    private CargoFacade cargoFacade;
    private GenericConverter converter;

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(cargoFacade);
        }
        return converter;
    }

    public String novo() {
        cargo = new Cargo();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        cargo = new Cargo();
        return "form1?faces-redirect=true";
    }

    public List<Cargo> autoComplete(String query) {
        return cargoFacade.autoComplete("nome", query);
    }

    public String salvar() {
        cargo.ma();

        cargoFacade.salvar(cargo);
        return "list?faces-redirect=true";
    }

    public String salvar1() {
        cargo.ma();
        cargoFacade.salvar(cargo);
        return "list1?faces-redirect=true";
    }

    public String excluir(Cargo g) {

        try {
            cargoFacade.excluir(g);

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

    public List<Cargo> getListaCargos() {
        return cargoFacade.listarTodosCompleto();
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}

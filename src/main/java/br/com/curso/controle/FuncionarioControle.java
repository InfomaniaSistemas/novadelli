/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.FuncionarioFacade;
import br.com.curso.ejb.PessoaFacade;
import br.com.curso.entidade.Funcionario;
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
public class FuncionarioControle implements Serializable {
    
    private Funcionario funcionario;
    @EJB
    private FuncionarioFacade funcionarioFacade;
    @EJB
    private PessoaFacade pessoaFacade;
    private GenericConverter converter;
    
    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(funcionarioFacade);
        }
        return converter;
    }
    
    public String novo() {
        funcionario = new Funcionario();
        funcionario.setDtCadasrto(new Date());
        return "form?faces-redirect=true";
    }
    
    public List<Funcionario> autoComplete(String query) {
        return funcionarioFacade.autoComplete("nome", query);
    }
    
    public String salvar() {
        if (funcionario.getId() == null) {
            
            Long id1 = pessoaFacade.listarTodos().get(0).getId();
            funcionario.setId(id1 + 1);
        }
        funcionario.setNome(funcionario.getNome().toUpperCase());
        funcionario.setValidaFuncionario(Boolean.TRUE);
        funcionarioFacade.salvar(funcionario);
        return "list?faces-redirect=true";
    }
    
    public String excluir(Funcionario g) {
        
        try {
            funcionarioFacade.excluir(g);
            
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
    
    public List<Funcionario> getListaFuncionarios() {
        return funcionarioFacade.listarTodosCompleto();
    }
    
    public Funcionario getFuncionario() {
        return funcionario;
    }
    
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    
}

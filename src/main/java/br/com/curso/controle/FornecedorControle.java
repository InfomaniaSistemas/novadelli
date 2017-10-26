/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContasPagarFacade;
import br.com.curso.ejb.ContasReceberFacade;
import br.com.curso.ejb.FornecedorFacade;
import br.com.curso.ejb.PessoaFacade;
import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.ContasReceber;
import br.com.curso.entidade.Fornecedor;
import br.com.curso.entidade.Pessoa;
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
public class FornecedorControle implements Serializable {
    
    private Fornecedor fornecedor;
    private Pessoa pessoa;
    @EJB
    private FornecedorFacade fornecedorFacade;
    @EJB
    private ContasPagarFacade contasPagarFacade;
    @EJB
    private PessoaFacade pessoaFacade;
    @EJB
    private ContasReceberFacade contasReceberFacade;
    private GenericConverter converter;
    
    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(fornecedorFacade);
        }
        return converter;
    }
    
    public String novo() {
        fornecedor = new Fornecedor();
        fornecedor.setDtCadasrto(new Date());
        return "form?faces-redirect=true";
    }
    
    public List<Fornecedor> autoComplete(String query) {
        return fornecedorFacade.autoComplete("nome", query);
    }
    
    public List<Fornecedor> autoComplete2(String query) {
        return fornecedorFacade.autoComplete("razaoSocial", query);
    }
    
    public List<ContasReceber> getListaContasRecebidas() {
        return contasReceberFacade.validaMensalidadePago(fornecedor);
    }

//    public List<ContasReceber> getTotalRecebido() {
//        return contasReceberFacade.somaRecebidas(pessoa);
//    }
    public List<ContasPagar> getListaContasAPagar() {
        return contasPagarFacade.validaAPagar(fornecedor);
    }
    
    public List<ContasPagar> getListaContasPagas() {
        return contasPagarFacade.validaPago(fornecedor);
    }
    
    public List<ContasReceber> getListaContasAreceber() {
        return contasReceberFacade.validaMensalidadeAPagar(fornecedor);
    }
    
    public String salvar() {
        
        if (fornecedor.getId() == null) {
            Long id1 = pessoaFacade.listarTodosCompleto().get(0).getId();
            fornecedor.setId(id1 + 1);
            
        }
        fornecedor.setNome(fornecedor.getNome().toUpperCase());
        fornecedorFacade.salvar(fornecedor);
        return "list?faces-redirect=true";
    }
    
    public String excluir(Fornecedor g) {
        
        try {
            fornecedorFacade.excluir(g);
            
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
        
        return "list";
        
    }
    
    public List<Fornecedor> getListaFornecedors() {
        return fornecedorFacade.listarTodosCompleto();
    }
    
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    
}

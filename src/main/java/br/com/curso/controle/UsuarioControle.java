/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.UsuarioFacade;
import br.com.curso.entidade.PermissaoUsuario;
import br.com.curso.entidade.Usuario;
import java.io.IOException;
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
public class UsuarioControle implements Serializable {

    private Usuario usuario;
    @EJB
    private UsuarioFacade produtoFacade;
    private GenericConverter converter;

    public void notas() throws IOException {

        Runtime.getRuntime().exec("D:/StikyNot.exe");

    }

    public void backup() throws IOException {

        Runtime.getRuntime().exec("D:/BackupPostGres.bat");

    }

    public void calc() throws IOException {

        Runtime.getRuntime().exec("D:/calc.exe");

    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(produtoFacade);
        }
        return converter;
    }

    public List<Usuario> getListaUsuarios() {
        return produtoFacade.listarTodos();
    }

//    public void validaSenha() {
//
//        if (usuario.validaSenha(true)) {
//            FacesContext.getCurrentInstance().
//                    addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_INFO,
//                                    "As senha conferem!", ""));
//
//        } else {
//            FacesContext.getCurrentInstance().
//                    addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
//                                    "As senhas digitadas não são iguais!", ""));
//
//        }
//
//    }
    public String novo() {
        usuario = new Usuario();
        return "form?faces-redirect=true";
    }

    public PermissaoUsuario[] getPermissoes() {
        return PermissaoUsuario.values();
    }

    public List<Usuario> autoComplete(String query) {
        return produtoFacade.autoComplete("nome", query);
    }

    public String salvar() {

        produtoFacade.salvar(usuario);
        return "list?faces-redirect=true";
    }

    public String excluir(Usuario g) {

        try {
            produtoFacade.excluir(g);

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

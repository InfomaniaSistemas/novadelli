/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.ejb.UsuarioFacade;
import br.com.curso.ejb.ValidaPagamentoFacade;
import br.com.curso.entidade.Usuario;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ricardo
 */
@ManagedBean
@SessionScoped
public class LoginControler implements Serializable {

    private Usuario usuario;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ValidaPagamentoFacade validaPagamentoFacade;
    private String login;
    private String senha;

    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:login";
    }

  
    public String logar() {
        usuario = usuarioFacade.autenticarUsuario(login, senha);
        if (usuario != null) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new GrantedAuthorityImpl(usuario.getPermissao()));
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(login, senha, roles));
            if (context.getAuthentication().isAuthenticated()) {
                if (usuario.getPermissao().equals("ADMIN")) {
                    return "/index?faces-redirect=true";
                } else {
                    return "/index?faces-redirect=true";

                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Falha na autenticação do usuário", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuário inesistente no sistema", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
    }

    
    public String logar1() {
        usuario = usuarioFacade.autenticarUsuario(login, senha);
        if (usuario != null) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new GrantedAuthorityImpl(usuario.getPermissao()));
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(login, senha, roles));
            if (context.getAuthentication().isAuthenticated()) {
                if (usuario.getPermissao().equals("ADMIN")) {
                    return "/ajuste/form?faces-redirect=true";
                } else {
                    return "/index1?faces-redirect=true";

                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Falha na autenticação do usuário", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuário inesistente no sistema", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
    }

    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}

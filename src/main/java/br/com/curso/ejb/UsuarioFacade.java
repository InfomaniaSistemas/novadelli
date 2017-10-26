/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Miguel
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

      public Usuario autenticarUsuario(String login, String senha) {
        try {
            Query query = em.createQuery("FROM Usuario AS u "
                    + "WHERE u.login=:login "
                    + "AND u.senha=:senha");
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            return (Usuario) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
    
    
    public Usuario buscarUsuario(String login, String senha) {
        String consulta = 
               "FROM Usuario AS u where u.login='" + login + "' AND u.senha='" + senha + "'";
        
        Query q = em.createQuery(consulta);
        
        if (q.getResultList().size() == 1) {
            return (Usuario) q.getResultList().get(0);
        } else {
            return null;
        }
    }

}

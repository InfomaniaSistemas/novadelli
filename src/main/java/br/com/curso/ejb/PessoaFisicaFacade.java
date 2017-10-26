/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.PessoaFisica;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Miguel
 */
@Stateless
public class PessoaFisicaFacade extends AbstractFacade<PessoaFisica> {
    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PessoaFisicaFacade() {
        super(PessoaFisica.class);
    }
    
      
   public List<PessoaFisica> validaRelatorio(Long aux) {
        Query q = em.createQuery("FROM PessoaFisica As a WHERE a.id = :para1");
        q.setParameter("para1", aux);

        return q.getResultList();
    }
   
 

        
    
}

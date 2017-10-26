/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.Fornecedor;
import br.com.curso.entidade.Funcionario;
import br.com.curso.entidade.Pessoa;
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
public class FuncionarioFacade extends AbstractFacade<Funcionario> {
    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
      public List<Funcionario> funcio() {
        Query q = em.createQuery("FROM Funcionario As a WHERE a.id = -1");
        return q.getResultList();
    }

    

    public FuncionarioFacade() {
        super(Funcionario.class);
    }
    
}

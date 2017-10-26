/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.CodigoMensal;
import br.com.curso.entidade.ValidaPagamento;
import java.util.Date;
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
public class CodigoMensalFacade extends AbstractFacade<CodigoMensal> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<ValidaPagamento> validaVenda(String cod, Date dtIn, Date dtFim) {

        Query q = em.createQuery("FROM CodigoMensal As a WHERE a.cod = :para1 AND a.dtIn = :para2 AND a.dtFim = :para3");
        q.setParameter("para1", cod);
        q.setParameter("para2", dtIn);
        q.setParameter("para3", dtFim);
        return q.getResultList();
    }

    public CodigoMensalFacade() {
        super(CodigoMensal.class);
    }

}

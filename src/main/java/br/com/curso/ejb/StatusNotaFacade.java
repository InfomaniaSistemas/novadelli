/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.Pessoa;
import br.com.curso.entidade.StatusNota;
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
public class StatusNotaFacade extends AbstractFacade<StatusNota> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<StatusNota> listaUm(Long auxId) {
        Query q = em.createQuery("FROM StatusNota As a WHERE a.id = " + auxId);

        return q.getResultList();
    }

    public StatusNotaFacade() {
        super(StatusNota.class);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.ContaBancaria;
import java.text.SimpleDateFormat;
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
public class ContaBancariaFacade extends AbstractFacade<ContaBancaria> {
    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

     public List<ContaBancaria> validaVendaDia2(Long conta) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");


        Query q = em.createQuery("FROM ContaBancaria As a WHERE a.id = :para");
        q.setParameter("para", conta);
        return q.getResultList();
    }
    
    public ContaBancariaFacade() {
        super(ContaBancaria.class);
    }
    
}

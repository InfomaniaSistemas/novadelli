/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.LancamentoCaixa;
import br.com.curso.entidade.Venda;
import java.text.SimpleDateFormat;
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
public class LancamentoCaixaFacade extends AbstractFacade<LancamentoCaixa> {
    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    
    
    
    public List<LancamentoCaixa> validaVendaDia(Date aux) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);

        Query q = em.createQuery("FROM LancamentoCaixa As a WHERE a.dtEntrada = :para order by id desc");
        q.setParameter("para", aux);
        return q.getResultList();
    }

    
    
    public List<LancamentoCaixa> validaSaldoAnterior(Date aux) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);

        Query q = em.createQuery("FROM LancamentoCaixa As a WHERE a.dtEntrada < :para order by id desc");
        q.setParameter("para", aux);
        return q.getResultList();
    }

    
    
    
    
    public LancamentoCaixaFacade() {
        super(LancamentoCaixa.class);
    }
    
}

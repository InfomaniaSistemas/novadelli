/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.ContaBancaria;
import br.com.curso.entidade.LancamentoBancario;
import br.com.curso.entidade.LancamentoCaixa;
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
public class LancamentoBancarioFacade extends AbstractFacade<LancamentoBancario> {
    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<LancamentoBancario> validaVendaDia2(Date aux, ContaBancaria conta) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);

        Query q = em.createQuery("FROM LancamentoBancario As a WHERE a.dtEntrada = :para AND contaBancaria = :para1 order by id desc");
        q.setParameter("para", aux);
        q.setParameter("para1", conta);
        return q.getResultList();
    }
    
    public List<LancamentoBancario> validaVendaDia(Date aux) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);

        Query q = em.createQuery("FROM LancamentoBancario As a WHERE a.dtEntrada = :para order by id desc");
        q.setParameter("para", aux);
        return q.getResultList();
    }
    

    public LancamentoBancarioFacade() {
        super(LancamentoBancario.class);
    }
    
}

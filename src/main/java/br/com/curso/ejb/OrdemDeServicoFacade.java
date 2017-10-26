/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.OrdemDeServico;
import br.com.curso.entidade.Pessoa;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Miguel
 */
@Stateless
public class OrdemDeServicoFacade extends AbstractFacade<OrdemDeServico> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    @EJB
    private ContasReceberFacade contasReceberFacade;

    @EJB
    private PessoaFacade pessoaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrdemDeServicoFacade() {
        super(OrdemDeServico.class);
    }

    public List<OrdemDeServico> listarAbertas() {

        return em.createQuery("FROM OrdemDeServico As a WHERE a.status = 'Aberta'").getResultList();

    }

    public List<OrdemDeServico> listarFechadas() {

        return em.createQuery("FROM OrdemDeServico As a WHERE a.status = 'Fechada'").getResultList();

    }

    public List<OrdemDeServico> listarOutras() {

        return em.createQuery("FROM OrdemDeServico As a WHERE a.status = 'Outras'").getResultList();

    }

}

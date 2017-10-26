/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.Caixa;
import br.com.curso.entidade.CartaoCredito;
import br.com.curso.entidade.CartaoDebito;
import br.com.curso.entidade.Cheque;
import br.com.curso.entidade.ContaBancaria;
import br.com.curso.entidade.ContasRecebidas;
import br.com.curso.entidade.LancamentoBancario;
import br.com.curso.entidade.LancamentoCaixa;
import br.com.curso.entidade.Pessoa;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Miguel
 */
@Stateless
public class ContasRecebidasFacade extends AbstractFacade<ContasRecebidas> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    ContasRecebidas contasReceber1;

    @EJB
    private ContasRecebidasFacade contasReceber1Facade;
    @EJB
    private LancamentoBancarioFacade lancamentoBancarioFacade;
    @EJB
    private ChequeFacade chequeFacade;
    @EJB
    private CartaoDebitoFacade cartaoDebitoFacade;
    @EJB
    private CartaoCreditoFacade cartaoCreditoFacade;
    @EJB
    private LancamentoCaixaFacade lancamentoCaixaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContasRecebidasFacade() {
        super(ContasRecebidas.class);
    }

    public List<ContasRecebidas> validaMensalidadePago(Pessoa aluno) {
        Query q = em.createQuery("FROM ContasRecebidas As a WHERE a.pessoa = :para1 AND a.status = 'RECEBIDO' ORDER BY a.dtPagamento");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasRecebidas> validaMensalidadePagoEditado(Pessoa aluno) {
        Query q = em.createQuery("FROM ContasRecebidas As a WHERE a.pessoa = :para1 AND a.status = 'RECEBIDO' OR a.pessoa = :para1 AND a.status = 'RESTOS A PAGAR'   ORDER BY a.dtPagamento");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasRecebidas> validaMensalidadeAPagar(Pessoa aluno) {
        Query q = em.createQuery("FROM ContasRecebidas As a WHERE a.pessoa = :para1 AND a.status = 'A RECEBER' OR a.pessoa = :para1 AND a.status = 'RESTOS A PAGAR' ORDER BY a.dtVencimento ASC");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasRecebidas> validaMensalidadeAPagar2(Pessoa aluno) {
        Query q = em.createQuery("FROM ContasRecebidas As a WHERE a.pessoa = :para1 AND a.status != 'RECEBIDO' ORDER BY a.dtVencimento ASC");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasRecebidas> validaMensalidadeAPagarPorPessoa(Long aluno) {
        Query q = em.createQuery("FROM ContasRecebidas As a WHERE a.pessoa = :para1 AND a.status = 'A RECEBER' OR a.status = 'RESTOS A PAGAR' ORDER BY a.dtVencimento ASC");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasRecebidas> listarAreceber() {
        Query q = em.createQuery("FROM ContasRecebidas As a WHERE a.status = 'A RECEBER' OR a.status = 'RESTOS A PAGAR' ORDER BY a.dtVencimento ASC");

        return q.getResultList();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.Caixa;
import br.com.curso.entidade.CartaoCredito;
import br.com.curso.entidade.Cheque;
import br.com.curso.entidade.ContaBancaria;
import br.com.curso.entidade.LancamentoBancario;
import br.com.curso.entidade.LancamentoCaixa;
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
public class ChequeFacade extends AbstractFacade<Cheque> {

    @EJB
    private LancamentoCaixaFacade lancamentoCaixaFacade;
    @EJB
    private LancamentoBancarioFacade lancamentoBancarioFacade;
    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChequeFacade() {
        super(Cheque.class);
    }

    public void lancarCaixa(Cheque cartaoDebito, BigDecimal valorEntrada, BigDecimal valorSaida) {
        List<Caixa> aux = em.createQuery("FROM Caixa As a WHERE a.id = 1").getResultList();

        if (valorEntrada.compareTo(BigDecimal.ZERO) == 0) {

            BigDecimal novosaldo = aux.get(0).getSaldo().add(valorSaida);
            aux.get(0).setSaldo(novosaldo);

        } else {

            BigDecimal novosaldo = aux.get(0).getSaldo().add(valorEntrada);
            aux.get(0).setSaldo(novosaldo);

        }

        LancamentoCaixa la = new LancamentoCaixa();

        la.setCaixa(aux.get(0));
        la.setPessoa(cartaoDebito.getPessoa());

        if (valorEntrada.compareTo(BigDecimal.ZERO) == 0) {

            la.setTipo("SAIDA");
            la.setDocumento("CHEQUE"+" "+cartaoDebito.getNumeroCheque());
            la.setDtEntrada(new Date());
            la.setLacamentoEntrada(BigDecimal.ZERO);
            la.setLacamentoSaida(valorSaida);

            lancamentoCaixaFacade.salvar(la);
        } else {

            la.setTipo("ENTRADA");
            la.setDocumento("CHEQUE"+" "+cartaoDebito.getNumeroCheque());
            la.setLacamentoEntrada(valorEntrada);
            la.setLacamentoSaida(BigDecimal.ZERO);
            la.setDtEntrada(new Date());

            lancamentoCaixaFacade.salvar(la);
        }

    }

    public void lancarConta(Cheque cartaoDebito, BigDecimal valorEntrada, BigDecimal valorSaida, Long id) {
        List<ContaBancaria> aux = em.createQuery("FROM ContaBancaria As a WHERE a.id = " + id).getResultList();

        if (valorEntrada.compareTo(BigDecimal.ZERO) == 0) {

            BigDecimal novosaldo = aux.get(0).getSaldo().subtract(valorSaida);
            aux.get(0).setSaldo(novosaldo);
        } else {

            BigDecimal novosaldo = aux.get(0).getSaldo().add(valorEntrada);
            aux.get(0).setSaldo(novosaldo);

        }

        LancamentoBancario la = new LancamentoBancario();

        la.setContaBancaria(aux.get(0));
        la.setPessoa(cartaoDebito.getPessoa());

        if (valorEntrada.compareTo(BigDecimal.ZERO) == 0) {

            la.setTipo("SAIDA");
            la.setDocumento("CHEQUE"+" "+ cartaoDebito.getNumeroCheque());
            la.setDtEntrada(new Date());
            la.setLacamentoEntrada(BigDecimal.ZERO);
            la.setLacamentoSaida(valorSaida);

            lancamentoBancarioFacade.salvar(la);
        } else {

            la.setTipo("ENTRADA");
            la.setDocumento("CHEQUE"+" "+ cartaoDebito.getNumeroCheque());
            la.setLacamentoEntrada(valorEntrada);
            la.setLacamentoSaida(BigDecimal.ZERO);
            la.setDtEntrada(new Date());

            lancamentoBancarioFacade.salvar(la);
        }

    }

    public List<Cheque> validaVendaDia(Date aux) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);

        Query q = em.createQuery("FROM Cheque As a WHERE  a.situacao = 'A DEBITAR' order by a.dtvencimento");
        return q.getResultList();
    }
    
        public List<Cheque> validaTotalADebitar () {

        Query q = em.createQuery("FROM Cheque As a WHERE  a.situacao = 'A DEBITAR'");
        return q.getResultList();
    }

    public List<Cheque> validaTotalGeral() {

        Query q = em.createQuery("FROM Cheque As a WHERE  a.situacao = 'A DEBITAR'");
        return q.getResultList();
    }

    public List<Cheque> validaEntradaDia(Date aux) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);

        Query q = em.createQuery("FROM Cheque As a WHERE a.dtmovimento = :para order by id desc");
        q.setParameter("para", aux);
        return q.getResultList();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.Banco;
import br.com.curso.entidade.Bandeira;
import br.com.curso.entidade.Caixa;
import br.com.curso.entidade.CartaoCredito;
import br.com.curso.entidade.CartaoDebito;
import br.com.curso.entidade.Cheque;
import br.com.curso.entidade.ContaBancaria;
import br.com.curso.entidade.ContasReceber;
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
public class ContasReceberFacade extends AbstractFacade<ContasReceber> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    ContasReceber contasReceber1;

    @EJB
    private ContasReceberFacade contasReceber1Facade;
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

    public void lancarConta(ContasReceber cartaoDebito, BigDecimal valorSaida, Long id) {
        List<ContaBancaria> aux = em.createQuery("FROM ContaBancaria As a WHERE a.id = " + id).getResultList();

        BigDecimal novosaldo = aux.get(0).getSaldo().add(valorSaida);
        aux.get(0).setSaldo(novosaldo);

        LancamentoBancario la = new LancamentoBancario();

        la.setContaBancaria(aux.get(0));
        la.setPessoa(cartaoDebito.getPessoa());

        la.setTipo("ENTRADA");
        la.setDocumento("CONTA A RECEBER N°" + " " + cartaoDebito.getId());
        la.setDtEntrada(new Date());
        la.setLacamentoEntrada(valorSaida);
        la.setLacamentoSaida(BigDecimal.ZERO);

        lancamentoBancarioFacade.salvar(la);

    }

    public List<ContasReceber> contasVencidas(Date aux) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);
        Boolean aux1 = false;
        Query q = em.createQuery("FROM ContasReceber As a WHERE a.dtVencimento <= :para AND a.status = 'A RECEBER'");
        q.setParameter("para", aux);
        return q.getResultList();
    }

    public List<ContasReceber> contasVencidasPorPessoa(Date aux, Pessoa pes) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);
        Boolean aux1 = false;
        Query q = em.createQuery("FROM ContasReceber As a WHERE a.pessoa = :para1 AND a.dtVencimento <= :para AND a.status = 'A RECEBER'");
        q.setParameter("para", aux);
        q.setParameter("para1", pes);
        return q.getResultList();
    }

    public void gerarSalvar(ContasReceber c) {

        contasReceber1 = new ContasReceber();

        contasReceber1.setValorPrincipal(c.getValorPrincipal());
        contasReceber1.setDtMovimento(c.getDtPagamento());
        contasReceber1.setDtVencimento(c.getDtaux2());
        contasReceber1.setValor(c.getValorPrincipal());
        contasReceber1.setStatus("A RECEBER");
        contasReceber1.setPessoa(c.getPessoa());
        contasReceber1.setTurma(c.getTurma());
        contasReceber1Facade.salvar(contasReceber1);

    }

    public void lancarCartao(ContasReceber venda, Date dt, Bandeira bandeira) {
        CartaoCredito cr = new CartaoCredito();
        cr.setDtmovimento(venda.getDtPagamento());
        cr.setDtvencimento(dt);
        cr.setBandeira(bandeira);
        cr.setValor(venda.getUltimoPagamento());
        cr.setValorSaida(BigDecimal.ZERO);
        cr.setContasReceber(venda);
        cr.setPessoa(venda.getPessoa());
        cr.setSituacao("A DEBITAR");
        cartaoCreditoFacade.salvar(cr);
    }

    public void lancarCheque(ContasReceber venda, String numeroDoc, Date dt, Banco banco) {
        Cheque cr = new Cheque();
        cr.setDtmovimento(venda.getDtPagamento());
        cr.setValor(venda.getUltimoPagamento());
        cr.setValorSaida(BigDecimal.ZERO);
        cr.setBanco(banco);
        cr.setContasReceber(venda);
        cr.setNumeroCheque(numeroDoc);
        cr.setPessoa(venda.getPessoa());
        cr.setSituacao("A DEBITAR");
        cr.setDtvencimento(dt);
        chequeFacade.salvar(cr);
    }

    public void lancarCartaoDebito(ContasReceber venda, Date dt, Bandeira bandeira) {
        CartaoDebito cr = new CartaoDebito();
        cr.setDtmovimento(venda.getDtPagamento());
        cr.setDtmovimento(dt);
        cr.setValor(venda.getUltimoPagamento());
        cr.setValorSaida(BigDecimal.ZERO);
        cr.setBandeira(bandeira);
        cr.setContasReceber(venda);
        cr.setPessoa(venda.getPessoa());
        cr.setSituacao("A DEBITAR");
        cartaoDebitoFacade.salvar(cr);
    }

    public void lancarCaixa(ContasReceber venda) {
        List<Caixa> aux = em.createQuery("FROM Caixa As a WHERE a.id = 1").getResultList();

        BigDecimal novosaldo = aux.get(0).getSaldo().add(venda.getUltimoPagamento());
        aux.get(0).setSaldo(novosaldo);

        LancamentoCaixa la = new LancamentoCaixa();

        la.setPessoa(venda.getPessoa());
        la.setCaixa(aux.get(0));
        la.setTipo("ENTRADA");
        la.setDocumento("DINHEIRO");
        la.setObs("CONTA A RECEBER Nº" + venda.getId());
        la.setLacamentoEntrada(venda.getUltimoPagamento());
        la.setLacamentoSaida(BigDecimal.ZERO);
        la.setDtEntrada(venda.getDtPagamento());

        lancamentoCaixaFacade.salvar(la);
    }

    public ContasReceberFacade() {
        super(ContasReceber.class);
    }

    public List<ContasReceber> validaMensalidadePago(Pessoa aluno) {
        Query q = em.createQuery("FROM ContasReceber As a WHERE a.pessoa = :para1 AND a.status = 'RECEBIDO' ORDER BY a.dtPagamento");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasReceber> validaMensalidadePagoEditado(Pessoa aluno) {
        Query q = em.createQuery("FROM ContasReceber As a WHERE a.pessoa = :para1 AND a.status = 'RECEBIDO' OR a.pessoa = :para1 AND a.status = 'RESTOS A PAGAR'   ORDER BY a.dtPagamento");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasReceber> validaMensalidadeAPagar(Pessoa aluno) {
        Query q = em.createQuery("FROM ContasReceber As a WHERE a.pessoa = :para1 AND a.status = 'A RECEBER' OR a.pessoa = :para1 AND a.status = 'RESTOS A PAGAR' ORDER BY a.dtVencimento ASC");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasReceber> validaMensalidadeAPagar2(Pessoa aluno) {
        Query q = em.createQuery("FROM ContasReceber As a WHERE a.pessoa = :para1 AND a.status != 'RECEBIDO' ORDER BY a.dtVencimento ASC");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasReceber> validaMensalidadeAPagarPorPessoa(Long aluno) {
        Query q = em.createQuery("FROM ContasReceber As a WHERE a.pessoa = :para1 AND a.status = 'A RECEBER' OR a.status = 'RESTOS A PAGAR' ORDER BY a.dtVencimento ASC");
        q.setParameter("para1", aluno);

        return q.getResultList();
    }

    public List<ContasReceber> listarAreceber() {
        Query q = em.createQuery("FROM ContasReceber As a WHERE a.status = 'A RECEBER' OR a.status = 'RESTOS A PAGAR' ORDER BY a.dtVencimento ASC");

        return q.getResultList();
    }
}

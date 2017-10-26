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
import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.Empresa;
import br.com.curso.entidade.Fornecedor;
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
public class ContasPagarFacade extends AbstractFacade<ContasPagar> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @EJB
    private LancamentoCaixaFacade lancamentoCaixaFacade;
    @EJB
    private LancamentoBancarioFacade lancamentoBancarioFacade;
    @EJB
    private ChequeFacade chequeFacade;
    @EJB
    private CartaoCreditoFacade cartaoCreditoFacade;
    @EJB
    private CartaoDebitoFacade cartaoDebitoFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<ContasPagar> validaPago(Pessoa fornecedor) {
        Query q = em.createQuery("FROM ContasPagar As a WHERE a.pessoa = :para1 AND a.status = 'PAGO' ORDER BY a.dtPagamento ASC");
        q.setParameter("para1", fornecedor);

        return q.getResultList();
    }

    public void lancarConta(ContasPagar cartaoDebito, BigDecimal valorSaida, Long id) {
        List<ContaBancaria> aux = em.createQuery("FROM ContaBancaria As a WHERE a.id = " + id).getResultList();

        BigDecimal novosaldo = aux.get(0).getSaldo().subtract(valorSaida);
        aux.get(0).setSaldo(novosaldo);

        LancamentoBancario la = new LancamentoBancario();

        la.setContaBancaria(aux.get(0));
        la.setPessoa(cartaoDebito.getPessoa());

        la.setTipo("SAIDA");
        la.setDocumento("CONTA A PAGAR N°" + " " + cartaoDebito.getId());
        la.setDtEntrada(new Date());
        la.setLacamentoEntrada(BigDecimal.ZERO);
        la.setLacamentoSaida(valorSaida);

        lancamentoBancarioFacade.salvar(la);

    }

    public void lancarCartao(ContasPagar venda) {
        CartaoCredito cr = new CartaoCredito();
        cr.setDtmovimento(new Date());
        cr.setDtvencimento(venda.getDtVencimentoFatura());
        cr.setValorSaida(venda.getUltimoPagamento());
        cr.setValor(BigDecimal.ZERO);
        cr.setBandeira(venda.getBandeira());
        cr.setContasPagar(venda);
        cr.setPessoa(venda.getPessoa());
        cr.setSituacao("A DEBITAR");
        cr.setNumeroCheque(venda.getNumDoc());
        cartaoCreditoFacade.salvar(cr);
    }

    public void lancarCheque(ContasPagar venda) {
        Cheque cr = new Cheque();
        cr.setDtmovimento(new Date());
        cr.setDtmovimento(venda.getDtVencimentoFatura());
        cr.setValorSaida(venda.getUltimoPagamento());
        cr.setValor(BigDecimal.ZERO);
        cr.setBanco(venda.getBanco());
        cr.setContasPagar(venda);
        cr.setNumeroCheque(venda.getNumDoc());
        cr.setPessoa(venda.getPessoa());
        cr.setSituacao("A DEBITAR");
        chequeFacade.salvar(cr);
    }

    public void lancarCartaoDebito(ContasPagar venda) {
        CartaoDebito cr = new CartaoDebito();
        cr.setDtmovimento(new Date());
        cr.setValorSaida(venda.getUltimoPagamento());
        cr.setValor(BigDecimal.ZERO);
        cr.setBandeira(venda.getBandeira());
        cr.setContasPagar(venda);
        cr.setPessoa(venda.getPessoa());
        cr.setSituacao("A DEBITAR");
        cr.setNumeroCheque(venda.getNumDoc());
        cartaoDebitoFacade.salvar(cr);
    }

    public void lancarCaixa(ContasPagar venda) {
        List<Caixa> aux = em.createQuery("FROM Caixa As a WHERE a.id = 1").getResultList();

        BigDecimal novosaldo = aux.get(0).getSaldo().subtract(venda.getUltimoPagamento());
        aux.get(0).setSaldo(novosaldo);

        LancamentoCaixa la = new LancamentoCaixa();

        la.setPessoa(venda.getPessoa());
        la.setCaixa(aux.get(0));
        la.setTipo("SAÍDA");
        la.setLacamentoSaida(venda.getUltimoPagamento());
        la.setDtEntrada(venda.getDtPagamento());
        la.setLacamentoEntrada(BigDecimal.ZERO);
        lancamentoCaixaFacade.salvar(la);

    }

    public List<ContasPagar> validaAPagar(Pessoa fornecedor) {
        Query q = em.createQuery("FROM ContasPagar As a WHERE a.pessoa = :para1 AND a.status = 'A PAGAR' OR a.status = 'RESTOS A PAGAR' ORDER BY a.dtVencimento ASC");
        q.setParameter("para1", fornecedor);

        return q.getResultList();
    }

    public List<ContasPagar> contasVencidas(Date aux) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);
        Boolean aux1 = false;
        Query q = em.createQuery("FROM ContasPagar As a WHERE a.dtVencimento <= :para AND a.status = 'A PAGAR'");
        q.setParameter("para", aux);
        return q.getResultList();
    }

    public List<ContasPagar> listarApagar() {
        List<ContasPagar> b = null;

        Query q = em.createQuery("FROM ContasPagar As a WHERE a.status = 'A PAGAR'  OR a.status = 'RESTOS A PAGAR' ORDER BY dtVencimento ASC ");

        return q.getResultList();

    }

    public List<ContasPagar> listarApagarCliente(Pessoa pessoa) {

        Query q = em.createQuery("FROM ContasPagar As a WHERE a.status = 'A PAGAR'  AND a.pessoa = :para OR a.status = 'RESTOS A PAGAR'  AND a.pessoa = :para ORDER BY dtVencimento ASC ");
        q.setParameter("para", pessoa);
        return q.getResultList();

    }

    public List<ContasPagar> listarApagarFilial(Empresa empresa) {

        Query q = em.createQuery("FROM ContasPagar As a WHERE a.status = 'A PAGAR'  AND a.empresa = :para OR a.status = 'RESTOS A PAGAR'  AND a.empresa = :para ORDER BY dtVencimento ASC ");
        q.setParameter("para", empresa);
        return q.getResultList();

    }

    public List<ContasPagar> listarApagarDtVendimento(Date dtVencimento, Date dtVencimento2) {

        Query q = em.createQuery("FROM ContasPagar As a WHERE a.status = 'A PAGAR' AND a.dtVencimento BETWEEN :para AND :para2 OR a.status = 'RESTOS A PAGAR'  AND a.dtVencimento BETWEEN :para AND :para2 ORDER BY dtVencimento ASC ");
        q.setParameter("para", dtVencimento);
        q.setParameter("para2", dtVencimento2);
        return q.getResultList();

    }

    public List<ContasPagar> listarApagarNota(String nota) {

        Query q = em.createQuery("FROM ContasPagar As a WHERE a.status = 'A PAGAR' AND a.compra.numDoc = :para OR a.status = 'RESTOS A PAGAR'  AND a.compra.numDoc = :para ORDER BY dtVencimento ASC ");
        q.setParameter("para", nota);
        return q.getResultList();

    }

    public List<ContasPagar> listarApagar1() {
        Query q = em.createQuery("FROM ContasPagar As a WHERE a.pessoa = null AND a.status = 'A PAGAR' OR a.status = 'RESTOS A PAGAR'");

        return q.getResultList();
    }

    public List<ContasPagar> listarRestoaApagar() {
        Query q = em.createQuery("FROM ContasPagar As a WHERE a.status = 'RESTOS A PAGAR'");

        return q.getResultList();
    }

    public List<ContasPagar> validaPago(Fornecedor fornecedor) {
        Query q = em.createQuery("FROM ContasPagar As a WHERE a.pessoa = :para1 AND a.status = 'PAGO' ORDER BY a.dtVencimento ASC ");
        q.setParameter("para1", fornecedor);

        return q.getResultList();
    }

    public ContasPagarFacade() {
        super(ContasPagar.class);
    }
}

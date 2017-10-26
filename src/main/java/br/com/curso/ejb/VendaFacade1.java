/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.Caixa;
import br.com.curso.entidade.ContasReceber;
import br.com.curso.entidade.LancamentoCaixa;
import br.com.curso.entidade.Pessoa;
import br.com.curso.entidade.Produto;
import br.com.curso.entidade.Venda;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class VendaFacade1 extends AbstractFacade<Venda> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    @EJB
    private ContasReceberFacade contasReceberFacade;
    @EJB
    private ProdutoFacade produtoFacade;
    @EJB
    private LancamentoCaixaFacade lancamentoCaixaFacade;

    @EJB
    private PessoaFacade pessoaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendaFacade1() {
        super(Venda.class);
    }

    public void salvaCont(Venda c, Pessoa p) {

        Integer aux = c.getPessoa().getContF();
        c.getPessoa().setContF(aux + 1);
        pessoaFacade.salvar(p);
    }

    @Override
    public void salvar(Venda entity) {

        if (entity.getVenda().equals(false)) {

            entity = em.merge(entity);
        } else {
            entity = em.merge(entity);

            if (entity.getEntrada().compareTo(BigDecimal.ZERO) == 1) {

                geraContasRecebidas(entity);

            } else {

                geraContasReceber(entity);
            }

        }

    }

    public void extornarProd(Venda venda) {

        for (int i = 0; i < venda.getItensVenda().size(); i++) {

            Produto prod = venda.getItensVenda().get(i).getPlantio();
            BigDecimal novoEstoque = prod.getEstoque().add(venda.getItensVenda().get(i).getQuantidade());
            prod.setEstoque(novoEstoque);
            produtoFacade.salvar(prod);
        }

    }

    public void extornarProdCond(Venda venda) {

        for (int i = 0; i < venda.getItensVenda().size(); i++) {

            Produto prod = venda.getItensVenda().get(i).getPlantio();
            BigDecimal novoEstoque = prod.getEmCondicional().subtract(venda.getItensVenda().get(i).getQuantidade());
            prod.setEmCondicional(novoEstoque);
            produtoFacade.salvar(prod);
        }

    }

    public void baixaProd(Venda venda) {

        for (int i = 0; i < venda.getItensVenda().size(); i++) {

            Produto prod = venda.getItensVenda().get(i).getPlantio();
            BigDecimal novoEstoque = prod.getEstoque().subtract(venda.getItensVenda().get(i).getQuantidade());
            prod.setEstoque(novoEstoque);
            produtoFacade.salvar(prod);
        }

    }

    public void baixaProdCond(Venda venda) {

        for (int i = 0; i < venda.getItensVenda().size(); i++) {

            Produto prod = venda.getItensVenda().get(i).getPlantio();
            BigDecimal novoEstoque = prod.getEmCondicional().add(venda.getItensVenda().get(i).getQuantidade());
            prod.setEmCondicional(novoEstoque);
            produtoFacade.salvar(prod);
        }

    }

    public void lancarCaixa(Venda venda) {
        List<Caixa> aux = em.createQuery("FROM Caixa As a WHERE a.id = 1").getResultList();

        BigDecimal novosaldo = aux.get(0).getSaldo().add(venda.getTotalDesc());
        aux.get(0).setSaldo(novosaldo);

        LancamentoCaixa la = new LancamentoCaixa();

        la.setCaixa(aux.get(0));
        la.setPessoa(venda.getPessoa());
        la.setTipo("ENTRADA");
        la.setDocumento(venda.getTipoDoc());
        la.setLacamentoEntrada(venda.getTotalDesc());
        la.setLacamentoSaida(BigDecimal.ZERO);
        la.setDtEntrada(venda.getDtVenda());

        lancamentoCaixaFacade.salvar(la);
    }

    public void carregaItensOs(Venda os) {
        os.setItensVenda(em.createQuery("FROM ItemVenda AS iop WHERE iop.venda=" + os).getResultList());
        os.setParcelas(em.createQuery("FROM Parcela AS ios WHERE ios.venda=" + os).getResultList());
    }

    private void geraContasRecebidas(Venda venda) {

        ContasReceber cr = new ContasReceber();
        cr.setVenda(venda);
        cr.setDtMovimento(venda.getDtVenda());
        cr.setDtPagamento(cr.getDtMovimento());
        cr.setDtVencimento(cr.getDtPagamento());
        cr.setValor(venda.getEntrada());
        cr.setStatus("RECEBIDO");
        cr.setValorPago(cr.getValor());
        cr.setValorAPagar(cr.getValorPago());
        cr.setPessoa(venda.getPessoa());
        contasReceberFacade.salvar(cr);

    }

    public List<Venda> validaRelatorioVenda(Long aux) {
        Query q = em.createQuery("FROM Venda As a WHERE a.id = :para1 AND a.valida = 'PF'");
        q.setParameter("para1", aux);

        return q.getResultList();
    }

    public List<Venda> validaVendaDia(Date aux) {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");

        String p = dt.format(aux);
        System.out.println(aux);
        Boolean aux1 = false;
        Query q = em.createQuery("FROM Venda As a WHERE a.dtVenda = :para AND a.venda = :para1 order by id desc");
        q.setParameter("para", aux);
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> listaVendas() {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para1 order by id desc");
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> listaOrcamentos() {

        Boolean aux = false;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para order by id desc");
        q.setParameter("para", aux);
        return q.getResultList();
    }

    private void geraContasReceber(Venda venda) {
        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasReceber cr = new ContasReceber();
            cr.setDtMovimento(venda.getDtVenda());
            cr.setDtPagamento(venda.getDtVenda());
            cr.setDtVencimento(venda.getDtVenda());
            cr.setValorPrincipal(venda.getTotalDesc());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setVenda(venda);
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getPessoa());
            cr.setStatus("RECEBIDO");
            contasReceberFacade.salvar(cr);
        } else {
            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasReceber cr = new ContasReceber();
                cr.setDtMovimento(venda.getDtVenda());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDtVenda());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorAPagar(cr.getValor());
                cr.setDescricao(venda.getObs());
                cr.setPessoa(venda.getPessoa());
                cr.setVenda(venda);
                cr.setStatus("A RECEBER");
                contasReceberFacade.salvar(cr);
            }
        }

    }
}

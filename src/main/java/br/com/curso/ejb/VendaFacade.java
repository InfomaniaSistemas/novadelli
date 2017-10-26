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
import br.com.curso.entidade.ContasReceber;
import br.com.curso.entidade.ContasRecebidas;
import br.com.curso.entidade.LancamentoCaixa;
import br.com.curso.entidade.Parcela;
import br.com.curso.entidade.Pessoa;
import br.com.curso.entidade.Produto;
import br.com.curso.entidade.Venda;
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
public class VendaFacade extends AbstractFacade<Venda> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    @EJB
    private ContasReceberFacade contasReceberFacade;
    @EJB
    private ContasRecebidasFacade contasRecebidasFacade;
    @EJB
    private ItemVendaFacade itemVendaFacade;
    @EJB
    private ProdutoFacade produtoFacade;
    @EJB
    private CartaoCreditoFacade cartaoCreditoFacade;
    @EJB
    private CartaoDebitoFacade cartaoDebitoFacade;
    @EJB
    private ChequeFacade chequeFacade;
    @EJB
    private LancamentoCaixaFacade lancamentoCaixaFacade;

    @EJB
    private PessoaFacade pessoaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendaFacade() {
        super(Venda.class);
    }

    public void salvaCont(Venda c, Pessoa p) {

        Integer aux = c.getPessoa().getContF();
        c.getPessoa().setContF(aux + 1);
        pessoaFacade.salvar(p);
    }

    public void salvarNota(Venda entity) {

        entity = em.merge(entity);
    }

    @Override
    public void salvar(Venda entity) {

        if (entity.getVenda().equals(false)) {

            entity = em.merge(entity);

        } else if (entity.getTipoDoc().equals("DINHEIRO")) {

            entity = em.merge(entity);

            geraContasReceber(entity);

        } else if (entity.getTipoDoc().equals("CHEQUE")) {
            entity = em.merge(entity);
            geraCheque(entity);
            if (entity.getEntrada().compareTo(BigDecimal.ZERO) == 1) {

                geraContasRecebidas(entity);

            }

        } else if (entity.getTipoDoc().equals("CARTÃO CRÉDITO")) {
            entity = em.merge(entity);
            geraCartaoCredito(entity);

            if (entity.getEntrada().compareTo(BigDecimal.ZERO) == 1) {

                geraContasRecebidas(entity);

            }
            //CONTROLE CARTÃO CREDITO
        } else if (entity.getTipoDoc().equals("CARTÃO DÉBITO")) {
            entity = em.merge(entity);
            geraCartaoDebito(entity);
            if (entity.getEntrada().compareTo(BigDecimal.ZERO) == 1) {

                geraContasRecebidas(entity);

            }
            //CONTROLE CARTÃO DÉBITO

        } else if (entity.getTipoDoc().equals("CREDIÁRIO")) {
            entity = em.merge(entity);
            geraContasReceber(entity);
            if (entity.getEntrada().compareTo(BigDecimal.ZERO) == 1) {

                geraContasRecebidas(entity);

            }
            //CONTROLE CARTÃO DÉBITO

        }
    }

    public List<Venda> validaVendaDias(Date dtTeste) {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.dtVenda = :para AND a.venda = :para1 order by id desc");
        q.setParameter("para", dtTeste);
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> validaVendaDiasPessoa(Pessoa aux) {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.pessoa = :para AND a.venda = :para1 order by id desc");
        q.setParameter("para", aux);
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> validaVendaCod(Long aux) {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.id = :para AND a.venda = :para1 order by id desc");
        q.setParameter("para", aux);
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public void extornarProd(Venda venda) {

        for (int i = 0; i < venda.getItensVenda().size(); i++) {

            Produto prod = venda.getItensVenda().get(i).getPlantio();
            BigDecimal novoEstoque = prod.getEstoque().add(venda.getItensVenda().get(i).getQuantidade());
            prod.setEstoque(novoEstoque);
            produtoFacade.salvar(prod);
        }

    }

    public void validaIdItem(Venda venda) {

        System.out.println("Aqui Entrou");
        for (int i = 0; i < venda.getItensVenda().size(); i++) {
            if (venda.getItensVenda().get(i).getId() == null) {
                Long id3 = itemVendaFacade.listarTodos().get(0).getId();
                Long n = (long) id3 + i;
                System.out.println("Aqui Conta");
                venda.getItensVenda().get(i).setId(n);
                itemVendaFacade.salvar(venda.getItensVenda().get(i));
            }
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

    public void lancarCaixa(Venda venda, BigDecimal valor) {
        List<Caixa> aux = em.createQuery("FROM Caixa As a WHERE a.id = 1").getResultList();

        BigDecimal novosaldo = aux.get(0).getSaldo().add(valor);
        aux.get(0).setSaldo(novosaldo);

        LancamentoCaixa la = new LancamentoCaixa();

        la.setCaixa(aux.get(0));
        la.setPessoa(venda.getPessoa());
        la.setTipo("ENTRADA");
        la.setDocumento("DINHEIRO");
        la.setLacamentoEntrada(valor);
        la.setLacamentoSaida(BigDecimal.ZERO);
        la.setDtEntrada(venda.getDtVenda());

        lancamentoCaixaFacade.salvar(la);
    }

    public void carregaItensOs(Venda os) {
        os.setItensVenda(em.createQuery("FROM ItemVenda AS iop WHERE iop.venda=" + os).getResultList());
        os.setParcelas(em.createQuery("FROM Parcela AS ios WHERE ios.venda=" + os).getResultList());
    }

    public void carregaVenda(Venda os) {
        os.setItensVenda(em.createQuery("FROM ItemVenda AS iop WHERE iop.venda=" + os).getResultList());
        os.setParcelas(em.createQuery("FROM Parcela AS ios WHERE ios.venda=" + os).getResultList());
    }

    private void geraContasRecebidas(Venda venda) {

        ContasRecebidas cr = new ContasRecebidas();
        Long id1 = contasRecebidasFacade.listarTodos().get(0).getId();
        cr.setId(id1 + 1);
        cr.setVenda(venda);
        cr.setDtMovimento(venda.getDtVenda());
        cr.setDtPagamento(cr.getDtMovimento());
        cr.setDtVencimento(cr.getDtPagamento());
        cr.setValor(venda.getEntrada());
        cr.setRestosAPagar(BigDecimal.ZERO);
        cr.setStatus("RECEBIDO");
        lancarCaixa(venda, venda.getEntrada());
        cr.setValorPago(cr.getValor());
        cr.setValorAPagar(cr.getValorPago());
        cr.setValorPrincipal(cr.getValorPago());
        cr.setPessoa(venda.getPessoa());
        contasRecebidasFacade.salvar(cr);

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
        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.dtVenda = :para AND a.venda = :para1 order by id desc");
        q.setParameter("para", aux);
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> validaVenda() {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para1 order by id desc");
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> validaVendaFiscal() {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para1  AND a.xml is not null AND a.modeloNota = '65' order by id desc");
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> validaVendaFiscalRetransmissao() {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para1  AND a.naoTransmitida = :para1 AND a.modeloNota = '65' order by id desc");
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> validaVendaFiscalRetransmissao55() {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para1  AND a.naoTransmitida = :para1 AND a.modeloNota = '55' order by id desc");
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> validaVendaFiscalEmissao() {

        Boolean aux1 = false;
        Boolean aux2 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para2  AND a.fiscal = :para1 order by id desc");
        q.setParameter("para1", aux1);
        q.setParameter("para2", aux2);
        return q.getResultList();
    }

    public List<Venda> listaVendas() {

        Boolean aux1 = true;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para1 order by id desc");
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Parcela> listaParcela(Venda aux1) {

        Query q = em.createQuery("FROM Parcela As a WHERE a.venda = :para1");
        q.setParameter("para1", aux1);
        return q.getResultList();
    }

    public List<Venda> listaOrcamentos() {

        Boolean aux = false;
        Query q = em.createQuery("FROM Venda As a WHERE a.venda = :para order by id desc");
        q.setParameter("para", aux);
        return q.getResultList();
    }

    private void geraCheque(Venda venda) {

        if (venda.getTipoPagamento().equals("A PRAZO")) {

            for (int i = 0; i < venda.getQtdParcela(); i++) {

                Cheque cr = new Cheque();
                cr.setDtvencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtmovimento(new Date());
                cr.setBanco(venda.getBanco());
                cr.setValor(venda.getParcelas().get(i).getValor());
                cr.setValorSaida(BigDecimal.ZERO);
                cr.setPessoa(venda.getPessoa());
                cr.setVenda(venda);
                cr.setSituacao("A DEBITAR");
                cr.setNumeroCheque(venda.getParcelas().get(i).getDoc());
                chequeFacade.salvar(cr);

            }
        } else {
            Cheque cr = new Cheque();
            cr.setDtmovimento(venda.getDtVenda());
            cr.setValor(venda.getTotalDescEn());
            cr.setBanco(venda.getBanco());
            cr.setVenda(venda);
            cr.setValorSaida(BigDecimal.ZERO);
            cr.setPessoa(venda.getPessoa());
            cr.setSituacao("A DEBITAR");
            cr.setNumeroCheque(venda.getNumeroDoc());
            chequeFacade.salvar(cr);

        }

        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasRecebidas cr = new ContasRecebidas();
            Long id1 = contasRecebidasFacade.listarTodosCompleto().get(0).getId();
            cr.setId(id1 + 1);
            cr.setDtMovimento(venda.getDtVenda());
            cr.setDtPagamento(venda.getDtVenda());
            cr.setDtVencimento(venda.getDtVenda());
            cr.setValorPrincipal(venda.getTotalDescEn());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setValorPago(venda.getTotalDescEn());
            cr.setVenda(venda);
            cr.setTipoDocumento("CHEQUE");
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getPessoa());
            cr.setStatus("RECEBIDO");
            contasRecebidasFacade.salvar(cr);

        } else {

            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasReceber cr = new ContasReceber();
                Long id1 = contasReceberFacade.listarTodosCompleto().get(0).getId();
                cr.setId(id1 + 1);
                cr.setDtMovimento(venda.getDtVenda());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDtVenda());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorAPagar(cr.getValor());
                cr.setValorPago(venda.getParcelas().get(i).getValor());
                cr.setDescricao(venda.getObs());
                cr.setPessoa(venda.getPessoa());
                cr.setTipoDocumento("CHEQUE");
                cr.setNumeroDoc(venda.getParcelas().get(i).getDoc());
                cr.setVenda(venda);
                cr.setStatus("RECEBIDO");
                contasReceberFacade.salvar(cr);
            }
        }

    }

    private void geraCartaoCredito(Venda venda) {

        if (venda.getTipoPagamento().equals("A PRAZO")) {

            for (int i = 0; i < venda.getQtdParcela(); i++) {

                CartaoCredito cr = new CartaoCredito();
                cr.setDtvencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtmovimento(new Date());
                cr.setBandeira(venda.getBandeira());
                cr.setValor(venda.getParcelas().get(i).getValor());
                cr.setValorSaida(BigDecimal.ZERO);
                cr.setVenda(venda);
                cr.setPessoa(venda.getPessoa());
                cr.setSituacao("A DEBITAR");
                cr.setNumeroCheque(venda.getParcelas().get(i).getDoc());
                cartaoCreditoFacade.salvar(cr);

            }
        } else {
            CartaoCredito cr = new CartaoCredito();
            cr.setDtmovimento(venda.getDtVenda());
            cr.setBandeira(venda.getBandeira());
            cr.setValor(venda.getTotalDescEn());
            cr.setVenda(venda);
            cr.setValorSaida(BigDecimal.ZERO);
            cr.setPessoa(venda.getPessoa());
            cr.setSituacao("A DEBITAR");
            cr.setNumeroCheque(venda.getNumeroDoc());
            cartaoCreditoFacade.salvar(cr);

        }

        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasRecebidas cr = new ContasRecebidas();
            Long id1 = contasRecebidasFacade.listarTodos().get(0).getId();
            cr.setId(id1 + 1);
            cr.setDtMovimento(venda.getDtVenda());
            cr.setDtPagamento(venda.getDtVenda());
            cr.setDtVencimento(venda.getDtVenda());
            cr.setValorPrincipal(venda.getTotalDescEn());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setVenda(venda);
            cr.setTipoDocumento("CARTÃO CRÉDITO");
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getPessoa());
            cr.setStatus("RECEBIDO");
            contasRecebidasFacade.salvar(cr);

        } else {

            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasReceber cr = new ContasReceber();
                Long id1 = contasReceberFacade.listarTodos().get(0).getId();
                cr.setId(id1 + 1);
                cr.setDtMovimento(venda.getDtVenda());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDtVenda());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorAPagar(cr.getValor());
                cr.setValorPago(venda.getParcelas().get(i).getValor());
                cr.setDescricao(venda.getObs());
                cr.setPessoa(venda.getPessoa());
                cr.setTipoDocumento("CARTÃO CRÉDITO");
                cr.setNumeroDoc(venda.getParcelas().get(i).getDoc());
                cr.setVenda(venda);
                cr.setStatus("RECEBIDO");
                contasReceberFacade.salvar(cr);
            }
        }

    }

    private void geraCartaoDebito(Venda venda) {

        if (venda.getTipoPagamento().equals("A PRAZO")) {

            for (int i = 0; i < venda.getQtdParcela(); i++) {

                CartaoDebito cr = new CartaoDebito();
                cr.setDtvencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtmovimento(new Date());
                cr.setValor(venda.getParcelas().get(i).getValor());
                cr.setValorSaida(BigDecimal.ZERO);
                cr.setVenda(venda);
                cr.setPessoa(venda.getPessoa());
                cr.setSituacao("A DEBITAR");
                cr.setNumeroCheque(venda.getParcelas().get(i).getDoc());
                cartaoDebitoFacade.salvar(cr);

            }
        } else {
            CartaoDebito cr = new CartaoDebito();
            cr.setDtmovimento(venda.getDtVenda());

            cr.setValor(venda.getTotalDescEn());
            cr.setPessoa(venda.getPessoa());
            cr.setBandeira(venda.getBandeira());
            cr.setValorSaida(BigDecimal.ZERO);
            cr.setVenda(venda);
            cr.setSituacao("A DEBITAR");
            cr.setNumeroCheque(venda.getNumeroDoc());
            cartaoDebitoFacade.salvar(cr);

        }

        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasRecebidas cr = new ContasRecebidas();
            Long id1 = contasRecebidasFacade.listarTodos().get(0).getId();
            cr.setId(id1 + 1);
            cr.setDtMovimento(venda.getDtVenda());
            cr.setDtPagamento(venda.getDtVenda());
            cr.setDtVencimento(venda.getDtVenda());
            cr.setValorPrincipal(venda.getTotalDescEn());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setVenda(venda);
            cr.setTipoDocumento("CARTÃO DÉBITO");
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getPessoa());
            cr.setStatus("RECEBIDO");
            contasRecebidasFacade.salvar(cr);

        } else {

            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasReceber cr = new ContasReceber();
                Long id1 = contasReceberFacade.listarTodos().get(0).getId();
                cr.setId(id1 + 1);
                cr.setDtMovimento(venda.getDtVenda());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDtVenda());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorAPagar(cr.getValor());
                cr.setDescricao(venda.getObs());
                cr.setPessoa(venda.getPessoa());
                cr.setTipoDocumento("CARTÃO DÉBITO");
                cr.setNumeroDoc(venda.getParcelas().get(i).getDoc());
                cr.setVenda(venda);
                cr.setStatus("RECEBIDO");
                contasReceberFacade.salvar(cr);
            }
        }

    }

    private void geraContasReceber(Venda venda) {
        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasRecebidas cr = new ContasRecebidas();

            Long id1 = contasRecebidasFacade.listarTodos().get(0).getId();
            cr.setId(id1 + 1);
            cr.setDtMovimento(venda.getDtVenda());
            cr.setDtPagamento(venda.getDtVenda());
            cr.setDtVencimento(venda.getDtVenda());
            cr.setValorPrincipal(venda.getTotalDescEn());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setRestosAPagar(BigDecimal.ZERO);
            cr.setValorPago(venda.getTotalDescEn());
            cr.setVenda(venda);
            lancarCaixa(venda, venda.getTotalDescEn());
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getPessoa());
            cr.setStatus("RECEBIDO");
            contasRecebidasFacade.salvar(cr);
        } else {
            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasReceber cr = new ContasReceber();
                Long id1 = contasReceberFacade.listarTodos().get(0).getId();
                cr.setId(id1 + 1);
                cr.setDtMovimento(venda.getDtVenda());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDtVenda());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorPago(BigDecimal.ZERO);
                cr.setRestosAPagar(BigDecimal.ZERO);
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
